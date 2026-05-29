package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.action;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.centerDiagramAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.fitToContentAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.reorganizeAction;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Acciones de toolbar para diagramas de comportamiento. */
final class BehaviorToolbarContributor implements DiagramToolbarContributor {

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.BPMN_BASIC.equals(diagramTypeId)
                || DiagramTypeId.OPERATIONAL_FLOW.equals(diagramTypeId)
                || DiagramTypeId.UML_USE_CASE.equals(diagramTypeId)
                || DiagramTypeId.UML_ACTIVITY.equals(diagramTypeId)
                || DiagramTypeId.UML_SEQUENCE.equals(diagramTypeId)
                || DiagramTypeId.UML_STATE.equals(diagramTypeId);
    }

    @Override
    public List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        if (diagramTypeId == null) {
            return List.of();
        }
        if (DiagramTypeId.BPMN_BASIC.equals(diagramTypeId)) {
            return bpmnActions();
        }
        if (DiagramTypeId.OPERATIONAL_FLOW.equals(diagramTypeId)) {
            return operationalFlowActions();
        }
        if (DiagramTypeId.UML_USE_CASE.equals(diagramTypeId)) {
            return useCaseActions();
        }
        if (DiagramTypeId.UML_ACTIVITY.equals(diagramTypeId)) {
            return umlActivityActions();
        }
        if (DiagramTypeId.UML_SEQUENCE.equals(diagramTypeId)) {
            return umlSequenceActions();
        }
        if (DiagramTypeId.UML_STATE.equals(diagramTypeId)) {
            return umlStateActions();
        }
        return List.of();
    }

    private List<DiagramToolbarAction> bpmnActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_BPMN_START, "Inicio", "Agregar evento de inicio", ToolbarIcon.ADD_BPMN_START, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_BPMN_ACTIVITY, "Actividad", "Agregar actividad del proceso", ToolbarIcon.ADD_BPMN_ACTIVITY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_BPMN_DECISION, "Decisión", "Agregar decisión o compuerta", ToolbarIcon.ADD_BPMN_DECISION, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_BPMN_END, "Fin", "Agregar evento final", ToolbarIcon.ADD_BPMN_END, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_BPMN_LANE, "Carril", "Agregar carril/responsable", ToolbarIcon.ADD_LANE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_BEHAVIOR_FLOW, "Flujo", "Agregar flujo entre elementos", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.REMOVE_BEHAVIOR_ITEM, "Eliminar", "Eliminar elemento o relación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_BEHAVIOR_DIAGRAM, "Validar", "Revisar consistencia del proceso", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Ordenar tareas, compuertas y carriles según el flujo BPMN básico"),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar BPMN básico como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar BPMN básico como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar BPMN básico como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> operationalFlowActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_BPMN_ACTIVITY, "Paso", "Agregar paso operativo", ToolbarIcon.ADD_BPMN_ACTIVITY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_BPMN_LANE, "Responsable", "Agregar responsable o área", ToolbarIcon.ADD_LANE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_BPMN_DECISION, "Decisión", "Agregar decisión operativa", ToolbarIcon.ADD_BPMN_DECISION, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_BEHAVIOR_NOTE, "Documento", "Agregar documento o evidencia", ToolbarIcon.ADD_MESSAGE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_BEHAVIOR_FLOW, "Conexión", "Conectar pasos operativos", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.REMOVE_BEHAVIOR_ITEM, "Eliminar", "Eliminar elemento o relación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_BEHAVIOR_DIAGRAM, "Validar", "Revisar flujo operativo", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Ordenar pasos, responsables y evidencias según la secuencia operativa"),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar flujo operativo como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar flujo operativo como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar flujo operativo como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> useCaseActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_USE_CASE_ACTOR, "Actor", "Agregar actor externo", ToolbarIcon.ADD_ACTOR, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_USE_CASE, "Caso de uso", "Agregar caso de uso", ToolbarIcon.ADD_USE_CASE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_USE_CASE_SYSTEM, "Límite", "Agregar límite del sistema", ToolbarIcon.ADD_SYSTEM_BOUNDARY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_USE_CASE_ASSOCIATION, "Asociación", "Agregar asociación", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_USE_CASE_INCLUDE, "Include", "Agregar relación include", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_USE_CASE_EXTEND, "Extend", "Agregar relación extend", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_USE_CASE_GENERALIZATION, "Generalización", "Agregar generalización", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.REMOVE_BEHAVIOR_ITEM, "Eliminar", "Eliminar elemento o relación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_BEHAVIOR_DIAGRAM, "Validar", "Revisar actores, casos de uso y relaciones", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Ubicar actores fuera, casos dentro del límite y relaciones legibles"),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar casos de uso como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar casos de uso como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar casos de uso como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> umlActivityActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_UML_INITIAL_STATE, "Inicio", "Agregar inicio", ToolbarIcon.ADD_BPMN_START, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_ACTION, "Acción", "Agregar acción", ToolbarIcon.ADD_BPMN_ACTIVITY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_DECISION, "Decisión", "Agregar decisión", ToolbarIcon.ADD_BPMN_DECISION, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_FINAL_STATE, "Final", "Agregar final", ToolbarIcon.ADD_BPMN_END, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_BPMN_LANE, "Carril", "Agregar carril o responsable", ToolbarIcon.ADD_LANE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_BEHAVIOR_FLOW, "Flujo", "Agregar flujo", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.REMOVE_BEHAVIOR_ITEM, "Eliminar", "Eliminar elemento o relación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_BEHAVIOR_DIAGRAM, "Validar", "Revisar flujo de actividad", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Ordenar actividad como flujo de acciones, decisiones, carriles y finales"),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar actividad UML como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar actividad UML como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar actividad UML como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> umlSequenceActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_SEQUENCE_PARTICIPANT, "Participante", "Agregar participante", ToolbarIcon.ADD_ACTOR, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_SEQUENCE_ACTIVATION, "Activación", "Agregar activación", ToolbarIcon.ADD_BPMN_ACTIVITY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_SEQUENCE_FRAGMENT, "Fragmento", "Agregar fragmento alt/opt/loop/par/break/critical/ref", ToolbarIcon.ADD_SYSTEM_BOUNDARY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_SEQUENCE_MESSAGE, "Mensaje", "Agregar mensaje", ToolbarIcon.ADD_MESSAGE, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_SEQUENCE_RETURN_MESSAGE, "Respuesta", "Agregar respuesta", ToolbarIcon.ADD_MESSAGE, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.REMOVE_BEHAVIOR_ITEM, "Eliminar", "Eliminar elemento o relación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_BEHAVIOR_DIAGRAM, "Validar", "Revisar participantes y mensajes", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Reajustar participantes, líneas de vida y mensajes temporales"), fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar secuencia UML como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar secuencia UML como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar secuencia UML como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> umlStateActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_UML_INITIAL_STATE, "Inicio", "Agregar estado inicial", ToolbarIcon.ADD_BPMN_START, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_STATE, "Estado", "Agregar estado", ToolbarIcon.ADD_STATE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_FINAL_STATE, "Final", "Agregar estado final", ToolbarIcon.ADD_BPMN_END, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_STATE_TRANSITION, "Transición", "Agregar transición", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.REMOVE_BEHAVIOR_ITEM, "Eliminar", "Eliminar elemento o relación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_BEHAVIOR_DIAGRAM, "Validar", "Revisar estados y transiciones", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Ordenar estados como ciclo de vida de inicio a fin"),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar estados UML como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar estados UML como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar estados UML como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

}
