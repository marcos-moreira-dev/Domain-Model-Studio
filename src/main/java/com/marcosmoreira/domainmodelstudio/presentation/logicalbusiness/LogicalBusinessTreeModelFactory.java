package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.collapsed;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.entityIds;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.expanded;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.group;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.itemIds;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.pendingQuestionIds;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.shortText;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.sorted;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.sortedEntities;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeItems.totalNodes;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.COMPLETE;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.EMPTY;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.TRACE;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.WARNING;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.markerForChildren;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.markerForDocument;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.markerForIssue;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.markerForMaturity;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.markerForStatus;
import javafx.scene.control.TreeItem;

/** Construye el TreeView del expediente lógico desde el documento canónico importado. */
final class LogicalBusinessTreeModelFactory {

    private LogicalBusinessTreeModelFactory() {
    }

    static TreeItem<LogicalBusinessTreeNode> build(
            LogicalBusinessDocument document,
            List<LogicalBusinessValidationIssue> issues
    ) {
        LogicalBusinessIssueIndex issueIndex = LogicalBusinessIssueIndex.from(issues);
        TreeItem<LogicalBusinessTreeNode> root = expanded(LogicalBusinessTreeNode.of(
                "Levantamiento lógico",
                markerForDocument(document, issueIndex),
                "Expediente documental estructurado del negocio.",
                LogicalBusinessSelection.document(),
                totalNodes(document)
        ));
        root.getChildren().add(group(
                "portada-contexto",
                "Portada y contexto",
                "Datos generales, fuente principal y notas de contexto.",
                List.of(document.projectName()),
                document.mainSource().isBlank() ? 1 : 2
        ));
        root.getChildren().add(canonicalSectionsGroup(document));
        root.getChildren().add(kindGroup(
                document,
                issueIndex,
                "lenguaje-negocio",
                "Lenguaje del negocio",
                "Actores, conceptos y evidencias detectadas.",
                LogicalBusinessItemKind.ACTOR,
                LogicalBusinessItemKind.CONCEPT,
                LogicalBusinessItemKind.EVIDENCE
        ));
        root.getChildren().add(kindGroup(
                document,
                issueIndex,
                "estados-datos",
                "Estados y datos observables",
                "Estados del negocio y datos observables para sostener decisiones lógicas.",
                LogicalBusinessItemKind.STATE
        ));
        root.getChildren().add(kindGroup(
                document,
                issueIndex,
                "reglas-condiciones",
                "Reglas y condiciones",
                "Reglas, precondiciones, invariantes y postcondiciones.",
                LogicalBusinessItemKind.RULE,
                LogicalBusinessItemKind.PRECONDITION,
                LogicalBusinessItemKind.INVARIANT,
                LogicalBusinessItemKind.POSTCONDITION
        ));
        root.getChildren().add(kindGroup(
                document,
                issueIndex,
                "acciones-flujos",
                "Acciones y flujos",
                "Acciones transformadoras, casos de uso, macroflujos y flujos.",
                LogicalBusinessItemKind.ACTION,
                LogicalBusinessItemKind.USE_CASE,
                LogicalBusinessItemKind.MACRO_FLOW,
                LogicalBusinessItemKind.FLOW
        ));
        root.getChildren().add(entitiesGroup(document, issueIndex));
        root.getChildren().add(kindGroup(
                document,
                issueIndex,
                "reportes-decisiones",
                "Reportes y decisiones",
                "Consultas, reportes, cálculos y decisiones que el negocio necesita sostener.",
                LogicalBusinessItemKind.REPORT,
                LogicalBusinessItemKind.CALCULATION
        ));
        root.getChildren().add(risksAndQuestionsGroup(document, issueIndex));
        root.getChildren().add(group(
                "trazas-internas",
                "Impacto y dependencias",
                "Trazas entrantes, salientes y referencias no resueltas del foco seleccionado.",
                List.of(),
                -1,
                TRACE
        ));
        root.getChildren().add(expanded(LogicalBusinessTreeNode.of(
                "Madurez y cierre",
                markerForMaturity(document),
                "Nivel de madurez, bloqueos y próximos pasos.",
                LogicalBusinessSelection.maturity(),
                -1
        )));
        return root;
    }

    private static TreeItem<LogicalBusinessTreeNode> canonicalSectionsGroup(LogicalBusinessDocument document) {
        TreeItem<LogicalBusinessTreeNode> group = collapsed(LogicalBusinessTreeNode.of(
                "Secciones canónicas",
                document.sections().isEmpty() ? EMPTY : COMPLETE,
                "Índice técnico de las secciones importadas desde la plantilla escrita en piedra.",
                LogicalBusinessSelection.group("secciones-canonicas"),
                document.sections().size()
        ));
        document.sections().stream()
                .sorted(Comparator.comparingInt(LogicalBusinessSectionOrdering::index)
                        .thenComparing(LogicalBusinessSection::id))
                .map(LogicalBusinessTreeModelFactory::sectionNode)
                .forEach(group.getChildren()::add);
        return group;
    }

    private static TreeItem<LogicalBusinessTreeNode> sectionNode(LogicalBusinessSection section) {
        return expanded(LogicalBusinessTreeNode.of(
                section.title(),
                markerForStatus(section.status(), null),
                section.purpose().isBlank() ? section.notes() : section.purpose(),
                LogicalBusinessSelection.section(section.id()),
                section.itemIds().size()
        ));
    }

    private static TreeItem<LogicalBusinessTreeNode> kindGroup(
            LogicalBusinessDocument document,
            LogicalBusinessIssueIndex issueIndex,
            String groupId,
            String label,
            String detail,
            LogicalBusinessItemKind... kinds
    ) {
        TreeItem<LogicalBusinessTreeNode> group = group(groupId, label, detail, List.of(), 0);
        int count = 0;
        for (LogicalBusinessItemKind kind : kinds) {
            List<LogicalBusinessItem> items = sorted(document.itemsByKind(kind));
            count += items.size();
            TreeItem<LogicalBusinessTreeNode> kindNode = group(
                    groupId + "." + kind.name().toLowerCase(),
                    LogicalBusinessStatusFormatter.itemKind(kind),
                    "Elementos de tipo " + LogicalBusinessStatusFormatter.itemKind(kind).toLowerCase() + ".",
                    itemIds(items),
                    items.size()
            );
            items.stream().map(item -> itemNode(item, issueIndex)).forEach(kindNode.getChildren()::add);
            group.getChildren().add(kindNode);
        }
        group.setValue(LogicalBusinessTreeNode.of(label, markerForChildren(group.getChildren()), detail,
                LogicalBusinessSelection.group(groupId), count));
        return group;
    }

    private static TreeItem<LogicalBusinessTreeNode> entitiesGroup(LogicalBusinessDocument document, LogicalBusinessIssueIndex issueIndex) {
        TreeItem<LogicalBusinessTreeNode> group = group(
                "entidades-candidatas",
                "Entidades candidatas",
                "Entidades, atributos y relaciones candidatas. Todavía no son tablas finales.",
                entityIds(document.entityCandidates()),
                document.entityCandidates().size()
        );
        sortedEntities(document.entityCandidates()).stream()
                .map(entity -> entityNode(entity, issueIndex))
                .forEach(group.getChildren()::add);
        if (document.entityCandidates().isEmpty()) {
            document.itemsByKind(LogicalBusinessItemKind.ENTITY).stream()
                    .map(item -> itemNode(item, issueIndex))
                    .forEach(group.getChildren()::add);
        }
        group.setValue(LogicalBusinessTreeNode.of(
                "Entidades candidatas",
                markerForChildren(group.getChildren()),
                "Entidades, atributos y relaciones candidatas. Todavía no son tablas finales.",
                LogicalBusinessSelection.group("entidades-candidatas"),
                group.getChildren().size()
        ));
        return group;
    }

    private static TreeItem<LogicalBusinessTreeNode> risksAndQuestionsGroup(
            LogicalBusinessDocument document,
            LogicalBusinessIssueIndex issueIndex
    ) {
        TreeItem<LogicalBusinessTreeNode> group = group(
                "riesgos-preguntas",
                "Riesgos y preguntas pendientes",
                "Riesgos, supuestos y dudas que pueden bloquear decisiones internas.",
                List.of(),
                0
        );
        List<LogicalBusinessItem> risks = sorted(document.itemsByKind(LogicalBusinessItemKind.RISK));
        TreeItem<LogicalBusinessTreeNode> risksNode = group(
                "riesgos-preguntas.riesgos",
                "Riesgos",
                "Riesgos detectados en el levantamiento.",
                itemIds(risks),
                risks.size()
        );
        risks.stream().map(item -> itemNode(item, issueIndex)).forEach(risksNode.getChildren()::add);
        group.getChildren().add(risksNode);

        List<LogicalBusinessItem> suppositions = sorted(document.itemsByKind(LogicalBusinessItemKind.SUPPORTING_ASSUMPTION));
        TreeItem<LogicalBusinessTreeNode> suppositionsNode = group(
                "riesgos-preguntas.supuestos",
                "Supuestos",
                "Supuestos detectados que requieren confirmación humana.",
                itemIds(suppositions),
                suppositions.size()
        );
        suppositions.stream().map(item -> itemNode(item, issueIndex)).forEach(suppositionsNode.getChildren()::add);
        group.getChildren().add(suppositionsNode);

        TreeItem<LogicalBusinessTreeNode> questionsNode = group(
                "riesgos-preguntas.preguntas",
                "Preguntas pendientes",
                "Dudas que todavía requieren validación humana.",
                pendingQuestionIds(document.pendingQuestions()),
                document.pendingQuestions().size()
        );
        document.pendingQuestions().stream()
                .sorted(Comparator.comparing(LogicalBusinessPendingQuestion::id))
                .map(question -> pendingQuestionNode(question, issueIndex))
                .forEach(questionsNode.getChildren()::add);
        group.getChildren().add(questionsNode);
        int count = risks.size() + suppositions.size() + document.pendingQuestions().size();
        group.setValue(LogicalBusinessTreeNode.of(
                "Riesgos y preguntas pendientes",
                markerForChildren(group.getChildren()),
                "Riesgos, supuestos y dudas que pueden bloquear decisiones internas.",
                LogicalBusinessSelection.group("riesgos-preguntas"),
                count
        ));
        return group;
    }

    private static TreeItem<LogicalBusinessTreeNode> entityNode(
            LogicalBusinessEntityCandidate entity,
            LogicalBusinessIssueIndex issueIndex
    ) {
        TreeItem<LogicalBusinessTreeNode> node = expanded(LogicalBusinessTreeNode.of(
                entity.id() + " — " + entity.name(),
                markerForStatus(entity.status(), issueIndex.severity(entity.id())),
                entity.logicalJustification(),
                LogicalBusinessSelection.entity(entity.id()),
                entity.attributes().size() + entity.relationships().size()
        ));
        if (!entity.attributes().isEmpty()) {
            TreeItem<LogicalBusinessTreeNode> attributes = group(
                    entity.id() + ".atributos",
                    "Atributos candidatos",
                    "Atributos candidatos visibles para " + entity.name() + ".",
                    entity.attributes().stream().map(LogicalBusinessAttributeCandidate::id).toList(),
                    entity.attributes().size()
            );
            entity.attributes().stream()
                    .sorted(Comparator.comparing(LogicalBusinessAttributeCandidate::id))
                    .map(attribute -> attributeNode(attribute, issueIndex))
                    .forEach(attributes.getChildren()::add);
            attributes.setValue(LogicalBusinessTreeNode.of("Atributos candidatos", markerForChildren(attributes.getChildren()),
                    "Atributos candidatos visibles para " + entity.name() + ".",
                    LogicalBusinessSelection.group(entity.id() + ".atributos"), entity.attributes().size()));
            node.getChildren().add(attributes);
        }
        if (!entity.relationships().isEmpty()) {
            TreeItem<LogicalBusinessTreeNode> relationships = group(
                    entity.id() + ".relaciones",
                    "Relaciones candidatas",
                    "Relaciones candidatas visibles para " + entity.name() + ".",
                    entity.relationships().stream().map(LogicalBusinessRelationshipCandidate::id).toList(),
                    entity.relationships().size()
            );
            entity.relationships().stream()
                    .sorted(Comparator.comparing(LogicalBusinessRelationshipCandidate::id))
                    .map(relationship -> relationshipNode(entity.id(), relationship, issueIndex))
                    .forEach(relationships.getChildren()::add);
            relationships.setValue(LogicalBusinessTreeNode.of("Relaciones candidatas", markerForChildren(relationships.getChildren()),
                    "Relaciones candidatas visibles para " + entity.name() + ".",
                    LogicalBusinessSelection.group(entity.id() + ".relaciones"), entity.relationships().size()));
            node.getChildren().add(relationships);
        }
        return node;
    }

    private static TreeItem<LogicalBusinessTreeNode> itemNode(LogicalBusinessItem item, LogicalBusinessIssueIndex issueIndex) {
        return expanded(LogicalBusinessTreeNode.of(
                item.id() + " — " + item.title(),
                markerForStatus(item.status(), issueIndex.severity(item.id())),
                item.description().isBlank() ? item.humanReading() : item.description(),
                LogicalBusinessSelection.item(item.id()),
                -1
        ));
    }

    private static TreeItem<LogicalBusinessTreeNode> attributeNode(
            LogicalBusinessAttributeCandidate attribute,
            LogicalBusinessIssueIndex issueIndex
    ) {
        return expanded(LogicalBusinessTreeNode.of(
                attribute.id() + " — " + attribute.name(),
                markerForIssue(issueIndex.severity(attribute.id())),
                attribute.reason(),
                LogicalBusinessSelection.attribute(attribute.entityId(), attribute.id()),
                -1
        ));
    }

    private static TreeItem<LogicalBusinessTreeNode> relationshipNode(
            String ownerEntityId,
            LogicalBusinessRelationshipCandidate relationship,
            LogicalBusinessIssueIndex issueIndex
    ) {
        return expanded(LogicalBusinessTreeNode.of(
                relationship.id() + " — " + relationship.name(),
                markerForIssue(issueIndex.severity(relationship.id())),
                relationship.justification(),
                LogicalBusinessSelection.relationship(ownerEntityId, relationship.id()),
                -1
        ));
    }

    private static TreeItem<LogicalBusinessTreeNode> pendingQuestionNode(
            LogicalBusinessPendingQuestion question,
            LogicalBusinessIssueIndex issueIndex
    ) {
        LogicalBusinessIssueSeverity severity = issueIndex.severity(question.id());
        String marker = severity != null ? markerForIssue(severity) : switch (question.priority()) {
            case CRITICAL, HIGH -> WARNING;
            case MEDIUM, LOW -> markerForStatus(question.status(), null);
        };
        return expanded(LogicalBusinessTreeNode.of(
                question.id() + " — " + shortText(question.question()),
                marker,
                question.affects(),
                LogicalBusinessSelection.pendingQuestion(question.id()),
                -1
        ));
    }

}
