package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Resultado de escanear una carpeta antes de parsear archivos concretos. */
public record SourceScanResult(
        List<SourceRoot> sourceRoots,
        List<SourceFileCandidate> files,
        List<Path> ignoredPaths,
        List<String> warnings
) {
    public SourceScanResult {
        sourceRoots = List.copyOf(sourceRoots == null ? List.of() : sourceRoots);
        files = List.copyOf(files == null ? List.of() : files);
        ignoredPaths = List.copyOf(ignoredPaths == null ? List.of() : ignoredPaths);
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
    }

    public List<SourceFileCandidate> filesForRoot(String sourceRootId) {
        String normalized = sourceRootId == null ? "" : sourceRootId.strip();
        return files.stream().filter(file -> file.sourceRootId().equals(normalized)).toList();
    }

    public List<SourceRoot> rootsFor(SourceLanguage language) {
        SourceLanguage normalized = language == null ? SourceLanguage.UNKNOWN : language;
        return sourceRoots.stream().filter(root -> root.supports(normalized)).toList();
    }

    public List<SourceFileCandidate> filesForLanguage(SourceLanguage language) {
        SourceLanguage normalized = language == null ? SourceLanguage.UNKNOWN : language;
        return files.stream().filter(file -> file.language() == normalized).toList();
    }

    public Map<SourceLanguage, Long> fileCountByLanguage() {
        return files.stream().collect(Collectors.groupingBy(SourceFileCandidate::language, Collectors.counting()));
    }

    public boolean hasFiles() {
        return !files.isEmpty();
    }
}

