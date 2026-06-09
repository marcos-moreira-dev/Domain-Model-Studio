package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Política de presentación para módulos UML tratados como contenedores visuales.
 *
 * <p>Centraliza etiquetas, conteos y estilo semántico para que los packages/carpetas
 * importados desde código se lean como zonas de trabajo y no como cajas sueltas.</p>
 */
public final class UmlClassModuleContainerPolicy {

    public UmlClassModuleContainerDescriptor describe(UmlModuleGroup module, List<UmlClassNode> classes) {
        Objects.requireNonNull(module, "module");
        long classCount = Objects.requireNonNullElse(classes, List.<UmlClassNode>of()).stream()
                .filter(node -> node.moduleId().equals(module.id()))
                .count();
        return new UmlClassModuleContainerDescriptor(
                module.id(),
                module.displayName(),
                sourceRootLabel(module),
                pathLabel(module),
                classCount == 1 ? "1 clase" : classCount + " clases",
                roleStyleClass(module));
    }

    public boolean contains(UmlModuleGroup module, UmlClassNode node) {
        return module != null && node != null && node.moduleId().equals(module.id());
    }

    private String sourceRootLabel(UmlModuleGroup module) {
        String sourceRoot = sourceRoot(module);
        if (sourceRoot.isBlank()) {
            return "Source root no especificado";
        }
        return "Root: " + sourceRoot;
    }

    private String pathLabel(UmlModuleGroup module) {
        String path = firstNonBlank(module.path(), module.description());
        if (path.isBlank()) {
            return "Sin ruta/package";
        }
        return compactPath(path);
    }

    private String roleStyleClass(UmlModuleGroup module) {
        String key = (sourceRoot(module) + " " + module.id() + " " + module.path() + " " + module.description())
                .toLowerCase(Locale.ROOT);
        if (containsAny(key, "backend", "java", "spring", "api")) {
            return "uml-class-canvas-module-backend";
        }
        if (containsAny(key, "frontend", "front", "typescript", "angular", "src/app")) {
            return "uml-class-canvas-module-frontend";
        }
        if (containsAny(key, "shared", "common", "comun", "común")) {
            return "uml-class-canvas-module-shared";
        }
        if (containsAny(key, "library", "lib")) {
            return "uml-class-canvas-module-library";
        }
        return "uml-class-canvas-module-source";
    }

    private String sourceRoot(UmlModuleGroup module) {
        String notes = normalize(module.notes());
        int index = notes.toLowerCase(Locale.ROOT).indexOf("source root:");
        if (index < 0) {
            return "";
        }
        String value = notes.substring(index + "source root:".length());
        int separator = value.indexOf('|');
        return normalize(separator >= 0 ? value.substring(0, separator) : value);
    }

    private String compactPath(String value) {
        String normalized = normalize(value)
                .replace("Agrupador generado desde código fuente.", "")
                .replace("Package/carpeta:", "")
                .replace("ruta:", "")
                .replace("Ruta:", "")
                .replace(';', ' ')
                .strip();
        if (normalized.length() <= 72) {
            return normalized;
        }
        return "…" + normalized.substring(normalized.length() - 69);
    }

    private static boolean containsAny(String value, String... fragments) {
        for (String fragment : fragments) {
            if (value.contains(fragment)) {
                return true;
            }
        }
        return false;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            String normalized = normalize(value);
            if (!normalized.isBlank()) {
                return normalized;
            }
        }
        return "";
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
