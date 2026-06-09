package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import java.util.Map;

/**
 * Lee el conjunto de documentos especializados opcionales del bloque {@code model} de un proyecto {@code .dms}.
 *
 * <p>Su responsabilidad es detectar qué payloads están presentes y delegar la lectura concreta
 * al reader especializado existente. No migra versiones, no cambia nombres de claves y no decide
 * qué workspace se abrirá después; esas decisiones permanecen en capas superiores.</p>
 */

final class DmsProjectSpecializedPayloadReader {

    private final DmsProjectSpecializedJsonReader specializedReader = new DmsProjectSpecializedJsonReader();

    DmsProjectSpecializedPayload read(Map<String, Object> modelObject) {
        return new DmsProjectSpecializedPayload(
                modelObject.containsKey("dataDictionary")
                        ? specializedReader.readDataDictionary(asObject(modelObject.get("dataDictionary"), "dataDictionary"))
                        : null,
                modelObject.containsKey("moduleMap")
                        ? specializedReader.readModuleMap(asObject(modelObject.get("moduleMap"), "moduleMap"))
                        : null,
                modelObject.containsKey("umlClassDiagram")
                        ? specializedReader.readUmlClassDiagram(asObject(modelObject.get("umlClassDiagram"), "umlClassDiagram"))
                        : null,
                modelObject.containsKey("rolesPermissions")
                        ? specializedReader.readRolesPermissions(asObject(modelObject.get("rolesPermissions"), "rolesPermissions"))
                        : null,
                modelObject.containsKey("screenFlow")
                        ? specializedReader.readScreenFlow(asObject(modelObject.get("screenFlow"), "screenFlow"))
                        : null,
                modelObject.containsKey("wireframe")
                        ? specializedReader.readWireframe(asObject(modelObject.get("wireframe"), "wireframe"))
                        : null,
                modelObject.containsKey("behaviorDiagram")
                        ? specializedReader.readBehaviorDiagram(asObject(modelObject.get("behaviorDiagram"), "behaviorDiagram"))
                        : null,
                modelObject.containsKey("architectureDiagram")
                        ? specializedReader.readArchitectureDiagram(asObject(modelObject.get("architectureDiagram"), "architectureDiagram"))
                        : null,
                modelObject.containsKey("freeGraph")
                        ? specializedReader.readFreeGraph(asObject(modelObject.get("freeGraph"), "freeGraph"))
                        : null,
                modelObject.containsKey("logicalBusinessDocument")
                        ? specializedReader.readLogicalBusinessDocument(asObject(modelObject.get("logicalBusinessDocument"), "logicalBusinessDocument"))
                        : null,
                modelObject.containsKey("logicalBusinessGraphDocument")
                        ? specializedReader.readLogicalBusinessGraphDocument(asObject(modelObject.get("logicalBusinessGraphDocument"), "logicalBusinessGraphDocument"))
                        : null
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asObject(Object value, String context) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException("Se esperaba objeto JSON en: " + context);
    }
}
