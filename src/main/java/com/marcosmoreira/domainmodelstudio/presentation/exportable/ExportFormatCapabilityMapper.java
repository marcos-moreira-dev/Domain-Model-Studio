package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/** Traduce capacidades oficiales de producto a formatos de exportación visibles. */
final class ExportFormatCapabilityMapper {

    Set<ExportFormat> formatsFrom(DiagramCapabilitySet capabilities, boolean pngAvailable) {
        Objects.requireNonNull(capabilities, "capabilities");
        EnumSet<ExportFormat> formats = EnumSet.noneOf(ExportFormat.class);
        if (capabilities.has(DiagramCapability.EXPORT_SVG)) {
            formats.add(ExportFormat.SVG);
        }
        if (pngAvailable && capabilities.has(DiagramCapability.EXPORT_PNG)) {
            formats.add(ExportFormat.PNG);
        }
        if (capabilities.has(DiagramCapability.EXPORT_PDF)) {
            formats.add(ExportFormat.PDF);
        }
        if (capabilities.has(DiagramCapability.EXPORT_MARKDOWN)) {
            formats.add(ExportFormat.MARKDOWN);
        }
        return formats.isEmpty() ? Set.of() : Set.copyOf(formats);
    }
}
