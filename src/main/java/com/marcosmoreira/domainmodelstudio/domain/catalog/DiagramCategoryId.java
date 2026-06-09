package com.marcosmoreira.domainmodelstudio.domain.catalog;

import java.util.Objects;

/** Identificador estable de una categoría de diagramas. */
public record DiagramCategoryId(String value) {

    public static final DiagramCategoryId BUSINESS_ANALYSIS = new DiagramCategoryId("business-analysis");
    public static final DiagramCategoryId DATA_MODELING = new DiagramCategoryId("data-modeling");
    public static final DiagramCategoryId BUSINESS_PROCESS = new DiagramCategoryId("business-process");
    public static final DiagramCategoryId SOFTWARE_ARCHITECTURE = new DiagramCategoryId("software-architecture");
    public static final DiagramCategoryId UML_STRUCTURAL = new DiagramCategoryId("uml-structural");
    public static final DiagramCategoryId UML_BEHAVIOR = new DiagramCategoryId("uml-behavior");
    public static final DiagramCategoryId UML_INTERACTION = new DiagramCategoryId("uml-interaction");
    public static final DiagramCategoryId ADMIN_APPLICATIONS = new DiagramCategoryId("admin-applications");
    public static final DiagramCategoryId TECHNICAL_DOCUMENTATION = new DiagramCategoryId("technical-documentation");

    public DiagramCategoryId {
        Objects.requireNonNull(value, "value");
        value = value.strip();
        if (value.isBlank()) {
            throw new IllegalArgumentException("El identificador de categoría no puede estar vacío.");
        }
    }

    public static DiagramCategoryId of(String value) {
        return new DiagramCategoryId(value);
    }
}
