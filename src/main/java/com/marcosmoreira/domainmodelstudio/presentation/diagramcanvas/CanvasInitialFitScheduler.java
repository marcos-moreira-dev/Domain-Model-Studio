package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import java.util.Objects;
import javafx.geometry.Bounds;

/**
 * Agenda el ajuste inicial de un diagrama sin duplicar banderas en cada centro visual.
 *
 * <p>El scheduler solo decide si corresponde disparar el ajuste. La superficie sigue
 * siendo responsable de esperar a que JavaFX mida el viewport y los nodos.</p>
 */
public final class CanvasInitialFitScheduler {

    private boolean fitScheduled;
    private String lastFitKey = "";

    public void schedule(String fitKey, boolean contentAvailable, ZoomableDiagramSurface surface) {
        Objects.requireNonNull(surface, "surface");
        String safeKey = fitKey == null ? "" : fitKey;
        if (!safeKey.equals(lastFitKey)) {
            fitScheduled = false;
            lastFitKey = safeKey;
        }
        if (fitScheduled || !contentAvailable) {
            if (!contentAvailable) {
                fitScheduled = false;
            }
            return;
        }
        fitScheduled = true;
        surface.fitToContentWhenReady();
    }

    public void schedule(String fitKey, boolean contentAvailable, ZoomableDiagramSurface surface, ViewportFitMode mode) {
        schedule(fitKey, contentAvailable, surface, mode, null);
    }

    public void schedule(
            String fitKey,
            boolean contentAvailable,
            ZoomableDiagramSurface surface,
            ViewportFitMode mode,
            Bounds fallbackBounds
    ) {
        Objects.requireNonNull(surface, "surface");
        Objects.requireNonNull(mode, "mode");
        String safeKey = fitKey == null ? "" : fitKey;
        if (!safeKey.equals(lastFitKey)) {
            fitScheduled = false;
            lastFitKey = safeKey;
        }
        if (fitScheduled || !contentAvailable) {
            if (!contentAvailable) {
                fitScheduled = false;
            }
            return;
        }
        fitScheduled = true;
        surface.fitToContentWhenReady(mode, fallbackBounds);
    }

    public void reset() {
        fitScheduled = false;
        lastFitKey = "";
    }

    public boolean fitScheduled() {
        return fitScheduled;
    }

    public String lastFitKey() {
        return lastFitKey;
    }
}
