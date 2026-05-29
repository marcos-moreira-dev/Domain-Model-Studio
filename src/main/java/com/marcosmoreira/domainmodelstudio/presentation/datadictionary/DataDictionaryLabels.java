package com.marcosmoreira.domainmodelstudio.presentation.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import java.util.Locale;

/** Etiquetas de presentación del diccionario de datos. */
final class DataDictionaryLabels {

    private DataDictionaryLabels() {
    }

    static String optional(String value) {
        return value == null || value.isBlank() ? "—" : value;
    }

    static String constraintSummary(DataDictionaryField field) {
        if (field.constraints().isEmpty()) {
            return "—";
        }
        return field.constraints().stream()
                .map(DataDictionaryLabels::label)
                .sorted()
                .reduce((left, right) -> left + ", " + right)
                .orElse("—");
    }

    static String visibilitySummary(DataDictionaryField field) {
        if (field.visibility().isEmpty()) {
            return "—";
        }
        return field.visibility().stream()
                .map(DataDictionaryLabels::label)
                .sorted()
                .reduce((left, right) -> left + ", " + right)
                .orElse("—");
    }

    static String label(Enum<?> value) {
        if (value == null) {
            return "—";
        }
        return switch (value.name()) {
            case "MAIN" -> "Principal";
            case "CATALOG" -> "Catálogo";
            case "TRANSACTIONAL" -> "Transaccional";
            case "ASSOCIATIVE" -> "Asociativa";
            case "AUDIT" -> "Auditoría";
            case "SUPPORT" -> "Soporte";
            case "DRAFT" -> "Borrador";
            case "REVIEWED" -> "Revisado";
            case "APPROVED" -> "Aprobado";
            case "IMPLEMENTED" -> "En uso";
            case "OBSOLETE" -> "Obsoleto";
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
            case "FILE_ATTACHMENT" -> "Archivo";
            case "FLEXIBLE_STRUCTURE" -> "Estructura flexible";
            case "UNKNOWN" -> "Desconocido";
            case "REQUIRED" -> "Obligatorio";
            case "UNIQUE" -> "Único";
            case "PRIMARY_KEY" -> "Clave principal";
            case "FOREIGN_KEY" -> "Referencia";
            case "DERIVED" -> "Derivado";
            case "READ_ONLY" -> "Solo lectura";
            case "VISIBLE_IN_FORM" -> "Visible formulario";
            case "VISIBLE_IN_REPORT" -> "Visible reporte";
            case "FORM" -> "Formulario";
            case "TABLE" -> "Tabla";
            case "REPORT" -> "Reporte";
            default -> value.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        };
    }
}
