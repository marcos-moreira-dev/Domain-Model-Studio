package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.Set;

/** Lenguaje detectado o solicitado para importar código fuente hacia UML Clases. */
public enum SourceLanguage {
    JAVA("java", "Java", Set.of(".java")),
    TYPESCRIPT("typescript", "TypeScript", Set.of(".ts", ".tsx")),
    UNKNOWN("unknown", "Desconocido", Set.of());

    private final String id;
    private final String displayName;
    private final Set<String> fileExtensions;

    SourceLanguage(String id, String displayName, Set<String> fileExtensions) {
        this.id = id;
        this.displayName = displayName;
        this.fileExtensions = fileExtensions;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public Set<String> fileExtensions() {
        return fileExtensions;
    }

    public boolean matchesFileName(String fileName) {
        String normalized = fileName == null ? "" : fileName.strip().toLowerCase();
        return fileExtensions.stream().anyMatch(normalized::endsWith);
    }

    public static SourceLanguage fromId(String id) {
        String normalized = id == null ? "" : id.strip().toLowerCase();
        for (SourceLanguage language : values()) {
            if (language.id.equals(normalized)) {
                return language;
            }
        }
        return UNKNOWN;
    }
}
