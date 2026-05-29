package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenKind;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransitionKind;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ScreenFlowMarkdownExporterTest {

    @Test
    void shouldExportImportableScreenFlowMarkdown() {
        ScreenFlowDocument document = new ScreenFlowDocument(
                "Flujo de prueba",
                "1.0",
                LocalDate.now(),
                List.of(
                        new ScreenNode("inicio", "Inicio", ScreenKind.DASHBOARD, "General", "/inicio", "Entrada", ""),
                        new ScreenNode("ventas", "Ventas", ScreenKind.LIST, "Ventas", "/ventas", "Listado", "")),
                List.of(new ScreenTransition("nav_1", "inicio", "ventas", ScreenTransitionKind.NAVIGATES, "abrir ventas", "", "")));
        DiagramProject project = DiagramProject.blank("flujo", "Flujo de prueba", DiagramTypeId.SCREEN_FLOW)
                .withScreenFlow(document);

        String markdown = new ScreenFlowMarkdownExporter().export(project);

        assertTrue(markdown.contains("diagram_type: \"screen-flow\""));
        assertTrue(markdown.contains("importable: true"));
        assertTrue(markdown.contains("# Pantallas"));
        assertTrue(markdown.contains("# Navegación"));
        assertTrue(markdown.contains("inicio -> ventas"));
    }
}
