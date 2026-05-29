package com.marcosmoreira.domainmodelstudio.domain.datadictionary;

import java.util.Objects;
import java.util.Set;

/** Campo documentado dentro del diccionario de datos. */
public record DataDictionaryField(
        String name,
        String displayName,
        String technicalName,
        LogicalDataType logicalType,
        String physicalTypeSuggestion,
        Set<FieldConstraint> constraints,
        String foreignKeyReference,
        String defaultValue,
        String expectedFormat,
        String description,
        String businessRule,
        String validationRule,
        String example,
        Set<FieldVisibility> visibility,
        boolean userEditable,
        String notes
) {

    public DataDictionaryField {
        name = requireText(name, "name");
        displayName = normalizeOrDefault(displayName, name);
        technicalName = normalizeOrDefault(technicalName, name);
        Objects.requireNonNull(logicalType, "logicalType");
        physicalTypeSuggestion = normalizeOptional(physicalTypeSuggestion);
        constraints = Set.copyOf(Objects.requireNonNull(constraints, "constraints"));
        foreignKeyReference = normalizeOptional(foreignKeyReference);
        defaultValue = normalizeOptional(defaultValue);
        expectedFormat = normalizeOptional(expectedFormat);
        description = normalizeOptional(description);
        businessRule = normalizeOptional(businessRule);
        validationRule = normalizeOptional(validationRule);
        example = normalizeOptional(example);
        visibility = Set.copyOf(Objects.requireNonNull(visibility, "visibility"));
        notes = normalizeOptional(notes);
        validateForeignKey(constraints, foreignKeyReference);
    }

    public DataDictionaryField(
            String name,
            LogicalDataType logicalType,
            Set<FieldConstraint> constraints,
            String description,
            String example
    ) {
        this(name, name, name, logicalType, "", constraints, "", "", "", description,
                "", "", example, Set.of(), true, "");
    }

    public boolean isRequired() {
        return hasConstraint(FieldConstraint.REQUIRED);
    }

    public boolean isUnique() {
        return hasConstraint(FieldConstraint.UNIQUE);
    }

    public boolean isPrimaryKey() {
        return hasConstraint(FieldConstraint.PRIMARY_KEY);
    }

    public boolean isForeignKey() {
        return hasConstraint(FieldConstraint.FOREIGN_KEY);
    }

    public boolean isVisibleIn(FieldVisibility fieldVisibility) {
        return visibility.contains(Objects.requireNonNull(fieldVisibility, "fieldVisibility"));
    }

    public boolean hasConstraint(FieldConstraint constraint) {
        return constraints.contains(Objects.requireNonNull(constraint, "constraint"));
    }

    public boolean hasPhysicalTypeSuggestion() {
        return !physicalTypeSuggestion.isBlank();
    }

    public boolean hasValidationRule() {
        return !validationRule.isBlank();
    }

    public boolean hasBusinessRule() {
        return !businessRule.isBlank();
    }

    private static void validateForeignKey(Set<FieldConstraint> constraints, String foreignKeyReference) {
        boolean markedAsForeignKey = constraints.contains(FieldConstraint.FOREIGN_KEY);
        if (markedAsForeignKey && foreignKeyReference.isBlank()) {
            throw new IllegalArgumentException("Un campo FOREIGN_KEY debe indicar foreignKeyReference.");
        }
        if (!markedAsForeignKey && !foreignKeyReference.isBlank()) {
            throw new IllegalArgumentException("foreignKeyReference solo debe usarse en campos FOREIGN_KEY.");
        }
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }

    private static String normalizeOrDefault(String value, String defaultValue) {
        String normalized = normalizeOptional(value);
        return normalized.isBlank() ? defaultValue : normalized;
    }

    private static String normalizeOptional(String value) {
        return value == null ? "" : value.strip();
    }
}
