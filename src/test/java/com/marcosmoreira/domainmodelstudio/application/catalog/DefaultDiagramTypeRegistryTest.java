package com.marcosmoreira.domainmodelstudio.application.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import org.junit.jupiter.api.Test;

class DefaultDiagramTypeRegistryTest {

    @Test
    void shouldExposeOfficialDiagramTypesInStableOrder() {
        DiagramTypeRegistry registry = new DefaultDiagramTypeRegistry();

        List<DiagramTypeDescriptor> types = registry.findAll();

        assertEquals(19, types.size());
        assertEquals(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, types.get(0).id());
        assertEquals(DiagramTypeId.LOGICAL_BUSINESS_GRAPH, types.get(1).id());
        assertEquals(DiagramTypeId.CONCEPTUAL_MODEL, types.get(2).id());
        assertEquals(DiagramTypeId.DATA_DICTIONARY, types.get(3).id());
        assertEquals(DiagramTypeId.BPMN_BASIC, types.get(4).id());
        assertEquals(DiagramTypeId.OPERATIONAL_FLOW, types.get(5).id());
        assertEquals(DiagramTypeId.C4_CONTEXT, types.get(6).id());
        assertEquals(DiagramTypeId.C4_CONTAINERS, types.get(7).id());
        assertEquals(DiagramTypeId.TECHNICAL_DEPLOYMENT, types.get(8).id());
        assertEquals(DiagramTypeId.UML_CLASS, types.get(9).id());
        assertEquals(DiagramTypeId.UML_USE_CASE, types.get(10).id());
        assertEquals(DiagramTypeId.UML_ACTIVITY, types.get(11).id());
        assertEquals(DiagramTypeId.UML_SEQUENCE, types.get(12).id());
        assertEquals(DiagramTypeId.UML_STATE, types.get(13).id());
        assertEquals(DiagramTypeId.ADMIN_MODULE_MAP, types.get(14).id());
        assertEquals(DiagramTypeId.ROLES_PERMISSIONS_MAP, types.get(15).id());
        assertEquals(DiagramTypeId.SCREEN_FLOW, types.get(16).id());
        assertEquals(DiagramTypeId.ADMIN_WIREFRAMES, types.get(17).id());
        assertEquals(DiagramTypeId.FREE_GRAPH, types.get(18).id());
    }

    @Test
    void shouldExposeHonestProductStatus() {
        DiagramTypeRegistry registry = new DefaultDiagramTypeRegistry();

        DiagramTypeDescriptor logicalBusiness = registry.findById(DiagramTypeId.LOGICAL_BUSINESS_INTAKE).orElseThrow();
        DiagramTypeDescriptor logicalBusinessGraph = registry.findById(DiagramTypeId.LOGICAL_BUSINESS_GRAPH).orElseThrow();
        DiagramTypeDescriptor conceptualModel = registry.findById(DiagramTypeId.CONCEPTUAL_MODEL).orElseThrow();
        DiagramTypeDescriptor dataDictionary = registry.findById(DiagramTypeId.DATA_DICTIONARY).orElseThrow();
        DiagramTypeDescriptor moduleMap = registry.findById(DiagramTypeId.ADMIN_MODULE_MAP).orElseThrow();
        DiagramTypeDescriptor rolesPermissions = registry.findById(DiagramTypeId.ROLES_PERMISSIONS_MAP).orElseThrow();
        DiagramTypeDescriptor screenFlow = registry.findById(DiagramTypeId.SCREEN_FLOW).orElseThrow();
        DiagramTypeDescriptor wireframes = registry.findById(DiagramTypeId.ADMIN_WIREFRAMES).orElseThrow();
        DiagramTypeDescriptor umlClass = registry.findById(DiagramTypeId.UML_CLASS).orElseThrow();
        DiagramTypeDescriptor c4Context = registry.findById(DiagramTypeId.C4_CONTEXT).orElseThrow();
        DiagramTypeDescriptor c4Containers = registry.findById(DiagramTypeId.C4_CONTAINERS).orElseThrow();
        DiagramTypeDescriptor technicalDeployment = registry.findById(DiagramTypeId.TECHNICAL_DEPLOYMENT).orElseThrow();
        DiagramTypeDescriptor freeGraph = registry.findById(DiagramTypeId.FREE_GRAPH).orElseThrow();

        assertEquals(DiagramSupportStatus.AVAILABLE, logicalBusiness.supportStatus());
        assertTrue(logicalBusiness.isAvailable());
        assertTrue(logicalBusiness.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(logicalBusiness.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT));
        assertTrue(logicalBusiness.supports(DiagramCapability.MANUAL_EDITING));
        assertTrue(logicalBusiness.supports(DiagramCapability.SAVE_DMS));
        assertTrue(logicalBusiness.supports(DiagramCapability.LOAD_DMS));
        assertTrue(logicalBusiness.supports(DiagramCapability.AI_RESOURCES));
        assertTrue(logicalBusiness.supports(DiagramCapability.THEORY_HELP));
        assertFalse(logicalBusiness.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(logicalBusiness.supports(DiagramCapability.IMPORT_MARKDOWN));

        assertEquals(DiagramSupportStatus.AVAILABLE, logicalBusinessGraph.supportStatus());
        assertFalse(logicalBusinessGraph.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.EXPORT_SVG));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.SAVE_DMS));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.LOAD_DMS));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.AI_RESOURCES));
        assertTrue(logicalBusinessGraph.supports(DiagramCapability.THEORY_HELP));
        assertTrue(logicalBusinessGraph.isAvailable());

        assertEquals(DiagramSupportStatus.AVAILABLE, conceptualModel.supportStatus());
        assertTrue(conceptualModel.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, dataDictionary.supportStatus());
        assertTrue(dataDictionary.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, moduleMap.supportStatus());
        assertTrue(moduleMap.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, rolesPermissions.supportStatus());
        assertTrue(rolesPermissions.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, screenFlow.supportStatus());
        assertTrue(screenFlow.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, wireframes.supportStatus());
        assertTrue(wireframes.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, umlClass.supportStatus());
        assertTrue(umlClass.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, c4Context.supportStatus());
        assertTrue(c4Context.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, c4Containers.supportStatus());
        assertTrue(c4Containers.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, technicalDeployment.supportStatus());
        assertTrue(technicalDeployment.isAvailable());
        assertEquals(DiagramSupportStatus.AVAILABLE, freeGraph.supportStatus());
        assertTrue(freeGraph.isAvailable());
    }

    @Test
    void shouldFindTypesByCategory() {
        DiagramTypeRegistry registry = new DefaultDiagramTypeRegistry();

        List<DiagramTypeDescriptor> analysisTypes = registry.findByCategory(DiagramCategoryId.BUSINESS_ANALYSIS);
        List<DiagramTypeDescriptor> dataTypes = registry.findByCategory(DiagramCategoryId.DATA_MODELING);
        List<DiagramTypeDescriptor> adminTypes = registry.findByCategory(DiagramCategoryId.ADMIN_APPLICATIONS);
        List<DiagramTypeDescriptor> technicalDocumentationTypes = registry.findByCategory(DiagramCategoryId.TECHNICAL_DOCUMENTATION);

        assertEquals(2, analysisTypes.size());
        assertEquals(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, analysisTypes.get(0).id());
        assertEquals(DiagramTypeId.LOGICAL_BUSINESS_GRAPH, analysisTypes.get(1).id());
        assertEquals(2, dataTypes.size());
        assertEquals(4, adminTypes.size());
        assertEquals(1, technicalDocumentationTypes.size());
        assertTrue(dataTypes.stream().noneMatch(type -> type.displayName().contains("ERD")));
        assertTrue(dataTypes.stream().noneMatch(type -> type.id().value().equals("logical-relational-model")));
        assertTrue(adminTypes.stream().anyMatch(type -> type.id().equals(DiagramTypeId.ADMIN_WIREFRAMES)));
        assertEquals(DiagramTypeId.FREE_GRAPH, technicalDocumentationTypes.get(0).id());
    }

    @Test
    void shouldReturnEmptyResultForUnknownType() {
        DiagramTypeRegistry registry = new DefaultDiagramTypeRegistry();

        assertTrue(registry.findById(DiagramTypeId.CONCEPTUAL_MODEL).isPresent());
        assertFalse(registry.findById(DiagramTypeId.of("unknown-type")).isPresent());
        assertFalse(registry.findById(DiagramTypeId.of("postgresql-physical-erd")).isPresent());
        assertFalse(registry.findById(DiagramTypeId.of("logical-relational-model")).isPresent());
    }

    @Test
    void shouldExposeCapabilitiesInsideTypeDescriptors() {
        DiagramTypeRegistry registry = new DefaultDiagramTypeRegistry();

        DiagramTypeDescriptor logicalBusiness = registry.findById(DiagramTypeId.LOGICAL_BUSINESS_INTAKE).orElseThrow();
        DiagramTypeDescriptor logicalBusinessGraph = registry.findById(DiagramTypeId.LOGICAL_BUSINESS_GRAPH).orElseThrow();
        DiagramTypeDescriptor conceptualModel = registry.findById(DiagramTypeId.CONCEPTUAL_MODEL).orElseThrow();
        DiagramTypeDescriptor dataDictionary = registry.findById(DiagramTypeId.DATA_DICTIONARY).orElseThrow();
        DiagramTypeDescriptor moduleMap = registry.findById(DiagramTypeId.ADMIN_MODULE_MAP).orElseThrow();
        DiagramTypeDescriptor rolesPermissions = registry.findById(DiagramTypeId.ROLES_PERMISSIONS_MAP).orElseThrow();
        DiagramTypeDescriptor screenFlow = registry.findById(DiagramTypeId.SCREEN_FLOW).orElseThrow();
        DiagramTypeDescriptor umlClass = registry.findById(DiagramTypeId.UML_CLASS).orElseThrow();
        DiagramTypeDescriptor c4Context = registry.findById(DiagramTypeId.C4_CONTEXT).orElseThrow();
        DiagramTypeDescriptor c4Containers = registry.findById(DiagramTypeId.C4_CONTAINERS).orElseThrow();
        DiagramTypeDescriptor technicalDeployment = registry.findById(DiagramTypeId.TECHNICAL_DEPLOYMENT).orElseThrow();
        DiagramTypeDescriptor freeGraph = registry.findById(DiagramTypeId.FREE_GRAPH).orElseThrow();

        assertTrue(logicalBusiness.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(logicalBusiness.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT));
        assertTrue(logicalBusiness.supports(DiagramCapability.MANUAL_EDITING));
        assertTrue(logicalBusiness.supports(DiagramCapability.SAVE_DMS));
        assertTrue(logicalBusiness.supports(DiagramCapability.LOAD_DMS));
        assertTrue(logicalBusiness.supports(DiagramCapability.AI_RESOURCES));
        assertTrue(logicalBusiness.supports(DiagramCapability.THEORY_HELP));
        assertFalse(logicalBusiness.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(logicalBusiness.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(logicalBusiness.supports(DiagramCapability.EXPORT_PDF));
        assertTrue(logicalBusiness.supports(DiagramCapability.EXPORT_MARKDOWN));

        assertTrue(conceptualModel.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(conceptualModel.supports(DiagramCapability.EXPORT_SVG));
        assertTrue(conceptualModel.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertFalse(conceptualModel.supports(DiagramCapability.PLANNING_VIEW));

        assertTrue(dataDictionary.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT));
        assertTrue(dataDictionary.supports(DiagramCapability.MANUAL_EDITING));
        assertTrue(dataDictionary.supports(DiagramCapability.EXPORT_PDF));
        assertTrue(dataDictionary.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(dataDictionary.supports(DiagramCapability.AI_RESOURCES));
        assertFalse(dataDictionary.supports(DiagramCapability.PLANNING_VIEW));

        assertFalse(moduleMap.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(moduleMap.supports(DiagramCapability.AI_RESOURCES));
        assertTrue(moduleMap.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(moduleMap.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(moduleMap.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(moduleMap.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(moduleMap.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(moduleMap.supports(DiagramCapability.SAVE_DMS));

        assertFalse(rolesPermissions.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(rolesPermissions.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(rolesPermissions.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertFalse(rolesPermissions.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(rolesPermissions.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT));
        assertTrue(rolesPermissions.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(rolesPermissions.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(rolesPermissions.supports(DiagramCapability.SAVE_DMS));

        assertFalse(screenFlow.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(screenFlow.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(screenFlow.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(screenFlow.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(screenFlow.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(screenFlow.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(screenFlow.supports(DiagramCapability.SAVE_DMS));

        assertFalse(umlClass.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(umlClass.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(umlClass.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(umlClass.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(umlClass.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(umlClass.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(umlClass.supports(DiagramCapability.SAVE_DMS));

        assertFalse(c4Context.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(c4Context.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(c4Context.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(c4Context.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(c4Context.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(c4Context.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(c4Context.supports(DiagramCapability.SAVE_DMS));

        assertFalse(c4Containers.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(c4Containers.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(c4Containers.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(c4Containers.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(c4Containers.supports(DiagramCapability.EXPORT_MARKDOWN));

        assertFalse(technicalDeployment.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(technicalDeployment.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(technicalDeployment.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(technicalDeployment.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(technicalDeployment.supports(DiagramCapability.EXPORT_MARKDOWN));

        assertFalse(freeGraph.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(freeGraph.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(freeGraph.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(freeGraph.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(freeGraph.supports(DiagramCapability.MANUAL_EDITING));
        assertTrue(freeGraph.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(freeGraph.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(freeGraph.supports(DiagramCapability.SAVE_DMS));
        assertTrue(freeGraph.supports(DiagramCapability.LOAD_DMS));
    }

    @Test
    void officialCatalogShouldRejectFalseAvailabilityCombinations() {
        DiagramTypeRegistry registry = new DefaultDiagramTypeRegistry();

        for (DiagramTypeDescriptor type : registry.findAll()) {
            if (type.supportStatus() == DiagramSupportStatus.AVAILABLE) {
                assertTrue(type.supports(DiagramCapability.SHOW_VISUAL_OUTPUT)
                        || type.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT));
                assertTrue(type.supports(DiagramCapability.EXPORT_PNG)
                        || type.supports(DiagramCapability.EXPORT_SVG)
                        || type.supports(DiagramCapability.EXPORT_PDF)
                        || type.supports(DiagramCapability.EXPORT_MARKDOWN));
                assertFalse(type.supports(DiagramCapability.PLANNING_VIEW));
            }
            if (type.supportStatus() == DiagramSupportStatus.IN_PREPARATION) {
                assertTrue(type.supports(DiagramCapability.PLANNING_VIEW));
                assertFalse(type.supports(DiagramCapability.CREATE_PROJECT));
                assertFalse(type.supports(DiagramCapability.IMPORT_MARKDOWN));
                assertFalse(type.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
                assertFalse(type.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT));
                assertFalse(type.supports(DiagramCapability.MANUAL_EDITING));
                assertFalse(type.supports(DiagramCapability.EXPORT_PNG));
                assertFalse(type.supports(DiagramCapability.EXPORT_SVG));
                assertFalse(type.supports(DiagramCapability.EXPORT_PDF));
                assertFalse(type.supports(DiagramCapability.EXPORT_MARKDOWN));
                assertFalse(type.supports(DiagramCapability.SAVE_DMS));
                assertFalse(type.supports(DiagramCapability.LOAD_DMS));
                assertFalse(type.supports(DiagramCapability.BATCH_EXPORT));
            }
        }
    }

    @Test
    void shouldRejectDuplicateTypeIds() {
        DiagramTypeDescriptor type = new DiagramTypeDescriptor(
                DiagramTypeId.CONCEPTUAL_MODEL,
                "Modelo conceptual",
                DiagramCategoryId.DATA_MODELING,
                DiagramSupportStatus.AVAILABLE,
                DiagramCapabilitySet.empty(),
                "Descripción de prueba.",
                "modelo-conceptual",
                "modelo-conceptual-gramatica");

        assertThrows(IllegalArgumentException.class,
                () -> new DefaultDiagramTypeRegistry(List.of(type, type)));
    }
}
