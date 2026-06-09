package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.RemoveUmlClassDiagramItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlClassDiagramValidationResult;
import com.marcosmoreira.domainmodelstudio.application.umlclass.ValidateUmlClassDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

/** Encapsula las acciones de edición CRUD del modelo UML Clases. */
final class UmlClassEditingController {
    private final AddUmlModuleUseCase addModuleUseCase;
    private final AddUmlClassUseCase addClassUseCase;
    private final AddUmlMemberUseCase addMemberUseCase;
    private final AddUmlRelationUseCase addRelationUseCase;
    private final UpdateUmlModuleUseCase updateModuleUseCase;
    private final UpdateUmlClassUseCase updateClassUseCase;
    private final UpdateUmlMemberUseCase updateMemberUseCase;
    private final UpdateUmlRelationUseCase updateRelationUseCase;
    private final RemoveUmlClassDiagramItemUseCase removeItemUseCase;
    private final ValidateUmlClassDiagramUseCase validateUseCase;
    private final Supplier<UmlClassDiagramDocument> documentSupplier;
    private final BiConsumer<UmlClassDiagramDocument, String> documentApplier;
    private final Consumer<String> statusConsumer;
    private final ObservableList<UmlModuleGroup> modules;
    private final ObservableList<UmlClassNode> classes;
    private final ObservableList<UmlClassMember> members;
    private final ObservableList<UmlClassRelation> relations;
    private final ObjectProperty<UmlModuleGroup> selectedModule;
    private final ObjectProperty<UmlClassNode> selectedClass;
    private final ObjectProperty<UmlClassMember> selectedMember;
    private final ObjectProperty<UmlClassRelation> selectedRelation;

    UmlClassEditingController(UmlClassEditingDependencies dependencies, UmlClassEditingContext context) {
        this.addModuleUseCase = dependencies.addModuleUseCase();
        this.addClassUseCase = dependencies.addClassUseCase();
        this.addMemberUseCase = dependencies.addMemberUseCase();
        this.addRelationUseCase = dependencies.addRelationUseCase();
        this.updateModuleUseCase = dependencies.updateModuleUseCase();
        this.updateClassUseCase = dependencies.updateClassUseCase();
        this.updateMemberUseCase = dependencies.updateMemberUseCase();
        this.updateRelationUseCase = dependencies.updateRelationUseCase();
        this.removeItemUseCase = dependencies.removeItemUseCase();
        this.validateUseCase = dependencies.validateUseCase();
        this.documentSupplier = context.documentSupplier();
        this.documentApplier = context.documentApplier();
        this.statusConsumer = context.statusConsumer();
        this.modules = context.modules();
        this.classes = context.classes();
        this.members = context.members();
        this.relations = context.relations();
        this.selectedModule = context.selectedModule();
        this.selectedClass = context.selectedClass();
        this.selectedMember = context.selectedMember();
        this.selectedRelation = context.selectedRelation();
    }

    void addModule() {
        UmlClassDiagramDocument document = requireDocument();
        apply(addModuleUseCase.add(document, "Módulo"), "Módulo UML agregado.");
        if (!modules.isEmpty()) selectedModule.set(modules.get(modules.size() - 1));
    }

    void addClass(UmlClassKind kind) {
        UmlClassDiagramDocument document = requireDocument();
        String moduleId = selectedModule.get() == null ? "" : selectedModule.get().id();
        apply(addClassUseCase.add(document, moduleId, defaultClassName(kind), kind), "Clase UML agregada.");
        if (!classes.isEmpty()) selectedClass.set(classes.get(classes.size() - 1));
    }

    void addAttribute() { addMember(UmlMemberKind.ATTRIBUTE); }

    void addMethod() { addMember(UmlMemberKind.METHOD); }

    void addRelation() {
        UmlClassDiagramDocument document = requireDocument();
        if (classes.size() < 2) {
            statusConsumer.accept("Se necesitan al menos dos clases para crear una relación.");
            return;
        }
        String source = selectedClass.get() == null ? classes.get(0).id() : selectedClass.get().id();
        String target = classes.stream().filter(node -> !node.id().equals(source)).findFirst().orElse(classes.get(0)).id();
        apply(addRelationUseCase.add(document, source, target, UmlRelationKind.DEPENDENCY), "Relación UML agregada.");
        if (!relations.isEmpty()) selectedRelation.set(relations.get(relations.size() - 1));
    }

    void removeSelected() {
        UmlClassDiagramDocument document = requireDocument();
        if (selectedMember.get() != null) {
            apply(removeItemUseCase.removeMember(document, selectedMember.get().id()), "Miembro UML eliminado.");
            selectedMember.set(null);
        } else if (selectedRelation.get() != null) {
            apply(removeItemUseCase.removeRelation(document, selectedRelation.get().id()), "Relación UML eliminada.");
            selectedRelation.set(null);
        } else if (selectedClass.get() != null) {
            apply(removeItemUseCase.removeClass(document, selectedClass.get().id()), "Clase UML eliminada.");
            selectedClass.set(classes.isEmpty() ? null : classes.get(0));
        } else if (selectedModule.get() != null) {
            apply(removeItemUseCase.removeModule(document, selectedModule.get().id()), "Módulo UML eliminado.");
            selectedModule.set(modules.isEmpty() ? null : modules.get(0));
        } else {
            statusConsumer.accept("Selecciona un módulo, clase, miembro o relación para eliminar.");
        }
    }

    void applyModuleChanges(String displayName, String path, String description, String notes) {
        if (selectedModule.get() == null) return;
        String previousId = selectedModule.get().id();
        apply(updateModuleUseCase.update(requireDocument(), previousId, displayName, path, description, notes),
                "Módulo UML actualizado.");
        selectModuleById(previousId);
    }

    void applyClassChanges(UmlModuleGroup module, String displayName, String packageName, UmlClassKind kind,
                           UmlVisibility visibility, String responsibility, String description, String notes) {
        if (selectedClass.get() == null) return;
        String previousId = selectedClass.get().id();
        String moduleId = module == null ? "" : module.id();
        apply(updateClassUseCase.update(requireDocument(), previousId, moduleId, displayName, packageName,
                kind, visibility, responsibility, description, notes), "Clase UML actualizada.");
        selectClassById(previousId);
    }

    void applyMemberChanges(UmlMemberKind kind, String name, String type, String signature,
                            UmlVisibility visibility, boolean staticMember, String description) {
        if (selectedMember.get() == null) return;
        String previousId = selectedMember.get().id();
        apply(updateMemberUseCase.update(requireDocument(), previousId, kind, name, type, signature,
                visibility, staticMember, description), "Miembro UML actualizado.");
        selectMemberById(previousId);
    }

    void applyRelationChanges(UmlClassNode source, UmlClassNode target, UmlRelationKind kind,
                              String label, String description, String notes) {
        if (selectedRelation.get() == null || source == null || target == null) return;
        String previousId = selectedRelation.get().id();
        apply(updateRelationUseCase.update(requireDocument(), previousId, source.id(), target.id(), kind,
                label, description, notes), "Relación UML actualizada.");
        selectRelationById(previousId);
    }

    UmlClassDiagramValidationResult validateDocument() {
        UmlClassDiagramValidationResult result = validateUseCase.validate(requireDocument());
        statusConsumer.accept(result.summary());
        return result;
    }

    private void addMember(UmlMemberKind kind) {
        UmlClassDiagramDocument document = requireDocument();
        if (selectedClass.get() == null) {
            statusConsumer.accept("Selecciona una clase antes de agregar atributos o métodos.");
            return;
        }
        String classId = selectedClass.get().id();
        apply(addMemberUseCase.add(document, classId, kind),
                kind == UmlMemberKind.METHOD ? "Método agregado." : "Atributo agregado.");
        selectClassById(classId);
        if (!members.isEmpty()) selectedMember.set(members.get(members.size() - 1));
    }

    private void apply(UmlClassDiagramDocument updated, String message) {
        documentApplier.accept(Objects.requireNonNull(updated, "updated"), message);
    }

    private UmlClassDiagramDocument requireDocument() {
        UmlClassDiagramDocument document = documentSupplier.get();
        if (document == null) {
            throw new IllegalStateException("No hay diagrama UML Clases activo.");
        }
        return document;
    }

    private void selectModuleById(String id) {
        modules.stream().filter(module -> module.id().equals(id)).findFirst().ifPresent(selectedModule::set);
    }

    private void selectClassById(String id) {
        classes.stream().filter(node -> node.id().equals(id)).findFirst().ifPresent(selectedClass::set);
        refreshMembersForSelection();
    }

    private void selectMemberById(String id) {
        refreshMembersForSelection();
        members.stream().filter(member -> member.id().equals(id)).findFirst().ifPresent(selectedMember::set);
    }

    private void selectRelationById(String id) {
        relations.stream().filter(relation -> relation.id().equals(id)).findFirst().ifPresent(selectedRelation::set);
    }

    private void refreshMembersForSelection() {
        UmlClassNode node = selectedClass.get();
        members.setAll(node == null ? java.util.List.of() : node.members());
    }

    private String defaultClassName(UmlClassKind kind) {
        return switch (kind == null ? UmlClassKind.CLASS : kind) {
            case INTERFACE -> "Interfaz";
            case ENUM -> "Enum";
            default -> "Clase";
        };
    }

    record UmlClassEditingDependencies(
            AddUmlModuleUseCase addModuleUseCase,
            AddUmlClassUseCase addClassUseCase,
            AddUmlMemberUseCase addMemberUseCase,
            AddUmlRelationUseCase addRelationUseCase,
            UpdateUmlModuleUseCase updateModuleUseCase,
            UpdateUmlClassUseCase updateClassUseCase,
            UpdateUmlMemberUseCase updateMemberUseCase,
            UpdateUmlRelationUseCase updateRelationUseCase,
            RemoveUmlClassDiagramItemUseCase removeItemUseCase,
            ValidateUmlClassDiagramUseCase validateUseCase
    ) { }

    record UmlClassEditingContext(
            Supplier<UmlClassDiagramDocument> documentSupplier,
            BiConsumer<UmlClassDiagramDocument, String> documentApplier,
            Consumer<String> statusConsumer,
            ObservableList<UmlModuleGroup> modules,
            ObservableList<UmlClassNode> classes,
            ObservableList<UmlClassMember> members,
            ObservableList<UmlClassRelation> relations,
            ObjectProperty<UmlModuleGroup> selectedModule,
            ObjectProperty<UmlClassNode> selectedClass,
            ObjectProperty<UmlClassMember> selectedMember,
            ObjectProperty<UmlClassRelation> selectedRelation
    ) { }
}
