package com.marcosmoreira.domainmodelstudio.presentation.datadictionary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class DataDictionaryCssNoRadiusScopedTest {

    private static final Path CSS = Path.of("src/main/resources/css/data-dictionary.css");

    @Test
    void dataDictionaryCssStaysRectangularAndCompact() throws IOException {
        String css = Files.readString(CSS, StandardCharsets.UTF_8);
        long lines = css.lines().count();

        assertFalse(css.contains("-fx-border-radius"),
                "La tanda 34 no debe reintroducir bordes redondeados en el diccionario.");
        assertFalse(css.contains("-fx-background-radius"),
                "La tanda 34 no debe reintroducir fondos redondeados en el diccionario.");
        assertFalse(css.contains("-fx-arc-width") || css.contains("-fx-arc-height"),
                "El CSS del diccionario no debe usar arcos decorativos.");
        assertTrue(lines <= 280,
                "data-dictionary.css debe mantenerse compacto; líneas actuales: " + lines);
        assertTrue(css.contains("No es canvas ni ERD físico"),
                "El CSS debe conservar el contrato visual documental en su comentario de cabecera.");
    }
}
