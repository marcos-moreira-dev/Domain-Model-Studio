package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.batch;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchCandidateReader;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportPolicy;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownImportCandidate;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.MarkdownFrontMatterProbe;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.MarkdownFrontMatterSnapshot;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/** Lector de filesystem para una carpeta raíz de proyectos Markdown. */
public final class FileSystemMarkdownBatchReader implements MarkdownBatchCandidateReader {

    private final MarkdownFrontMatterProbe frontMatterProbe;
    private final MarkdownFileTypeSuffixResolver suffixResolver;
    private final MarkdownImportCandidateClassifier classifier;

    public FileSystemMarkdownBatchReader() {
        this(new MarkdownFrontMatterProbe(), new MarkdownFileTypeSuffixResolver(), new MarkdownImportCandidateClassifier());
    }

    public FileSystemMarkdownBatchReader(
            MarkdownFrontMatterProbe frontMatterProbe,
            MarkdownFileTypeSuffixResolver suffixResolver,
            MarkdownImportCandidateClassifier classifier
    ) {
        this.frontMatterProbe = Objects.requireNonNull(frontMatterProbe, "frontMatterProbe");
        this.suffixResolver = Objects.requireNonNull(suffixResolver, "suffixResolver");
        this.classifier = Objects.requireNonNull(classifier, "classifier");
    }

    @Override
    public List<MarkdownImportCandidate> readCandidates(Path sourceRoot, MarkdownBatchImportPolicy policy) throws IOException {
        Objects.requireNonNull(sourceRoot, "sourceRoot");
        MarkdownBatchImportPolicy effectivePolicy = policy == null ? MarkdownBatchImportPolicy.defaultPolicy() : policy;
        if (!Files.isDirectory(sourceRoot)) {
            throw new IOException("La ruta seleccionada no es una carpeta: " + sourceRoot.toAbsolutePath());
        }
        try (Stream<Path> stream = effectivePolicy.recursive() ? Files.walk(sourceRoot) : Files.list(sourceRoot)) {
            return stream
                    .filter(path -> !path.equals(sourceRoot))
                    .filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(path -> sourceRoot.relativize(path).toString().toLowerCase()))
                    .map(path -> classify(path, effectivePolicy))
                    .toList();
        }
    }

    private MarkdownImportCandidate classify(Path file, MarkdownBatchImportPolicy policy) {
        Optional<DiagramTypeId> suffixType = suffixResolver.resolve(file.getFileName() == null ? file.toString() : file.getFileName().toString());
        MarkdownFrontMatterSnapshot snapshot = MarkdownFrontMatterSnapshot.none();
        if (isMarkdown(file)) {
            try {
                snapshot = frontMatterProbe.read(file);
            } catch (IOException exception) {
                // La importación real reportará IO si el archivo era candidato; aquí basta con clasificarlo como no importable.
                snapshot = MarkdownFrontMatterSnapshot.none();
            }
        }
        return classifier.classify(file, snapshot, suffixType, policy);
    }

    private boolean isMarkdown(Path file) {
        String name = file.getFileName() == null ? file.toString() : file.getFileName().toString().toLowerCase();
        return name.endsWith(".md") || name.endsWith(".markdown");
    }
}
