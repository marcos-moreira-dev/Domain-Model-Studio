package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Fachada interna para producir borradores Markdown compatibles desde un levantamiento lógico.
 *
 * <p>Esta infraestructura queda fuera del SideDock y de la toolbar del Levantamiento lógico. Sus
 * salidas son insumos revisables: no importan automáticamente, no guardan proyectos, no dibujan
 * diagramas y no sustituyen la decisión del usuario ni el trabajo posterior de la IA.</p>
 *
 * <p>El nombre del paquete y de la clase se conserva por compatibilidad con pruebas y código legado,
 * pero el contrato vigente es "borradores compatibles", no generación automática de proyectos.</p>
 */
public final class LogicalBusinessDerivationService {

    private final Map<LogicalBusinessDerivationTarget, LogicalBusinessDerivationWriter> writers;

    public LogicalBusinessDerivationService() {
        this(List.of(
                new LogicalBusinessFreeGraphDraftWriter(),
                new LogicalBusinessGraphDraftWriter(),
                new LogicalBusinessConceptualModelDraftWriter(),
                new LogicalBusinessDataDictionaryDraftWriter(),
                new LogicalBusinessUseCaseDraftWriter(),
                new LogicalBusinessBpmnDraftWriter(),
                new LogicalBusinessRolesPermissionsDraftWriter(),
                new LogicalBusinessScreenFlowDraftWriter(),
                new LogicalBusinessWireframeDraftWriter()
        ));
    }

    LogicalBusinessDerivationService(List<LogicalBusinessDerivationWriter> contributors) {
        EnumMap<LogicalBusinessDerivationTarget, LogicalBusinessDerivationWriter> indexed =
                new EnumMap<>(LogicalBusinessDerivationTarget.class);
        contributors.forEach(writer -> indexed.put(writer.target(), writer));
        this.writers = Map.copyOf(indexed);
    }

    public List<LogicalBusinessDerivationTarget> availableTargets() {
        return List.copyOf(writers.keySet());
    }

    /**
     * Produce todos los borradores compatibles soportados para el documento recibido.
     *
     * @param document fuente lógica canónica de reglas, acciones, entidades candidatas y pendientes.
     * @return borradores por destino, listos para revisión humana antes de cualquier uso posterior.
     */
    public List<LogicalBusinessDerivationDraft> compatibleDrafts(LogicalBusinessDocument document) {
        LogicalBusinessDerivationContext context = new LogicalBusinessDerivationContext(document);
        return writers.values().stream()
                .map(writer -> writer.write(context))
                .toList();
    }

    public LogicalBusinessDerivationDraft compatibleDraft(LogicalBusinessDocument document, LogicalBusinessDerivationTarget target) {
        Objects.requireNonNull(target, "target");
        LogicalBusinessDerivationWriter writer = writers.get(target);
        if (writer == null) {
            throw new IllegalArgumentException("No existe escritor de borrador compatible para " + target);
        }
        return writer.write(new LogicalBusinessDerivationContext(document));
    }

    /**
     * @deprecated usar {@link #compatibleDrafts(LogicalBusinessDocument)}. El nombre se conserva
     * solo para compatibilidad con integraciones legadas.
     */
    @Deprecated(forRemoval = false)
    public List<LogicalBusinessDerivationDraft> deriveAll(LogicalBusinessDocument document) {
        return compatibleDrafts(document);
    }

    /**
     * @deprecated usar {@link #compatibleDraft(LogicalBusinessDocument, LogicalBusinessDerivationTarget)}.
     * El nombre se conserva solo para compatibilidad con integraciones legadas.
     */
    @Deprecated(forRemoval = false)
    public LogicalBusinessDerivationDraft derive(LogicalBusinessDocument document, LogicalBusinessDerivationTarget target) {
        return compatibleDraft(document, target);
    }
}
