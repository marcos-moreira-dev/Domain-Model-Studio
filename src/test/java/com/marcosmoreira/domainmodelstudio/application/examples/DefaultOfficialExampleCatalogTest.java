package com.marcosmoreira.domainmodelstudio.application.examples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DefaultOfficialExampleCatalogTest {

    private final DefaultOfficialExampleCatalog catalog = new DefaultOfficialExampleCatalog();

    @Test
    void exposesOneOfficialExampleForEveryVisibleDiagramType() {
        Set<DiagramTypeId> types = new HashSet<>();
        for (OfficialExampleDescriptor example : catalog.findAll()) {
            types.add(example.diagramTypeId());
        }

        assertTrue(types.contains(DiagramTypeId.CONCEPTUAL_MODEL));
        assertTrue(types.contains(DiagramTypeId.DATA_DICTIONARY));
        assertTrue(types.contains(DiagramTypeId.ADMIN_MODULE_MAP));
        assertTrue(types.contains(DiagramTypeId.ADMIN_WIREFRAMES));
        assertTrue(types.contains(DiagramTypeId.BPMN_BASIC));
        assertTrue(types.contains(DiagramTypeId.OPERATIONAL_FLOW));
        assertTrue(types.contains(DiagramTypeId.C4_CONTEXT));
        assertTrue(types.contains(DiagramTypeId.C4_CONTAINERS));
        assertTrue(types.contains(DiagramTypeId.TECHNICAL_DEPLOYMENT));
        assertTrue(types.contains(DiagramTypeId.UML_USE_CASE));
        assertTrue(types.contains(DiagramTypeId.UML_CLASS));
        assertTrue(types.contains(DiagramTypeId.UML_ACTIVITY));
        assertTrue(types.contains(DiagramTypeId.UML_SEQUENCE));
        assertTrue(types.contains(DiagramTypeId.UML_STATE));
        assertTrue(types.contains(DiagramTypeId.ROLES_PERMISSIONS_MAP));
        assertTrue(types.contains(DiagramTypeId.SCREEN_FLOW));
        assertTrue(types.contains(DiagramTypeId.FREE_GRAPH));
        assertTrue(types.contains(DiagramTypeId.LOGICAL_BUSINESS_GRAPH));
        assertTrue(types.contains(DiagramTypeId.LOGICAL_BUSINESS_INTAKE));
    }

    @Test
    void keepsIdsUniqueAndResourceLocationsValid() {
        Set<String> ids = new HashSet<>();
        for (OfficialExampleDescriptor example : catalog.findAll()) {
            assertTrue(ids.add(example.id()), "ID duplicado: " + example.id());
            boolean locationIsOfficialExample = example.classpathLocation().startsWith("ai-resources/official-markdown/diagramas/")
                    || example.classpathLocation().startsWith("ai-resources/official-markdown/levantamiento-logico/");
            assertTrue(locationIsOfficialExample, "Ubicación oficial no reconocida: " + example.classpathLocation());
            assertTrue(example.classpathLocation().endsWith(".md"));
            assertFalse(example.title().isBlank());
            assertFalse(example.diagramTypeName().isBlank());
        }
    }

    @Test
    void separatesImportableExamplesFromReferenceDocuments() {
        assertEquals(19, catalog.findAll().size());
        assertEquals(19, catalog.findImportable().size());
        assertEquals(1, catalog.findByDiagramType(DiagramTypeId.LOGICAL_BUSINESS_INTAKE).size());
        assertEquals(1, catalog.findByDiagramType(DiagramTypeId.LOGICAL_BUSINESS_GRAPH).size());
        assertEquals(1, catalog.findByDiagramType(DiagramTypeId.DATA_DICTIONARY).size());
        assertTrue(catalog.findByDiagramType(DiagramTypeId.DATA_DICTIONARY).get(0).importable());
    }
}
