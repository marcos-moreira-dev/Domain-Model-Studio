package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportItemResult;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportItemStatus;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportResult;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Construye reportes legibles y copiables para importación masiva de carpetas Markdown. */
final class MarkdownFolderImportReportFormatter {

    private static final int SUMMARY_ITEM_LIMIT = 12;

    String buildSummary(MarkdownBatchImportResult result) {
        Objects.requireNonNull(result, "result");
        long problemMarkdown = correctionCandidateItems(result).size();
        String correctionGuidance = problemMarkdown == 0
                ? "Todo está en orden. No se detectaron Markdown no catalogados, rechazados o fuera de gramática."
                : "Hay " + problemMarkdown + " Markdown no importados o rechazados. Usa Copiar problemas para pedir a la IA que los corrija contra la gramática oficial.";
        return "Carpeta Markdown revisada de forma recursiva.\n"
                + "Proyectos importados: " + result.importedCount() + "\n"
                + "Markdown problemáticos para corregir: " + problemMarkdown + "\n"
                + "Omitidos totales: " + result.skippedCount() + "\n"
                + "Rechazados: " + result.rejectedCount() + "\n"
                + "\n" + correctionGuidance;
    }


    String buildSelectedFilesSummary(MarkdownBatchImportResult result) {
        Objects.requireNonNull(result, "result");
        long problemMarkdown = correctionCandidateItems(result).size();
        String unit = result.scannedFiles() == 1 ? "archivo Markdown seleccionado" : "archivos Markdown seleccionados";
        String correctionGuidance = problemMarkdown == 0
                ? "Todo está en orden. No se detectaron Markdown seleccionados rechazados o fuera de gramática."
                : "Hay " + problemMarkdown + " Markdown seleccionados no importados o rechazados. Usa Copiar problemas para corregirlos con la gramática oficial.";
        return "Selección Markdown revisada.\n"
                + "Archivos revisados: " + result.scannedFiles() + " " + unit + "\n"
                + "Proyectos importados: " + result.importedCount() + "\n"
                + "Markdown problemáticos para corregir: " + problemMarkdown + "\n"
                + "Rechazados: " + result.rejectedCount() + "\n"
                + "\n" + correctionGuidance;
    }

    String buildDetailedReport(MarkdownBatchImportResult result) {
        Objects.requireNonNull(result, "result");
        StringBuilder report = new StringBuilder();
        report.append("# Reporte de importación Markdown recursiva")
                .append("\n\nCarpeta raíz: ").append(result.sourceRoot().toAbsolutePath())
                .append("\nArchivos revisados: ").append(result.scannedFiles())
                .append("\nCandidatos de proyecto: ").append(result.candidateFiles())
                .append("\nProyectos importados: ").append(result.importedCount())
                .append("\nOmitidos: ").append(result.skippedCount())
                .append("\nRechazados: ").append(result.rejectedCount())
                .append("\n\nUsa este reporte para corregir con IA los Markdown que no respetan la gramática oficial de Domain Model Studio.");
        appendSection(report, result, "Proyectos importados", result.importedItems());
        appendSection(report, result, "Archivos Markdown no catalogados", nonCatalogedMarkdownItems(result));
        appendSection(report, result, "Archivos no Markdown omitidos", nonMarkdownItems(result));
        return report.toString();
    }

    boolean hasCorrectionCandidates(MarkdownBatchImportResult result) {
        return !correctionCandidateItems(result).isEmpty();
    }

    String buildCorrectionReport(MarkdownBatchImportResult result) {
        Objects.requireNonNull(result, "result");
        List<MarkdownBatchImportItemResult> items = correctionCandidateItems(result);
        if (items.isEmpty()) {
            return "# Markdown problemáticos para corregir\n\nNo se detectaron Markdown no catalogados ni rechazados. No necesitas pedir corrección a la IA para esta importación.";
        }
        StringBuilder report = new StringBuilder();
        report.append("# Markdown problemáticos para corregir")
                .append("\n\nCarpeta raíz: ").append(result.sourceRoot().toAbsolutePath())
                .append("\nArchivos para revisar/corregir: ").append(items.size())
                .append("\n\nPega este bloque a la IA junto con la gramática oficial de Domain Model Studio. Pídele que corrija solo estos Markdown para que sean importables.");
        appendSection(report, result, "Archivos Markdown no catalogados o rechazados", items);
        return report.toString();
    }

    String previewList(MarkdownBatchImportResult result, java.util.function.Predicate<MarkdownBatchImportItemResult> filter) {
        Objects.requireNonNull(result, "result");
        var items = result.items().stream().filter(filter).limit(SUMMARY_ITEM_LIMIT).toList();
        if (items.isEmpty()) {
            return "";
        }
        StringBuilder preview = new StringBuilder();
        for (MarkdownBatchImportItemResult item : items) {
            preview.append("\n- ").append(relativePath(result, item.sourceFile()))
                    .append(" → ").append(readableStatus(item.status()));
            item.errorMessage().ifPresent(error -> preview.append(" — ").append(error));
        }
        long total = result.items().stream().filter(filter).count();
        if (total > SUMMARY_ITEM_LIMIT) {
            preview.append("\n- ... ").append(total - SUMMARY_ITEM_LIMIT).append(" más en el reporte detallado");
        }
        return preview.toString();
    }

    private void appendSection(
            StringBuilder report,
            MarkdownBatchImportResult result,
            String title,
            List<MarkdownBatchImportItemResult> items
    ) {
        report.append("\n\n## ").append(title);
        if (items.isEmpty()) {
            report.append("\n\nSin archivos en esta categoría.");
            return;
        }
        items.stream()
                .sorted(Comparator.comparing(item -> relativePath(result, item.sourceFile()).toLowerCase()))
                .forEach(item -> appendItem(report, result, item));
    }

    private void appendItem(StringBuilder report, MarkdownBatchImportResult result, MarkdownBatchImportItemResult item) {
        report.append("\n\n- Archivo: ").append(relativePath(result, item.sourceFile()))
                .append("\n  Estado: ").append(readableStatus(item.status()));
        item.declaredDiagramType().ifPresent(type -> report.append("\n  diagram_type: ").append(type.value()));
        item.suffixDiagramType().ifPresent(type -> report.append("\n  Tipo sugerido por nombre: ").append(type.value()));
        item.errorMessage().ifPresent(error -> report.append("\n  Error: ").append(error));
        if (!item.warnings().isEmpty()) {
            report.append("\n  Advertencias:");
            for (String warning : item.warnings()) {
                report.append("\n    - ").append(warning);
            }
        }
    }

    private List<MarkdownBatchImportItemResult> correctionCandidateItems(MarkdownBatchImportResult result) {
        return result.items().stream()
                .filter(item -> !item.imported())
                .filter(item -> item.status() != MarkdownBatchImportItemStatus.SKIPPED_NOT_MARKDOWN)
                .filter(item -> isMarkdown(item.sourceFile()))
                .toList();
    }

    private List<MarkdownBatchImportItemResult> nonCatalogedMarkdownItems(MarkdownBatchImportResult result) {
        return correctionCandidateItems(result);
    }

    private List<MarkdownBatchImportItemResult> nonMarkdownItems(MarkdownBatchImportResult result) {
        return result.items().stream()
                .filter(item -> item.status() == MarkdownBatchImportItemStatus.SKIPPED_NOT_MARKDOWN)
                .toList();
    }

    private String relativePath(MarkdownBatchImportResult result, Path file) {
        try {
            return result.sourceRoot().toAbsolutePath().normalize()
                    .relativize(file.toAbsolutePath().normalize())
                    .toString()
                    .replace('\\', '/');
        } catch (IllegalArgumentException exception) {
            return file.toString();
        }
    }

    private boolean isMarkdown(Path file) {
        String name = file.getFileName() == null ? file.toString() : file.getFileName().toString().toLowerCase();
        return name.endsWith(".md") || name.endsWith(".markdown");
    }

    private String readableStatus(MarkdownBatchImportItemStatus status) {
        return switch (status) {
            case IMPORTED -> "Importado";
            case IMPORTED_WITH_WARNINGS -> "Importado con advertencias";
            case IMPORTED_WITH_VALIDATION_ERRORS -> "Importado con errores de validación";
            case SKIPPED_NOT_MARKDOWN -> "Omitido: no es Markdown";
            case SKIPPED_README -> "Omitido: README";
            case SKIPPED_MISSING_FRONTMATTER -> "Omitido: falta frontmatter";
            case SKIPPED_MISSING_DIAGRAM_TYPE -> "Omitido: falta diagram_type";
            case SKIPPED_IMPORTABLE_FALSE -> "Omitido: importable=false";
            case SKIPPED_TEMPLATE -> "Omitido: plantilla no rellena";
            case SKIPPED_PROMPT -> "Omitido: prompt/guía IA";
            case SKIPPED_GRAMMAR -> "Omitido: gramática/documentación";
            case REJECTED_UNKNOWN_DIAGRAM_TYPE -> "Rechazado: diagram_type no registrado";
            case REJECTED_UNSUPPORTED_DIAGRAM_TYPE -> "Rechazado: tipo sin importación Markdown";
            case REJECTED_SUFFIX_MISMATCH -> "Rechazado: nombre y frontmatter no coinciden";
            case REJECTED_PARSE_ERROR -> "Rechazado: error de gramática/parsing";
            case REJECTED_IO_ERROR -> "Rechazado: error de lectura";
            case REJECTED_LIMIT_EXCEEDED -> "Rechazado: límite de candidatos excedido";
        };
    }
}
