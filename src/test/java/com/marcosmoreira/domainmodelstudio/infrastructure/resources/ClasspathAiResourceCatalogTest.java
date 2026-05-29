package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import org.junit.jupiter.api.Test;

class ClasspathAiResourceCatalogTest {

    @Test
    void shouldExposeOnlyExportableResources() {
        ClasspathAiResourceCatalog catalog = new ClasspathAiResourceCatalog(List.of(
                resource("visible", true),
                resource("oculto", false)));

        List<AiResourceDescriptor> exportable = catalog.findAllExportable();

        assertEquals(1, exportable.size());
        assertEquals("visible", exportable.get(0).id());
    }

    @Test
    void shouldFindResourcesByDiagramType() {
        ClasspathAiResourceCatalog catalog = new ClasspathAiResourceCatalog(List.of(resource("conceptual", true)));

        assertEquals(1, catalog.findByDiagramType(DiagramTypeId.CONCEPTUAL_MODEL).size());
        assertTrue(catalog.findByDiagramType(DiagramTypeId.DATA_DICTIONARY).isEmpty());
    }

    private static AiResourceDescriptor resource(String id, boolean exportable) {
        return new AiResourceDescriptor(
                id,
                id + ".md",
                DiagramTypeId.CONCEPTUAL_MODEL,
                "ai-resources/01_gramatica_markdown_modelo_conceptual.md",
                exportable,
                true,
                "Recurso de prueba.");
    }
}
