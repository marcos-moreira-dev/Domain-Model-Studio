package com.marcosmoreira.domainmodelstudio.domain.freegraph;

import java.util.Objects;

/** Nodo semántico de un grafo libre. */
public final class FreeGraphNode {

    private final String id;
    private final String title;
    private final String content;
    private final int orderIndex;

    public FreeGraphNode(String id, String title, String content, int orderIndex) {
        this.id = required(id, "id");
        this.title = required(title, "title");
        this.content = normalize(content);
        if (orderIndex < 0) {
            throw new IllegalArgumentException("El campo orderIndex no puede ser negativo.");
        }
        this.orderIndex = orderIndex;
    }

    public static FreeGraphNode of(String id, String title) {
        return new FreeGraphNode(id, title, "", 0);
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String content() {
        return content;
    }

    public int orderIndex() {
        return orderIndex;
    }

    public FreeGraphNode renamed(String updatedTitle) {
        return new FreeGraphNode(id, updatedTitle, content, orderIndex);
    }

    public FreeGraphNode withContent(String updatedContent) {
        return new FreeGraphNode(id, title, updatedContent, orderIndex);
    }

    public FreeGraphNode withOrderIndex(int updatedOrderIndex) {
        return new FreeGraphNode(id, title, content, updatedOrderIndex);
    }

    public FreeGraphNode withDetails(String updatedTitle, String updatedContent, int updatedOrderIndex) {
        return new FreeGraphNode(id, updatedTitle, updatedContent, updatedOrderIndex);
    }

    private static String required(String value, String fieldName) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El campo " + fieldName + " no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof FreeGraphNode node && node.id.equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
