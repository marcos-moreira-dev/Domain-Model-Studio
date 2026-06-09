package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/** Módulo SideDock: ayuda contextual, límites operativos y glosario obligatorio del expediente lógico. */
final class LogicalBusinessHelpPanel {

    private final LogicalBusinessViewModel viewModel;
    private final LogicalBusinessContextualHelpGuide guide = new LogicalBusinessContextualHelpGuide();
    private final VBox content = LogicalBusinessUiNodes.panelRoot();
    private final ScrollPane root = new ScrollPane(content);

    LogicalBusinessHelpPanel(LogicalBusinessViewModel viewModel) {
        this.viewModel = viewModel;
        root.setFitToWidth(true);
        root.getStyleClass().add("logical-business-help-scroll");
        content.getStyleClass().add("logical-business-help-content");
        viewModel.currentProjectProperty().addListener((observable, previous, current) -> refresh());
        viewModel.selectionProperty().addListener((observable, previous, current) -> refresh());
        refresh();
    }

    Parent root() {
        return root;
    }

    private void refresh() {
        content.getChildren().clear();
        LogicalBusinessContextualHelpGuide.HelpContent help = guide.forSelection(
                viewModel.selection(),
                viewModel.selectedItem().orElse(null)
        );
        content.getChildren().add(LogicalBusinessUiNodes.subtitle(help.focusTitle()));
        content.getChildren().add(LogicalBusinessUiNodes.text(help.intro()));
        help.sections().forEach(section -> content.getChildren().add(helpBlock(section)));
        content.getChildren().add(LogicalBusinessUiNodes.subtitle("Glosario obligatorio"));
        content.getChildren().add(LogicalBusinessUiNodes.text(
                "Estos recordatorios aplican siempre, aunque el foco seleccionado sea una regla, una acción, una entidad o una pregunta pendiente."));
        LogicalBusinessGlossary.glossarySections()
                .forEach(section -> content.getChildren().add(glossaryBlock(section)));
    }

    private VBox helpBlock(LogicalBusinessContextualHelpGuide.HelpSection section) {
        VBox block = new VBox(4);
        block.getStyleClass().add("logical-business-help-block");
        if ("Cierre documental".equals(section.title())) {
            block.getStyleClass().add("logical-business-help-closing");
        }
        block.getChildren().add(LogicalBusinessUiNodes.subtitle(section.title()));
        block.getChildren().add(LogicalBusinessUiNodes.text(section.body()));
        return block;
    }

    private VBox glossaryBlock(LogicalBusinessGlossary.GlossarySection section) {
        VBox block = new VBox(5);
        block.getStyleClass().add("logical-business-glossary-section");
        if (section.styleClass() != null && !section.styleClass().isBlank()) {
            block.getStyleClass().add(section.styleClass());
        }
        block.getChildren().add(LogicalBusinessUiNodes.subtitle(section.title()));
        block.getChildren().add(LogicalBusinessUiNodes.text(section.intro()));
        section.entries().forEach(entry -> block.getChildren().add(glossaryEntry(entry)));
        return block;
    }

    private VBox glossaryEntry(LogicalBusinessGlossary.GlossaryEntry entry) {
        VBox row = new VBox(1);
        row.getStyleClass().add("logical-business-glossary-entry");
        row.getChildren().add(LogicalBusinessUiNodes.compactMeta(entry.term()));
        row.getChildren().add(LogicalBusinessUiNodes.text(entry.description()));
        return row;
    }
}
