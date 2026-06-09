package com.marcosmoreira.domainmodelstudio.domain.diagram;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetCatalog;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualCommentLayer;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleSheet;
import java.util.Objects;
import java.util.Optional;

/**
 * Agregado principal del proyecto editable.
 *
 * <p>Conserva separadas las dimensiones centrales del producto: metadatos,
 * modelo semántico visual, layouts, estilos, estado de vista y documentos
 * especializados como el diccionario de datos.</p>
 */
public final class DiagramProject {

    private final ProjectMetadata metadata;
    private final DiagramModel model;
    private final DiagramLayouts layouts;
    private final DiagramStyleSheet styleSheet;
    private final DiagramViewState viewState;
    private final DataDictionaryDocument dataDictionary;
    private final ModuleMapDocument moduleMap;
    private final UmlClassDiagramDocument umlClassDiagram;
    private final RolesPermissionsDocument rolesPermissions;
    private final ScreenFlowDocument screenFlow;
    private final WireframeDocument wireframe;
    private final BehaviorDiagramDocument behaviorDiagram;
    private final ArchitectureDiagramDocument architectureDiagram;
    private final FreeGraphDocument freeGraph;
    private final LogicalBusinessDocument logicalBusinessDocument;
    private final LogicalBusinessGraphDocument logicalBusinessGraphDocument;
    private final VisualCommentLayer visualComments;
    private final ProjectAssetCatalog assetCatalog;

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet
    ) {
        this(metadata, model, layouts, styleSheet, DiagramViewState.defaults(), null, null, null, null, null, null, null, null, null, null, null, ProjectAssetCatalog.empty());
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState
    ) {
        this(metadata, model, layouts, styleSheet, viewState, null, null, null, null, null, null, null, null, null, null, null, ProjectAssetCatalog.empty());
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState,
            DataDictionaryDocument dataDictionary
    ) {
        this(metadata, model, layouts, styleSheet, viewState, dataDictionary, null, null, null, null, null, null, null, null, null, null, ProjectAssetCatalog.empty());
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState,
            DataDictionaryDocument dataDictionary,
            ModuleMapDocument moduleMap
    ) {
        this(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, null, null, null, null, null, null, null, null, null, ProjectAssetCatalog.empty());
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState,
            DataDictionaryDocument dataDictionary,
            ModuleMapDocument moduleMap,
            UmlClassDiagramDocument umlClassDiagram
    ) {
        this(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, null, null, null, null, null, null, null, null, ProjectAssetCatalog.empty());
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState,
            DataDictionaryDocument dataDictionary,
            ModuleMapDocument moduleMap,
            UmlClassDiagramDocument umlClassDiagram,
            RolesPermissionsDocument rolesPermissions,
            ScreenFlowDocument screenFlow,
            WireframeDocument wireframe,
            BehaviorDiagramDocument behaviorDiagram,
            ArchitectureDiagramDocument architectureDiagram
    ) {
        this(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram,
                rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, null, null, null, ProjectAssetCatalog.empty());
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState,
            DataDictionaryDocument dataDictionary,
            ModuleMapDocument moduleMap,
            UmlClassDiagramDocument umlClassDiagram,
            RolesPermissionsDocument rolesPermissions,
            ScreenFlowDocument screenFlow,
            WireframeDocument wireframe,
            BehaviorDiagramDocument behaviorDiagram,
            ArchitectureDiagramDocument architectureDiagram,
            ProjectAssetCatalog assetCatalog
    ) {
        this(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram,
                rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, null, null, null, assetCatalog);
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState,
            DataDictionaryDocument dataDictionary,
            ModuleMapDocument moduleMap,
            UmlClassDiagramDocument umlClassDiagram,
            RolesPermissionsDocument rolesPermissions,
            ScreenFlowDocument screenFlow,
            WireframeDocument wireframe,
            BehaviorDiagramDocument behaviorDiagram,
            ArchitectureDiagramDocument architectureDiagram,
            FreeGraphDocument freeGraph,
            ProjectAssetCatalog assetCatalog
    ) {
        this(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram,
                rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, null, null, assetCatalog);
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState,
            DataDictionaryDocument dataDictionary,
            ModuleMapDocument moduleMap,
            UmlClassDiagramDocument umlClassDiagram,
            RolesPermissionsDocument rolesPermissions,
            ScreenFlowDocument screenFlow,
            WireframeDocument wireframe,
            BehaviorDiagramDocument behaviorDiagram,
            ArchitectureDiagramDocument architectureDiagram,
            FreeGraphDocument freeGraph,
            LogicalBusinessDocument logicalBusinessDocument,
            ProjectAssetCatalog assetCatalog
    ) {
        this(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram,
                rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph,
                logicalBusinessDocument, null, assetCatalog);
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState,
            DataDictionaryDocument dataDictionary,
            ModuleMapDocument moduleMap,
            UmlClassDiagramDocument umlClassDiagram,
            RolesPermissionsDocument rolesPermissions,
            ScreenFlowDocument screenFlow,
            WireframeDocument wireframe,
            BehaviorDiagramDocument behaviorDiagram,
            ArchitectureDiagramDocument architectureDiagram,
            FreeGraphDocument freeGraph,
            LogicalBusinessDocument logicalBusinessDocument,
            LogicalBusinessGraphDocument logicalBusinessGraphDocument,
            ProjectAssetCatalog assetCatalog
    ) {
        this(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram,
                rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph,
                logicalBusinessDocument, logicalBusinessGraphDocument, VisualCommentLayer.empty(), assetCatalog);
    }

    public DiagramProject(
            ProjectMetadata metadata,
            DiagramModel model,
            DiagramLayouts layouts,
            DiagramStyleSheet styleSheet,
            DiagramViewState viewState,
            DataDictionaryDocument dataDictionary,
            ModuleMapDocument moduleMap,
            UmlClassDiagramDocument umlClassDiagram,
            RolesPermissionsDocument rolesPermissions,
            ScreenFlowDocument screenFlow,
            WireframeDocument wireframe,
            BehaviorDiagramDocument behaviorDiagram,
            ArchitectureDiagramDocument architectureDiagram,
            FreeGraphDocument freeGraph,
            LogicalBusinessDocument logicalBusinessDocument,
            LogicalBusinessGraphDocument logicalBusinessGraphDocument,
            VisualCommentLayer visualComments,
            ProjectAssetCatalog assetCatalog
    ) {
        this.metadata = Objects.requireNonNull(metadata, "Los metadatos no pueden ser null");
        this.model = Objects.requireNonNull(model, "El modelo no puede ser null");
        this.layouts = Objects.requireNonNull(layouts, "Los layouts no pueden ser null");
        this.styleSheet = Objects.requireNonNull(styleSheet, "La hoja de estilos no puede ser null");
        this.viewState = viewState == null ? DiagramViewState.defaults() : viewState;
        this.dataDictionary = dataDictionary;
        this.moduleMap = moduleMap;
        this.umlClassDiagram = umlClassDiagram;
        this.rolesPermissions = rolesPermissions;
        this.screenFlow = screenFlow;
        this.wireframe = wireframe;
        this.behaviorDiagram = behaviorDiagram;
        this.architectureDiagram = architectureDiagram;
        this.freeGraph = freeGraph;
        this.logicalBusinessDocument = logicalBusinessDocument;
        this.logicalBusinessGraphDocument = logicalBusinessGraphDocument;
        this.visualComments = visualComments == null ? VisualCommentLayer.empty() : visualComments;
        this.assetCatalog = assetCatalog == null ? ProjectAssetCatalog.empty() : assetCatalog;
    }

    public static DiagramProject blank(String id, String title) {
        return blank(id, title, ProjectType.defaultType());
    }

    public static DiagramProject blank(String id, String title, ProjectType projectType) {
        return blank(id, title, projectType, projectType == null ? null : projectType.diagramTypeId());
    }

    public static DiagramProject blank(String id, String title, DiagramTypeId diagramTypeId) {
        return blank(id, title, ProjectType.defaultType(), diagramTypeId);
    }

    public static DiagramProject blank(String id, String title, ProjectType projectType, DiagramTypeId diagramTypeId) {
        return new DiagramProject(
                new ProjectMetadata(id, title, projectType, diagramTypeId, "0.1.0", "draft",
                        com.marcosmoreira.domainmodelstudio.domain.notation.NotationType.CHEN, "", ""),
                DiagramModel.empty(),
                DiagramLayouts.empty(),
                DiagramStyleSheet.defaults()
        );
    }

    public ProjectMetadata metadata() {
        return metadata;
    }

    public DiagramModel model() {
        return model;
    }

    public DiagramLayouts layouts() {
        return layouts;
    }

    public DiagramStyleSheet styleSheet() {
        return styleSheet;
    }

    public DiagramViewState viewState() {
        return viewState;
    }

    public Optional<DataDictionaryDocument> dataDictionary() {
        return Optional.ofNullable(dataDictionary);
    }

    public Optional<ModuleMapDocument> moduleMap() {
        return Optional.ofNullable(moduleMap);
    }

    public Optional<UmlClassDiagramDocument> umlClassDiagram() {
        return Optional.ofNullable(umlClassDiagram);
    }

    public Optional<RolesPermissionsDocument> rolesPermissions() {
        return Optional.ofNullable(rolesPermissions);
    }

    public Optional<ScreenFlowDocument> screenFlow() {
        return Optional.ofNullable(screenFlow);
    }

    public Optional<WireframeDocument> wireframe() {
        return Optional.ofNullable(wireframe);
    }

    public Optional<BehaviorDiagramDocument> behaviorDiagram() {
        return Optional.ofNullable(behaviorDiagram);
    }

    public Optional<ArchitectureDiagramDocument> architectureDiagram() {
        return Optional.ofNullable(architectureDiagram);
    }

    public Optional<FreeGraphDocument> freeGraph() {
        return Optional.ofNullable(freeGraph);
    }

    public Optional<LogicalBusinessDocument> logicalBusinessDocument() {
        return Optional.ofNullable(logicalBusinessDocument);
    }

    public Optional<LogicalBusinessGraphDocument> logicalBusinessGraphDocument() {
        return Optional.ofNullable(logicalBusinessGraphDocument);
    }

    public VisualCommentLayer visualComments() {
        return visualComments;
    }

    public ProjectAssetCatalog assetCatalog() {
        return assetCatalog;
    }

    public DiagramProject withModel(DiagramModel updatedModel) {
        return new DiagramProject(metadata, updatedModel, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withMetadata(ProjectMetadata updatedMetadata) {
        return new DiagramProject(updatedMetadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withLayouts(DiagramLayouts updatedLayouts) {
        return new DiagramProject(metadata, model, updatedLayouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withStyleSheet(DiagramStyleSheet updatedStyleSheet) {
        return new DiagramProject(metadata, model, layouts, updatedStyleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withViewState(DiagramViewState updatedViewState) {
        return new DiagramProject(metadata, model, layouts, styleSheet, updatedViewState, dataDictionary, moduleMap, umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withDataDictionary(DataDictionaryDocument updatedDataDictionary) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, updatedDataDictionary, moduleMap, umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withModuleMap(ModuleMapDocument updatedModuleMap) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, updatedModuleMap, umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withUmlClassDiagram(UmlClassDiagramDocument updatedUmlClassDiagram) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, updatedUmlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withRolesPermissions(RolesPermissionsDocument updatedRolesPermissions) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, updatedRolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withScreenFlow(ScreenFlowDocument updatedScreenFlow) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, rolesPermissions, updatedScreenFlow, wireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withWireframe(WireframeDocument updatedWireframe) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, rolesPermissions, screenFlow, updatedWireframe, behaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withBehaviorDiagram(BehaviorDiagramDocument updatedBehaviorDiagram) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, rolesPermissions, screenFlow, wireframe, updatedBehaviorDiagram, architectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withArchitectureDiagram(ArchitectureDiagramDocument updatedArchitectureDiagram) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap, umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, updatedArchitectureDiagram, freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withFreeGraph(FreeGraphDocument updatedFreeGraph) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap,
                umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram,
                updatedFreeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withLogicalBusinessDocument(LogicalBusinessDocument updatedLogicalBusinessDocument) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap,
                umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram,
                freeGraph, updatedLogicalBusinessDocument, logicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withLogicalBusinessGraphDocument(LogicalBusinessGraphDocument updatedLogicalBusinessGraphDocument) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap,
                umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram,
                freeGraph, logicalBusinessDocument, updatedLogicalBusinessGraphDocument, visualComments, assetCatalog);
    }

    public DiagramProject withVisualComments(VisualCommentLayer updatedVisualComments) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap,
                umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram,
                freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, updatedVisualComments, assetCatalog);
    }

    public DiagramProject withAssetCatalog(ProjectAssetCatalog updatedAssetCatalog) {
        return new DiagramProject(metadata, model, layouts, styleSheet, viewState, dataDictionary, moduleMap,
                umlClassDiagram, rolesPermissions, screenFlow, wireframe, behaviorDiagram, architectureDiagram,
                freeGraph, logicalBusinessDocument, logicalBusinessGraphDocument, visualComments, updatedAssetCatalog);
    }
}
