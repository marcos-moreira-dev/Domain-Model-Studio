package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SvgExportContractTest {

    @Test
    void svgContractIsDocumentalAndNotUniversalWysiwyg() {
        assertTrue(SvgExportContract.appliesTo(ExportFormat.SVG));
        assertFalse(SvgExportContract.appliesTo(ExportFormat.PNG));
        assertTrue(SvgExportContract.DISPLAY_NAME.contains("vectorial documental"));
        assertTrue(SvgExportContract.TOOLTIP_DETAIL.contains("no promete ser una copia WYSIWYG exacta"));
    }
}
