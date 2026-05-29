package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Catálogo oficial de tipos de diagrama visible definido desde la fuente única de producto. */
public final class DefaultDiagramTypeRegistry implements DiagramTypeRegistry {

    private static final List<DiagramTypeDescriptor> OFFICIAL_TYPES = DefaultDiagramTypeDefinitions.all().stream()
            .map(DefaultDiagramTypeRegistry::toDescriptor)
            .toList();

    private final List<DiagramTypeDescriptor> types;
    private final Map<DiagramTypeId, DiagramTypeDescriptor> typesById;
    private final DiagramCatalogConsistencyValidator consistencyValidator = new DiagramCatalogConsistencyValidator();

    public DefaultDiagramTypeRegistry() {
        this(OFFICIAL_TYPES);
    }

    public DefaultDiagramTypeRegistry(List<DiagramTypeDescriptor> types) {
        this.types = List.copyOf(Objects.requireNonNull(types, "types"));
        this.typesById = indexById(this.types);
        this.consistencyValidator.validate(this.types);
    }

    @Override
    public List<DiagramTypeDescriptor> findAll() {
        return types;
    }

    @Override
    public List<DiagramTypeDescriptor> findByCategory(DiagramCategoryId categoryId) {
        Objects.requireNonNull(categoryId, "categoryId");
        return types.stream()
                .filter(type -> type.categoryId().equals(categoryId))
                .toList();
    }

    @Override
    public Optional<DiagramTypeDescriptor> findById(DiagramTypeId id) {
        Objects.requireNonNull(id, "id");
        return Optional.ofNullable(typesById.get(id));
    }

    private static DiagramTypeDescriptor toDescriptor(DiagramTypeOfficialDefinition definition) {
        return new DiagramTypeDescriptor(
                definition.id(),
                definition.displayName(),
                definition.categoryId(),
                definition.supportStatus(),
                definition.capabilities(),
                definition.workspaceKind(),
                definition.shortDescription(),
                definition.theoryTopicId(),
                definition.grammarResourceId(),
                definition.toolbarPolicyId(),
                definition.minimalExampleResource(),
                definition.officialExampleResource());
    }

    private static Map<DiagramTypeId, DiagramTypeDescriptor> indexById(List<DiagramTypeDescriptor> types) {
        Map<DiagramTypeId, DiagramTypeDescriptor> indexed = types.stream()
                .collect(Collectors.toMap(
                        DiagramTypeDescriptor::id,
                        Function.identity(),
                        (left, right) -> {
                            throw new IllegalArgumentException("Tipo de diagrama duplicado: " + left.id().value());
                        },
                        LinkedHashMap::new));
        return Map.copyOf(indexed);
    }
}
