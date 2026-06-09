package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassDisplayLabelPolicyTest {

    private final UmlClassDisplayLabelPolicy policy = new UmlClassDisplayLabelPolicy();

    @Test
    void longClassNamesAreTruncatedOnlyOnTheCanvasLabel() {
        UmlClassNode node = new UmlClassNode(
                "c1",
                "m1",
                "ProductoAdministracionAvanzadaServiceParaReportesHistoricos",
                "com.cedro.productos",
                UmlClassKind.SERVICE,
                UmlVisibility.PUBLIC,
                "Servicio de aplicación",
                "Origen: Java; ruta: backend/src/main/java/com/cedro/productos/ProductoAdministracionAvanzadaServiceParaReportesHistoricos.java",
                List.of(),
                "Source root: backend-java"
        );

        assertEquals("ProductoAdministracionAva...", policy.classTitle(node));
        assertTrue(policy.classTooltip(node).contains("ProductoAdministracionAvanzadaServiceParaReportesHistoricos"));
        assertTrue(policy.classMetadataPanel(node).contains("backend/src/main/java"));
    }

    @Test
    void longerMemberLinesRemainReadableOnCanvasAndCompleteInTooltip() {
        UmlClassMember member = new UmlClassMember(
                "m1",
                UmlMemberKind.METHOD,
                "generarReporteHistoricoDeProductos",
                "ReporteHistoricoProductosDto",
                "generarReporteHistoricoDeProductos(ProductoFiltroAvanzado filtro)",
                UmlVisibility.PUBLIC,
                false,
                "Método importado desde TypeScript/Java"
        );

        assertEquals("+ generarReporteHistoricoDeProductos(ProductoFiltroAvanzado filtro)", policy.memberLine(member));
        assertTrue(policy.memberTooltip(member).contains("ProductoFiltroAvanzado"));
    }

    @Test
    void extremelyLongMemberLinesStillUseEllipsisOnCanvas() {
        UmlClassMember member = new UmlClassMember(
                "m2",
                UmlMemberKind.METHOD,
                "generarReporteHistoricoDeProductos",
                "ReporteHistoricoProductosDto",
                "generarReporteHistoricoDeProductos(ProductoFiltroAvanzado filtro, UUID sucursalId, String estado, int pagina, int tamanoPagina)",
                UmlVisibility.PUBLIC,
                false,
                "Método importado desde TypeScript/Java"
        );

        assertEquals("+ generarReporteHistoricoDeProductos(ProductoFiltroAvanzado filtro, UUID ...", policy.memberLine(member));
        assertTrue(policy.memberTooltip(member).contains("tamanoPagina"));
    }
}
