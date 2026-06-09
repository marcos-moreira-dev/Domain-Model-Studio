package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/** Puerto para detectar raíces, lenguajes y archivos candidatos dentro de un directorio fuente. */
public interface SourceDirectoryScannerPort {
    SourceScanResult scan(SourceCodeImportRequest request);
}
