package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategory;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Catálogo oficial de familias de diagramas definido por la planificación P-31. */
public final class DefaultDiagramCategoryCatalog implements DiagramCategoryCatalog {

    private static final List<DiagramCategory> OFFICIAL_CATEGORIES = List.of(
            category(DiagramCategoryId.BUSINESS_ANALYSIS, "Levantamiento y análisis",
                    "Preparar documentos fuente para entender negocio, reglas, estados, entidades y preguntas pendientes.", 5),
            category(DiagramCategoryId.DATA_MODELING, "Modelado de datos",
                    "Representar entidades, campos, reglas y estructuras de información.", 10),
            category(DiagramCategoryId.BUSINESS_PROCESS, "Procesos de negocio",
                    "Representar flujos de trabajo, pasos, responsables y puntos de control.", 20),
            category(DiagramCategoryId.SOFTWARE_ARCHITECTURE, "Arquitectura de software",
                    "Representar sistemas, contenedores y relaciones técnicas de alto nivel.", 30),
            category(DiagramCategoryId.UML_STRUCTURAL, "UML estructural",
                    "Representar estructura estática de software o del dominio cuando aporte claridad técnica.", 40),
            category(DiagramCategoryId.UML_BEHAVIOR, "UML de comportamiento",
                    "Representar actividades, estados y reglas dinámicas del sistema.", 50),
            category(DiagramCategoryId.UML_INTERACTION, "UML de interacción",
                    "Representar comunicación temporal entre actores, objetos o componentes.", 60),
            category(DiagramCategoryId.ADMIN_APPLICATIONS, "Aplicaciones administrativas",
                    "Representar módulos, roles, permisos, pantallas y flujos de operación administrativa.", 70),
            category(DiagramCategoryId.TECHNICAL_DOCUMENTATION, "Documentación técnica",
                    "Organizar entregables técnicos como diccionario de datos, reportes y anexos.", 80)
    );

    private final List<DiagramCategory> categories;
    private final Map<DiagramCategoryId, DiagramCategory> categoriesById;

    public DefaultDiagramCategoryCatalog() {
        this(OFFICIAL_CATEGORIES);
    }

    public DefaultDiagramCategoryCatalog(List<DiagramCategory> categories) {
        this.categories = List.copyOf(Objects.requireNonNull(categories, "categories"));
        this.categoriesById = indexById(this.categories);
    }

    @Override
    public List<DiagramCategory> findAll() {
        return categories;
    }

    @Override
    public Optional<DiagramCategory> findById(DiagramCategoryId id) {
        Objects.requireNonNull(id, "id");
        return Optional.ofNullable(categoriesById.get(id));
    }

    private static DiagramCategory category(DiagramCategoryId id, String name, String purpose, int order) {
        return new DiagramCategory(id, name, purpose, order);
    }

    private static Map<DiagramCategoryId, DiagramCategory> indexById(List<DiagramCategory> categories) {
        Map<DiagramCategoryId, DiagramCategory> indexed = categories.stream()
                .collect(Collectors.toMap(
                        DiagramCategory::id,
                        Function.identity(),
                        (left, right) -> {
                            throw new IllegalArgumentException("Categoría duplicada: " + left.id().value());
                        },
                        LinkedHashMap::new));
        return Map.copyOf(indexed);
    }
}
