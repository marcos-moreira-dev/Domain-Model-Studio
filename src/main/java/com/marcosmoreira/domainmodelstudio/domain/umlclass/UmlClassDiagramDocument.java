package com.marcosmoreira.domainmodelstudio.domain.umlclass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Documento semántico del diagrama UML Clases con agrupadores por módulo/carpeta. */
public record UmlClassDiagramDocument(
        String projectName,
        String version,
        LocalDate documentDate,
        List<UmlModuleGroup> modules,
        List<UmlClassNode> classes,
        List<UmlClassRelation> relations,
        List<UmlClassDiagramView> views,
        String notes
) {
    public UmlClassDiagramDocument {
        projectName = normalizeOrDefault(projectName, "UML Clases");
        version = normalizeOrDefault(version, "borrador");
        documentDate = documentDate == null ? LocalDate.now() : documentDate;
        modules = List.copyOf(modules == null ? List.of() : modules);
        classes = List.copyOf(classes == null ? List.of() : classes);
        relations = List.copyOf(relations == null ? List.of() : relations);
        views = List.copyOf(views == null ? List.of() : views);
        notes = normalize(notes);
        validateReferences(modules, classes, relations, views);
    }

    public UmlClassDiagramDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            List<UmlModuleGroup> modules,
            List<UmlClassNode> classes,
            List<UmlClassRelation> relations,
            String notes
    ) {
        this(projectName, version, documentDate, modules, classes, relations, List.of(), notes);
    }

    public static UmlClassDiagramDocument blank(String projectName) {
        return new UmlClassDiagramDocument(projectName, "borrador", LocalDate.now(), List.of(), List.of(), List.of(), List.of(), "");
    }

    public Optional<UmlModuleGroup> moduleById(String moduleId) {
        String normalized = normalize(moduleId);
        return modules.stream().filter(module -> module.id().equals(normalized)).findFirst();
    }

    public Optional<UmlClassNode> classById(String classId) {
        String normalized = normalize(classId);
        return classes.stream().filter(node -> node.id().equals(normalized)).findFirst();
    }

    public Optional<UmlClassRelation> relationById(String relationId) {
        String normalized = normalize(relationId);
        return relations.stream().filter(relation -> relation.id().equals(normalized)).findFirst();
    }

    public Optional<UmlClassDiagramView> viewById(String viewId) {
        String normalized = normalize(viewId);
        return views.stream().filter(view -> view.id().equals(normalized)).findFirst();
    }

    public Optional<UmlClassNode> classOwningMember(String memberId) {
        String normalized = normalize(memberId);
        return classes.stream().filter(node -> node.memberById(normalized).isPresent()).findFirst();
    }

    public UmlClassDiagramDocument withModule(UmlModuleGroup module) {
        Objects.requireNonNull(module, "module");
        if (moduleById(module.id()).isPresent()) {
            throw new IllegalArgumentException("Ya existe el módulo UML: " + module.id());
        }
        ArrayList<UmlModuleGroup> updated = new ArrayList<>(modules);
        updated.add(module);
        return new UmlClassDiagramDocument(projectName, version, documentDate, updated, classes, relations, views, notes);
    }

    public UmlClassDiagramDocument withUpdatedModule(UmlModuleGroup module) {
        ArrayList<UmlModuleGroup> updated = new ArrayList<>();
        boolean replaced = false;
        for (UmlModuleGroup current : modules) {
            if (current.id().equals(module.id())) {
                updated.add(module);
                replaced = true;
            } else {
                updated.add(current);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe módulo UML para actualizar: " + module.id());
        }
        return new UmlClassDiagramDocument(projectName, version, documentDate, updated, classes, relations, views, notes);
    }

    public UmlClassDiagramDocument withoutModule(String moduleId) {
        String normalized = normalize(moduleId);
        List<UmlModuleGroup> updatedModules = modules.stream().filter(module -> !module.id().equals(normalized)).toList();
        if (updatedModules.size() == modules.size()) {
            throw new IllegalArgumentException("No existe módulo UML para eliminar: " + normalized);
        }
        List<UmlClassNode> updatedClasses = classes.stream().filter(node -> !node.moduleId().equals(normalized)).toList();
        java.util.Set<String> remainingClassIds = updatedClasses.stream().map(UmlClassNode::id).collect(java.util.stream.Collectors.toSet());
        List<UmlClassRelation> updatedRelations = relations.stream()
                .filter(relation -> remainingClassIds.contains(relation.sourceClassId()))
                .filter(relation -> remainingClassIds.contains(relation.targetClassId()))
                .toList();
        List<UmlClassDiagramView> updatedViews = sanitizeViews(views, updatedModules, updatedClasses, updatedRelations);
        return new UmlClassDiagramDocument(projectName, version, documentDate, updatedModules, updatedClasses, updatedRelations, updatedViews, notes);
    }

    public UmlClassDiagramDocument withClass(UmlClassNode node) {
        Objects.requireNonNull(node, "node");
        if (classById(node.id()).isPresent()) {
            throw new IllegalArgumentException("Ya existe la clase UML: " + node.id());
        }
        ArrayList<UmlClassNode> updated = new ArrayList<>(classes);
        updated.add(node);
        return new UmlClassDiagramDocument(projectName, version, documentDate, modules, updated, relations, views, notes);
    }

    public UmlClassDiagramDocument withUpdatedClass(UmlClassNode node) {
        ArrayList<UmlClassNode> updated = new ArrayList<>();
        boolean replaced = false;
        for (UmlClassNode current : classes) {
            if (current.id().equals(node.id())) {
                updated.add(node);
                replaced = true;
            } else {
                updated.add(current);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe clase UML para actualizar: " + node.id());
        }
        return new UmlClassDiagramDocument(projectName, version, documentDate, modules, updated, relations, views, notes);
    }

    public UmlClassDiagramDocument withoutClass(String classId) {
        String normalized = normalize(classId);
        List<UmlClassNode> updatedClasses = classes.stream().filter(node -> !node.id().equals(normalized)).toList();
        if (updatedClasses.size() == classes.size()) {
            throw new IllegalArgumentException("No existe clase UML para eliminar: " + normalized);
        }
        List<UmlClassRelation> updatedRelations = relations.stream()
                .filter(relation -> !relation.sourceClassId().equals(normalized))
                .filter(relation -> !relation.targetClassId().equals(normalized))
                .toList();
        List<UmlClassDiagramView> updatedViews = sanitizeViews(views, modules, updatedClasses, updatedRelations);
        return new UmlClassDiagramDocument(projectName, version, documentDate, modules, updatedClasses, updatedRelations, updatedViews, notes);
    }

    public UmlClassDiagramDocument withRelation(UmlClassRelation relation) {
        Objects.requireNonNull(relation, "relation");
        if (relationById(relation.id()).isPresent()) {
            throw new IllegalArgumentException("Ya existe la relación UML: " + relation.id());
        }
        ArrayList<UmlClassRelation> updated = new ArrayList<>(relations);
        updated.add(relation);
        return new UmlClassDiagramDocument(projectName, version, documentDate, modules, classes, updated, views, notes);
    }

    public UmlClassDiagramDocument withUpdatedRelation(UmlClassRelation relation) {
        ArrayList<UmlClassRelation> updated = new ArrayList<>();
        boolean replaced = false;
        for (UmlClassRelation current : relations) {
            if (current.id().equals(relation.id())) {
                updated.add(relation);
                replaced = true;
            } else {
                updated.add(current);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe relación UML para actualizar: " + relation.id());
        }
        return new UmlClassDiagramDocument(projectName, version, documentDate, modules, classes, updated, views, notes);
    }

    public UmlClassDiagramDocument withoutRelation(String relationId) {
        String normalized = normalize(relationId);
        List<UmlClassRelation> updated = relations.stream().filter(relation -> !relation.id().equals(normalized)).toList();
        if (updated.size() == relations.size()) {
            throw new IllegalArgumentException("No existe relación UML para eliminar: " + normalized);
        }
        List<UmlClassDiagramView> updatedViews = sanitizeViews(views, modules, classes, updated);
        return new UmlClassDiagramDocument(projectName, version, documentDate, modules, classes, updated, updatedViews, notes);
    }

    public UmlClassDiagramDocument withView(UmlClassDiagramView view) {
        Objects.requireNonNull(view, "view");
        if (viewById(view.id()).isPresent()) {
            throw new IllegalArgumentException("Ya existe la vista UML: " + view.id());
        }
        ArrayList<UmlClassDiagramView> updated = new ArrayList<>(views);
        updated.add(view);
        return new UmlClassDiagramDocument(projectName, version, documentDate, modules, classes, relations, updated, notes);
    }

    public UmlClassDiagramDocument withUpdatedView(UmlClassDiagramView view) {
        Objects.requireNonNull(view, "view");
        ArrayList<UmlClassDiagramView> updated = new ArrayList<>();
        boolean replaced = false;
        for (UmlClassDiagramView current : views) {
            if (current.id().equals(view.id())) {
                updated.add(view);
                replaced = true;
            } else {
                updated.add(current);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe vista UML para actualizar: " + view.id());
        }
        return new UmlClassDiagramDocument(projectName, version, documentDate, modules, classes, relations, updated, notes);
    }

    public UmlClassDiagramDocument withoutView(String viewId) {
        String normalized = normalize(viewId);
        List<UmlClassDiagramView> updated = views.stream().filter(view -> !view.id().equals(normalized)).toList();
        if (updated.size() == views.size()) {
            throw new IllegalArgumentException("No existe vista UML para eliminar: " + normalized);
        }
        return new UmlClassDiagramDocument(projectName, version, documentDate, modules, classes, relations, updated, notes);
    }

    public List<UmlModuleGroup> modulesForView(String viewId) {
        return viewById(viewId).map(view -> modules.stream().filter(view::includesModule).toList()).orElse(List.of());
    }

    public List<UmlClassNode> classesForView(String viewId) {
        return viewById(viewId).map(view -> classes.stream().filter(view::includesClass).toList()).orElse(List.of());
    }

    public List<UmlClassRelation> relationsForView(String viewId) {
        return viewById(viewId).map(view -> relations.stream().filter(view::includesRelation).toList()).orElse(List.of());
    }

    public int moduleCount() { return modules.size(); }
    public int classCount() { return classes.size(); }
    public int relationCount() { return relations.size(); }
    public int viewCount() { return views.size(); }
    public int memberCount() { return classes.stream().mapToInt(node -> node.members().size()).sum(); }

    private static void validateReferences(List<UmlModuleGroup> modules, List<UmlClassNode> classes,
                                           List<UmlClassRelation> relations, List<UmlClassDiagramView> views) {
        java.util.Set<String> moduleIds = modules.stream().map(UmlModuleGroup::id).collect(java.util.stream.Collectors.toSet());
        for (UmlClassNode node : classes) {
            if (!node.moduleId().isBlank() && !moduleIds.contains(node.moduleId())) {
                throw new IllegalArgumentException("La clase '" + node.displayName() + "' apunta a un módulo inexistente: " + node.moduleId());
            }
        }
        java.util.Set<String> classIds = classes.stream().map(UmlClassNode::id).collect(java.util.stream.Collectors.toSet());
        java.util.Set<String> relationIds = relations.stream().map(UmlClassRelation::id).collect(java.util.stream.Collectors.toSet());
        for (UmlClassRelation relation : relations) {
            if (!classIds.contains(relation.sourceClassId())) {
                throw new IllegalArgumentException("Relación UML con clase origen inexistente: " + relation.sourceClassId());
            }
            if (!classIds.contains(relation.targetClassId())) {
                throw new IllegalArgumentException("Relación UML con clase destino inexistente: " + relation.targetClassId());
            }
        }
        for (UmlClassDiagramView view : views) {
            for (String moduleId : view.moduleIds()) {
                if (!moduleIds.contains(moduleId)) {
                    throw new IllegalArgumentException("Vista UML con módulo inexistente: " + moduleId);
                }
            }
            for (String classId : view.classIds()) {
                if (!classIds.contains(classId)) {
                    throw new IllegalArgumentException("Vista UML con clase inexistente: " + classId);
                }
            }
            for (String relationId : view.relationIds()) {
                if (!relationIds.contains(relationId)) {
                    throw new IllegalArgumentException("Vista UML con relación inexistente: " + relationId);
                }
            }
        }
    }

    private static List<UmlClassDiagramView> sanitizeViews(List<UmlClassDiagramView> views, List<UmlModuleGroup> modules,
                                                          List<UmlClassNode> classes, List<UmlClassRelation> relations) {
        java.util.Set<String> moduleIds = modules.stream().map(UmlModuleGroup::id).collect(java.util.stream.Collectors.toSet());
        java.util.Set<String> classIds = classes.stream().map(UmlClassNode::id).collect(java.util.stream.Collectors.toSet());
        java.util.Set<String> relationIds = relations.stream().map(UmlClassRelation::id).collect(java.util.stream.Collectors.toSet());
        return views.stream()
                .map(view -> new UmlClassDiagramView(view.id(), view.kind(), view.displayName(), view.description(),
                        view.sourceRootIds(),
                        view.moduleIds().stream().filter(moduleIds::contains).toList(),
                        view.classIds().stream().filter(classIds::contains).toList(),
                        view.relationIds().stream().filter(relationIds::contains).toList(),
                        view.notes()))
                .toList();
    }

    private static String normalizeOrDefault(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
