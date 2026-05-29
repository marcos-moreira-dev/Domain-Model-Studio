package com.marcosmoreira.domainmodelstudio.application.theory;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Catálogo teórico oficial para el centro de ayuda tipo referencia académica. */
public final class DefaultTheoryCatalog implements TheoryCatalog {

    private static final String TOPICS_ROOT = "help/topics/";

    private static final List<TheoryTopicResource> OFFICIAL_RESOURCES = List.of(
            resource(TheoryTopicId.CONCEPTUAL_MODEL, DiagramTypeId.CONCEPTUAL_MODEL, "conceptual-model.md"),
            resource(TheoryTopicId.DATA_DICTIONARY, DiagramTypeId.DATA_DICTIONARY, "data-dictionary.md"),
            resource(TheoryTopicId.BPMN_BASIC, DiagramTypeId.BPMN_BASIC, "bpmn-basic.md"),
            resource(TheoryTopicId.OPERATIONAL_FLOW, DiagramTypeId.OPERATIONAL_FLOW, "operational-flow.md"),
            resource(TheoryTopicId.C4_CONTEXT_CONTAINERS, DiagramTypeId.C4_CONTEXT, "c4-context-containers.md"),
            resource(TheoryTopicId.TECHNICAL_DEPLOYMENT, DiagramTypeId.TECHNICAL_DEPLOYMENT, "technical-deployment.md"),
            resource(TheoryTopicId.UML_USE_CASE, DiagramTypeId.UML_USE_CASE, "uml-use-case.md"),
            resource(TheoryTopicId.UML_CLASS, DiagramTypeId.UML_CLASS, "uml-class.md"),
            resource(TheoryTopicId.UML_ACTIVITY, DiagramTypeId.UML_ACTIVITY, "uml-activity.md"),
            resource(TheoryTopicId.UML_SEQUENCE, DiagramTypeId.UML_SEQUENCE, "uml-sequence.md"),
            resource(TheoryTopicId.UML_STATE, DiagramTypeId.UML_STATE, "uml-state.md"),
            resource(TheoryTopicId.ADMIN_MODULE_MAP, DiagramTypeId.ADMIN_MODULE_MAP, "admin-module-map.md"),
            resource(TheoryTopicId.ROLES_PERMISSIONS, DiagramTypeId.ROLES_PERMISSIONS_MAP, "roles-permissions-map.md"),
            resource(TheoryTopicId.SCREEN_FLOW, DiagramTypeId.SCREEN_FLOW, "screen-flow.md"),
            resource(TheoryTopicId.ADMIN_WIREFRAMES, DiagramTypeId.ADMIN_WIREFRAMES, "admin-wireframes.md"),
            resource(TheoryTopicId.FREE_GRAPH, DiagramTypeId.FREE_GRAPH, "free-graph.md"),
            resource(TheoryTopicId.LOGICAL_BUSINESS_INTAKE, DiagramTypeId.LOGICAL_BUSINESS_INTAKE, "logical-business-intake.md"),
            resource(TheoryTopicId.LOGICAL_BUSINESS_GRAPH, DiagramTypeId.LOGICAL_BUSINESS_GRAPH, "logical-business-graph.md")
    );

    private final List<TheoryTopic> topics;
    private final Map<TheoryTopicId, TheoryTopic> topicsById;
    private final Map<DiagramTypeId, TheoryTopic> topicsByDiagramType;

    public DefaultTheoryCatalog() {
        this(new TheoryTopicResourceLoader().loadAll(OFFICIAL_RESOURCES));
    }

    public DefaultTheoryCatalog(List<TheoryTopic> topics) {
        this.topics = List.copyOf(Objects.requireNonNull(topics, "topics"));
        this.topicsById = indexById(this.topics);
        this.topicsByDiagramType = indexByDiagramType(this.topics);
    }

    public List<TheoryTopic> findAll() {
        return topics;
    }

    @Override
    public Optional<TheoryTopic> findById(TheoryTopicId id) {
        Objects.requireNonNull(id, "id");
        return Optional.ofNullable(topicsById.get(id));
    }

    @Override
    public Optional<TheoryTopic> findByDiagramType(DiagramTypeId diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        return Optional.ofNullable(topicsByDiagramType.get(diagramTypeId));
    }

    private static Map<TheoryTopicId, TheoryTopic> indexById(List<TheoryTopic> topics) {
        Map<TheoryTopicId, TheoryTopic> index = new LinkedHashMap<>();
        for (TheoryTopic topic : topics) {
            TheoryTopic previous = index.put(topic.id(), topic);
            if (previous != null) {
                throw new IllegalArgumentException("Tema teórico duplicado: " + topic.id().value());
            }
        }
        return Map.copyOf(index);
    }

    private static Map<DiagramTypeId, TheoryTopic> indexByDiagramType(List<TheoryTopic> topics) {
        Map<DiagramTypeId, TheoryTopic> index = new LinkedHashMap<>();
        for (TheoryTopic topic : topics) {
            index.putIfAbsent(topic.diagramTypeId(), topic);
            if (topic.id().equals(TheoryTopicId.C4_CONTEXT_CONTAINERS)) {
                index.putIfAbsent(DiagramTypeId.C4_CONTAINERS, topic);
            }
        }
        return Map.copyOf(index);
    }

    private static TheoryTopicResource resource(TheoryTopicId topicId, DiagramTypeId diagramTypeId, String fileName) {
        return new TheoryTopicResource(topicId, diagramTypeId, TOPICS_ROOT + fileName);
    }
}
