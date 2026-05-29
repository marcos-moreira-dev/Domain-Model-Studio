package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Normaliza el modelo neutral producido por parsers de código antes de convertirlo a UML Clases.
 *
 * <p>Su responsabilidad es deduplicar raíces/módulos/tipos, completar módulos faltantes, clasificar roles
 * arquitectónicos tentativos y resolver relaciones internas sin depender de Java, TypeScript ni Angular.</p>
 */
public final class ParsedCodeProjectNormalizer {
    private final ParsedCodeTypeRoleClassifier roleClassifier;
    private final ParsedCodeInternalRelationInferencePolicy internalRelationInferencePolicy;
    private final ParsedCodeApiRelationInferencePolicy apiRelationInferencePolicy;

    public ParsedCodeProjectNormalizer() {
        this(new ParsedCodeTypeRoleClassifier(), new ParsedCodeInternalRelationInferencePolicy());
    }

    public ParsedCodeProjectNormalizer(ParsedCodeTypeRoleClassifier roleClassifier) {
        this(roleClassifier, new ParsedCodeInternalRelationInferencePolicy());
    }

    public ParsedCodeProjectNormalizer(ParsedCodeTypeRoleClassifier roleClassifier,
                                       ParsedCodeInternalRelationInferencePolicy internalRelationInferencePolicy) {
        this(roleClassifier, internalRelationInferencePolicy, new ParsedCodeApiRelationInferencePolicy());
    }

    public ParsedCodeProjectNormalizer(ParsedCodeTypeRoleClassifier roleClassifier,
                                       ParsedCodeInternalRelationInferencePolicy internalRelationInferencePolicy,
                                       ParsedCodeApiRelationInferencePolicy apiRelationInferencePolicy) {
        this.roleClassifier = roleClassifier == null ? new ParsedCodeTypeRoleClassifier() : roleClassifier;
        this.internalRelationInferencePolicy = internalRelationInferencePolicy == null
                ? new ParsedCodeInternalRelationInferencePolicy()
                : internalRelationInferencePolicy;
        this.apiRelationInferencePolicy = apiRelationInferencePolicy == null
                ? new ParsedCodeApiRelationInferencePolicy()
                : apiRelationInferencePolicy;
    }

    public ParsedCodeProject normalize(ParsedCodeProject project) {
        if (project == null) {
            return new ParsedCodeProject("Proyecto de código fuente", List.of(), List.of(), List.of(), List.of(), List.of());
        }
        Map<String, ParsedCodeSourceRoot> rootsById = rootsById(project.sourceRoots());
        Map<String, ParsedCodeModule> modulesById = new LinkedHashMap<>();
        project.modules().forEach(module -> modulesById.putIfAbsent(module.id(), module));

        List<String> warnings = new ArrayList<>(project.warnings());
        Map<String, ParsedCodeType> typesById = new LinkedHashMap<>();
        for (ParsedCodeType type : project.types()) {
            ParsedCodeModule module = moduleFor(type, rootsById, modulesById);
            ParsedCodeType normalizedType = normalizeType(type, module, rootsById.get(type.sourceRootId()));
            ParsedCodeType previous = typesById.putIfAbsent(normalizedType.id(), normalizedType);
            if (previous != null) {
                warnings.add("Tipo duplicado omitido durante normalización: " + normalizedType.id());
            }
        }

        List<ParsedCodeRelation> explicitRelations = normalizeRelations(project.relations(), typesById);
        ParsedCodeProject normalizedBase = new ParsedCodeProject(project.projectName(), List.copyOf(rootsById.values()),
                List.copyOf(modulesById.values()), List.copyOf(typesById.values()), explicitRelations, List.copyOf(warnings));
        List<ParsedCodeRelation> relations = mergeExplicitAndInferredRelations(explicitRelations,
                internalRelationInferencePolicy.inferInternalRelations(normalizedBase),
                apiRelationInferencePolicy.inferApiRelations(normalizedBase), typesById);
        return new ParsedCodeProject(project.projectName(), List.copyOf(rootsById.values()),
                List.copyOf(modulesById.values()), List.copyOf(typesById.values()), relations, List.copyOf(warnings));
    }

    private List<ParsedCodeRelation> mergeExplicitAndInferredRelations(List<ParsedCodeRelation> explicitRelations,
                                                                       List<ParsedCodeRelation> internalRelations,
                                                                       List<ParsedCodeRelation> apiRelations,
                                                                       Map<String, ParsedCodeType> typesById) {
        ArrayList<ParsedCodeRelation> merged = new ArrayList<>(explicitRelations);
        merged.addAll(internalRelations == null ? List.of() : internalRelations);
        merged.addAll(apiRelations == null ? List.of() : apiRelations);
        return normalizeRelations(merged, typesById);
    }

    private Map<String, ParsedCodeSourceRoot> rootsById(List<ParsedCodeSourceRoot> roots) {
        Map<String, ParsedCodeSourceRoot> out = new LinkedHashMap<>();
        for (ParsedCodeSourceRoot root : roots) {
            out.putIfAbsent(root.id(), root);
        }
        return out;
    }

    private ParsedCodeModule moduleFor(ParsedCodeType type, Map<String, ParsedCodeSourceRoot> rootsById,
                                       Map<String, ParsedCodeModule> modulesById) {
        if (!type.moduleId().isBlank() && modulesById.containsKey(type.moduleId())) {
            return modulesById.get(type.moduleId());
        }
        ParsedCodeSourceRoot root = rootsById.get(type.sourceRootId());
        String qualifiedName = inferModuleName(type);
        String displayName = qualifiedName.isBlank() ? "default" : qualifiedName;
        String id = type.sourceRootId() + ":" + sanitize(displayName);
        Path relativePath = type.sourcePath() == null ? null : type.sourcePath().getParent();
        ParsedCodeModule module = new ParsedCodeModule(id, type.sourceRootId(), qualifiedName, displayName, relativePath);
        modulesById.putIfAbsent(id, module);
        if (root == null) {
            return module;
        }
        return modulesById.get(id);
    }

    private ParsedCodeType normalizeType(ParsedCodeType type, ParsedCodeModule module, ParsedCodeSourceRoot root) {
        Map<String, String> metadata = new LinkedHashMap<>(type.metadata());
        if (root != null) {
            metadata.putIfAbsent(ParsedCodeMetadataKeys.SOURCE_ROOT_KIND, root.kind().name().toLowerCase(Locale.ROOT));
            firstLanguage(root).ifPresent(language -> metadata.putIfAbsent(ParsedCodeMetadataKeys.LANGUAGE, language.id()));
        }
        ParsedCodeTypeRole role = roleClassifier.classify(type, root);
        metadata.putIfAbsent(ParsedCodeMetadataKeys.ROLE, role.id());
        return new ParsedCodeType(type.id(), type.sourceRootId(), module.id(), type.qualifiedName(), type.simpleName(),
                type.kind(), type.sourcePath(), type.packageName(), type.members(), type.annotations(), metadata);
    }

    private Optional<SourceLanguage> firstLanguage(ParsedCodeSourceRoot root) {
        return root.languageVersions().stream()
                .map(SourceLanguageVersion::language)
                .filter(language -> language != SourceLanguage.UNKNOWN)
                .findFirst();
    }

    private List<ParsedCodeRelation> normalizeRelations(List<ParsedCodeRelation> relations,
                                                        Map<String, ParsedCodeType> typesById) {
        Map<String, ParsedCodeRelation> out = new LinkedHashMap<>();
        for (ParsedCodeRelation relation : relations) {
            ParsedCodeType sourceType = typesById.get(relation.sourceTypeId());
            if (sourceType == null) {
                continue;
            }
            Optional<ParsedCodeType> target = resolveTarget(sourceType, relation.targetTypeName(), typesById);
            if (target.isPresent() && target.get().id().equals(sourceType.id())) {
                continue;
            }
            ParsedCodeRelation normalized = withResolvedTarget(relation, target);
            String key = relationKey(normalized);
            out.putIfAbsent(key, normalized);
        }
        return List.copyOf(out.values());
    }

    private Optional<ParsedCodeType> resolveTarget(ParsedCodeType sourceType, String targetName,
                                                   Map<String, ParsedCodeType> typesById) {
        String normalizedTarget = normalize(targetName);
        if (normalizedTarget.isBlank()) {
            return Optional.empty();
        }
        String packageQualified = sourceType.packageName().isBlank()
                ? normalizedTarget
                : sourceType.packageName() + "." + normalizedTarget;
        return typesById.values().stream()
                .filter(type -> type.sourceRootId().equals(sourceType.sourceRootId()))
                .filter(type -> type.qualifiedName().equals(packageQualified))
                .findFirst()
                .or(() -> typesById.values().stream()
                        .filter(type -> type.sourceRootId().equals(sourceType.sourceRootId()))
                        .filter(type -> type.simpleName().equals(normalizedTarget))
                        .findFirst())
                .or(() -> typesById.values().stream()
                        .filter(type -> type.qualifiedName().equals(normalizedTarget))
                        .findFirst())
                .or(() -> typesById.values().stream()
                        .filter(type -> type.simpleName().equals(normalizedTarget))
                        .findFirst());
    }

    private ParsedCodeRelation withResolvedTarget(ParsedCodeRelation relation, Optional<ParsedCodeType> target) {
        Map<String, String> metadata = new LinkedHashMap<>(relation.metadata());
        metadata.put(ParsedCodeMetadataKeys.RESOLVED, Boolean.toString(target.isPresent()));
        target.ifPresent(type -> {
            metadata.put(ParsedCodeMetadataKeys.TARGET_TYPE_ID, type.id());
            metadata.put(ParsedCodeMetadataKeys.TARGET_QUALIFIED_NAME, type.qualifiedName());
            metadata.put(ParsedCodeMetadataKeys.TARGET_SOURCE_ROOT_ID, type.sourceRootId());
        });
        return new ParsedCodeRelation(relation.id(), relation.sourceTypeId(), relation.targetTypeName(),
                relation.kind(), relation.description(), metadata);
    }

    private String relationKey(ParsedCodeRelation relation) {
        String targetKey = relation.metadata().getOrDefault(ParsedCodeMetadataKeys.TARGET_TYPE_ID,
                normalize(relation.targetTypeName()));
        return relation.sourceTypeId() + "|" + relation.kind() + "|" + targetKey;
    }

    private String inferModuleName(ParsedCodeType type) {
        if (!type.packageName().isBlank()) {
            return type.packageName();
        }
        if (type.sourcePath() != null && type.sourcePath().getParent() != null) {
            return type.sourcePath().getParent().toString().replace('\\', '.').replace('/', '.');
        }
        return "default";
    }

    private String sanitize(String value) {
        String normalized = normalize(value).toLowerCase(Locale.ROOT);
        return normalized.replaceAll("[^a-z0-9._-]+", "-").replaceAll("^-+|-+$", "");
    }

    private String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
