package com.marcosmoreira.domainmodelstudio.application.examples;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import org.junit.jupiter.api.Test;

class OfficialExampleResourceCoverageTest {

    @Test
    void everyRegisteredExampleResourceExistsInClasspath() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (OfficialExampleDescriptor example : new DefaultOfficialExampleCatalog().findAll()) {
            try (InputStream stream = classLoader.getResourceAsStream(example.classpathLocation())) {
                assertNotNull(stream, "No existe recurso: " + example.classpathLocation());
                String markdown = new String(stream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                assertTrue(markdown.contains("diagram_type"), "Falta diagram_type en " + example.sourceName());
            }
        }
    }
}
