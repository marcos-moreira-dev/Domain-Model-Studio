package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Guardarraíles de regresión creados después del smoke manual de Grafo libre y UML Clases.
 *
 * <p>No reemplazan pruebas visuales manuales. Sirven para evitar que los arreglos críticos de
 * etiquetas, selección, arrastre, jerarquías y apertura de código desaparezcan durante el refactor.</p>
 */
class Tanda14RuntimeRegressionContractsSourceTest {

    private static final Path MAIN = Path.of("src", "main", "java");

    @Test
    void freeGraphConnectorLabelsMustRemainVisibleEvenWithoutExplicitMarkdownLabel() throws IOException {
        String adapter = read("presentation/freegraph/FreeGraphCanvasAdapter.java");
        String labelRenderer = read("presentation/interactivecanvas/CanvasConnectorLabelOverlayRenderer.java");
        String factory = read("presentation/interactivecanvas/CanvasConnectorLabelNodeFactory.java");

        assertTrue(adapter.contains("labelFor(edge)"),
                "Grafo libre debe entregar etiquetas visibles al canvas, no conectores mudos.");
        assertTrue(adapter.contains("String separator = edge.direction() == FreeGraphEdgeDirection.UNDIRECTED ? \" — \" : \" → \";"),
                "Si no hay etiqueta explícita, debe existir fallback Origen → Destino / Origen — Destino.");
        assertTrue(labelRenderer.contains("visibleConnectorLabel"),
                "El renderer común debe conservar una última capa de fallback para etiquetas visibles.");
        assertTrue(labelRenderer.contains("connector.kind().startsWith(\"free-graph-edge\")"),
                "El fallback transversal debe reconocer relaciones de Grafo libre.");
        assertTrue(factory.contains("setPrefSize(width, height)") && factory.contains("-fx-background-color"),
                "Las etiquetas deben tener tamaño y contraste explícitos para no volverse invisibles.");
    }

    @Test
    void umlClassSelectionDragAndTextClippingMustRemainProtected() throws IOException {
        String nodeInteraction = read("presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java");
        String nodeRegistry = read("presentation/interactivecanvas/CanvasNodeVisualRegistry.java");
        String adapter = read("presentation/umlclass/UmlClassCanvasAdapter.java");
        String renderKit = read("presentation/umlclass/UmlClassRenderKit.java");

        assertTrue(nodeInteraction.contains("adapter.selectNode(node.id(), event.isShiftDown())"),
                "El clic directo debe sincronizar selección visual y selección semántica.");
        assertTrue(nodeInteraction.contains("visualRegistry.markSelectedNodeLocally(node.id(), event.isShiftDown() || preserveSelectionGroup)")
                        && nodeRegistry.contains("markSelectedNodeLocally"),
                "El feedback inmediato de selección debe permanecer y conservar grupos seleccionados durante el drag.");
        assertTrue(nodeInteraction.contains("moveDraggedNodeOrSelection"),
                "Debe existir fallback de arrastre cuando la selección semántica no está lista.");
        assertTrue(adapter.contains("moveModuleAndVisibleClassesBy") && adapter.contains("classMoveCoveredBySelectedModule"),
                "Mover módulos no debe duplicar movimiento de clases internas.");
        assertTrue(adapter.contains("filter(id -> normalize(id).startsWith(CLASS_PREFIX))"),
                "La selección por rectángulo debe priorizar clases sobre módulos para abrir código/propiedades.");
        assertTrue(renderKit.contains("CanvasNodeViewFactory"),
                "UML Clases debe seguir usando la fábrica transversal de nodos con hitbox común.");
        assertTrue(renderKit.contains("setClip(new Rectangle(bounds.width(), bounds.height()))"),
                "El texto interno de clases debe quedar recortado dentro de la tarjeta.");
        assertTrue(renderKit.contains("OverrunStyle.ELLIPSIS"),
                "Las líneas largas de clases deben usar elipsis.");
    }

    @Test
    void umlClassRelationHierarchyMustRemainVisibleOnScreenAndSvg() throws IOException {
        String renderKit = read("presentation/umlclass/UmlClassRenderKit.java");
        String svgWriter = read("infrastructure/svg/specialized/SpecializedVisualSvgWriter.java");
        String css = Files.readString(Path.of("src", "main", "resources", "css", "uml-class.css"), StandardCharsets.UTF_8);

        assertTrue(renderKit.contains("HOLLOW_TRIANGLE"), "Herencia/implementación deben usar triángulo hueco.");
        assertTrue(renderKit.contains("FILLED_DIAMOND"), "Composición debe usar diamante relleno.");
        assertTrue(renderKit.contains("HOLLOW_DIAMOND"), "Agregación debe usar diamante hueco.");
        assertTrue(renderKit.contains("DiagramArrowKind.OPEN"), "Dependencia debe usar flecha abierta.");
        assertTrue(svgWriter.contains("uml-inheritance")
                        && svgWriter.contains("uml-composition")
                        && svgWriter.contains("uml-aggregation"),
                "El SVG especializado debe conservar marcadores distintos de jerarquía UML.");
        assertTrue(css.contains("uml-class-canvas-arrow-head")
                        && css.contains("uml-class-canvas-connector-uml-relation-inheritance")
                        && css.contains("uml-class-canvas-connector-uml-relation-composition"),
                "La hoja visual de UML debe declarar estilos diferenciables para conectores y puntas.");
    }

    @Test
    void umlSourceCodeActionsMustRemainContextualAndHonest() throws IOException {
        String capabilities = read("domain/catalog/DiagramCapability.java");
        String definitions = read("application/catalog/definitions/DiagramCapabilityProfiles.java");
        String toolbar = read("presentation/toolbar/UmlClassToolbarContributor.java");
        String viewModel = read("presentation/toolbar/MainToolbarViewModel.java");
        String umlViewModel = read("presentation/umlclass/UmlClassDiagramViewModel.java");

        assertTrue(capabilities.contains("IMPORT_SOURCE_CODE") && capabilities.contains("OPEN_SOURCE_CODE"),
                "Las capacidades de código fuente deben existir como contrato explícito de producto.");
        assertTrue(definitions.contains("DiagramCapability.IMPORT_SOURCE_CODE")
                        && definitions.contains("DiagramCapability.OPEN_SOURCE_CODE"),
                "UML Clases debe declarar importación/apertura de código en el catálogo real.");
        assertTrue(toolbar.contains("IMPORT_UML_FROM_SOURCE") && toolbar.contains("OPEN_UML_SOURCE"),
                "La toolbar de UML debe conservar acciones reales de código fuente.");
        assertTrue(viewModel.contains("!DiagramTypeId.UML_CLASS.equals(activeDiagramTypeProperty().get())")
                        && !viewModel.contains("umlClassDiagramViewModel.selectedSourcePath().isEmpty()"),
                "Abrir código debe quedar clicable en UML Clases y reportar por estado si falta clase o ruta fuente.");
        assertTrue(umlViewModel.contains("sourceFileResolver.inspect(node)")
                        && umlViewModel.contains("openSelectedSourceWithProgramChooser"),
                "La apertura debe pasar por resolución de ruta y selector del sistema cuando aplique.");
    }

    private static String read(String packageRelativePath) throws IOException {
        Path path = MAIN
                .resolve("com")
                .resolve("marcosmoreira")
                .resolve("domainmodelstudio")
                .resolve(packageRelativePath.replace('/', java.io.File.separatorChar));
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
