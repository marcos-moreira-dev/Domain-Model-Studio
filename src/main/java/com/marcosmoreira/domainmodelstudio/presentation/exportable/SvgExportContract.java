package com.marcosmoreira.domainmodelstudio.presentation.exportable;

/**
 * Contrato visible de la exportación SVG en Domain Model Studio.
 *
 * <p>El SVG es una salida vectorial documental del artefacto activo. No promete
 * ser una copia WYSIWYG exacta del canvas JavaFX; para eso existen PNG y smoke
 * visual por tipo. Esta clase centraliza el lenguaje para que menú, toolbar,
 * ayuda y documentación no prometan más de lo implementado.</p>
 */
public final class SvgExportContract {

    public static final String DISPLAY_NAME = "SVG vectorial documental";
    public static final String MENU_LABEL = "Exportar SVG vectorial documental...";
    public static final String DIALOG_TITLE = "Exportar SVG vectorial documental";
    public static final String SUCCESS_PREFIX = "SVG vectorial documental exportado en: ";
    public static final String CANCELED_STATUS = "Exportación SVG documental cancelada.";
    public static final String NOT_AVAILABLE_STATUS = "La salida activa no tiene SVG documental exportable.";
    public static final String FAILURE_PREFIX = "No se pudo exportar SVG documental: ";
    public static final String TOOLTIP_DETAIL = "Genera una salida vectorial documental: escalable, revisable y útil para informes; no promete ser una copia WYSIWYG exacta del canvas.";

    private SvgExportContract() {
    }

    public static boolean appliesTo(ExportFormat format) {
        return ExportFormat.SVG.equals(format);
    }
}
