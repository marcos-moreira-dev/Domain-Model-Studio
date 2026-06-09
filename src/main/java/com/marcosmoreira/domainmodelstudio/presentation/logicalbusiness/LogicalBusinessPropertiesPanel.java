package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/** Módulo SideBar: propiedades del documento o elemento seleccionado. */
final class LogicalBusinessPropertiesPanel {

    private final LogicalBusinessViewModel viewModel;
    private final VBox content = LogicalBusinessUiNodes.panelRoot();
    private final ScrollPane root = new ScrollPane(content);

    LogicalBusinessPropertiesPanel(LogicalBusinessViewModel viewModel) {
        this.viewModel = viewModel;
        root.setFitToWidth(true);
        viewModel.currentProjectProperty().addListener((obs, oldValue, newValue) -> refresh());
        viewModel.selectionProperty().addListener((obs, oldValue, newValue) -> refresh());
        refresh();
    }

    Parent root() {
        return root;
    }

    private void refresh() {
        content.getChildren().clear();
        if (viewModel.selectedAttribute().isPresent()) {
            renderAttribute(viewModel.selectedAttribute().get());
            return;
        }
        if (viewModel.selectedRelationship().isPresent()) {
            renderRelationship(viewModel.selectedRelationship().get());
            return;
        }
        if (viewModel.selectedPendingQuestion().isPresent()) {
            renderPendingQuestion(viewModel.selectedPendingQuestion().get());
            return;
        }
        if (viewModel.selectedEntity().isPresent()) {
            renderEntity(viewModel.selectedEntity().get());
            return;
        }
        if (viewModel.selectedItem().isPresent()) {
            renderItem(viewModel.selectedItem().get());
            return;
        }
        renderDocument(viewModel.currentDocument());
    }

    private void renderAttribute(LogicalBusinessAttributeCandidate attribute) {
        content.getChildren().add(LogicalBusinessUiNodes.subtitle("Atributo seleccionado"));
        content.getChildren().addAll(
                LogicalBusinessUiNodes.meta("ID", attribute.id()),
                LogicalBusinessUiNodes.meta("Entidad", attribute.entityId()),
                LogicalBusinessUiNodes.meta("Nombre", attribute.name()),
                LogicalBusinessUiNodes.meta("Tipo tentativo", attribute.tentativeType()),
                LogicalBusinessUiNodes.meta("Calculado", attribute.calculated() ? "Sí" : "No"),
                LogicalBusinessUiNodes.subtitle("Razón"),
                LogicalBusinessUiNodes.text(attribute.reason()),
                LogicalBusinessUiNodes.subtitle("Fórmula"),
                LogicalBusinessUiNodes.text(attribute.formula()),
                LogicalBusinessUiNodes.subtitle("Riesgo"),
                LogicalBusinessUiNodes.text(attribute.riskIfWrong()),
                LogicalBusinessUiNodes.meta("Fuentes", String.join(", ", attribute.sourceReferences()))
        );
    }

    private void renderRelationship(LogicalBusinessRelationshipCandidate relationship) {
        content.getChildren().add(LogicalBusinessUiNodes.subtitle("Relación seleccionada"));
        content.getChildren().addAll(
                LogicalBusinessUiNodes.meta("ID", relationship.id()),
                LogicalBusinessUiNodes.meta("Origen", relationship.sourceEntityId()),
                LogicalBusinessUiNodes.meta("Destino", relationship.targetEntityId()),
                LogicalBusinessUiNodes.meta("Nombre", relationship.name()),
                LogicalBusinessUiNodes.meta("Cardinalidad tentativa", relationship.cardinalityHint()),
                LogicalBusinessUiNodes.subtitle("Justificación"),
                LogicalBusinessUiNodes.text(relationship.justification()),
                LogicalBusinessUiNodes.meta("Fuentes", String.join(", ", relationship.sourceReferences()))
        );
    }

    private void renderPendingQuestion(LogicalBusinessPendingQuestion question) {
        content.getChildren().add(LogicalBusinessUiNodes.subtitle("Pregunta seleccionada"));
        content.getChildren().addAll(
                LogicalBusinessUiNodes.meta("ID", question.id()),
                LogicalBusinessUiNodes.meta("Prioridad", question.priority().name()),
                LogicalBusinessUiNodes.meta("Estado", LogicalBusinessStatusFormatter.itemStatus(question.status())),
                LogicalBusinessUiNodes.subtitle("Pregunta"),
                LogicalBusinessUiNodes.text(question.question()),
                LogicalBusinessUiNodes.subtitle("Afecta"),
                LogicalBusinessUiNodes.text(question.affects())
        );
    }

    private void renderDocument(LogicalBusinessDocument document) {
        content.getChildren().add(LogicalBusinessUiNodes.subtitle("Documento activo"));
        if (document == null) {
            content.getChildren().add(LogicalBusinessUiNodes.text("No hay levantamiento lógico activo."));
            return;
        }
        content.getChildren().addAll(
                LogicalBusinessUiNodes.meta("Proyecto", document.projectName()),
                LogicalBusinessUiNodes.meta("Versión", document.version()),
                LogicalBusinessUiNodes.meta("Fecha", document.documentDate().toString()),
                LogicalBusinessUiNodes.meta("Estado", LogicalBusinessStatusFormatter.documentStatus(document.documentStatus())),
                LogicalBusinessUiNodes.meta("Fuente principal", document.mainSource()),
                LogicalBusinessUiNodes.meta("Madurez", LogicalBusinessStatusFormatter.maturity(document.maturity().level()))
        );
    }

    private void renderItem(LogicalBusinessItem item) {
        content.getChildren().add(LogicalBusinessUiNodes.subtitle("Elemento seleccionado"));
        content.getChildren().addAll(
                LogicalBusinessUiNodes.meta("ID", item.id()),
                LogicalBusinessUiNodes.meta("Tipo", LogicalBusinessStatusFormatter.itemKind(item.kind())),
                LogicalBusinessUiNodes.meta("Estado", LogicalBusinessStatusFormatter.itemStatus(item.status())),
                LogicalBusinessUiNodes.meta("Fuente", item.source()),
                LogicalBusinessUiNodes.subtitle("Descripción"),
                LogicalBusinessUiNodes.text(item.description()),
                LogicalBusinessUiNodes.subtitle("Lectura humana"),
                LogicalBusinessUiNodes.text(item.humanReading()),
                LogicalBusinessUiNodes.subtitle("Contenido"),
                LogicalBusinessUiNodes.text(item.content()),
                LogicalBusinessUiNodes.meta("Referencias", String.join(", ", item.referenceIds()))
        );
    }

    private void renderEntity(LogicalBusinessEntityCandidate entity) {
        content.getChildren().add(LogicalBusinessUiNodes.subtitle("Entidad seleccionada"));
        content.getChildren().addAll(
                LogicalBusinessUiNodes.meta("ID", entity.id()),
                LogicalBusinessUiNodes.meta("Nombre", entity.name()),
                LogicalBusinessUiNodes.meta("Estado", LogicalBusinessStatusFormatter.itemStatus(entity.status())),
                LogicalBusinessUiNodes.subtitle("Justificación lógica"),
                LogicalBusinessUiNodes.text(entity.logicalJustification()),
                LogicalBusinessUiNodes.meta("Fuentes", String.join(", ", entity.sourceReferences())),
                LogicalBusinessUiNodes.meta("Reglas", String.join(", ", entity.associatedRules())),
                LogicalBusinessUiNodes.meta("Invariantes", String.join(", ", entity.associatedInvariants())),
                LogicalBusinessUiNodes.subtitle("Atributos candidatos")
        );
        for (LogicalBusinessAttributeCandidate attribute : entity.attributes()) {
            content.getChildren().add(LogicalBusinessUiNodes.text(attribute.id() + " — " + attribute.name()
                    + " · " + attribute.reason()));
        }
        content.getChildren().add(LogicalBusinessUiNodes.subtitle("Relaciones candidatas"));
        for (LogicalBusinessRelationshipCandidate relationship : entity.relationships()) {
            content.getChildren().add(LogicalBusinessUiNodes.text(relationship.id() + " — " + relationship.name()
                    + " · " + relationship.justification()));
        }
        content.getChildren().addAll(
                LogicalBusinessUiNodes.subtitle("Riesgo de modelado"),
                LogicalBusinessUiNodes.text(entity.modelingRisk())
        );
    }
}
