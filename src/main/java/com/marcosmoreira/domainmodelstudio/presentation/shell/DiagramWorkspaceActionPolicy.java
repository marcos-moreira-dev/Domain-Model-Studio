package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;

/**
 * Política visible de acciones de workspace para el menú global.
 *
 * <p>Centraliza qué tipos tienen navegación de canvas y auto-layout para que
 * MainShellView no acumule reglas de capacidad por tipo.</p>
 */
final class DiagramWorkspaceActionPolicy {

    private DiagramWorkspaceActionPolicy() {
    }

    static boolean supportsVisualNavigation(DiagramTypeId typeId) {
        return DiagramTypeId.CONCEPTUAL_MODEL.equals(typeId)
                || DiagramTypeId.ADMIN_MODULE_MAP.equals(typeId)
                || DiagramTypeId.UML_CLASS.equals(typeId)
                || DiagramTypeId.SCREEN_FLOW.equals(typeId)
                || DiagramTypeId.ADMIN_WIREFRAMES.equals(typeId)
                || DiagramTypeId.FREE_GRAPH.equals(typeId)
                || DiagramTypeId.LOGICAL_BUSINESS_GRAPH.equals(typeId)
                || DiagramTypeId.BPMN_BASIC.equals(typeId)
                || DiagramTypeId.OPERATIONAL_FLOW.equals(typeId)
                || DiagramTypeId.UML_USE_CASE.equals(typeId)
                || DiagramTypeId.UML_ACTIVITY.equals(typeId)
                || DiagramTypeId.UML_SEQUENCE.equals(typeId)
                || DiagramTypeId.UML_STATE.equals(typeId)
                || DiagramTypeId.C4_CONTEXT.equals(typeId)
                || DiagramTypeId.C4_CONTAINERS.equals(typeId)
                || DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(typeId);
    }

    static boolean supportsAutoLayout(DiagramTypeId typeId) {
        return DiagramTypeId.CONCEPTUAL_MODEL.equals(typeId)
                || DiagramTypeId.ADMIN_MODULE_MAP.equals(typeId)
                || DiagramTypeId.UML_CLASS.equals(typeId)
                || DiagramTypeId.FREE_GRAPH.equals(typeId)
                || DiagramTypeId.LOGICAL_BUSINESS_GRAPH.equals(typeId)
                || DiagramTypeId.BPMN_BASIC.equals(typeId)
                || DiagramTypeId.OPERATIONAL_FLOW.equals(typeId)
                || DiagramTypeId.UML_USE_CASE.equals(typeId)
                || DiagramTypeId.UML_ACTIVITY.equals(typeId)
                || DiagramTypeId.UML_SEQUENCE.equals(typeId)
                || DiagramTypeId.UML_STATE.equals(typeId)
                || DiagramTypeId.C4_CONTEXT.equals(typeId)
                || DiagramTypeId.C4_CONTAINERS.equals(typeId);
    }
}
