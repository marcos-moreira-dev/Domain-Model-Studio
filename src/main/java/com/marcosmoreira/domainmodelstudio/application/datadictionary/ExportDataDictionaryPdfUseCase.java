package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import java.io.IOException;
import java.nio.file.Path;

/** Contrato futuro para exportar el diccionario de datos como PDF. */
public interface ExportDataDictionaryPdfUseCase {

    Path export(DataDictionaryDocument document, Path destinationFile) throws IOException;
}
