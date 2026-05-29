package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import java.time.LocalDate;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.MarkdownDiagramParser;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class DmsProjectFileRepositoryTest {

    @Test
    void savesAndOpensDmsProjectPreservingModelAndLayout() throws Exception {
        ImportMarkdownModelUseCase importUseCase = new ImportMarkdownModelUseCase(
                new MarkdownDiagramParser(),
                new DiagramProjectValidator()
        );
        DiagramProject project = new GenerateInitialChenLayoutUseCase()
                .generate(importUseCase.importFile(Path.of("examples/markdown/supermercado_chen.md")).project());
        Path target = Files.createTempFile("domain-model-studio-", ".dms");

        DmsProjectFileRepository repository = new DmsProjectFileRepository();
        repository.save(project, target);
        DiagramProject opened = repository.open(target);

        assertEquals(project.metadata().title(), opened.metadata().title());
        assertEquals(project.model().entityCount(), opened.model().entityCount());
        assertEquals(project.model().relationshipCount(), opened.model().relationshipCount());
        assertTrue(opened.layouts().layoutFor(project.metadata().activeNotation()).isPresent());
    }

    @Test
    void savesAndOpensPlaceholderProjectPreservingDiagramTypeId() throws Exception {
        DiagramProject project = DiagramProject.blank(
                "diccionario_datos_nuevo_1",
                "Diccionario de datos nuevo",
                DiagramTypeId.DATA_DICTIONARY
        ).withDataDictionary(DataDictionaryDocument.blank("Diccionario de datos nuevo", LocalDate.of(2026, 1, 1)));
        Path target = Files.createTempFile("domain-model-studio-placeholder-", ".dms");

        DmsProjectFileRepository repository = new DmsProjectFileRepository();
        repository.save(project, target);
        DiagramProject opened = repository.open(target);

        assertEquals(DiagramTypeId.DATA_DICTIONARY, opened.metadata().diagramTypeId());
        assertEquals("Diccionario de datos nuevo", opened.metadata().title());
    }

    @Test
    void opensLegacyProjectWithoutDiagramTypeIdAsConceptualModel() {
        String legacyJson = """
                {
                  "project": {
                    "id": "legacy",
                    "title": "Legacy",
                    "projectType": "CONCEPTUAL_MODEL",
                    "version": "0.1.0",
                    "status": "draft",
                    "activeNotation": "CHEN",
                    "sourceMarkdownPath": "",
                    "description": ""
                  },
                  "model": {
                    "entities": [],
                    "relationships": []
                  },
                  "layouts": {
                    "activeNotation": "CHEN",
                    "byNotation": {}
                  }
                }
                """;

        DiagramProject opened = new DmsProjectJsonReader().read(legacyJson);

        assertEquals(DiagramTypeId.CONCEPTUAL_MODEL, opened.metadata().diagramTypeId());
    }
}
