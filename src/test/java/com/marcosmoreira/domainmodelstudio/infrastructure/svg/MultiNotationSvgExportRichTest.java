package com.marcosmoreira.domainmodelstudio.infrastructure.svg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialCrowsFootLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.notation.SwitchNotationUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.MarkdownDiagramParser;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Pruebas ricas de exportación SVG multi-notación.
 *
 * <p>Estas pruebas cuidan que la salida documental no se degrade a un archivo vacío
 * o a una simple cadena sin geometría. El SVG debe conservar el carácter vectorial
 * del producto: figuras, textos, conectores y metadatos de notación.</p>
 */
class MultiNotationSvgExportRichTest {

    @Test
    void chenSvgContainsExpectedConceptualGeometryAndMetadata() throws Exception {
        DiagramProject project = importedProjectWithChenLayout("examples/markdown/supermercado_chen_detallado.md");

        String svg = new MultiNotationSvgDiagramExporter().export(project);

        assertBasicSvgDocument(svg, "notation=CHEN");
        assertTrue(svg.contains("<rect"), "Chen debe exportar entidades como rectángulos.");
        assertTrue(svg.contains("<ellipse"), "Chen debe exportar atributos como óvalos.");
        assertTrue(svg.contains("<polygon"), "Chen debe exportar relaciones como rombos/polígonos.");
        assertTrue(svg.contains("<polyline"), "Chen debe exportar conectores vectoriales.");
        assertTrue(svg.contains("<text"), "Chen debe exportar etiquetas legibles.");
        assertTrue(svg.contains("Producto") || svg.contains("producto"));
    }

    @Test
    void crowsFootSvgContainsCompactEntityBoxesAndRelationshipMarkers() throws Exception {
        DiagramProject chenProject = importedProjectWithChenLayout("examples/markdown/supermercado_multi_notacion.md");
        DiagramProject crowsFootProject = new SwitchNotationUseCase(
                new GenerateInitialChenLayoutUseCase(),
                new GenerateInitialCrowsFootLayoutUseCase()
        ).switchTo(chenProject, NotationType.CROWS_FOOT);

        String svg = new MultiNotationSvgDiagramExporter().export(crowsFootProject);

        assertBasicSvgDocument(svg, "notation=CROWS_FOOT");
        assertTrue(svg.contains("<rect"), "Crow's Foot debe exportar cajas de entidad compactas.");
        assertTrue(svg.contains("<line"), "Crow's Foot debe exportar relaciones directas con líneas.");
        assertTrue(svg.contains("<text"), "Crow's Foot debe exportar nombres y atributos internos.");
        assertTrue(svg.contains("font-weight=\"bold\""), "La cabecera de entidad debe diferenciarse visualmente.");
        assertFalse(svg.contains("<ellipse"), "Crow's Foot básico no debe dibujar atributos como óvalos Chen.");
    }

    private static void assertBasicSvgDocument(String svg, String expectedNotationMetadata) {
        assertTrue(svg.startsWith("<?xml"), "La exportación debe iniciar como documento XML.");
        assertTrue(svg.contains("<svg"), "La exportación debe contener raíz SVG.");
        assertTrue(svg.contains("<metadata>"), "La exportación debe incluir metadatos mínimos.");
        assertTrue(svg.contains(expectedNotationMetadata), "La exportación debe declarar la notación activa.");
        assertTrue(svg.contains("</svg>"), "La exportación debe cerrar el documento SVG.");
        assertTrue(svg.length() > 1_000, "El SVG de ejemplo no debería ser una salida vacía o mínima.");
    }

    private static DiagramProject importedProjectWithChenLayout(String path) throws Exception {
        ImportMarkdownModelUseCase importUseCase = new ImportMarkdownModelUseCase(
                new MarkdownDiagramParser(),
                new DiagramProjectValidator()
        );
        return new GenerateInitialChenLayoutUseCase()
                .generate(importUseCase.importFile(Path.of(path)).project());
    }
}
