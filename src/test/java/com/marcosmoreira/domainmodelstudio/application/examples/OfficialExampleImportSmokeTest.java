package com.marcosmoreira.domainmodelstudio.application.examples;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DiagramMarkdownImportDispatcher;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class OfficialExampleImportSmokeTest {

    @Test
    void everyImportableOfficialExampleCanBeParsedByDispatcher() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        DiagramMarkdownImportDispatcher dispatcher = new DiagramMarkdownImportDispatcher(new DefaultDiagramTypeRegistry());

        for (OfficialExampleDescriptor example : new DefaultOfficialExampleCatalog().findImportable()) {
            try (InputStream stream = classLoader.getResourceAsStream(example.classpathLocation())) {
                assertNotNull(stream, "No existe recurso importable: " + example.classpathLocation());
                String markdown = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                assertDoesNotThrow(() -> dispatcher.parse(markdown, example.sourceName()),
                        "No se pudo importar ejemplo oficial: " + example.sourceName());
            }
        }
    }
}
