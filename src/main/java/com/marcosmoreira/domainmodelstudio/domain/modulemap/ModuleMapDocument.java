package com.marcosmoreira.domainmodelstudio.domain.modulemap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Documento semántico del mapa de módulos de una aplicación administrativa. */
public final class ModuleMapDocument {

    private final String projectName;
    private final String version;
    private final LocalDate documentDate;
    private final List<ModuleNode> modules;
    private final List<ModuleDependency> dependencies;
    private final String notes;
    private final Map<String, ModuleNode> modulesById;

    public ModuleMapDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            List<ModuleNode> modules,
            List<ModuleDependency> dependencies,
            String notes
    ) {
        this.projectName = normalize(projectName).isBlank() ? "Mapa de módulos" : normalize(projectName);
        this.version = normalize(version).isBlank() ? "borrador" : normalize(version);
        this.documentDate = documentDate == null ? LocalDate.now() : documentDate;
        this.modules = List.copyOf(modules == null ? List.of() : modules);
        this.dependencies = List.copyOf(dependencies == null ? List.of() : dependencies);
        this.notes = normalize(notes);
        this.modulesById = indexModules(this.modules);
        validateReferences();
    }

    public static ModuleMapDocument blank(String projectName) {
        return new ModuleMapDocument(projectName, "borrador", LocalDate.now(), List.of(), List.of(), "");
    }

    public String projectName() {
        return projectName;
    }

    public String version() {
        return version;
    }

    public LocalDate documentDate() {
        return documentDate;
    }

    public List<ModuleNode> modules() {
        return modules;
    }

    public List<ModuleDependency> dependencies() {
        return dependencies;
    }

    public String notes() {
        return notes;
    }

    public int moduleCount() {
        return modules.size();
    }

    public int dependencyCount() {
        return dependencies.size();
    }

    public Optional<ModuleNode> moduleById(String id) {
        return Optional.ofNullable(modulesById.get(normalize(id)));
    }

    public List<ModuleNode> rootModules() {
        return modules.stream().filter(ModuleNode::rootModule).toList();
    }

    public List<ModuleNode> childrenOf(String parentId) {
        String normalizedParent = normalize(parentId);
        return modules.stream()
                .filter(module -> module.parentId().equals(normalizedParent))
                .toList();
    }

    public ModuleMapDocument withModule(ModuleNode module) {
        Objects.requireNonNull(module, "module");
        if (modulesById.containsKey(module.id())) {
            throw new IllegalArgumentException("Ya existe un módulo con ID: " + module.id());
        }
        List<ModuleNode> updated = new ArrayList<>(modules);
        updated.add(module);
        return new ModuleMapDocument(projectName, version, documentDate, updated, dependencies, notes);
    }

    public ModuleMapDocument withUpdatedModule(ModuleNode updatedModule) {
        Objects.requireNonNull(updatedModule, "updatedModule");
        List<ModuleNode> updated = new ArrayList<>();
        boolean replaced = false;
        for (ModuleNode module : modules) {
            if (module.id().equals(updatedModule.id())) {
                updated.add(updatedModule);
                replaced = true;
            } else {
                updated.add(module);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe módulo para actualizar: " + updatedModule.id());
        }
        return new ModuleMapDocument(projectName, version, documentDate, updated, dependencies, notes);
    }

    public ModuleMapDocument withoutModule(String moduleId) {
        String normalizedId = normalize(moduleId);
        if (!modulesById.containsKey(normalizedId)) {
            throw new IllegalArgumentException("No existe módulo para eliminar: " + normalizedId);
        }
        List<ModuleNode> updatedModules = modules.stream()
                .filter(module -> !module.id().equals(normalizedId))
                .filter(module -> !module.parentId().equals(normalizedId))
                .toList();
        List<ModuleDependency> updatedDependencies = dependencies.stream()
                .filter(dependency -> !dependency.sourceModuleId().equals(normalizedId))
                .filter(dependency -> !dependency.targetModuleId().equals(normalizedId))
                .toList();
        return new ModuleMapDocument(projectName, version, documentDate, updatedModules, updatedDependencies, notes);
    }

    public ModuleMapDocument withDependency(ModuleDependency dependency) {
        Objects.requireNonNull(dependency, "dependency");
        if (dependencies.stream().anyMatch(existing -> existing.id().equals(dependency.id()))) {
            throw new IllegalArgumentException("Ya existe una dependencia con ID: " + dependency.id());
        }
        List<ModuleDependency> updated = new ArrayList<>(dependencies);
        updated.add(dependency);
        return new ModuleMapDocument(projectName, version, documentDate, modules, updated, notes);
    }

    public ModuleMapDocument withUpdatedDependency(ModuleDependency updatedDependency) {
        Objects.requireNonNull(updatedDependency, "updatedDependency");
        List<ModuleDependency> updated = new ArrayList<>();
        boolean replaced = false;
        for (ModuleDependency dependency : dependencies) {
            if (dependency.id().equals(updatedDependency.id())) {
                updated.add(updatedDependency);
                replaced = true;
            } else {
                updated.add(dependency);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe dependencia para actualizar: " + updatedDependency.id());
        }
        return new ModuleMapDocument(projectName, version, documentDate, modules, updated, notes);
    }

    public ModuleMapDocument withoutDependency(String dependencyId) {
        String normalizedId = normalize(dependencyId);
        List<ModuleDependency> updated = dependencies.stream()
                .filter(dependency -> !dependency.id().equals(normalizedId))
                .toList();
        if (updated.size() == dependencies.size()) {
            throw new IllegalArgumentException("No existe dependencia para eliminar: " + normalizedId);
        }
        return new ModuleMapDocument(projectName, version, documentDate, modules, updated, notes);
    }

    public ModuleMapDocument withNotes(String updatedNotes) {
        return new ModuleMapDocument(projectName, version, documentDate, modules, dependencies, updatedNotes);
    }

    private void validateReferences() {
        for (ModuleNode module : modules) {
            if (!module.parentId().isBlank() && !modulesById.containsKey(module.parentId())) {
                throw new IllegalArgumentException("El módulo " + module.displayName()
                        + " referencia un padre inexistente: " + module.parentId());
            }
        }
        for (ModuleDependency dependency : dependencies) {
            if (!modulesById.containsKey(dependency.sourceModuleId())) {
                throw new IllegalArgumentException("Dependencia con origen inexistente: " + dependency.sourceModuleId());
            }
            if (!modulesById.containsKey(dependency.targetModuleId())) {
                throw new IllegalArgumentException("Dependencia con destino inexistente: " + dependency.targetModuleId());
            }
        }
    }

    private static Map<String, ModuleNode> indexModules(List<ModuleNode> modules) {
        Map<String, ModuleNode> indexed = new LinkedHashMap<>();
        for (ModuleNode module : modules) {
            ModuleNode previous = indexed.put(module.id(), module);
            if (previous != null) {
                throw new IllegalArgumentException("Módulo duplicado: " + module.id());
            }
        }
        return Map.copyOf(indexed);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
