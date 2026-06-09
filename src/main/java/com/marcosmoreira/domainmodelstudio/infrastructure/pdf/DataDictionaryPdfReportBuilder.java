package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/** Convierte el modelo del diccionario de datos en un reporte PDF formal. */
final class DataDictionaryPdfReportBuilder {

    private static final String DEST_COVER = "data-dictionary.cover";
    private static final String DEST_INDEX = "data-dictionary.index";
    private static final String DEST_SUMMARY = "data-dictionary.summary";
    private static final String DEST_ENTITY_OVERVIEW = "data-dictionary.entity-overview";
    private static final String DEST_ENTITIES = "data-dictionary.entities";
    private static final String DEST_NOTES = "data-dictionary.notes";

    SimplePdfDocument build(DataDictionaryDocument document) {
        Objects.requireNonNull(document, "document");
        SimplePdfDocument pdf = new SimplePdfDocument("Diccionario de datos - " + document.projectName());
        renderCover(pdf, document);
        renderIndex(pdf, document);
        renderSummary(pdf, document);
        renderEntityOverview(pdf, document);
        renderEntities(pdf, document);
        renderNotes(pdf, document);
        return pdf;
    }

    private static void renderCover(SimplePdfDocument pdf, DataDictionaryDocument document) {
        pdf.anchor(DEST_COVER);
        pdf.title("Diccionario de datos");
        pdf.paragraph("Proyecto: " + document.projectName());
        if (document.hasLogoReference()) {
            renderLogo(pdf, document.logoReference());
        }
        pdf.table(List.of("Dato", "Valor"), List.of(
                List.of("Proyecto", document.projectName()),
                List.of("Cliente", optional(document.clientName())),
                List.of("Organización", optional(document.organizationName())),
                List.of("Autor", optional(document.author())),
                List.of("Versión", document.version()),
                List.of("Fecha", document.documentDate().toString()),
                List.of("Estado", label(document.status()))
        ), 0.35, 0.65);
        pdf.heading("Introducción");
        if (document.hasIntroduction()) {
            pdf.paragraph(document.introduction());
        } else {
            pdf.paragraph("Este documento describe entidades, campos, tipos de datos, restricciones, visibilidad, "
                    + "reglas y observaciones del sistema. Sirve como referencia de análisis, desarrollo, revisión "
                    + "y mantenimiento del modelo de datos.");
        }
    }

    private static void renderLogo(SimplePdfDocument pdf, String logoReference) {
        Path logoPath = Path.of(logoReference.strip());
        // El PDF es un entregable para cliente. Si el logo no existe o no es legible,
        // se omite en silencio: los diagnósticos operativos deben mostrarse en la UI,
        // no dentro del documento final.
        pdf.image(logoPath, 180.0, 90.0);
    }

    private static void renderIndex(SimplePdfDocument pdf, DataDictionaryDocument document) {
        pdf.heading("Indice del documento", DEST_INDEX);
        pdf.darkRedSmall("(Haga clic en algún tema del índice para ver más sobre el enlace de interés)");
        pdf.linkedIndex(indexEntries(document));
    }

    private static List<PdfIndexEntry> indexEntries(DataDictionaryDocument document) {
        ArrayList<PdfIndexEntry> entries = new ArrayList<>();
        entries.add(PdfIndexEntry.linked("Resumen ejecutivo", DEST_SUMMARY));
        if (!document.entities().isEmpty()) {
            entries.add(PdfIndexEntry.linked("Tabla general de entidades", DEST_ENTITY_OVERVIEW));
            entries.add(PdfIndexEntry.linked("Entidades y campos", DEST_ENTITIES));
            document.entities().stream()
                    .sorted(Comparator.comparing(DataDictionaryEntity::displayName))
                    .map(entity -> PdfIndexEntry.linked(
                            entity.displayName() + " (" + entity.technicalName() + ")",
                            entityDestination(entity),
                            1))
                    .forEach(entries::add);
        }
        if (!document.notes().isBlank()) {
            entries.add(PdfIndexEntry.linked("Observaciones generales", DEST_NOTES));
        }
        return entries;
    }

    private static void renderSummary(SimplePdfDocument pdf, DataDictionaryDocument document) {
        pdf.heading("Resumen ejecutivo", DEST_SUMMARY);
        pdf.table(List.of("Indicador", "Valor"), List.of(
                List.of("Entidades documentadas", Integer.toString(document.entityCount())),
                List.of("Campos documentados", Integer.toString(document.fieldCount())),
                List.of("Estado del documento", label(document.status())),
                List.of("Entidades con clave primaria", Long.toString(document.entities().stream()
                        .filter(DataDictionaryEntity::hasPrimaryKey).count()))
        ), 0.55, 0.45);
        if (document.isEmpty()) {
            pdf.callout("Borrador", "El diccionario aún no contiene entidades. Puede exportarse como evidencia trazable.");
        }
    }

    private static void renderEntityOverview(SimplePdfDocument pdf, DataDictionaryDocument document) {
        if (document.entities().isEmpty()) {
            return;
        }
        pdf.heading("Tabla general de entidades", DEST_ENTITY_OVERVIEW);
        pdf.table(List.of("Entidad", "Técnico", "Tipo", "Campos", "Módulo", "Estado"),
                document.entities().stream()
                        .sorted(Comparator.comparing(DataDictionaryEntity::displayName))
                        .map(entity -> List.of(
                                entity.displayName(),
                                entity.technicalName(),
                                label(entity.kind()),
                                Integer.toString(entity.fieldCount()),
                                optional(entity.moduleName()),
                                label(entity.status())))
                        .toList(),
                0.22, 0.20, 0.16, 0.10, 0.18, 0.14);
    }

    private static void renderEntities(SimplePdfDocument pdf, DataDictionaryDocument document) {
        pdf.heading("Entidades y campos", DEST_ENTITIES);
        document.entities().stream()
                .sorted(Comparator.comparing(DataDictionaryEntity::displayName))
                .forEach(entity -> renderEntity(pdf, entity));
    }

    private static void renderEntity(SimplePdfDocument pdf, DataDictionaryEntity entity) {
        pdf.gap(10.0);
        pdf.heading(entity.displayName() + " (" + entity.technicalName() + ")", entityDestination(entity));
        pdf.table(List.of("Propiedad", "Valor"), List.of(
                List.of("Tipo", label(entity.kind())),
                List.of("Módulo", optional(entity.moduleName())),
                List.of("Origen", entity.origin()),
                List.of("Estado", label(entity.status())),
                List.of("Descripción", optional(entity.description()))
        ), 0.32, 0.68);
        if (entity.fields().isEmpty()) {
            pdf.paragraph("Sin campos documentados todavía.");
        } else {
            pdf.table(List.of("Campo", "Técnico", "Tipo", "Restricciones", "Descripción"),
                    entity.fields().stream().map(DataDictionaryPdfReportBuilder::fieldRow).toList(),
                    0.20, 0.20, 0.17, 0.20, 0.23);
            entity.fields().stream()
                    .filter(DataDictionaryPdfReportBuilder::hasFieldDetails)
                    .forEach(field -> renderFieldDetails(pdf, field));
        }
        if (!entity.notes().isBlank()) {
            pdf.callout("Notas de entidad", entity.notes());
        }
    }

    private static List<String> fieldRow(DataDictionaryField field) {
        return List.of(
                field.displayName(),
                field.technicalName(),
                label(field.logicalType()),
                constraints(field),
                optional(field.description())
        );
    }

    private static void renderFieldDetails(SimplePdfDocument pdf, DataDictionaryField field) {
        pdf.gap(4.0);
        pdf.heading("Detalle de campo: " + field.displayName());
        java.util.ArrayList<List<String>> rows = new java.util.ArrayList<>();
        addDetail(rows, "Tipo físico sugerido", field.physicalTypeSuggestion());
        addDetail(rows, "Referencia", field.foreignKeyReference());
        addDetail(rows, "Valor por defecto", field.defaultValue());
        addDetail(rows, "Formato esperado", field.expectedFormat());
        addDetail(rows, "Visible en", visibility(field));
        addDetail(rows, "Editable por usuario", yesNo(field.userEditable()));
        addDetail(rows, "Descripción", field.description());
        addDetail(rows, "Regla de negocio", field.businessRule());
        addDetail(rows, "Validación", field.validationRule());
        addDetail(rows, "Ejemplo", field.example());
        addDetail(rows, "Notas", field.notes());
        if (!rows.isEmpty()) {
            pdf.table(List.of("Dato", "Valor"), rows, 0.35, 0.65);
        }
    }

    private static void addDetail(List<List<String>> rows, String label, String value) {
        if (value != null && !value.isBlank()) {
            rows.add(List.of(label, value.strip()));
        }
    }

    private static boolean hasFieldDetails(DataDictionaryField field) {
        return field.hasPhysicalTypeSuggestion()
                || !field.foreignKeyReference().isBlank()
                || !field.defaultValue().isBlank()
                || !field.expectedFormat().isBlank()
                || !field.description().isBlank()
                || field.hasBusinessRule()
                || field.hasValidationRule()
                || !field.example().isBlank()
                || !field.notes().isBlank();
    }

    private static void renderNotes(SimplePdfDocument pdf, DataDictionaryDocument document) {
        if (!document.notes().isBlank()) {
            pdf.heading("Observaciones generales", DEST_NOTES);
            pdf.paragraph(document.notes());
        }
    }

    private static String entityDestination(DataDictionaryEntity entity) {
        return "data-dictionary.entity." + stableId(entity.id());
    }

    private static String stableId(String value) {
        String normalized = value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
        String stable = normalized.replaceAll("[^a-z0-9._-]+", "-");
        return stable.isBlank() ? "entity" : stable;
    }

    private static String constraints(DataDictionaryField field) {
        if (field.constraints().isEmpty()) {
            return "sin restricciones registradas";
        }
        return field.constraints().stream()
                .sorted(Comparator.comparing(Enum::name))
                .map(DataDictionaryPdfReportBuilder::label)
                .collect(Collectors.joining(", "));
    }

    private static String visibility(DataDictionaryField field) {
        if (field.visibility().isEmpty()) {
            return "no definido";
        }
        return field.visibility().stream()
                .sorted(Comparator.comparing(Enum::name))
                .map(DataDictionaryPdfReportBuilder::label)
                .collect(Collectors.joining(", "));
    }

    private static String label(Enum<?> value) {
        if (value == null) {
            return "-";
        }
        return switch (value.name()) {
            case "DRAFT" -> "Borrador";
            case "REVIEWED" -> "Revisado";
            case "APPROVED" -> "Aprobado";
            case "IMPLEMENTED" -> "En uso";
            case "OBSOLETE" -> "Obsoleto";
            case "MAIN" -> "Principal";
            case "CATALOG" -> "Catálogo";
            case "TRANSACTIONAL" -> "Transaccional";
            case "ASSOCIATIVE" -> "Asociativa";
            case "AUDIT" -> "Auditoría";
            case "SUPPORT" -> "Soporte";
            case "REQUIRED" -> "Obligatorio";
            case "UNIQUE" -> "Único";
            case "PRIMARY_KEY" -> "Clave primaria";
            case "FOREIGN_KEY" -> "Clave foránea";
            case "DERIVED" -> "Derivado";
            case "READ_ONLY" -> "Solo lectura";
            case "VISIBLE_IN_FORM" -> "Visible en formulario";
            case "VISIBLE_IN_REPORT" -> "Visible en reporte";
            case "FORM" -> "Formulario";
            case "TABLE" -> "Tabla";
            case "REPORT" -> "Reporte";
            case "IDENTIFIER" -> "Identificador";
            case "SHORT_TEXT" -> "Texto corto";
            case "LONG_TEXT" -> "Texto largo";
            case "INTEGER_NUMBER" -> "Número entero";
            case "DECIMAL_NUMBER" -> "Número decimal";
            case "MONEY" -> "Moneda";
            case "PERCENTAGE" -> "Porcentaje";
            case "BOOLEAN" -> "Booleano";
            case "DATE" -> "Fecha";
            case "TIME" -> "Hora";
            case "DATE_TIME" -> "Fecha y hora";
            case "EMAIL" -> "Correo";
            case "PHONE" -> "Teléfono";
            case "IDENTIFICATION" -> "Identificación";
            case "URL" -> "URL";
            case "STATUS" -> "Estado";
            case "REFERENCE" -> "Referencia";
            case "FILE_ATTACHMENT" -> "Archivo adjunto";
            case "FLEXIBLE_STRUCTURE" -> "Estructura flexible";
            case "UNKNOWN" -> "Desconocido";
            default -> fallbackLabel(value);
        };
    }

    private static String fallbackLabel(Enum<?> value) {
        String text = value.name().toLowerCase(java.util.Locale.ROOT).replace('_', ' ');
        return text.substring(0, 1).toUpperCase(java.util.Locale.ROOT) + text.substring(1);
    }

    private static String optional(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private static String yesNo(boolean value) {
        return value ? "sí" : "no";
    }
}
