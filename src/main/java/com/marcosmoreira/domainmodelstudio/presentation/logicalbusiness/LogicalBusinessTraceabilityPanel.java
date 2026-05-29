package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.application.logicalbusiness.LogicalBusinessTraceLink;
import com.marcosmoreira.domainmodelstudio.application.logicalbusiness.LogicalBusinessTraceabilityReport;
import java.util.List;
import javafx.scene.Parent;
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
        content.getChildren().setAll(LogicalBusinessUiNodes.subtitle("Trazas internas del expediente"));
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
                "Selecciona una regla, acción, entidad, atributo, relación o pregunta para ver trazas del foco.",
                "logical-business-inspector-info");
        card.getChildren().add(LogicalBusinessUiNodes.compactMeta(
                "Trazas detectadas: " + viewModel.traceabilityLinks().size()));
        content.getChildren().add(card);
        content.getChildren().add(LogicalBusinessUiNodes.inspectorCard(
                "Alcance",
                "Las trazas explican relaciones dentro del mismo levantamiento. No sincronizan otros proyectos, no generan artefactos y no reemplazan revisión humana.",
                "logical-business-inspector-summary"));
    }

    private void renderReport(LogicalBusinessTraceabilityReport report) {
        content.getChildren().add(LogicalBusinessUiNodes.inspectorCard(
                "Foco: " + report.focusId(),
                report.empty() ? "Este elemento todavía no tiene trazas registradas." : "Muestra qué sustenta este elemento y qué otros elementos dependen de él dentro del expediente.",
                "logical-business-inspector-summary"));
        renderLinks("Trazas que usa", report.outgoing());
        renderLinks("Elementos sustentados por este foco", report.incoming());
        renderUnresolved(report.unresolvedReferences());
    }

    private void renderLinks(String title, List<LogicalBusinessTraceLink> links) {
        VBox card = LogicalBusinessUiNodes.inspectorCard(title,
                links.isEmpty() ? "Sin registros para esta sección." : "Trazas directas del elemento seleccionado.");
        links.stream().limit(8).forEach(link -> card.getChildren().add(LogicalBusinessUiNodes.text(
                link.sourceId() + " —" + LogicalBusinessTraceRelationLabels.labelFor(link.relation()) + "→ " + link.targetId())));
        content.getChildren().add(card);
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
