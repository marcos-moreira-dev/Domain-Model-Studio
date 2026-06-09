package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** Convierte el levantamiento logico en un reporte PDF formal. */
final class LogicalBusinessPdfReportBuilder {

    SimplePdfDocument build(LogicalBusinessDocument document) {
        Objects.requireNonNull(document, "document");
        SimplePdfDocument pdf = new SimplePdfDocument("Levantamiento logico - " + document.projectName());
        renderCover(pdf, document);
        renderReadingGuide(pdf, document);
        renderSummary(pdf, document);
        renderMaturity(pdf, document);
        renderSections(pdf, document);
        renderLooseItems(pdf, document);
        renderEntities(pdf, document);
        renderPendingQuestions(pdf, document);
        renderNotes(pdf, document);
        return pdf;
    }

    private static void renderCover(SimplePdfDocument pdf, LogicalBusinessDocument document) {
        pdf.anchor(LogicalBusinessPdfReadingGuide.DEST_COVER);
        pdf.title("Levantamiento logico del negocio");
        pdf.table(List.of("Dato", "Valor"), List.of(
                List.of("Proyecto", value(document.projectName())),
                List.of("Version", value(document.version())),
                List.of("Fecha", document.documentDate().toString()),
                List.of("Estado", document.documentStatus().displayName()),
                List.of("Fuente principal", value(document.mainSource())),
                List.of("Madurez", label(document.maturity().level()))
        ), 0.32, 0.68);
        pdf.paragraph("Documento estructurado para revisar entrevistas, observaciones, reglas, acciones, entidades, "
                + "preguntas y trazas internas como fuente logica del negocio.");
    }

    private static void renderReadingGuide(SimplePdfDocument pdf, LogicalBusinessDocument document) {
        pdf.heading("Indice y guia de lectura", LogicalBusinessPdfReadingGuide.DEST_READING_GUIDE);
        pdf.callout("Como leer este PDF", "Los codigos usan prefijos canonicos: RN es regla, ACC es accion, "
                + "ENT es entidad candidata, ATR es atributo candidato, REL es relacion candidata y PEND es "
                + "pregunta pendiente. Las entidades, atributos y relaciones son candidatas: orientan el analisis, "
                + "pero no aprueban automaticamente tablas, columnas ni llaves fisicas.");
        pdf.linkedTable(List.of("Bloque del PDF", "Contenido", "Para que sirve"),
                LogicalBusinessPdfReadingGuide.documentIndexRows(document),
                0.25, 0.37, 0.38);
        pdf.heading("Indice por secciones del expediente");
        pdf.linkedTable(List.of("ID", "Seccion", "Elementos", "Codigos"),
                LogicalBusinessPdfReadingGuide.sectionIndexRows(document),
                0.14, 0.50, 0.14, 0.22);
        pdf.heading("Guia de codigos y categorias");
        pdf.table(List.of("Codigo", "Categoria", "Registros", "Significado"),
                LogicalBusinessPdfReadingGuide.codeGuideRows(document),
                0.12, 0.24, 0.13, 0.51);
    }

    private static void renderSummary(SimplePdfDocument pdf, LogicalBusinessDocument document) {
        pdf.heading("Resumen ejecutivo", LogicalBusinessPdfReadingGuide.DEST_SUMMARY);
        int attributes = document.entityCandidates().stream().mapToInt(entity -> entity.attributes().size()).sum();
        int relationships = document.entityCandidates().stream().mapToInt(entity -> entity.relationships().size()).sum();
        pdf.table(List.of("Indicador", "Valor"), List.of(
                List.of("Secciones", Integer.toString(document.sections().size())),
                List.of("Elementos logicos", Integer.toString(document.items().size())),
                List.of("Entidades candidatas", Integer.toString(document.entityCandidates().size())),
                List.of("Atributos candidatos", Integer.toString(attributes)),
                List.of("Relaciones candidatas", Integer.toString(relationships)),
                List.of("Preguntas pendientes", Integer.toString(document.pendingQuestions().size())),
                List.of("Bloqueos estructurales", Integer.toString(document.structuralIssues().size()))
        ), 0.58, 0.42);

        if (document.items().isEmpty() && document.entityCandidates().isEmpty()) {
            pdf.callout("Borrador", "El expediente aun no contiene elementos logicos ni entidades candidatas.");
        }
        if (!document.structuralIssues().isEmpty()) {
            pdf.heading("Calidad operativa");
            pdf.table(List.of("Severidad", "Referencia", "Hallazgo"),
                    document.structuralIssues().stream()
                            .map(LogicalBusinessPdfReportBuilder::issueRow)
                            .toList(),
                    0.18, 0.22, 0.60);
        }
    }

    private static List<String> issueRow(LogicalBusinessValidationIssue issue) {
        return List.of(label(issue.severity()), value(issue.targetId()), value(issue.message()));
    }

    private static void renderMaturity(SimplePdfDocument pdf, LogicalBusinessDocument document) {
        LogicalBusinessMaturity maturity = document.maturity();
        pdf.heading("Madurez del expediente", LogicalBusinessPdfReadingGuide.DEST_MATURITY);
        pdf.table(List.of("Dimension", "Detalle"), List.of(
                List.of("Nivel", label(maturity.level())),
                List.of("Fortalezas", join(maturity.strengths())),
                List.of("Bloqueos", join(maturity.blockers())),
                List.of("Siguientes pasos", join(maturity.nextSteps()))
        ), 0.32, 0.68);
    }

    private static void renderSections(SimplePdfDocument pdf, LogicalBusinessDocument document) {
        pdf.heading("Cuerpo del levantamiento", LogicalBusinessPdfReadingGuide.DEST_BODY);
        if (document.sections().isEmpty()) {
            pdf.paragraph("Sin secciones registradas.");
            return;
        }
        for (LogicalBusinessSection section : document.sections()) {
            pdf.heading(section.title(), LogicalBusinessPdfReadingGuide.sectionDestinationId(section));
            pdf.table(List.of("Dato", "Valor"), List.of(
                    List.of("ID", section.id()),
                    List.of("Estado", label(section.status())),
                    List.of("Proposito", value(section.purpose())),
                    List.of("Notas", value(section.notes())),
                    List.of("Elementos referenciados", join(section.itemIds()))
            ), 0.28, 0.72);
            if (section.itemIds().isEmpty()) {
                pdf.small("La seccion no referencia elementos logicos.");
            }
            for (String itemId : section.itemIds()) {
                document.itemById(itemId).ifPresent(item -> renderItem(pdf, item));
            }
        }
    }

    private static void renderLooseItems(SimplePdfDocument pdf, LogicalBusinessDocument document) {
        Set<String> sectionedIds = document.sections().stream()
                .flatMap(section -> section.itemIds().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        List<LogicalBusinessItem> looseItems = document.items().stream()
                .filter(item -> !sectionedIds.contains(item.id()))
                .toList();
        if (looseItems.isEmpty()) {
            return;
        }
        pdf.heading("Elementos logicos sin seccion");
        looseItems.forEach(item -> renderItem(pdf, item));
    }

    private static void renderItem(SimplePdfDocument pdf, LogicalBusinessItem item) {
        pdf.gap(4.0);
        pdf.heading(item.id() + " - " + item.title());
        pdf.table(List.of("Dato", "Valor"), detailRows(
                "Tipo", label(item.kind()),
                "Estado", label(item.status()),
                "Fuente", item.source(),
                "Descripcion", item.description(),
                "Lectura humana", item.humanReading(),
                "Contenido", item.content(),
                "Referencias internas", join(item.referenceIds())
        ), 0.28, 0.72);
    }

    private static void renderEntities(SimplePdfDocument pdf, LogicalBusinessDocument document) {
        pdf.heading("Entidades candidatas", LogicalBusinessPdfReadingGuide.DEST_ENTITIES);
        if (document.entityCandidates().isEmpty()) {
            pdf.paragraph("Sin entidades candidatas registradas.");
            return;
        }
        document.entityCandidates().stream()
                .sorted(Comparator.comparing(LogicalBusinessEntityCandidate::id))
                .forEach(entity -> renderEntity(pdf, entity));
    }

    private static void renderEntity(SimplePdfDocument pdf, LogicalBusinessEntityCandidate entity) {
        pdf.heading(entity.id() + " - " + entity.name());
        pdf.table(List.of("Dato", "Valor"), detailRows(
                "Estado", label(entity.status()),
                "Justificacion logica", entity.logicalJustification(),
                "Fuentes logicas", join(entity.sourceReferences()),
                "Reglas asociadas", join(entity.associatedRules()),
                "Invariantes asociadas", join(entity.associatedInvariants()),
                "Casos que crean", join(entity.createdByUseCases()),
                "Casos que modifican", join(entity.modifiedByUseCases()),
                "Casos que consultan", join(entity.queriedByUseCases()),
                "Riesgo de modelado", entity.modelingRisk()
        ), 0.30, 0.70);
        renderAttributes(pdf, entity);
        renderRelationships(pdf, entity);
    }

    private static void renderAttributes(SimplePdfDocument pdf, LogicalBusinessEntityCandidate entity) {
        if (entity.attributes().isEmpty()) {
            pdf.small("Sin atributos candidatos registrados para esta entidad.");
            return;
        }
        pdf.table(List.of("Atributo", "Tipo", "Calculado", "Fuentes", "Razon"),
                entity.attributes().stream()
                        .sorted(Comparator.comparing(LogicalBusinessAttributeCandidate::id))
                        .map(LogicalBusinessPdfReportBuilder::attributeRow)
                        .toList(),
                0.20, 0.14, 0.12, 0.22, 0.32);
        entity.attributes().stream()
                .filter(LogicalBusinessPdfReportBuilder::hasAttributeDetails)
                .sorted(Comparator.comparing(LogicalBusinessAttributeCandidate::id))
                .forEach(attribute -> renderAttributeDetails(pdf, attribute));
    }

    private static List<String> attributeRow(LogicalBusinessAttributeCandidate attribute) {
        return List.of(
                attribute.id() + " - " + attribute.name(),
                value(attribute.tentativeType()),
                yesNo(attribute.calculated()),
                join(attribute.sourceReferences()),
                value(attribute.reason())
        );
    }

    private static void renderAttributeDetails(SimplePdfDocument pdf, LogicalBusinessAttributeCandidate attribute) {
        pdf.gap(2.0);
        pdf.table(List.of("Detalle de atributo", "Valor"), detailRows(
                "ID", attribute.id(),
                "Entidad", attribute.entityId(),
                "Formula", attribute.formula(),
                "Riesgo si es incorrecto", attribute.riskIfWrong(),
                "Reglas relacionadas", join(attribute.relatedRules()),
                "Invariantes relacionadas", join(attribute.relatedInvariants())
        ), 0.34, 0.66);
    }

    private static boolean hasAttributeDetails(LogicalBusinessAttributeCandidate attribute) {
        return !attribute.formula().isBlank()
                || !attribute.riskIfWrong().isBlank()
                || !attribute.relatedRules().isEmpty()
                || !attribute.relatedInvariants().isEmpty();
    }

    private static void renderRelationships(SimplePdfDocument pdf, LogicalBusinessEntityCandidate entity) {
        if (entity.relationships().isEmpty()) {
            pdf.small("Sin relaciones candidatas registradas para esta entidad.");
            return;
        }
        pdf.table(List.of("Relacion", "Origen", "Destino", "Cardinalidad", "Justificacion"),
                entity.relationships().stream()
                        .sorted(Comparator.comparing(LogicalBusinessRelationshipCandidate::id))
                        .map(LogicalBusinessPdfReportBuilder::relationshipRow)
                        .toList(),
                0.18, 0.15, 0.15, 0.16, 0.36);
    }

    private static List<String> relationshipRow(LogicalBusinessRelationshipCandidate relationship) {
        return List.of(
                relationship.id() + " - " + relationship.name(),
                relationship.sourceEntityId(),
                relationship.targetEntityId(),
                value(relationship.cardinalityHint()),
                value(relationship.justification()) + " Fuentes: " + join(relationship.sourceReferences())
        );
    }

    private static void renderPendingQuestions(SimplePdfDocument pdf, LogicalBusinessDocument document) {
        pdf.heading("Preguntas pendientes", LogicalBusinessPdfReadingGuide.DEST_PENDING_QUESTIONS);
        if (document.pendingQuestions().isEmpty()) {
            pdf.paragraph("Sin preguntas pendientes registradas.");
            return;
        }
        pdf.table(List.of("ID", "Prioridad", "Estado", "Afecta", "Pregunta"),
                document.pendingQuestions().stream()
                        .sorted(Comparator.comparing(LogicalBusinessPendingQuestion::id))
                        .map(LogicalBusinessPdfReportBuilder::questionRow)
                        .toList(),
                0.15, 0.15, 0.15, 0.22, 0.33);
    }

    private static List<String> questionRow(LogicalBusinessPendingQuestion question) {
        return List.of(
                question.id(),
                label(question.priority()),
                label(question.status()),
                value(question.affects()),
                value(question.question())
        );
    }

    private static void renderNotes(SimplePdfDocument pdf, LogicalBusinessDocument document) {
        if (!document.notes().isBlank()) {
            pdf.heading("Notas generales", LogicalBusinessPdfReadingGuide.DEST_NOTES);
            pdf.paragraph(document.notes());
        }
    }

    private static List<List<String>> detailRows(String... pairs) {
        List<List<String>> rows = new ArrayList<>();
        for (int index = 0; index + 1 < pairs.length; index += 2) {
            rows.add(List.of(value(pairs[index]), value(pairs[index + 1])));
        }
        return rows;
    }

    private static String label(LogicalBusinessItemKind kind) {
        if (kind == null) {
            return "-";
        }
        return switch (kind) {
            case RULE -> "Regla";
            case PRECONDITION -> "Precondicion";
            case INVARIANT -> "Invariante";
            case POSTCONDITION -> "Postcondicion";
            case ACTION -> "Accion";
            case USE_CASE -> "Caso de uso";
            case MACRO_FLOW -> "Macroflujo";
            case FLOW -> "Flujo";
            case ENTITY -> "Entidad";
            case ATTRIBUTE -> "Atributo";
            case RELATIONSHIP -> "Relacion";
            case REPORT -> "Reporte";
            case CALCULATION -> "Calculo";
            case RISK -> "Riesgo";
            case SUPPORTING_ASSUMPTION -> "Supuesto";
            case PENDING_QUESTION -> "Pregunta pendiente";
            case ACTOR -> "Actor";
            case STATE -> "Estado";
            case CONCEPT -> "Concepto";
            case EVIDENCE -> "Evidencia";
        };
    }

    private static String label(Enum<?> value) {
        if (value == null) {
            return "-";
        }
        return switch (value.name()) {
            case "EMPTY" -> "Vacio";
            case "DRAFT" -> "Borrador";
            case "PARTIAL" -> "Parcial";
            case "COMPLETE" -> "Completo";
            case "VALIDATED" -> "Validado";
            case "WITH_DOUBTS" -> "Con dudas";
            case "BLOCKING" -> "Bloqueante";
            case "DERIVABLE" -> "Usable como fuente";
            case "DISCARDED" -> "Descartado";
            case "INITIAL" -> "Inicial";
            case "CONSISTENT" -> "Consistente";
            case "SOURCE_READY" -> "Usable como fuente";
            case "LOW" -> "Baja";
            case "MEDIUM" -> "Media";
            case "HIGH" -> "Alta";
            case "CRITICAL" -> "Critica";
            default -> fallbackLabel(value);
        };
    }

    private static String fallbackLabel(Enum<?> value) {
        String text = value.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        return text.substring(0, 1).toUpperCase(Locale.ROOT) + text.substring(1);
    }

    private static String join(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "-";
        }
        return values.stream().filter(value -> value != null && !value.isBlank())
                .map(String::strip)
                .collect(Collectors.joining(", "));
    }

    private static String value(String value) {
        return value == null || value.isBlank() ? "-" : value.strip();
    }

    private static String yesNo(boolean value) {
        return value ? "si" : "no";
    }
}
