package com.marcosmoreira.domainmodelstudio.domain.validation;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Índice auxiliar de IDs conocidos por el modelo.
 *
 * <p>Se usa para validar layout, conectores y estilos sin mezclar esa lógica con las
 * clases de modelo. Incluye entidades, atributos y relaciones del modelo conceptual.
 * Para proyectos especializados, agrega también los IDs visuales estables de sus
 * documentos propios, porque esos proyectos no guardan sus nodos en el modelo ER.</p>
 */
final class DiagramElementIndex {

    private final Set<DiagramElementId> semanticElementIds;

    private DiagramElementIndex(Set<DiagramElementId> semanticElementIds) {
        this.semanticElementIds = Set.copyOf(semanticElementIds);
    }

    static DiagramElementIndex from(DiagramModel model) {
        Set<DiagramElementId> ids = conceptualIds(model);
        return new DiagramElementIndex(ids);
    }

    static DiagramElementIndex from(DiagramProject project) {
        Set<DiagramElementId> ids = conceptualIds(project.model());
        addSpecializedIds(project, ids);
        return new DiagramElementIndex(ids);
    }

    boolean containsSemanticElement(DiagramElementId id) {
        return semanticElementIds.contains(id);
    }

    Set<DiagramElementId> semanticElementIds() {
        return semanticElementIds;
    }

    private static Set<DiagramElementId> conceptualIds(DiagramModel model) {
        Set<DiagramElementId> ids = new LinkedHashSet<>();
        for (EntityElement entity : model.entities()) {
            ids.add(entity.id());
            for (AttributeElement attribute : entity.attributes()) {
                ids.add(attribute.id());
            }
        }
        for (RelationshipElement relationship : model.relationships()) {
            ids.add(relationship.id());
        }
        return ids;
    }

    private static void addSpecializedIds(DiagramProject project, Set<DiagramElementId> ids) {
        project.moduleMap().ifPresent(document -> {
            for (ModuleNode module : document.modules()) {
                ids.add(prefixed("module", module.id()));
            }
        });

        project.screenFlow().ifPresent(document -> {
            for (ScreenNode screen : document.screens()) {
                ids.add(prefixed("screen", screen.id()));
            }
        });

        project.wireframe().ifPresent(document -> {
            for (WireframeScreen screen : document.screens()) {
                ids.add(prefixed("wireframe-screen", screen.id()));
            }
            for (WireframeComponent component : document.components()) {
                ids.add(prefixed("wireframe-component", component.id()));
            }
        });

        project.umlClassDiagram().ifPresent(document -> {
            for (UmlModuleGroup module : document.modules()) {
                ids.add(prefixed("uml-module", module.id()));
            }
            for (UmlClassNode umlClass : document.classes()) {
                ids.add(prefixed("uml-class", umlClass.id()));
            }
        });

        project.behaviorDiagram().ifPresent(document -> {
            for (BehaviorNode node : document.nodes()) {
                ids.add(prefixed("behavior-node", node.id()));
            }
        });

        project.architectureDiagram().ifPresent(document -> {
            for (ArchitectureNode node : document.nodes()) {
                ids.add(prefixed("architecture-node", node.id()));
            }
        });

        project.rolesPermissions().ifPresent(document -> {
            for (RoleNode role : document.roles()) {
                ids.add(prefixed("role", role.id()));
            }
            for (PermissionNode permission : document.permissions()) {
                ids.add(prefixed("permission", permission.id()));
            }
        });

        project.freeGraph().ifPresent(document -> {
            for (FreeGraphNode node : document.nodes()) {
                ids.add(prefixed("free-graph-node", node.id()));
            }
        });
    }

    private static DiagramElementId prefixed(String prefix, String rawId) {
        String normalized = rawId == null ? "" : rawId.strip();
        if (normalized.isBlank()) {
            return DiagramElementId.of(prefix + ":__blank__");
        }
        return DiagramElementId.of(prefix + ":" + normalized);
    }
}
