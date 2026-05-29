package com.marcosmoreira.domainmodelstudio.application.importmodel;

/**
 * Error controlado al interpretar Markdown estructurado.
 *
 * <p>Se usa para mensajes entendibles de importación. No representa fallos de IO,
 * sino errores de gramática, secciones faltantes o referencias incoherentes detectadas
 * durante el parseo.</p>
 */
public final class MarkdownModelParsingException extends Exception {

    public MarkdownModelParsingException(String message) {
        super(message);
    }

    public MarkdownModelParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
