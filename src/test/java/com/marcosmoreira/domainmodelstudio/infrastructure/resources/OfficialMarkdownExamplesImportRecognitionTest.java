package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DiagramMarkdownImportDispatcher;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class OfficialMarkdownExamplesImportRecognitionTest {

    private static final Path DIAGRAMS_DIR = Path.of("examples", "markdown", "diagramas");
    private final DiagramMarkdownImportDispatcher dispatcher = new DiagramMarkdownImportDispatcher(
            new DefaultDiagramTypeRegistry()
    );

    @Test
    void importableExamplesShouldImport() throws Exception {
        Path conceptualExample = DIAGRAMS_DIR.resolve("conceptual_model_colegio_minimo_importable.md");
        Path dataDictionaryExample = DIAGRAMS_DIR.resolve("data_dictionary_colegio_minimo.md");
        Path moduleMapExample = DIAGRAMS_DIR.resolve("admin_module_map_restaurante_minimo.md");
        Path rolesPermissionsExample = DIAGRAMS_DIR.resolve("roles_permissions_optica_minimo.md");
        Path screenFlowExample = DIAGRAMS_DIR.resolve("screen_flow_ventas_minimo.md");
        Path wireframeExample = DIAGRAMS_DIR.resolve("admin_wireframes_ventas_minimo.md");
        Path umlClassExample = DIAGRAMS_DIR.resolve("uml_class_restaurante_minimo.md");
        Path c4ContextExample = DIAGRAMS_DIR.resolve("c4_context_sistema_administrativo_minimo.md");
        Path c4ContainersExample = DIAGRAMS_DIR.resolve("c4_containers_sistema_administrativo_minimo.md");
        Path technicalDeploymentExample = DIAGRAMS_DIR.resolve("technical_deployment_piloto_minimo.md");
        Path bpmnExample = DIAGRAMS_DIR.resolve("bpmn_basic_venta_minimo.md");
        Path operationalExample = DIAGRAMS_DIR.resolve("operational_flow_soporte_minimo.md");
        Path useCaseExample = DIAGRAMS_DIR.resolve("uml_use_case_restaurante_minimo.md");
        Path activityExample = DIAGRAMS_DIR.resolve("uml_activity_cierre_caja_minimo.md");
        Path sequenceExample = DIAGRAMS_DIR.resolve("uml_sequence_login_minimo.md");
        Path stateExample = DIAGRAMS_DIR.resolve("uml_state_orden_minimo.md");
        Path freeGraphExample = DIAGRAMS_DIR.resolve("free_graph_minimo.md");
        Path logicalBusinessGraphMinimalExample = DIAGRAMS_DIR.resolve("logical_business_graph_minimo.md");
        Path logicalBusinessGraphExample = DIAGRAMS_DIR.resolve("logical_business_graph_uens_gordito.md");

        assertDoesNotThrow(() -> dispatcher.parse(conceptualExample));
        assertDoesNotThrow(() -> dispatcher.parse(dataDictionaryExample));
        assertDoesNotThrow(() -> dispatcher.parse(moduleMapExample));
        assertDoesNotThrow(() -> dispatcher.parse(rolesPermissionsExample));
        assertDoesNotThrow(() -> dispatcher.parse(screenFlowExample));
        assertDoesNotThrow(() -> dispatcher.parse(wireframeExample));
        assertDoesNotThrow(() -> dispatcher.parse(umlClassExample));
        assertDoesNotThrow(() -> dispatcher.parse(c4ContextExample));
        assertDoesNotThrow(() -> dispatcher.parse(c4ContainersExample));
        assertDoesNotThrow(() -> dispatcher.parse(technicalDeploymentExample));
        assertDoesNotThrow(() -> dispatcher.parse(bpmnExample));
        assertDoesNotThrow(() -> dispatcher.parse(operationalExample));
        assertDoesNotThrow(() -> dispatcher.parse(useCaseExample));
        assertDoesNotThrow(() -> dispatcher.parse(activityExample));
        assertDoesNotThrow(() -> dispatcher.parse(sequenceExample));
        assertDoesNotThrow(() -> dispatcher.parse(stateExample));
        assertDoesNotThrow(() -> dispatcher.parse(freeGraphExample));
        assertDoesNotThrow(() -> dispatcher.parse(logicalBusinessGraphMinimalExample));
        assertDoesNotThrow(() -> dispatcher.parse(logicalBusinessGraphExample));
    }

    @Test
    void preparationExamplesShouldBeRecognizedButNotLoadedAsProjects() throws Exception {
        for (Path markdownFile : diagramExamples()) {
            String fileName = markdownFile.getFileName().toString();
            if (fileName.startsWith("conceptual_model_")
                    || fileName.startsWith("data_dictionary_")
                    || fileName.startsWith("admin_module_map_")
                    || fileName.startsWith("roles_permissions_")
                    || fileName.startsWith("screen_flow_")
                    || fileName.startsWith("admin_wireframes_")
                    || fileName.startsWith("uml_class_")
                    || fileName.startsWith("c4_context_")
                    || fileName.startsWith("c4_containers_")
                    || fileName.startsWith("technical_deployment_")
                    || fileName.startsWith("bpmn_basic_")
                    || fileName.startsWith("operational_flow_")
                    || fileName.startsWith("uml_use_case_")
                    || fileName.startsWith("uml_activity_")
                    || fileName.startsWith("uml_sequence_")
                    || fileName.startsWith("uml_state_")
                    || fileName.startsWith("free_graph_")
                    || fileName.startsWith("logical_business_graph_")) {
                continue;
            }
            MarkdownModelParsingException exception = assertThrows(
                    MarkdownModelParsingException.class,
                    () -> dispatcher.parse(markdownFile),
                    markdownFile + " debe reconocerse sin cargarse como proyecto editable todavía.");
            assertTrue(
                    exception.getMessage().contains("todavía está en preparación"),
                    markdownFile + " debe indicar preparación en lenguaje de usuario.");
        }
    }

    private static Iterable<Path> diagramExamples() throws IOException {
        try (Stream<Path> files = Files.list(DIAGRAMS_DIR)) {
            return files
                    .filter(path -> path.getFileName().toString().endsWith(".md"))
                    .filter(path -> !"README.md".equals(path.getFileName().toString()))
                    .toList();
        }
    }
}
