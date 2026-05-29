package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;

/** Contrato para crear o derivar un diccionario de datos. */
public interface CreateDataDictionaryUseCase {

    DataDictionaryDocument createBlank(String projectName);
}
