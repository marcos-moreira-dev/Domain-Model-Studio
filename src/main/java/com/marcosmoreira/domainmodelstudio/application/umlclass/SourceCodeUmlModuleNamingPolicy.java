package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeModule;
import java.util.Locale;

/** Crea nombres de módulos UML compactos sin perder la ruta/package original. */
final class SourceCodeUmlModuleNamingPolicy {

    String displayName(ParsedCodeModule module) {
        String qualified = normalize(module.displayName().isBlank() ? module.qualifiedName() : module.displayName());
        if (qualified.isBlank()) {
            return "módulo";
        }
        String normalized = qualified.replace('\\', '.').replace('/', '.');
        String[] parts = normalized.split("\\.");
        for (int i = parts.length - 1; i >= 0; i--) {
            String candidate = parts[i].strip();
            if (!candidate.isBlank() && !genericSegment(candidate)) {
                return candidate;
            }
        }
        return parts.length == 0 ? qualified : parts[parts.length - 1].strip();
    }

    String description(ParsedCodeModule module) {
        String qualified = normalize(module.qualifiedName());
        String path = module.relativePath() == null ? "" : module.relativePath().toString().replace('\\', '/');
        if (qualified.isBlank() && path.isBlank()) {
            return "Agrupador generado desde código fuente.";
        }
        if (path.isBlank()) {
            return "Agrupador generado desde código fuente. Package/carpeta: " + qualified;
        }
        if (qualified.isBlank()) {
            return "Agrupador generado desde código fuente. Ruta: " + path;
        }
        return "Agrupador generado desde código fuente. Package/carpeta: " + qualified + "; ruta: " + path;
    }

    private boolean genericSegment(String segment) {
        String normalized = segment.toLowerCase(Locale.ROOT);
        return normalized.equals("src") || normalized.equals("main") || normalized.equals("java")
                || normalized.equals("app") || normalized.equals("features") || normalized.equals("feature")
                || normalized.equals("com") || normalized.equals("org") || normalized.equals("net");
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
