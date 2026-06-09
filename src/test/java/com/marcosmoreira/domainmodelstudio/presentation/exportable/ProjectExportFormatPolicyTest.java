package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.datadictionary.DefaultCreateDataDictionaryUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.CreateModuleMapUseCase;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProjectExportFormatPolicyTest {

    private final ProjectExportFormatPolicy policy = new ProjectExportFormatPolicy();

    @Test
    void conceptualModelExportsOnlyDiagramFormats() {
        DiagramProject project = DiagramProject.blank("conceptual", "Conceptual", DiagramTypeId.CONCEPTUAL_MODEL);

        assertEquals(
                Set.of(ExportFormat.SVG, ExportFormat.PNG, ExportFormat.MARKDOWN),
                policy.formatsForProject(project, true));
    }

    @Test
    void dataDictionaryNeedsDocumentBeforeOfferingPdfAndMarkdown() {
        DiagramProject emptyProject = DiagramProject.blank("dictionary", "Diccionario", DiagramTypeId.DATA_DICTIONARY);

        assertTrue(policy.formatsForProject(emptyProject, true).isEmpty());

        DiagramProject project = emptyProject.withDataDictionary(
                new DefaultCreateDataDictionaryUseCase().createBlank("Diccionario"));

        assertEquals(
                Set.of(ExportFormat.PDF, ExportFormat.MARKDOWN),
                policy.formatsForProject(project, true));
    }

    @Test
    void logicalBusinessNeedsDocumentBeforeOfferingPdfAndMarkdown() {
        DiagramProject emptyProject = DiagramProject.blank("logical", "Levantamiento", DiagramTypeId.LOGICAL_BUSINESS_INTAKE);

        assertTrue(policy.formatsForProject(emptyProject, true).isEmpty());

        DiagramProject project = emptyProject.withLogicalBusinessDocument(
                LogicalBusinessDocument.blank("Levantamiento"));

        assertEquals(
                Set.of(ExportFormat.PDF, ExportFormat.MARKDOWN),
                policy.formatsForProject(project, true));
    }

    @Test
    void visualSpecializedTypesNeedTheirOwnDocumentBeforeOfferingFormats() {
        DiagramProject emptyProject = DiagramProject.blank("modules", "Mapa", DiagramTypeId.ADMIN_MODULE_MAP);

        assertTrue(policy.formatsForProject(emptyProject, true).isEmpty());

        DiagramProject project = emptyProject.withModuleMap(
                new CreateModuleMapUseCase().createBlank("Mapa"));

        assertEquals(
                Set.of(ExportFormat.SVG, ExportFormat.PNG, ExportFormat.MARKDOWN),
                policy.formatsForProject(project, true));
    }

    @Test
    void visualFormatsCanOmitPngWhenNoPngActionIsAvailable() {
        DiagramProject project = DiagramProject.blank("modules", "Mapa", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(new CreateModuleMapUseCase().createBlank("Mapa"));

        assertEquals(
                Set.of(ExportFormat.SVG, ExportFormat.MARKDOWN),
                policy.formatsForProject(project, false));
    }

    @Test
    void freeGraphNeedsDocumentBeforeOfferingVisualFormats() {
        DiagramProject emptyProject = DiagramProject.blank("free", "Grafo", DiagramTypeId.FREE_GRAPH);

        assertTrue(policy.formatsForProject(emptyProject, true).isEmpty());

        DiagramProject project = emptyProject.withFreeGraph(FreeGraphDocument.blank("Grafo"));

        assertEquals(
                Set.of(ExportFormat.SVG, ExportFormat.PNG, ExportFormat.MARKDOWN),
                policy.formatsForProject(project, true));
    }


    @Test
    void noFormatAppearsWithoutOfficialCapability() {
        DiagramProject dictionary = DiagramProject.blank("dictionary", "Diccionario", DiagramTypeId.DATA_DICTIONARY)
                .withDataDictionary(new DefaultCreateDataDictionaryUseCase().createBlank("Diccionario"));

        Set<ExportFormat> formats = policy.formatsForProject(dictionary, true);

        assertEquals(Set.of(ExportFormat.PDF, ExportFormat.MARKDOWN), formats);
        assertTrue(!formats.contains(ExportFormat.SVG));
        assertTrue(!formats.contains(ExportFormat.PNG));
    }
}
