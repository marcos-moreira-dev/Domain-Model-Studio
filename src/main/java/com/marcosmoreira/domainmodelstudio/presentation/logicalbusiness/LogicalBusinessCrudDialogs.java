package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/** Formularios controlados para crear piezas del expediente lógico sin escribir texto fuente crudo. */
final class LogicalBusinessCrudDialogs {

    private LogicalBusinessCrudDialogs() {
    }

    static Optional<ItemRequest> item(List<LogicalBusinessSection> sections) {
        ComboBox<LogicalBusinessItemKind> kind = combo(List.of(
                LogicalBusinessItemKind.RULE,
                LogicalBusinessItemKind.PRECONDITION,
                LogicalBusinessItemKind.INVARIANT,
                LogicalBusinessItemKind.POSTCONDITION,
                LogicalBusinessItemKind.ACTION,
                LogicalBusinessItemKind.USE_CASE,
                LogicalBusinessItemKind.REPORT,
                LogicalBusinessItemKind.CALCULATION,
                LogicalBusinessItemKind.RISK,
                LogicalBusinessItemKind.SUPPORTING_ASSUMPTION,
                LogicalBusinessItemKind.EVIDENCE,
                LogicalBusinessItemKind.STATE,
                LogicalBusinessItemKind.ACTOR,
                LogicalBusinessItemKind.CONCEPT
        ));
        ComboBox<LogicalBusinessSection> section = combo(sections);
        section.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(LogicalBusinessSection value) {
                return value == null ? "Sin sección" : value.id() + " — " + value.title();
            }
            @Override public LogicalBusinessSection fromString(String value) { return section.getValue(); }
        });
        TextField title = new TextField();
        title.setPromptText("Nombre del elemento lógico");
        TextArea description = area("Descripción breve o criterio inicial", 4);
        return dialog("Crear elemento lógico", form(
                row("Tipo", kind), row("Sección", section), row("Título", title), row("Descripción", description)
        )).map(unused -> new ItemRequest(kind.getValue(), id(section.getValue()), title.getText(), description.getText()));
    }

    static Optional<EntityRequest> entity() {
        TextField name = new TextField();
        name.setPromptText("Nombre de la entidad candidata");
        TextArea justification = area("Razón lógica o evidencia mínima", 4);
        return dialog("Crear entidad candidata", form(
                row("Nombre", name), row("Justificación lógica", justification)
        )).map(unused -> new EntityRequest(name.getText(), justification.getText()));
    }

    static Optional<AttributeRequest> attribute() {
        TextField name = new TextField();
        name.setPromptText("Nombre del atributo candidato");
        TextArea reason = area("Por qué este dato existe en el negocio", 3);
        TextField type = new TextField();
        type.setPromptText("texto, número, fecha, dinero, booleano...");
        return dialog("Crear atributo candidato", form(
                row("Nombre", name), row("Razón", reason), row("Tipo tentativo", type)
        )).map(unused -> new AttributeRequest(name.getText(), reason.getText(), type.getText()));
    }

    static Optional<RelationshipRequest> relationship(List<LogicalBusinessEntityCandidate> entities) {
        ComboBox<LogicalBusinessEntityCandidate> source = entityCombo(entities);
        ComboBox<LogicalBusinessEntityCandidate> target = entityCombo(entities);
        TextField name = new TextField();
        name.setPromptText("Nombre de la relación candidata");
        TextField cardinality = new TextField();
        cardinality.setPromptText("1 - N, 0..1 - N, etc.");
        TextArea justification = area("Por qué ambas entidades deben relacionarse", 4);
        return dialog("Crear relación candidata", form(
                row("Entidad origen", source), row("Entidad destino", target), row("Nombre", name),
                row("Cardinalidad tentativa", cardinality), row("Justificación", justification)
        )).map(unused -> new RelationshipRequest(id(source.getValue()), id(target.getValue()), name.getText(),
                cardinality.getText(), justification.getText()));
    }

    static Optional<QuestionRequest> question() {
        TextArea question = area("Pregunta pendiente", 4);
        TextArea affects = area("Qué reglas, entidades, procesos o decisiones afecta", 3);
        ComboBox<LogicalBusinessQuestionPriority> priority = combo(List.of(LogicalBusinessQuestionPriority.values()));
        return dialog("Crear pregunta pendiente", form(
                row("Pregunta", question), row("Afecta", affects), row("Prioridad", priority)
        )).map(unused -> new QuestionRequest(question.getText(), affects.getText(), priority.getValue()));
    }

    private static ComboBox<LogicalBusinessEntityCandidate> entityCombo(List<LogicalBusinessEntityCandidate> entities) {
        ComboBox<LogicalBusinessEntityCandidate> box = combo(entities);
        box.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(LogicalBusinessEntityCandidate value) {
                return value == null ? "—" : value.id() + " — " + value.name();
            }
            @Override public LogicalBusinessEntityCandidate fromString(String value) { return box.getValue(); }
        });
        return box;
    }

    private static <T> ComboBox<T> combo(List<T> values) {
        ComboBox<T> box = new ComboBox<>();
        box.getItems().setAll(values);
        if (!values.isEmpty()) {
            box.setValue(values.get(0));
        }
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private static TextArea area(String prompt, int rows) {
        TextArea area = new TextArea();
        area.setPromptText(prompt);
        area.setWrapText(true);
        area.setPrefRowCount(rows);
        return area;
    }

    private static GridPane form(Node... rows) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(14));
        grid.setHgap(10);
        grid.setVgap(9);
        for (int i = 0; i < rows.length; i++) {
            grid.add(rows[i], 0, i);
        }
        return grid;
    }

    private static Node row(String label, Node field) {
        javafx.scene.control.Label caption = new javafx.scene.control.Label(label);
        caption.getStyleClass().add("logical-business-field-label");
        javafx.scene.layout.VBox box = new javafx.scene.layout.VBox(4, caption, field);
        GridPane.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private static Optional<ButtonType> dialog(String title, Node content) {
        Dialog<ButtonType> dialog = new Dialog<>();
        ButtonType create = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(create, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setMinWidth(560);
        return dialog.showAndWait().filter(create::equals);
    }

    private static String id(LogicalBusinessSection section) {
        return section == null ? "" : section.id();
    }

    private static String id(LogicalBusinessEntityCandidate entity) {
        return entity == null ? "" : entity.id();
    }

    record ItemRequest(LogicalBusinessItemKind kind, String sectionId, String title, String description) { }
    record EntityRequest(String name, String justification) { }
    record AttributeRequest(String name, String reason, String tentativeType) { }
    record RelationshipRequest(String sourceEntityId, String targetEntityId, String name, String cardinalityHint,
                               String justification) { }
    record QuestionRequest(String question, String affects, LogicalBusinessQuestionPriority priority) { }
}
