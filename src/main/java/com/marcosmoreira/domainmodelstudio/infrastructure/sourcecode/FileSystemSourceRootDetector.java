package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguageVersion;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootDetectionResult;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootDetectorPort;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootKind;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

/** Detector inicial de raíces Java/TypeScript para proyectos mono-repo, full stack o módulos puntuales. */
public final class FileSystemSourceRootDetector implements SourceRootDetectorPort {
    private static final Set<String> IGNORED_CANDIDATE_NAMES = Set.of(
            ".git", ".idea", ".vscode", "target", "build", "dist", "out", "node_modules", "coverage", ".gradle");

    @Override
    public SourceRootDetectionResult detect(SourceCodeImportRequest request) {
        Path projectRoot = request.projectRoot();
        List<String> warnings = new ArrayList<>();
        if (!Files.isDirectory(projectRoot)) {
            warnings.add("La carpeta seleccionada no existe o no es un directorio: " + projectRoot);
            return new SourceRootDetectionResult(projectRoot, List.of(), warnings);
        }

        List<SourceRoot> roots = new ArrayList<>();
        Set<String> usedIds = new HashSet<>();
        for (Path candidate : candidateDirectories(projectRoot, warnings)) {
            detectCandidate(request, projectRoot, candidate, roots, usedIds);
        }
        if (roots.isEmpty()) {
            addFallbackRootFromSourceFiles(request, projectRoot, roots, usedIds, warnings);
        }
        if (roots.isEmpty()) {
            warnings.add("No se detectaron raíces Java o TypeScript en la carpeta seleccionada.");
        }
        return new SourceRootDetectionResult(projectRoot, roots, warnings);
    }

    private static List<Path> candidateDirectories(Path projectRoot, List<String> warnings) {
        List<Path> candidates = new ArrayList<>();
        candidates.add(projectRoot);
        try (Stream<Path> stream = Files.list(projectRoot)) {
            stream.filter(Files::isDirectory)
                    .filter(path -> !IGNORED_CANDIDATE_NAMES.contains(fileName(path)))
                    .sorted()
                    .forEach(candidates::add);
        } catch (IOException exception) {
            warnings.add("No se pudieron inspeccionar subcarpetas de " + projectRoot + ": " + exception.getMessage());
        }
        return candidates;
    }

    private static void detectCandidate(SourceCodeImportRequest request, Path projectRoot, Path candidate,
                                        List<SourceRoot> roots, Set<String> usedIds) {
        if (hasJavaMarkers(candidate)) {
            roots.add(sourceRoot(request, projectRoot, candidate, SourceLanguage.JAVA, SourceRootKind.BACKEND, usedIds));
        }
        if (hasTypeScriptMarkers(candidate)) {
            roots.add(sourceRoot(request, projectRoot, candidate, SourceLanguage.TYPESCRIPT, SourceRootKind.FRONTEND, usedIds));
        }
    }

    private static void addFallbackRootFromSourceFiles(SourceCodeImportRequest request, Path projectRoot,
                                                       List<SourceRoot> roots, Set<String> usedIds,
                                                       List<String> warnings) {
        Set<SourceLanguage> detectedLanguages = sourceLanguagesPresentUnder(projectRoot, warnings);
        for (SourceLanguage language : detectedLanguages) {
            SourceRootKind kind = language == SourceLanguage.TYPESCRIPT ? SourceRootKind.FRONTEND : SourceRootKind.BACKEND;
            roots.add(sourceRoot(request, projectRoot, projectRoot, language, kind, usedIds));
        }
        if (!detectedLanguages.isEmpty()) {
            warnings.add("No se encontraron marcadores de proyecto, pero se detectaron archivos fuente; "
                    + "la carpeta seleccionada se tratará como raíz importable.");
        }
    }

    private static Set<SourceLanguage> sourceLanguagesPresentUnder(Path root, List<String> warnings) {
        Set<SourceLanguage> languages = new LinkedHashSet<>();
        try {
            Files.walkFileTree(root, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) {
                    if (!root.equals(directory) && SourceFileScanPolicy.isIgnoredDirectory(directory)) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    SourceLanguage language = SourceFileScanPolicy.languageForFileName(fileName(file));
                    if (language != SourceLanguage.UNKNOWN) {
                        languages.add(language);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exception) {
                    warnings.add("No se pudo inspeccionar " + file + ": " + exception.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException exception) {
            warnings.add("No se pudo buscar archivos fuente en " + root + ": " + exception.getMessage());
        }
        return languages;
    }

    private static SourceRoot sourceRoot(SourceCodeImportRequest request, Path projectRoot, Path candidate,
                                         SourceLanguage language, SourceRootKind defaultKind, Set<String> usedIds) {
        String baseId = baseId(projectRoot, candidate) + "-" + language.id();
        String id = uniqueId(baseId, usedIds);
        SourceRootKind kind = kindFromPath(candidate, defaultKind);
        String displayName = displayName(projectRoot, candidate, language, kind);
        return new SourceRoot(id, displayName, candidate, kind, List.of(preferredVersion(request, language)));
    }

    private static boolean hasJavaMarkers(Path directory) {
        return exists(directory.resolve("pom.xml"))
                || exists(directory.resolve("build.gradle"))
                || exists(directory.resolve("build.gradle.kts"))
                || exists(directory.resolve("src/main/java"))
                || exists(directory.resolve("src/test/java"));
    }

    private static boolean hasTypeScriptMarkers(Path directory) {
        return exists(directory.resolve("package.json"))
                || exists(directory.resolve("angular.json"))
                || exists(directory.resolve("tsconfig.json"))
                || exists(directory.resolve("src/app"));
    }

    private static boolean exists(Path path) {
        return Files.exists(path);
    }

    private static SourceLanguageVersion preferredVersion(SourceCodeImportRequest request, SourceLanguage language) {
        return request.preferredLanguageVersions().stream()
                .filter(version -> version.language() == language)
                .findFirst()
                .orElseGet(() -> SourceLanguageVersion.flexible(language));
    }

    private static SourceRootKind kindFromPath(Path candidate, SourceRootKind defaultKind) {
        String name = fileName(candidate).toLowerCase(Locale.ROOT);
        if (name.contains("shared") || name.contains("common")) {
            return SourceRootKind.SHARED;
        }
        if (name.contains("lib") || name.contains("library")) {
            return SourceRootKind.LIBRARY;
        }
        if (name.contains("front") || name.contains("web") || name.contains("admin")) {
            return SourceRootKind.FRONTEND;
        }
        if (name.contains("back") || name.contains("api") || name.contains("server")) {
            return SourceRootKind.BACKEND;
        }
        return defaultKind;
    }

    private static String displayName(Path projectRoot, Path candidate, SourceLanguage language, SourceRootKind kind) {
        String rootName = projectRoot.equals(candidate) ? "Raíz" : fileName(candidate);
        return rootName + " - " + kind.displayName() + " - " + language.displayName();
    }

    private static String baseId(Path projectRoot, Path candidate) {
        if (projectRoot.equals(candidate)) {
            return "root";
        }
        return sanitize(fileName(candidate));
    }

    private static String uniqueId(String baseId, Set<String> usedIds) {
        String candidate = baseId;
        int index = 2;
        while (!usedIds.add(candidate)) {
            candidate = baseId + "-" + index;
            index++;
        }
        return candidate;
    }

    private static String sanitize(String value) {
        String normalized = value == null ? "root" : value.strip().toLowerCase(Locale.ROOT);
        normalized = normalized.replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
        return normalized.isBlank() ? "root" : normalized;
    }

    private static String fileName(Path path) {
        Path fileName = path == null ? null : path.getFileName();
        return fileName == null ? "root" : fileName.toString();
    }
}
