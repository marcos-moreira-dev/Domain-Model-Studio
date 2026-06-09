package com.marcosmoreira.domainmodelstudio.domain.screenflow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Documento semántico de navegación entre pantallas. */
public final class ScreenFlowDocument {

    private final String projectName;
    private final String version;
    private final LocalDate documentDate;
    private final List<ScreenNode> screens;
    private final List<ScreenTransition> transitions;

    public ScreenFlowDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            List<ScreenNode> screens,
            List<ScreenTransition> transitions
    ) {
        this.projectName = textOrDefault(projectName, "Flujo de pantallas");
        this.version = textOrDefault(version, "0.1.0");
        this.documentDate = documentDate == null ? LocalDate.now() : documentDate;
        this.screens = List.copyOf(screens == null ? List.of() : screens);
        this.transitions = List.copyOf(transitions == null ? List.of() : transitions);
    }

    public static ScreenFlowDocument blank(String projectName) {
        return new ScreenFlowDocument(projectName, "0.1.0", LocalDate.now(), List.of(), List.of());
    }

    public String projectName() {
        return projectName;
    }

    public String version() {
        return version;
    }

    public LocalDate documentDate() {
        return documentDate;
    }

    public List<ScreenNode> screens() {
        return screens;
    }

    public List<ScreenTransition> transitions() {
        return transitions;
    }

    public Optional<ScreenNode> screenById(String id) {
        String normalized = normalize(id);
        return screens.stream().filter(screen -> screen.id().equals(normalized)).findFirst();
    }

    public ScreenFlowDocument withScreen(ScreenNode screen) {
        var list = new ArrayList<>(screens);
        list.add(screen);
        return copy(list, transitions);
    }

    public ScreenFlowDocument withTransition(ScreenTransition transition) {
        var list = new ArrayList<>(transitions);
        list.add(transition);
        return copy(screens, list);
    }

    public ScreenFlowDocument withUpdatedScreen(ScreenNode screen) {
        return copy(replaceScreen(screen), transitions);
    }

    public ScreenFlowDocument withUpdatedTransition(ScreenTransition transition) {
        return copy(screens, replaceTransition(transition));
    }

    public ScreenFlowDocument withoutScreen(String id) {
        String normalized = normalize(id);
        return copy(
                screens.stream().filter(screen -> !screen.id().equals(normalized)).toList(),
                transitions.stream()
                        .filter(transition -> !transition.sourceScreenId().equals(normalized))
                        .filter(transition -> !transition.targetScreenId().equals(normalized))
                        .toList());
    }

    public ScreenFlowDocument withoutTransition(String id) {
        String normalized = normalize(id);
        return copy(screens, transitions.stream().filter(transition -> !transition.id().equals(normalized)).toList());
    }

    public String nextScreenId() {
        return nextId("screen", screens.stream().map(ScreenNode::id).toList());
    }

    public String nextTransitionId() {
        return nextId("nav", transitions.stream().map(ScreenTransition::id).toList());
    }

    private ScreenFlowDocument copy(List<ScreenNode> screens, List<ScreenTransition> transitions) {
        return new ScreenFlowDocument(projectName, version, documentDate, screens, transitions);
    }

    private List<ScreenNode> replaceScreen(ScreenNode screen) {
        var output = new ArrayList<ScreenNode>();
        boolean replaced = false;
        for (ScreenNode current : screens) {
            if (current.id().equals(screen.id())) {
                output.add(screen);
                replaced = true;
            } else {
                output.add(current);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe pantalla: " + screen.id());
        }
        return output;
    }

    private List<ScreenTransition> replaceTransition(ScreenTransition transition) {
        var output = new ArrayList<ScreenTransition>();
        boolean replaced = false;
        for (ScreenTransition current : transitions) {
            if (current.id().equals(transition.id())) {
                output.add(transition);
                replaced = true;
            } else {
                output.add(current);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe transición: " + transition.id());
        }
        return output;
    }

    private static String nextId(String prefix, List<String> existingIds) {
        int counter = existingIds.size() + 1;
        String id;
        do {
            id = prefix + "-" + counter++;
        } while (existingIds.contains(id));
        return id;
    }

    private static String textOrDefault(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
