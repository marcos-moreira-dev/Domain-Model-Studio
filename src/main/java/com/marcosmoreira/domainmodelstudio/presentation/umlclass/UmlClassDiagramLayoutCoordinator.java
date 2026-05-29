package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfilePolicy;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfileRecommendation;
import com.marcosmoreira.domainmodelstudio.application.visual.DefaultVisualLayoutGenerator;
import com.marcosmoreira.domainmodelstudio.application.visual.UmlClassContainerLayoutSupport;
import com.marcosmoreira.domainmodelstudio.application.visual.UmlClassLayoutPolicy;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutSpecificationFactory;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasRenderFailureReport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualLayerOrderViewModelSupport;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualNodeSizeViewModelSupport;

/**
 * Concentrates layout, coste visual y operaciones de lienzo de UML Clases.
 *
 * <p>El ViewModel conserva la API usada por la interfaz, pero esta clase evita que
 * selección, edición, seguridad de render y movimiento de nodos queden mezclados en
 * un único archivo gigante.</p>
 */
final class UmlClassDiagramLayoutCoordinator {
    private final Port port;
    private final UmlSourceImportRenderProfilePolicy renderProfilePolicy = new UmlSourceImportRenderProfilePolicy();
    private final UmlClassContainerLayoutSupport containerLayoutSupport = new UmlClassContainerLayoutSupport();
    private final UmlClassVisualCostEstimator visualCostEstimator = new UmlClassVisualCostEstimator();
    private final UmlClassRuntimeMemoryMonitor runtimeMemoryMonitor = new UmlClassRuntimeMemoryMonitor();
    private final UmlClassExportSafetyPolicy exportSafetyPolicy = new UmlClassExportSafetyPolicy();
    private final UmlClassLargeFailureAdvisor largeFailureAdvisor = new UmlClassLargeFailureAdvisor();
    private final ObjectProperty<UmlSourceImportRenderProfile> activeRenderProfile =
            new SimpleObjectProperty<>(UmlSourceImportRenderProfile.safeDefault());
    private final ObjectProperty<UmlClassVisualCostEstimate> activeVisualCostEstimate =
            new SimpleObjectProperty<>(UmlClassVisualCostEstimate.empty(UmlSourceImportRenderProfile.safeDefault()));
    private final ObjectProperty<UmlClassRuntimeMemorySnapshot> activeRuntimeMemorySnapshot =
            new SimpleObjectProperty<>(UmlClassRuntimeMemorySnapshot.empty());
    private UmlSourceImportRenderProfileRecommendation activeRenderProfileRecommendation =
            renderProfilePolicy.recommend(0, 0, 0, 0);
    private VisualLayoutService visualLayoutService = visualLayoutServiceFor(UmlSourceImportRenderProfile.safeDefault());
    private boolean layoutPrepared;
    private String preparedLayoutScopeKey = "";

    UmlClassDiagramLayoutCoordinator(Port port) {
        this.port = Objects.requireNonNull(port, "port");
    }

    ObjectProperty<UmlSourceImportRenderProfile> activeRenderProfileProperty() { return activeRenderProfile; }
    UmlSourceImportRenderProfile activeRenderProfile() { return activeRenderProfile.get(); }
    UmlSourceImportRenderProfileRecommendation activeRenderProfileRecommendation() { return activeRenderProfileRecommendation; }
    ObjectProperty<UmlClassVisualCostEstimate> activeVisualCostEstimateProperty() { return activeVisualCostEstimate; }
    UmlClassVisualCostEstimate activeVisualCostEstimate() { return activeVisualCostEstimate.get(); }
    ObjectProperty<UmlClassRuntimeMemorySnapshot> activeRuntimeMemorySnapshotProperty() { return activeRuntimeMemorySnapshot; }
    UmlClassRuntimeMemorySnapshot activeRuntimeMemorySnapshot() { return activeRuntimeMemorySnapshot.get(); }

    void resetLayoutPreparation() {
        layoutPrepared = false;
        preparedLayoutScopeKey = "";
    }

    void configureRenderProfile(UmlClassDiagramDocument document) {
        UmlClassDiagramDocument safeDocument = document == null ? UmlClassDiagramDocument.blank("") : document;
        activeRenderProfileRecommendation = renderProfilePolicy.recommend(safeDocument);
        activeRenderProfile.set(activeRenderProfileRecommendation.profile());
        visualLayoutService = visualLayoutServiceFor(activeRenderProfileRecommendation.profile());
        resetLayoutPreparation();
    }

    void refreshVisualState(UmlClassDiagramFilterResult filtered) {
        UmlClassDiagramFilterResult safeFiltered = filtered == null
                ? new UmlClassDiagramFilterResult(List.of(), List.of(), List.of())
                : filtered;
        activeVisualCostEstimate.set(visualCostEstimator.estimate(
                safeFiltered.modules(), safeFiltered.classes(), safeFiltered.relations(), activeRenderProfile()));
        activeRuntimeMemorySnapshot.set(runtimeMemoryMonitor.snapshot());
        prepareVisibleLayout(safeFiltered);
    }

    void resetVisualState() {
        activeVisualCostEstimate.set(UmlClassVisualCostEstimate.empty(activeRenderProfile()));
        activeRuntimeMemorySnapshot.set(runtimeMemoryMonitor.snapshot());
    }

    void exportVisualAsPng(Path targetFile, ExportPngAction pngExportAction) throws IOException {
        exportSafetyPolicy.ensurePngExportAllowed(activeVisualCostEstimate(), activeRuntimeMemorySnapshot());
        try {
            pngExportAction.export(targetFile);
        } catch (OutOfMemoryError error) {
            activeRuntimeMemorySnapshot.set(runtimeMemoryMonitor.snapshot());
            throw largeFailureAdvisor.asExportIOException("Exportación PNG", error);
        } catch (RuntimeException exception) {
            throw largeFailureAdvisor.asExportIOException("Exportación PNG", exception);
        }
    }

    void handleCanvasRenderFailure(CanvasRenderFailureReport report, BooleanSupplier switchToSummaryAfterFailure) {
        activeRuntimeMemorySnapshot.set(runtimeMemoryMonitor.snapshot());
        boolean switchedToSummary = switchToSummaryAfterFailure != null && switchToSummaryAfterFailure.getAsBoolean();
        port.status(largeFailureAdvisor.renderFailureMessage(
                report,
                switchedToSummary,
                activeVisualCostEstimate(),
                activeRuntimeMemorySnapshot()));
    }

    void reorganizeLayout() {
        if (!ensureProjectForLayout("No hay diagrama UML Clases abierto para autoorganizar.")) {
            return;
        }
        DiagramProject reorganized = visualLayoutService.regenerateVisualLayout(port.currentProject().withUmlClassDiagram(port.currentDocument()));
        layoutPrepared = true;
        preparedLayoutScopeKey = "full-regenerated:" + activeRenderProfile();
        reorganized = containerLayoutSupport.fitAllModules(reorganized, port.currentDocument().classes());
        port.replaceCurrentProject(reorganized);
        port.notifyProjectChanged();
        port.status("UML Clases autoorganizado por contenedores, módulos y relaciones.");
    }

    NodeLayout layoutForModule(UmlModuleGroup module) {
        Objects.requireNonNull(module, "module");
        return preparedNodeLayout(VisualElementLayoutIds.umlModule(module.id()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para el módulo UML: " + module.id()));
    }

    NodeLayout layoutForClass(UmlClassNode node) {
        Objects.requireNonNull(node, "node");
        return preparedNodeLayout(VisualElementLayoutIds.umlClass(node.id()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para la clase UML: " + node.id()));
    }

    Optional<ConnectorLayout> layoutForConnector(DiagramElementId connectorId) {
        Objects.requireNonNull(connectorId, "connectorId");
        if (port.currentProject() == null || port.currentDocument() == null) {
            return Optional.empty();
        }
        prepareCurrentLayoutOnce();
        return port.currentProject().layouts().activeLayout().connectorById(connectorId);
    }

    Optional<Integer> addConnectorBendPoint(DiagramElementId connectorId, double x, double y) {
        if (!ensureProjectForLayout("No hay diagrama UML Clases abierto.")) {
            return Optional.empty();
        }
        try {
            ConnectorLayout current = preparedConnectorLayout(connectorId);
            ConnectorLayout updatedConnector = visualLayoutService.insertBendPointInNearestSegment(
                    port.currentProject().layouts().activeLayout(),
                    current,
                    x,
                    y);
            applyPreparedConnectorLayout(updatedConnector);
            port.notifyProjectChanged();
            int index = visualLayoutService.bendPointIndexAt(updatedConnector, x, y).orElse(-1);
            port.status("Punto intermedio UML agregado.");
            return index < 0 ? Optional.empty() : Optional.of(index);
        } catch (IllegalArgumentException exception) {
            port.status("No se pudo agregar punto intermedio UML: " + exception.getMessage());
            return Optional.empty();
        }
    }

    void moveConnectorBendPointTo(DiagramElementId connectorId, int bendPointIndex, double x, double y) {
        if (!ensureProjectForLayout("No hay diagrama UML Clases abierto.")) {
            return;
        }
        try {
            ConnectorLayout current = preparedConnectorLayout(connectorId);
            var point = current.bendPoints().get(bendPointIndex);
            applyPreparedConnectorLayout(current.withMovedBendPoint(bendPointIndex, x - point.x(), y - point.y()));
            port.notifyProjectChanged();
            port.status("Punto intermedio UML actualizado.");
        } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
            port.status("No se pudo mover punto intermedio UML: " + exception.getMessage());
        }
    }

    void removeConnectorBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        if (!ensureProjectForLayout("No hay diagrama UML Clases abierto.")) {
            return;
        }
        try {
            ConnectorLayout current = preparedConnectorLayout(connectorId);
            applyPreparedConnectorLayout(current.withoutBendPoint(bendPointIndex));
            port.notifyProjectChanged();
            port.status("Punto intermedio UML eliminado.");
        } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
            port.status("No se pudo eliminar punto intermedio UML: " + exception.getMessage());
        }
    }

    void moveModuleTo(String moduleId, double x, double y) {
        if (!ensureProjectForLayout("No hay diagrama UML Clases abierto.")) {
            return;
        }
        try {
            NodeLayout currentModuleLayout = preparedNodeLayout(VisualElementLayoutIds.umlModule(moduleId))
                    .orElseThrow(() -> new IllegalArgumentException("No existe layout para el módulo UML: " + moduleId));
            double deltaX = x - currentModuleLayout.x();
            double deltaY = y - currentModuleLayout.y();
            movePreparedNodeTo(VisualElementLayoutIds.umlModule(moduleId), x, y);
            for (UmlClassNode umlClass : classesInModule(moduleId)) {
                movePreparedNodeByIfPresent(VisualElementLayoutIds.umlClass(umlClass.id()), deltaX, deltaY);
            }
            port.notifyProjectChanged();
            port.status("Módulo UML y clases internas actualizados.");
        } catch (IllegalArgumentException exception) {
            port.status("No se pudo mover módulo UML: " + exception.getMessage());
        }
    }


    boolean reorderSelectedElement(DiagramElementId layoutId, VisualLayerOrderCommand command) {
        return VisualLayerOrderViewModelSupport.reorderSelectedNode(visualLayoutService, port.currentProject(), layoutId, command,
                port::replaceCurrentProject, port::notifyProjectChanged, port::status);
    }

    boolean resizeSelectedElement(DiagramElementId layoutId, VisualNodeSizeCommand command) {
        return VisualNodeSizeViewModelSupport.resizeSelectedNode(visualLayoutService, port.currentProject(), layoutId, command,
                port::replaceCurrentProject, port::notifyProjectChanged, port::status);
    }

    void moveConnectorLabelBy(DiagramElementId connectorId, double deltaX, double deltaY) {
        if (!ensureProjectForLayout("No hay diagrama UML Clases abierto.")) {
            return;
        }
        try {
            ConnectorLayout current = preparedConnectorLayout(connectorId);
            applyPreparedConnectorLayout(current.withMovedLabelOffset(deltaX, deltaY, 240.0));
            port.notifyProjectChanged();
            port.status("Etiqueta de relación UML actualizada.");
        } catch (IllegalArgumentException exception) {
            port.status("No se pudo mover etiqueta UML: " + exception.getMessage());
        }
    }

    void moveClassTo(String classId, double x, double y) {
        if (!ensureProjectForLayout("No hay diagrama UML Clases abierto.")) {
            return;
        }
        try {
            movePreparedNodeTo(VisualElementLayoutIds.umlClass(classId), x, y);
            port.replaceCurrentProject(containerLayoutSupport.expandModuleForClass(
                    port.currentProject(), classId, port.currentDocument().classes()));
            port.notifyProjectChanged();
            port.status("Clase UML y módulo contenedor actualizados.");
        } catch (IllegalArgumentException exception) {
            port.status("No se pudo mover clase UML: " + exception.getMessage());
        }
    }

    void prepareCurrentLayoutOnce() {
        if (port.currentProject() != null && !layoutPrepared) {
            port.replaceCurrentProject(visualLayoutService.ensureAdditionalVisualLayout(port.currentProject()));
            layoutPrepared = true;
            preparedLayoutScopeKey = "full-additional:" + activeRenderProfile();
        }
    }

    void prepareVisibleLayout(UmlClassDiagramFilterResult filtered) {
        if (port.currentProject() == null || port.currentDocument() == null || filtered == null) {
            return;
        }
        String scopeKey = layoutScopeKey(filtered);
        if (layoutPrepared && scopeKey.equals(preparedLayoutScopeKey)) {
            return;
        }
        if (filtered.modules().isEmpty() && filtered.classes().isEmpty() && filtered.relations().isEmpty()) {
            layoutPrepared = true;
            preparedLayoutScopeKey = scopeKey;
            return;
        }
        UmlClassDiagramDocument currentDocument = port.currentDocument();
        UmlClassDiagramDocument scopedDocument = new UmlClassDiagramDocument(
                currentDocument.projectName(),
                currentDocument.version(),
                currentDocument.documentDate(),
                filtered.modules(),
                filtered.classes(),
                filtered.relations(),
                List.of(),
                currentDocument.notes());
        DiagramProject scopedProject = port.currentProject().withUmlClassDiagram(scopedDocument);
        DiagramProject preparedProject = visualLayoutService.ensureAdditionalVisualLayout(scopedProject);
        port.replaceCurrentProject(preparedProject.withUmlClassDiagram(currentDocument));
        layoutPrepared = true;
        preparedLayoutScopeKey = scopeKey;
    }

    private void movePreparedNodeTo(DiagramElementId elementId, double x, double y) {
        DiagramProject currentProject = port.currentProject();
        DiagramLayout updated = currentProject.layouts().activeLayout().moveNode(elementId, x, y);
        port.replaceCurrentProject(currentProject.withLayouts(currentProject.layouts().withLayout(updated)));
    }

    private void movePreparedNodeByIfPresent(DiagramElementId elementId, double deltaX, double deltaY) {
        port.currentProject().layouts().activeLayout().nodeFor(elementId)
                .ifPresent(current -> movePreparedNodeTo(elementId, current.x() + deltaX, current.y() + deltaY));
    }

    private ConnectorLayout preparedConnectorLayout(DiagramElementId connectorId) {
        prepareCurrentLayoutOnce();
        return port.currentProject().layouts().activeLayout().connectorById(connectorId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la relación UML: " + connectorId));
    }

    private void applyPreparedConnectorLayout(ConnectorLayout connectorLayout) {
        DiagramProject currentProject = port.currentProject();
        DiagramLayout updated = currentProject.layouts().activeLayout().withConnector(connectorLayout);
        port.replaceCurrentProject(currentProject.withLayouts(currentProject.layouts().withLayout(updated)));
    }

    private List<UmlClassNode> classesInModule(String moduleId) {
        String normalizedModuleId = moduleId == null ? "" : moduleId.strip();
        List<UmlClassNode> source = port.currentDocument() == null ? List.of() : port.currentDocument().classes();
        return source.stream()
                .filter(node -> node.moduleId().equals(normalizedModuleId))
                .toList();
    }

    private Optional<NodeLayout> preparedNodeLayout(DiagramElementId elementId) {
        Objects.requireNonNull(elementId, "elementId");
        if (port.currentProject() == null || port.currentDocument() == null) {
            return Optional.empty();
        }
        prepareCurrentLayoutOnce();
        Optional<NodeLayout> layout = port.currentProject().layouts().activeLayout().nodeFor(elementId);
        if (layout.isPresent()) {
            return layout;
        }
        layoutPrepared = false;
        prepareCurrentLayoutOnce();
        return port.currentProject().layouts().activeLayout().nodeFor(elementId);
    }

    private boolean ensureProjectForLayout(String message) {
        if (port.currentProject() == null || port.currentDocument() == null) {
            port.status(message);
            return false;
        }
        prepareCurrentLayoutOnce();
        return true;
    }

    private String layoutScopeKey(UmlClassDiagramFilterResult filtered) {
        return activeRenderProfile() + "|"
                + (port.selectedView() == null ? "" : port.selectedView().id()) + "|"
                + normalizeForScope(port.searchQuery()) + "|"
                + (port.classKindFilter() == null ? "" : port.classKindFilter().name()) + "|"
                + (port.relationKindFilter() == null ? "" : port.relationKindFilter().name()) + "|"
                + filtered.modules().stream().map(UmlModuleGroup::id).toList().hashCode() + "|"
                + filtered.classes().stream().map(UmlClassNode::id).toList().hashCode() + "|"
                + filtered.relations().stream().map(UmlClassRelation::id).toList().hashCode();
    }

    private static String normalizeForScope(String value) {
        return value == null ? "" : value.strip();
    }

    private static VisualLayoutService visualLayoutServiceFor(UmlSourceImportRenderProfile renderProfile) {
        return new VisualLayoutService(
                new VisualLayoutSpecificationFactory(new UmlClassLayoutPolicy(renderProfile)),
                new DefaultVisualLayoutGenerator());
    }

    interface Port {
        DiagramProject currentProject();
        UmlClassDiagramDocument currentDocument();
        void replaceCurrentProject(DiagramProject project);
        UmlClassDiagramView selectedView();
        String searchQuery();
        UmlClassKind classKindFilter();
        UmlRelationKind relationKindFilter();
        void notifyProjectChanged();
        void status(String message);
    }
}
