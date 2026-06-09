package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Verifica que los render kits no vuelvan a ser cajas genéricas sin teoría mínima. */
class RenderKitTheoryMinimumSourceTest {

    @Test
    void umlClassRenderKitShouldDifferentiateStructuralRelations() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassRenderKit.java");

        assertTrue(source.contains("uml-relation-inheritance") && source.contains("HOLLOW_TRIANGLE"),
                "Herencia e implementación deben tener punta triangular hueca.");
        assertTrue(source.contains("uml-relation-composition") && source.contains("FILLED_DIAMOND"),
                "Composición debe diferenciarse con diamante lleno.");
        assertTrue(source.contains("uml-relation-aggregation") && source.contains("HOLLOW_DIAMOND"),
                "Agregación debe diferenciarse con diamante hueco.");
        assertTrue(source.contains("uml-relation-dependency") && source.contains("OPEN"),
                "Dependencia debe diferenciarse de una asociación plana.");
    }

    @Test
    void behaviorRenderKitShouldDifferentiateUseCaseAndFlowRelations() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorRenderKit.java");

        assertTrue(source.contains("case \"association\" -> DiagramArrowKind.NONE"),
                "La asociación de casos de uso no debe dibujarse como flujo dirigido obligatorio.");
        assertTrue(source.contains("case \"generalization\" -> DiagramArrowKind.HOLLOW_TRIANGLE"),
                "La generalización UML debe usar punta triangular hueca.");
        assertTrue(source.contains("baseKind.equals(\"include\")") && source.contains("baseKind.equals(\"extend\")"),
                "Include/extend deben distinguirse como relaciones punteadas.");
    }

    @Test
    void migratedRenderKitsShouldShareConnectorGeometryAndDrawingFacadeConnectors() throws IOException {
        for (String path : new String[] {
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapRenderKit.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowRenderKit.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassRenderKit.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorRenderKit.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureRenderKit.java"
        }) {
            String source = read(path);
            assertTrue(source.contains("CanvasConnectorGeometry.edgeToEdgePoints"),
                    path + " debe compartir la geometría común de conectores.");
            assertTrue(source.contains("safeDrawingFacade.connectors().polyline"),
                    path + " debe dibujar conectores con el fachada común de dibujo.");
        }
    }

    @Test
    void connectorStyleShouldAllowFamilySpecificArrowAndLabelClasses() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/DiagramConnectorStyle.java");

        assertTrue(source.contains("withArrowStyleClass"), "Debe poder conservar clases CSS de flechas por familia.");
        assertTrue(source.contains("withLabelStyleClass"), "Debe poder conservar clases CSS de etiquetas por familia.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
