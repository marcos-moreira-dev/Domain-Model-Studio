package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class OfficialMarkdownExamplesCatalogTest {

    private static final Path TEMPLATES_DIR = Path.of("examples", "markdown", "plantillas");
    private static final Path DIAGRAMS_DIR = Path.of("examples", "markdown", "diagramas");

    @Test
    void everyVisibleDiagramTypeShouldHaveOfficialTemplate() throws Exception {
        Map<String, Path> templatesByDiagramType = markdownFiles(TEMPLATES_DIR).stream()
                .collect(Collectors.toMap(
                        path -> frontMatterOrFail(path).get("diagram_type"),
                        path -> path));

        for (DiagramTypeDescriptor descriptor : new DefaultDiagramTypeRegistry().findAll()) {
            assertTrue(
                    templatesByDiagramType.containsKey(descriptor.id().value()),
                    "Falta plantilla oficial para " + descriptor.id().value());
        }
    }

    @Test
    void allOfficialMarkdownFilesShouldUseRegisteredDiagramTypeIds() throws Exception {
        Set<String> registeredIds = new DefaultDiagramTypeRegistry().findAll().stream()
                .map(DiagramTypeDescriptor::id)
                .map(DiagramTypeId::value)
                .collect(Collectors.toSet());

        for (Path markdownFile : allOfficialMarkdownFiles()) {
            Map<String, String> metadata = frontMatter(markdownFile);
            assertTrue(
                    registeredIds.contains(metadata.get("diagram_type")),
                    markdownFile + " usa diagram_type no registrado: " + metadata.get("diagram_type"));
        }
    }

    @Test
    void preparationExamplesShouldNotBeMarkedAsImportable() throws Exception {
        for (Path markdownFile : allOfficialMarkdownFiles()) {
            Map<String, String> metadata = frontMatter(markdownFile);
            boolean importableNow = "conceptual-model".equals(metadata.get("diagram_type"))
                    || "data-dictionary".equals(metadata.get("diagram_type"))
                    || "admin-module-map".equals(metadata.get("diagram_type"))
                    || "roles-permissions-map".equals(metadata.get("diagram_type"))
                    || "screen-flow".equals(metadata.get("diagram_type"))
                    || "admin-wireframes".equals(metadata.get("diagram_type"))
                    || "uml-class".equals(metadata.get("diagram_type"))
                    || "c4-context".equals(metadata.get("diagram_type"))
                    || "c4-containers".equals(metadata.get("diagram_type"))
                    || "technical-deployment".equals(metadata.get("diagram_type"))
                    || "bpmn-basic".equals(metadata.get("diagram_type"))
                    || "operational-flow".equals(metadata.get("diagram_type"))
                    || "uml-use-case".equals(metadata.get("diagram_type"))
                    || "uml-activity".equals(metadata.get("diagram_type"))
                    || "uml-sequence".equals(metadata.get("diagram_type"))
                    || "uml-state".equals(metadata.get("diagram_type"))
                    || "free-graph".equals(metadata.get("diagram_type"))
                    || "logical-business-graph".equals(metadata.get("diagram_type"))
                    || "logical-business-intake".equals(metadata.get("diagram_type"));
            if (!importableNow) {
                assertEquals(
                        "false",
                        metadata.get("importable"),
                        markdownFile + " no debe declarar importación real todavía.");
            }
        }
    }

    @Test
    void priorityExamplesShouldExist() {
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("conceptual_model_colegio_minimo_importable.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("data_dictionary_colegio_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("admin_module_map_restaurante_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("roles_permissions_optica_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("screen_flow_ventas_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("admin_wireframes_ventas_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("uml_class_restaurante_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("c4_context_sistema_administrativo_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("c4_containers_sistema_administrativo_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("bpmn_basic_venta_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("technical_deployment_piloto_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("logical_business_graph_minimo.md")));
        assertTrue(Files.exists(DIAGRAMS_DIR.resolve("logical_business_graph_uens_gordito.md")));
    }

    private static Map<String, String> frontMatterOrFail(Path markdownFile) {
        try {
            return frontMatter(markdownFile);
        } catch (IOException exception) {
            throw new AssertionError("No se pudo leer front matter de " + markdownFile, exception);
        }
    }

    private static Set<Path> allOfficialMarkdownFiles() throws IOException {
        try (Stream<Path> files = Stream.concat(markdownFiles(TEMPLATES_DIR).stream(), markdownFiles(DIAGRAMS_DIR).stream())) {
            return files.collect(Collectors.toSet());
        }
    }

    private static Set<Path> markdownFiles(Path directory) throws IOException {
        try (Stream<Path> files = Files.list(directory)) {
            return files
                    .filter(path -> path.getFileName().toString().endsWith(".md"))
                    .filter(path -> !"README.md".equals(path.getFileName().toString()))
                    .collect(Collectors.toSet());
        }
    }

    private static Map<String, String> frontMatter(Path markdownFile) throws IOException {
        String content = Files.readString(markdownFile, StandardCharsets.UTF_8);
        String[] lines = content.split("\\R");
        assertTrue(lines.length > 2, markdownFile + " debe contener front matter.");
        assertEquals("---", lines[0].strip(), markdownFile + " debe iniciar con front matter.");

        Map<String, String> metadata = new HashMap<>();
        for (int index = 1; index < lines.length; index++) {
            String line = lines[index].strip();
            if ("---".equals(line)) {
                break;
            }
            if (line.contains(":")) {
                String key = line.substring(0, line.indexOf(':')).strip();
                String value = line.substring(line.indexOf(':') + 1).strip();
                metadata.put(key, unquote(value));
            }
        }
        assertFalse(metadata.isEmpty(), markdownFile + " debe declarar metadatos.");
        assertTrue(metadata.containsKey("diagram_type"), markdownFile + " debe declarar diagram_type.");
        assertTrue(metadata.containsKey("importable"), markdownFile + " debe declarar importable.");
        return metadata;
    }

    private static String unquote(String value) {
        String cleaned = value.strip();
        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            return cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned;
    }
}
