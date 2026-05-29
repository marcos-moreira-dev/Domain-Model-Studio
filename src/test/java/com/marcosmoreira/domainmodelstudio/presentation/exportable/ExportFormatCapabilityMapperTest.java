package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExportFormatCapabilityMapperTest {

    private final ExportFormatCapabilityMapper mapper = new ExportFormatCapabilityMapper();

    @Test
    void mapsDocumentCapabilitiesWithoutInventingVisualFormats() {
        DiagramCapabilitySet capabilities = DiagramCapabilitySet.of(
                DiagramCapability.EXPORT_PDF,
                DiagramCapability.EXPORT_MARKDOWN);

        assertEquals(Set.of(ExportFormat.PDF, ExportFormat.MARKDOWN), mapper.formatsFrom(capabilities, true));
    }

    @Test
    void pngRequiresBothCapabilityAndRuntimeSurface() {
        DiagramCapabilitySet capabilities = DiagramCapabilitySet.of(
                DiagramCapability.EXPORT_SVG,
                DiagramCapability.EXPORT_PNG,
                DiagramCapability.EXPORT_MARKDOWN);

        assertEquals(Set.of(ExportFormat.SVG, ExportFormat.MARKDOWN), mapper.formatsFrom(capabilities, false));
        assertEquals(Set.of(ExportFormat.SVG, ExportFormat.PNG, ExportFormat.MARKDOWN), mapper.formatsFrom(capabilities, true));
    }
}
