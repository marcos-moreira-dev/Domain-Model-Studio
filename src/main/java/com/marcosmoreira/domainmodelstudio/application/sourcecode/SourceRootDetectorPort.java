package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/** Puerto para detectar subproyectos, lenguajes y raíces lógicas antes del escaneo de archivos. */
public interface SourceRootDetectorPort {
    SourceRootDetectionResult detect(SourceCodeImportRequest request);
}
