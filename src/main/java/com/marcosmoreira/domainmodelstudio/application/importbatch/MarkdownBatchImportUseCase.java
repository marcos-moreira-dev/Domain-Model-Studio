package com.marcosmoreira.domainmodelstudio.application.importbatch;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelResult;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelUseCase;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CancellationException;

/**
 * Caso de uso para importar varios proyectos Markdown desde una carpeta raíz.
 *
 * <p>No abre diálogos ni toca JavaFX. La presentación elige la carpeta y abre los
 * proyectos válidos en pestañas. Los archivos que no son proyectos se reportan como
 * omitidos y no bloquean el lote.</p>
 */
public final class MarkdownBatchImportUseCase {

    private final MarkdownBatchCandidateReader candidateReader;
    private final ImportMarkdownModelUseCase importMarkdownModelUseCase;
    private final DiagramTypeRegistry diagramTypeRegistry;

    public MarkdownBatchImportUseCase(
            MarkdownBatchCandidateReader candidateReader,
            ImportMarkdownModelUseCase importMarkdownModelUseCase,
            DiagramTypeRegistry diagramTypeRegistry
    ) {
        this.candidateReader = Objects.requireNonNull(candidateReader, "candidateReader");
        this.importMarkdownModelUseCase = Objects.requireNonNull(importMarkdownModelUseCase, "importMarkdownModelUseCase");
        this.diagramTypeRegistry = Objects.requireNonNull(diagramTypeRegistry, "diagramTypeRegistry");
    }

    public MarkdownBatchImportResult importFolder(MarkdownBatchImportRequest request) throws IOException {
        Objects.requireNonNull(request, "request");
        ensureNotCancelled();
        List<MarkdownImportCandidate> candidates = candidateReader.readCandidates(request.sourceRoot(), request.policy());
        ensureNotCancelled();
        List<MarkdownBatchImportItemResult> results = new ArrayList<>();
        int processedProjectCandidates = 0;
        for (MarkdownImportCandidate candidate : candidates) {
            ensureNotCancelled();
            if (!candidate.projectCandidate()) {
                results.add(MarkdownBatchImportItemResult.skipped(candidate, mapSkippedStatus(candidate.kind())));
                continue;
            }
            processedProjectCandidates++;
            if (processedProjectCandidates > request.policy().maxFiles()) {
                results.add(rejected(candidate, Optional.empty(), MarkdownBatchImportItemStatus.REJECTED_LIMIT_EXCEEDED,
                        "La carpeta contiene más de " + request.policy().maxFiles() + " proyectos Markdown candidatos."));
                continue;
            }
            results.add(importCandidate(candidate, request.policy()));
        }
        return summarize(request.sourceRoot(), candidates.size(), results);
    }


    private void ensureNotCancelled() {
        if (Thread.currentThread().isInterrupted()) {
            throw new CancellationException("Importación Markdown cancelada por el usuario.");
        }
    }

    private MarkdownBatchImportItemResult importCandidate(
            MarkdownImportCandidate candidate,
            MarkdownBatchImportPolicy policy
    ) {
        Optional<DiagramTypeId> declaredType = candidate.declaredDiagramType().map(this::diagramTypeIdFor);
        if (declaredType.isEmpty()) {
            return rejected(candidate, Optional.empty(), MarkdownBatchImportItemStatus.REJECTED_UNKNOWN_DIAGRAM_TYPE,
                    "El archivo declara un diagram_type vacío o inválido.");
        }

        Optional<DiagramTypeDescriptor> descriptor = diagramTypeRegistry.findById(declaredType.get());
        if (descriptor.isEmpty()) {
            return rejected(candidate, declaredType, MarkdownBatchImportItemStatus.REJECTED_UNKNOWN_DIAGRAM_TYPE,
                    "Tipo de proyecto no registrado: " + declaredType.get().value());
        }
        if (!descriptor.get().supports(DiagramCapability.IMPORT_MARKDOWN)) {
            return rejected(candidate, declaredType, MarkdownBatchImportItemStatus.REJECTED_UNSUPPORTED_DIAGRAM_TYPE,
                    "El tipo no declara IMPORT_MARKDOWN: " + descriptor.get().displayName());
        }

        List<String> warnings = new ArrayList<>(candidate.notes());
        if (candidate.suffixDiagramType().isPresent() && !candidate.suffixDiagramType().get().equals(declaredType.get())) {
            String message = "El nombre del archivo sugiere " + candidate.suffixDiagramType().get().value()
                    + ", pero el frontmatter declara " + declaredType.get().value() + ".";
            if (policy.strictSuffixMatch()) {
                return rejected(candidate, declaredType, MarkdownBatchImportItemStatus.REJECTED_SUFFIX_MISMATCH, message);
            }
            warnings.add(message + " Se usó diagram_type como fuente principal.");
        }

        try {
            ImportMarkdownModelResult importResult = importMarkdownModelUseCase.importFile(candidate.sourceFile());
            MarkdownBatchImportItemStatus status = importedStatus(importResult);
            warnings.addAll(importResult.validationResult().warnings().stream()
                    .map(issue -> issue.message())
                    .toList());
            if (importResult.validationResult().hasErrors()) {
                warnings.addAll(importResult.validationResult().errors().stream()
                        .map(issue -> issue.message())
                        .toList());
            }
            return new MarkdownBatchImportItemResult(
                    candidate.sourceFile(),
                    candidate.displayName(),
                    declaredType,
                    candidate.suffixDiagramType(),
                    status,
                    Optional.of(importResult),
                    warnings,
                    Optional.empty());
        } catch (IOException exception) {
            return rejected(candidate, declaredType, MarkdownBatchImportItemStatus.REJECTED_IO_ERROR, exception.getMessage());
        } catch (MarkdownModelParsingException | IllegalArgumentException exception) {
            return rejected(candidate, declaredType, MarkdownBatchImportItemStatus.REJECTED_PARSE_ERROR, exception.getMessage());
        }
    }

    private MarkdownBatchImportResult summarize(
            java.nio.file.Path sourceRoot,
            int scannedFiles,
            List<MarkdownBatchImportItemResult> results
    ) {
        int imported = (int) results.stream().filter(MarkdownBatchImportItemResult::imported).count();
        int skipped = (int) results.stream().filter(MarkdownBatchImportItemResult::skipped).count();
        int rejected = (int) results.stream().filter(MarkdownBatchImportItemResult::rejected).count();
        int projectCandidates = (int) results.stream()
                .filter(item -> !item.status().skipped())
                .count();
        return new MarkdownBatchImportResult(
                sourceRoot,
                scannedFiles,
                projectCandidates,
                imported,
                skipped,
                rejected,
                results,
                List.of());
    }

    private MarkdownBatchImportItemResult rejected(
            MarkdownImportCandidate candidate,
            Optional<DiagramTypeId> declaredType,
            MarkdownBatchImportItemStatus status,
            String errorMessage
    ) {
        return new MarkdownBatchImportItemResult(
                candidate.sourceFile(),
                candidate.displayName(),
                declaredType,
                candidate.suffixDiagramType(),
                status,
                Optional.empty(),
                candidate.notes(),
                Optional.ofNullable(errorMessage == null || errorMessage.isBlank() ? "Error no especificado." : errorMessage));
    }

    private MarkdownBatchImportItemStatus importedStatus(ImportMarkdownModelResult result) {
        if (result.validationResult().hasErrors()) {
            return MarkdownBatchImportItemStatus.IMPORTED_WITH_VALIDATION_ERRORS;
        }
        if (result.validationResult().hasWarnings()) {
            return MarkdownBatchImportItemStatus.IMPORTED_WITH_WARNINGS;
        }
        return MarkdownBatchImportItemStatus.IMPORTED;
    }

    private MarkdownBatchImportItemStatus mapSkippedStatus(MarkdownImportCandidateKind kind) {
        return switch (kind) {
            case SKIPPED_NOT_MARKDOWN -> MarkdownBatchImportItemStatus.SKIPPED_NOT_MARKDOWN;
            case SKIPPED_README -> MarkdownBatchImportItemStatus.SKIPPED_README;
            case SKIPPED_MISSING_FRONTMATTER -> MarkdownBatchImportItemStatus.SKIPPED_MISSING_FRONTMATTER;
            case SKIPPED_MISSING_DIAGRAM_TYPE -> MarkdownBatchImportItemStatus.SKIPPED_MISSING_DIAGRAM_TYPE;
            case SKIPPED_IMPORTABLE_FALSE -> MarkdownBatchImportItemStatus.SKIPPED_IMPORTABLE_FALSE;
            case SKIPPED_TEMPLATE -> MarkdownBatchImportItemStatus.SKIPPED_TEMPLATE;
            case SKIPPED_PROMPT -> MarkdownBatchImportItemStatus.SKIPPED_PROMPT;
            case SKIPPED_GRAMMAR -> MarkdownBatchImportItemStatus.SKIPPED_GRAMMAR;
            case PROJECT_CANDIDATE -> throw new IllegalArgumentException("Un proyecto candidato no puede mapearse como omitido.");
        };
    }

    private DiagramTypeId diagramTypeIdFor(String rawValue) {
        String normalized = rawValue == null ? "" : rawValue.strip()
                .toLowerCase(Locale.ROOT)
                .replace('_', '-')
                .replace(' ', '-');
        if (normalized.equals("conceptualmodel")) {
            return DiagramTypeId.CONCEPTUAL_MODEL;
        }
        return DiagramTypeId.of(normalized);
    }
}
