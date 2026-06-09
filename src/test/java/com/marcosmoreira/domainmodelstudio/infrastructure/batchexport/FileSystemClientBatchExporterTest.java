package com.marcosmoreira.domainmodelstudio.infrastructure.batchexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.batchexport.ClientBatchExportRequest;
import com.marcosmoreira.domainmodelstudio.application.batchexport.OpenProjectExportItem;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectType;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.infrastructure.json.DmsProjectFileRepository;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.ConceptualModelMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DataDictionaryMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DelegatingMarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.ModuleMapMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness.LogicalBusinessMarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.pdf.DataDictionaryPdfExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.pdf.DelegatingPdfDiagramExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.pdf.LogicalBusinessPdfExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.svg.MultiNotationSvgDiagramExporter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSystemClientBatchExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void exportsFolderStructureForConceptualProject() throws Exception {
        DiagramProject project = DiagramProject.blank("demo", "Modelo conceptual demo", ProjectType.CONCEPTUAL_MODEL);
        FileSystemClientBatchExporter exporter = exporter();

        var result = exporter.export(new ClientBatchExportRequest(
                "Cliente Demo",
                tempDir,
                List.of(OpenProjectExportItem.conceptual("tab-1", "Modelo conceptual demo", project, Optional.empty()))));

        Path projectFolder = result.itemResults().getFirst().projectFolder();
        assertTrue(Files.exists(result.rootFolder().resolve("README_EXPORTACION.md")));
        assertTrue(Files.exists(result.manifestFile()));
        assertTrue(Files.exists(projectFolder.resolve("input/modelo_conceptual_demo.md")));
        assertTrue(Files.exists(projectFolder.resolve("editable/modelo_conceptual_demo.dms")));
        assertTrue(Files.exists(projectFolder.resolve("output/modelo_conceptual_demo_actualizado.md")));
        assertTrue(Files.exists(projectFolder.resolve("output/modelo_conceptual_demo.svg")));
        assertTrue(result.itemResults().getFirst().pendingPngTarget().isPresent());
    }

    @Test
    void exportsDocumentOutputsForDataDictionary() throws Exception {
        DiagramProject project = DiagramProject.blank("diccionario", "Diccionario de datos", DiagramTypeId.DATA_DICTIONARY)
                .withDataDictionary(DataDictionaryDocument.blank("Diccionario de datos", LocalDate.now()));
        FileSystemClientBatchExporter exporter = exporter();

        var result = exporter.export(new ClientBatchExportRequest(
                "Cliente Demo",
                tempDir,
                List.of(OpenProjectExportItem.document("tab-2", "Diccionario de datos", project, Optional.empty()))));

        Path projectFolder = result.itemResults().getFirst().projectFolder();
        assertTrue(Files.exists(projectFolder.resolve("input/diccionario_de_datos.md")));
        assertTrue(Files.exists(projectFolder.resolve("editable/diccionario_de_datos.dms")));
        assertTrue(Files.exists(projectFolder.resolve("output/diccionario_de_datos_actualizado.md")));
        assertTrue(Files.exists(projectFolder.resolve("output/diccionario_de_datos.pdf")));
        assertEquals(Optional.empty(), result.itemResults().getFirst().pendingPngTarget());
    }

    @Test
    void exportsDocumentOutputsForLogicalBusinessIntake() throws Exception {
        DiagramProject project = DiagramProject.blank(
                        "levantamiento",
                        "Levantamiento logico",
                        DiagramTypeId.LOGICAL_BUSINESS_INTAKE)
                .withLogicalBusinessDocument(LogicalBusinessDocument.blank("Levantamiento logico"));
        FileSystemClientBatchExporter exporter = exporter();

        var result = exporter.export(new ClientBatchExportRequest(
                "Cliente Demo",
                tempDir,
                List.of(OpenProjectExportItem.document(
                        "tab-5",
                        "Levantamiento logico",
                        project,
                        Optional.empty()))));

        Path projectFolder = result.itemResults().getFirst().projectFolder();
        assertTrue(Files.exists(projectFolder.resolve("input/levantamiento_logico.md")));
        assertTrue(Files.exists(projectFolder.resolve("editable/levantamiento_logico.dms")));
        assertTrue(Files.exists(projectFolder.resolve("output/levantamiento_logico_actualizado.md")));
        assertTrue(Files.exists(projectFolder.resolve("output/levantamiento_logico.pdf")));
        assertTrue(Files.size(projectFolder.resolve("output/levantamiento_logico.pdf")) > 800);
        assertEquals(Optional.empty(), result.itemResults().getFirst().pendingPngTarget());
    }

    @Test
    void exportsVisualDiagramMarkdownSvgAndPendingPng() throws Exception {
        DiagramProject project = DiagramProject.blank("modulos", "Mapa de módulos", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(ModuleMapDocument.blank("Mapa de módulos"));
        FileSystemClientBatchExporter exporter = exporter();

        var result = exporter.export(new ClientBatchExportRequest(
                "Cliente Demo",
                tempDir,
                List.of(OpenProjectExportItem.visualDiagram("tab-3", "Mapa de módulos", project, Optional.empty()))));

        Path projectFolder = result.itemResults().getFirst().projectFolder();
        assertTrue(Files.exists(projectFolder.resolve("input/mapa_de_modulos.md")));
        assertTrue(Files.exists(projectFolder.resolve("editable/mapa_de_modulos.dms")));
        assertTrue(Files.exists(projectFolder.resolve("output/mapa_de_modulos_actualizado.md")));
        assertTrue(Files.exists(projectFolder.resolve("output/mapa_de_modulos.svg")));
        assertTrue(result.itemResults().getFirst().pendingPngTarget().isPresent());
    }

    @Test
    void usesScopedOutputProjectForDeliverablesButKeepsCompleteDmsEditable() throws Exception {
        ModuleMapDocument completeDocument = ModuleMapDocument.blank("Mapa completo")
                .withModule(ModuleNode.root("administracion", "Administración"))
                .withModule(ModuleNode.root("reportes", "Reportes"));
        ModuleMapDocument scopedDocument = ModuleMapDocument.blank("Vista filtrada")
                .withModule(ModuleNode.root("reportes", "Reportes"));
        DiagramProject completeProject = DiagramProject.blank("modulos", "Mapa completo", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(completeDocument);
        DiagramProject scopedProject = DiagramProject.blank("modulos", "Mapa filtrado", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(scopedDocument);
        OpenProjectExportItem item = new OpenProjectExportItem(
                "tab-4",
                "Mapa de módulos",
                DiagramTypeId.ADMIN_MODULE_MAP,
                completeProject,
                Optional.empty(),
                true,
                true,
                false,
                false,
                scopedProject,
                "vista_filtrada");

        var result = exporter().export(new ClientBatchExportRequest("Cliente Demo", tempDir, List.of(item)));

        Path projectFolder = result.itemResults().getFirst().projectFolder();
        String markdown = Files.readString(projectFolder.resolve("output/vista_filtrada_actualizado.md"));
        String editable = Files.readString(projectFolder.resolve("editable/vista_filtrada.dms"));
        assertTrue(markdown.contains("Vista filtrada"));
        assertTrue(markdown.contains("Reportes"));
        assertTrue(!markdown.contains("Administración"));
        assertTrue(editable.contains("Administración"));
        assertTrue(result.notes().stream().anyMatch(note -> note.contains("vista visual activa")));
    }

    private static FileSystemClientBatchExporter exporter() {
        return new FileSystemClientBatchExporter(
                new DmsProjectFileRepository(),
                new MultiNotationSvgDiagramExporter(),
                new DelegatingMarkdownDiagramExporter(Map.of(
                        DiagramTypeId.CONCEPTUAL_MODEL, new ConceptualModelMarkdownExporter(),
                        DiagramTypeId.ADMIN_MODULE_MAP, new ModuleMapMarkdownExporter(),
                        DiagramTypeId.LOGICAL_BUSINESS_INTAKE, new LogicalBusinessMarkdownDiagramExporter())),
                new DataDictionaryMarkdownExporter(),
                new DelegatingPdfDiagramExporter(Map.of(
                        DiagramTypeId.DATA_DICTIONARY, new DataDictionaryPdfExporter(),
                        DiagramTypeId.LOGICAL_BUSINESS_INTAKE, new LogicalBusinessPdfExporter())));
    }
}
