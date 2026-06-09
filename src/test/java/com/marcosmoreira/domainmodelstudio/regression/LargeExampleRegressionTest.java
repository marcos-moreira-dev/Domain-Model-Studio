package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import com.marcosmoreira.domainmodelstudio.infrastructure.svg.MultiNotationSvgDiagramExporter;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de regresión con ejemplos reales y un modelo grande.
 *
 * <p>El objetivo no es exigir que el layout automático quede bello, sino garantizar que
 * el flujo técnico no se rompa con modelos más grandes o con ejemplos representativos.</p>
 */
class LargeExampleRegressionTest {

    private final ImportMarkdownModelUseCase importUseCase = new ImportMarkdownModelUseCase(
            new MarkdownDiagramParser(),
            new DiagramProjectValidator()
    );
    private final GenerateInitialChenLayoutUseCase chenLayout = new GenerateInitialChenLayoutUseCase();
    private final GenerateInitialCrowsFootLayoutUseCase crowsFootLayout = new GenerateInitialCrowsFootLayoutUseCase();
    private final SwitchNotationUseCase switchNotation = new SwitchNotationUseCase(chenLayout, crowsFootLayout);
    private final MultiNotationSvgDiagramExporter svgExporter = new MultiNotationSvgDiagramExporter();

    @Test
    void representativeExamplesShouldImportGenerateLayoutAndExportSvg() {
        List<String> examples = List.of(
                "examples/markdown/colegio_chen_detallado.md",
                "examples/markdown/supermercado_chen_detallado.md",
                "examples/markdown/restaurante_chen_detallado.md"
        );

        for (String example : examples) {
            assertDoesNotThrow(() -> {
                DiagramProject project = chenLayout.generate(importUseCase.importFile(Path.of(example)).project());
                assertFalse(project.model().entities().isEmpty(), example + " debe tener entidades");
                assertFalse(project.model().relationships().isEmpty(), example + " debe tener relaciones");
                String svg = svgExporter.export(project);
                assertTrue(svg.contains("<svg"), example + " debe exportar SVG válido");
                assertTrue(svg.length() > 1_000, example + " no debe producir SVG vacío");
            }, "Falló flujo representativo para " + example);
        }
    }

    @Test
    void largeFortyEntityExampleShouldSurviveFullChenAndCrowsFootFlow() throws Exception {
        DiagramProject imported = importUseCase
                .importFile(Path.of("examples/markdown/regresion/supermercado_40_entidades_chen.md"))
                .project();
        assertTrue(imported.model().entityCount() >= 40, "El ejemplo grande debe tener al menos 40 entidades.");
        assertTrue(imported.model().relationshipCount() >= 40, "El ejemplo grande debe tener al menos 40 relaciones.");

        DiagramProject chenProject = chenLayout.generate(imported);
        String chenSvg = svgExporter.export(chenProject);
        assertTrue(chenSvg.contains("notation=CHEN"));
        assertTrue(chenSvg.contains("<ellipse"), "Chen debe conservar atributos externos como óvalos.");
        assertTrue(chenSvg.length() > 20_000, "El SVG Chen grande no debería ser mínimo.");

        DiagramProject crowsFootProject = switchNotation.switchTo(chenProject, NotationType.CROWS_FOOT);
        String crowsFootSvg = svgExporter.export(crowsFootProject);
        assertTrue(crowsFootSvg.contains("notation=CROWS_FOOT"));
        assertTrue(crowsFootSvg.contains("<rect"), "Crow's Foot debe usar cajas de entidad.");
        assertTrue(crowsFootSvg.length() > 10_000, "El SVG Crow's Foot grande no debería ser mínimo.");
    }
}
