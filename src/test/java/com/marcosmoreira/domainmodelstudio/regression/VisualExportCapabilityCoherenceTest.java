package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.examples.DefaultOfficialExampleCatalog;
import com.marcosmoreira.domainmodelstudio.application.examples.OfficialExampleDescriptor;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.infrastructure.json.DmsProjectJsonReader;
import com.marcosmoreira.domainmodelstudio.infrastructure.json.DmsProjectJsonWriter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DiagramMarkdownImportDispatcher;
import com.marcosmoreira.domainmodelstudio.infrastructure.svg.MultiNotationSvgDiagramExporter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** Smoke anti-fachada para ejemplos visuales oficiales. */
class VisualExportCapabilityCoherenceTest {

    private final DefaultDiagramTypeRegistry typeRegistry = new DefaultDiagramTypeRegistry();
    private final DefaultOfficialExampleCatalog exampleCatalog = new DefaultOfficialExampleCatalog(typeRegistry);
    private final DiagramMarkdownImportDispatcher importer = new DiagramMarkdownImportDispatcher(typeRegistry);
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();
    private final GenerateInitialChenLayoutUseCase chenLayoutUseCase = new GenerateInitialChenLayoutUseCase();
    private final MultiNotationSvgDiagramExporter svgExporter = new MultiNotationSvgDiagramExporter();
    private final DmsProjectJsonWriter jsonWriter = new DmsProjectJsonWriter();
    private final DmsProjectJsonReader jsonReader = new DmsProjectJsonReader();

    @Test
    void visualTypesAdvertiseSvgOnlyWhenTheyHaveVisualOutput() {
        for (DiagramTypeDescriptor type : typeRegistry.findAll()) {
            if (type.supports(DiagramCapability.SHOW_VISUAL_OUTPUT)) {
                assertTrue(type.supports(DiagramCapability.EXPORT_PNG), type.id().value() + " debe exportar PNG.");
                assertTrue(type.supports(DiagramCapability.EXPORT_SVG), type.id().value() + " debe exportar SVG vectorial.");
            }
            if (type.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT)
                    && !DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(type.id())) {
                assertFalse(type.supports(DiagramCapability.EXPORT_SVG), type.id().value() + " no debe fingir SVG si es documento.");
            }
        }
    }

    @Test
    void everyImportableVisualOfficialExampleCanMovePersistAndExportSvg() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (OfficialExampleDescriptor example : exampleCatalog.findImportable()) {
            try (InputStream stream = classLoader.getResourceAsStream(example.classpathLocation())) {
                assertNotNull(stream, "No existe recurso oficial: " + example.classpathLocation());
                String markdown = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                DiagramProject imported = importer.parse(markdown, example.sourceName());
                assertEquals(example.diagramTypeId(), imported.metadata().diagramTypeId(), example.id());
                DiagramTypeDescriptor type = typeRegistry.findById(imported.metadata().diagramTypeId()).orElseThrow();
                if (!type.supports(DiagramCapability.SHOW_VISUAL_OUTPUT)) {
                    continue;
                }

                DiagramProject prepared = prepareVisualLayout(imported);
                DiagramLayout layout = prepared.layouts().activeLayout();
                assertTrue(layout.nodeCount() > 0, example.id() + " debe tener nodos visuales.");

                NodeLayout firstNode = layout.nodes().getFirst();
                double movedX = firstNode.x() + 17.0;
                double movedY = firstNode.y() + 23.0;
                DiagramProject moved = prepared.withLayouts(prepared.layouts()
                        .withLayout(layout.moveNode(firstNode.elementId(), movedX, movedY)));

                DiagramProject reopened = jsonReader.read(jsonWriter.write(moved));
                NodeLayout reopenedNode = reopened.layouts().activeLayout().nodeFor(firstNode.elementId())
                        .orElseThrow(() -> new AssertionError("No se reabrió layout para " + firstNode.elementId()));
                assertEquals(movedX, reopenedNode.x(), 0.01, example.id() + " debe persistir X.");
                assertEquals(movedY, reopenedNode.y(), 0.01, example.id() + " debe persistir Y.");

                String svg = svgExporter.export(reopened);
                assertTrue(svg.contains("<svg"), example.id() + " debe exportar SVG.");
                assertTrue(svg.length() > 500, example.id() + " no debe generar SVG vacío.");
                assertFalse(svg.contains("data:image"), example.id() + " no debe incrustar raster base64.");
                assertFalse(svg.contains("<image"), example.id() + " no debe usar imagen raster como fachada SVG.");
            }
        }
    }

    private DiagramProject prepareVisualLayout(DiagramProject imported) {
        if (DiagramTypeId.CONCEPTUAL_MODEL.equals(imported.metadata().diagramTypeId())) {
            return chenLayoutUseCase.generate(imported);
        }
        return visualLayoutService.ensureVisualLayout(imported);
    }
}
