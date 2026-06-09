package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de la tanda 09 para que BPMN/flujo operativo no vuelvan a grafo genérico. */
class BusinessProcessVisualIdentitySourceTest {

    @Test
    void behaviorAdapterTagsBpmnAndOperationalFlowDifferently() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorCanvasAdapter.java");

        assertTrue(source.contains("bpmn-"), "BPMN debe llevar prefijo visual propio.");
        assertTrue(source.contains("operational-"), "Flujo operativo debe llevar prefijo visual propio.");
    }

    @Test
    void operationalFlowHasOwnShapeKit() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/process/OperationalFlowShapeKit.java");

        assertTrue(source.contains("stepSymbol"), "El flujo operativo debe tener símbolo de paso.");
        assertTrue(source.contains("responsibleSymbol"), "El flujo operativo debe tener símbolo de responsable.");
        assertTrue(source.contains("documentSymbol"), "El flujo operativo debe tener símbolo de documento/evidencia.");
    }

    @Test
    void processValidationIsExtractedFromGeneralUseCase() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/application/behavior/ValidateBehaviorDiagramUseCase.java");

        assertTrue(source.contains("BusinessProcessValidationPolicy"),
                "Las reglas de BPMN/flujo operativo no deben inflar el caso de uso general.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
