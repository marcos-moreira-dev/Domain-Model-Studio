package com.marcosmoreira.domainmodelstudio.application.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ExportMarkdownUseCaseTest {

    @Test
    void writesMarkdownFileAndAddsExtensionWhenMissing() throws Exception {
        ExportMarkdownUseCase useCase = new ExportMarkdownUseCase(project -> "# Entidades\n");
        Path targetWithoutExtension = Files.createTempDirectory("dms-md-test").resolve("modelo-actualizado");

        Path exported = useCase.export(DiagramProject.blank("empty", "Empty"), targetWithoutExtension);

        assertEquals(targetWithoutExtension.resolveSibling("modelo-actualizado.md"), exported);
        assertTrue(Files.exists(exported));
    }
}
