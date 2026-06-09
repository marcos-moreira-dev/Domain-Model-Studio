package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlBehaviorSourceContractTest {

    @Test
    void behaviorValidationDelegatesUmlRulesToDedicatedPolicy() throws IOException {
        String source = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/application/behavior/ValidateBehaviorDiagramUseCase.java"));

        assertTrue(source.contains("UmlBehaviorValidationPolicy"));
        assertFalse(source.contains("Agrega al menos un actor."));
    }

    @Test
    void behaviorCanvasPrefixesUmlKindsForSpecializedRendering() throws IOException {
        String source = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorCanvasAdapter.java"));

        assertTrue(source.contains("uml-use-case-"));
        assertTrue(source.contains("uml-activity-"));
        assertTrue(source.contains("uml-state-"));
    }

    @Test
    void behaviorRenderKitUsesUmlSymbolsForBehaviorDiagrams() throws IOException {
        String source = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorRenderKit.java"));

        assertTrue(source.contains("UML_SHAPES.actionSymbol()"));
        assertTrue(source.contains("UML_SHAPES.stateSymbol()"));
        assertTrue(source.contains("DiagramArrowKind.OPEN"));
    }
}
