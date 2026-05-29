package com.marcosmoreira.domainmodelstudio.application.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class DefaultDiagramTypeDefinitionsTest {

    @Test
    void sourceOfTruthShouldDeclareNineteenOfficialTypes() {
        Map<DiagramTypeId, DiagramTypeOfficialDefinition> definitions = definitionsById();

        assertEquals(19, definitions.size());
        assertTrue(definitions.containsKey(DiagramTypeId.LOGICAL_BUSINESS_INTAKE));
        assertTrue(definitions.containsKey(DiagramTypeId.LOGICAL_BUSINESS_GRAPH));
        assertTrue(definitions.containsKey(DiagramTypeId.CONCEPTUAL_MODEL));
        assertTrue(definitions.containsKey(DiagramTypeId.DATA_DICTIONARY));
        assertTrue(definitions.containsKey(DiagramTypeId.ADMIN_WIREFRAMES));
        assertTrue(definitions.containsKey(DiagramTypeId.FREE_GRAPH));
    }

    @Test
    void registryShouldExposeWorkspaceAndExampleMetadataFromDefinitions() {
        Map<DiagramTypeId, DiagramTypeOfficialDefinition> definitions = definitionsById();

        for (DiagramTypeDescriptor descriptor : new DefaultDiagramTypeRegistry().findAll()) {
            DiagramTypeOfficialDefinition definition = definitions.get(descriptor.id());

            assertEquals(definition.workspaceKind(), descriptor.workspaceKind(), descriptor.id().value());
            assertEquals(definition.toolbarPolicyId(), descriptor.toolbarPolicyId(), descriptor.id().value());
            assertEquals(definition.minimalExampleResource(), descriptor.minimalExampleResource(), descriptor.id().value());
            assertEquals(definition.officialExampleResource(), descriptor.officialExampleResource(), descriptor.id().value());
            if (descriptor.supportStatus() == DiagramSupportStatus.AVAILABLE) {
                assertFalse(descriptor.workspaceKind() == DiagramWorkspaceKind.PLACEHOLDER_GUIDE, descriptor.id().value());
            } else {
                assertEquals(DiagramWorkspaceKind.PLACEHOLDER_GUIDE, descriptor.workspaceKind(), descriptor.id().value());
            }
        }
    }

    @Test
    void everyImportableOfficialExampleMustBelongToAnImportableType() {
        for (DiagramTypeOfficialDefinition definition : DefaultDiagramTypeDefinitions.all()) {
            if (definition.officialExampleImportable()) {
                assertTrue(
                        definition.capabilities().has(DiagramCapability.IMPORT_MARKDOWN),
                        definition.id().value() + " marca ejemplo importable sin IMPORT_MARKDOWN.");
            }
        }
    }

    @Test
    void officialDefinitionIdsShouldMatchCatalogIds() {
        Set<DiagramTypeId> definitionIds = definitionsById().keySet();
        Set<DiagramTypeId> catalogIds = new DefaultDiagramTypeRegistry().findAll().stream()
                .map(DiagramTypeDescriptor::id)
                .collect(Collectors.toSet());

        assertEquals(definitionIds, catalogIds);
    }

    private static Map<DiagramTypeId, DiagramTypeOfficialDefinition> definitionsById() {
        return DefaultDiagramTypeDefinitions.all().stream()
                .collect(Collectors.toMap(DiagramTypeOfficialDefinition::id, definition -> definition));
    }
}
