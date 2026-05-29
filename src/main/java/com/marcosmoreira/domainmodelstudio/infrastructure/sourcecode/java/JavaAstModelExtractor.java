package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.java;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMember;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMemberKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMetadataKeys;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeModule;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelation;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelationKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelationSemanticPolicy;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeVisibility;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceFileCandidate;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRoot;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;

/** Extrae un modelo neutral desde el AST Java ofrecido por el JDK. */
final class JavaAstModelExtractor {
    private final SourceRoot sourceRoot;
    private final SourceFileCandidate file;
    private final CompilationUnitTree unit;
    private final Map<String, ParsedCodeModule> modulesById = new LinkedHashMap<>();
    private final List<ParsedCodeType> types = new ArrayList<>();
    private final List<ParsedCodeRelation> relations = new ArrayList<>();

    JavaAstModelExtractor(SourceRoot sourceRoot, SourceFileCandidate file, CompilationUnitTree unit) {
        this.sourceRoot = sourceRoot;
        this.file = file;
        this.unit = unit;
    }

    Extraction extract() {
        String packageName = packageName();
        for (Tree declaration : unit.getTypeDecls()) {
            if (declaration instanceof ClassTree classTree) {
                extractType(classTree, packageName, "", 0);
            }
        }
        return new Extraction(List.copyOf(modulesById.values()), List.copyOf(types), List.copyOf(relations));
    }

    private void extractType(ClassTree classTree, String packageName, String outerQualifiedName, int nestingLevel) {
        String simpleName = classTree.getSimpleName().toString();
        if (simpleName.isBlank()) {
            return;
        }
        String qualifiedName = qualifiedName(packageName, outerQualifiedName, simpleName);
        String moduleId = moduleFor(packageName).id();
        List<ParsedCodeMember> members = membersFor(classTree, qualifiedName);
        ParsedCodeType parsedType = new ParsedCodeType(typeId(qualifiedName), sourceRoot.id(), moduleId, qualifiedName,
                simpleName, kindFor(classTree), file.relativePath(), packageName, members,
                annotations(classTree.getModifiers()), typeMetadata(classTree, nestingLevel));
        types.add(parsedType);
        collectInheritanceRelations(parsedType, classTree);
        collectAssociationRelations(parsedType, members);
        for (Tree member : classTree.getMembers()) {
            if (member instanceof ClassTree nestedType) {
                extractType(nestedType, packageName, qualifiedName, nestingLevel + 1);
            }
        }
    }

    private List<ParsedCodeMember> membersFor(ClassTree classTree, String ownerQualifiedName) {
        List<ParsedCodeMember> members = new ArrayList<>();
        for (Tree member : classTree.getMembers()) {
            if (member instanceof VariableTree variableTree) {
                members.add(fieldMember(variableTree, ownerQualifiedName));
            } else if (member instanceof MethodTree methodTree) {
                members.add(methodMember(methodTree, ownerQualifiedName, classTree.getSimpleName().toString()));
            }
        }
        return members;
    }

    private ParsedCodeMember fieldMember(VariableTree tree, String ownerQualifiedName) {
        String name = tree.getName().toString();
        String type = tree.getType() == null ? "" : tree.getType().toString();
        return new ParsedCodeMember(memberId(ownerQualifiedName, "field", name), ParsedCodeMemberKind.FIELD,
                name, type, fieldSignature(name, type), visibility(tree.getModifiers()), annotations(tree.getModifiers()), Map.of());
    }

    private ParsedCodeMember methodMember(MethodTree tree, String ownerQualifiedName, String ownerSimpleName) {
        String rawName = tree.getName().toString();
        boolean constructor = rawName.equals("<init>");
        String displayName = constructor ? ownerSimpleName : rawName;
        String returnType = constructor || tree.getReturnType() == null ? "" : tree.getReturnType().toString();
        return new ParsedCodeMember(memberId(ownerQualifiedName, constructor ? "constructor" : "method", displayName),
                constructor ? ParsedCodeMemberKind.CONSTRUCTOR : ParsedCodeMemberKind.METHOD, displayName, returnType,
                methodSignature(displayName, tree), visibility(tree.getModifiers()), annotations(tree.getModifiers()), Map.of());
    }

    private void collectInheritanceRelations(ParsedCodeType parsedType, ClassTree classTree) {
        Tree extendsClause = classTree.getExtendsClause();
        if (extendsClause != null) {
            addRelation(parsedType, JavaTypeNamePolicy.simpleName(extendsClause.toString()), ParsedCodeRelationKind.EXTENDS,
                    "Herencia Java detectada desde extends.");
        }
        ParsedCodeRelationKind relationKind = parsedType.kind() == ParsedCodeTypeKind.INTERFACE
                ? ParsedCodeRelationKind.EXTENDS
                : ParsedCodeRelationKind.IMPLEMENTS;
        for (Tree implemented : classTree.getImplementsClause()) {
            addRelation(parsedType, JavaTypeNamePolicy.simpleName(implemented.toString()), relationKind,
                    relationKind == ParsedCodeRelationKind.IMPLEMENTS
                            ? "Realización Java detectada desde implements."
                            : "Herencia de interfaz Java detectada.");
        }
    }

    private void collectAssociationRelations(ParsedCodeType parsedType, List<ParsedCodeMember> members) {
        for (ParsedCodeMember member : members) {
            if (member.kind() != ParsedCodeMemberKind.FIELD) {
                continue;
            }
            ParsedCodeRelationKind kind = ParsedCodeRelationSemanticPolicy.kindForField(
                    member.name(), member.type(), member.annotations());
            for (String target : JavaTypeNamePolicy.associationTargets(member.type())) {
                addRelation(parsedType, target, kind,
                        ParsedCodeRelationSemanticPolicy.descriptionForField(member.name(), kind),
                        ParsedCodeRelationSemanticPolicy.metadataForField(member.name(), member.type(), member.annotations(), "java"));
            }
        }
    }

    private ParsedCodeModule moduleFor(String packageName) {
        String moduleName = packageName.isBlank() ? moduleNameFromPath() : packageName;
        String id = sourceRoot.id() + ":" + sanitize(moduleName.isBlank() ? "default" : moduleName);
        return modulesById.computeIfAbsent(id, key -> new ParsedCodeModule(key, sourceRoot.id(), moduleName,
                moduleName.isBlank() ? "default" : moduleName, file.relativePath().getParent()));
    }

    private String packageName() {
        return unit.getPackageName() == null ? "" : unit.getPackageName().toString();
    }

    private String moduleNameFromPath() {
        return file.relativePath().getParent() == null ? "" : file.relativePath().getParent().toString().replace('\\', '.').replace('/', '.');
    }

    private Map<String, String> typeMetadata(ClassTree classTree, int nestingLevel) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ParsedCodeMetadataKeys.LANGUAGE, "java");
        metadata.put(ParsedCodeMetadataKeys.SOURCE_PATH, file.relativePath().toString());
        metadata.put(ParsedCodeMetadataKeys.ABSOLUTE_SOURCE_PATH, file.absolutePath().toString());
        metadata.put(ParsedCodeMetadataKeys.SOURCE_ROOT_PATH, sourceRoot.path().toString());
        metadata.put("nestingLevel", Integer.toString(nestingLevel));
        if (classTree.getModifiers().getFlags().contains(Modifier.ABSTRACT)) {
            metadata.put(ParsedCodeMetadataKeys.ABSTRACT_TYPE, "true");
        }
        return Map.copyOf(metadata);
    }

    private ParsedCodeTypeKind kindFor(ClassTree classTree) {
        return switch (classTree.getKind()) {
            case INTERFACE, ANNOTATION_TYPE -> ParsedCodeTypeKind.INTERFACE;
            case ENUM -> ParsedCodeTypeKind.ENUM;
            case RECORD -> ParsedCodeTypeKind.RECORD;
            default -> ParsedCodeTypeKind.CLASS;
        };
    }

    private ParsedCodeVisibility visibility(ModifiersTree modifiers) {
        if (modifiers.getFlags().contains(Modifier.PUBLIC)) {
            return ParsedCodeVisibility.PUBLIC;
        }
        if (modifiers.getFlags().contains(Modifier.PROTECTED)) {
            return ParsedCodeVisibility.PROTECTED;
        }
        if (modifiers.getFlags().contains(Modifier.PRIVATE)) {
            return ParsedCodeVisibility.PRIVATE;
        }
        return ParsedCodeVisibility.PACKAGE_PRIVATE;
    }

    private List<String> annotations(ModifiersTree modifiers) {
        return modifiers.getAnnotations().stream()
                .map(Tree::toString)
                .map(annotation -> annotation.startsWith("@") ? annotation : "@" + annotation)
                .toList();
    }

    private void addRelation(ParsedCodeType source, String targetName, ParsedCodeRelationKind kind, String description) {
        addRelation(source, targetName, kind, description, Map.of("language", "java"));
    }

    private void addRelation(ParsedCodeType source, String targetName, ParsedCodeRelationKind kind,
                             String description, Map<String, String> metadata) {
        if (targetName == null || targetName.isBlank() || targetName.equals(source.simpleName())) {
            return;
        }
        String id = source.id() + ":" + kind.name().toLowerCase() + ":" + sanitize(targetName) + ":" + relations.size();
        relations.add(new ParsedCodeRelation(id, source.id(), targetName, kind, description, metadata));
    }

    private String qualifiedName(String packageName, String outerQualifiedName, String simpleName) {
        if (!outerQualifiedName.isBlank()) {
            return outerQualifiedName + "$" + simpleName;
        }
        return packageName.isBlank() ? simpleName : packageName + "." + simpleName;
    }

    private String methodSignature(String displayName, MethodTree tree) {
        String params = tree.getParameters().stream()
                .map(parameter -> parameter.getType() + " " + parameter.getName())
                .reduce((left, right) -> left + ", " + right)
                .orElse("");
        return displayName + "(" + params + ")";
    }

    private String fieldSignature(String name, String type) {
        return type.isBlank() ? name : type + " " + name;
    }

    private String typeId(String qualifiedName) {
        return sourceRoot.id() + ":" + qualifiedName;
    }

    private String memberId(String ownerQualifiedName, String kind, String name) {
        return typeId(ownerQualifiedName) + ":" + kind + ":" + name;
    }

    private static String sanitize(String value) {
        return value == null ? "default" : value.strip().replaceAll("[^A-Za-z0-9_.-]+", "-");
    }

    record Extraction(List<ParsedCodeModule> modules, List<ParsedCodeType> types, List<ParsedCodeRelation> relations) {
    }
}
