package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.*;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.*;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Lee diccionario de datos, mapa de módulos y UML Clases desde el bloque model. */
final class DmsProjectCoreSpecializedJsonReader {

    DataDictionaryDocument readDataDictionary(Map<String, Object> object) {
        List<DataDictionaryEntity> entities = new ArrayList<>();
        for (Object entityValue : array(object, "entities")) {
            entities.add(readDataDictionaryEntity(asObject(entityValue, "dataDictionaryEntity")));
        }
        return new DataDictionaryDocument(
                stringOrDefault(object, "projectName", "Diccionario de datos"),
                stringOrDefault(object, "clientName", ""),
                stringOrDefault(object, "organizationName", ""),
                stringOrDefault(object, "author", ""),
                stringOrDefault(object, "version", "borrador"),
                java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                enumValue(DataDictionaryStatus.class, stringOrDefault(object, "status", "DRAFT"), DataDictionaryStatus.DRAFT),
                stringOrDefault(object, "introduction", ""),
                stringOrDefault(object, "logoReference", ""),
                entities,
                stringOrDefault(object, "notes", ""));
    }

    private DataDictionaryEntity readDataDictionaryEntity(Map<String, Object> object) {
        List<DataDictionaryField> fields = new ArrayList<>();
        for (Object fieldValue : array(object, "fields")) {
            fields.add(readDataDictionaryField(asObject(fieldValue, "dataDictionaryField")));
        }
        return new DataDictionaryEntity(
                string(object, "id"),
                stringOrDefault(object, "displayName", string(object, "id")),
                stringOrDefault(object, "technicalName", string(object, "id")),
                stringOrDefault(object, "description", ""),
                stringOrDefault(object, "moduleName", ""),
                enumValue(DataEntityKind.class, stringOrDefault(object, "kind", "MAIN"), DataEntityKind.MAIN),
                stringOrDefault(object, "origin", "levantamiento manual"),
                enumValue(DataDictionaryStatus.class, stringOrDefault(object, "status", "DRAFT"), DataDictionaryStatus.DRAFT),
                fields,
                stringOrDefault(object, "notes", ""));
    }

    private DataDictionaryField readDataDictionaryField(Map<String, Object> object) {
        return new DataDictionaryField(
                string(object, "name"),
                stringOrDefault(object, "displayName", string(object, "name")),
                stringOrDefault(object, "technicalName", string(object, "name")),
                enumValue(LogicalDataType.class, stringOrDefault(object, "logicalType", "UNKNOWN"), LogicalDataType.UNKNOWN),
                stringOrDefault(object, "physicalTypeSuggestion", ""),
                enumSet(FieldConstraint.class, array(object, "constraints")),
                stringOrDefault(object, "foreignKeyReference", ""),
                stringOrDefault(object, "defaultValue", ""),
                stringOrDefault(object, "expectedFormat", ""),
                stringOrDefault(object, "description", ""),
                stringOrDefault(object, "businessRule", ""),
                stringOrDefault(object, "validationRule", ""),
                stringOrDefault(object, "example", ""),
                enumSet(FieldVisibility.class, array(object, "visibility")),
                boolOrDefault(object, "userEditable", true),
                stringOrDefault(object, "notes", ""));
    }

    ModuleMapDocument readModuleMap(Map<String, Object> object) {
        List<ModuleNode> modules = new ArrayList<>();
        for (Object moduleValue : array(object, "modules")) {
            modules.add(readModuleNode(asObject(moduleValue, "moduleNode")));
        }
        List<ModuleDependency> dependencies = new ArrayList<>();
        for (Object dependencyValue : array(object, "dependencies")) {
            dependencies.add(readModuleDependency(asObject(dependencyValue, "moduleDependency")));
        }
        return new ModuleMapDocument(
                stringOrDefault(object, "projectName", "Mapa de módulos"),
                stringOrDefault(object, "version", "borrador"),
                java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                modules,
                dependencies,
                stringOrDefault(object, "notes", ""));
    }

    private ModuleNode readModuleNode(Map<String, Object> object) {
        return new ModuleNode(
                string(object, "id"),
                stringOrDefault(object, "displayName", string(object, "id")),
                stringOrDefault(object, "parentId", ""),
                enumValue(ModuleKind.class, stringOrDefault(object, "kind", "MAIN"), ModuleKind.MAIN),
                enumValue(ModuleStatus.class, stringOrDefault(object, "status", "PLANNED"), ModuleStatus.PLANNED),
                stringOrDefault(object, "responsibility", ""),
                stringOrDefault(object, "description", ""),
                stringList(array(object, "tags")),
                stringOrDefault(object, "notes", ""));
    }

    private ModuleDependency readModuleDependency(Map<String, Object> object) {
        return new ModuleDependency(
                string(object, "id"),
                stringOrDefault(object, "sourceModuleId", stringOrDefault(object, "source", "")),
                stringOrDefault(object, "targetModuleId", stringOrDefault(object, "target", "")),
                enumValue(DependencyKind.class, stringOrDefault(object, "kind", "USES"), DependencyKind.USES),
                stringOrDefault(object, "description", ""),
                stringOrDefault(object, "notes", ""));
    }

    UmlClassDiagramDocument readUmlClassDiagram(Map<String, Object> object) {
        List<UmlModuleGroup> modules = new ArrayList<>();
        for (Object moduleValue : array(object, "modules")) {
            modules.add(readUmlModule(asObject(moduleValue, "umlModule")));
        }
        List<UmlClassNode> classes = new ArrayList<>();
        for (Object classValue : array(object, "classes")) {
            classes.add(readUmlClass(asObject(classValue, "umlClass")));
        }
        List<UmlClassRelation> relations = new ArrayList<>();
        for (Object relationValue : array(object, "relations")) {
            relations.add(readUmlRelation(asObject(relationValue, "umlRelation")));
        }
        List<UmlClassDiagramView> views = new ArrayList<>();
        for (Object viewValue : array(object, "views")) {
            views.add(readUmlClassDiagramView(asObject(viewValue, "umlClassView")));
        }
        return new UmlClassDiagramDocument(
                stringOrDefault(object, "projectName", "UML Clases"),
                stringOrDefault(object, "version", "borrador"),
                java.time.LocalDate.parse(stringOrDefault(object, "documentDate", java.time.LocalDate.now().toString())),
                modules,
                classes,
                relations,
                views,
                stringOrDefault(object, "notes", ""));
    }

    private UmlClassDiagramView readUmlClassDiagramView(Map<String, Object> object) {
        return new UmlClassDiagramView(
                string(object, "id"),
                enumValue(UmlClassDiagramViewKind.class,
                        stringOrDefault(object, "kind", "CUSTOM"),
                        UmlClassDiagramViewKind.CUSTOM),
                stringOrDefault(object, "displayName", string(object, "id")),
                stringOrDefault(object, "description", ""),
                stringList(array(object, "sourceRootIds")),
                stringList(array(object, "moduleIds")),
                stringList(array(object, "classIds")),
                stringList(array(object, "relationIds")),
                stringOrDefault(object, "notes", ""));
    }

    private UmlModuleGroup readUmlModule(Map<String, Object> object) {
        return new UmlModuleGroup(
                string(object, "id"),
                stringOrDefault(object, "displayName", string(object, "id")),
                stringOrDefault(object, "path", ""),
                stringOrDefault(object, "description", ""),
                stringOrDefault(object, "notes", ""));
    }

    private UmlClassNode readUmlClass(Map<String, Object> object) {
        List<UmlClassMember> members = new ArrayList<>();
        for (Object memberValue : array(object, "members")) {
            members.add(readUmlMember(asObject(memberValue, "umlMember")));
        }
        return new UmlClassNode(
                string(object, "id"),
                stringOrDefault(object, "moduleId", ""),
                stringOrDefault(object, "displayName", string(object, "id")),
                stringOrDefault(object, "packageName", ""),
                enumValue(UmlClassKind.class, stringOrDefault(object, "kind", "CLASS"), UmlClassKind.CLASS),
                enumValue(UmlVisibility.class, stringOrDefault(object, "visibility", "PUBLIC"), UmlVisibility.PUBLIC),
                stringOrDefault(object, "responsibility", ""),
                stringOrDefault(object, "description", ""),
                members,
                stringOrDefault(object, "notes", ""));
    }

    private UmlClassMember readUmlMember(Map<String, Object> object) {
        return new UmlClassMember(
                string(object, "id"),
                enumValue(UmlMemberKind.class, stringOrDefault(object, "kind", "ATTRIBUTE"), UmlMemberKind.ATTRIBUTE),
                stringOrDefault(object, "name", string(object, "id")),
                stringOrDefault(object, "type", ""),
                stringOrDefault(object, "signature", ""),
                enumValue(UmlVisibility.class, stringOrDefault(object, "visibility", "PUBLIC"), UmlVisibility.PUBLIC),
                boolOrDefault(object, "staticMember", false),
                stringOrDefault(object, "description", ""));
    }

    private UmlClassRelation readUmlRelation(Map<String, Object> object) {
        return new UmlClassRelation(
                string(object, "id"),
                stringOrDefault(object, "sourceClassId", stringOrDefault(object, "source", "")),
                stringOrDefault(object, "targetClassId", stringOrDefault(object, "target", "")),
                enumValue(UmlRelationKind.class, stringOrDefault(object, "kind", "DEPENDENCY"), UmlRelationKind.DEPENDENCY),
                stringOrDefault(object, "label", ""),
                stringOrDefault(object, "description", ""),
                stringOrDefault(object, "notes", ""));
    }

    private List<String> stringList(List<Object> values) {
        List<String> result = new ArrayList<>();
        for (Object value : values) {
            String normalized = String.valueOf(value).strip();
            if (!normalized.isBlank()) {
                result.add(normalized);
            }
        }
        return result;
    }

    private <E extends Enum<E>> java.util.Set<E> enumSet(Class<E> enumType, List<Object> values) {
        java.util.LinkedHashSet<E> result = new java.util.LinkedHashSet<>();
        for (Object value : values) {
            E enumValue = enumValue(enumType, String.valueOf(value), null);
            if (enumValue != null) {
                result.add(enumValue);
            }
        }
        return result;
    }


    private List<Object> array(Map<String, Object> parent, String key) {
        Object value = parent.get(key);
        if (value == null) {
            return List.of();
        }
        if (value instanceof List<?> list) {
            return new ArrayList<>(list);
        }
        throw new IllegalArgumentException("Se esperaba arreglo en " + key);
    }

    private Map<String, Object> asObject(Object value, String context) {
        if (value instanceof Map<?, ?> map) {
            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            return result;
        }
        throw new IllegalArgumentException("Se esperaba objeto JSON en " + context);
    }

    private String string(Map<String, Object> object, String key) {
        Object value = object.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Campo obligatorio ausente: " + key);
        }
        return String.valueOf(value);
    }

    private String stringOrDefault(Map<String, Object> object, String key, String defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }

    private double numberOrDefault(Map<String, Object> object, String key, double defaultValue) {
        Object value = object.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    private boolean boolOrDefault(Map<String, Object> object, String key, boolean defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : booleanValue(value);
    }

    private boolean booleanValue(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    private <E extends Enum<E>> E enumValue(Class<E> enumType, String rawValue, E fallback) {
        if (rawValue == null || rawValue.isBlank()) {
            return fallback;
        }
        try {
            return Enum.valueOf(enumType, rawValue.trim().toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }
}
