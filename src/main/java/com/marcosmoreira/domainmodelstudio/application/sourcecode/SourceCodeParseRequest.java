package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.List;

/** Solicitud específica que un adaptador de lenguaje recibe para parsear archivos fuente. */
public record SourceCodeParseRequest(
        SourceRoot sourceRoot,
        SourceLanguageVersion languageVersion,
        List<SourceFileCandidate> files,
        SourceCodeImportProgressListener progressListener
) {
    public SourceCodeParseRequest {
        if (sourceRoot == null) {
            throw new IllegalArgumentException("La raíz fuente a parsear no puede ser nula.");
        }
        languageVersion = languageVersion == null
                ? SourceLanguageVersion.flexible(SourceLanguage.UNKNOWN)
                : languageVersion;
        files = List.copyOf(files == null ? List.of() : files);
        progressListener = progressListener == null ? SourceCodeImportProgressListener.NONE : progressListener;
    }

    public SourceCodeParseRequest(SourceRoot sourceRoot, SourceLanguageVersion languageVersion, List<SourceFileCandidate> files) {
        this(sourceRoot, languageVersion, files, SourceCodeImportProgressListener.NONE);
    }
}
