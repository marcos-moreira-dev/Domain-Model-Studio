package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Verifica que la tanda 05 migre símbolos piloto a kits especializados. */
class ShapeKitsIntegrationSourceTest {

    @Test
    void behaviorRenderKitUsesUmlAndBpmnShapeKits() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorRenderKit.java");

        assertTrue(source.contains("UmlShapeKit"), "Casos de uso/estados deben usar kit UML.");
        assertTrue(source.contains("BpmnShapeKit"), "BPMN/flujo operativo deben usar kit BPMN.");
        assertTrue(source.contains("UML_SHAPES.actorSymbol()"), "Actor UML debe salir del kit UML.");
        assertTrue(source.contains("BPMN_SHAPES.gatewaySymbol()"), "Gateway/decisión debe salir del kit BPMN.");
    }

    @Test
    void architectureRenderKitUsesC4ShapeKit() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureRenderKit.java");

        assertTrue(source.contains("C4ShapeKit"), "Arquitectura debe usar kit C4/despliegue.");
        assertTrue(source.contains("C4_SHAPES.databaseSymbol()"), "BD debe dibujarse como símbolo C4 especializado.");
        assertTrue(source.contains("C4_SHAPES.personSymbol()"), "Persona/cliente debe tener símbolo especializado.");
    }

    @Test
    void umlShapeKitContainsStickActor() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/uml/UmlShapeKit.java");

        assertTrue(source.contains("actorSymbol"), "Debe existir símbolo de actor UML.");
        assertTrue(source.contains("Circle head") && source.contains("Line body"),
                "Actor UML debe componerse como muñeco de palito, no como elipse genérica.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
