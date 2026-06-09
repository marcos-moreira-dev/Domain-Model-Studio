package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class VisualCommentToolbarTest {

    private final DefaultDiagramToolbarActionProvider provider = new DefaultDiagramToolbarActionProvider();

    @Test
    void shouldExposeVisualCommentOnlyOnEditableCanvasWorkspaces() {
        for (DiagramTypeId typeId : Set.of(
                DiagramTypeId.ADMIN_MODULE_MAP,
                DiagramTypeId.UML_CLASS,
                DiagramTypeId.SCREEN_FLOW,
                DiagramTypeId.ADMIN_WIREFRAMES,
                DiagramTypeId.BPMN_BASIC,
                DiagramTypeId.UML_SEQUENCE,
                DiagramTypeId.C4_CONTEXT,
                DiagramTypeId.FREE_GRAPH,
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH)) {
            assertTrue(actionIds(typeId).contains(DiagramToolbarActionId.ADD_VISUAL_COMMENT),
                    typeId + " debe exponer Comentario en toolbar contextual.");
        }

        for (DiagramTypeId typeId : Set.of(
                DiagramTypeId.CONCEPTUAL_MODEL,
                DiagramTypeId.DATA_DICTIONARY,
                DiagramTypeId.ROLES_PERMISSIONS_MAP,
                DiagramTypeId.LOGICAL_BUSINESS_INTAKE)) {
            assertFalse(actionIds(typeId).contains(DiagramToolbarActionId.ADD_VISUAL_COMMENT),
                    typeId + " no debe exponer Comentario visual.");
        }
    }

    private Set<DiagramToolbarActionId> actionIds(DiagramTypeId typeId) {
        return provider.actionsFor(typeId).stream()
                .map(DiagramToolbarAction::id)
                .collect(Collectors.toSet());
    }
}
