package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassCanvasInteractionTanda5SourceTest {

    private static final Path ADAPTER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java");

    @Test
    void moduleDragCommitShouldDelegateOnePersistentMoveToViewModel() throws Exception {
        String adapter = Files.readString(ADAPTER, StandardCharsets.UTF_8);
        String body = methodBody(adapter, "moveModuleAndVisibleClassesBy");

        assertTrue(body.contains("viewModel.moveModuleTo"),
                "El commit persistente del módulo debe delegar el movimiento agrupado al ViewModel.");
        assertFalse(body.contains("for (UmlClassNode"),
                "El adapter no debe mover de nuevo cada clase si el ViewModel ya mueve las clases internas del módulo.");
    }

    @Test
    void moduleDragPreviewShouldStillIncludeVisibleChildren() throws Exception {
        String adapter = Files.readString(ADAPTER, StandardCharsets.UTF_8);
        String previewBody = methodBody(adapter, "previewNodeIdsForDraggedNode");
        String addChildrenBody = methodBody(adapter, "addClassIdsForModule");

        assertTrue(previewBody.contains("addClassIdsForModule(result, moduleId);"),
                "La previsualización debe seguir moviendo visualmente las clases internas junto al módulo.");
        assertTrue(addChildrenBody.contains("target.add(classLayoutId(umlClass.id()))"),
                "El preview usa ids de clases visibles, sin persistirlas dos veces al soltar.");
    }

    private static String methodBody(String source, String methodName) {
        int methodIndex = source.indexOf(methodName + "(");
        assertTrue(methodIndex >= 0, "No se encontró el método " + methodName);
        int bodyStart = source.indexOf('{', methodIndex);
        assertTrue(bodyStart >= 0, "No se encontró el cuerpo del método " + methodName);
        int depth = 0;
        for (int index = bodyStart; index < source.length(); index++) {
            char current = source.charAt(index);
            if (current == '{') {
                depth++;
            } else if (current == '}') {
                depth--;
                if (depth == 0) {
                    return source.substring(bodyStart + 1, index);
                }
            }
        }
        throw new AssertionError("No se pudo extraer el cuerpo del método " + methodName);
    }
}
