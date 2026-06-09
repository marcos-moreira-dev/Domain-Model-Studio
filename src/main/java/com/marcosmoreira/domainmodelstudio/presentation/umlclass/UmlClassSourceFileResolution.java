package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/** Resultado explicable de intentar resolver el archivo fuente de una clase UML importada. */
final class UmlClassSourceFileResolution {

    enum Status {
        RESOLVED,
        NO_SOURCE_METADATA,
        INCOMPLETE_ROOT_RELATIVE_METADATA,
        CANDIDATE_NOT_FOUND,
        INVALID_PATH_METADATA
    }

    private final Status status;
    private final Path resolvedPath;
    private final List<Path> candidates;
    private final String detail;

    private UmlClassSourceFileResolution(Status status, Path resolvedPath, List<Path> candidates, String detail) {
        this.status = status;
        this.resolvedPath = resolvedPath;
        this.candidates = candidates == null ? List.of() : List.copyOf(candidates);
        this.detail = detail == null ? "" : detail.strip();
    }

    static UmlClassSourceFileResolution resolved(Path path) {
        return new UmlClassSourceFileResolution(Status.RESOLVED, path, List.of(path), "");
    }

    static UmlClassSourceFileResolution noSourceMetadata() {
        return new UmlClassSourceFileResolution(Status.NO_SOURCE_METADATA, null, List.of(), "");
    }

    static UmlClassSourceFileResolution incompleteRootRelativeMetadata() {
        return new UmlClassSourceFileResolution(Status.INCOMPLETE_ROOT_RELATIVE_METADATA, null, List.of(), "");
    }

    static UmlClassSourceFileResolution missingCandidates(List<Path> candidates) {
        return new UmlClassSourceFileResolution(Status.CANDIDATE_NOT_FOUND, null, candidates, "");
    }

    static UmlClassSourceFileResolution invalidMetadata(String detail) {
        return new UmlClassSourceFileResolution(Status.INVALID_PATH_METADATA, null, List.of(), detail);
    }

    Status status() {
        return status;
    }

    Optional<Path> path() {
        return Optional.ofNullable(resolvedPath);
    }

    List<Path> candidates() {
        return candidates;
    }

    boolean resolved() {
        return status == Status.RESOLVED && resolvedPath != null;
    }

    String userMessage(String className) {
        String label = className == null || className.isBlank() ? "La clase" : "La clase " + className.strip();
        return switch (status) {
            case RESOLVED -> "Archivo fuente detectado: " + resolvedPath;
            case NO_SOURCE_METADATA -> label + " no tiene metadatos de archivo fuente. Probablemente fue creada manualmente o importada desde Markdown sin ruta.";
            case INCOMPLETE_ROOT_RELATIVE_METADATA -> label + " tiene metadatos incompletos: falta la ruta raíz o la ruta relativa del archivo fuente.";
            case CANDIDATE_NOT_FOUND -> label + " apunta a una ruta fuente que ya no existe: " + firstCandidateText();
            case INVALID_PATH_METADATA -> label + " tiene metadatos de ruta no válidos" + (detail.isBlank() ? "." : ": " + detail);
        };
    }

    String panelSummary(String className) {
        if (resolved()) {
            return "Ruta resuelta\n" + resolvedPath;
        }
        return userMessage(className);
    }

    private String firstCandidateText() {
        return candidates.isEmpty() ? "sin candidato registrado" : candidates.get(0).toString();
    }
}
