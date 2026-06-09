package com.marcosmoreira.domainmodelstudio.application.export;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ExportSvgUseCaseTest {

    @Test
    void writesSvgFileAndAddsExtensionWhenMissing() throws Exception {
        ExportSvgUseCase useCase = new ExportSvgUseCase(project -> "<svg></svg>\n");
        Path targetWithoutExtension = Files.createTempDirectory("dms-svg-test").resolve("diagram-export");

        Path exported = useCase.export(DiagramProject.blank("empty", "Empty"), targetWithoutExtension);

        assertTrue(Files.exists(targetWithoutExtension.resolveSibling("diagram-export.svg")));
        assertTrue(exported.endsWith("diagram-export.svg"));
    }
}
