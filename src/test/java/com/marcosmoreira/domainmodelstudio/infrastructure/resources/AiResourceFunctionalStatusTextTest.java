package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Evita que los recursos para IA contradigan el estado funcional real del producto.
 */
class AiResourceFunctionalStatusTextTest {

    private static final List<Path> BEHAVIOR_RESOURCES = List.of(
            Path.of("src/main/resources/ai-resources/05_bpmn_basico_gramatica.md"),
            Path.of("src/main/resources/ai-resources/17_flujo_operativo_gramatica.md"),
            Path.of("src/main/resources/ai-resources/06_uml_casos_uso_gramatica.md"),
            Path.of("src/main/resources/ai-resources/08_uml_actividad_gramatica.md"),
            Path.of("src/main/resources/ai-resources/09_uml_secuencia_gramatica.md"),
            Path.of("src/main/resources/ai-resources/10_uml_estados_gramatica.md")
    );

    @Test
    void behaviorResourcesMustNotSayTheyAreNonImportableOrNonVisual() throws IOException {
        for (Path resource : BEHAVIOR_RESOURCES) {
            String text = Files.readString(resource);

            assertFalse(text.contains("Importable por la app: no"), resource.toString());
            assertFalse(text.contains("Editor visual implementado: no"), resource.toString());
            assertFalse(text.contains("no importa ni renderiza"), resource.toString());
        }
    }
}
