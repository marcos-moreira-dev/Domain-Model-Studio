package com.marcosmoreira.domainmodelstudio.productization;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Guardarraíl fuente de la tanda JD-4 de JavaDoc en presentación, workbench y canvas. */
class Jd4PresentationJavadocWorkbenchCanvasSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void presentationPackagesShouldExplainArchitecturalBoundaries() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/package-info.java",
                "Capa de shell JavaFX", "no debe contener semántica de negocio", "coordinadores especializados");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/package-info.java",
                "SideDock transversal", "sidebar legacy del modelo conceptual", "ayuda operativa");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/package-info.java",
                "viewport", "zoom", "paneo");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/package-info.java",
                "selección", "drag", "puntos intermedios");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/package-info.java",
                "Workspace visual del Grafo lógico", "no contaminar el canvas conceptual", "dominio");
    }

    @Test
    void criticalPresentationClassesShouldDocumentResponsibilities() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java",
                "Vista principal de la aplicación JavaFX", "toolbar global/contextual", "semántica de un tipo de proyecto");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/WorkbenchSideDock.java",
                "frontera operativa", "estructura, propiedades, apariencia", "no contiene reglas de negocio");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/ZoomableDiagramSurface.java",
                "workspace físico", "viewport visible", "capas");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java",
                "Vista JavaFX transversal", "adaptador", "canvas escriba directamente sobre el dominio");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasBendPointController.java",
                "puntos intermedios", "segmento más cercano", "evitar deformaciones visuales");
    }

    @Test
    void logicalBusinessGraphPresentationShouldExplainCanvasProjection() throws IOException {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphViewModel.java",
                "frontera presentación/aplicación", "nuevo proyecto coherente", "guardarse, exportarse o validarse");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphCanvasAdapter.java",
                "no convierte el Grafo lógico en grafo libre", "reutiliza infraestructura de canvas", "LogicalBusinessGraphDocument");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphWorkbenchContributor.java",
                "estructura, propiedades, leyenda", "carcasa transversal", "modelo conceptual");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphDiagramCenter.java",
                "ZoomableDiagramSurface", "InteractiveCanvasSurfaceView", "no validar semántica");
    }

    @Test
    void onboardingShouldIncludePresentationReadingRoute() throws IOException {
        assertContains("docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md",
                "Presentación: shell, workspaces y canvas", "MainShellView", "InteractiveCanvasSurfaceView");
        assertContains("docs/calidad/AUDITORIA_JAVADOC_JD4.md",
                "presentation/sidedock", "presentation/interactivecanvas", "No se tocó pantalla de inicio");
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Estado: aplicada en JD-4", "Tanda JD-9");
    }

    private static void assertContains(String relativePath, String... requiredFragments) throws IOException {
        String text = Files.readString(ROOT.resolve(relativePath));
        for (String fragment : requiredFragments) {
            assertTrue(text.contains(fragment), () -> relativePath + " no contiene: " + fragment);
        }
    }
}
