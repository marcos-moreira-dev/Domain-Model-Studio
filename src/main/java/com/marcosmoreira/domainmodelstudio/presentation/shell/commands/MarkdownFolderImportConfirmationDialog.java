package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.presentation.dialogs.GuidedActionDialog;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/** Confirmación explícita antes de iniciar una importación Markdown potencialmente masiva. */
final class MarkdownFolderImportConfirmationDialog {

    private static final int PREVIEW_LIMIT = 80;
    private static final int SCAN_LIMIT = 501;

    private final GuidedActionDialog.Request request;

    private MarkdownFolderImportConfirmationDialog(GuidedActionDialog.Request request) {
        this.request = request;
    }

    static MarkdownFolderImportConfirmationDialog forFolder(Path sourceRoot) {
        ImportPreview preview = ImportPreview.scan(sourceRoot);
        return new MarkdownFolderImportConfirmationDialog(new GuidedActionDialog.Request(
                "Confirmar importación Markdown",
                "Abrir carpeta Markdown de forma recursiva",
                summary(sourceRoot, preview),
                "Candidatos Markdown que se revisarán:",
                preview.detail(),
                "Importar",
                "Cancelar"));
    }

    Optional<Boolean> showAndWait() {
        return GuidedActionDialog.confirm(null, request);
    }

    private static String summary(Path sourceRoot, ImportPreview preview) {
        return "La aplicación revisará la carpeta y sus subcarpetas para abrir los Markdown compatibles con Domain Model Studio."
                + "\n\nCarpeta raíz: " + sourceRoot.toAbsolutePath()
                + "\n\nCandidatos Markdown detectados para revisión previa: " + preview.detectedCountLabel()
                + "\nSe importarán como máximo 500 proyectos candidatos."
                + "\nLos auxiliares, no catalogados o fuera de gramática quedarán en el reporte final para corregirlos con IA."
                + "\n\nEl procesamiento empezará solo después de presionar Importar.";
    }

    private record ImportPreview(int detectedCount, boolean truncated, List<String> relativeMarkdownFiles, String error) {

        static ImportPreview scan(Path sourceRoot) {
            try (Stream<Path> stream = Files.walk(sourceRoot)) {
                List<String> files = stream
                        .filter(path -> !path.equals(sourceRoot))
                        .filter(Files::isRegularFile)
                        .filter(MarkdownFolderImportConfirmationDialog::isMarkdown)
                        .sorted(Comparator.comparing(path -> sourceRoot.relativize(path).toString().toLowerCase()))
                        .limit(SCAN_LIMIT)
                        .map(path -> normalize(sourceRoot.relativize(path).toString()))
                        .toList();
                boolean truncated = files.size() >= SCAN_LIMIT;
                int detected = truncated ? 500 : files.size();
                return new ImportPreview(detected, truncated, files.stream().limit(PREVIEW_LIMIT).toList(), null);
            } catch (IOException exception) {
                return new ImportPreview(0, false, List.of(), exception.getMessage());
            }
        }

        String detectedCountLabel() {
            return truncated ? detectedCount + "+" : Integer.toString(detectedCount);
        }

        String detail() {
            if (error != null && !error.isBlank()) {
                return "No se pudo preparar la vista previa: " + error
                        + "\n\nPuedes intentar importar de todas formas; el reporte final indicará los problemas reales.";
            }
            if (relativeMarkdownFiles.isEmpty()) {
                return "No se detectaron archivos .md/.markdown en la revisión previa."
                        + "\n\nPuedes cancelar y revisar la carpeta seleccionada.";
            }
            StringBuilder text = new StringBuilder();
            text.append("La lista siguiente es una vista previa de candidatos Markdown; la importación real validará frontmatter, diagram_type y gramática oficial.\n\n");
            for (String file : relativeMarkdownFiles) {
                text.append("- ").append(file).append('\n');
            }
            if (truncated || detectedCount > relativeMarkdownFiles.size()) {
                text.append("\n... ").append(remainingLabel()).append(" más no mostrados en esta vista previa.");
            }
            return text.toString();
        }

        private String remainingLabel() {
            int remaining = Math.max(0, detectedCount - relativeMarkdownFiles.size());
            return truncated ? remaining + "+" : Integer.toString(remaining);
        }
    }

    private static boolean isMarkdown(Path file) {
        String name = file.getFileName() == null ? file.toString() : file.getFileName().toString().toLowerCase();
        return name.endsWith(".md") || name.endsWith(".markdown");
    }

    private static String normalize(String value) {
        return value.replace('\\', '/');
    }
}
