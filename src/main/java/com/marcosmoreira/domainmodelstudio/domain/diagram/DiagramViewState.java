package com.marcosmoreira.domainmodelstudio.domain.diagram;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Estado de vista persistible de un proyecto .dms.
 *
 * <p>El modelo semántico define qué existe y el layout define dónde está. Esta clase
 * guarda cómo se está mirando el diagrama: zoom, desplazamiento del lienzo, modo de
 * vista, grupos contraídos y filtros. Se mantiene neutral para que en el futuro pueda
 * aplicarse a UML Clases, mapas de módulos, BPMN, C4, wireframes y otros diagramas.</p>
 */
public final class DiagramViewState {

    public static final String DEFAULT_VIEW_MODE = "complete";

    private final double zoomFactor;
    private final double viewportX;
    private final double viewportY;
    private final String viewMode;
    private final Set<String> collapsedGroups;
    private final Map<String, Boolean> filters;

    public DiagramViewState(
            double zoomFactor,
            double viewportX,
            double viewportY,
            String viewMode,
            Set<String> collapsedGroups,
            Map<String, Boolean> filters
    ) {
        if (!Double.isFinite(zoomFactor) || zoomFactor <= 0.0) {
            throw new IllegalArgumentException("El zoom de vista debe ser un número positivo");
        }
        if (!Double.isFinite(viewportX) || !Double.isFinite(viewportY)) {
            throw new IllegalArgumentException("El desplazamiento de vista debe ser finito");
        }
        this.zoomFactor = zoomFactor;
        this.viewportX = viewportX;
        this.viewportY = viewportY;
        this.viewMode = normalizeViewMode(viewMode);
        this.collapsedGroups = copyTextSet(collapsedGroups);
        this.filters = copyFilters(filters);
    }

    public static DiagramViewState defaults() {
        return new DiagramViewState(1.0, 0.0, 0.0, DEFAULT_VIEW_MODE, Set.of(), Map.of());
    }

    public double zoomFactor() {
        return zoomFactor;
    }

    public double viewportX() {
        return viewportX;
    }

    public double viewportY() {
        return viewportY;
    }

    public String viewMode() {
        return viewMode;
    }

    public Set<String> collapsedGroups() {
        return Set.copyOf(collapsedGroups);
    }

    public Map<String, Boolean> filters() {
        return Map.copyOf(filters);
    }

    public DiagramViewState withZoomFactor(double updatedZoomFactor) {
        return new DiagramViewState(updatedZoomFactor, viewportX, viewportY, viewMode, collapsedGroups, filters);
    }

    public DiagramViewState withViewport(double updatedViewportX, double updatedViewportY) {
        return new DiagramViewState(zoomFactor, updatedViewportX, updatedViewportY, viewMode, collapsedGroups, filters);
    }

    public DiagramViewState withViewMode(String updatedViewMode) {
        return new DiagramViewState(zoomFactor, viewportX, viewportY, updatedViewMode, collapsedGroups, filters);
    }

    public DiagramViewState withCollapsedGroups(Set<String> updatedCollapsedGroups) {
        return new DiagramViewState(zoomFactor, viewportX, viewportY, viewMode, updatedCollapsedGroups, filters);
    }

    public DiagramViewState withFilters(Map<String, Boolean> updatedFilters) {
        return new DiagramViewState(zoomFactor, viewportX, viewportY, viewMode, collapsedGroups, updatedFilters);
    }

    private static String normalizeViewMode(String value) {
        String normalized = value == null ? "" : value.trim();
        return normalized.isEmpty() ? DEFAULT_VIEW_MODE : normalized;
    }

    private static Set<String> copyTextSet(Set<String> values) {
        LinkedHashSet<String> copy = new LinkedHashSet<>();
        if (values != null) {
            for (String value : values) {
                String normalized = value == null ? "" : value.trim();
                if (!normalized.isEmpty()) {
                    copy.add(normalized);
                }
            }
        }
        return Set.copyOf(copy);
    }

    private static Map<String, Boolean> copyFilters(Map<String, Boolean> values) {
        LinkedHashMap<String, Boolean> copy = new LinkedHashMap<>();
        if (values != null) {
            for (Map.Entry<String, Boolean> entry : values.entrySet()) {
                String key = Objects.toString(entry.getKey(), "").trim();
                if (!key.isEmpty()) {
                    copy.put(key, Boolean.TRUE.equals(entry.getValue()));
                }
            }
        }
        return Map.copyOf(copy);
    }
}
