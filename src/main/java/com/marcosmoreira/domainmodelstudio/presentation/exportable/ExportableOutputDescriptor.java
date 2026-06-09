package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.EnumSet;
import java.util.List;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * Describe la salida final activa de una pestaña.
 *
 * <p>Este descriptor es deliberadamente de presentación: habla de la salida que se
 * puede entregar/exportar, no de cómo está construido internamente el editor.</p>
 */
public record ExportableOutputDescriptor(
        DiagramTypeId diagramTypeId,
        String title,
        String suggestedFileStem,
        ExportableOutputKind kind,
        Set<ExportFormat> supportedFormats,
        List<String> notices
) {

    public ExportableOutputDescriptor {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        title = normalize(title, "Salida sin título");
        suggestedFileStem = sanitizeFileStem(suggestedFileStem == null || suggestedFileStem.isBlank()
                ? title
                : suggestedFileStem);
        Objects.requireNonNull(kind, "kind");
        supportedFormats = supportedFormats == null || supportedFormats.isEmpty()
                ? Set.of()
                : Set.copyOf(EnumSet.copyOf(supportedFormats));
        notices = notices == null ? List.of() : List.copyOf(notices);
    }

    public static ExportableOutputDescriptor visualDiagram(
            DiagramTypeId diagramTypeId,
            String title,
            String suggestedFileStem,
            Set<ExportFormat> supportedFormats
    ) {
        return new ExportableOutputDescriptor(
                diagramTypeId,
                title,
                suggestedFileStem,
                ExportableOutputKind.VISUAL_DIAGRAM,
                supportedFormats,
                List.of());
    }


    public static ExportableOutputDescriptor matrix(
            DiagramTypeId diagramTypeId,
            String title,
            String suggestedFileStem,
            Set<ExportFormat> supportedFormats
    ) {
        return new ExportableOutputDescriptor(
                diagramTypeId,
                title,
                suggestedFileStem,
                ExportableOutputKind.MATRIX,
                supportedFormats,
                List.of());
    }

    public static ExportableOutputDescriptor document(
            DiagramTypeId diagramTypeId,
            String title,
            String suggestedFileStem,
            Set<ExportFormat> supportedFormats
    ) {
        return new ExportableOutputDescriptor(
                diagramTypeId,
                title,
                suggestedFileStem,
                ExportableOutputKind.DOCUMENT,
                supportedFormats,
                List.of());
    }

    public boolean supports(ExportFormat format) {
        return format != null && supportedFormats.contains(format);
    }

    public String suggestedFileName(ExportFormat format) {
        Objects.requireNonNull(format, "format");
        return suggestedFileStem + "." + format.defaultExtension();
    }

    private static String normalize(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private static String sanitizeFileStem(String raw) {
        String ascii = Normalizer.normalize(normalize(raw, "salida"), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        String normalized = ascii.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? "salida" : normalized;
    }
}
