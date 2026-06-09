package com.marcosmoreira.domainmodelstudio.presentation.newproject;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategory;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Modelo sin JavaFX para poblar el diálogo Nuevo proyecto categorizado. */
public final class NewProjectDialogViewModel {

    private final List<NewProjectCategoryViewModel> categories;
    private final Map<String, List<NewProjectTypeCardViewModel>> typeCardsByCategoryId;
    private final Map<String, DiagramTypeDescriptor> descriptorsByTypeId;

    private NewProjectDialogViewModel(
            List<NewProjectCategoryViewModel> categories,
            Map<String, List<NewProjectTypeCardViewModel>> typeCardsByCategoryId,
            Map<String, DiagramTypeDescriptor> descriptorsByTypeId
    ) {
        this.categories = List.copyOf(categories);
        this.typeCardsByCategoryId = copyCardMap(typeCardsByCategoryId);
        this.descriptorsByTypeId = Map.copyOf(descriptorsByTypeId);
    }

    public static NewProjectDialogViewModel from(
            List<DiagramCategory> categories,
            List<DiagramTypeDescriptor> diagramTypes
    ) {
        Objects.requireNonNull(categories, "categories");
        Objects.requireNonNull(diagramTypes, "diagramTypes");

        List<DiagramCategory> orderedCategories = categories.stream()
                .sorted(Comparator.comparingInt(DiagramCategory::order))
                .toList();
        Map<String, List<NewProjectTypeCardViewModel>> cardsByCategory = new LinkedHashMap<>();
        Map<String, DiagramTypeDescriptor> descriptorsByTypeId = new LinkedHashMap<>();

        List<NewProjectCategoryViewModel> categoryCards = orderedCategories.stream()
                .map(NewProjectDialogViewModel::toCategoryViewModel)
                .toList();

        for (DiagramCategory category : orderedCategories) {
            cardsByCategory.put(category.id().value(), cardsFor(category.id(), diagramTypes, descriptorsByTypeId));
        }

        return new NewProjectDialogViewModel(categoryCards, cardsByCategory, descriptorsByTypeId);
    }

    public List<NewProjectCategoryViewModel> categories() {
        return categories;
    }

    public List<NewProjectTypeCardViewModel> typesFor(String categoryId) {
        Objects.requireNonNull(categoryId, "categoryId");
        return typeCardsByCategoryId.getOrDefault(categoryId, List.of());
    }

    public Optional<DiagramTypeDescriptor> descriptorFor(String diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        return Optional.ofNullable(descriptorsByTypeId.get(diagramTypeId));
    }

    public Optional<NewProjectCategoryViewModel> initialCategory() {
        return categories.stream().findFirst();
    }

    public Optional<NewProjectTypeCardViewModel> initialTypeFor(String categoryId) {
        return typesFor(categoryId).stream().filter(NewProjectTypeCardViewModel::selectable).findFirst()
                .or(() -> typesFor(categoryId).stream().findFirst());
    }

    private static NewProjectCategoryViewModel toCategoryViewModel(DiagramCategory category) {
        return new NewProjectCategoryViewModel(
                category.id().value(),
                category.displayName(),
                category.purpose(),
                category.order());
    }

    private static List<NewProjectTypeCardViewModel> cardsFor(
            DiagramCategoryId categoryId,
            List<DiagramTypeDescriptor> diagramTypes,
            Map<String, DiagramTypeDescriptor> descriptorsByTypeId
    ) {
        List<NewProjectTypeCardViewModel> cards = new ArrayList<>();
        for (DiagramTypeDescriptor descriptor : diagramTypes) {
            if (!descriptor.categoryId().equals(categoryId)) {
                continue;
            }
            descriptorsByTypeId.put(descriptor.id().value(), descriptor);
            cards.add(toTypeCard(descriptor));
        }
        return List.copyOf(cards);
    }

    private static NewProjectTypeCardViewModel toTypeCard(DiagramTypeDescriptor descriptor) {
        return new NewProjectTypeCardViewModel(
                descriptor.id().value(),
                descriptor.displayName(),
                statusLabel(descriptor.supportStatus()),
                descriptor.shortDescription(),
                visibleCapabilities(descriptor),
                selectable(descriptor));
    }

    private static boolean selectable(DiagramTypeDescriptor descriptor) {
        return descriptor.supports(DiagramCapability.CREATE_PROJECT)
                || descriptor.supports(DiagramCapability.PLANNING_VIEW);
    }

    private static List<String> visibleCapabilities(DiagramTypeDescriptor descriptor) {
        List<String> labels = new ArrayList<>();
        if (descriptor.supports(DiagramCapability.CREATE_PROJECT)) {
            labels.add("Crear proyecto editable");
        }
        if (descriptor.supports(DiagramCapability.PLANNING_VIEW)) {
            labels.add("Abrir guía de preparación");
        }
        if (descriptor.supports(DiagramCapability.IMPORT_MARKDOWN)) {
            labels.add("Importa Markdown");
        }
        if (descriptor.supports(DiagramCapability.SHOW_VISUAL_OUTPUT)) {
            labels.add("Diagrama exportable");
        }
        if (descriptor.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT)) {
            labels.add("Documento exportable");
        }
        if (descriptor.supports(DiagramCapability.EXPORT_MARKDOWN)) {
            labels.add("Exporta Markdown");
        }
        if (descriptor.supports(DiagramCapability.AI_RESOURCES)) {
            labels.add("Plantillas para IA");
        }
        if (descriptor.supports(DiagramCapability.THEORY_HELP)) {
            labels.add("Ayuda teórica");
        }
        if (!descriptor.supports(DiagramCapability.CREATE_PROJECT)) {
            labels.add("Solo referencia");
        }
        return List.copyOf(labels);
    }

    private static String statusLabel(DiagramSupportStatus status) {
        return switch (status) {
            case AVAILABLE -> "Disponible";
            case IN_PREPARATION -> "En preparación";
            case DOCUMENTATION_AVAILABLE -> "Referencia disponible";
            case TEMPLATE_AVAILABLE -> "Plantilla disponible";
            case EXPERIMENTAL -> "Experimental";
        };
    }

    private static Map<String, List<NewProjectTypeCardViewModel>> copyCardMap(
            Map<String, List<NewProjectTypeCardViewModel>> source
    ) {
        Map<String, List<NewProjectTypeCardViewModel>> copy = new LinkedHashMap<>();
        for (Map.Entry<String, List<NewProjectTypeCardViewModel>> entry : source.entrySet()) {
            copy.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        return Map.copyOf(copy);
    }
}
