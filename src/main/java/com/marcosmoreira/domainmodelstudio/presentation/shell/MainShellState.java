package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.statusbar.ProjectStatusSummary;
import com.marcosmoreira.domainmodelstudio.presentation.statusbar.ProjectStatusSummaryFormatter;
import com.marcosmoreira.domainmodelstudio.presentation.statusbar.StatusBarViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.placeholder.PlaceholderWorkspaceViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import com.marcosmoreira.domainmodelstudio.presentation.shell.tabs.ProjectTabOrderPolicy;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Estado compartido del shell principal.
 *
 * <p>Centraliza título, marca de cambios y mensajes de statusbar para que toolbar,
 * menú y comandos no manipulen propiedades dispersas ni creen dependencias circulares.</p>
 */
public final class MainShellState {

    public static final String HOME_TAB_ID = "__home__";
    private static final String EMPTY_PROJECT_TITLE = "Proyecto sin abrir";

    private final StringProperty projectTitle = new SimpleStringProperty(EMPTY_PROJECT_TITLE);
    private final StringProperty windowTitle = new SimpleStringProperty("Domain Model Studio - " + EMPTY_PROJECT_TITLE);
    private final BooleanProperty dirty = new SimpleBooleanProperty(false);
    private final BooleanProperty projectOpen = new SimpleBooleanProperty(false);
    private final BooleanProperty saveableProjectOpen = new SimpleBooleanProperty(false);
    private final ObjectProperty<NotationType> activeNotation = new SimpleObjectProperty<>();
    private final ObjectProperty<DiagramTypeId> activeDiagramType = new SimpleObjectProperty<>();
    private final ObjectProperty<PlaceholderWorkspaceViewModel> placeholderWorkspace = new SimpleObjectProperty<>();
    private final ObjectProperty<Set<ExportFormat>> activeExportFormats = new SimpleObjectProperty<>(Set.of());
    private final ObservableList<EditorTabViewState> editorTabs = FXCollections.observableArrayList();
    private final StringProperty activeEditorTabId = new SimpleStringProperty(HOME_TAB_ID);
    private final StatusBarViewModel statusBarViewModel;
    private final ProjectStatusSummaryFormatter statusSummaryFormatter = new ProjectStatusSummaryFormatter();

    public MainShellState(StatusBarViewModel statusBarViewModel) {
        this.statusBarViewModel = Objects.requireNonNull(statusBarViewModel, "statusBarViewModel");
        projectTitle.addListener((observable, oldValue, newValue) -> refreshWindowTitle());
        dirty.addListener((observable, oldValue, newValue) -> refreshWindowTitle());
        editorTabs.add(new EditorTabViewState(HOME_TAB_ID, "Pantalla de inicio", false, true, false));
        showNoProjectState();
    }

    public StringProperty windowTitleProperty() {
        return windowTitle;
    }


    public ObservableList<EditorTabViewState> editorTabs() {
        return editorTabs;
    }

    public StringProperty activeEditorTabIdProperty() {
        return activeEditorTabId;
    }

    public void setActiveEditorTab(String tabId) {
        activeEditorTabId.set(tabId == null || tabId.isBlank() ? HOME_TAB_ID : tabId);
    }

    public void addProjectTab(String tabId, String title, boolean tabDirty) {
        editorTabs.add(new EditorTabViewState(tabId, displayTabTitle(title, tabDirty), true, false, tabDirty));
    }

    public void updateProjectTab(String tabId, String title, boolean tabDirty) {
        for (int index = 0; index < editorTabs.size(); index++) {
            EditorTabViewState tab = editorTabs.get(index);
            if (tab.id().equals(tabId)) {
                editorTabs.set(index, new EditorTabViewState(tabId, displayTabTitle(title, tabDirty), true, false, tabDirty));
                return;
            }
        }
    }

    public void removeProjectTab(String tabId) {
        editorTabs.removeIf(tab -> tab.id().equals(tabId) && tab.closeable());
        if (tabId != null && tabId.equals(activeEditorTabId.get())) {
            activeEditorTabId.set(HOME_TAB_ID);
        }
    }

    public boolean moveProjectTabAfter(String movedTabId, String targetTabId) {
        List<EditorTabViewState> reordered = ProjectTabOrderPolicy.moveAfter(editorTabs, movedTabId, targetTabId);
        if (reordered.equals(List.copyOf(editorTabs))) {
            return false;
        }
        editorTabs.setAll(reordered);
        return true;
    }

    public Optional<String> adjacentProjectTabIdAfterClosing(String closingTabId) {
        if (closingTabId == null || closingTabId.isBlank()) {
            return Optional.empty();
        }
        int closingIndex = -1;
        for (int index = 0; index < editorTabs.size(); index++) {
            if (editorTabs.get(index).id().equals(closingTabId)) {
                closingIndex = index;
                break;
            }
        }
        if (closingIndex < 0) {
            return Optional.empty();
        }
        for (int index = closingIndex + 1; index < editorTabs.size(); index++) {
            EditorTabViewState candidate = editorTabs.get(index);
            if (candidate.closeable()) {
                return Optional.of(candidate.id());
            }
        }
        for (int index = closingIndex - 1; index >= 0; index--) {
            EditorTabViewState candidate = editorTabs.get(index);
            if (candidate.closeable()) {
                return Optional.of(candidate.id());
            }
        }
        return Optional.empty();
    }

    private String displayTabTitle(String title, boolean tabDirty) {
        String normalized = title == null || title.isBlank() ? "Modelo conceptual" : title.trim();
        return tabDirty ? normalized + " *" : normalized;
    }

    public boolean hasProjectOpen() {
        return projectOpen.get();
    }

    public boolean hasSaveableProjectOpen() {
        return saveableProjectOpen.get();
    }

    public boolean hasUnsavedChanges() {
        return saveableProjectOpen.get() && dirty.get();
    }


    public BooleanProperty projectOpenProperty() {
        return projectOpen;
    }

    public BooleanProperty saveableProjectOpenProperty() {
        return saveableProjectOpen;
    }

    public ObjectProperty<NotationType> activeNotationProperty() {
        return activeNotation;
    }

    public ObjectProperty<DiagramTypeId> activeDiagramTypeProperty() {
        return activeDiagramType;
    }

    public ObjectProperty<PlaceholderWorkspaceViewModel> placeholderWorkspaceProperty() {
        return placeholderWorkspace;
    }

    public ObjectProperty<Set<ExportFormat>> activeExportFormatsProperty() {
        return activeExportFormats;
    }

    public void setActiveExportFormats(Set<ExportFormat> formats) {
        activeExportFormats.set(formats == null || formats.isEmpty() ? Set.of() : Set.copyOf(formats));
    }

    public void updateStatus(String message) {
        statusBarViewModel.setMessage(message);
    }

    public void showNoProjectState() {
        placeholderWorkspace.set(null);
        projectOpen.set(false);
        saveableProjectOpen.set(false);
        dirty.set(false);
        projectTitle.set(EMPTY_PROJECT_TITLE);
        statusBarViewModel.setProjectState("Sin proyecto");
        statusBarViewModel.setMessage("Listo. Crea un proyecto, abre un .dms o importa un modelo Markdown.");
        activeNotation.set(null);
        activeDiagramType.set(null);
        setActiveExportFormats(Set.of());
        statusBarViewModel.setNotation("—");
        statusBarViewModel.setElementCount("Sin proyecto abierto");
        statusBarViewModel.setSaveState("Sin proyecto");
    }

    public void showProjectState(DiagramProject project, String stateLabel) {
        Objects.requireNonNull(project, "project");
        placeholderWorkspace.set(null);
        projectOpen.set(true);
        saveableProjectOpen.set(true);
        setProjectTitle(project.metadata().title());
        activeNotation.set(project.metadata().activeNotation());
        activeDiagramType.set(project.metadata().diagramTypeId());
        ProjectStatusSummary summary = statusSummaryFormatter.summarize(project);
        setNotation(summary.viewLabel());
        statusBarViewModel.setElementCount(summary.elementSummary());
        statusBarViewModel.setProjectState(stateLabel == null || stateLabel.isBlank()
                ? "Proyecto abierto"
                : stateLabel.trim());
        refreshSaveState();
    }


    public void showPlaceholderState(PlaceholderWorkspaceViewModel placeholder, String stateLabel) {
        Objects.requireNonNull(placeholder, "placeholder");
        placeholderWorkspace.set(placeholder);
        projectOpen.set(false);
        saveableProjectOpen.set(true);
        dirty.set(false);
        setProjectTitle(placeholder.title());
        activeNotation.set(null);
        activeDiagramType.set(DiagramTypeId.of(placeholder.diagramTypeId()));
        setActiveExportFormats(Set.of());
        statusBarViewModel.setNotation("—");
        statusBarViewModel.setElementCount("Vista de planificación");
        statusBarViewModel.setProjectState(stateLabel == null || stateLabel.isBlank()
                ? "En preparación"
                : stateLabel.trim());
        statusBarViewModel.setSaveState("Planificación");
        statusBarViewModel.setMessage(placeholder.title() + " abierto como vista de planificación.");
    }

    public void setProjectTitle(String title) {
        projectTitle.set(title == null || title.isBlank() ? "Proyecto sin título" : title.trim());
    }

    public void setElementCount(int entityCount, int relationshipCount) {
        if (!projectOpen.get()) {
            statusBarViewModel.setElementCount("Sin proyecto abierto");
            return;
        }
        int total = entityCount + relationshipCount;
        if (total == 0) {
            statusBarViewModel.setElementCount("Proyecto vacío");
            return;
        }
        statusBarViewModel.setElementCount(entityCount + " entidades / " + relationshipCount + " relaciones");
    }

    public void setNotation(String notation) {
        statusBarViewModel.setNotation(displayNotation(notation));
    }

    public void markDirty() {
        if (!saveableProjectOpen.get()) {
            showNoProjectState();
            return;
        }
        dirty.set(true);
        refreshSaveState();
    }

    public void markSaved() {
        dirty.set(false);
        refreshSaveState();
    }

    private void refreshSaveState() {
        if (!saveableProjectOpen.get()) {
            statusBarViewModel.setSaveState("Sin proyecto");
            return;
        }
        statusBarViewModel.setSaveState(dirty.get() ? "Cambios sin guardar" : "Guardado");
    }

    private String displayNotation(String notation) {
        if (notation == null || notation.isBlank()) {
            return "—";
        }
        return notation.toLowerCase(java.util.Locale.ROOT).contains("crow") ? "Pata de gallo" : notation.trim();
    }

    private void refreshWindowTitle() {
        String marker = dirty.get() && projectOpen.get() ? " *" : "";
        windowTitle.set("Domain Model Studio - " + projectTitle.get() + marker);
    }
}
