package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import org.junit.jupiter.api.Test;

/** Guardarraíl: ninguna acción visible de toolbar contextual puede quedar sin despacho. */
class DiagramToolbarActionExecutorCoverageTest {

    private final DefaultDiagramToolbarActionProvider provider = new DefaultDiagramToolbarActionProvider();

    @Test
    void everyVisibleContextualToolbarActionShouldHaveExecutorBinding() {
        for (DiagramTypeOfficialDefinition definition : DefaultDiagramTypeDefinitions.all()) {
            for (DiagramToolbarAction action : provider.actionsFor(definition.id())) {
                assertTrue(
                        DiagramToolbarActionExecutor.isHandled(action.id()),
                        () -> "Acción visible sin handler: " + action.id() + " en " + definition.id().value());
            }
        }
    }

    @Test
    void everyDeclaredActionIdShouldRemainHandledByDesign() {
        for (DiagramToolbarActionId actionId : DiagramToolbarActionId.values()) {
            if (actionId == DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_DERIVATIONS) {
                continue; // acción legacy retirada de la UI principal del Levantamiento lógico.
            }
            assertTrue(
                    DiagramToolbarActionExecutor.isHandled(actionId),
                    () -> "Acción declarada sin handler: " + actionId);
        }
    }
}
