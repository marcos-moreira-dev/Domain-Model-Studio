package com.marcosmoreira.domainmodelstudio.domain.validation;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Validador de proyecto completo.
 *
 * <p>Une validación semántica con validación de layout y estilos. Mantiene una frontera
 * limpia: no sabe leer archivos, no dibuja JavaFX y no exporta.</p>
 */
public final class DiagramProjectValidator {

    private final DiagramModelValidator modelValidator;

    public DiagramProjectValidator() {
        this(new DiagramModelValidator());
    }

    public DiagramProjectValidator(DiagramModelValidator modelValidator) {
        this.modelValidator = Objects.requireNonNull(modelValidator, "El validador de modelo no puede ser null");
    }

    public ValidationResult validate(DiagramProject project) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        ValidationResult result = modelValidator.validate(project.model());
        DiagramElementIndex semanticIndex = DiagramElementIndex.from(project);

        Set<DiagramElementId> connectorIds = new LinkedHashSet<>();
        for (DiagramLayout layout : project.layouts().layoutsByNotation().values()) {
            result = result.merge(validateLayout(layout, semanticIndex, connectorIds));
        }

        result = result.merge(validateStyleSheet(project, semanticIndex, connectorIds));
        return result;
    }

    private ValidationResult validateLayout(
            DiagramLayout layout,
            DiagramElementIndex semanticIndex,
            Set<DiagramElementId> connectorIds
    ) {
        ValidationResult result = ValidationResult.ok();

        for (var node : layout.nodes()) {
            if (!semanticIndex.containsSemanticElement(node.elementId())) {
                result = result.plus(ValidationIssue.error(
                        ValidationCode.LAYOUT_NODE_REFERENCES_UNKNOWN_ELEMENT,
                        node.elementId(),
                        "El layout " + layout.notation() + " contiene un nodo para un elemento inexistente: "
                                + node.elementId()
                ));
            }
        }

        for (ConnectorLayout connector : layout.connectors()) {
            connectorIds.add(connector.connectorId());
            result = result.merge(validateConnector(layout, connector, semanticIndex));
        }

        return result;
    }

    private ValidationResult validateConnector(
            DiagramLayout layout,
            ConnectorLayout connector,
            DiagramElementIndex semanticIndex
    ) {
        ValidationResult result = ValidationResult.ok();

        if (!semanticIndex.containsSemanticElement(connector.sourceElementId())) {
            result = result.plus(ValidationIssue.error(
                    ValidationCode.LAYOUT_CONNECTOR_REFERENCES_UNKNOWN_SOURCE,
                    connector.connectorId(),
                    "El conector '" + connector.connectorId() + "' del layout " + layout.notation()
                            + " referencia un origen inexistente: " + connector.sourceElementId()
            ));
        }

        if (!semanticIndex.containsSemanticElement(connector.targetElementId())) {
            result = result.plus(ValidationIssue.error(
                    ValidationCode.LAYOUT_CONNECTOR_REFERENCES_UNKNOWN_TARGET,
                    connector.connectorId(),
                    "El conector '" + connector.connectorId() + "' del layout " + layout.notation()
                            + " referencia un destino inexistente: " + connector.targetElementId()
            ));
        }

        return result;
    }

    private ValidationResult validateStyleSheet(
            DiagramProject project,
            DiagramElementIndex semanticIndex,
            Set<DiagramElementId> connectorIds
    ) {
        ValidationResult result = ValidationResult.ok();
        Set<DiagramElementId> validStyleTargets = new LinkedHashSet<>(semanticIndex.semanticElementIds());
        validStyleTargets.addAll(connectorIds);

        for (DiagramElementId styledElementId : project.styleSheet().stylesByElementId().keySet()) {
            if (!validStyleTargets.contains(styledElementId)) {
                result = result.plus(ValidationIssue.warning(
                        ValidationCode.STYLE_REFERENCES_UNKNOWN_ELEMENT,
                        styledElementId,
                        "La hoja de estilos contiene un estilo para un elemento no encontrado en modelo ni conectores: "
                                + styledElementId
                ));
            }
        }

        return result;
    }
}
