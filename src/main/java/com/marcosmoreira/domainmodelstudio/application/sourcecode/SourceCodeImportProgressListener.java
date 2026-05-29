package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/** Recibe mensajes de avance durante escaneo y parseo de código fuente. */
@FunctionalInterface
public interface SourceCodeImportProgressListener {

    SourceCodeImportProgressListener NONE = message -> { };

    void onProgress(String message);
}
