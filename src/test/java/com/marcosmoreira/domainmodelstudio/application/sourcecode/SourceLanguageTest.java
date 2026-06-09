package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SourceLanguageTest {

    @Test
    void languageShouldRecognizeSupportedExtensionsWithoutDependingOnVersions() {
        assertTrue(SourceLanguage.JAVA.matchesFileName("PedidoService.java"));
        assertTrue(SourceLanguage.TYPESCRIPT.matchesFileName("pedido.service.ts"));
        assertTrue(SourceLanguage.TYPESCRIPT.matchesFileName("pedido.component.tsx"));
        assertFalse(SourceLanguage.JAVA.matchesFileName("pedido.service.ts"));
    }

    @Test
    void languageVersionShouldBehaveAsHintNotHardDependency() {
        SourceLanguageVersion javaHint = SourceLanguageVersion.hinted(SourceLanguage.JAVA, "21");
        SourceLanguageVersion flexibleTs = SourceLanguageVersion.flexible(SourceLanguage.TYPESCRIPT);

        assertEquals(SourceLanguage.JAVA, javaHint.language());
        assertEquals("21", javaHint.versionHint());
        assertFalse(javaHint.strict());
        assertEquals("", flexibleTs.versionHint());
    }
}
