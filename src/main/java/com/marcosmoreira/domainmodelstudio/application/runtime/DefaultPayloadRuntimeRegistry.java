package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Registry por defecto de payloads de proyecto y su sección persistida en .dms v3. */
public final class DefaultPayloadRuntimeRegistry implements PayloadRuntimeRegistry {

    private final Map<DiagramTypeId, PayloadRuntimeDescriptor> descriptorsByType;
    private final Map<DiagramTypeId, PayloadRuntimeDescriptor> legacyDescriptorsByType;

    public DefaultPayloadRuntimeRegistry() {
        this(defaultDescriptors());
    }

    public DefaultPayloadRuntimeRegistry(List<PayloadRuntimeDescriptor> descriptors) {
        Objects.requireNonNull(descriptors, "descriptors");
        Map<DiagramTypeId, PayloadRuntimeDescriptor> indexed = new LinkedHashMap<>();
        for (PayloadRuntimeDescriptor descriptor : descriptors) {
            PayloadRuntimeDescriptor previous = indexed.put(descriptor.diagramTypeId(), descriptor);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Descriptor de payload duplicado para " + descriptor.diagramTypeId().value() + ".");
            }
        }
        this.descriptorsByType = Map.copyOf(indexed);
        this.legacyDescriptorsByType = Map.copyOf(legacyDescriptors());
    }

    @Override
    public Optional<PayloadRuntimeDescriptor> find(DiagramTypeId diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        PayloadRuntimeDescriptor descriptor = descriptorsByType.get(diagramTypeId);
        if (descriptor != null) {
            return Optional.of(descriptor);
        }
        return Optional.ofNullable(legacyDescriptorsByType.get(diagramTypeId));
    }

    @Override
    public PayloadRuntimeDescriptor require(DiagramTypeId diagramTypeId) {
        return find(diagramTypeId).orElseThrow(() -> new IllegalArgumentException(
                "No existe descriptor de payload para " + diagramTypeId.value() + "."));
    }

    @Override
    public List<PayloadRuntimeDescriptor> findAll() {
        return List.copyOf(descriptorsByType.values());
    }

    @Override
    public List<PayloadRuntimeDescriptor> specializedPayloadTypes() {
        return findAll().stream()
                .filter(PayloadRuntimeDescriptor::specializedPayload)
                .toList();
    }

    @Override
    public List<DiagramTypeId> detectSpecializedPayloadTypeIds(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        List<DiagramTypeId> present = new ArrayList<>();
        if (project.dataDictionary().isPresent()) present.add(DiagramTypeId.DATA_DICTIONARY);
        if (project.moduleMap().isPresent()) present.add(DiagramTypeId.ADMIN_MODULE_MAP);
        if (project.rolesPermissions().isPresent()) present.add(DiagramTypeId.ROLES_PERMISSIONS_MAP);
        if (project.screenFlow().isPresent()) present.add(DiagramTypeId.SCREEN_FLOW);
        if (project.wireframe().isPresent()) present.add(DiagramTypeId.ADMIN_WIREFRAMES);
        if (project.umlClassDiagram().isPresent()) present.add(DiagramTypeId.UML_CLASS);
        if (project.freeGraph().isPresent()) present.add(DiagramTypeId.FREE_GRAPH);
        if (project.logicalBusinessDocument().isPresent()) present.add(DiagramTypeId.LOGICAL_BUSINESS_INTAKE);
        if (project.logicalBusinessGraphDocument().isPresent()) present.add(DiagramTypeId.LOGICAL_BUSINESS_GRAPH);
        project.architectureDiagram()
                .map(document -> document.diagramKind().diagramTypeId())
                .ifPresent(present::add);
        project.behaviorDiagram()
                .map(document -> document.diagramKind().diagramTypeId())
                .ifPresent(present::add);
        return List.copyOf(present);
    }


    /**
     * Alias de compatibilidad para archivos .dms previos que guardaban ids editoriales antiguos.
     * No forma parte de findAll(), por lo que no aumenta el catálogo oficial ni el conteo de tipos.
     */
    private static Map<DiagramTypeId, PayloadRuntimeDescriptor> legacyDescriptors() {
        DiagramTypeId legacyUmlClass = new DiagramTypeId("uml-class-diagram");
        return Map.of(
                legacyUmlClass,
                new PayloadRuntimeDescriptor(
                        legacyUmlClass,
                        "legacy-uml-class-diagram-view",
                        "UML Clases legado",
                        "model.entities/model.relationships",
                        false,
                        project -> true,
                        project -> Optional.empty())
        );
    }

    private static List<PayloadRuntimeDescriptor> defaultDescriptors() {
        return List.of(
                conceptual(),
                dataDictionary(),
                moduleMap(),
                rolesPermissions(),
                screenFlow(),
                wireframe(),
                umlClass(),
                freeGraph(),
                logicalBusinessIntake(),
                logicalBusinessGraph(),
                architecture(DiagramTypeId.C4_CONTEXT, "C4 Contexto"),
                architecture(DiagramTypeId.C4_CONTAINERS, "C4 Contenedores"),
                architecture(DiagramTypeId.TECHNICAL_DEPLOYMENT, "Despliegue técnico"),
                behavior(DiagramTypeId.BPMN_BASIC, "BPMN básico"),
                behavior(DiagramTypeId.OPERATIONAL_FLOW, "Flujo operativo"),
                behavior(DiagramTypeId.UML_USE_CASE, "UML Casos de uso"),
                behavior(DiagramTypeId.UML_ACTIVITY, "UML Actividad"),
                behavior(DiagramTypeId.UML_SEQUENCE, "UML Secuencia"),
                behavior(DiagramTypeId.UML_STATE, "UML Estados")
        );
    }

    private static PayloadRuntimeDescriptor conceptual() {
        return new PayloadRuntimeDescriptor(
                DiagramTypeId.CONCEPTUAL_MODEL,
                "conceptual-er-model",
                "Modelo conceptual ER",
                "model.entities/model.relationships",
                false,
                project -> true,
                project -> Optional.empty());
    }

    private static PayloadRuntimeDescriptor dataDictionary() {
        return specialized(
                DiagramTypeId.DATA_DICTIONARY,
                "data-dictionary-document",
                "Diccionario de datos",
                "model.dataDictionary",
                project -> project.dataDictionary().isPresent());
    }

    private static PayloadRuntimeDescriptor moduleMap() {
        return specialized(
                DiagramTypeId.ADMIN_MODULE_MAP,
                "module-map-document",
                "Mapa de módulos",
                "model.moduleMap",
                project -> project.moduleMap().isPresent());
    }

    private static PayloadRuntimeDescriptor rolesPermissions() {
        return specialized(
                DiagramTypeId.ROLES_PERMISSIONS_MAP,
                "roles-permissions-document",
                "Roles y permisos",
                "model.rolesPermissions",
                project -> project.rolesPermissions().isPresent());
    }

    private static PayloadRuntimeDescriptor screenFlow() {
        return specialized(
                DiagramTypeId.SCREEN_FLOW,
                "screen-flow-document",
                "Flujo de pantallas",
                "model.screenFlow",
                project -> project.screenFlow().isPresent());
    }

    private static PayloadRuntimeDescriptor wireframe() {
        return specialized(
                DiagramTypeId.ADMIN_WIREFRAMES,
                "wireframe-document",
                "Wireframes administrativos",
                "model.wireframe",
                project -> project.wireframe().isPresent());
    }

    private static PayloadRuntimeDescriptor umlClass() {
        return specialized(
                DiagramTypeId.UML_CLASS,
                "uml-class-diagram-document",
                "UML Clases",
                "model.umlClassDiagram",
                project -> project.umlClassDiagram().isPresent());
    }

    private static PayloadRuntimeDescriptor freeGraph() {
        return specialized(
                DiagramTypeId.FREE_GRAPH,
                "free-graph-document",
                "Grafo libre",
                "model.freeGraph",
                project -> project.freeGraph().isPresent());
    }

    private static PayloadRuntimeDescriptor logicalBusinessIntake() {
        return specialized(
                DiagramTypeId.LOGICAL_BUSINESS_INTAKE,
                "logical-business-document",
                "Levantamiento lógico",
                "model.logicalBusinessDocument",
                project -> project.logicalBusinessDocument().isPresent());
    }

    private static PayloadRuntimeDescriptor logicalBusinessGraph() {
        return specialized(
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH,
                "logical-business-graph-document",
                "Grafo lógico del negocio",
                "model.logicalBusinessGraphDocument",
                project -> project.logicalBusinessGraphDocument().isPresent());
    }

    private static PayloadRuntimeDescriptor architecture(DiagramTypeId diagramTypeId, String displayName) {
        return new PayloadRuntimeDescriptor(
                diagramTypeId,
                "architecture-diagram-document",
                displayName,
                "model.architectureDiagram",
                true,
                project -> project.architectureDiagram().isPresent(),
                project -> project.architectureDiagram()
                        .filter(document -> !document.diagramKind().diagramTypeId().equals(diagramTypeId))
                        .map(document -> "El proyecto declara " + diagramTypeId.value()
                                + " pero el documento de arquitectura corresponde a "
                                + document.diagramKind().diagramTypeId().value() + "."));
    }

    private static PayloadRuntimeDescriptor behavior(DiagramTypeId diagramTypeId, String displayName) {
        return new PayloadRuntimeDescriptor(
                diagramTypeId,
                "behavior-diagram-document",
                displayName,
                "model.behaviorDiagram",
                true,
                project -> project.behaviorDiagram().isPresent(),
                project -> project.behaviorDiagram()
                        .filter(document -> !document.diagramKind().diagramTypeId().equals(diagramTypeId))
                        .map(document -> "El proyecto declara " + diagramTypeId.value()
                                + " pero el documento de comportamiento corresponde a "
                                + document.diagramKind().diagramTypeId().value() + "."));
    }

    private static PayloadRuntimeDescriptor specialized(
            DiagramTypeId diagramTypeId,
            String payloadKind,
            String displayName,
            String jsonSectionName,
            PayloadPresenceProbe presenceProbe
    ) {
        return new PayloadRuntimeDescriptor(
                diagramTypeId,
                payloadKind,
                displayName,
                jsonSectionName,
                true,
                presenceProbe,
                project -> Optional.empty());
    }
}
