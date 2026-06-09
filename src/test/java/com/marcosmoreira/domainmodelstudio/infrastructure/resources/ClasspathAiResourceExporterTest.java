package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ClasspathAiResourceExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldExportRegisteredClasspathResourcesAndIndex() throws Exception {
        ClasspathAiResourceCatalog catalog = new ClasspathAiResourceCatalog(List.of(
                new AiResourceDescriptor(
                        "modelo-conceptual-gramatica",
                        "01_gramatica_markdown_modelo_conceptual.md",
                        DiagramTypeId.CONCEPTUAL_MODEL,
                        "ai-resources/01_gramatica_markdown_modelo_conceptual.md",
                        true,
                        true,
                        "Gramática de prueba.")));
        ClasspathAiResourceExporter exporter = new ClasspathAiResourceExporter(catalog);

        var result = exporter.exportTo(tempDir.resolve("domain-model-studio-recursos-ia"));

        assertEquals(2, result.exportedFiles().size());
        assertTrue(Files.exists(result.destinationFolder().resolve("00_indice_recursos_ia.md")));
        assertTrue(Files.exists(result.destinationFolder().resolve("01_gramatica_markdown_modelo_conceptual.md")));
        String index = Files.readString(result.destinationFolder().resolve("00_indice_recursos_ia.md"));
        assertTrue(index.contains("modelo-conceptual"));
        assertTrue(index.contains("Tipo de recurso: Gramática"));
        assertTrue(index.contains("Uso recomendado:"));
        assertTrue(index.contains("Contrato de importación:"));
        assertTrue(index.contains("Importable por la aplicación: sí"));
    }

    @Test
    void shouldCreateSubfoldersWhenResourceFileNameContainsPath() throws Exception {
        ClasspathAiResourceCatalog catalog = new ClasspathAiResourceCatalog(List.of(
                new AiResourceDescriptor(
                        "plantilla-oficial-conceptual-model",
                        "official-markdown/plantillas/conceptual_model.md",
                        DiagramTypeId.CONCEPTUAL_MODEL,
                        "ai-resources/official-markdown/plantillas/conceptual_model.md",
                        true,
                        false,
                        "Plantilla oficial de prueba.")));
        ClasspathAiResourceExporter exporter = new ClasspathAiResourceExporter(catalog);

        var result = exporter.exportTo(tempDir.resolve("domain-model-studio-recursos-ia"));

        assertEquals(2, result.exportedFiles().size());
        assertTrue(Files.exists(result.destinationFolder().resolve("00_indice_recursos_ia.md")));
        assertTrue(Files.exists(result.destinationFolder().resolve("official-markdown/plantillas/conceptual_model.md")));
    }
}
