package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.application.logicalbusiness.LogicalBusinessTraceLink;
import com.marcosmoreira.domainmodelstudio.application.logicalbusiness.LogicalBusinessTraceabilityReport;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/** Módulo SideDock: trazas internas directas, inversas y referencias no resueltas. */
final class LogicalBusinessTraceabilityPanel {

    private final LogicalBusinessViewModel viewModel;
    private final VBox content = LogicalBusinessUiNodes.panelRoot();

    LogicalBusinessTraceabilityPanel(LogicalBusinessViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.currentProjectProperty().addListener((obs, oldValue, newValue) -> refresh());
        viewModel.selectionProperty().addListener((obs, oldValue, newValue) -> refresh());
        refresh();
    }

    Parent root() {
        return content;
    }

    private void refresh() {
        content.getChildren().setAll(LogicalBusinessUiNodes.subtitle("Impacto y dependencias del expediente"));
        if (viewModel.currentDocument() == null) {
            content.getChildren().add(LogicalBusinessUiNodes.text("No hay documento activo."));
            return;
        }
        if (viewModel.selectedTraceabilityId().isBlank()) {
            renderGlobalOrientation();
            return;
        }
        renderReport(viewModel.traceabilityReport());
    }

    private void renderGlobalOrientation() {
        VBox card = LogicalBusinessUiNodes.inspectorCard(
                "Resumen global",
                "Selecciona una regla, acción, entidad, atributo, relación o pregunta para ver de qué depende y qué elementos impacta.",
                "logical-business-inspector-info");
        card.getChildren().add(LogicalBusinessUiNodes.compactMeta(
                "Dependencias detectadas: " + viewModel.traceabilityLinks().size()));
        content.getChildren().add(card);
        content.getChildren().add(LogicalBusinessUiNodes.inspectorCard(
                "Alcance",
                "Las dependencias explican relaciones dentro del mismo levantamiento. No sincronizan otros proyectos, no generan artefactos y no reemplazan revisión humana.",
                "logical-business-inspector-summary"));
    }

    private void renderReport(LogicalBusinessTraceabilityReport report) {
        content.getChildren().add(LogicalBusinessUiNodes.inspectorCard(
                "Foco: " + report.focusId(),
                report.empty() ? "Este elemento todavía no tiene dependencias registradas." : "Muestra de qué depende este elemento y qué otros elementos pueden verse afectados por él.",
                "logical-business-inspector-summary"));
        renderLinks("Depende de", report.outgoing(), true);
        renderLinks("Impacta a", report.incoming(), false);
        renderUnresolved(report.unresolvedReferences());
    }

    private void renderLinks(String title, List<LogicalBusinessTraceLink> links, boolean outgoing) {
        VBox body = LogicalBusinessDisclosure.body();
        if (links.isEmpty()) {
            body.getChildren().add(LogicalBusinessUiNodes.text("Sin registros para esta sección."));
        } else {
            links.stream().limit(8)
                    .map(link -> traceButton(link, outgoing))
                    .forEach(body.getChildren()::add);
        }
        content.getChildren().add(LogicalBusinessDisclosure.section(
                title,
                links.size() + " enlaces",
                outgoing ? "Elementos que el foco usa como sustento." : "Elementos que dependen del foco seleccionado.",
                body,
                !links.isEmpty(),
                null,
                "logical-business-inspector-card"));
    }

    private Button traceButton(LogicalBusinessTraceLink link, boolean outgoing) {
        String targetId = outgoing ? link.targetId() : link.sourceId();
        Button button = new Button(link.sourceId()
                + " —" + LogicalBusinessTraceRelationLabels.labelFor(link.relation()) + "→ "
                + link.targetId());
        button.setMaxWidth(Double.MAX_VALUE);
        button.setWrapText(true);
        button.getStyleClass().addAll("logical-business-side-button", "logical-business-list-button");
        button.setOnAction(event -> viewModel.selectReference(targetId));
        return button;
    }

    private void renderUnresolved(List<String> unresolvedReferences) {
        if (unresolvedReferences.isEmpty()) {
            return;
        }
        VBox card = LogicalBusinessUiNodes.inspectorCard("Referencias no resueltas",
                "Revisar para mantener coherencia interna antes de cerrar o reutilizar el expediente como fuente.", "logical-business-inspector-warning");
        unresolvedReferences.stream().limit(8)
                .forEach(unresolved -> card.getChildren().add(LogicalBusinessUiNodes.text("⚠ " + unresolved)));
        content.getChildren().add(card);
    }
}
