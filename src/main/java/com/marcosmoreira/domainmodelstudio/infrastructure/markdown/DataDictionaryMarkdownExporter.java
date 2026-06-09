package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.datadictionary.ExportDataDictionaryMarkdownUseCase;
import com.marcosmoreira.domainmodelstudio.application.export.ExportTargetPathPolicy;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

/** Exporta el diccionario de datos como Markdown formal de entrega y referencia para IA. */
public final class DataDictionaryMarkdownExporter implements ExportDataDictionaryMarkdownUseCase {

    private final ExportTargetPathPolicy targetPathPolicy;

    public DataDictionaryMarkdownExporter() {
        this(new ExportTargetPathPolicy());
    }

    DataDictionaryMarkdownExporter(ExportTargetPathPolicy targetPathPolicy) {
        this.targetPathPolicy = Objects.requireNonNull(targetPathPolicy, "targetPathPolicy");
    }

    @Override
    public Path export(DataDictionaryDocument document, Path destinationFile) throws IOException {
        Objects.requireNonNull(destinationFile, "destinationFile");
        Path normalizedDestination = targetPathPolicy.ensureMarkdownExtension(destinationFile)
                .toAbsolutePath().normalize();
        Path parent = normalizedDestination.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(normalizedDestination, exportToString(document), StandardCharsets.UTF_8);
        return normalizedDestination;
    }

    @Override
    public String exportToString(DataDictionaryDocument document) {
        Objects.requireNonNull(document, "document");
        StringBuilder markdown = new StringBuilder(8192);
        writeFrontMatter(markdown, document);
        writeCover(markdown, document);
        writeSummary(markdown, document);
        writeEntityOverview(markdown, document);
        writeEntities(markdown, document);
        writeNotes(markdown, document);
        return markdown.toString();
    }

    private static void writeFrontMatter(StringBuilder markdown, DataDictionaryDocument document) {
        markdown.append("---\n")
                .append("diagram_type: \"").append(DiagramTypeId.DATA_DICTIONARY.value()).append("\"\n")
                .append("name: \"").append(escapeYaml(document.projectName())).append("\"\n")
                .append("client: \"").append(escapeYaml(document.clientName())).append("\"\n")
                .append("organization: \"").append(escapeYaml(document.organizationName())).append("\"\n")
                .append("author: \"").append(escapeYaml(document.author())).append("\"\n")
                .append("logo_reference: \"").append(escapeYaml(document.logoReference())).append("\"\n")
                .append("status: \"").append(label(document.status())).append("\"\n")
                .append("version: \"").append(escapeYaml(document.version())).append("\"\n")
                .append("document_date: \"").append(document.documentDate()).append("\"\n")
                .append("introduction: \"").append(escapeYaml(document.introduction())).append("\"\n")
                .append("importable: true\n")
                .append("---\n\n");
    }

    private static void writeCover(StringBuilder markdown, DataDictionaryDocument document) {
        markdown.append("# Diccionario de datos — ").append(document.projectName()).append("\n\n")
                .append("| Dato | Valor |\n")
                .append("|---|---|\n")
                .append("| Proyecto | ").append(cell(document.projectName())).append(" |\n")
                .append("| Cliente | ").append(cell(optional(document.clientName()))).append(" |\n")
                .append("| Organización | ").append(cell(optional(document.organizationName()))).append(" |\n")
                .append("| Autor | ").append(cell(optional(document.author()))).append(" |\n")
                .append("| Versión | ").append(cell(document.version())).append(" |\n")
                .append("| Fecha | ").append(document.documentDate()).append(" |\n")
                .append("| Estado | ").append(label(document.status())).append(" |\n")
                .append("| Logo opcional | ").append(cell(optional(document.logoReference()))).append(" |\n\n")
                .append("> Documento formal para revisar entidades, campos, tipos, restricciones, reglas, visibilidad y observaciones del sistema.\n\n");
        markdown.append("## Introducción\n\n");
        if (document.hasIntroduction()) {
            markdown.append(document.introduction()).append("\n\n");
        } else {
            markdown.append("Este documento describe el modelo de datos de referencia del sistema y sirve como apoyo para análisis, desarrollo, revisión y mantenimiento.\n\n");
        }
    }

    private static void writeSummary(StringBuilder markdown, DataDictionaryDocument document) {
        markdown.append("## Resumen ejecutivo\n\n")
                .append("- Entidades documentadas: ").append(document.entityCount()).append("\n")
                .append("- Campos documentados: ").append(document.fieldCount()).append("\n")
                .append("- Estado del documento: ").append(label(document.status())).append("\n")
                .append("- Entidades con clave primaria: ").append(document.entities().stream()
                        .filter(DataDictionaryEntity::hasPrimaryKey).count()).append("\n\n");
        if (document.isEmpty()) {
            markdown.append("El diccionario aún no contiene entidades. Puede usarse como borrador trazable.\n\n");
        }
    }

    private static void writeEntityOverview(StringBuilder markdown, DataDictionaryDocument document) {
        if (document.entities().isEmpty()) {
            return;
        }
        markdown.append("## Tabla general de entidades\n\n")
                .append("| Entidad | Nombre técnico | Tipo | Campos | Módulo | Estado |\n")
                .append("|---|---|---|---:|---|---|\n");
        document.entities().stream()
                .sorted(Comparator.comparing(DataDictionaryEntity::displayName))
                .forEach(entity -> markdown.append("| ").append(cell(entity.displayName()))
                        .append(" | `").append(entity.technicalName()).append('`')
                        .append(" | ").append(label(entity.kind()))
                        .append(" | ").append(entity.fieldCount())
                        .append(" | ").append(cell(optional(entity.moduleName())))
                        .append(" | ").append(label(entity.status()))
                        .append(" |\n"));
        markdown.append('\n');
    }

    private static void writeEntities(StringBuilder markdown, DataDictionaryDocument document) {
        markdown.append("## Entidades y campos\n\n");
        document.entities().stream()
                .sorted(Comparator.comparing(DataDictionaryEntity::displayName))
                .forEach(entity -> writeEntity(markdown, entity));
    }

    private static void writeEntity(StringBuilder markdown, DataDictionaryEntity entity) {
        markdown.append("### ").append(entity.displayName()).append("\n\n")
                .append("| Dato | Valor |\n")
                .append("|---|---|\n")
                .append("| Nombre técnico | `").append(entity.technicalName()).append("` |\n")
                .append("| Tipo | ").append(label(entity.kind())).append(" |\n")
                .append("| Módulo | ").append(cell(optional(entity.moduleName()))).append(" |\n")
                .append("| Origen | ").append(cell(entity.origin())).append(" |\n")
                .append("| Estado | ").append(label(entity.status())).append(" |\n")
                .append("| Descripción | ").append(cell(optional(entity.description()))).append(" |\n\n");
        if (entity.fields().isEmpty()) {
            markdown.append("Sin campos documentados todavía.\n\n");
        } else {
            markdown.append("| Campo | Nombre técnico | Tipo lógico | Restricciones | Descripción |\n")
                    .append("|---|---|---|---|---|\n");
            entity.fields().forEach(field -> writeFieldRow(markdown, field));
            markdown.append('\n');
            entity.fields().stream()
                    .filter(DataDictionaryMarkdownExporter::hasFieldDetails)
                    .forEach(field -> writeFieldDetails(markdown, field));
        }
        if (!entity.notes().isBlank()) {
            markdown.append("**Notas de entidad:** ").append(entity.notes()).append("\n\n");
        }
    }

    private static void writeFieldRow(StringBuilder markdown, DataDictionaryField field) {
        markdown.append("| ").append(cell(field.displayName()))
                .append(" | `").append(field.technicalName()).append('`')
                .append(" | ").append(label(field.logicalType()))
                .append(" | ").append(cell(constraints(field)))
                .append(" | ").append(cell(optional(field.description())))
                .append(" |\n");
    }

    private static void writeFieldDetails(StringBuilder markdown, DataDictionaryField field) {
        markdown.append("#### ").append(field.displayName()).append("\n\n");
        appendDetail(markdown, "Tipo físico sugerido", field.physicalTypeSuggestion());
        appendDetail(markdown, "Referencia", field.foreignKeyReference());
        appendDetail(markdown, "Valor por defecto", field.defaultValue());
        appendDetail(markdown, "Formato esperado", field.expectedFormat());
        appendDetail(markdown, "Visible en", visibility(field));
        appendDetail(markdown, "Editable por usuario", field.userEditable() ? "Sí" : "No");
        appendDetail(markdown, "Descripción", field.description());
        appendDetail(markdown, "Regla de negocio", field.businessRule());
        appendDetail(markdown, "Validación", field.validationRule());
        appendDetail(markdown, "Ejemplo", field.example());
        appendDetail(markdown, "Notas", field.notes());
        markdown.append('\n');
    }

    private static void appendDetail(StringBuilder markdown, String label, String value) {
        if (value != null && !value.isBlank()) {
            markdown.append("- ").append(label).append(": ").append(value.strip()).append("\n");
        }
    }

    private static boolean hasFieldDetails(DataDictionaryField field) {
        return !field.physicalTypeSuggestion().isBlank()
                || !field.foreignKeyReference().isBlank()
                || !field.defaultValue().isBlank()
                || !field.expectedFormat().isBlank()
                || !field.description().isBlank()
                || field.hasBusinessRule()
                || field.hasValidationRule()
                || !field.example().isBlank()
                || !field.notes().isBlank()
                || !field.visibility().isEmpty()
                || !constraints(field).isBlank();
    }

    private static void writeNotes(StringBuilder markdown, DataDictionaryDocument document) {
        if (!document.notes().isBlank()) {
            markdown.append("## Observaciones generales\n\n")
                    .append(document.notes()).append("\n");
        }
    }

    private static String constraints(DataDictionaryField field) {
        if (field.constraints().isEmpty()) {
            return "sin restricciones registradas";
        }
        return field.constraints().stream()
                .sorted(Comparator.comparing(Enum::name))
                .map(DataDictionaryMarkdownExporter::label)
                .collect(Collectors.joining(", "));
    }

    private static String visibility(DataDictionaryField field) {
        if (field.visibility().isEmpty()) {
            return "no definido";
        }
        return field.visibility().stream()
                .sorted(Comparator.comparing(Enum::name))
                .map(DataDictionaryMarkdownExporter::label)
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

    private static String cell(String value) {
        return optional(value).replace("|", "\\|").replace("\n", " ").replace("\r", " ");
    }

    private static String escapeYaml(String value) {
        return optional(value).replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
