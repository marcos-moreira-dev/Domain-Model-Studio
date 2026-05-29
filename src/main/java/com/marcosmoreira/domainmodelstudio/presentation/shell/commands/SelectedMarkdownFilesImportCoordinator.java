package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportItemResult;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportItemStatus;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportResult;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelResult;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportedProjectVisualPreparationUseCase;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/** Coordina la importación de uno o varios archivos Markdown seleccionados directamente. */
final class SelectedMarkdownFilesImportCoordinator {

    private final ShellCommandContext context;
    private final ProjectTabOpener projectTabOpener;
    private final ImportedProjectVisualPreparationUseCase visualPreparationUseCase;
    private final MarkdownFolderImportReportFormatter reportFormatter = new MarkdownFolderImportReportFormatter();

    SelectedMarkdownFilesImportCoordinator(
            ShellCommandContext context,
            ProjectTabOpener projectTabOpener,
            ImportedProjectVisualPreparationUseCase visualPreparationUseCase
    ) {
        this.context = Objects.requireNonNull(context, "context");
        this.projectTabOpener = Objects.requireNonNull(projectTabOpener, "projectTabOpener");
        this.visualPreparationUseCase = Objects.requireNonNull(visualPreparationUseCase, "visualPreparationUseCase");
    }

    void requestImportMarkdownFiles() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Importar uno o varios Markdown estructurados");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Modelo Markdown (*.md)", "*.md", "*.markdown"));
        List<File> selectedFiles = chooser.showOpenMultipleDialog(null);
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            context.shellState().updateStatus("Importación Markdown cancelada.");
            return;
        }
        importMarkdownFiles(selectedFiles.stream().map(File::toPath).toList());
    }

    void importMarkdownFiles(List<Path> markdownFiles) {
        List<Path> files = markdownFiles == null ? List.of() : markdownFiles.stream()
                .filter(Objects::nonNull)
                .toList();
        if (files.isEmpty()) {
            context.shellState().updateStatus("Importación Markdown cancelada: no se seleccionaron archivos.");
            return;
        }
        MarkdownFolderImportProgressDialog progressDialog = MarkdownFolderImportProgressDialog.forFiles(files);
        Task<MarkdownBatchImportResult> task = importTask(files);
        progressDialog.bind(task);
        PauseTransition delayedProgress = delayedProgress(progressDialog, task);
        configureTaskCallbacks(task, progressDialog, delayedProgress);
        delayedProgress.play();
        Thread worker = new Thread(task, "dms-markdown-file-import");
        worker.setDaemon(true);
        worker.start();
    }

    private PauseTransition delayedProgress(MarkdownFolderImportProgressDialog progressDialog, Task<?> task) {
        PauseTransition delay = new PauseTransition(Duration.millis(450));
        delay.setOnFinished(event -> {
            if (task.isRunning()) {
                progressDialog.show();
            }
        });
        return delay;
    }

    private Task<MarkdownBatchImportResult> importTask(List<Path> files) {
        return new Task<>() {
            @Override
            protected MarkdownBatchImportResult call() {
                updateMessage("Importando archivos Markdown seleccionados...");
                return importSelectedFiles(files);
            }
        };
    }

    private MarkdownBatchImportResult importSelectedFiles(List<Path> files) {
        Path sourceRoot = commonRoot(files);
        List<MarkdownBatchImportItemResult> items = new ArrayList<>();
        for (int index = 0; index < files.size(); index++) {
            ensureNotCancelled();
            Path file = files.get(index);
            items.add(importOne(file));
        }
        int imported = (int) items.stream().filter(MarkdownBatchImportItemResult::imported).count();
        int skipped = (int) items.stream().filter(MarkdownBatchImportItemResult::skipped).count();
        int rejected = (int) items.stream().filter(MarkdownBatchImportItemResult::rejected).count();
        return new MarkdownBatchImportResult(sourceRoot, files.size(), files.size(), imported, skipped, rejected, items, List.of());
    }

    private void ensureNotCancelled() {
        if (Thread.currentThread().isInterrupted()) {
            throw new CancellationException("Importación Markdown cancelada por el usuario.");
        }
    }

    private MarkdownBatchImportItemResult importOne(Path file) {
        if (!isMarkdown(file)) {
            return result(file, Optional.empty(), MarkdownBatchImportItemStatus.SKIPPED_NOT_MARKDOWN,
                    Optional.empty(), List.of(), Optional.empty());
        }
        try {
            ImportMarkdownModelResult imported = context.applicationServices().importServices()
                    .importMarkdownModelUseCase()
                    .importFile(file);
            List<String> warnings = validationMessages(imported);
            return result(file,
                    Optional.of(imported.project().metadata().diagramTypeId()),
                    importedStatus(imported),
                    Optional.of(imported),
                    warnings,
                    Optional.empty());
        } catch (IOException exception) {
            return result(file, Optional.empty(), MarkdownBatchImportItemStatus.REJECTED_IO_ERROR,
                    Optional.empty(), List.of(), Optional.ofNullable(exception.getMessage()));
        } catch (MarkdownModelParsingException | IllegalArgumentException exception) {
            return result(file, Optional.empty(), MarkdownBatchImportItemStatus.REJECTED_PARSE_ERROR,
                    Optional.empty(), List.of(), Optional.ofNullable(exception.getMessage()));
        }
    }

    private List<String> validationMessages(ImportMarkdownModelResult result) {
        List<String> messages = new ArrayList<>();
        messages.addAll(result.validationResult().warnings().stream().map(issue -> issue.message()).toList());
        messages.addAll(result.validationResult().errors().stream().map(issue -> issue.message()).toList());
        return messages;
    }

    private MarkdownBatchImportItemStatus importedStatus(ImportMarkdownModelResult result) {
        if (result.validationResult().hasErrors()) {
            return MarkdownBatchImportItemStatus.IMPORTED_WITH_VALIDATION_ERRORS;
        }
        if (result.validationResult().hasWarnings()) {
            return MarkdownBatchImportItemStatus.IMPORTED_WITH_WARNINGS;
        }
        return MarkdownBatchImportItemStatus.IMPORTED;
    }

    private MarkdownBatchImportItemResult result(
            Path file,
            Optional<DiagramTypeId> declaredType,
            MarkdownBatchImportItemStatus status,
            Optional<ImportMarkdownModelResult> importResult,
            List<String> warnings,
            Optional<String> error
    ) {
        return new MarkdownBatchImportItemResult(file, file.getFileName().toString(), declaredType, Optional.empty(),
                status, importResult, warnings, error);
    }

    private boolean isMarkdown(Path file) {
        String name = file.getFileName() == null ? file.toString() : file.getFileName().toString().toLowerCase();
        return name.endsWith(".md") || name.endsWith(".markdown");
    }

    private Path commonRoot(List<Path> files) {
        Path root = files.get(0).toAbsolutePath().normalize().getParent();
        if (root == null) {
            return files.get(0).toAbsolutePath().normalize();
        }
        for (Path file : files) {
            Path parent = file.toAbsolutePath().normalize().getParent();
            while (parent != null && root != null && !parent.startsWith(root)) {
                root = root.getParent();
            }
        }
        return root == null ? files.get(0).toAbsolutePath().normalize() : root;
    }

    private void configureTaskCallbacks(
            Task<MarkdownBatchImportResult> task,
            MarkdownFolderImportProgressDialog progressDialog,
            PauseTransition delayedProgress
    ) {
        task.setOnSucceeded(event -> {
            delayedProgress.stop();
            progressDialog.close();
            openImportedProjectsGradually(task.getValue());
        });
        task.setOnCancelled(event -> {
            delayedProgress.stop();
            progressDialog.close();
            context.shellState().updateStatus("Importación Markdown cancelada por el usuario.");
        });
        task.setOnFailed(event -> {
            delayedProgress.stop();
            progressDialog.close();
            context.shellState().updateStatus("No se pudieron importar los Markdown seleccionados.");
        });
    }

    private void openImportedProjectsGradually(MarkdownBatchImportResult result) {
        MarkdownImportedProjectBatchOpener opener = new MarkdownImportedProjectBatchOpener(
                projectTabOpener,
                visualPreparationUseCase,
                context.shellState()::updateStatus);
        opener.open(result, openedCount -> {
            updateStatus(result, openedCount);
            showSummary(result);
        });
    }

    private void updateStatus(MarkdownBatchImportResult result, int openedCount) {
        context.shellState().updateStatus("Markdown seleccionados importados: "
                + openedCount + " pestañas abiertas, "
                + result.rejectedCount() + " rechazados.");
    }

    private void showSummary(MarkdownBatchImportResult result) {
        boolean hasProblemFiles = reportFormatter.hasCorrectionCandidates(result);
        String detail = hasProblemFiles
                ? reportFormatter.buildCorrectionReport(result)
                : reportFormatter.buildDetailedReport(result);
        MarkdownFolderImportResultDialog dialog = new MarkdownFolderImportResultDialog(
                reportFormatter.buildSelectedFilesSummary(result), detail, hasProblemFiles);
        dialog.showDeferred();
    }
}
