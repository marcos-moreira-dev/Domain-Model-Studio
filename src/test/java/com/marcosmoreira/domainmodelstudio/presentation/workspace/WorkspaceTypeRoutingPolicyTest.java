package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Guardarraíles del contrato tipo visible -> familia de workspace. */
class WorkspaceTypeRoutingPolicyTest {

    private final WorkspaceTypeRoutingPolicy routingPolicy = new WorkspaceTypeRoutingPolicy();

    @Test
    void everyAvailableOutputTypeMustHaveNonPlaceholderWorkspaceKind() {
        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            boolean outputType = type.supports(DiagramCapability.SHOW_VISUAL_OUTPUT)
                    || type.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT);
            if (type.isAvailable() && outputType) {
                assertFalse(
                        WorkspaceKind.PLACEHOLDER_GUIDE == routingPolicy.kindOf(type.id()),
                        type.id().value() + " no debe caer en guía si declara salida real.");
            }
        }
    }

    @Test
    void behaviorDiagramFamilyMustRouteToBehaviorWorkspace() {
        Set<DiagramTypeId> behaviorTypes = Set.of(
                DiagramTypeId.BPMN_BASIC,
                DiagramTypeId.OPERATIONAL_FLOW,
                DiagramTypeId.UML_USE_CASE,
                DiagramTypeId.UML_ACTIVITY,
                DiagramTypeId.UML_SEQUENCE,
                DiagramTypeId.UML_STATE
        );
        for (DiagramTypeId typeId : behaviorTypes) {
            assertEquals(WorkspaceKind.BEHAVIOR_DIAGRAM, routingPolicy.kindOf(typeId), typeId.value());
            assertTrue(routingPolicy.isBehaviorDiagram(typeId), typeId.value());
        }
    }

    @Test
    void conceptualModelUsesCommonWorkspaceRouteAfterDedicatedMigration() {
        assertEquals(WorkspaceKind.CONCEPTUAL_CANVAS, routingPolicy.kindOf(DiagramTypeId.CONCEPTUAL_MODEL));
        assertFalse(routingPolicy.usesGenericConceptualSidePanels(DiagramTypeId.CONCEPTUAL_MODEL));
        assertTrue(routingPolicy.usesSpecializedWorkspace(DiagramTypeId.CONCEPTUAL_MODEL));
    }

    @Test
    void specializedEditorsMustNotUseConceptualSidePanelsByDefault() {
        Set<DiagramTypeId> specializedTypes = Set.of(
                DiagramTypeId.CONCEPTUAL_MODEL,
                DiagramTypeId.DATA_DICTIONARY,
                DiagramTypeId.LOGICAL_BUSINESS_INTAKE,
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH,
                DiagramTypeId.ADMIN_MODULE_MAP,
                DiagramTypeId.UML_CLASS,
                DiagramTypeId.ROLES_PERMISSIONS_MAP,
                DiagramTypeId.SCREEN_FLOW,
                DiagramTypeId.ADMIN_WIREFRAMES,
                DiagramTypeId.C4_CONTEXT,
                DiagramTypeId.C4_CONTAINERS,
                DiagramTypeId.TECHNICAL_DEPLOYMENT,
                DiagramTypeId.BPMN_BASIC,
                DiagramTypeId.OPERATIONAL_FLOW,
                DiagramTypeId.UML_USE_CASE,
                DiagramTypeId.UML_ACTIVITY,
                DiagramTypeId.UML_SEQUENCE,
                DiagramTypeId.UML_STATE
        );
        for (DiagramTypeId typeId : specializedTypes) {
            assertFalse(routingPolicy.usesGenericConceptualSidePanels(typeId), typeId.value());
            assertTrue(routingPolicy.usesSpecializedWorkspace(typeId), typeId.value());
        }
    }
}
