package com.marcosmoreira.domainmodelstudio.application.examples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class OfficialExampleUensFamilyTest {

    private final DefaultOfficialExampleCatalog catalog = new DefaultOfficialExampleCatalog();

    @Test
    void officialExamplesKeepOnlyUensFamilyAsAutomaticOfficialExamples() {
        var all = catalog.findAll();
        var uensExamples = all.stream().filter(OfficialExampleUensFamilyTest::isUensExample).toList();
        var opticaExamples = all.stream().filter(OfficialExampleUensFamilyTest::isOpticaExample).toList();

        assertEquals(19, uensExamples.size());
        assertEquals(0, opticaExamples.size(), "Óptica Horizonte queda como recurso histórico/IA, no como ejemplo oficial automático.");
        assertTrue(uensExamples.stream().anyMatch(example -> example.diagramTypeId().equals(DiagramTypeId.LOGICAL_BUSINESS_INTAKE)),
                "El levantamiento lógico oficial gordito debe pertenecer a la familia UENS.");

        for (OfficialExampleDescriptor example : uensExamples) {
            assertTrue(
                    example.id().startsWith("uens-") || example.id().equals("ejemplo-levantamiento-logico-uens-gordito"),
                    "Ejemplo fuera de la familia UENS: " + example.id());
            assertTrue(example.title().startsWith("UENS —"), "Título fuera de la familia UENS: " + example.title());
            assertTrue(example.sourceName().contains("uens"), "Recurso fuera de la familia UENS: " + example.sourceName());
            assertTrue(example.summary().toLowerCase(Locale.ROOT).contains("unidad educativa"),
                    "Resumen fuera de la familia UENS: " + example.id());
        }
    }

    @Test
    void keepsOneOfficialExamplePerVisibleDiagramType() {
        Set<DiagramTypeId> types = catalog.findAll().stream()
                .map(OfficialExampleDescriptor::diagramTypeId)
                .collect(Collectors.toSet());

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
        assertTrue(types.contains(DiagramTypeId.LOGICAL_BUSINESS_INTAKE));
        assertTrue(types.contains(DiagramTypeId.LOGICAL_BUSINESS_GRAPH));
    }

    @Test
    void officialExamplesExplainTheirOwnDomainFamily() {
        for (OfficialExampleDescriptor example : catalog.findAll()) {
            String summary = example.summary().toLowerCase(Locale.ROOT);
            if (isOpticaExample(example)) {
                assertTrue(
                        summary.contains("óptica horizonte") || summary.contains("optica horizonte"),
                        "El resumen debe aclarar la familia ficticia de óptica: " + example.id());
            } else {
                assertTrue(
                        summary.contains("unidad educativa"),
                        "El resumen debe aclarar que UENS es una unidad educativa: " + example.id());
                assertFalse(summary.contains("óptica horizonte") || summary.contains("optica horizonte"),
                        "Un ejemplo UENS no debe quedar prefijado como óptica: " + example.id());
            }
        }
    }

    @Test
    void dataDictionaryIsImportableAsEditableDocument() {
        OfficialExampleDescriptor dataDictionary = catalog.findByDiagramType(DiagramTypeId.DATA_DICTIONARY).get(0);

        assertTrue(dataDictionary.importable());
        assertTrue(dataDictionary.summary().contains("Diccionario editable"));
    }

    private static boolean isOpticaExample(OfficialExampleDescriptor example) {
        String searchable = (example.id() + " " + example.title() + " " + example.sourceName())
                .toLowerCase(Locale.ROOT);
        return searchable.contains("optica") || searchable.contains("óptica");
    }

    private static boolean isUensExample(OfficialExampleDescriptor example) {
        return !isOpticaExample(example);
    }
}
