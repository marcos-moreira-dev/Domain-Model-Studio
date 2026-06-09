package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Panel lateral único para workspaces autocontenidos.
 *
 * <p>La clase conserva el estado de activación del dock y delega la construcción visual en
 * factories pequeñas. Así el SideDock puede cambiar con la pestaña activa sin mezclar estado,
 * layout y creación de controles.</p>
 *
 * <p>Desde el punto de vista de arquitectura, esta es la frontera operativa entre un workspace
 * especializado y sus módulos laterales: estructura, propiedades, apariencia, validación,
 * trazabilidad o ayuda. no contiene reglas de negocio; solo decide qué módulo está visible y
 * cómo se monta su contenido.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> al cambiar de un Grafo lógico a un Wireframe,
 * el dock conserva la misma carcasa visual, pero reemplaza los módulos. Esa es la razón
 * de separar {@code SideDockModule} del contenido específico de cada workspace.</p>
 */
public final class WorkbenchSideDock {

    private final BorderPane root = new BorderPane();
    private final VBox rail = new VBox(6);
    private final ScrollPane railScroll = new ScrollPane(rail);
    private final BorderPane contentHost = new BorderPane();
    private final SideDockStatePolicy statePolicy = new SideDockStatePolicy();
    private final SideDockRailFactory railFactory = new SideDockRailFactory();
    private final SideDockModuleFrameFactory frameFactory = new SideDockModuleFrameFactory();
    private final SideDockSizingPolicy sizingPolicy = new SideDockSizingPolicy();
    private SideDockContext context;
    private List<SideDockModule> modules = List.of();
    private SideDockModuleId activeModuleId;
    private BorderPane activeFrame;
    private boolean contentVisible = true;

    public WorkbenchSideDock() {
        root.getStyleClass().add("workbench-side-dock");
        rail.getStyleClass().add("side-dock-rail");
        rail.setAlignment(Pos.TOP_CENTER);
        railScroll.getStyleClass().add("side-dock-rail-scroll");
        railScroll.setFitToWidth(true);
        railScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        railScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentHost.getStyleClass().add("side-dock-content-host");
        root.setLeft(railScroll);
        root.setCenter(contentHost);
    }

    public Parent getRoot() {
        return root;
    }

    public void refresh(SideDockContext context, List<SideDockModule> modules) {
        this.context = Objects.requireNonNull(context, "context");
        this.modules = List.copyOf(Objects.requireNonNull(modules, "modules"));
        Set<SideDockModuleId> available = new LinkedHashSet<>();
        this.modules.forEach(module -> available.add(module.id()));
        SideDockModuleId previousActive = activeModuleId;
        Optional<SideDockModuleId> nextActive = statePolicy.nextActiveModule(activeModuleId, available);
        activeModuleId = nextActive.orElse(null);
        boolean activeChanged = !Objects.equals(previousActive, activeModuleId);
        contentVisible = activeModuleId != null && (activeChanged || contentVisible);
        rebuildRail();
        renderActiveModule();
    }

    public void activate(SideDockModuleId moduleId) {
        if (moduleId == null || modules.stream().noneMatch(module -> module.id() == moduleId)) {
            return;
        }
        if (moduleId == activeModuleId && contentVisible) {
            contentVisible = false;
        } else {
            activeModuleId = moduleId;
            contentVisible = true;
        }
        rebuildRail();
        renderActiveModule();
    }

    public Optional<SideDockModuleId> activeModuleId() {
        return Optional.ofNullable(activeModuleId).filter(ignored -> contentVisible);
    }

    private void rebuildRail() {
        rail.getChildren().clear();
        for (SideDockModule module : modules) {
            boolean active = module.id() == activeModuleId && contentVisible;
            rail.getChildren().add(railFactory.tabFor(module, active, () -> activate(module.id())));
        }
    }

    private void renderActiveModule() {
        detachActiveFrame();
        contentHost.setCenter(null);
        boolean open = contentVisible && activeModuleId != null;
        contentHost.setVisible(open);
        contentHost.setManaged(open);
        sizingPolicy.apply(root, open);
        if (!open) {
            return;
        }
        modules.stream()
                .filter(module -> module.id() == activeModuleId)
                .findFirst()
                .ifPresent(this::renderModule);
    }

    private void renderModule(SideDockModule module) {
        activeFrame = frameFactory.frameFor(module, context, () -> activate(module.id()));
        contentHost.setCenter(activeFrame);
    }

    private void detachActiveFrame() {
        if (activeFrame != null) {
            activeFrame.setCenter(null);
            activeFrame = null;
        }
    }
}
