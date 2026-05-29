package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreenTemplateKind;
import com.marcosmoreira.domainmodelstudio.presentation.shell.MainShellCommandHandler;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Punto único de despacho para acciones contextuales de toolbar.
 *
 * <p>La vista solo conoce identificadores visibles. Esta clase garantiza que cada
 * acción que aparece en una barra contextual tenga un comando asociado, evitando
 * botones huérfanos o handlers silenciosos repartidos por la interfaz.</p>
 */
public final class DiagramToolbarActionExecutor {

    private final MainShellCommandHandler commandHandler;
    private final Supplier<WireframeScreenTemplateKind> wireframeTemplateSupplier;

    public DiagramToolbarActionExecutor(
            MainShellCommandHandler commandHandler,
            Supplier<WireframeScreenTemplateKind> wireframeTemplateSupplier
    ) {
        this.commandHandler = Objects.requireNonNull(commandHandler, "commandHandler");
        this.wireframeTemplateSupplier = Objects.requireNonNull(wireframeTemplateSupplier, "wireframeTemplateSupplier");
    }

    public static boolean isHandled(DiagramToolbarActionId actionId) {
        if (actionId == null) {
            return false;
        }
        return switch (actionId) {
            case LOGICAL_BUSINESS_SHOW_DERIVATIONS -> false;
            case ADD_ENTITY,
                    ADD_ATTRIBUTE,
                    ADD_RELATIONSHIP,
                    DUPLICATE_ELEMENT,
                    DELETE_ELEMENT,
                    DELETE_SELECTED_BEND_POINT,
                    ADD_DICTIONARY_ENTITY,
                    ADD_DICTIONARY_FIELD,
                    REMOVE_DICTIONARY_ITEM,
                    VALIDATE_DICTIONARY,
                    EXPORT_DICTIONARY_PDF,
                    ADD_MODULE,
                    ADD_SUBMODULE,
                    ADD_MODULE_DEPENDENCY,
                    REMOVE_MODULE_MAP_ITEM,
                    VALIDATE_MODULE_MAP,
                    ADD_UML_MODULE,
                    ADD_UML_CLASS,
                    ADD_UML_INTERFACE,
                    ADD_UML_ENUM,
                    ADD_UML_ATTRIBUTE,
                    ADD_UML_METHOD,
                    ADD_UML_RELATION,
                    IMPORT_UML_FROM_SOURCE,
                    OPEN_UML_SOURCE,
                    REMOVE_UML_ITEM,
                    VALIDATE_UML_CLASS,
                    ADD_ROLE,
                    ADD_PERMISSION,
                    ADD_PERMISSION_ASSIGNMENT,
                    REMOVE_ROLES_PERMISSIONS_ITEM,
                    VALIDATE_ROLES_PERMISSIONS,
                    ADD_SCREEN,
                    ADD_SCREEN_TRANSITION,
                    REMOVE_SCREEN_FLOW_ITEM,
                    VALIDATE_SCREEN_FLOW,
                    ADD_WIREFRAME_SCREEN,
                    ADD_WIREFRAME_SECTION,
                    ADD_WIREFRAME_FORM,
                    ADD_WIREFRAME_TABLE,
                    ADD_WIREFRAME_FIELD,
                    ADD_WIREFRAME_BUTTON,
                    APPLY_WIREFRAME_TEMPLATE,
                    REMOVE_WIREFRAME_ITEM,
                    VALIDATE_WIREFRAME,
                    ADD_BPMN_START,
                    ADD_BPMN_ACTIVITY,
                    ADD_BPMN_DECISION,
                    ADD_BPMN_END,
                    ADD_BPMN_LANE,
                    ADD_BEHAVIOR_FLOW,
                    ADD_BEHAVIOR_NOTE,
                    ADD_USE_CASE_ACTOR,
                    ADD_USE_CASE,
                    ADD_USE_CASE_SYSTEM,
                    ADD_USE_CASE_ASSOCIATION,
                    ADD_USE_CASE_INCLUDE,
                    ADD_USE_CASE_EXTEND,
                    ADD_USE_CASE_GENERALIZATION,
                    ADD_UML_ACTION,
                    ADD_UML_DECISION,
                    ADD_UML_INITIAL_STATE,
                    ADD_UML_FINAL_STATE,
                    ADD_SEQUENCE_PARTICIPANT,
                    ADD_SEQUENCE_ACTIVATION,
                    ADD_SEQUENCE_FRAGMENT,
                    ADD_SEQUENCE_MESSAGE,
                    ADD_SEQUENCE_RETURN_MESSAGE,
                    ADD_STATE,
                    ADD_STATE_TRANSITION,
                    REMOVE_BEHAVIOR_ITEM,
                    VALIDATE_BEHAVIOR_DIAGRAM,
                    ADD_C4_PERSON,
                    ADD_C4_SYSTEM,
                    ADD_C4_EXTERNAL_SYSTEM,
                    ADD_C4_BOUNDARY,
                    ADD_C4_CONTAINER,
                    ADD_C4_APPLICATION,
                    ADD_C4_API,
                    ADD_ARCHITECTURE_DATABASE,
                    ADD_ARCHITECTURE_EXTERNAL_SERVICE,
                    ADD_DEPLOYMENT_ENVIRONMENT,
                    ADD_DEPLOYMENT_SERVER,
                    ADD_DEPLOYMENT_CLIENT,
                    ADD_DEPLOYMENT_SERVICE,
                    ADD_DEPLOYMENT_NETWORK,
                    ADD_DEPLOYMENT_ARTIFACT,
                    ADD_ARCHITECTURE_USES,
                    ADD_ARCHITECTURE_DEPENDENCY,
                    ADD_ARCHITECTURE_INTEGRATION,
                    ADD_ARCHITECTURE_CALL,
                    ADD_ARCHITECTURE_READS_WRITES,
                    ADD_DEPLOYMENT_CONNECTION,
                    ADD_DEPLOYMENT_HOSTING,
                    ADD_DEPLOYMENT_TARGET,
                    REMOVE_ARCHITECTURE_ITEM,
                    VALIDATE_ARCHITECTURE_DIAGRAM,
                    FREE_GRAPH_SELECT_TOOL,
                    FREE_GRAPH_ADD_NODE_TOOL,
                    FREE_GRAPH_ADD_EDGE_TOOL,
                    ADD_FREE_GRAPH_NODE,
                    ADD_FREE_GRAPH_EDGE,
                    REMOVE_FREE_GRAPH_ITEM,
                    VALIDATE_FREE_GRAPH,
                    VALIDATE_MODEL,
                    REORGANIZE_DIAGRAM,
                    SWITCH_TO_CHEN,
                    SWITCH_TO_CROWS_FOOT,
                    ZOOM_IN,
                    ZOOM_OUT,
                    RESET_ZOOM,
                    FIT_TO_CONTENT,
                    CENTER_DIAGRAM,
                    CENTER_SELECTION,
                    BRING_SELECTION_TO_FRONT,
                    SEND_SELECTION_TO_BACK,
                    RAISE_SELECTION_LAYER,
                    LOWER_SELECTION_LAYER,
                    GROW_SELECTED_FIGURE,
                    SHRINK_SELECTED_FIGURE,
                    TRANSFER_VISUAL_SELECTION,
                    LOGICAL_BUSINESS_SHOW_STRUCTURE,
                    LOGICAL_BUSINESS_SHOW_PROPERTIES,
                    LOGICAL_BUSINESS_SHOW_VALIDATION,
                    LOGICAL_BUSINESS_SHOW_TRACEABILITY,
                    LOGICAL_BUSINESS_SHOW_HELP,
                    EXPORT_SVG,
                    EXPORT_MARKDOWN,
                    EXPORT_PNG -> true;
        };
    }

    public boolean canExecute(DiagramToolbarActionId actionId) {
        return isHandled(actionId);
    }

    public void execute(DiagramToolbarActionId actionId) {
        Objects.requireNonNull(actionId, "actionId");
        switch (actionId) {
            case ADD_ENTITY -> commandHandler.requestAddEntityTool();
            case ADD_ATTRIBUTE -> commandHandler.requestAddAttributeToSelectedEntity();
            case ADD_RELATIONSHIP -> commandHandler.requestAddRelationshipTool();
            case DUPLICATE_ELEMENT -> commandHandler.requestDuplicateSelectedEntity();
            case DELETE_ELEMENT -> commandHandler.requestRemoveSelectedElement();
            case DELETE_SELECTED_BEND_POINT -> commandHandler.requestDeleteSelectedBendPoint();
            case ADD_DICTIONARY_ENTITY -> commandHandler.requestAddDataDictionaryEntity();
            case ADD_DICTIONARY_FIELD -> commandHandler.requestAddDataDictionaryField();
            case REMOVE_DICTIONARY_ITEM -> commandHandler.requestRemoveDataDictionaryItem();
            case VALIDATE_DICTIONARY -> commandHandler.requestValidateDataDictionary();
            case EXPORT_DICTIONARY_PDF -> commandHandler.requestExportDataDictionaryPdf();
            case ADD_MODULE -> commandHandler.requestAddModuleMapModule();
            case ADD_SUBMODULE -> commandHandler.requestAddModuleMapSubmodule();
            case ADD_MODULE_DEPENDENCY -> commandHandler.requestAddModuleMapDependency();
            case REMOVE_MODULE_MAP_ITEM -> commandHandler.requestRemoveModuleMapItem();
            case VALIDATE_MODULE_MAP -> commandHandler.requestValidateModuleMap();
            case ADD_UML_MODULE -> commandHandler.requestAddUmlModule();
            case ADD_UML_CLASS -> commandHandler.requestAddUmlClass();
            case ADD_UML_INTERFACE -> commandHandler.requestAddUmlInterface();
            case ADD_UML_ENUM -> commandHandler.requestAddUmlEnum();
            case ADD_UML_ATTRIBUTE -> commandHandler.requestAddUmlAttribute();
            case ADD_UML_METHOD -> commandHandler.requestAddUmlMethod();
            case ADD_UML_RELATION -> commandHandler.requestAddUmlRelation();
            case IMPORT_UML_FROM_SOURCE -> commandHandler.requestImportUmlClassFromSourceCode();
            case OPEN_UML_SOURCE -> commandHandler.requestOpenSelectedUmlSourceFile();
            case REMOVE_UML_ITEM -> commandHandler.requestRemoveUmlItem();
            case VALIDATE_UML_CLASS -> commandHandler.requestValidateUmlClassDiagram();
            case ADD_ROLE -> commandHandler.requestAddRole();
            case ADD_PERMISSION -> commandHandler.requestAddPermission();
            case ADD_PERMISSION_ASSIGNMENT -> commandHandler.requestAddPermissionAssignment();
            case REMOVE_ROLES_PERMISSIONS_ITEM -> commandHandler.requestRemoveRolesPermissionsItem();
            case VALIDATE_ROLES_PERMISSIONS -> commandHandler.requestValidateRolesPermissions();
            case ADD_SCREEN -> commandHandler.requestAddScreen();
            case ADD_SCREEN_TRANSITION -> commandHandler.requestAddScreenTransition();
            case REMOVE_SCREEN_FLOW_ITEM -> commandHandler.requestRemoveScreenFlowItem();
            case VALIDATE_SCREEN_FLOW -> commandHandler.requestValidateScreenFlow();
            case ADD_WIREFRAME_SCREEN -> commandHandler.requestAddWireframeScreen();
            case ADD_WIREFRAME_SECTION -> commandHandler.requestAddWireframeSection();
            case ADD_WIREFRAME_FORM -> commandHandler.requestAddWireframeForm();
            case ADD_WIREFRAME_TABLE -> commandHandler.requestAddWireframeTable();
            case ADD_WIREFRAME_FIELD -> commandHandler.requestAddWireframeField();
            case ADD_WIREFRAME_BUTTON -> commandHandler.requestAddWireframeButton();
            case APPLY_WIREFRAME_TEMPLATE -> commandHandler.requestApplyWireframeTemplate(wireframeTemplateSupplier.get());
            case REMOVE_WIREFRAME_ITEM -> commandHandler.requestRemoveWireframeItem();
            case VALIDATE_WIREFRAME -> commandHandler.requestValidateWireframe();
            case ADD_BPMN_START -> commandHandler.requestAddBpmnStart();
            case ADD_BPMN_ACTIVITY -> commandHandler.requestAddBpmnActivity();
            case ADD_BPMN_DECISION -> commandHandler.requestAddBpmnDecision();
            case ADD_BPMN_END -> commandHandler.requestAddBpmnEnd();
            case ADD_BPMN_LANE -> commandHandler.requestAddBpmnLane();
            case ADD_BEHAVIOR_FLOW -> commandHandler.requestAddBehaviorFlow();
            case ADD_BEHAVIOR_NOTE -> commandHandler.requestAddBehaviorNote();
            case ADD_USE_CASE_ACTOR -> commandHandler.requestAddUseCaseActor();
            case ADD_USE_CASE -> commandHandler.requestAddUseCase();
            case ADD_USE_CASE_SYSTEM -> commandHandler.requestAddUseCaseSystem();
            case ADD_USE_CASE_ASSOCIATION -> commandHandler.requestAddUseCaseAssociation();
            case ADD_USE_CASE_INCLUDE -> commandHandler.requestAddUseCaseInclude();
            case ADD_USE_CASE_EXTEND -> commandHandler.requestAddUseCaseExtend();
            case ADD_USE_CASE_GENERALIZATION -> commandHandler.requestAddUseCaseGeneralization();
            case ADD_UML_ACTION -> commandHandler.requestAddUmlAction();
            case ADD_UML_DECISION -> commandHandler.requestAddUmlDecision();
            case ADD_UML_INITIAL_STATE -> commandHandler.requestAddUmlInitialState();
            case ADD_UML_FINAL_STATE -> commandHandler.requestAddUmlFinalState();
            case ADD_SEQUENCE_PARTICIPANT -> commandHandler.requestAddSequenceParticipant();
            case ADD_SEQUENCE_ACTIVATION -> commandHandler.requestAddSequenceActivation();
            case ADD_SEQUENCE_FRAGMENT -> commandHandler.requestAddSequenceFragment();
            case ADD_SEQUENCE_MESSAGE -> commandHandler.requestAddSequenceMessage();
            case ADD_SEQUENCE_RETURN_MESSAGE -> commandHandler.requestAddSequenceReturnMessage();
            case ADD_STATE -> commandHandler.requestAddState();
            case ADD_STATE_TRANSITION -> commandHandler.requestAddStateTransition();
            case REMOVE_BEHAVIOR_ITEM -> commandHandler.requestRemoveBehaviorItem();
            case VALIDATE_BEHAVIOR_DIAGRAM -> commandHandler.requestValidateBehaviorDiagram();
            case ADD_C4_PERSON -> commandHandler.requestAddC4Person();
            case ADD_C4_SYSTEM -> commandHandler.requestAddC4System();
            case ADD_C4_EXTERNAL_SYSTEM -> commandHandler.requestAddC4ExternalSystem();
            case ADD_C4_BOUNDARY -> commandHandler.requestAddC4Boundary();
            case ADD_C4_CONTAINER -> commandHandler.requestAddC4Container();
            case ADD_C4_APPLICATION -> commandHandler.requestAddC4Application();
            case ADD_C4_API -> commandHandler.requestAddC4Api();
            case ADD_ARCHITECTURE_DATABASE -> commandHandler.requestAddArchitectureDatabase();
            case ADD_ARCHITECTURE_EXTERNAL_SERVICE -> commandHandler.requestAddArchitectureExternalService();
            case ADD_DEPLOYMENT_ENVIRONMENT -> commandHandler.requestAddDeploymentEnvironment();
            case ADD_DEPLOYMENT_SERVER -> commandHandler.requestAddDeploymentServer();
            case ADD_DEPLOYMENT_CLIENT -> commandHandler.requestAddDeploymentClient();
            case ADD_DEPLOYMENT_SERVICE -> commandHandler.requestAddDeploymentService();
            case ADD_DEPLOYMENT_NETWORK -> commandHandler.requestAddDeploymentNetwork();
            case ADD_DEPLOYMENT_ARTIFACT -> commandHandler.requestAddDeploymentArtifact();
            case ADD_ARCHITECTURE_USES -> commandHandler.requestAddArchitectureUses();
            case ADD_ARCHITECTURE_DEPENDENCY -> commandHandler.requestAddArchitectureDependency();
            case ADD_ARCHITECTURE_INTEGRATION -> commandHandler.requestAddArchitectureIntegration();
            case ADD_ARCHITECTURE_CALL -> commandHandler.requestAddArchitectureCall();
            case ADD_ARCHITECTURE_READS_WRITES -> commandHandler.requestAddArchitectureReadsWrites();
            case ADD_DEPLOYMENT_CONNECTION -> commandHandler.requestAddDeploymentConnection();
            case ADD_DEPLOYMENT_HOSTING -> commandHandler.requestAddDeploymentHosting();
            case ADD_DEPLOYMENT_TARGET -> commandHandler.requestAddDeploymentTarget();
            case REMOVE_ARCHITECTURE_ITEM -> commandHandler.requestRemoveArchitectureItem();
            case VALIDATE_ARCHITECTURE_DIAGRAM -> commandHandler.requestValidateArchitectureDiagram();
            case FREE_GRAPH_SELECT_TOOL -> commandHandler.requestActivateFreeGraphSelectTool();
            case FREE_GRAPH_ADD_NODE_TOOL -> commandHandler.requestActivateFreeGraphAddNodeTool();
            case FREE_GRAPH_ADD_EDGE_TOOL -> commandHandler.requestActivateFreeGraphAddEdgeTool();
            case ADD_FREE_GRAPH_NODE -> commandHandler.requestAddFreeGraphNode();
            case ADD_FREE_GRAPH_EDGE -> commandHandler.requestAddFreeGraphEdge();
            case REMOVE_FREE_GRAPH_ITEM -> commandHandler.requestRemoveFreeGraphItem();
            case VALIDATE_FREE_GRAPH -> commandHandler.requestValidateFreeGraph();
            case VALIDATE_MODEL -> commandHandler.requestValidateProject();
            case REORGANIZE_DIAGRAM -> commandHandler.requestRegenerateLayout();
            case SWITCH_TO_CHEN -> commandHandler.requestSwitchNotation(NotationType.CHEN);
            case SWITCH_TO_CROWS_FOOT -> commandHandler.requestSwitchNotation(NotationType.CROWS_FOOT);
            case ZOOM_IN -> commandHandler.requestZoomIn();
            case ZOOM_OUT -> commandHandler.requestZoomOut();
            case RESET_ZOOM -> commandHandler.requestResetZoom();
            case FIT_TO_CONTENT -> commandHandler.requestFitToContent();
            case CENTER_DIAGRAM -> commandHandler.requestCenterDiagram();
            case CENTER_SELECTION -> commandHandler.requestCenterSelection();
            case BRING_SELECTION_TO_FRONT -> commandHandler.requestBringSelectionToFront();
            case SEND_SELECTION_TO_BACK -> commandHandler.requestSendSelectionToBack();
            case RAISE_SELECTION_LAYER -> commandHandler.requestRaiseSelectionLayer();
            case LOWER_SELECTION_LAYER -> commandHandler.requestLowerSelectionLayer();
            case GROW_SELECTED_FIGURE -> commandHandler.requestGrowSelectedVisualElement();
            case SHRINK_SELECTED_FIGURE -> commandHandler.requestShrinkSelectedVisualElement();
            case TRANSFER_VISUAL_SELECTION -> commandHandler.requestTransferVisualSelection();
            case LOGICAL_BUSINESS_SHOW_STRUCTURE -> commandHandler.requestShowLogicalBusinessStructure();
            case LOGICAL_BUSINESS_SHOW_PROPERTIES -> commandHandler.requestShowLogicalBusinessProperties();
            case LOGICAL_BUSINESS_SHOW_VALIDATION -> commandHandler.requestShowLogicalBusinessValidation();
            case LOGICAL_BUSINESS_SHOW_TRACEABILITY -> commandHandler.requestShowLogicalBusinessTraceability();
            case LOGICAL_BUSINESS_SHOW_HELP -> commandHandler.requestShowLogicalBusinessHelp();
            case EXPORT_SVG -> commandHandler.requestExportSvg();
            case EXPORT_MARKDOWN -> commandHandler.requestExportMarkdown();
            case EXPORT_PNG -> commandHandler.requestExportPng();
        }
    }
}
