package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import java.util.Map;

/** Fachada de lectura para las secciones especializadas opcionales del bloque model. */
final class DmsProjectSpecializedJsonReader {

    private final DmsProjectCoreSpecializedJsonReader coreReader = new DmsProjectCoreSpecializedJsonReader();
    private final DmsProjectAuxiliarySpecializedJsonReader auxiliaryReader = new DmsProjectAuxiliarySpecializedJsonReader();
    private final DmsProjectLogicalBusinessJsonReader logicalBusinessReader = new DmsProjectLogicalBusinessJsonReader();

    DataDictionaryDocument readDataDictionary(Map<String, Object> object) {
        return coreReader.readDataDictionary(object);
    }

    ModuleMapDocument readModuleMap(Map<String, Object> object) {
        return coreReader.readModuleMap(object);
    }

    UmlClassDiagramDocument readUmlClassDiagram(Map<String, Object> object) {
        return coreReader.readUmlClassDiagram(object);
    }

    RolesPermissionsDocument readRolesPermissions(Map<String, Object> object) {
        return auxiliaryReader.readRolesPermissions(object);
    }

    ScreenFlowDocument readScreenFlow(Map<String, Object> object) {
        return auxiliaryReader.readScreenFlow(object);
    }

    WireframeDocument readWireframe(Map<String, Object> object) {
        return auxiliaryReader.readWireframe(object);
    }

    BehaviorDiagramDocument readBehaviorDiagram(Map<String, Object> object) {
        return auxiliaryReader.readBehaviorDiagram(object);
    }

    ArchitectureDiagramDocument readArchitectureDiagram(Map<String, Object> object) {
        return auxiliaryReader.readArchitectureDiagram(object);
    }

    FreeGraphDocument readFreeGraph(Map<String, Object> object) {
        return auxiliaryReader.readFreeGraph(object);
    }

    LogicalBusinessDocument readLogicalBusinessDocument(Map<String, Object> object) {
        return logicalBusinessReader.read(object);
    }

    LogicalBusinessGraphDocument readLogicalBusinessGraphDocument(Map<String, Object> object) {
        return auxiliaryReader.readLogicalBusinessGraph(object);
    }
}
