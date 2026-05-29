package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProject;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportPhaseReporter;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportProgressListener;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeProjectParserUseCase;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import java.util.Objects;

/** Caso de uso que transforma un directorio de código en un diagrama UML Clases editable. */
public final class GenerateUmlClassDiagramFromSourceCodeUseCase {
    private final SourceCodeProjectParserUseCase parserUseCase;
    private final SourceCodeToUmlClassDiagramMapper mapper;

    public GenerateUmlClassDiagramFromSourceCodeUseCase(SourceCodeProjectParserUseCase parserUseCase,
                                                        SourceCodeToUmlClassDiagramMapper mapper) {
        this.parserUseCase = Objects.requireNonNull(parserUseCase, "parserUseCase");
        this.mapper = mapper == null ? new SourceCodeToUmlClassDiagramMapper() : mapper;
    }

    public UmlClassDiagramDocument generate(SourceCodeImportRequest request) {
        return generate(request, SourceCodeImportProgressListener.NONE);
    }

    public UmlClassDiagramDocument generate(SourceCodeImportRequest request, SourceCodeImportProgressListener progressListener) {
        SourceCodeImportProgressListener progress = progressListener == null
                ? SourceCodeImportProgressListener.NONE
                : progressListener;
        SourceCodeImportPhaseReporter reporter = SourceCodeImportPhaseReporter.start(progress);
        reporter.stage("Escaneo y parseo", "Iniciando lectura de raíces Java/TypeScript.");
        ParsedCodeProject parsedProject = parserUseCase.parse(request, reporter::progress);
        reporter.stage("Mapeo UML", "Construyendo documento UML Clases editable.");
        UmlClassDiagramDocument document = mapper.map(parsedProject);
        reporter.stage("Documento UML listo", summaryFor(document));
        return document;
    }

    private String summaryFor(UmlClassDiagramDocument document) {
        return document.classes().size() + " clases, "
                + document.modules().size() + " módulos, "
                + document.relations().size() + " relaciones y "
                + document.views().size() + " vistas internas.";
    }
}
