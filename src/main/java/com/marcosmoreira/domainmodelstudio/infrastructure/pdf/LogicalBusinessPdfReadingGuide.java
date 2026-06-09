package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Microcopy estable para orientar la lectura del PDF de levantamiento logico. */
final class LogicalBusinessPdfReadingGuide {

    static final String DEST_COVER = "logical-business.cover";
    static final String DEST_READING_GUIDE = "logical-business.reading-guide";
    static final String DEST_SUMMARY = "logical-business.summary";
    static final String DEST_MATURITY = "logical-business.maturity";
    static final String DEST_BODY = "logical-business.body";
    static final String DEST_ENTITIES = "logical-business.entities";
    static final String DEST_PENDING_QUESTIONS = "logical-business.pending-questions";
    static final String DEST_NOTES = "logical-business.notes";

    private LogicalBusinessPdfReadingGuide() {
    }

    static List<PdfTableRow> documentIndexRows(LogicalBusinessDocument document) {
        PdfCounts counts = counts(document);
        return List.of(
                row("Portada", "Proyecto, version, fecha, estado, fuente y madurez.",
                        "Ubica el expediente y su estado editorial.", DEST_COVER),
                row("Resumen ejecutivo", counts.summary(),
                        "Dimensiona el volumen antes de leer el detalle.", DEST_SUMMARY),
                row("Indice y guia de lectura", "Bloques, secciones, codigos y categorias.",
                        "Permite saber que significa cada prefijo y donde mirar.", DEST_READING_GUIDE),
                row("Madurez del expediente", "Fortalezas, bloqueos y siguientes pasos.",
                        "Muestra si el expediente esta listo para usar como fuente.", DEST_MATURITY),
                row("Cuerpo del levantamiento", counts.sectionsAndItems(),
                        "Recorre las secciones logicas y sus elementos vinculados.", DEST_BODY),
                row("Entidades candidatas", counts.detailedCandidates(),
                        "Lista conceptos que podrian convertirse en modelo de datos, sin aprobar tablas finales.",
                        DEST_ENTITIES),
                row("Preguntas pendientes", counts.pendingQuestions(),
                        "Expone dudas que deben resolverse o aceptarse antes de cerrar decisiones.",
                        DEST_PENDING_QUESTIONS),
                row("Notas generales", document.notes().isBlank() ? "Sin notas generales." : "Incluye notas generales.",
                        "Conserva observaciones editoriales del expediente.",
                        document.notes().isBlank() ? "" : DEST_NOTES));
    }

    static List<PdfTableRow> sectionIndexRows(LogicalBusinessDocument document) {
        if (document.sections().isEmpty()) {
            return List.of(PdfTableRow.unlinked(List.of("-", "Sin secciones registradas.", "-", "-")));
        }
        return document.sections().stream()
                .map(LogicalBusinessPdfReadingGuide::sectionRow)
                .toList();
    }

    static String sectionDestinationId(LogicalBusinessSection section) {
        return "logical-business.section." + section.id();
    }

    static List<List<String>> codeGuideRows(LogicalBusinessDocument document) {
        return Arrays.stream(LogicalBusinessItemKind.values())
                .map(kind -> codeGuideRow(document, kind))
                .filter(row -> !row.get(2).equals("0"))
                .toList();
    }

    private static PdfTableRow sectionRow(LogicalBusinessSection section) {
        return PdfTableRow.linked(List.of(
                section.id(),
                section.title(),
                Integer.toString(section.itemIds().size()),
                prefixes(section.itemIds())),
                sectionDestinationId(section));
    }

    private static PdfTableRow row(String block, String content, String purpose, String destinationId) {
        return destinationId == null || destinationId.isBlank()
                ? PdfTableRow.unlinked(List.of(block, content, purpose))
                : PdfTableRow.linked(List.of(block, content, purpose), destinationId);
    }

    private static String prefixes(List<String> ids) {
        Set<String> prefixes = ids.stream()
                .map(LogicalBusinessPdfReadingGuide::prefix)
                .filter(prefix -> !prefix.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return prefixes.isEmpty() ? "-" : String.join(", ", prefixes);
    }

    private static String prefix(String id) {
        if (id == null) {
            return "";
        }
        int separator = id.indexOf('-');
        return separator <= 0 ? "" : id.substring(0, separator).strip().toUpperCase();
    }

    private static List<String> codeGuideRow(LogicalBusinessDocument document, LogicalBusinessItemKind kind) {
        return List.of(kind.prefix(), categoryName(kind), Integer.toString(count(document, kind)), explanation(kind));
    }

    private static int count(LogicalBusinessDocument document, LogicalBusinessItemKind kind) {
        int itemCount = document.itemsByKind(kind).size();
        return switch (kind) {
            case ENTITY -> Math.max(itemCount, document.entityCandidates().size());
            case ATTRIBUTE -> Math.max(itemCount, attributeCount(document));
            case RELATIONSHIP -> Math.max(itemCount, relationshipCount(document));
            case PENDING_QUESTION -> Math.max(itemCount, document.pendingQuestions().size());
            default -> itemCount;
        };
    }

    private static PdfCounts counts(LogicalBusinessDocument document) {
        return new PdfCounts(
                document.sections().size(),
                document.items().size(),
                document.entityCandidates().size(),
                attributeCount(document),
                relationshipCount(document),
                document.pendingQuestions().size());
    }

    private static int attributeCount(LogicalBusinessDocument document) {
        return document.entityCandidates().stream().mapToInt(entity -> entity.attributes().size()).sum();
    }

    private static int relationshipCount(LogicalBusinessDocument document) {
        return document.entityCandidates().stream().mapToInt(entity -> entity.relationships().size()).sum();
    }

    private static String categoryName(LogicalBusinessItemKind kind) {
        return switch (kind) {
            case RULE -> "Regla de negocio";
            case PRECONDITION -> "Precondicion";
            case INVARIANT -> "Invariante";
            case POSTCONDITION -> "Postcondicion";
            case ACTION -> "Accion transformadora";
            case USE_CASE -> "Caso de uso";
            case MACRO_FLOW -> "Macroflujo";
            case FLOW -> "Flujo operativo";
            case ENTITY -> "Entidad candidata";
            case ATTRIBUTE -> "Atributo candidato";
            case RELATIONSHIP -> "Relacion candidata";
            case REPORT -> "Reporte o consulta";
            case CALCULATION -> "Calculo";
            case RISK -> "Riesgo";
            case SUPPORTING_ASSUMPTION -> "Supuesto";
            case PENDING_QUESTION -> "Pregunta pendiente";
            case ACTOR -> "Actor o participante";
            case STATE -> "Estado observable";
            case CONCEPT -> "Concepto del dominio";
            case EVIDENCE -> "Evidencia";
        };
    }

    private static String explanation(LogicalBusinessItemKind kind) {
        return switch (kind) {
            case RULE -> "Restriccion, politica o criterio que gobierna el negocio.";
            case PRECONDITION -> "Condicion que debe cumplirse antes de ejecutar una accion.";
            case INVARIANT -> "Verdad que debe mantenerse durante cambios u operaciones.";
            case POSTCONDITION -> "Resultado verificable que queda despues de una accion.";
            case ACTION -> "Operacion que cambia estado, datos o situacion del negocio.";
            case USE_CASE -> "Interaccion observable entre un actor y el sistema o proceso.";
            case MACRO_FLOW -> "Gran agrupador de trabajo o recorrido principal del negocio.";
            case FLOW -> "Secuencia operativa mas concreta dentro de un macroflujo.";
            case ENTITY -> "Concepto del negocio que podria requerir seguimiento; no es tabla aprobada.";
            case ATTRIBUTE -> "Dato candidato de una entidad; no es columna fisica definitiva.";
            case RELATIONSHIP -> "Vinculo candidato entre entidades; no aprueba llaves foraneas.";
            case REPORT -> "Salida de informacion, consulta, listado o reporte esperado.";
            case CALCULATION -> "Formula, computo o lectura cuantitativa que requiere regla clara.";
            case RISK -> "Punto de incertidumbre, inconsistencia o decision peligrosa.";
            case SUPPORTING_ASSUMPTION -> "Supuesto de trabajo que aun debe confirmarse o descartarse.";
            case PENDING_QUESTION -> "Duda abierta que puede bloquear validacion o diseno definitivo.";
            case ACTOR -> "Rol, area, persona o sistema participante.";
            case STATE -> "Situacion observable por la que pasa un concepto del negocio.";
            case CONCEPT -> "Termino estable del dominio que conviene nombrar de forma canonica.";
            case EVIDENCE -> "Documento, observacion o fuente que sustenta una afirmacion.";
        };
    }

    private record PdfCounts(
            int sections,
            int items,
            int entities,
            int attributes,
            int relationships,
            int questions
    ) {
        String summary() {
            return sections + " secciones, " + items + " elementos, " + entities + " entidades, "
                    + attributes + " atributos, " + relationships + " relaciones, " + questions + " preguntas.";
        }

        String sectionsAndItems() {
            return sections + " secciones y " + items + " elementos logicos.";
        }

        String detailedCandidates() {
            return entities + " entidades detalladas, " + attributes + " atributos detallados y "
                    + relationships + " relaciones detalladas.";
        }

        String pendingQuestions() {
            return questions + " preguntas pendientes.";
        }
    }
}
