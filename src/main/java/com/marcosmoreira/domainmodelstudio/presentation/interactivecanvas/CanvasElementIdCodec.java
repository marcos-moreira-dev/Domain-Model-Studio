package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Normaliza identificadores usados por el lienzo común y extrae sus identificadores
 * semánticos sin que cada adaptador repita utilidades de prefijos.
 */
public final class CanvasElementIdCodec {

    private final String nodePrefix;
    private final List<String> connectorPrefixes;

    private CanvasElementIdCodec(String nodePrefix, List<String> connectorPrefixes) {
        this.nodePrefix = requirePrefix(nodePrefix, "nodePrefix");
        this.connectorPrefixes = List.copyOf(connectorPrefixes.stream()
                .map(prefix -> requirePrefix(prefix, "connectorPrefix"))
                .toList());
    }

    public static CanvasElementIdCodec withPrefixes(String nodePrefix, String... connectorPrefixes) {
        return new CanvasElementIdCodec(nodePrefix, Arrays.asList(connectorPrefixes == null ? new String[0] : connectorPrefixes));
    }

    public String nodePrefix() {
        return nodePrefix;
    }

    public List<String> connectorPrefixes() {
        return connectorPrefixes;
    }

    public String normalize(String value) {
        return normalizeText(value);
    }

    public String rawNodeId(String canvasId) {
        return rawIdAfterPrefix(canvasId, nodePrefix);
    }

    public Optional<String> rawConnectorId(String canvasId) {
        String normalized = normalize(canvasId);
        for (String prefix : connectorPrefixes) {
            if (normalized.startsWith(prefix)) {
                return Optional.of(normalized.substring(prefix.length()));
            }
        }
        return Optional.empty();
    }

    public boolean isNodeId(String canvasId) {
        return normalize(canvasId).startsWith(nodePrefix);
    }

    public boolean isConnectorId(String canvasId) {
        String normalized = normalize(canvasId);
        return connectorPrefixes.stream().anyMatch(normalized::startsWith);
    }

    public String rawIdAfterPrefix(String value, String prefix) {
        String normalized = normalize(value);
        String safePrefix = Objects.toString(prefix, "").strip();
        return normalized.startsWith(safePrefix) ? normalized.substring(safePrefix.length()) : "";
    }

    public static String normalizeText(String value) {
        return value == null ? "" : value.strip();
    }

    private static String requirePrefix(String prefix, String name) {
        String normalized = normalizeText(prefix);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El prefijo " + name + " no puede estar vacío");
        }
        return normalized;
    }
}
