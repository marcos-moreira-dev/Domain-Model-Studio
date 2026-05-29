package com.marcosmoreira.domainmodelstudio.domain.wireframe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Documento semántico de wireframes administrativos. */
public final class WireframeDocument {

    private final String projectName;
    private final String version;
    private final LocalDate documentDate;
    private final List<WireframeScreen> screens;
    private final List<WireframeComponent> components;

    public WireframeDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            List<WireframeScreen> screens,
            List<WireframeComponent> components
    ) {
        this.projectName = textOrDefault(projectName, "Wireframes administrativos");
        this.version = textOrDefault(version, "0.1.0");
        this.documentDate = documentDate == null ? LocalDate.now() : documentDate;
        this.screens = List.copyOf(screens == null ? List.of() : screens);
        this.components = List.copyOf(components == null ? List.of() : components);
    }

    public static WireframeDocument blank(String projectName) {
        return new WireframeDocument(projectName, "0.1.0", LocalDate.now(), List.of(), List.of());
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

    public List<WireframeScreen> screens() {
        return screens;
    }

    public List<WireframeComponent> components() {
        return components;
    }

    public Optional<WireframeScreen> screenById(String id) {
        String normalized = normalize(id);
        return screens.stream().filter(screen -> screen.id().equals(normalized)).findFirst();
    }

    public WireframeDocument withScreen(WireframeScreen screen) {
        var list = new ArrayList<>(screens);
        list.add(screen);
        return copy(list, components);
    }

    public WireframeDocument withComponent(WireframeComponent component) {
        var list = new ArrayList<>(components);
        list.add(component);
        return copy(screens, list);
    }

    public WireframeDocument withUpdatedScreen(WireframeScreen screen) {
        return copy(replaceScreen(screen), components);
    }

    public WireframeDocument withUpdatedComponent(WireframeComponent component) {
        return copy(screens, replaceComponent(component));
    }

    public WireframeDocument withoutScreen(String id) {
        String normalized = normalize(id);
        return copy(
                screens.stream().filter(screen -> !screen.id().equals(normalized)).toList(),
                components.stream().filter(component -> !component.screenId().equals(normalized)).toList());
    }

    public WireframeDocument withoutComponent(String id) {
        String normalized = normalize(id);
        return copy(screens, components.stream().filter(component -> !component.id().equals(normalized)).toList());
    }

    public String nextScreenId() {
        return nextId("wire-screen", screens.stream().map(WireframeScreen::id).toList());
    }

    public String nextComponentId() {
        return nextId("component", components.stream().map(WireframeComponent::id).toList());
    }

    private WireframeDocument copy(List<WireframeScreen> screens, List<WireframeComponent> components) {
        return new WireframeDocument(projectName, version, documentDate, screens, components);
    }

    private List<WireframeScreen> replaceScreen(WireframeScreen screen) {
        var output = new ArrayList<WireframeScreen>();
        boolean replaced = false;
        for (WireframeScreen current : screens) {
            if (current.id().equals(screen.id())) {
                output.add(screen);
                replaced = true;
            } else {
                output.add(current);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe pantalla wireframe: " + screen.id());
        }
        return output;
    }

    private List<WireframeComponent> replaceComponent(WireframeComponent component) {
        var output = new ArrayList<WireframeComponent>();
        boolean replaced = false;
        for (WireframeComponent current : components) {
            if (current.id().equals(component.id())) {
                output.add(component);
                replaced = true;
            } else {
                output.add(current);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe componente wireframe: " + component.id());
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
