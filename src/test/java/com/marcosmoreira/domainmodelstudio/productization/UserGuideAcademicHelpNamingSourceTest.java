package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl documental de Tanda 32 para la frontera entre guía académica y ayuda operativa. */
class UserGuideAcademicHelpNamingSourceTest {

    private static final Path DOCS = Path.of("docs");

    @Test
    void userGuideShouldNameAcademicGuideAndOperationalSideDockHelpSeparately() throws IOException {
        String readme = readDoc("user-guide/README.md");
        String manual = readDoc("user-guide/08_manual_integrado.md");
        String glossary = readDoc("user-guide/09_glosario_producto.md");
        String architecture = readDoc("arquitectura/13_ayuda_chm_academica.md");

        assertTrue(readme.contains("Ayuda > Guía académica"));
        assertTrue(readme.contains("botón Ayuda del SideDock"));
        assertTrue(manual.contains("# 08 - Guía académica integrada"));
        assertTrue(manual.contains("separada de la ayuda operativa del SideDock"));
        assertTrue(glossary.contains("Guía académica"));
        assertTrue(glossary.contains("Ayuda operativa"));
        assertTrue(architecture.contains("Ayuda → Guía académica"));
        assertFalse(readme.contains("Ayuda > Manual de Domain Model Studio"));
        assertFalse(manual.contains("Ayuda > Manual de Domain Model Studio"));
    }

    private static String readDoc(String relativePath) throws IOException {
        return Files.readString(DOCS.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
