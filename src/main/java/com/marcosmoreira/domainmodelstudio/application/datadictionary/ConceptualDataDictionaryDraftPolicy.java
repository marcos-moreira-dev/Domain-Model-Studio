package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Reglas conservadoras para convertir un modelo conceptual en borrador de diccionario.
 *
 * <p>Esta política evita inferencias agresivas: la salida se considera material de
 * levantamiento editable, no una base de datos física ni una migración SQL.</p>
 */
public final class ConceptualDataDictionaryDraftPolicy {

    public DataEntityKind inferEntityKind(EntityElement entity) {
        if (entity.kind() == EntityKind.WEAK) {
            return DataEntityKind.SUPPORT;
        }
        return DataEntityKind.MAIN;
    }

    public LogicalDataType inferLogicalType(AttributeElement attribute) {
        String normalized = normalize(attribute.name());
        if (attribute.isPrimaryKey() || normalized.equals("id") || normalized.endsWith(" id")
                || normalized.endsWith("_id")) {
            return LogicalDataType.IDENTIFIER;
        }
        if (containsAny(normalized, "fecha", "date")) {
            return LogicalDataType.DATE;
        }
        if (containsAny(normalized, "hora", "time")) {
            return LogicalDataType.TIME;
        }
        if (containsAny(normalized, "correo", "email", "mail")) {
            return LogicalDataType.EMAIL;
        }
        if (containsAny(normalized, "telefono", "teléfono", "celular", "phone")) {
            return LogicalDataType.PHONE;
        }
        if (containsAny(normalized, "cedula", "cédula", "ruc", "identificacion", "identificación")) {
            return LogicalDataType.IDENTIFICATION;
        }
        if (containsAny(normalized, "precio", "costo", "valor", "monto", "total", "saldo")) {
            return LogicalDataType.MONEY;
        }
        if (containsAny(normalized, "porcentaje", "percent")) {
            return LogicalDataType.PERCENTAGE;
        }
        if (containsAny(normalized, "cantidad", "numero", "número", "edad")) {
            return LogicalDataType.INTEGER_NUMBER;
        }
        if (containsAny(normalized, "estado", "status")) {
            return LogicalDataType.STATUS;
        }
        if (attribute.isMultivalued() || attribute.hasTag(AttributeTag.COMPOSITE)) {
            return LogicalDataType.FLEXIBLE_STRUCTURE;
        }
        return LogicalDataType.UNKNOWN;
    }

    public Set<FieldConstraint> constraintsFor(AttributeElement attribute) {
        LinkedHashSet<FieldConstraint> constraints = new LinkedHashSet<>();
        if (attribute.isPrimaryKey() || attribute.isPartialKey()) {
            constraints.add(FieldConstraint.PRIMARY_KEY);
            constraints.add(FieldConstraint.REQUIRED);
            constraints.add(FieldConstraint.UNIQUE);
        }
        if (attribute.hasTag(AttributeTag.UNIQUE)) {
            constraints.add(FieldConstraint.UNIQUE);
        }
        if (attribute.isDerived()) {
            constraints.add(FieldConstraint.DERIVED);
            constraints.add(FieldConstraint.READ_ONLY);
        }
        if (!attribute.isOptional() && !attribute.isDerived()) {
            constraints.add(FieldConstraint.VISIBLE_IN_FORM);
        }
        constraints.add(FieldConstraint.VISIBLE_IN_REPORT);
        return Set.copyOf(constraints);
    }

    public String entityOrigin() {
        return "borrador generado desde modelo conceptual";
    }

    public String fieldNotes(AttributeElement attribute) {
        LinkedHashSet<String> notes = new LinkedHashSet<>();
        notes.add("Campo sugerido desde atributo conceptual; revisar tipo, obligatoriedad y reglas antes de implementar.");
        if (attribute.isOptional()) {
            notes.add("El atributo conceptual está marcado como opcional.");
        }
        if (attribute.isMultivalued()) {
            notes.add("El atributo conceptual es multivaluado; decidir si será estructura flexible, tabla detalle o relación separada.");
        }
        if (attribute.hasTag(AttributeTag.COMPOSITE)) {
            notes.add("El atributo conceptual es compuesto; decidir si se descompone en campos más pequeños.");
        }
        if (attribute.isDerived()) {
            notes.add("El atributo conceptual es derivado; documentar fórmula, fuente o regla de cálculo.");
        }
        if (attribute.hasTag(AttributeTag.SENSITIVE)) {
            notes.add("El atributo conceptual es sensible; revisar visibilidad, auditoría y permisos.");
        }
        return String.join(" ", notes);
    }

    private static boolean containsAny(String value, String... tokens) {
        for (String token : tokens) {
            if (value.contains(token)) {
                return true;
            }
        }
        return false;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip().toLowerCase();
    }
}
