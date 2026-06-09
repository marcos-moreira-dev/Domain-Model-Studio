package com.marcosmoreira.domainmodelstudio.presentation.sidebar;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.selection.DiagramSelectionModel;
import java.util.Objects;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;

/**
 * ViewModel del árbol de modelo.
 *
 * <p>el árbol mantiene IDs estables y se sincroniza con la selección
 * compartida del canvas. No parsea archivos ni valida; solo representa un proyecto ya
 * construido por application.</p>
 */
public final class ModelTreeViewModel {

    private final StringProperty title = new SimpleStringProperty("Estructura");
    private final TreeItem<ModelTreeNode> rootItem = new TreeItem<>(ModelTreeNode.group("Proyecto actual"));
    private final DiagramSelectionModel selectionModel;

    public ModelTreeViewModel(DiagramSelectionModel selectionModel) {
        this.selectionModel = Objects.requireNonNull(selectionModel, "selectionModel");
        loadPlaceholderTree();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public TreeItem<ModelTreeNode> rootItem() {
        return rootItem;
    }

    public DiagramSelectionModel selectionModel() {
        return selectionModel;
    }

    public void selectFromTree(ModelTreeNode node) {
        if (node == null || !node.selectable()) {
            return;
        }
        selectionModel.select(node.elementId().orElseThrow());
    }

    public Optional<NotationType> notationFromTree(ModelTreeNode node) {
        return node == null ? Optional.empty() : node.notation();
    }

    public void clearProject() {
        loadPlaceholderTree();
    }

    public void loadProject(DiagramProject project) {
        rootItem.setValue(ModelTreeNode.group(project.metadata().title()));
        rootItem.getChildren().clear();
        rootItem.setExpanded(true);

        TreeItem<ModelTreeNode> entities = new TreeItem<>(ModelTreeNode.group("Entidades (" + project.model().entityCount() + ")"));
        entities.setExpanded(true);
        for (EntityElement entity : project.model().entities()) {
            TreeItem<ModelTreeNode> entityItem = new TreeItem<>(ModelTreeNode.element(entity.name() + "  [" + entity.id() + "]", entity.id()));
            TreeItem<ModelTreeNode> attributes = new TreeItem<>(ModelTreeNode.group("Atributos (" + entity.attributes().size() + ")"));
            for (AttributeElement attribute : entity.attributes()) {
                attributes.getChildren().add(new TreeItem<>(ModelTreeNode.element(
                        attribute.name() + formatTags(attribute),
                        attribute.id()
                )));
            }
            attributes.setExpanded(false);
            entityItem.getChildren().add(attributes);
            entities.getChildren().add(entityItem);
        }

        TreeItem<ModelTreeNode> relationships = new TreeItem<>(ModelTreeNode.group("Relaciones (" + project.model().relationshipCount() + ")"));
        relationships.setExpanded(true);
        for (RelationshipElement relationship : project.model().relationships()) {
            relationships.getChildren().add(new TreeItem<>(ModelTreeNode.element(
                    relationship.name()
                            + "  [" + relationship.fromCardinality().displayText()
                            + " - " + relationship.toCardinality().displayText() + "]",
                    relationship.id()
            )));
        }

        TreeItem<ModelTreeNode> views = new TreeItem<>(ModelTreeNode.group("Vistas"));
        views.getChildren().add(new TreeItem<>(notationNode("Chen", NotationType.CHEN, project.metadata().activeNotation())));
        views.getChildren().add(new TreeItem<>(notationNode("Pata de gallo", NotationType.CROWS_FOOT, project.metadata().activeNotation())));
        views.setExpanded(true);

        rootItem.getChildren().add(entities);
        rootItem.getChildren().add(relationships);
        rootItem.getChildren().add(views);
    }

    public TreeItem<ModelTreeNode> findItemByElementId(DiagramElementId elementId) {
        if (elementId == null) {
            return null;
        }
        return findItemByElementId(rootItem, elementId);
    }

    private TreeItem<ModelTreeNode> findItemByElementId(TreeItem<ModelTreeNode> item, DiagramElementId elementId) {
        if (item == null) {
            return null;
        }
        if (item.getValue() != null && item.getValue().elementId().filter(elementId::equals).isPresent()) {
            return item;
        }
        for (TreeItem<ModelTreeNode> child : item.getChildren()) {
            TreeItem<ModelTreeNode> found = findItemByElementId(child, elementId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private ModelTreeNode notationNode(String label, NotationType notation, NotationType activeNotation) {
        String visibleLabel = notation == activeNotation ? label + " - activa" : label;
        return ModelTreeNode.notation(visibleLabel, notation);
    }

    private void loadPlaceholderTree() {
        rootItem.setValue(ModelTreeNode.group("Sin proyecto abierto"));
        rootItem.setExpanded(true);
        rootItem.getChildren().clear();

        TreeItem<ModelTreeNode> start = new TreeItem<>(ModelTreeNode.group("Primeros pasos"));
        start.getChildren().add(new TreeItem<>(ModelTreeNode.group("Importar Markdown estructurado")));
        start.getChildren().add(new TreeItem<>(ModelTreeNode.group("Abrir proyecto .dms")));
        start.getChildren().add(new TreeItem<>(ModelTreeNode.group("Crear proyecto nuevo")));
        start.setExpanded(true);

        TreeItem<ModelTreeNode> views = new TreeItem<>(ModelTreeNode.group("Vistas disponibles"));
        views.getChildren().add(new TreeItem<>(ModelTreeNode.group("Chen")));
        views.getChildren().add(new TreeItem<>(ModelTreeNode.group("Pata de gallo")));
        views.setExpanded(true);

        rootItem.getChildren().add(start);
        rootItem.getChildren().add(views);
    }

    private String formatTags(AttributeElement attribute) {
        return attribute.tags().isEmpty() ? "" : " " + attribute.tags();
    }
}
