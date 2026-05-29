package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.batch;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportPolicy;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownImportCandidate;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownImportCandidateKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.MarkdownFrontMatterSnapshot;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** Clasifica archivos de una carpeta raíz sin intentar parsearlos como proyecto todavía. */
public final class MarkdownImportCandidateClassifier {

    public MarkdownImportCandidate classify(
            Path file,
            MarkdownFrontMatterSnapshot frontMatter,
            Optional<DiagramTypeId> suffixDiagramType,
            MarkdownBatchImportPolicy policy
    ) {
        String fileName = file.getFileName() == null ? file.toString() : file.getFileName().toString();
        String normalizedName = normalize(fileName);
        if (!isMarkdown(normalizedName)) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_NOT_MARKDOWN, suffixDiagramType);
        }
        if (normalizedName.equals("readme.md") || normalizedName.equals("readme.markdown")) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_README, suffixDiagramType);
        }
        if (looksLikePrompt(normalizedName)) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_PROMPT, suffixDiagramType);
        }
        if (looksLikeGrammar(normalizedName)) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_GRAMMAR, suffixDiagramType);
        }
        if (frontMatter == null || !frontMatter.hasFrontMatter()) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_MISSING_FRONTMATTER, suffixDiagramType);
        }

        Optional<String> sampleKind = frontMatter.value("sample_kind").map(String::strip);
        if (sampleKind.map(this::isPromptKind).orElse(false)) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_PROMPT, suffixDiagramType);
        }
        if (sampleKind.map(this::isGrammarKind).orElse(false)) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_GRAMMAR, suffixDiagramType);
        }
        if (policy.respectImportableFlag() && frontMatter.booleanValue("importable").isPresent()
                && Boolean.FALSE.equals(frontMatter.booleanValue("importable").get())) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_IMPORTABLE_FALSE, suffixDiagramType);
        }
        if (policy.skipTemplatesByDefault() && sampleKind.map(this::isTemplateKind).orElse(false)) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_TEMPLATE, suffixDiagramType);
        }
        Optional<String> diagramType = frontMatter.value("diagram_type").map(String::strip).filter(value -> !value.isBlank());
        if (diagramType.isEmpty()) {
            return skipped(file, MarkdownImportCandidateKind.SKIPPED_MISSING_DIAGRAM_TYPE, suffixDiagramType);
        }
        return new MarkdownImportCandidate(
                file,
                fileName,
                MarkdownImportCandidateKind.PROJECT_CANDIDATE,
                diagramType,
                frontMatter.booleanValue("importable"),
                sampleKind,
                suffixDiagramType,
                List.of());
    }

    private MarkdownImportCandidate skipped(
            Path file,
            MarkdownImportCandidateKind kind,
            Optional<DiagramTypeId> suffixDiagramType
    ) {
        return new MarkdownImportCandidate(
                file,
                file.getFileName() == null ? file.toString() : file.getFileName().toString(),
                kind,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                suffixDiagramType,
                List.of());
    }

    private boolean isMarkdown(String normalizedName) {
        return normalizedName.endsWith(".md") || normalizedName.endsWith(".markdown");
    }

    private boolean looksLikePrompt(String normalizedName) {
        return normalizedName.contains("prompt") || normalizedName.contains("guia-ia") || normalizedName.contains("ia-prompt");
    }

    private boolean looksLikeGrammar(String normalizedName) {
        return normalizedName.contains("gramatica") || normalizedName.contains("grammar");
    }

    private boolean isPromptKind(String sampleKind) {
        String normalized = normalize(sampleKind);
        return normalized.contains("prompt") || normalized.equals("prompt-guide");
    }

    private boolean isGrammarKind(String sampleKind) {
        String normalized = normalize(sampleKind);
        return normalized.contains("grammar") || normalized.contains("gramatica");
    }

    private boolean isTemplateKind(String sampleKind) {
        String normalized = normalize(sampleKind);
        return normalized.equals("template") || normalized.equals("ai-template") || normalized.contains("plantilla");
    }

    private String normalize(String value) {
        return Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replace('_', '-')
                .replace(' ', '-');
    }
}
