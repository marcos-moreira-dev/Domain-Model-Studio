package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;

/** Política de filtrado para evitar que el escáner lea carpetas pesadas o archivos de prueba por accidente. */
final class SourceFileScanPolicy {
    private static final Set<String> IGNORED_DIRECTORY_NAMES = Set.of(
            ".git", ".idea", ".vscode", "target", "build", "dist", "out", "node_modules", "coverage", ".gradle");

    private SourceFileScanPolicy() {
    }

    static boolean isIgnoredDirectory(Path directory) {
        return IGNORED_DIRECTORY_NAMES.contains(fileName(directory));
    }

    static boolean shouldIncludeFile(SourceCodeImportRequest request, Path relativePath, SourceLanguage language) {
        if (language == SourceLanguage.UNKNOWN) {
            return false;
        }
        return request.includeTests() || !isTestPath(relativePath, language);
    }

    static boolean isSourceFileName(String fileName) {
        return SourceLanguage.JAVA.matchesFileName(fileName) || SourceLanguage.TYPESCRIPT.matchesFileName(fileName);
    }

    static SourceLanguage languageForFileName(String fileName) {
        if (SourceLanguage.JAVA.matchesFileName(fileName)) {
            return SourceLanguage.JAVA;
        }
        if (SourceLanguage.TYPESCRIPT.matchesFileName(fileName)) {
            return SourceLanguage.TYPESCRIPT;
        }
        return SourceLanguage.UNKNOWN;
    }

    private static boolean isTestPath(Path relativePath, SourceLanguage language) {
        String normalized = normalize(relativePath);
        if (normalized.contains("/src/test/") || normalized.contains("/__tests__/") || normalized.contains("/test/")) {
            return true;
        }
        String fileName = fileName(relativePath);
        if (language == SourceLanguage.JAVA) {
            return fileName.endsWith("test.java") || fileName.endsWith("tests.java") || fileName.endsWith("it.java");
        }
        if (language == SourceLanguage.TYPESCRIPT) {
            return fileName.endsWith(".spec.ts") || fileName.endsWith(".test.ts")
                    || fileName.endsWith(".spec.tsx") || fileName.endsWith(".test.tsx");
        }
        return false;
    }

    private static String normalize(Path path) {
        return ("/" + path.toString().replace('\\', '/')).toLowerCase(Locale.ROOT);
    }

    private static String fileName(Path path) {
        Path fileName = path == null ? null : path.getFileName();
        return fileName == null ? "" : fileName.toString().toLowerCase(Locale.ROOT);
    }
}
