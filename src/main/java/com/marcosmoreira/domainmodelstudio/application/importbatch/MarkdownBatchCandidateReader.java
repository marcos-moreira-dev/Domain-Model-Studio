package com.marcosmoreira.domainmodelstudio.application.importbatch;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** Puerto para listar y preclasificar archivos de una carpeta raíz Markdown. */
public interface MarkdownBatchCandidateReader {

    List<MarkdownImportCandidate> readCandidates(Path sourceRoot, MarkdownBatchImportPolicy policy) throws IOException;
}
