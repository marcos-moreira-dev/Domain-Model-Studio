package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramViewState;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class DmsProjectJsonReaderVersionTest {

    @Test
    void readsLegacyProjectWithoutFormatVersionUsingDefaultViewState() {
        String legacyJson = """
                {
                  "project": {
                    "id": "legacy",
                    "title": "Legacy",
                    "projectType": "CONCEPTUAL_MODEL"
                  },
                  "model": {
                    "entities": [],
                    "relationships": []
                  },
                  "layouts": {
                    "activeNotation": "CHEN",
                    "byNotation": {}
                  }
                }
                """;

        DiagramProject project = new DmsProjectJsonReader().read(legacyJson);

        assertEquals(DiagramViewState.DEFAULT_VIEW_MODE, project.viewState().viewMode());
        assertEquals(1.0, project.viewState().zoomFactor());
        assertTrue(project.viewState().collapsedGroups().isEmpty());
    }

    @Test
    void readsCurrentProjectPreservingViewState() {
        String json = """
                {
                  "formatVersion": 2,
                  "project": {
                    "id": "uml_restaurante",
                    "title": "UML Restaurante",
                    "projectType": "CONCEPTUAL_MODEL",
                    "diagramTypeId": "uml-class-diagram",
                    "version": "0.1.0",
                    "status": "draft",
                    "activeNotation": "CHEN"
                  },
                  "model": {
                    "modelKind": "uml-class-diagram",
                    "entities": [],
                    "relationships": []
                  },
                  "layouts": {
                    "activeNotation": "CHEN",
                    "byNotation": {}
                  },
                  "view": {
                    "zoomFactor": 1.5,
                    "viewportX": 120.0,
                    "viewportY": 80.0,
                    "viewMode": "modules",
                    "collapsedGroups": ["ventas"],
                    "filters": {"mostrar-dtos": false}
                  }
                }
                """;

        DiagramProject project = new DmsProjectJsonReader().read(json);

        assertEquals("uml-class-diagram", project.metadata().diagramTypeId().value());
        assertEquals("modules", project.viewState().viewMode());
        assertEquals(1.5, project.viewState().zoomFactor());
        assertTrue(project.viewState().collapsedGroups().contains("ventas"));
        assertFalse(project.viewState().filters().get("mostrar-dtos"));
    }

    @Test
    void readsBundledOldExampleWithLegacyShape() throws Exception {
        Path example = Path.of("examples/projects/supermercado_v1_multi_notacion.dms");
        DiagramProject project = new DmsProjectJsonReader().read(Files.readString(example));

        assertEquals("Modelo conceptual - Supermercado", project.metadata().title());
        assertEquals(NotationType.CHEN, project.layouts().activeNotation());
        assertEquals(4, project.model().entityCount());
        assertEquals(2, project.model().relationshipCount());
        assertTrue(project.layouts().layoutFor(NotationType.CHEN).isPresent());
        assertTrue(project.layouts().layoutFor(NotationType.CROWS_FOOT).isPresent());
    }
}
