package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de la tanda 14: aplicaciones administrativas con semántica propia. */
class AdminApplicationsSourceTest {

    @Test
    void administrativeProjectsUseDedicatedLayoutPolicy() throws IOException {
        String factory = read("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/VisualLayoutSpecificationFactory.java");
        String policy = read("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/AdminApplicationsLayoutPolicy.java");
        String generator = read("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/DefaultVisualLayoutGenerator.java");

        assertTrue(factory.contains("AdminApplicationsLayoutPolicy"));
        assertTrue(policy.contains("moduleMapReferences"));
        assertTrue(policy.contains("screenFlowReferences"));
        assertTrue(policy.contains("wireframeReferences"));
        assertTrue(generator.contains("moduleMapPositionFor"));
        assertTrue(generator.contains("screenFlowPositionFor"));
        assertTrue(generator.contains("wireframePositionFor"));
    }

    @Test
    void moduleMapAndScreenFlowUseAdministrativeShapeKit() throws IOException {
        String moduleMap = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapRenderKit.java");
        String screenFlow = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowRenderKit.java");

        assertTrue(moduleMap.contains("AdminShapeKit"));
        assertTrue(moduleMap.contains("packageBox"));
        assertTrue(moduleMap.contains("moduleSymbol()"));
        assertTrue(screenFlow.contains("AdminShapeKit"));
        assertTrue(screenFlow.contains("screenSymbol()"));
    }

    @Test
    void wireframesRemainPrimitiveScaffolding() throws IOException {
        String figureFactory = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeComponentFigureFactory.java");
        String renderKit = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeRenderKit.java");

        assertTrue(figureFactory.contains("botones,\n * campos y tablas son solo figuras")
                || figureFactory.contains("botones,") && figureFactory.contains("son solo figuras"));
        assertTrue(renderKit.contains("no controles reales"));
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
