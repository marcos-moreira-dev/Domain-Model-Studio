package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/** Funciones comunes para clonar layout, IDs y metadatos durante pegado visual. */
final class ProjectVisualSelectionTransferSupport {

    private static final double PASTE_OFFSET = 36.0;

    private ProjectVisualSelectionTransferSupport() {
    }

static List<UmlClassMember> copyMembers(List<UmlClassMember> members, Set<String> pending) {
        List<UmlClassMember> copied = new ArrayList<>();
        for (UmlClassMember member : members == null ? List.<UmlClassMember>of() : members) {
            String newId = uniqueRawId(member.id(), id -> true, pending);
            pending.add(newId);
            copied.add(new UmlClassMember(newId, member.kind(), member.name(), member.type(), member.signature(), member.visibility(), member.staticMember(), member.description()));
        }
        return copied;
    }

    static DiagramLayout copyNodeLayouts(
            DiagramLayout layout,
            ProjectVisualSelectionTransferPayload payload,
            Function<String, String> newRawIdResolver,
            Function<String, String> newLayoutIdResolver
    ) {
        DiagramLayout updated = layout;
        for (NodeLayout nodeLayout : payload.nodeLayouts()) {
            String oldLayoutId = nodeLayout.elementId().value();
            String newRawId = newRawIdResolver.apply(oldLayoutId);
            if (newRawId == null || newRawId.isBlank()) continue;
            String newLayoutId = newLayoutIdResolver.apply(newRawId);
            updated = updated.withNode(new NodeLayout(
                    DiagramElementId.of(newLayoutId),
                    nodeLayout.position().translatedBy(PASTE_OFFSET, PASTE_OFFSET),
                    nodeLayout.size(),
                    nodeLayout.visible(),
                    nodeLayout.locked(),
                    nodeLayout.zOrder()));
        }
        return updated;
    }

    static DiagramLayout copyConnectorLayouts(
            DiagramLayout layout,
            ProjectVisualSelectionTransferPayload payload,
            Function<String, String> newRawConnectorResolver,
            Function<String, String> newRawNodeResolver,
            Function<String, String> newConnectorLayoutIdResolver,
            Function<String, String> newNodeLayoutIdResolver
    ) {
        DiagramLayout updated = layout;
        for (ConnectorLayout connectorLayout : payload.connectorLayouts()) {
            String newConnectorRawId = newRawConnectorResolver.apply(connectorLayout.connectorId().value());
            if (newConnectorRawId == null || newConnectorRawId.isBlank()) continue;
            String newSourceRawId = newRawNodeResolver.apply(connectorLayout.sourceElementId().value());
            String newTargetRawId = newRawNodeResolver.apply(connectorLayout.targetElementId().value());
            if (newSourceRawId == null || newTargetRawId == null) continue;
            updated = updated.withConnector(copyConnectorLayout(
                    connectorLayout,
                    DiagramElementId.of(newConnectorLayoutIdResolver.apply(newConnectorRawId)),
                    DiagramElementId.of(newNodeLayoutIdResolver.apply(newSourceRawId)),
                    DiagramElementId.of(newNodeLayoutIdResolver.apply(newTargetRawId))));
        }
        return updated;
    }

    static DiagramLayout copyConnectorLayouts(
            DiagramLayout layout,
            ProjectVisualSelectionTransferPayload payload,
            Function<String, String> newRawConnectorResolver,
            Function<String, String> newRawNodeResolver,
            Function<String, String> rawIdentity
    ) {
        return copyConnectorLayouts(layout, payload, newRawConnectorResolver, newRawNodeResolver, rawIdentity, rawIdentity);
    }

    static ConnectorLayout copyConnectorLayout(ConnectorLayout layout, DiagramElementId connectorId,
                                                       DiagramElementId sourceId, DiagramElementId targetId) {
        List<BendPoint> bendPoints = layout.bendPoints().stream()
                .map(point -> point.translatedBy(PASTE_OFFSET, PASTE_OFFSET))
                .toList();
        return new ConnectorLayout(
                connectorId,
                sourceId,
                targetId,
                layout.sourceAnchor(),
                layout.targetAnchor(),
                layout.pathKind(),
                bendPoints,
                layout.sourceMarker(),
                layout.targetMarker(),
                layout.sourceMarkerOrientation(),
                layout.targetMarkerOrientation(),
                layout.labelOffsetX(),
                layout.labelOffsetY(),
                layout.visible());
    }

    static Set<String> rawIds(Set<String> values, String prefix) {
        Set<String> result = new LinkedHashSet<>();
        for (String value : values == null ? Set.<String>of() : values) {
            String raw = rawAfter(value, prefix);
            if (!raw.isBlank()) result.add(raw);
        }
        return result;
    }

    static String rawAfter(String value, String prefix) {
        String normalized = value == null ? "" : value.strip();
        return normalized.startsWith(prefix) ? normalized.substring(prefix.length()).strip() : "";
    }


    static Set<String> conceptualElementIds(DiagramModel model) {
        Set<String> ids = new LinkedHashSet<>();
        if (model == null) {
            return ids;
        }
        for (EntityElement entity : model.entities()) {
            ids.add(entity.id().value());
            for (AttributeElement attribute : entity.attributes()) {
                ids.add(attribute.id().value());
            }
        }
        for (RelationshipElement relationship : model.relationships()) {
            ids.add(relationship.id().value());
        }
        return ids;
    }

    static Set<String> idsOfArchitectureNodes(ArchitectureDiagramDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.nodes().forEach(node -> ids.add(node.id()));
        return ids;
    }

    static Set<String> idsOfArchitectureEdges(ArchitectureDiagramDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.edges().forEach(edge -> ids.add(edge.id()));
        return ids;
    }

    static Set<String> idsOfBehaviorNodes(BehaviorDiagramDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.nodes().forEach(node -> ids.add(node.id()));
        return ids;
    }

    static Set<String> idsOfBehaviorEdges(BehaviorDiagramDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.edges().forEach(edge -> ids.add(edge.id()));
        return ids;
    }

    static Set<String> idsOfModules(ModuleMapDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.modules().forEach(module -> ids.add(module.id()));
        return ids;
    }

    static Set<String> idsOfDependencies(ModuleMapDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.dependencies().forEach(dependency -> ids.add(dependency.id()));
        return ids;
    }

    static Set<String> idsOfScreens(ScreenFlowDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.screens().forEach(screen -> ids.add(screen.id()));
        return ids;
    }

    static Set<String> idsOfTransitions(ScreenFlowDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.transitions().forEach(transition -> ids.add(transition.id()));
        return ids;
    }

    static Set<String> idsOfWireframeScreens(WireframeDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.screens().forEach(screen -> ids.add(screen.id()));
        return ids;
    }

    static Set<String> idsOfWireframeComponents(WireframeDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.components().forEach(component -> ids.add(component.id()));
        return ids;
    }

    static Set<String> idsOfUmlModules(UmlClassDiagramDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.modules().forEach(module -> ids.add(module.id()));
        return ids;
    }

    static Set<String> idsOfUmlClasses(UmlClassDiagramDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.classes().forEach(node -> ids.add(node.id()));
        return ids;
    }

    static Set<String> idsOfUmlRelations(UmlClassDiagramDocument document) {
        Set<String> ids = new LinkedHashSet<>();
        document.relations().forEach(relation -> ids.add(relation.id()));
        return ids;
    }

    static String uniqueRawId(String originalId, Set<String> occupiedIds, Iterable<String> pendingIds) {
        Set<String> occupied = new LinkedHashSet<>();
        if (occupiedIds != null) {
            for (String id : occupiedIds) {
                if (id != null && !id.isBlank()) {
                    occupied.add(id.strip());
                }
            }
        }
        return uniqueRawId(originalId, id -> !occupied.contains(id), pendingIds);
    }

    static String uniqueRawId(String originalId, Predicate<String> available, Iterable<String> pendingIds) {
        Set<String> pending = new LinkedHashSet<>();
        for (String id : pendingIds == null ? List.<String>of() : pendingIds) {
            if (id != null && !id.isBlank()) pending.add(id.strip());
        }
        String base = originalId == null || originalId.isBlank() ? "copiado" : originalId.strip();
        int suffix = 1;
        String candidate;
        do {
            candidate = base + "_copia" + (suffix == 1 ? "" : "_" + suffix);
            suffix++;
        } while (!available.test(candidate) || pending.contains(candidate));
        return candidate;
    }

    static String copyName(String value) {
        String normalized = value == null ? "" : value.strip();
        return normalized.isBlank() ? "Copia" : normalized + " (copia)";
    }

    static Set<String> with(Set<String> base, String... values) {
        Set<String> updated = new LinkedHashSet<>(base == null ? Set.of() : base);
        for (String value : values == null ? new String[0] : values) {
            if (value != null && !value.isBlank()) updated.add(value.strip());
        }
        return updated;
    }
}
