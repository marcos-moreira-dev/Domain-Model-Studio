package com.marcosmoreira.domainmodelstudio.infrastructure.batchexport;

import com.marcosmoreira.domainmodelstudio.application.batchexport.ClientBatchExportItemResult;
import com.marcosmoreira.domainmodelstudio.application.batchexport.ClientBatchExportRequest;
import com.marcosmoreira.domainmodelstudio.application.batchexport.ClientBatchExportResult;
import com.marcosmoreira.domainmodelstudio.application.batchexport.ClientBatchExportService;
import com.marcosmoreira.domainmodelstudio.application.batchexport.OpenProjectExportItem;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.ExportDataDictionaryMarkdownUseCase;
import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.application.export.PdfDiagramExporter;
import com.marcosmoreira.domainmodelstudio.application.export.SvgDiagramExporter;
import com.marcosmoreira.domainmodelstudio.application.project.ProjectRepository;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Exportación por lote al sistema de archivos con estructura input/editable/output. */
public final class FileSystemClientBatchExporter implements ClientBatchExportService {

    private static final DateTimeFormatter EXPORT_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final ProjectRepository projectRepository;
    private final SvgDiagramExporter svgDiagramExporter;
    private final MarkdownDiagramExporter markdownDiagramExporter;
    private final ExportDataDictionaryMarkdownUseCase dataDictionaryMarkdownExporter;
    private final PdfDiagramExporter pdfDiagramExporter;
    private final ExportFolderNamePolicy folderNamePolicy;

    public FileSystemClientBatchExporter(
            ProjectRepository projectRepository,
            SvgDiagramExporter svgDiagramExporter,
            MarkdownDiagramExporter markdownDiagramExporter,
            ExportDataDictionaryMarkdownUseCase dataDictionaryMarkdownExporter,
            PdfDiagramExporter pdfDiagramExporter
    ) {
        this(projectRepository, svgDiagramExporter, markdownDiagramExporter, dataDictionaryMarkdownExporter,
                pdfDiagramExporter, new ExportFolderNamePolicy());
    }

    public FileSystemClientBatchExporter(
            ProjectRepository projectRepository,
            SvgDiagramExporter svgDiagramExporter,
            MarkdownDiagramExporter markdownDiagramExporter,
            ExportDataDictionaryMarkdownUseCase dataDictionaryMarkdownExporter,
            PdfDiagramExporter pdfDiagramExporter,
            ExportFolderNamePolicy folderNamePolicy
    ) {
        this.projectRepository = Objects.requireNonNull(projectRepository, "projectRepository");
        this.svgDiagramExporter = Objects.requireNonNull(svgDiagramExporter, "svgDiagramExporter");
        this.markdownDiagramExporter = Objects.requireNonNull(markdownDiagramExporter, "markdownDiagramExporter");
        this.dataDictionaryMarkdownExporter = Objects.requireNonNull(dataDictionaryMarkdownExporter, "dataDictionaryMarkdownExporter");
        this.pdfDiagramExporter = Objects.requireNonNull(pdfDiagramExporter, "pdfDiagramExporter");
        this.folderNamePolicy = Objects.requireNonNull(folderNamePolicy, "folderNamePolicy");
    }

    @Override
    public ClientBatchExportResult export(ClientBatchExportRequest request) throws IOException {
        Objects.requireNonNull(request, "request");
        Path rootFolder = request.destinationRoot().resolve(rootFolderName(request));
        List<Path> createdFolders = new ArrayList<>();
        List<Path> exportedFiles = new ArrayList<>();
        List<ClientBatchExportItemResult> itemResults = new ArrayList<>();
        List<String> notes = new ArrayList<>();

        createDirectory(rootFolder, createdFolders);
        for (int index = 0; index < request.items().size(); index++) {
            OpenProjectExportItem item = request.items().get(index);
            ClientBatchExportItemResult itemResult = exportItem(index + 1, rootFolder, item, createdFolders, exportedFiles, notes);
            itemResults.add(itemResult);
        }

        Path readme = rootFolder.resolve("README_EXPORTACION.md");
        Files.writeString(readme, buildReadme(request, rootFolder, itemResults), StandardCharsets.UTF_8);
        exportedFiles.add(readme);

        Path manifest = rootFolder.resolve("MANIFEST_EXPORTACION.md");
        Files.writeString(manifest, buildManifest(request, rootFolder, itemResults, notes), StandardCharsets.UTF_8);
        exportedFiles.add(manifest);

        return new ClientBatchExportResult(rootFolder, manifest, createdFolders, exportedFiles, itemResults, notes);
    }

    private ClientBatchExportItemResult exportItem(
            int index,
            Path rootFolder,
            OpenProjectExportItem item,
            List<Path> createdFolders,
            List<Path> exportedFiles,
            List<String> notes
    ) throws IOException {
        String baseFileName = folderNamePolicy.toFolderName(item.suggestedFileStem());
        Path projectFolder = rootFolder.resolve(String.format("%02d_%s", index, baseFileName));
        Path inputFolder = projectFolder.resolve("input");
        Path editableFolder = projectFolder.resolve("editable");
        Path outputFolder = projectFolder.resolve("output");
        createDirectory(projectFolder, createdFolders);
        createDirectory(inputFolder, createdFolders);
        createDirectory(editableFolder, createdFolders);
        createDirectory(outputFolder, createdFolders);

        List<Path> itemFiles = new ArrayList<>();
        exportMarkdownInput(item, inputFolder.resolve(baseFileName + ".md"), itemFiles, notes);
        exportEditableDms(item, editableFolder.resolve(baseFileName + ".dms"), itemFiles);
        if (item.usesScopedOutputProject()) {
            notes.add(item.displayName() + ": salidas de output generadas desde la vista visual activa.");
        }
        if (item.exportMarkdown()) {
            exportMarkdownOutput(item, outputFolder.resolve(baseFileName + "_actualizado.md"), itemFiles, notes);
        }
        if (item.exportSvg()) {
            exportSvg(item, outputFolder.resolve(baseFileName + ".svg"), itemFiles, notes);
        }
        if (item.exportPdf()) {
            exportPdf(item, outputFolder.resolve(baseFileName + ".pdf"), itemFiles, notes);
        }
        Optional<Path> pngTarget = item.exportPngSnapshot()
                ? Optional.of(outputFolder.resolve(baseFileName + ".png"))
                : Optional.empty();
        pngTarget.ifPresent(path -> notes.add(item.displayName()
                + ": PNG se genera desde la vista visual activa durante el cierre de la exportación."));
        exportedFiles.addAll(itemFiles);
        return new ClientBatchExportItemResult(
                item.tabId(),
                item.displayName(),
                projectFolder,
                inputFolder,
                editableFolder,
                outputFolder,
                baseFileName,
                itemFiles,
                pngTarget);
    }

    private void exportMarkdownInput(
            OpenProjectExportItem item,
            Path targetFile,
            List<Path> itemFiles,
            List<String> notes
    ) throws IOException {
        Optional<Path> sourceMarkdown = item.sourceMarkdown().filter(Files::exists).filter(Files::isRegularFile);
        if (sourceMarkdown.isPresent()) {
            Files.copy(sourceMarkdown.get(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            itemFiles.add(targetFile);
            return;
        }
        if (item.exportMarkdown()) {
            writeMarkdown(item, item.project(), targetFile);
            itemFiles.add(targetFile);
            notes.add(item.displayName() + ": se generó input Markdown desde el proyecto porque no se encontró archivo fuente original.");
            return;
        }
        Path noteFile = targetFile.resolveSibling("README_INPUT.md");
        Files.writeString(noteFile,
                "# Input Markdown no disponible\n\n"
                        + "Esta pestaña no declara todavía un Markdown exportable.\n"
                        + "El archivo editable principal de esta carpeta es el `.dms`.\n",
                StandardCharsets.UTF_8);
        itemFiles.add(noteFile);
    }

    private void exportMarkdownOutput(
            OpenProjectExportItem item,
            Path targetFile,
            List<Path> itemFiles,
            List<String> notes
    ) throws IOException {
        try {
            writeMarkdown(item, item.outputProject(), targetFile);
            itemFiles.add(targetFile);
        } catch (IllegalArgumentException exception) {
            notes.add(item.displayName() + ": no se pudo generar Markdown actualizado: " + exception.getMessage());
        }
    }

    private void writeMarkdown(OpenProjectExportItem item, DiagramProject project, Path targetFile) throws IOException {
        if (item.diagramTypeId().equals(DiagramTypeId.DATA_DICTIONARY)) {
            DataDictionaryDocument document = project.dataDictionary()
                    .orElseThrow(() -> new IllegalArgumentException("El diccionario no contiene documento exportable."));
            dataDictionaryMarkdownExporter.export(document, targetFile);
            return;
        }
        Path parent = targetFile.toAbsolutePath().normalize().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(targetFile, markdownDiagramExporter.export(project), StandardCharsets.UTF_8);
    }

    private void exportEditableDms(OpenProjectExportItem item, Path targetFile, List<Path> itemFiles) throws IOException {
        projectRepository.save(item.project(), targetFile);
        itemFiles.add(targetFile);
    }

    private void exportSvg(OpenProjectExportItem item, Path targetFile, List<Path> itemFiles, List<String> notes) throws IOException {
        try {
            Files.writeString(targetFile, svgDiagramExporter.export(item.outputProject()), StandardCharsets.UTF_8);
            itemFiles.add(targetFile);
        } catch (IllegalArgumentException exception) {
            notes.add(item.displayName() + ": SVG omitido: " + exception.getMessage());
        }
    }

    private void exportPdf(OpenProjectExportItem item, Path targetFile, List<Path> itemFiles, List<String> notes) throws IOException {
        try {
            pdfDiagramExporter.export(item.outputProject(), targetFile);
            itemFiles.add(targetFile);
        } catch (IllegalArgumentException exception) {
            notes.add(item.displayName() + ": PDF omitido: " + exception.getMessage());
        }
    }

    private void createDirectory(Path directory, List<Path> createdFolders) throws IOException {
        Files.createDirectories(directory);
        createdFolders.add(directory);
    }

    private String rootFolderName(ClientBatchExportRequest request) {
        return folderNamePolicy.toFolderName(request.clientName()) + "_dms_" + LocalDateTime.now().format(EXPORT_TIMESTAMP);
    }

    private String buildReadme(
            ClientBatchExportRequest request,
            Path rootFolder,
            List<ClientBatchExportItemResult> itemResults
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append("# Exportación Domain Model Studio\n\n");
        builder.append("Carpeta base: ").append(request.clientName()).append("\n\n");
        builder.append("Carpeta raíz: `").append(rootFolder.getFileName()).append("`\n\n");
        builder.append("## Estructura\n\n");
        builder.append("Cada proyecto abierto se exporta en una subcarpeta con:\n\n");
        builder.append("- `input/`: Markdown fuente original o generado desde el proyecto.\n");
        builder.append("- `editable/`: archivo `.dms` editable de Domain Model Studio.\n");
        builder.append("- `output/`: entrega actualizada en PNG, SVG, PDF o Markdown según el tipo.\n\n");
        builder.append("## Proyectos incluidos\n\n");
        for (ClientBatchExportItemResult item : itemResults) {
            builder.append("- `").append(item.projectFolder().getFileName()).append("`: ")
                    .append(item.displayName()).append("\n");
        }
        return builder.toString();
    }

    private String buildManifest(
            ClientBatchExportRequest request,
            Path rootFolder,
            List<ClientBatchExportItemResult> itemResults,
            List<String> notes
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append("# Manifiesto de exportación\n\n");
        builder.append("Carpeta base: ").append(request.clientName()).append("\n");
        builder.append("Destino: `").append(rootFolder.toAbsolutePath()).append("`\n");
        builder.append("Cantidad de proyectos: ").append(itemResults.size()).append("\n\n");
        builder.append("## Archivos por proyecto\n\n");
        for (ClientBatchExportItemResult item : itemResults) {
            builder.append("### ").append(item.displayName()).append("\n\n");
            for (Path file : item.exportedFiles()) {
                builder.append("- `").append(rootFolder.relativize(file)).append("`\n");
            }
            item.pendingPngTarget().ifPresent(path -> builder.append("- `")
                    .append(rootFolder.relativize(path))
                    .append("` — PNG generado desde la vista visual activa al finalizar la exportación.\n"));
            builder.append("\n");
        }
        if (!notes.isEmpty()) {
            builder.append("## Notas\n\n");
            for (String note : notes) {
                builder.append("- ").append(note).append("\n");
            }
        }
        return builder.toString();
    }
}
