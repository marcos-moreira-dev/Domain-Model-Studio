package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;
import java.util.Optional;

/** Salida final activa junto con los datos mínimos necesarios para exportarla. */
public final class ExportableOutput {

    private final ExportableOutputDescriptor descriptor;
    private final DiagramProject project;
    private final DiagramProject visualProject;
    private final DataDictionaryDocument dataDictionaryDocument;
    private final ExportPngAction pngAction;

    private ExportableOutput(
            ExportableOutputDescriptor descriptor,
            DiagramProject project,
            DiagramProject visualProject,
            DataDictionaryDocument dataDictionaryDocument,
            ExportPngAction pngAction
    ) {
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor");
        this.project = project;
        this.visualProject = visualProject == null ? project : visualProject;
        this.dataDictionaryDocument = dataDictionaryDocument;
        this.pngAction = pngAction;
    }

    public static ExportableOutput visual(
            ExportableOutputDescriptor descriptor,
            DiagramProject project,
            ExportPngAction pngAction
    ) {
        return new ExportableOutput(
                Objects.requireNonNull(descriptor, "descriptor"),
                Objects.requireNonNull(project, "project"),
                Objects.requireNonNull(project, "project"),
                null,
                Objects.requireNonNull(pngAction, "pngAction"));
    }

    public static ExportableOutput visualScoped(
            ExportableOutputDescriptor descriptor,
            DiagramProject project,
            DiagramProject visualProject,
            ExportPngAction pngAction
    ) {
        return new ExportableOutput(
                Objects.requireNonNull(descriptor, "descriptor"),
                Objects.requireNonNull(project, "project"),
                Objects.requireNonNull(visualProject, "visualProject"),
                null,
                Objects.requireNonNull(pngAction, "pngAction"));
    }


    public static ExportableOutput matrix(
            ExportableOutputDescriptor descriptor,
            DiagramProject project,
            ExportPngAction pngAction
    ) {
        return new ExportableOutput(
                Objects.requireNonNull(descriptor, "descriptor"),
                Objects.requireNonNull(project, "project"),
                Objects.requireNonNull(project, "project"),
                null,
                Objects.requireNonNull(pngAction, "pngAction"));
    }

    public static ExportableOutput document(
            ExportableOutputDescriptor descriptor,
            DiagramProject project,
            DataDictionaryDocument document
    ) {
        return new ExportableOutput(
                Objects.requireNonNull(descriptor, "descriptor"),
                Objects.requireNonNull(project, "project"),
                Objects.requireNonNull(project, "project"),
                Objects.requireNonNull(document, "document"),
                null);
    }

    public static ExportableOutput projectDocument(
            ExportableOutputDescriptor descriptor,
            DiagramProject project
    ) {
        return new ExportableOutput(
                Objects.requireNonNull(descriptor, "descriptor"),
                Objects.requireNonNull(project, "project"),
                Objects.requireNonNull(project, "project"),
                null,
                null);
    }

    public ExportableOutputDescriptor descriptor() {
        return descriptor;
    }

    public Optional<DiagramProject> project() {
        return Optional.ofNullable(project);
    }

    /** Proyecto visual concreto para SVG/PNG: puede ser una vista filtrada del proyecto completo. */
    public Optional<DiagramProject> visualProject() {
        return Optional.ofNullable(visualProject);
    }

    public Optional<DataDictionaryDocument> dataDictionaryDocument() {
        return Optional.ofNullable(dataDictionaryDocument);
    }

    public Optional<ExportPngAction> pngAction() {
        return Optional.ofNullable(pngAction);
    }

    public boolean supports(ExportFormat format) {
        return descriptor.supports(format);
    }
}
