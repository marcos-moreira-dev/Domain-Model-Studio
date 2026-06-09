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

/**
 * Contenedor explícito de los documentos especializados opcionales persistidos dentro de {@code model}.
 *
 * <p>La Tanda 31 separa estos payloads del reader/writer principal para que el formato
 * {@code .dms} sea más mantenible. El record no define un nuevo schema: solo agrupa las
 * mismas claves opcionales que ya viven dentro del bloque {@code model}.</p>
 */

record DmsProjectSpecializedPayload(
        DataDictionaryDocument dataDictionary,
        ModuleMapDocument moduleMap,
        UmlClassDiagramDocument umlClassDiagram,
        RolesPermissionsDocument rolesPermissions,
        ScreenFlowDocument screenFlow,
        WireframeDocument wireframe,
        BehaviorDiagramDocument behaviorDiagram,
        ArchitectureDiagramDocument architectureDiagram,
        FreeGraphDocument freeGraph,
        LogicalBusinessDocument logicalBusinessDocument,
        LogicalBusinessGraphDocument logicalBusinessGraphDocument
) {
}
