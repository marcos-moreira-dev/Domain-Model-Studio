package com.marcosmoreira.domainmodelstudio.presentation.newproject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramCategoryCatalog;
import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class NewProjectDialogViewModelTest {

    @Test
    void groupsDiagramTypesByOfficialCategory() {
        NewProjectDialogViewModel model = NewProjectDialogViewModel.from(
                new DefaultDiagramCategoryCatalog().findAll(),
                new DefaultDiagramTypeRegistry().findAll()
        );

        assertFalse(model.categories().isEmpty());
        assertTrue(model.typesFor(DiagramCategoryId.DATA_MODELING.value()).stream()
                .anyMatch(type -> DiagramTypeId.CONCEPTUAL_MODEL.value().equals(type.id())));
        assertTrue(model.typesFor(DiagramCategoryId.ADMIN_APPLICATIONS.value()).stream()
                .anyMatch(type -> DiagramTypeId.ADMIN_WIREFRAMES.value().equals(type.id())));
    }

    @Test
    void doesNotExposePostgresqlErdOrLogicalRelationalModelInNewProjectDialog() {
        NewProjectDialogViewModel model = NewProjectDialogViewModel.from(
                new DefaultDiagramCategoryCatalog().findAll(),
                new DefaultDiagramTypeRegistry().findAll()
        );

        assertTrue(model.typesFor(DiagramCategoryId.DATA_MODELING.value()).stream()
                .noneMatch(type -> "postgresql-physical-erd".equals(type.id())));
        assertTrue(model.typesFor(DiagramCategoryId.DATA_MODELING.value()).stream()
                .noneMatch(type -> "logical-relational-model".equals(type.id())));
    }

    @Test
    void sequenceDiagramExposesEditableVisualProject() {
        NewProjectDialogViewModel model = NewProjectDialogViewModel.from(
                new DefaultDiagramCategoryCatalog().findAll(),
                new DefaultDiagramTypeRegistry().findAll()
        );

        NewProjectTypeCardViewModel sequenceDiagram = model.typesFor(DiagramCategoryId.UML_INTERACTION.value()).stream()
                .filter(type -> DiagramTypeId.UML_SEQUENCE.value().equals(type.id()))
                .findFirst()
                .orElseThrow();

        assertTrue(sequenceDiagram.selectable());
        assertTrue(sequenceDiagram.capabilities().contains("Crear proyecto editable"));
        assertTrue(sequenceDiagram.capabilities().contains("Diagrama exportable"));
        assertTrue(sequenceDiagram.capabilities().contains("Exporta Markdown"));
        assertFalse(sequenceDiagram.capabilities().contains("Abrir guía de preparación"));
    }

    @Test
    void availableTypesExposeEditableProjectAndExportableOutput() {
        NewProjectDialogViewModel model = NewProjectDialogViewModel.from(
                new DefaultDiagramCategoryCatalog().findAll(),
                new DefaultDiagramTypeRegistry().findAll()
        );

        NewProjectTypeCardViewModel conceptual = model.typesFor(DiagramCategoryId.DATA_MODELING.value()).stream()
                .filter(type -> DiagramTypeId.CONCEPTUAL_MODEL.value().equals(type.id()))
                .findFirst()
                .orElseThrow();

        assertTrue(conceptual.selectable());
        assertTrue(conceptual.capabilities().contains("Crear proyecto editable"));
        assertTrue(conceptual.capabilities().contains("Diagrama exportable"));
        assertTrue(conceptual.capabilities().contains("Exporta Markdown"));
        assertFalse(conceptual.capabilities().contains("Abrir guía de preparación"));
    }



    @Test
    void moduleMapExposesEditableVisualProject() {
        NewProjectDialogViewModel model = NewProjectDialogViewModel.from(
                new DefaultDiagramCategoryCatalog().findAll(),
                new DefaultDiagramTypeRegistry().findAll()
        );

        NewProjectTypeCardViewModel moduleMap = model.typesFor(DiagramCategoryId.ADMIN_APPLICATIONS.value()).stream()
                .filter(type -> DiagramTypeId.ADMIN_MODULE_MAP.value().equals(type.id()))
                .findFirst()
                .orElseThrow();

        assertTrue(moduleMap.selectable());
        assertTrue(moduleMap.capabilities().contains("Crear proyecto editable"));
        assertTrue(moduleMap.capabilities().contains("Diagrama exportable"));
        assertTrue(moduleMap.capabilities().contains("Exporta Markdown"));
        assertFalse(moduleMap.capabilities().contains("Abrir guía de preparación"));
    }


    @Test
    void wireframesExposeEditableVisualProject() {
        NewProjectDialogViewModel model = NewProjectDialogViewModel.from(
                new DefaultDiagramCategoryCatalog().findAll(),
                new DefaultDiagramTypeRegistry().findAll()
        );

        NewProjectTypeCardViewModel wireframes = model.typesFor(DiagramCategoryId.ADMIN_APPLICATIONS.value()).stream()
                .filter(type -> DiagramTypeId.ADMIN_WIREFRAMES.value().equals(type.id()))
                .findFirst()
                .orElseThrow();

        assertTrue(wireframes.selectable());
        assertTrue(wireframes.capabilities().contains("Crear proyecto editable"));
        assertTrue(wireframes.capabilities().contains("Diagrama exportable"));
        assertTrue(wireframes.capabilities().contains("Exporta Markdown"));
        assertFalse(wireframes.capabilities().contains("Abrir guía de preparación"));
    }

    @Test
    void umlClassExposesEditableVisualProject() {
        NewProjectDialogViewModel model = NewProjectDialogViewModel.from(
                new DefaultDiagramCategoryCatalog().findAll(),
                new DefaultDiagramTypeRegistry().findAll()
        );

        NewProjectTypeCardViewModel umlClass = model.typesFor(DiagramCategoryId.UML_STRUCTURAL.value()).stream()
                .filter(type -> DiagramTypeId.UML_CLASS.value().equals(type.id()))
                .findFirst()
                .orElseThrow();

        assertTrue(umlClass.selectable());
        assertTrue(umlClass.capabilities().contains("Crear proyecto editable"));
        assertTrue(umlClass.capabilities().contains("Diagrama exportable"));
        assertTrue(umlClass.capabilities().contains("Exporta Markdown"));
        assertFalse(umlClass.capabilities().contains("Abrir guía de preparación"));
    }

    @Test
    void resolvesDescriptorFromSelectedCardId() {
        NewProjectDialogViewModel model = NewProjectDialogViewModel.from(
                new DefaultDiagramCategoryCatalog().findAll(),
                new DefaultDiagramTypeRegistry().findAll()
        );

        assertTrue(model.descriptorFor(DiagramTypeId.CONCEPTUAL_MODEL.value()).orElseThrow().isAvailable());
    }
}
