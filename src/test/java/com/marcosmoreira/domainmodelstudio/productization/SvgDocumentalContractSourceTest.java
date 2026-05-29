package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 25: SVG es vectorial documental, no WYSIWYG universal. */
class SvgDocumentalContractSourceTest {

    @Test
    void shellAndToolbarShouldUseDocumentalSvgLanguage() throws Exception {
        String contract = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/SvgExportContract.java"));
        String shellView = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java"));
        String commandHandler = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ExportCommandHandler.java"));
        String tooltipCatalog = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarTooltipCatalog.java"));

        assertTrue(contract.contains("SVG vectorial documental"));
        assertTrue(contract.contains("no promete ser una copia WYSIWYG exacta"));
        assertTrue(shellView.contains("SvgExportContract.MENU_LABEL"));
        assertTrue(commandHandler.contains("SvgExportContract.DIALOG_TITLE"));
        assertTrue(tooltipCatalog.contains("SvgExportContract.TOOLTIP_DETAIL"));
    }

    @Test
    void productDocumentationShouldStateDocumentalSvgContract() throws Exception {
        String userGuide = Files.readString(Path.of("docs/user-guide/05_exportar_svg_png.md"));
        String matrix = Files.readString(Path.of("docs/producto/MATRIZ_CAPACIDADES_REALES.md"));
        String decisions = Files.readString(Path.of("docs/producto/decisiones_producto.md"));

        assertTrue(userGuide.contains("SVG vectorial documental"));
        assertTrue(userGuide.contains("no promete ser una copia WYSIWYG exacta"));
        assertTrue(matrix.contains("SVG documental"));
        assertTrue(decisions.contains("SVG vectorial documental"));
    }
}
