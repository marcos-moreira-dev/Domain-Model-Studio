package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.application.export.ExportTargetPathPolicy;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassExportSafetyPolicy;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportableOutput;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.SvgExportContract;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/** Comandos de exportación directa de la salida activa. */
public final class ExportCommandHandler {

    private final ShellCommandContext context;
    private final UmlClassExportSafetyPolicy umlClassExportSafetyPolicy = new UmlClassExportSafetyPolicy();
    private final ExportTargetPathPolicy targetPathPolicy = new ExportTargetPathPolicy();

    public ExportCommandHandler(ShellCommandContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    public void requestExportSvg() {
        Optional<ExportableOutput> activeOutput = activeOutputFor(ExportFormat.SVG);
        if (activeOutput.isEmpty()) {
            return;
        }
        ExportableOutput output = activeOutput.get();
        DiagramProject project = output.project().orElse(null);
        DiagramProject visualProject = output.visualProject().orElse(project);
        if (project == null || visualProject == null) {
            context.shellState().updateStatus(SvgExportContract.NOT_AVAILABLE_STATUS);
            return;
        }
        java.io.File file = ExportFileChooserFactory.showSaveDialog(
                SvgExportContract.DIALOG_TITLE,
                ExportFormat.SVG,
                output.descriptor().suggestedFileName(ExportFormat.SVG));
        if (file == null) {
            context.shellState().updateStatus(SvgExportContract.CANCELED_STATUS);
            return;
        }
        try {
            umlClassExportSafetyPolicy.ensureSvgExportAllowed(visualProject);
            java.nio.file.Path exported = context.applicationServices().exportServices().exportSvgUseCase().export(visualProject, file.toPath());
            context.shellState().updateStatus(SvgExportContract.SUCCESS_PREFIX + exported.toAbsolutePath());
        } catch (IOException | IllegalArgumentException exception) {
            context.shellState().updateStatus(SvgExportContract.FAILURE_PREFIX + exception.getMessage());
        } catch (OutOfMemoryError error) {
            String message = "Memoria insuficiente al exportar SVG. Reduce la vista activa, usa Resumen o aplica filtros antes de exportar.";
            context.shellState().updateStatus(message);
            new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
        }
    }

    public void requestExportMarkdown() {
        Optional<ExportableOutput> activeOutput = activeOutputFor(ExportFormat.MARKDOWN);
        if (activeOutput.isEmpty()) {
            return;
        }
        ExportableOutput output = activeOutput.get();
        java.io.File file = ExportFileChooserFactory.showSaveDialog(
                "Exportar Markdown actualizado",
                ExportFormat.MARKDOWN,
                output.descriptor().suggestedFileStem() + "_actualizado.md");
        if (file == null) {
            context.shellState().updateStatus("Exportación Markdown cancelada.");
            return;
        }
        try {
            java.nio.file.Path exported;
            if (output.dataDictionaryDocument().isPresent()) {
                exported = context.applicationServices().exportServices().exportDataDictionaryMarkdownUseCase()
                        .export(output.dataDictionaryDocument().orElseThrow(), file.toPath());
            } else {
                DiagramProject project = output.project()
                        .orElseThrow(() -> new IllegalStateException("La salida activa no tiene Markdown exportable."));
                exported = context.applicationServices().exportServices().exportMarkdownUseCase().export(project, file.toPath());
            }
            context.shellState().updateStatus("Markdown actualizado exportado en: " + exported.toAbsolutePath());
        } catch (IOException | IllegalArgumentException | IllegalStateException exception) {
            context.shellState().updateStatus("No se pudo exportar Markdown: " + exception.getMessage());
        }
    }

    public void requestExportPng() {
        Optional<ExportableOutput> activeOutput = activeOutputFor(ExportFormat.PNG);
        if (activeOutput.isEmpty()) {
            return;
        }
        ExportableOutput output = activeOutput.get();
        java.io.File file = ExportFileChooserFactory.showSaveDialog(
                "Exportar diagrama PNG",
                ExportFormat.PNG,
                output.descriptor().suggestedFileName(ExportFormat.PNG));
        if (file == null) {
            context.shellState().updateStatus("Exportación PNG cancelada.");
            return;
        }
        try {
            java.nio.file.Path targetFile = targetPathPolicy.ensurePngExtension(file.toPath());
            output.pngAction()
                    .orElseThrow(() -> new IllegalStateException("La salida activa no tiene PNG exportable."))
                    .export(targetFile);
            context.shellState().updateStatus("PNG exportado en: " + targetFile.toAbsolutePath());
        } catch (IOException | IllegalStateException exception) {
            context.shellState().updateStatus("No se pudo exportar PNG: " + exception.getMessage());
        } catch (OutOfMemoryError error) {
            String message = "Memoria insuficiente al exportar PNG. La imagen probablemente es demasiado grande; prueba SVG o reduce el contenido visible.";
            context.shellState().updateStatus(message);
            new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
        }
    }

    public void requestExportPdf() {
        Optional<ExportableOutput> activeOutput = activeOutputFor(ExportFormat.PDF);
        if (activeOutput.isEmpty()) {
            return;
        }
        ExportableOutput output = activeOutput.get();
        java.io.File file = ExportFileChooserFactory.showSaveDialog(
                "Exportar documento PDF",
                ExportFormat.PDF,
                output.descriptor().suggestedFileName(ExportFormat.PDF));
        if (file == null) {
            context.shellState().updateStatus("Exportación PDF cancelada.");
            return;
        }
        try {
            DiagramProject project = output.project()
                    .orElseThrow(() -> new IllegalStateException("La salida activa no tiene proyecto PDF exportable."));
            java.nio.file.Path exported = context.applicationServices().exportServices().exportPdfUseCase()
                    .export(project, file.toPath());
            context.shellState().updateStatus("PDF exportado en: " + exported.toAbsolutePath());
        } catch (IOException | IllegalArgumentException | IllegalStateException exception) {
            context.shellState().updateStatus("No se pudo exportar PDF: " + exception.getMessage());
        }
    }

    private Optional<ExportableOutput> activeOutputFor(ExportFormat format) {
        Optional<ExportableOutput> output = context.activeOutputProvider().activeOutput();
        if (output.isEmpty()) {
            context.shellState().updateStatus("No hay salida activa para exportar " + format.displayName() + ".");
            return Optional.empty();
        }
        if (!output.get().supports(format)) {
            context.shellState().updateStatus("La salida activa no ofrece exportación " + format.displayName() + ".");
            return Optional.empty();
        }
        return output;
    }
}
