package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import java.io.IOException;
import java.nio.file.Path;

/** Contrato para exportar el diccionario de datos como Markdown profesional. */
public interface ExportDataDictionaryMarkdownUseCase {

    Path export(DataDictionaryDocument document, Path destinationFile) throws IOException;

    String exportToString(DataDictionaryDocument document);
}
