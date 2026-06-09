package com.marcosmoreira.domainmodelstudio.application.importmodel;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Puerto de aplicación para convertir Markdown estructurado en un proyecto de diagrama.
 *
 * <p>La capa application define el contrato; la infraestructura decide cómo parsear el
 * archivo concreto. Esta frontera evita que presentation conozca detalles del parser y
 * evita que application dependa de una clase técnica específica.</p>
 */
public interface MarkdownModelParser {

    DiagramProject parse(Path markdownFile) throws IOException, MarkdownModelParsingException;

    DiagramProject parse(String markdownContent, String sourceName) throws MarkdownModelParsingException;
}
