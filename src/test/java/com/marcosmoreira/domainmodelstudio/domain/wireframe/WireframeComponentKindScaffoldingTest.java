package com.marcosmoreira.domainmodelstudio.domain.wireframe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class WireframeComponentKindScaffoldingTest {

    @Test
    void includesAdministrativeScaffoldingBlocks() {
        assertEquals("Barra superior", WireframeComponentKind.TOP_BAR.displayName());
        assertEquals("Menú lateral", WireframeComponentKind.SIDEBAR.displayName());
        assertEquals("Modal", WireframeComponentKind.MODAL.displayName());
        assertEquals("Reporte", WireframeComponentKind.REPORT.displayName());
        assertEquals("Paginación", WireframeComponentKind.PAGINATION.displayName());
    }

    @Test
    void keepsBasicPrimitiveGeometryBlocks() {
        assertTrue(Arrays.asList(WireframeComponentKind.values()).contains(WireframeComponentKind.FIELD));
        assertTrue(Arrays.asList(WireframeComponentKind.values()).contains(WireframeComponentKind.TABLE));
        assertTrue(Arrays.asList(WireframeComponentKind.values()).contains(WireframeComponentKind.BUTTON));
    }
}
