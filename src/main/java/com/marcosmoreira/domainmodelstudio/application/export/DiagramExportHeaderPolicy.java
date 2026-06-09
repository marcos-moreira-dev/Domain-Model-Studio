package com.marcosmoreira.domainmodelstudio.application.export;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectMetadata;

/**
 * Política común para construir encabezados de exportación visual.
 *
 * <p>Centraliza el texto y las dimensiones reservadas para que SVG y PNG no inventen
 * encabezados incompatibles entre sí. La política no participa en Markdown canónico,
 * persistencia {@code .dms} ni generación de documentos estructurados.</p>
 */
public final class DiagramExportHeaderPolicy {

    /** Alto reservado para encabezado en exportaciones PNG del canvas común. */
    public static final double PNG_HEADER_HEIGHT = 74.0;

    /** Alto mínimo del bloque visual del encabezado SVG especializado. */
    public static final double SVG_HEADER_BLOCK_HEIGHT = 44.0;

    private DiagramExportHeaderPolicy() {
    }

    public static DiagramExportHeaderMetadata forProject(DiagramProject project, String fallbackTitle) {
        if (project == null) {
            return DiagramExportHeaderMetadata.of(
                    firstNonBlank(fallbackTitle, "Diagrama exportado"),
                    "Salida visual de Domain Model Studio",
                    "Salida visual");
        }
        ProjectMetadata metadata = project.metadata();
        String subtitle = firstNonBlank(metadata.description(), metadata.diagramTypeId().value());
        return DiagramExportHeaderMetadata.of(metadata.title(), subtitle, metadata.diagramTypeId().value());
    }

    public static DiagramExportHeaderMetadata forDiagramType(DiagramTypeId diagramTypeId, String fallbackTitle) {
        return fallback(diagramTypeId, fallbackTitle);
    }

    public static DiagramExportHeaderMetadata forSpecializedSvg(String title, String typeLabel, String viewSummary) {
        return DiagramExportHeaderMetadata.of(
                title,
                firstNonBlank(viewSummary, typeLabel, "SVG vectorial documental"),
                firstNonBlank(typeLabel, viewSummary, "Diagrama especializado"));
    }

    private static DiagramExportHeaderMetadata fallback(DiagramTypeId diagramTypeId, String fallbackTitle) {
        DiagramTypeId safeType = diagramTypeId == null ? DiagramTypeId.CONCEPTUAL_MODEL : diagramTypeId;
        return DiagramExportHeaderMetadata.of(
                firstNonBlank(fallbackTitle, "Diagrama exportado"),
                safeType.value(),
                safeType.value());
    }

    private static String firstNonBlank(String... values) {
        if (values != null) {
            for (String value : values) {
                if (value != null && !value.isBlank()) {
                    return value.strip();
                }
            }
        }
        return "";
    }
}
