package com.marcosmoreira.domainmodelstudio.domain.diagram;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/**
 * Metadatos semánticos del proyecto.
 */
public final class ProjectMetadata {

    private final String id;
    private final String title;
    private final String version;
    private final String status;
    private final ProjectType projectType;
    private final DiagramTypeId diagramTypeId;
    private final NotationType activeNotation;
    private final String sourceMarkdownPath;
    private final String description;

    public ProjectMetadata(
            String id,
            String title,
            String version,
            String status,
            NotationType activeNotation,
            String sourceMarkdownPath,
            String description
    ) {
        this(id, title, ProjectType.defaultType(), ProjectType.defaultType().diagramTypeId(),
                version, status, activeNotation, sourceMarkdownPath, description);
    }

    public ProjectMetadata(
            String id,
            String title,
            ProjectType projectType,
            String version,
            String status,
            NotationType activeNotation,
            String sourceMarkdownPath,
            String description
    ) {
        this(id, title, projectType, inferDiagramTypeId(projectType),
                version, status, activeNotation, sourceMarkdownPath, description);
    }

    public ProjectMetadata(
            String id,
            String title,
            ProjectType projectType,
            DiagramTypeId diagramTypeId,
            String version,
            String status,
            NotationType activeNotation,
            String sourceMarkdownPath,
            String description
    ) {
        this.id = requireText(id, "El ID del proyecto no puede estar vacío");
        this.title = requireText(title, "El título del proyecto no puede estar vacío");
        this.projectType = projectType == null ? ProjectType.defaultType() : projectType;
        this.diagramTypeId = diagramTypeId == null ? this.projectType.diagramTypeId() : diagramTypeId;
        this.version = normalizeOrDefault(version, "0.1.0");
        this.status = normalizeOrDefault(status, "draft");
        this.activeNotation = activeNotation == null ? NotationType.CHEN : activeNotation;
        this.sourceMarkdownPath = normalizeOptionalText(sourceMarkdownPath);
        this.description = normalizeOptionalText(description);
    }

    public static ProjectMetadata draft(String id, String title) {
        return new ProjectMetadata(id, title, ProjectType.defaultType(), ProjectType.defaultType().diagramTypeId(),
                "0.1.0", "draft", NotationType.CHEN, "", "");
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public ProjectType projectType() {
        return projectType;
    }

    public DiagramTypeId diagramTypeId() {
        return diagramTypeId;
    }

    public String version() {
        return version;
    }

    public String status() {
        return status;
    }

    public NotationType activeNotation() {
        return activeNotation;
    }

    public String sourceMarkdownPath() {
        return sourceMarkdownPath;
    }

    public String description() {
        return description;
    }

    public ProjectMetadata withActiveNotation(NotationType notationType) {
        return new ProjectMetadata(id, title, projectType, diagramTypeId,
                version, status, notationType, sourceMarkdownPath, description);
    }

    public ProjectMetadata withTitle(String updatedTitle) {
        return new ProjectMetadata(id, updatedTitle, projectType, diagramTypeId,
                version, status, activeNotation, sourceMarkdownPath, description);
    }

    public ProjectMetadata withDescription(String updatedDescription) {
        return new ProjectMetadata(id, title, projectType, diagramTypeId,
                version, status, activeNotation, sourceMarkdownPath, updatedDescription);
    }

    public ProjectMetadata withDiagramTypeId(DiagramTypeId updatedDiagramTypeId) {
        return new ProjectMetadata(id, title, projectType, updatedDiagramTypeId,
                version, status, activeNotation, sourceMarkdownPath, description);
    }

    public ProjectMetadata withSourceMarkdownPath(String updatedSourceMarkdownPath) {
        return new ProjectMetadata(id, title, projectType, diagramTypeId,
                version, status, activeNotation, updatedSourceMarkdownPath, description);
    }

    private static DiagramTypeId inferDiagramTypeId(ProjectType projectType) {
        return projectType == null ? ProjectType.defaultType().diagramTypeId() : projectType.diagramTypeId();
    }

    private static String requireText(String value, String message) {
        String normalized = Objects.requireNonNull(value, message).trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private static String normalizeOrDefault(String value, String defaultValue) {
        String normalized = normalizeOptionalText(value);
        return normalized.isEmpty() ? defaultValue : normalized;
    }

    private static String normalizeOptionalText(String value) {
        return value == null ? "" : value.trim();
    }
}
