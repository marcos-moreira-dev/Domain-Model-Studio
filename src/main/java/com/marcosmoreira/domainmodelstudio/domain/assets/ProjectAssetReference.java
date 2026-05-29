package com.marcosmoreira.domainmodelstudio.domain.assets;

import java.util.Locale;
import java.util.Objects;

/**
 * Referencia estable a un recurso del proyecto.
 *
 * <p>El .dms no debe depender de rutas absolutas del computador del usuario. Por eso
 * esta referencia guarda rutas relativas normalizadas, pensadas para copiarse junto al
 * archivo del proyecto o empaquetarse en una carpeta de trabajo controlada.</p>
 */
public final class ProjectAssetReference {

    private final String id;
    private final ProjectAssetKind kind;
    private final String displayName;
    private final String relativePath;
    private final String mimeType;
    private final String purpose;
    private final String checksum;
    private final String notes;

    public ProjectAssetReference(
            String id,
            ProjectAssetKind kind,
            String displayName,
            String relativePath,
            String mimeType,
            String purpose,
            String checksum,
            String notes
    ) {
        this.id = requireText(id, "El id del recurso no puede estar vacío");
        this.kind = kind == null ? ProjectAssetKind.OTHER : kind;
        this.displayName = normalize(displayName).isBlank() ? this.id : normalize(displayName);
        this.relativePath = normalizeProjectPath(relativePath);
        this.mimeType = normalizeMimeType(mimeType);
        this.purpose = normalize(purpose);
        this.checksum = normalize(checksum);
        this.notes = normalize(notes);
    }

    public static ProjectAssetReference logo(String id, String displayName, String relativePath) {
        return new ProjectAssetReference(id, ProjectAssetKind.LOGO, displayName, relativePath, "", "Logo de documento", "", "");
    }

    public String id() {
        return id;
    }

    public ProjectAssetKind kind() {
        return kind;
    }

    public String displayName() {
        return displayName;
    }

    public String relativePath() {
        return relativePath;
    }

    public String mimeType() {
        return mimeType;
    }

    public String purpose() {
        return purpose;
    }

    public String checksum() {
        return checksum;
    }

    public String notes() {
        return notes;
    }

    public boolean isLogo() {
        return kind == ProjectAssetKind.LOGO;
    }

    private static String normalizeProjectPath(String raw) {
        String normalized = normalize(raw).replace('\\', '/');
        while (normalized.contains("//")) {
            normalized = normalized.replace("//", "/");
        }
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("La ruta relativa del recurso no puede estar vacía");
        }
        String lower = normalized.toLowerCase(Locale.ROOT);
        if (normalized.startsWith("/") || normalized.startsWith("~") || normalized.matches("^[A-Za-z]:.*")
                || lower.startsWith("file:") || lower.startsWith("http:") || lower.startsWith("https:")) {
            throw new IllegalArgumentException("El recurso debe usar ruta relativa del proyecto: " + raw);
        }
        for (String part : normalized.split("/")) {
            if (part.equals("..")) {
                throw new IllegalArgumentException("La ruta del recurso no puede escapar del proyecto: " + raw);
            }
        }
        return normalized;
    }

    private static String normalizeMimeType(String raw) {
        return normalize(raw).toLowerCase(Locale.ROOT);
    }

    private static String requireText(String raw, String message) {
        String normalized = normalize(raw);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private static String normalize(String raw) {
        return Objects.toString(raw, "").trim();
    }
}
