package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Construye trazas internas navegables entre reglas, acciones, entidades, atributos y preguntas.
 *
 * <p>Este servicio traduce referencias textuales y campos estructurados en enlaces explícitos
 * del mismo expediente. Sirve para explicar por qué existe un elemento, qué regla lo
 * respalda o qué pregunta bloquea una decisión lógica sin mezclar esa lectura con
 * sincronización externa, generación automática ni infraestructura de UI.</p>
 */
public final class LogicalBusinessTraceabilityService {

    /**
     * Crea un reporte centrado en un elemento lógico.
     *
     * @param document fuente estructurada de trazas internas.
     * @param focusId identificador del foco; puede estar vacío para consulta general.
     * @return enlaces salientes, entrantes y referencias no resueltas del foco.
     */
    public LogicalBusinessTraceabilityReport reportFor(LogicalBusinessDocument document, String focusId) {
        Objects.requireNonNull(document, "document");
        String focus = clean(focusId);
        List<LogicalBusinessTraceLink> links = allLinks(document);
        LogicalBusinessReferenceIndex index = new LogicalBusinessReferenceIndex(document);
        return new LogicalBusinessTraceabilityReport(
                focus,
                links.stream().filter(link -> link.sourceId().equals(focus)).toList(),
                links.stream().filter(link -> link.targetId().equals(focus)).toList(),
                unresolvedFor(document, index, focus));
    }

    public List<LogicalBusinessTraceLink> allLinks(LogicalBusinessDocument document) {
        Objects.requireNonNull(document, "document");
        List<LogicalBusinessTraceLink> links = new ArrayList<>();
        document.items().forEach(item -> addItemLinks(item, links));
        document.pendingQuestions().forEach(question -> addQuestionLink(question, links));
        document.entityCandidates().forEach(entity -> addEntityLinks(entity, links));
        return links.stream().distinct().toList();
    }

    private void addItemLinks(LogicalBusinessItem item, List<LogicalBusinessTraceLink> links) {
        item.referenceIds().forEach(reference -> links.add(new LogicalBusinessTraceLink(
                item.id(), "referencia", reference, item.id() + " referencia " + reference)));
    }

    private void addQuestionLink(LogicalBusinessPendingQuestion question, List<LogicalBusinessTraceLink> links) {
        idsIn(question.affects()).forEach(reference -> links.add(new LogicalBusinessTraceLink(
                question.id(), "bloquea", reference, question.id() + " afecta o bloquea " + reference)));
    }

    private void addEntityLinks(LogicalBusinessEntityCandidate entity, List<LogicalBusinessTraceLink> links) {
        entity.sourceReferences().forEach(reference -> add(links, entity.id(), "deriva_de", reference));
        entity.associatedRules().forEach(reference -> add(links, entity.id(), "regla_asociada", reference));
        entity.associatedInvariants().forEach(reference -> add(links, entity.id(), "invariante_asociada", reference));
        entity.createdByUseCases().forEach(reference -> add(links, reference, "crea", entity.id()));
        entity.modifiedByUseCases().forEach(reference -> add(links, reference, "modifica", entity.id()));
        entity.queriedByUseCases().forEach(reference -> add(links, reference, "consulta", entity.id()));
        entity.attributes().forEach(attribute -> addAttributeLinks(attribute, links));
        entity.relationships().forEach(relationship -> addRelationshipLinks(relationship, links));
    }

    private void addAttributeLinks(LogicalBusinessAttributeCandidate attribute, List<LogicalBusinessTraceLink> links) {
        add(links, attribute.entityId(), "tiene_atributo", attribute.id());
        attribute.sourceReferences().forEach(reference -> add(links, attribute.id(), "deriva_de", reference));
        attribute.relatedRules().forEach(reference -> add(links, attribute.id(), "regla_asociada", reference));
        attribute.relatedInvariants().forEach(reference -> add(links, attribute.id(), "invariante_asociada", reference));
    }

    private void addRelationshipLinks(LogicalBusinessRelationshipCandidate relationship, List<LogicalBusinessTraceLink> links) {
        add(links, relationship.sourceEntityId(), "relaciona", relationship.targetEntityId());
        add(links, relationship.id(), "origen", relationship.sourceEntityId());
        add(links, relationship.id(), "destino", relationship.targetEntityId());
        relationship.sourceReferences().forEach(reference -> add(links, relationship.id(), "deriva_de", reference));
    }

    private List<String> unresolvedFor(LogicalBusinessDocument document, LogicalBusinessReferenceIndex index, String focusId) {
        Set<String> unresolved = new LinkedHashSet<>();
        for (LogicalBusinessTraceLink link : allLinks(document)) {
            if (!focusId.isBlank() && !link.touches(focusId)) {
                continue;
            }
            if (!index.known(link.targetId())) {
                unresolved.add(link.sourceId() + " → " + link.targetId());
            }
        }
        return List.copyOf(unresolved);
    }

    private void add(List<LogicalBusinessTraceLink> links, String source, String relation, String target) {
        links.add(new LogicalBusinessTraceLink(source, relation, target, source + " " + relation + " " + target));
    }

    private List<String> idsIn(String text) {
        List<String> ids = new ArrayList<>();
        for (String token : clean(text).split("[^A-Za-z0-9-]+")) {
            if (token.matches("[A-Z]+-\\d+")) {
                ids.add(token);
            }
        }
        return ids;
    }

    private String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
