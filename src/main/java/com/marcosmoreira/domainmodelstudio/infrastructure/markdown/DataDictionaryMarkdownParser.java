package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldVisibility;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/** Importa Markdown oficial de diccionario de datos hacia un documento editable. */
public final class DataDictionaryMarkdownParser implements MarkdownModelParser {

    @Override
    public DiagramProject parse(Path markdownFile) throws IOException, MarkdownModelParsingException {
        Objects.requireNonNull(markdownFile, "markdownFile");
        return parse(Files.readString(markdownFile, StandardCharsets.UTF_8), markdownFile.toString());
    }

    @Override
    public DiagramProject parse(String markdownContent, String sourceName) throws MarkdownModelParsingException {
        Objects.requireNonNull(markdownContent, "markdownContent");
        MarkdownImportDocument importDocument = MarkdownImportDocument.parse(markdownContent);
        MarkdownFrontMatter frontMatter = importDocument.frontMatter();
        String title = frontMatter.valueOrDefault("name", "Diccionario de datos importado");
        List<DataDictionaryEntity> entities = parseEntities(importDocument.body());
        if (entities.isEmpty()) {
            throw new MarkdownModelParsingException("El diccionario de datos no contiene entidades reconocibles.");
        }
        DataDictionaryDocument document;
        try {
            document = new DataDictionaryDocument(
                    title,
                    frontMatter.valueOrDefault("client", ""),
                    frontMatter.valueOrDefault("organization", ""),
                    frontMatter.valueOrDefault("author", ""),
                    frontMatter.valueOrDefault("version", "borrador"),
                    LocalDate.now(),
                    DataDictionaryStatus.DRAFT,
                    frontMatter.valueOrDefault("introduction", ""),
                    frontMatter.valueOrDefault("logo_reference", ""),
                    entities,
                    frontMatter.valueOrDefault("description", ""));
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir el diccionario de datos: " + exception.getMessage(), exception);
        }
        return MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title), title, DiagramTypeId.DATA_DICTIONARY).withDataDictionary(document),
                sourceName);
    }

    private static List<DataDictionaryEntity> parseEntities(String body) throws MarkdownModelParsingException {
        List<DataDictionaryEntity> entities = new ArrayList<>();
        PendingEntity pending = null;
        List<String> tableRows = new ArrayList<>();
        boolean collectingFieldTable = false;
        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank() || line.startsWith("# ") || line.startsWith(">")) {
                collectingFieldTable = false;
                continue;
            }
            if (isEntityHeading(line)) {
                flushPending(entities, pending, tableRows);
                pending = new PendingEntity(headingText(line));
                tableRows = new ArrayList<>();
                collectingFieldTable = false;
                continue;
            }
            if (pending == null) {
                continue;
            }
            if (MarkdownTextUtils.isPropertyLine(line) && !line.startsWith("|")) {
                String key = normalizeKey(MarkdownTextUtils.keyBeforeColon(line));
                String value = MarkdownTextUtils.valueAfterColon(line);
                if (key.equals("proposito") || key.equals("propósito") || key.equals("descripcion") || key.equals("descripción")) {
                    pending.description = value;
                } else if (key.equals("responsable del dato") || key.equals("responsable") || key.equals("modulo") || key.equals("módulo")) {
                    pending.moduleName = value;
                } else if (key.equals("origen")) {
                    pending.origin = value;
                } else if (key.equals("tipo")) {
                    pending.kind = inferKind(value);
                } else if (key.equals("notas")) {
                    pending.notes = value;
                }
                continue;
            }
            if (line.startsWith("|")) {
                if (isFieldTableHeader(line)) {
                    collectingFieldTable = true;
                    tableRows.add(line);
                } else if (collectingFieldTable) {
                    tableRows.add(line);
                }
            } else {
                collectingFieldTable = false;
            }
        }
        flushPending(entities, pending, tableRows);
        return List.copyOf(entities);
    }

    private static boolean isEntityHeading(String line) {
        if (!(line.startsWith("## ") || line.startsWith("### "))) {
            return false;
        }
        String text = normalizeText(headingText(line));
        return !(text.equals("introduccion")
                || text.equals("resumen")
                || text.equals("resumen ejecutivo")
                || text.equals("tabla general de entidades")
                || text.equals("entidades y campos")
                || text.equals("observaciones generales")
                || text.equals("documento de entrega")
                || text.equals("diccionario de datos"));
    }

    private static String headingText(String line) {
        return line.replaceFirst("^#+\\s+", "").strip();
    }

    private static boolean isFieldTableHeader(String line) {
        String text = normalizeText(line);
        return text.contains("campo")
                && (text.contains("tipo esperado")
                || text.contains("tipo logico")
                || text.contains("nombre tecnico"));
    }

    private static void flushPending(
            List<DataDictionaryEntity> entities,
            PendingEntity pending,
            List<String> tableRows
    ) throws MarkdownModelParsingException {
        if (pending == null) {
            return;
        }
        List<DataDictionaryField> fields = parseFields(pending.displayName, tableRows);
        try {
            entities.add(new DataDictionaryEntity(
                    MarkdownTextUtils.toStableId(pending.displayName),
                    pending.displayName,
                    MarkdownTextUtils.toStableId(pending.displayName),
                    pending.description,
                    pending.moduleName,
                    pending.kind,
                    pending.origin,
                    DataDictionaryStatus.DRAFT,
                    fields,
                    pending.notes));
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("Entidad inválida en diccionario: " + pending.displayName + ". " + exception.getMessage(), exception);
        }
    }

    private static List<DataDictionaryField> parseFields(String entityName, List<String> tableRows) throws MarkdownModelParsingException {
        List<DataDictionaryField> fields = new ArrayList<>();
        for (String row : tableRows) {
            if (row.contains("---") || row.toLowerCase(Locale.ROOT).contains("campo | tipo")) {
                continue;
            }
            List<String> cells = splitTableRow(row);
            if (cells.size() < 2) {
                continue;
            }
            String name = cleanCell(cells.get(0));
            if (name.isBlank() || name.equalsIgnoreCase("campo")) {
                continue;
            }
            if (looksLikeExportedFieldRow(cells)) {
                fields.add(toField(entityName, name, cleanCell(cells.get(1)), cleanCell(cells.get(2)),
                        cleanCell(cells.get(3)), cleanCell(cells.get(3)), cleanCell(cells.get(4))));
                continue;
            }
            String type = cells.size() > 1 ? cleanCell(cells.get(1)) : "";
            String required = cells.size() > 2 ? cleanCell(cells.get(2)) : "";
            String rule = cells.size() > 3 ? cleanCell(cells.get(3)) : "";
            String observation = cells.size() > 4 ? cleanCell(cells.get(4)) : "";
            fields.add(toField(entityName, name, MarkdownTextUtils.toStableId(name), type, required, rule, observation));
        }
        return List.copyOf(fields);
    }

    private static boolean looksLikeExportedFieldRow(List<String> cells) {
        if (cells.size() < 5) {
            return false;
        }
        String technicalName = cleanCell(cells.get(1));
        String typeText = normalizeText(cleanCell(cells.get(2)));
        String constraintsText = normalizeText(cleanCell(cells.get(3)));
        return !technicalName.isBlank()
                && !typeText.isBlank()
                && !constraintsText.equals("regla")
                && (typeText.contains("texto") || typeText.contains("numero") || typeText.contains("número")
                || typeText.contains("ident") || typeText.contains("fecha") || typeText.contains("hora")
                || typeText.contains("correo") || typeText.contains("email") || typeText.contains("estado")
                || typeText.contains("moneda") || typeText.contains("dinero") || typeText.contains("porcentaje")
                || typeText.contains("archivo") || typeText.contains("referencia") || typeText.contains("estructura")
                || typeText.contains("desconocido") || typeText.contains("booleano"));
    }

    private static DataDictionaryField toField(
            String entityName,
            String name,
            String technicalName,
            String type,
            String required,
            String rule,
            String observation
    ) throws MarkdownModelParsingException {
        technicalName = technicalName == null || technicalName.isBlank() ? MarkdownTextUtils.toStableId(name) : technicalName.strip();
        Set<FieldConstraint> constraints = new LinkedHashSet<>();
        String normalizedRule = normalizeText(rule + " " + observation + " " + name);
        if (isYes(required) || normalizedRule.contains("obligatorio")) {
            constraints.add(FieldConstraint.REQUIRED);
        }
        if (normalizedRule.contains("unico") || normalizedRule.contains("unique")) {
            constraints.add(FieldConstraint.UNIQUE);
        }
        if (technicalName.equals("id") || normalizedRule.contains("primary_key") || normalizedRule.contains("clave primaria")) {
            constraints.add(FieldConstraint.PRIMARY_KEY);
            constraints.add(FieldConstraint.UNIQUE);
            constraints.add(FieldConstraint.REQUIRED);
        }
        String foreignKeyReference = "";
        if (!technicalName.equals("id") && (technicalName.endsWith("_id") || normalizedRule.contains("fk valida") || normalizedRule.contains("foreign key"))) {
            constraints.add(FieldConstraint.FOREIGN_KEY);
            foreignKeyReference = inferForeignKeyReference(technicalName);
        }
        LogicalDataType logicalType = inferLogicalType(type, name, rule);
        try {
            return new DataDictionaryField(
                    technicalName,
                    name,
                    technicalName,
                    logicalType,
                    physicalTypeSuggestion(type),
                    constraints,
                    foreignKeyReference,
                    "",
                    rule,
                    observation,
                    rule,
                    rule,
                    "",
                    Set.of(FieldVisibility.FORM, FieldVisibility.TABLE, FieldVisibility.REPORT),
                    !constraints.contains(FieldConstraint.READ_ONLY),
                    "");
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("Campo inválido en " + entityName + ": " + name + ". " + exception.getMessage(), exception);
        }
    }

    private static List<String> splitTableRow(String row) {
        String trimmed = row.strip();
        if (trimmed.startsWith("|")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("|")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return java.util.Arrays.stream(trimmed.split("\\|", -1))
                .map(String::strip)
                .toList();
    }

    private static LogicalDataType inferLogicalType(String type, String name, String rule) {
        String text = normalizeText(type + " " + name + " " + rule);
        if (text.contains("correo") || text.contains("email")) return LogicalDataType.EMAIL;
        if (text.contains("identificacion") || text.contains("cedula") || text.contains("cédula")) return LogicalDataType.IDENTIFICATION;
        if (text.contains("telefono") || text.contains("teléfono") || text.contains("phone")) return LogicalDataType.PHONE;
        if (text.contains("fecha") && text.contains("hora")) return LogicalDataType.DATE_TIME;
        if (text.contains("fecha")) return LogicalDataType.DATE;
        if (text.contains("hora")) return LogicalDataType.TIME;
        if (text.contains("decimal") || text.contains("nota") || text.contains("promedio")) return LogicalDataType.DECIMAL_NUMBER;
        if (text.contains("dinero") || text.contains("monto") || text.contains("valor")) return LogicalDataType.MONEY;
        if (text.contains("porcentaje")) return LogicalDataType.PERCENTAGE;
        if (text.contains("entero") || text.contains("numero entero") || text.endsWith(" id") || text.contains("_id")) return LogicalDataType.INTEGER_NUMBER;
        if (text.contains("boolean") || text.contains("si/no") || text.contains("verdadero")) return LogicalDataType.BOOLEAN;
        if (text.contains("catalogo") || text.contains("catálogo") || text.contains("estado") || text.contains("rol")) return LogicalDataType.STATUS;
        if (text.contains("archivo")) return LogicalDataType.FILE_ATTACHMENT;
        if (text.contains("json") || text.contains("estructura")) return LogicalDataType.FLEXIBLE_STRUCTURE;
        if (text.contains("texto largo") || text.contains("observacion") || text.contains("descripcion")) return LogicalDataType.LONG_TEXT;
        if (text.equals("id") || text.contains("identificador")) return LogicalDataType.IDENTIFIER;
        if (text.contains("texto")) return LogicalDataType.SHORT_TEXT;
        return LogicalDataType.UNKNOWN;
    }

    private static DataEntityKind inferKind(String value) {
        String text = normalizeText(value);
        if (text.contains("catalogo")) return DataEntityKind.CATALOG;
        if (text.contains("transacc")) return DataEntityKind.TRANSACTIONAL;
        if (text.contains("asoci")) return DataEntityKind.ASSOCIATIVE;
        if (text.contains("auditor")) return DataEntityKind.AUDIT;
        if (text.contains("soporte") || text.contains("support")) return DataEntityKind.SUPPORT;
        return DataEntityKind.MAIN;
    }

    private static String inferForeignKeyReference(String technicalName) {
        if (technicalName.endsWith("_id") && technicalName.length() > 3) {
            return technicalName.substring(0, technicalName.length() - 3);
        }
        return "referencia_externa";
    }

    private static String physicalTypeSuggestion(String type) {
        String value = cleanCell(type);
        return value.isBlank() ? "" : value;
    }

    private static boolean isYes(String value) {
        String text = normalizeText(value);
        return text.equals("si") || text.equals("sí") || text.equals("yes") || text.equals("true") || text.equals("obligatorio");
    }

    private static String cleanCell(String value) {
        return value == null ? "" : value.replace("`", "").strip();
    }

    private static String normalizeKey(String value) {
        return normalizeText(value).replace('_', ' ');
    }

    private static String normalizeText(String value) {
        String normalized = java.text.Normalizer.normalize(value == null ? "" : value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replace('_', ' ')
                .strip();
        return normalized.replaceAll("\\s+", " ");
    }



    private static String stableProjectId(String title) {
        return MarkdownTextUtils.toStableId(title + " data dictionary");
    }

    private static final class PendingEntity {
        private final String displayName;
        private String description = "";
        private String moduleName = "";
        private String origin = "levantamiento manual";
        private DataEntityKind kind = DataEntityKind.MAIN;
        private String notes = "";

        private PendingEntity(String displayName) throws MarkdownModelParsingException {
            if (displayName == null || displayName.isBlank()) {
                throw new MarkdownModelParsingException("Entidad de diccionario sin nombre.");
            }
            this.displayName = displayName.strip();
        }
    }
}
