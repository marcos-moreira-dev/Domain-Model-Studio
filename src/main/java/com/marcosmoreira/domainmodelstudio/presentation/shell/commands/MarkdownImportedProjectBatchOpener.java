package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportItemResult;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportResult;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelResult;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportedProjectVisualPreparationUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

/** Abre en tandas los proyectos importados para evitar pausas largas del hilo JavaFX. */
final class MarkdownImportedProjectBatchOpener {

    private static final int BATCH_SIZE = 4;

    private final ProjectTabOpener projectTabOpener;
    private final ImportedProjectVisualPreparationUseCase visualPreparationUseCase;
    private final Consumer<String> statusUpdater;

    MarkdownImportedProjectBatchOpener(
            ProjectTabOpener projectTabOpener,
            ImportedProjectVisualPreparationUseCase visualPreparationUseCase,
            Consumer<String> statusUpdater
    ) {
        this.projectTabOpener = Objects.requireNonNull(projectTabOpener, "projectTabOpener");
        this.visualPreparationUseCase = Objects.requireNonNull(visualPreparationUseCase, "visualPreparationUseCase");
        this.statusUpdater = Objects.requireNonNull(statusUpdater, "statusUpdater");
    }

    void open(MarkdownBatchImportResult result, Consumer<Integer> onFinished) {
        Runnable action = () -> openOnFxThread(result.importedItems(), 0, 0, onFinished);
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            Platform.runLater(action);
        }
    }

    private void openOnFxThread(
            List<MarkdownBatchImportItemResult> items,
            int index,
            int opened,
            Consumer<Integer> onFinished
    ) {
        int current = index;
        int openedNow = opened;
        int end = Math.min(items.size(), index + BATCH_SIZE);
        while (current < end) {
            if (openOne(items.get(current))) {
                openedNow++;
            }
            current++;
        }
        statusUpdater.accept("Abriendo proyectos importados: " + openedNow + " de " + items.size() + "...");
        if (current >= items.size()) {
            int finalOpened = openedNow;
            Platform.runLater(() -> onFinished.accept(finalOpened));
            return;
        }
        int nextIndex = current;
        int nextOpened = openedNow;
        PauseTransition pause = new PauseTransition(Duration.millis(20));
        pause.setOnFinished(event -> openOnFxThread(items, nextIndex, nextOpened, onFinished));
        pause.play();
    }

    private boolean openOne(MarkdownBatchImportItemResult item) {
        ImportMarkdownModelResult importResult = item.importResult().orElse(null);
        if (importResult == null) {
            return false;
        }
        DiagramProject prepared = visualPreparationUseCase.prepare(importResult.project());
        projectTabOpener.openProjectInNewTab(prepared, item.displayName(), true);
        return true;
    }
}
