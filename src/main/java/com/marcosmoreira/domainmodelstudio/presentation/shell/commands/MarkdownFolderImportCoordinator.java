package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.application.canonization.CanonizationArtifactRole;
import com.marcosmoreira.domainmodelstudio.application.canonization.CanonizationFlowReport;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportPolicy;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportRequest;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportResult;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportedProjectVisualPreparationUseCase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;

/** Coordina la UI para abrir una carpeta raíz de proyectos Markdown compatibles. */
public final class MarkdownFolderImportCoordinator {

    private final ShellCommandContext context;
    private final ProjectTabOpener projectTabOpener;
    private final ImportedProjectVisualPreparationUseCase visualPreparationUseCase;
    private final MarkdownFolderImportReportFormatter reportFormatter = new MarkdownFolderImportReportFormatter();

    public MarkdownFolderImportCoordinator(
            ShellCommandContext context,
            ProjectTabOpener projectTabOpener,
            ImportedProjectVisualPreparationUseCase visualPreparationUseCase
    ) {
        this.context = Objects.requireNonNull(context, "context");
        this.projectTabOpener = Objects.requireNonNull(projectTabOpener, "projectTabOpener");
        this.visualPreparationUseCase = Objects.requireNonNull(visualPreparationUseCase, "visualPreparationUseCase");
    }

    public void requestImportMarkdownFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Abrir carpeta Markdown recursiva");
        File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory == null) {
            context.shellState().updateStatus("Importación de carpeta Markdown cancelada.");
            return;
        }
        importMarkdownFolder(selectedDirectory.toPath());
    }

    public void importMarkdownFolder(Path sourceRoot) {
        Objects.requireNonNull(sourceRoot, "sourceRoot");
        if (!confirmImport(sourceRoot)) {
            context.shellState().updateStatus("Importación de carpeta Markdown cancelada.");
            return;
        }
        MarkdownFolderImportProgressDialog progressDialog = MarkdownFolderImportProgressDialog.forImport(sourceRoot);
        Task<MarkdownBatchImportResult> task = importTask(sourceRoot);
        progressDialog.bind(task);
        PauseTransition delayedProgress = delayedProgress(progressDialog, task);
        configureTaskCallbacks(task, progressDialog, delayedProgress);
        delayedProgress.play();
        Thread worker = new Thread(task, "dms-markdown-folder-import");
        worker.setDaemon(true);
        worker.start();
    }

    private boolean confirmImport(Path sourceRoot) {
        return MarkdownFolderImportConfirmationDialog.forFolder(sourceRoot)
                .showAndWait()
                .orElse(false);
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

    private Task<MarkdownBatchImportResult> importTask(Path sourceRoot) {
        return new Task<>() {
            @Override
            protected MarkdownBatchImportResult call() throws Exception {
                updateMessage("Leyendo carpeta Markdown de forma recursiva...");
                return context.applicationServices().importServices().markdownBatchImportUseCase()
                        .importFolder(new MarkdownBatchImportRequest(sourceRoot, MarkdownBatchImportPolicy.recursiveProductionPolicy()));
            }
        };
    }

    private void configureTaskCallbacks(
            Task<MarkdownBatchImportResult> task,
            MarkdownFolderImportProgressDialog progressDialog,
            PauseTransition delayedProgress
    ) {
        task.setOnSucceeded(event -> {
            delayedProgress.stop();
            progressDialog.close();
            MarkdownBatchImportResult result = task.getValue();
            openImportedProjectsGradually(result);
        });
        task.setOnCancelled(event -> {
            delayedProgress.stop();
            progressDialog.close();
            context.shellState().updateStatus("Importación de carpeta Markdown cancelada por el usuario.");
        });
        task.setOnFailed(event -> {
            delayedProgress.stop();
            progressDialog.close();
            Throwable exception = task.getException();
            if (exception instanceof CancellationException) {
                context.shellState().updateStatus("Importación de carpeta Markdown cancelada.");
                return;
            }
            String message = exception == null ? "Error no especificado." : exception.getMessage();
            context.shellState().updateStatus("No se pudo abrir la carpeta Markdown: " + message);
            showError("No se pudo abrir la carpeta Markdown", message);
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
        context.shellState().updateStatus("Carpeta Markdown recursiva importada: "
                + openedCount + " pestañas abiertas, "
                + result.skippedCount() + " omitidos, "
                + result.rejectedCount() + " rechazados.");
    }

    private void showSummary(MarkdownBatchImportResult result) {
        StringBuilder summary = new StringBuilder(reportFormatter.buildSummary(result));
        appendCanonizationSection(summary, result);
        boolean hasProblemFiles = reportFormatter.hasCorrectionCandidates(result);
        String detail = hasProblemFiles
                ? reportFormatter.buildCorrectionReport(result)
                : reportFormatter.buildDetailedReport(result);
        MarkdownFolderImportResultDialog dialog = new MarkdownFolderImportResultDialog(summary.toString(),
                detail,
                hasProblemFiles);
        dialog.showDeferred();
    }

    private void appendCanonizationSection(StringBuilder summary, MarkdownBatchImportResult result) {
        CanonizationFlowReport report = context.applicationServices().documentationServices()
                .canonizationFlowReportUseCase()
                .from(result);
        summary.append("\n\nCanonización documental:")
                .append("\n- Estado: ").append(report.readiness().displayName())
                .append("\n- Levantamientos lógicos: ").append(report.logicalBusinessIntakeCount())
                .append("\n- Artefactos de datos: ").append(report.countByRole(CanonizationArtifactRole.DATA_MODEL))
                .append("\n- Vistas lógicas: ").append(report.countByRole(CanonizationArtifactRole.LOGICAL_VIEW))
                .append("\n- Arquitectura: ").append(report.countByRole(CanonizationArtifactRole.ARCHITECTURE_VIEW))
                .append("\n- Procesos/comportamiento: ").append(report.countByRole(CanonizationArtifactRole.BEHAVIOR_VIEW))
                .append("\n- Administrativos: ").append(report.countByRole(CanonizationArtifactRole.ADMINISTRATIVE_VIEW));
        if (!report.recommendations().isEmpty()) {
            summary.append("\nRecomendaciones:");
            report.recommendations().stream()
                    .limit(4)
                    .forEach(recommendation -> summary.append("\n- ").append(recommendation));
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message == null ? "Error no especificado." : message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.showAndWait();
    }
}
