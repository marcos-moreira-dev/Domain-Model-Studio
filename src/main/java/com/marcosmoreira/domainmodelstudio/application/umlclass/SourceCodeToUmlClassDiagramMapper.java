package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMember;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMemberKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMetadataKeys;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeModule;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProject;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelation;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelationKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeRole;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeVisibility;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Convierte el modelo neutral de código fuente en un documento UML Clases editable. */
public final class SourceCodeToUmlClassDiagramMapper {

    private final SourceCodeUmlModuleNamingPolicy moduleNaming = new SourceCodeUmlModuleNamingPolicy();

    public UmlClassDiagramDocument map(ParsedCodeProject project) {
        Objects.requireNonNull(project, "project");
        Map<String, String> moduleIdMap = new LinkedHashMap<>();
        Map<String, String> typeIdMap = new LinkedHashMap<>();
        Map<String, String> relationIdMap = new LinkedHashMap<>();
        List<UmlModuleGroup> modules = mapModules(project, moduleIdMap);
        List<UmlClassNode> classes = mapClasses(project, moduleIdMap, typeIdMap);
        List<UmlClassRelation> relations = mapRelations(project.relations(), typeIdMap, relationIdMap);
        var views = new SourceCodeUmlClassViewBuilder().buildViews(project, moduleIdMap, typeIdMap, relationIdMap);
        return new UmlClassDiagramDocument(project.projectName() + " — UML desde código", "borrador",
                LocalDate.now(), modules, classes, relations, views, notesFor(project));
    }

    private List<UmlModuleGroup> mapModules(ParsedCodeProject project, Map<String, String> moduleIdMap) {
        ArrayList<UmlModuleGroup> modules = new ArrayList<>();
        Set<String> usedIds = new LinkedHashSet<>();
        for (ParsedCodeModule module : project.modules()) {
            String id = uniqueId(UmlClassDiagramIds.slug(module.sourceRootId() + " " + module.qualifiedName(), "modulo"), usedIds);
            moduleIdMap.put(module.id(), id);
            modules.add(new UmlModuleGroup(id, moduleNaming.displayName(module), pathOf(module),
                    moduleNaming.description(module), sourceRootNote(module.sourceRootId())));
        }
        return modules;
    }

    private String pathOf(ParsedCodeModule module) {
        return module.relativePath() == null ? module.qualifiedName() : module.relativePath().toString();
    }

    private List<UmlClassNode> mapClasses(ParsedCodeProject project, Map<String, String> moduleIdMap,
                                          Map<String, String> typeIdMap) {
        ArrayList<UmlClassNode> classes = new ArrayList<>();
        Set<String> usedIds = new LinkedHashSet<>();
        for (ParsedCodeType type : project.types()) {
            String id = uniqueId(UmlClassDiagramIds.slug(type.sourceRootId() + " " + type.qualifiedName(), "clase"), usedIds);
            typeIdMap.put(type.id(), id);
            classes.add(new UmlClassNode(id, moduleIdMap.getOrDefault(type.moduleId(), ""), type.simpleName(),
                    type.packageName(), classKind(type), UmlVisibility.PUBLIC, responsibilityOf(type),
                    descriptionOf(type), membersOf(type, id), notesOf(type)));
        }
        return classes;
    }

    private List<UmlClassMember> membersOf(ParsedCodeType type, String classId) {
        ArrayList<UmlClassMember> members = new ArrayList<>();
        Set<String> usedIds = new LinkedHashSet<>();
        for (ParsedCodeMember member : type.members()) {
            String id = uniqueId(UmlClassDiagramIds.slug(classId + " " + member.id(), "miembro"), usedIds);
            members.add(new UmlClassMember(id, memberKind(member.kind()), member.name(), member.type(),
                    signatureOf(member), visibilityOf(member.visibility()), false, annotationsOf(member.annotations())));
        }
        return members;
    }

    private List<UmlClassRelation> mapRelations(List<ParsedCodeRelation> parsedRelations, Map<String, String> typeIdMap,
                                                Map<String, String> relationIdMap) {
        ArrayList<UmlClassRelation> relations = new ArrayList<>();
        Set<String> usedIds = new LinkedHashSet<>();
        Set<String> semanticKeys = new LinkedHashSet<>();
        for (ParsedCodeRelation relation : parsedRelations) {
            String sourceId = typeIdMap.get(relation.sourceTypeId());
            String targetId = typeIdMap.get(relation.metadata().get(ParsedCodeMetadataKeys.TARGET_TYPE_ID));
            if (sourceId == null || targetId == null || sourceId.equals(targetId)) {
                continue;
            }
            UmlRelationKind kind = relationKind(relation.kind());
            String semanticKey = sourceId + "->" + targetId + ":" + kind;
            if (!semanticKeys.add(semanticKey)) {
                continue;
            }
            String id = uniqueId(UmlClassDiagramIds.slug(semanticKey, "relacion"), usedIds);
            relationIdMap.put(relation.id(), id);
            relations.add(new UmlClassRelation(id, sourceId, targetId, kind, labelFor(relation.kind()),
                    relation.description(), notesFor(relation)));
        }
        return relations;
    }

    private UmlClassKind classKind(ParsedCodeType type) {
        String role = type.metadata().getOrDefault(ParsedCodeMetadataKeys.ROLE, "");
        if (ParsedCodeTypeRole.BACKEND_CONTROLLER.id().equals(role)) { return UmlClassKind.CONTROLLER; }
        if (ParsedCodeTypeRole.BACKEND_SERVICE.id().equals(role) || ParsedCodeTypeRole.FRONTEND_SERVICE.id().equals(role)) {
            return UmlClassKind.SERVICE;
        }
        if (ParsedCodeTypeRole.BACKEND_REPOSITORY.id().equals(role)) { return UmlClassKind.REPOSITORY; }
        if (ParsedCodeTypeRole.DTO.id().equals(role)) { return UmlClassKind.DTO; }
        if (ParsedCodeTypeRole.FRONTEND_COMPONENT.id().equals(role)) { return UmlClassKind.COMPONENT; }
        if ("true".equalsIgnoreCase(type.metadata().getOrDefault(ParsedCodeMetadataKeys.ABSTRACT_TYPE, ""))) {
            return UmlClassKind.ABSTRACT_CLASS;
        }
        return switch (type.kind()) {
            case INTERFACE -> UmlClassKind.INTERFACE;
            case ENUM -> UmlClassKind.ENUM;
            case RECORD -> UmlClassKind.RECORD;
            default -> UmlClassKind.CLASS;
        };
    }

    private UmlMemberKind memberKind(ParsedCodeMemberKind kind) {
        return switch (kind == null ? ParsedCodeMemberKind.UNKNOWN : kind) {
            case METHOD -> UmlMemberKind.METHOD;
            case CONSTRUCTOR -> UmlMemberKind.CONSTRUCTOR;
            default -> UmlMemberKind.ATTRIBUTE;
        };
    }

    private UmlVisibility visibilityOf(ParsedCodeVisibility visibility) {
        return switch (visibility == null ? ParsedCodeVisibility.UNKNOWN : visibility) {
            case PUBLIC -> UmlVisibility.PUBLIC;
            case PROTECTED -> UmlVisibility.PROTECTED;
            case PRIVATE -> UmlVisibility.PRIVATE;
            case PACKAGE_PRIVATE, INTERNAL -> UmlVisibility.PACKAGE;
            case UNKNOWN -> UmlVisibility.UNSPECIFIED;
        };
    }

    private UmlRelationKind relationKind(ParsedCodeRelationKind kind) {
        return switch (kind == null ? ParsedCodeRelationKind.UNKNOWN : kind) {
            case EXTENDS -> UmlRelationKind.INHERITANCE;
            case IMPLEMENTS -> UmlRelationKind.IMPLEMENTATION;
            case ASSOCIATION -> UmlRelationKind.ASSOCIATION;
            case AGGREGATION -> UmlRelationKind.AGGREGATION;
            case COMPOSITION -> UmlRelationKind.COMPOSITION;
            default -> UmlRelationKind.DEPENDENCY;
        };
    }

    private String responsibilityOf(ParsedCodeType type) {
        String role = type.metadata().getOrDefault(ParsedCodeMetadataKeys.ROLE, ParsedCodeTypeRole.UNKNOWN.id());
        return "Tipo importado desde código fuente" + (role.isBlank() ? "." : " con rol tentativo: " + role + ".");
    }

    private String descriptionOf(ParsedCodeType type) {
        String language = type.metadata().getOrDefault(ParsedCodeMetadataKeys.LANGUAGE, "lenguaje no especificado");
        String absolutePath = privatePath(type.metadata().getOrDefault(ParsedCodeMetadataKeys.ABSOLUTE_SOURCE_PATH, ""));
        String origin = "Origen: " + language + "; ruta: " + pathOf(type);
        return origin + (absolutePath.isBlank() ? "" : "; ruta absoluta: " + absolutePath)
                + "; nombre completo: " + type.qualifiedName();
    }

    private String notesOf(ParsedCodeType type) {
        ArrayList<String> parts = new ArrayList<>();
        parts.add(sourceRootNote(type.sourceRootId()));
        if (!type.annotations().isEmpty()) {
            parts.add("Anotaciones: " + String.join(", ", type.annotations()));
        }
        if (type.metadata().containsKey(ParsedCodeMetadataKeys.SOURCE_ROOT_PATH)) {
            parts.add("Source root path: " + privatePath(type.metadata().get(ParsedCodeMetadataKeys.SOURCE_ROOT_PATH)));
        }
        if (type.metadata().containsKey(ParsedCodeMetadataKeys.FRAMEWORK_HINT)) {
            parts.add("Framework: " + type.metadata().get(ParsedCodeMetadataKeys.FRAMEWORK_HINT));
        }
        return String.join(" | ", parts);
    }

    private String privatePath(String value) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.isBlank()) {
            return "";
        }
        String userHome = System.getProperty("user.home", "");
        if (!userHome.isBlank()) {
            String forwardHome = userHome.replace('\\', '/');
            String forwardValue = normalized.replace('\\', '/');
            if (forwardValue.equals(forwardHome)) {
                return "${USER_HOME}";
            }
            if (forwardValue.startsWith(forwardHome + "/")) {
                return "${USER_HOME}" + normalized.substring(userHome.length());
            }
        }
        return normalized;
    }

    private String notesFor(ParsedCodeProject project) {
        if (project.warnings().isEmpty()) {
            return "Generado desde modelo neutral de código fuente. La importación Markdown de UML sigue siendo paralela.";
        }
        return "Generado desde modelo neutral de código fuente. La importación Markdown de UML sigue siendo paralela. Advertencias: " + String.join(" | ", project.warnings());
    }

    private String notesFor(ParsedCodeRelation relation) {
        String target = relation.metadata().getOrDefault(ParsedCodeMetadataKeys.TARGET_QUALIFIED_NAME, relation.targetTypeName());
        String inferred = Boolean.parseBoolean(relation.metadata().getOrDefault(ParsedCodeMetadataKeys.INFERRED, "false"))
                ? " Relación inferida: "
                + relation.metadata().getOrDefault(ParsedCodeMetadataKeys.INFERENCE_REASON, "sin detalle") + "."
                : "";
        String api = relation.kind() == ParsedCodeRelationKind.API_CALL
                ? " Endpoint: " + relation.metadata().getOrDefault(ParsedCodeMetadataKeys.API_HTTP_METHOD, "")
                + " " + relation.metadata().getOrDefault(ParsedCodeMetadataKeys.API_PATH, relation.targetTypeName()) + "."
                : "";
        String ownership = relation.metadata().getOrDefault(ParsedCodeMetadataKeys.RELATION_OWNERSHIP_HINT, "");
        String sourceMember = relation.metadata().getOrDefault(ParsedCodeMetadataKeys.RELATION_SOURCE_MEMBER, "");
        String member = sourceMember.isBlank() ? "" : " Miembro origen: " + sourceMember + ".";
        String ownershipNote = ownership.isBlank() ? "" : " Semántica: " + ownership + ".";
        return "Relación detectada desde código fuente hacia " + target + "." + member + ownershipNote + inferred + api;
    }

    private String labelFor(ParsedCodeRelationKind kind) {
        return switch (kind == null ? ParsedCodeRelationKind.UNKNOWN : kind) {
            case EXTENDS -> "extends";
            case IMPLEMENTS -> "implements";
            case ASSOCIATION -> "usa";
            case AGGREGATION -> "agrega";
            case COMPOSITION -> "compone";
            case API_CALL -> "api";
            case DEPENDENCY -> "depende";
            case UNKNOWN -> "";
        };
    }

    private String signatureOf(ParsedCodeMember member) {
        return member.signature().isBlank() ? member.name() : member.signature();
    }

    private String annotationsOf(List<String> annotations) {
        return annotations == null || annotations.isEmpty() ? "" : "Anotaciones: " + String.join(", ", annotations);
    }

    private String pathOf(ParsedCodeType type) {
        return type.sourcePath() == null ? "" : type.sourcePath().toString();
    }

    private String sourceRootNote(String sourceRootId) {
        return "Source root: " + (sourceRootId == null ? "" : sourceRootId);
    }

    private String uniqueId(String base, Set<String> usedIds) {
        String normalizedBase = base == null || base.isBlank() ? "id" : base;
        String candidate = normalizedBase;
        int counter = 2;
        while (!usedIds.add(candidate)) {
            candidate = normalizedBase + "_" + counter++;
        }
        return candidate;
    }
}
