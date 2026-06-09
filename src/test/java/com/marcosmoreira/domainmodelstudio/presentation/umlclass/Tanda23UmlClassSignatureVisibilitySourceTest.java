package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 23: firmas UML importadas desde código deben truncarse mucho menos. */
class Tanda23UmlClassSignatureVisibilitySourceTest {

    @Test
    void memberSignaturesShouldExposeRoughlyTwiceThePreviousCharacters() throws IOException {
        String labelPolicy = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDisplayLabelPolicy.java");
        String metrics = read("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/UmlClassBoxMetricsCalculator.java");

        assertTrue(labelPolicy.contains("MEMBER_LINE_MAX = 76"),
                "Atributos y métodos deben mostrar aproximadamente el doble del límite histórico de 38 caracteres.");
        assertTrue(metrics.contains("MAX_WIDTH = 560.0"),
                "Las cajas UML deben poder crecer lo suficiente para que el nuevo límite sea visible.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
