package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/** Módulo SideDock: coherencia interna y madurez documental del levantamiento lógico. */
final class LogicalBusinessValidationPanel {

    private final LogicalBusinessViewModel viewModel;
    private final VBox content = LogicalBusinessUiNodes.panelRoot();

    LogicalBusinessValidationPanel(LogicalBusinessViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.currentProjectProperty().addListener((obs, oldValue, newValue) -> refresh());
        viewModel.selectionProperty().addListener((obs, oldValue, newValue) -> refresh());
        refresh();
    }

    Parent root() {
        return content;
    }

    private void refresh() {
        content.getChildren().setAll(LogicalBusinessUiNodes.subtitle("Coherencia interna del expediente"));
        if (viewModel.currentDocument() == null) {
            content.getChildren().add(LogicalBusinessUiNodes.text("No hay documento activo."));
            return;
        }
        renderScopeNote();
        renderSummary(viewModel.validationIssues(), viewModel.assessedMaturity());
        renderFocusedIssues(viewModel.validationIssues(), viewModel.selection());
    }

    private void renderScopeNote() {
        content.getChildren().add(LogicalBusinessUiNodes.inspectorCard(
                "Alcance de la validación",
                "Revisa coherencia interna del expediente activo: no aprueba el negocio real, no sincroniza proyectos y no genera artefactos.",
                "logical-business-inspector-info"));
    }

    private void renderSummary(List<LogicalBusinessValidationIssue> issues, LogicalBusinessMaturity maturity) {
        VBox card = LogicalBusinessUiNodes.inspectorCard("Resumen de coherencia",
                "Madurez documental: " + LogicalBusinessStatusFormatter.maturity(maturity.level()),
                "logical-business-inspector-summary");
        card.getChildren().add(LogicalBusinessUiNodes.compactMeta("Bloqueos: "
                + LogicalBusinessInspectorSupport.count(issues, LogicalBusinessIssueSeverity.BLOCKING)
                + " · Advertencias: "
                + LogicalBusinessInspectorSupport.count(issues, LogicalBusinessIssueSeverity.WARNING)
                + " · Información: "
                + LogicalBusinessInspectorSupport.count(issues, LogicalBusinessIssueSeverity.INFO)));
        maturity.blockers().stream().limit(3)
                .forEach(blocker -> card.getChildren().add(LogicalBusinessUiNodes.text("⛔ " + blocker)));
        maturity.nextSteps().stream().limit(3)
                .forEach(step -> card.getChildren().add(LogicalBusinessUiNodes.text("→ " + step)));
        card.getChildren().add(LogicalBusinessUiNodes.compactMeta(maturity.usableAsSource()
                ? "Uso como fuente: el expediente puede reutilizarse bajo revisión humana e IA."
                : "Uso como fuente: todavía requiere resolver hallazgos antes de reutilizarse con confianza."));
        content.getChildren().add(card);
    }

    private void renderFocusedIssues(List<LogicalBusinessValidationIssue> issues, LogicalBusinessSelection selection) {
        content.getChildren().add(LogicalBusinessUiNodes.subtitle(
                "Hallazgos del foco: " + LogicalBusinessInspectorSupport.focusTitle(selection)));
        List<LogicalBusinessValidationIssue> focused = LogicalBusinessInspectorSupport.issuesForFocus(issues, selection);
        List<LogicalBusinessValidationIssue> visible = focused.isEmpty() ? issues.stream().limit(8).toList() : focused;
        if (visible.isEmpty()) {
            content.getChildren().add(LogicalBusinessUiNodes.inspectorCard(
                    "Sin hallazgos visibles",
                    LogicalBusinessInspectorSupport.emptyFocusMessage(selection),
                    "logical-business-inspector-info"));
            return;
        }
        if (focused.isEmpty()) {
            content.getChildren().add(LogicalBusinessUiNodes.compactMeta(
                    "Mostrando resumen global porque el foco actual no tiene hallazgos propios."));
        }
        visible.forEach(issue -> content.getChildren().add(LogicalBusinessInspectorSupport.issueCard(issue)));
    }
}
