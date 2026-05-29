package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportPolicy;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownImportCandidateKind;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.MarkdownFrontMatterProbe;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MarkdownImportCandidateClassifierTest {

    private final MarkdownImportCandidateClassifier classifier = new MarkdownImportCandidateClassifier();
    private final MarkdownFrontMatterProbe probe = new MarkdownFrontMatterProbe();

    @Test
    void ignoresReadmePromptsAndFilesWithoutFrontMatter() {
        assertEquals(MarkdownImportCandidateKind.SKIPPED_README,
                classifier.classify(Path.of("README.md"), probe.read(""), Optional.empty(), MarkdownBatchImportPolicy.defaultPolicy()).kind());
        assertEquals(MarkdownImportCandidateKind.SKIPPED_PROMPT,
                classifier.classify(Path.of("19_grafo_logico_prompt_ia.md"), probe.read(""), Optional.empty(), MarkdownBatchImportPolicy.defaultPolicy()).kind());
        assertEquals(MarkdownImportCandidateKind.SKIPPED_MISSING_FRONTMATTER,
                classifier.classify(Path.of("notas.md"), probe.read("# Notas"), Optional.empty(), MarkdownBatchImportPolicy.defaultPolicy()).kind());
    }

    @Test
    void skipsTemplatesAndImportableFalseButAcceptsProjects() {
        assertEquals(MarkdownImportCandidateKind.SKIPPED_TEMPLATE,
                classifier.classify(Path.of("uml_class.md"), probe.read("""
                        ---
                        diagram_type: "uml-class"
                        importable: true
                        sample_kind: "template"
                        ---
                        """), Optional.empty(), MarkdownBatchImportPolicy.defaultPolicy()).kind());
        assertEquals(MarkdownImportCandidateKind.SKIPPED_IMPORTABLE_FALSE,
                classifier.classify(Path.of("conceptual_model.md"), probe.read("""
                        ---
                        diagram_type: "conceptual-model"
                        importable: false
                        ---
                        """), Optional.empty(), MarkdownBatchImportPolicy.defaultPolicy()).kind());
        assertEquals(MarkdownImportCandidateKind.PROJECT_CANDIDATE,
                classifier.classify(Path.of("01_levantamiento-logico.md"), probe.read("""
                        ---
                        diagram_type: "logical-business-intake"
                        importable: true
                        sample_kind: "project"
                        ---
                        """), Optional.empty(), MarkdownBatchImportPolicy.defaultPolicy()).kind());
    }
}
