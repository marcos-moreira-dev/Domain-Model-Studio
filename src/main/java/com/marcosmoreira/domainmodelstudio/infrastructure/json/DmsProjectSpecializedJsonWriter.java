package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;

/** Coordina la escritura de secciones especializadas opcionales del bloque model. */
final class DmsProjectSpecializedJsonWriter {

    private final DmsProjectCoreSpecializedJsonWriter coreWriter = new DmsProjectCoreSpecializedJsonWriter();
    private final DmsProjectAuxiliarySpecializedJsonWriter auxiliaryWriter = new DmsProjectAuxiliarySpecializedJsonWriter();
    private final DmsProjectLogicalBusinessJsonWriter logicalBusinessWriter = new DmsProjectLogicalBusinessJsonWriter();

    void writeSpecializedDocuments(DiagramProject project, StringBuilder json, int level) {
        coreWriter.writeCoreDocuments(project, json, level);
        auxiliaryWriter.writeAuxiliaryDocuments(project, json, level);
        project.logicalBusinessDocument().ifPresent(document -> logicalBusinessWriter.write(document, json, level, true));
    }
}
