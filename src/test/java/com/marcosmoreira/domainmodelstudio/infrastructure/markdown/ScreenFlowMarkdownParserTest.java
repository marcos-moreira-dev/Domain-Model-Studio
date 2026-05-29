package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ScreenFlowMarkdownParserTest {

    @Test
    void shouldImportOfficialScreenFlowExample() throws Exception {
        ScreenFlowMarkdownParser parser = new ScreenFlowMarkdownParser();

        DiagramProject project = parser.parse(Path.of("examples", "markdown", "diagramas", "screen_flow_ventas_minimo.md"));

        assertEquals(DiagramTypeId.SCREEN_FLOW, project.metadata().diagramTypeId());
        assertTrue(project.screenFlow().isPresent());
        assertEquals(4, project.screenFlow().orElseThrow().screens().size());
        assertEquals(5, project.screenFlow().orElseThrow().transitions().size());
    }
}
