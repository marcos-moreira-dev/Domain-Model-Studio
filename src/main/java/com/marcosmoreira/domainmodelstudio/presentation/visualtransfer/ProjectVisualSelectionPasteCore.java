package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import static com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferSupport.*;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferService.PasteResult;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/** Pegado de selecciones para canvas conceptual, arquitectura y comportamiento. */
final class ProjectVisualSelectionPasteCore {

    private ProjectVisualSelectionPasteCore() {
    }

static PasteResult pasteConceptual(DiagramProject targetProject, ProjectVisualSelectionTransferPayload payload) {
        DiagramProject sourceProject = payload.sourceProject();
        Set<String> selectedEntityIds = payload.selectedNodeIds();
        Set<String> selectedRelationshipIds = payload.selectedConnectorIds();
        for (RelationshipElement relationship : sourceProject.model().relationships()) {
            if (selectedRelationshipIds.contains(relationship.id().value())) {
                selectedEntityIds = with(selectedEntityIds, relationship.fromEntityId().value(), relationship.toEntityId().value());
            }
        }
        for (RelationshipElement relationship : sourceProject.model().relationships()) {
            if (selectedEntityIds.contains(relationship.fromEntityId().value())
                    && selectedEntityIds.contains(relationship.toEntityId().value())) {
                selectedRelationshipIds = with(selectedRelationshipIds, relationship.id().value());
            }
        }
        DiagramModel model = targetProject.model();
        Set<String> occupiedIds = conceptualElementIds(model);
        Map<String, String> entityIdMap = new LinkedHashMap<>();
        Set<String> pendingIds = new LinkedHashSet<>();
        for (EntityElement entity : sourceProject.model().entities()) {
            if (!selectedEntityIds.contains(entity.id().value())) {
                continue;
            }
            String newId = uniqueRawId(entity.id().value(), occupiedIds, pendingIds);
            pendingIds.add(newId);
            occupiedIds.add(newId);
            entityIdMap.put(entity.id().value(), newId);
            List<AttributeElement> copiedAttributes = new ArrayList<>();
            for (AttributeElement attribute : entity.attributes()) {
                String newAttributeId = uniqueRawId(attribute.id().value(), occupiedIds, pendingIds);
                pendingIds.add(newAttributeId);
                occupiedIds.add(newAttributeId);
                copiedAttributes.add(new AttributeElement(
                        DiagramElementId.of(newAttributeId),
                        attribute.name(),
                        attribute.tags(),
                        attribute.description()));
            }
            model = model.withEntity(new EntityElement(
                    DiagramElementId.of(newId),
                    copyName(entity.name()),
                    entity.kind(),
                    entity.module(),
                    entity.description(),
                    copiedAttributes));
        }
        Map<String, String> relationshipIdMap = new LinkedHashMap<>();
        for (RelationshipElement relationship : sourceProject.model().relationships()) {
            if (!selectedRelationshipIds.contains(relationship.id().value())) {
                continue;
            }
            String newSource = entityIdMap.get(relationship.fromEntityId().value());
            String newTarget = entityIdMap.get(relationship.toEntityId().value());
            if (newSource == null || newTarget == null) {
                continue;
            }
            String newId = uniqueRawId(relationship.id().value(), occupiedIds, pendingIds);
            pendingIds.add(newId);
            occupiedIds.add(newId);
            relationshipIdMap.put(relationship.id().value(), newId);
            model = model.withRelationship(new RelationshipElement(
                    DiagramElementId.of(newId),
                    copyName(relationship.name()),
                    DiagramElementId.of(newSource),
                    DiagramElementId.of(newTarget),
                    relationship.fromCardinality(),
                    relationship.toCardinality(),
                    relationship.kind(),
                    relationship.fromParticipation(),
                    relationship.toParticipation(),
                    relationship.description()));
        }
        DiagramLayout layout = targetProject.layouts().activeLayout();
        layout = copyNodeLayouts(layout, payload, id -> entityIdMap.get(id), Function.identity());
        layout = copyConnectorLayouts(layout, payload, id -> relationshipIdMap.get(id), id -> entityIdMap.get(id), Function.identity());
        DiagramProject updated = targetProject.withModel(model).withLayouts(targetProject.layouts().withLayout(layout));
        return new PasteResult(updated, entityIdMap.size(), relationshipIdMap.size(), "Selección pegada en Modelo conceptual.");
    }

static PasteResult pasteArchitecture(DiagramProject targetProject, ProjectVisualSelectionTransferPayload payload) {
        ArchitectureDiagramDocument source = payload.sourceProject().architectureDiagram().orElse(null);
        ArchitectureDiagramDocument document = targetProject.architectureDiagram().orElse(null);
        if (source == null || document == null) {
            return PasteResult.empty(targetProject);
        }
        Map<String, String> nodeIdMap = new LinkedHashMap<>();
        Set<String> occupiedNodeIds = idsOfArchitectureNodes(document);
        Set<String> occupiedEdgeIds = idsOfArchitectureEdges(document);
        Set<String> selectedNodeIds = rawIds(payload.selectedNodeIds(), "architecture-node:");
        Set<String> selectedEdgeIds = rawIds(payload.selectedConnectorIds(), "architecture-edge:");
        for (ArchitectureEdge edge : source.edges()) if (selectedEdgeIds.contains(edge.id())) selectedNodeIds = with(selectedNodeIds, edge.sourceNodeId(), edge.targetNodeId());
        for (ArchitectureEdge edge : source.edges()) if (selectedNodeIds.contains(edge.sourceNodeId()) && selectedNodeIds.contains(edge.targetNodeId())) selectedEdgeIds = with(selectedEdgeIds, edge.id());
        Set<String> pending = new LinkedHashSet<>();
        for (ArchitectureNode node : source.nodes()) {
            if (!selectedNodeIds.contains(node.id())) continue;
            String newId = uniqueRawId(node.id(), occupiedNodeIds, pending);
            pending.add(newId);
            occupiedNodeIds.add(newId);
            nodeIdMap.put(node.id(), newId);
            document = document.withNode(new ArchitectureNode(newId, node.kind(), copyName(node.displayName()), node.technology(), node.owner(), node.environment(), node.description(), node.notes(), document.nodes().size()));
        }
        Map<String, String> edgeIdMap = new LinkedHashMap<>();
        for (ArchitectureEdge edge : source.edges()) {
            if (!selectedEdgeIds.contains(edge.id())) continue;
            String sourceId = nodeIdMap.get(edge.sourceNodeId());
            String targetId = nodeIdMap.get(edge.targetNodeId());
            if (sourceId == null || targetId == null) continue;
            String newId = uniqueRawId(edge.id(), occupiedEdgeIds, pending);
            pending.add(newId);
            occupiedEdgeIds.add(newId);
            edgeIdMap.put(edge.id(), newId);
            document = document.withEdge(new ArchitectureEdge(newId, sourceId, targetId, edge.kind(), edge.label(), edge.protocol(), edge.notes()));
        }
        DiagramLayout layout = targetProject.layouts().activeLayout();
        layout = copyNodeLayouts(layout, payload, id -> nodeIdMap.get(rawAfter(id, "architecture-node:")), id -> VisualElementLayoutIds.architectureNode(id).value());
        layout = copyConnectorLayouts(layout, payload, id -> edgeIdMap.get(rawAfter(id, "architecture-edge:")), id -> nodeIdMap.get(rawAfter(id, "architecture-node:")), id -> VisualElementLayoutIds.architectureEdge(id).value(), id -> VisualElementLayoutIds.architectureNode(id).value());
        DiagramProject updated = targetProject.withArchitectureDiagram(document).withLayouts(targetProject.layouts().withLayout(layout));
        return new PasteResult(updated, nodeIdMap.size(), edgeIdMap.size(), "Selección pegada en diagrama de arquitectura.");
    }

static PasteResult pasteBehavior(DiagramProject targetProject, ProjectVisualSelectionTransferPayload payload) {
        BehaviorDiagramDocument source = payload.sourceProject().behaviorDiagram().orElse(null);
        BehaviorDiagramDocument document = targetProject.behaviorDiagram().orElse(null);
        if (source == null || document == null) return PasteResult.empty(targetProject);
        Set<String> selectedNodeIds = rawIds(payload.selectedNodeIds(), "behavior-node:");
        Set<String> selectedEdgeIds = rawIds(payload.selectedConnectorIds(), "behavior-edge:");
        for (BehaviorEdge edge : source.edges()) if (selectedEdgeIds.contains(edge.id())) selectedNodeIds = with(selectedNodeIds, edge.sourceNodeId(), edge.targetNodeId());
        for (BehaviorEdge edge : source.edges()) if (selectedNodeIds.contains(edge.sourceNodeId()) && selectedNodeIds.contains(edge.targetNodeId())) selectedEdgeIds = with(selectedEdgeIds, edge.id());
        Map<String, String> nodeIdMap = new LinkedHashMap<>();
        Set<String> occupiedNodeIds = idsOfBehaviorNodes(document);
        Set<String> occupiedEdgeIds = idsOfBehaviorEdges(document);
        Set<String> pending = new LinkedHashSet<>();
        for (BehaviorNode node : source.nodes()) {
            if (!selectedNodeIds.contains(node.id())) continue;
            String newId = uniqueRawId(node.id(), occupiedNodeIds, pending);
            pending.add(newId);
            nodeIdMap.put(node.id(), newId);
            document = document.withNode(new BehaviorNode(newId, node.kind(), copyName(node.displayName()), node.owner(), node.description(), node.notes(), document.nodes().size()));
        }
        Map<String, String> edgeIdMap = new LinkedHashMap<>();
        for (BehaviorEdge edge : source.edges()) {
            if (!selectedEdgeIds.contains(edge.id())) continue;
            String sourceId = nodeIdMap.get(edge.sourceNodeId());
            String targetId = nodeIdMap.get(edge.targetNodeId());
            if (sourceId == null || targetId == null) continue;
            String newId = uniqueRawId(edge.id(), occupiedEdgeIds, pending);
            pending.add(newId);
            edgeIdMap.put(edge.id(), newId);
            document = document.withEdge(new BehaviorEdge(newId, sourceId, targetId, edge.kind(), edge.label(), edge.condition(), edge.notes()));
        }
        DiagramLayout layout = targetProject.layouts().activeLayout();
        layout = copyNodeLayouts(layout, payload, id -> nodeIdMap.get(rawAfter(id, "behavior-node:")), id -> VisualElementLayoutIds.behaviorNode(id).value());
        layout = copyConnectorLayouts(layout, payload, id -> edgeIdMap.get(rawAfter(id, "behavior-edge:")), id -> nodeIdMap.get(rawAfter(id, "behavior-node:")), id -> VisualElementLayoutIds.behaviorEdge(id).value(), id -> VisualElementLayoutIds.behaviorNode(id).value());
        DiagramProject updated = targetProject.withBehaviorDiagram(document).withLayouts(targetProject.layouts().withLayout(layout));
        return new PasteResult(updated, nodeIdMap.size(), edgeIdMap.size(), "Selección pegada en diagrama de comportamiento.");
    }
}
