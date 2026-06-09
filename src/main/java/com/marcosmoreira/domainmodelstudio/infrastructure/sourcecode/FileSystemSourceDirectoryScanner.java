package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceDirectoryScannerPort;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceFileCandidate;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootDetectionResult;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootDetectorPort;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceScanResult;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Escáner de archivos fuente basado en el sistema de archivos local. */
public final class FileSystemSourceDirectoryScanner implements SourceDirectoryScannerPort {
    private final SourceRootDetectorPort rootDetector;

    public FileSystemSourceDirectoryScanner() {
        this(new FileSystemSourceRootDetector());
    }

    public FileSystemSourceDirectoryScanner(SourceRootDetectorPort rootDetector) {
        this.rootDetector = rootDetector == null ? new FileSystemSourceRootDetector() : rootDetector;
    }

    @Override
    public SourceScanResult scan(SourceCodeImportRequest request) {
        SourceRootDetectionResult detection = rootDetector.detect(request);
        List<Path> ignoredPaths = new ArrayList<>();
        List<String> warnings = new ArrayList<>(detection.warnings());
        Map<FileLanguageKey, RankedCandidate> candidatesByFile = new HashMap<>();

        for (SourceRoot root : detection.sourceRoots()) {
            scanRoot(request, root, ignoredPaths, warnings, candidatesByFile);
        }

        List<SourceFileCandidate> files = candidatesByFile.values().stream()
                .sorted(Comparator.comparing(candidate -> candidate.file().relativePath().toString()))
                .map(RankedCandidate::file)
                .toList();
        return new SourceScanResult(detection.sourceRoots(), files, ignoredPaths, warnings);
    }

    private static void scanRoot(SourceCodeImportRequest request, SourceRoot root, List<Path> ignoredPaths,
                                 List<String> warnings, Map<FileLanguageKey, RankedCandidate> candidatesByFile) {
        if (!Files.isDirectory(root.path())) {
            warnings.add("La raíz de código no existe o no es directorio: " + root.path());
            return;
        }
        try {
            Files.walkFileTree(root.path(), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) {
                    if (!root.path().equals(directory) && SourceFileScanPolicy.isIgnoredDirectory(directory)) {
                        ignoredPaths.add(directory);
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    collectCandidate(request, root, file, ignoredPaths, candidatesByFile);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exception) {
                    warnings.add("No se pudo leer " + file + ": " + exception.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException exception) {
            warnings.add("No se pudo escanear la raíz " + root.path() + ": " + exception.getMessage());
        }
    }

    private static void collectCandidate(SourceCodeImportRequest request, SourceRoot root, Path file,
                                         List<Path> ignoredPaths,
                                         Map<FileLanguageKey, RankedCandidate> candidatesByFile) {
        String fileName = file.getFileName() == null ? "" : file.getFileName().toString();
        if (!SourceFileScanPolicy.isSourceFileName(fileName)) {
            return;
        }
        SourceLanguage language = SourceFileScanPolicy.languageForFileName(fileName);
        if (!root.supports(language)) {
            return;
        }
        Path relativePath = root.path().relativize(file);
        if (!SourceFileScanPolicy.shouldIncludeFile(request, relativePath, language)) {
            ignoredPaths.add(file);
            return;
        }
        SourceFileCandidate candidate = new SourceFileCandidate(file.toAbsolutePath().normalize(), relativePath,
                language, root.id());
        keepMostSpecificRoot(candidatesByFile, root, candidate);
    }

    private static void keepMostSpecificRoot(Map<FileLanguageKey, RankedCandidate> candidatesByFile,
                                             SourceRoot root, SourceFileCandidate candidate) {
        FileLanguageKey key = new FileLanguageKey(candidate.absolutePath(), candidate.language());
        int depth = root.path().toAbsolutePath().normalize().getNameCount();
        RankedCandidate existing = candidatesByFile.get(key);
        if (existing == null || depth >= existing.sourceRootDepth()) {
            candidatesByFile.put(key, new RankedCandidate(candidate, depth));
        }
    }

    private record FileLanguageKey(Path absolutePath, SourceLanguage language) {
    }

    private record RankedCandidate(SourceFileCandidate file, int sourceRootDepth) {
    }
}
