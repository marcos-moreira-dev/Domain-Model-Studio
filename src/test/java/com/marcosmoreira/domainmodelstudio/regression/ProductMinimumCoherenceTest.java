package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.OfficialAiResourceDescriptors;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Pruebas de cierre para evitar que el producto vuelva a declarar salidas falsas. */
class ProductMinimumCoherenceTest {

    private static final Path TEMPLATES_DIR = Path.of("examples/markdown/plantillas");
    private static final Path EXAMPLES_DIR = Path.of("examples/markdown/diagramas");

    @Test
    void availableTypesShouldDeclareOnlyCoherentOutputCapabilities() {
        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            if (type.supportStatus() == DiagramSupportStatus.IN_PREPARATION) {
                assertTrue(type.supports(DiagramCapability.PLANNING_VIEW), type.id().value() + " debe abrir guía mientras está en preparación.");
                assertFalse(type.supports(DiagramCapability.IMPORT_MARKDOWN), type.id().value() + " no debe importar Markdown visible sin salida real.");
                assertFalse(type.supports(DiagramCapability.EXPORT_MARKDOWN), type.id().value() + " no debe exportar Markdown visible sin salida real.");
                assertFalse(type.supports(DiagramCapability.SHOW_VISUAL_OUTPUT), type.id().value() + " no debe prometer salida visual sin canvas real.");
                continue;
            }
            assertTrue(type.isAvailable(), type.id().value() + " debe estar disponible solo tras tener salida real.");
            assertFalse(type.supports(DiagramCapability.PLANNING_VIEW), type.id().value() + " no debe abrir guía como salida principal.");
            assertTrue(type.supports(DiagramCapability.MANUAL_EDITING), type.id().value() + " debe permitir corrección manual mínima.");
            assertTrue(type.supports(DiagramCapability.SAVE_DMS), type.id().value() + " debe guardar .dms.");
            assertTrue(type.supports(DiagramCapability.LOAD_DMS), type.id().value() + " debe abrir .dms.");
            assertTrue(type.supports(DiagramCapability.EXPORT_MARKDOWN), type.id().value() + " debe exportar Markdown actualizado.");

            boolean visual = type.supports(DiagramCapability.SHOW_VISUAL_OUTPUT);
            boolean document = type.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT);
            assertTrue(visual || document, type.id().value() + " debe declarar salida visual o documental.");
            assertFalse(visual && document, type.id().value() + " no debe mezclar salida visual y documental principal.");

            if (visual) {
                assertTrue(type.supports(DiagramCapability.EXPORT_PNG), type.id().value() + " debe exportar PNG.");
                assertFalse(type.supports(DiagramCapability.EXPORT_PDF), type.id().value() + " no debe prometer PDF si es diagrama visual.");
            }
            if (document && !DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(type.id())
                    && !DiagramTypeId.LOGICAL_BUSINESS_INTAKE.equals(type.id())) {
                assertTrue(type.supports(DiagramCapability.EXPORT_PDF), type.id().value() + " debe exportar PDF documental.");
                assertFalse(type.supports(DiagramCapability.EXPORT_PNG), type.id().value() + " no debe prometer PNG como salida principal documental.");
            }
            if (DiagramTypeId.LOGICAL_BUSINESS_INTAKE.equals(type.id())) {
                assertFalse(type.supports(DiagramCapability.EXPORT_PDF), "levantamiento lógico no promete PDF en el MVP documental.");
                assertTrue(type.supports(DiagramCapability.EXPORT_MARKDOWN), "levantamiento lógico debe exportar Markdown desde Tanda 7.");
            }
            if (DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(type.id())) {
                assertTrue(type.supports(DiagramCapability.EXPORT_PNG), "roles/permisos debe exportar PNG de matriz.");
                assertTrue(type.supports(DiagramCapability.EXPORT_MARKDOWN), "roles/permisos debe exportar Markdown documental.");
            }
            if (DiagramTypeId.UML_CLASS.equals(type.id())) {
                assertTrue(type.supports(DiagramCapability.IMPORT_SOURCE_CODE), "UML Clases debe declarar importación de código fuente.");
                assertTrue(type.supports(DiagramCapability.OPEN_SOURCE_CODE), "UML Clases debe declarar apertura de código fuente.");
            } else {
                assertFalse(type.supports(DiagramCapability.IMPORT_SOURCE_CODE), type.id().value() + " no debe prometer importación de código fuente.");
                assertFalse(type.supports(DiagramCapability.OPEN_SOURCE_CODE), type.id().value() + " no debe prometer apertura de código fuente.");
            }
        }
    }

    @Test
    void eachVisibleTypeShouldHaveOfficialTemplateAndExample() throws IOException {
        Map<String, Path> templatesByType = markdownByDiagramType(TEMPLATES_DIR);
        Map<String, Path> examplesByType = markdownByDiagramType(EXAMPLES_DIR);

        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            if (type.supportStatus() == DiagramSupportStatus.IN_PREPARATION
                    || DiagramTypeId.LOGICAL_BUSINESS_INTAKE.equals(type.id())) {
                continue;
            }
            String typeId = type.id().value();
            assertTrue(templatesByType.containsKey(typeId), "Falta plantilla oficial para " + typeId);
            assertTrue(examplesByType.containsKey(typeId), "Falta ejemplo oficial para " + typeId);

            Map<String, String> exampleFrontMatter = frontMatter(examplesByType.get(typeId));
            String importable = exampleFrontMatter.getOrDefault("importable", "false");
            assertEquals(
                    Boolean.toString(type.supports(DiagramCapability.IMPORT_MARKDOWN)),
                    importable,
                    "El ejemplo oficial debe reflejar si el tipo es importable: " + typeId);
        }
    }

    @Test
    void aiResourcesMarkedAsImportableShouldBelongToImportableTypes() {
        Map<DiagramTypeId, DiagramTypeDescriptor> typesById = new DefaultDiagramTypeRegistry().findAll().stream()
                .collect(Collectors.toMap(DiagramTypeDescriptor::id, type -> type));

        for (AiResourceDescriptor resource : OfficialAiResourceDescriptors.all()) {
            assertTrue(typesById.containsKey(resource.diagramTypeId()), "Recurso IA con tipo no registrado: " + resource.id());
            if (resource.importableByApplication()) {
                DiagramTypeDescriptor type = typesById.get(resource.diagramTypeId());
                assertTrue(
                        type.supports(DiagramCapability.IMPORT_MARKDOWN),
                        resource.id() + " no puede ser importable si el tipo no importa Markdown.");
            }
        }
    }

    @Test
    void publicDocumentationShouldNotKeepOldPreparationMessage() throws IOException {
        List<Path> files = List.of(
                Path.of("docs/raiz/README.md"),
                Path.of("docs/estado/ESTADO_ACTUAL.md"),
                Path.of("docs/user-guide/02_importar_markdown.md"),
                Path.of("docs/testeo/checklists/smoke_ui_mvp.md"));
        List<String> outdatedMessages = List.of(
                "En preparación como diagramas exportables",
                "Por ahora, la importación real está activa para el modelo conceptual",
                "Tipos no implementados",
                "placeholders no generen outputs falsos");

        for (Path file : files) {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            for (String message : outdatedMessages) {
                assertFalse(content.contains(message), file + " conserva mensaje obsoleto: " + message);
            }
        }
    }

    private static Map<String, Path> markdownByDiagramType(Path directory) throws IOException {
        Map<String, Path> result = new HashMap<>();
        try (Stream<Path> files = Files.list(directory)) {
            for (Path file : files.filter(path -> path.getFileName().toString().endsWith(".md")).toList()) {
                if ("README.md".equals(file.getFileName().toString())) {
                    continue;
                }
                Map<String, String> frontMatter = frontMatter(file);
                Optional.ofNullable(frontMatter.get("diagram_type"))
                        .ifPresent(diagramType -> result.put(diagramType, file));
            }
        }
        return result;
    }

    private static Map<String, String> frontMatter(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        Map<String, String> result = new HashMap<>();
        if (lines.isEmpty() || !"---".equals(lines.getFirst().strip())) {
            return result;
        }
        for (String rawLine : lines.subList(1, lines.size())) {
            String line = rawLine.strip();
            if ("---".equals(line)) {
                break;
            }
            int colon = line.indexOf(':');
            if (colon > 0) {
                String key = line.substring(0, colon).strip();
                String value = line.substring(colon + 1).strip();
                result.put(key, unquote(value));
            }
        }
        return result;
    }

    private static String unquote(String value) {
        String cleaned = value.strip();
        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            return cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned;
    }
}
