package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import org.junit.jupiter.api.Test;

class DmsProjectJsonWriterVersionTest {

    @Test
    void writesTopLevelFormatVersionAndViewState() {
        String json = new DmsProjectJsonWriter().write(DiagramProject.blank("proyecto", "Proyecto"));

        assertTrue(json.contains("\"formatVersion\": " + DmsProjectFormat.CURRENT_FORMAT_VERSION));
        assertTrue(json.contains("\"modelKind\": \"conceptual-model\""));
        assertTrue(json.contains("\"view\""));
        assertTrue(json.contains("\"zoomFactor\""));
        assertTrue(json.contains("\"collapsedGroups\""));
        assertTrue(json.contains("\"filters\""));
    }
}
