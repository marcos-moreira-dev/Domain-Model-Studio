package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessFormControls.*;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Workspace principal del levantamiento lógico.
 *
 * <p>La vista central porta el resultado humano del artefacto activo. Los datos
 * editables se presentan como formulario de escritorio; los datos automáticos se
 * mantienen como lectura, sin simular campos editables falsos.</p>
 */
final class LogicalBusinessDocumentView {

    private static final String APPLY_DOCUMENT_LABEL = "Actualizar documento";

    private final LogicalBusinessViewModel viewModel;
    private final VBox content = new VBox(0);
    private final ScrollPane root = new ScrollPane(content);

    LogicalBusinessDocumentView(LogicalBusinessViewModel viewModel) {
        this.viewModel = viewModel;
        content.setPadding(Insets.EMPTY);
        content.getStyleClass().add("logical-business-document-content");
        root.setFitToWidth(true);
        root.getStyleClass().add("logical-business-document-scroll");
        viewModel.currentProjectProperty().addListener((obs, oldValue, newValue) -> refresh());
        viewModel.selectionProperty().addListener((obs, oldValue, newValue) -> refresh());
        refresh();
    }

    Parent root() {
        return root;
    }

    private void refresh() {
        content.getChildren().clear();
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null) {
            renderEmpty("No hay levantamiento lógico activo.");
            return;
        }
        renderHeader(document);
        if (viewModel.selection().kindIs(LogicalBusinessSelectionKind.DOCUMENT)
                || viewModel.selection().kindIs(LogicalBusinessSelectionKind.GROUP)) {
            renderDocumentOverview(document);
            return;
        }
        if (viewModel.selection().kindIs(LogicalBusinessSelectionKind.MATURITY)) {
            renderMaturity();
            return;
        }
        if (viewModel.selectedAttribute().isPresent()) {
            renderAttribute(viewModel.selectedEntity().orElse(null), viewModel.selectedAttribute().get());
            return;
        }
        if (viewModel.selectedRelationship().isPresent()) {
            renderRelationship(viewModel.selectedEntity().orElse(null), viewModel.selectedRelationship().get());
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
        if (viewModel.selectedSection().isPresent()) {
            renderSection(document, viewModel.selectedSection().get());
            return;
        }
        renderEmpty("El documento está creado, pero todavía no contiene secciones importadas.");
    }

    private void renderHeader(LogicalBusinessDocument document) {
        VBox header = new VBox(8);
        header.getStyleClass().add("logical-business-expedient-header");
        header.getChildren().add(title(document.projectName()));
        header.getChildren().add(paragraph("Documento estructurado para convertir entrevistas, observaciones y reglas en una fuente lógica canónica del negocio."));
        header.getChildren().add(chipRow(List.of(
                "Versión " + document.version(),
                LogicalBusinessStatusFormatter.documentStatus(document.documentStatus()),
                "Madurez " + LogicalBusinessStatusFormatter.maturity(document.maturity().level())
        )));
        content.getChildren().add(header);
    }

    private void renderDocumentOverview(LogicalBusinessDocument document) {
        VBox form = form("Resumen del expediente", "Datos generales editables y conteos automáticos del levantamiento.");
        form.getChildren().add(metricRow(
                metric("Secciones", Integer.toString(document.sections().size())),
                metric("Elementos", Integer.toString(document.items().size())),
                metric("Entidades", Integer.toString(document.entityCandidates().size())),
                metric("Preguntas", Integer.toString(document.pendingQuestions().size()))
        ));

        TextField projectName = textField(document.projectName());
        TextField version = textField(document.version());
        DatePicker date = new DatePicker(document.documentDate());
        ComboBox<LogicalBusinessDocumentStatus> status = combo(document.documentStatus(),
                LogicalBusinessDocumentStatus.values(), LogicalBusinessStatusFormatter::documentStatus);
        TextArea mainSource = textArea(document.mainSource(), 2);
        TextArea notes = textArea(document.notes(), 4);
        form.getChildren().addAll(
                formRow("Nombre del proyecto", projectName),
                formRow("Versión", version),
                formRow("Fecha", date),
                formRow("Estado visible", status),
                formRow("Fuente principal", mainSource),
                formRow("Notas", notes),
                applyButton(() -> viewModel.applyDocumentHeader(projectName.getText(), version.getText(),
                        safeDate(date.getValue()), status.getValue(), mainSource.getText(), notes.getText()))
        );
        content.getChildren().add(form);
    }

    private void renderMaturity() {
        var maturity = viewModel.assessedMaturity();
        VBox form = form("Madurez del levantamiento", "Resumen para decidir si el expediente puede usarse como fuente revisable.");
        form.getChildren().add(chipRow(List.of(LogicalBusinessStatusFormatter.maturity(maturity.level()))));
        addListField(form, "Fortalezas", maturity.strengths());
        addListField(form, "Bloqueos", maturity.blockers());
        addListField(form, "Siguientes pasos", maturity.nextSteps());
        content.getChildren().add(form);
    }

    private void renderSection(LogicalBusinessDocument document, LogicalBusinessSection section) {
        VBox form = form(section.title(), "Sección navegable del levantamiento lógico.");
        TextField titleField = textField(section.title());
        TextArea purpose = textArea(section.purpose(), 3);
        ComboBox<LogicalBusinessItemStatus> status = combo(section.status(),
                LogicalBusinessItemStatus.values(), LogicalBusinessStatusFormatter::itemStatus);
        TextArea notes = textArea(section.notes(), 3);
        form.getChildren().addAll(
                chipRow(List.of(section.id(), LogicalBusinessStatusFormatter.itemStatus(section.status()))),
                formRow("Título", titleField),
                formRow("Propósito", purpose),
                formRow("Estado", status),
                formRow("Notas", notes)
        );
        LogicalBusinessLinkedRows.addSectionItems(document, section, form, viewModel);
        form.getChildren().add(applyButton(() -> viewModel.applySectionEdit(section, titleField.getText(),
                purpose.getText(), status.getValue(), notes.getText())));
        content.getChildren().add(form);
    }

    private void renderItem(LogicalBusinessItem item) {
        VBox form = form(item.id() + " — " + item.title(), LogicalBusinessItemPresentation.kindReading(item));
        TextField titleField = textField(item.title());
        ComboBox<LogicalBusinessItemStatus> status = combo(item.status(),
                LogicalBusinessItemStatus.values(), LogicalBusinessStatusFormatter::itemStatus);
        TextArea source = textArea(item.source(), 2);
        TextArea description = textArea(item.description(), 3);
        TextArea humanReading = textArea(item.humanReading(), 3);
        TextArea contentField = textArea(item.content(), 7);
        TextArea references = textArea(String.join("\n", item.referenceIds()), 3);
        form.getChildren().addAll(
                chipRow(List.of(LogicalBusinessStatusFormatter.itemKind(item.kind()),
                        LogicalBusinessStatusFormatter.itemStatus(item.status()))),
                formRow("Título", titleField),
                formRow("Estado", status),
                formRow("Fuente", source),
                formRow("Descripción", description),
                formRow("Lectura humana", humanReading),
                formRow(LogicalBusinessItemPresentation.fieldTitleForContent(item), contentField),
                formRow("Referencias del expediente", references),
                applyButton(() -> viewModel.applyItemEdit(item, titleField.getText(), status.getValue(),
                        source.getText(), description.getText(), humanReading.getText(), contentField.getText(),
                        parseList(references.getText())))
        );
        content.getChildren().add(form);
    }

    private void renderEntity(LogicalBusinessEntityCandidate entity) {
        VBox form = form(entity.id() + " — " + entity.name(), "Entidad candidata del negocio. Todavía no es tabla final ni decisión física de base de datos.");
        form.getChildren().add(metricRow(
                metric("Atributos", Integer.toString(entity.attributes().size())),
                metric("Relaciones", Integer.toString(entity.relationships().size())),
                metric("Fuentes", Integer.toString(entity.sourceReferences().size()))
        ));
        TextField name = textField(entity.name());
        ComboBox<LogicalBusinessItemStatus> status = combo(entity.status(),
                LogicalBusinessItemStatus.values(), LogicalBusinessStatusFormatter::itemStatus);
        TextArea justification = textArea(entity.logicalJustification(), 4);
        TextArea sources = textArea(String.join("\n", entity.sourceReferences()), 3);
        TextArea rules = textArea(String.join("\n", entity.associatedRules()), 3);
        TextArea invariants = textArea(String.join("\n", entity.associatedInvariants()), 3);
        TextArea created = textArea(String.join("\n", entity.createdByUseCases()), 3);
        TextArea modified = textArea(String.join("\n", entity.modifiedByUseCases()), 3);
        TextArea queried = textArea(String.join("\n", entity.queriedByUseCases()), 3);
        TextArea risk = textArea(entity.modelingRisk(), 4);
        form.getChildren().addAll(
                chipRow(List.of("Entidad candidata", LogicalBusinessStatusFormatter.itemStatus(entity.status()))),
                formRow("Nombre", name), formRow("Estado", status),
                formRow("Justificación lógica", justification), formRow("Fuentes", sources),
                formRow("Reglas asociadas", rules), formRow("Invariantes asociadas", invariants),
                formRow("Creada por", created), formRow("Modificada por", modified),
                formRow("Consultada por", queried), formRow("Riesgo de modelado", risk)
        );
        LogicalBusinessLinkedRows.addEntityCandidateChildren(entity, form, viewModel);
        form.getChildren().add(applyButton(() -> viewModel.applyEntityEdit(entity, name.getText(), status.getValue(),
                justification.getText(), parseList(sources.getText()), parseList(rules.getText()),
                parseList(invariants.getText()), parseList(created.getText()), parseList(modified.getText()),
                parseList(queried.getText()), risk.getText())));
        content.getChildren().add(form);
    }

    private void renderAttribute(LogicalBusinessEntityCandidate entity, LogicalBusinessAttributeCandidate attribute) {
        VBox form = form(attribute.id() + " — " + attribute.name(),
                "Atributo candidato de " + attribute.entityId() + ". Todavía no es columna final ni tipo físico definitivo.");
        TextField name = textField(attribute.name());
        TextArea reason = textArea(attribute.reason(), 3);
        TextField tentativeType = textField(attribute.tentativeType());
        CheckBox calculated = new CheckBox("Atributo calculado");
        calculated.setSelected(attribute.calculated());
        TextArea formula = textArea(attribute.formula(), 3);
        TextArea risk = textArea(attribute.riskIfWrong(), 3);
        TextArea sources = textArea(String.join("\n", attribute.sourceReferences()), 3);
        TextArea rules = textArea(String.join("\n", attribute.relatedRules()), 3);
        TextArea invariants = textArea(String.join("\n", attribute.relatedInvariants()), 3);
        form.getChildren().addAll(
                chipRow(List.of("Atributo candidato", attribute.calculated() ? "Calculado" : "Dato registrado")),
                formRow("Nombre", name), formRow("Razón", reason), formRow("Tipo tentativo", tentativeType),
                formRow("Cálculo", calculated), formRow("Fórmula o criterio", formula),
                formRow("Riesgo si se modela mal", risk), formRow("Fuentes", sources),
                formRow("Reglas relacionadas", rules), formRow("Invariantes relacionadas", invariants),
                applyButton(() -> viewModel.applyAttributeEdit(entity, attribute, name.getText(), reason.getText(),
                        tentativeType.getText(), calculated.isSelected(), formula.getText(), risk.getText(),
                        parseList(sources.getText()), parseList(rules.getText()), parseList(invariants.getText())))
        );
        content.getChildren().add(form);
    }

    private void renderRelationship(LogicalBusinessEntityCandidate entity, LogicalBusinessRelationshipCandidate relationship) {
        VBox form = form(relationship.id() + " — " + relationship.name(), "Relación candidata entre entidades. Todavía no es llave foránea ni cardinalidad física aprobada.");
        TextField source = textField(relationship.sourceEntityId());
        TextField target = textField(relationship.targetEntityId());
        TextField name = textField(relationship.name());
        TextField cardinality = textField(relationship.cardinalityHint());
        TextArea justification = textArea(relationship.justification(), 4);
        TextArea sources = textArea(String.join("\n", relationship.sourceReferences()), 3);
        form.getChildren().addAll(
                chipRow(List.of("Relación candidata", relationship.sourceEntityId() + " → " + relationship.targetEntityId())),
                formRow("Entidad origen", source), formRow("Entidad destino", target), formRow("Nombre", name),
                formRow("Cardinalidad tentativa", cardinality), formRow("Justificación", justification),
                formRow("Fuentes", sources),
                applyButton(() -> viewModel.applyRelationshipEdit(entity, relationship, source.getText(),
                        target.getText(), name.getText(), cardinality.getText(), justification.getText(),
                        parseList(sources.getText())))
        );
        content.getChildren().add(form);
    }

    private void renderPendingQuestion(LogicalBusinessPendingQuestion question) {
        VBox form = form(question.id() + " — Pregunta pendiente", "Duda que puede bloquear reglas, entidades o decisiones internas del levantamiento.");
        TextArea questionText = textArea(question.question(), 4);
        TextArea affects = textArea(question.affects(), 3);
        ComboBox<LogicalBusinessQuestionPriority> priority = combo(question.priority(),
                LogicalBusinessQuestionPriority.values(), LogicalBusinessStatusFormatter::priority);
        ComboBox<LogicalBusinessItemStatus> status = combo(question.status(),
                LogicalBusinessItemStatus.values(), LogicalBusinessStatusFormatter::itemStatus);
        form.getChildren().addAll(
                chipRow(List.of("Prioridad " + LogicalBusinessStatusFormatter.priority(question.priority()),
                        LogicalBusinessStatusFormatter.itemStatus(question.status()))),
                formRow("Pregunta", questionText), formRow("Afecta", affects),
                formRow("Prioridad", priority), formRow("Estado", status),
                applyButton(() -> viewModel.applyPendingQuestionEdit(question, questionText.getText(), affects.getText(),
                        priority.getValue(), status.getValue()))
        );
        content.getChildren().add(form);
    }

    private void renderEmpty(String message) {
        VBox form = form("Levantamiento lógico", message);
        form.getChildren().add(paragraph("Usa este módulo como expediente lógico: estados, acciones, reglas, entidades, atributos, evidencia y madurez."));
        content.getChildren().add(form);
    }
}
