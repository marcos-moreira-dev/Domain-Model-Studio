package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Infiere relaciones internas de arquitectura cuando el código no expone una referencia directa simple.
 *
 * <p>Esta política trabaja sobre el modelo neutral ya normalizado y no depende de Java, TypeScript,
 * Spring, Angular ni del dominio UML. Su objetivo es complementar relaciones de campos/herencia con
 * patrones prudentes: Controller → Service, Service → Repository, Repository → Entity y
 * Component → Service, entre otros. No crea relaciones entre raíces distintas; eso queda reservado para
 * la futura vista de integración API.</p>
 */
public final class ParsedCodeInternalRelationInferencePolicy {
    private static final List<String> STEM_SUFFIXES = List.of(
            "restcontroller", "controller", "resource", "serviceimpl", "apiservice", "httpservice",
            "repository", "component", "interceptor", "configuration", "directive", "resolver", "service",
            "entity", "module", "mapper", "request", "response", "model", "guard", "store", "effect",
            "effects", "page", "view", "dao", "dto", "config", "pipe", "type");

    public List<ParsedCodeRelation> inferInternalRelations(ParsedCodeProject project) {
        if (project == null || project.types().isEmpty()) {
            return List.of();
        }
        Map<String, ParsedCodeType> typesById = typesById(project.types());
        Set<String> existingPairs = existingSemanticPairs(project.relations(), typesById);
        ArrayList<ParsedCodeRelation> out = new ArrayList<>();
        for (ParsedCodeType source : project.types()) {
            for (RelationRule rule : rulesFor(source)) {
                inferForRule(project, source, rule, existingPairs).ifPresent(out::add);
            }
        }
        return List.copyOf(out);
    }

    private Optional<ParsedCodeRelation> inferForRule(ParsedCodeProject project, ParsedCodeType source,
                                                      RelationRule rule, Set<String> existingPairs) {
        List<ScoredType> candidates = project.types().stream()
                .filter(target -> !target.id().equals(source.id()))
                .filter(target -> target.sourceRootId().equals(source.sourceRootId()))
                .filter(target -> rule.accepts(target))
                .map(target -> score(source, target, rule))
                .filter(scored -> scored.score() >= rule.minimumScore())
                .sorted(Comparator.comparingInt(ScoredType::score).reversed()
                        .thenComparing(scored -> scored.type().simpleName()))
                .toList();
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        ScoredType best = candidates.getFirst();
        String pairKey = source.id() + "->" + best.type().id();
        if (existingPairs.contains(pairKey)) {
            return Optional.empty();
        }
        existingPairs.add(pairKey);
        return Optional.of(relationFor(source, best.type(), rule, best.score()));
    }

    private ScoredType score(ParsedCodeType source, ParsedCodeType target, RelationRule rule) {
        String sourceStem = semanticStem(source.simpleName());
        String targetStem = semanticStem(target.simpleName());
        int score = 0;
        if (!sourceStem.isBlank() && sourceStem.equals(targetStem)) {
            score += 80;
        } else if (!sourceStem.isBlank() && !targetStem.isBlank()
                && (sourceStem.contains(targetStem) || targetStem.contains(sourceStem))) {
            score += 55;
        }
        if (!source.moduleId().isBlank() && source.moduleId().equals(target.moduleId())) {
            score += 25;
        }
        if (sameLanguage(source, target)) {
            score += 10;
        }
        if (rule.preferredTargetRole().equals(roleOf(target))) {
            score += 10;
        }
        return new ScoredType(target, score);
    }

    private ParsedCodeRelation relationFor(ParsedCodeType source, ParsedCodeType target, RelationRule rule, int score) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ParsedCodeMetadataKeys.INFERRED, "true");
        metadata.put(ParsedCodeMetadataKeys.INFERENCE_REASON, rule.reason());
        metadata.put(ParsedCodeMetadataKeys.SOURCE_ROLE, roleOf(source));
        metadata.put(ParsedCodeMetadataKeys.TARGET_ROLE, roleOf(target));
        metadata.put(ParsedCodeMetadataKeys.TARGET_TYPE_ID, target.id());
        metadata.put(ParsedCodeMetadataKeys.TARGET_QUALIFIED_NAME, target.qualifiedName());
        metadata.put(ParsedCodeMetadataKeys.TARGET_SOURCE_ROOT_ID, target.sourceRootId());
        metadata.put(ParsedCodeMetadataKeys.RESOLVED, "true");
        metadata.put("score", Integer.toString(score));
        languageOf(source).ifPresent(language -> metadata.put(ParsedCodeMetadataKeys.LANGUAGE, language));
        String id = source.id() + ":inferred:" + rule.kind().name().toLowerCase(Locale.ROOT)
                + ":" + sanitize(target.simpleName());
        String description = rule.description(source, target);
        return new ParsedCodeRelation(id, source.id(), target.qualifiedName(), rule.kind(), description, metadata);
    }

    private List<RelationRule> rulesFor(ParsedCodeType source) {
        String role = roleOf(source);
        SourceRootKind rootKind = rootKindOf(source);
        ArrayList<RelationRule> rules = new ArrayList<>();
        if (ParsedCodeTypeRole.BACKEND_CONTROLLER.id().equals(role)) {
            rules.add(rule(ParsedCodeTypeRole.BACKEND_SERVICE, ParsedCodeRelationKind.DEPENDENCY,
                    "controller-service", "Controlador backend vinculado con servicio de aplicación."));
            rules.add(rule(ParsedCodeTypeRole.DTO, ParsedCodeRelationKind.DEPENDENCY,
                    "controller-dto", "Controlador backend vinculado con DTO de entrada/salida."));
        }
        if (ParsedCodeTypeRole.BACKEND_SERVICE.id().equals(role)) {
            rules.add(rule(ParsedCodeTypeRole.BACKEND_REPOSITORY, ParsedCodeRelationKind.DEPENDENCY,
                    "service-repository", "Servicio backend vinculado con repositorio de persistencia."));
            rules.add(rule(ParsedCodeTypeRole.BACKEND_ENTITY, ParsedCodeRelationKind.DEPENDENCY,
                    "service-entity", "Servicio backend vinculado con entidad de dominio/persistencia."));
            rules.add(rule(ParsedCodeTypeRole.DTO, ParsedCodeRelationKind.DEPENDENCY,
                    "service-dto", "Servicio backend vinculado con DTO relacionado."));
        }
        if (ParsedCodeTypeRole.BACKEND_REPOSITORY.id().equals(role)) {
            rules.add(rule(ParsedCodeTypeRole.BACKEND_ENTITY, ParsedCodeRelationKind.ASSOCIATION,
                    "repository-entity", "Repositorio backend vinculado con entidad persistida."));
        }
        if (ParsedCodeTypeRole.FRONTEND_COMPONENT.id().equals(role)) {
            rules.add(rule(ParsedCodeTypeRole.FRONTEND_SERVICE, ParsedCodeRelationKind.DEPENDENCY,
                    "component-service", "Componente frontend vinculado con servicio de UI/API."));
            rules.add(rule(ParsedCodeTypeRole.FRONTEND_MODEL, ParsedCodeRelationKind.DEPENDENCY,
                    "component-model", "Componente frontend vinculado con modelo de vista."));
        }
        if (ParsedCodeTypeRole.FRONTEND_SERVICE.id().equals(role)) {
            rules.add(rule(ParsedCodeTypeRole.FRONTEND_MODEL, ParsedCodeRelationKind.DEPENDENCY,
                    "service-model", "Servicio frontend vinculado con modelo o interfaz de datos."));
            rules.add(rule(ParsedCodeTypeRole.DTO, ParsedCodeRelationKind.DEPENDENCY,
                    "service-dto", "Servicio frontend vinculado con DTO relacionado."));
        }
        if (ParsedCodeTypeRole.GUARD.id().equals(role) || ParsedCodeTypeRole.INTERCEPTOR.id().equals(role)) {
            rules.add(rule(ParsedCodeTypeRole.FRONTEND_SERVICE, ParsedCodeRelationKind.DEPENDENCY,
                    "frontend-infrastructure-service", "Elemento de infraestructura frontend vinculado con servicio."));
        }
        if (rootKind == SourceRootKind.FRONTEND && ParsedCodeTypeRole.DOMAIN_TYPE.id().equals(role)) {
            rules.add(rule(ParsedCodeTypeRole.FRONTEND_MODEL, ParsedCodeRelationKind.DEPENDENCY,
                    "frontend-domain-model", "Tipo frontend genérico vinculado con modelo de datos."));
        }
        return List.copyOf(rules);
    }

    private RelationRule rule(ParsedCodeTypeRole targetRole, ParsedCodeRelationKind kind, String reason,
                              String description) {
        return new RelationRule(targetRole.id(), kind, reason, description, 80);
    }

    private Set<String> existingSemanticPairs(List<ParsedCodeRelation> relations, Map<String, ParsedCodeType> typesById) {
        Set<String> out = new LinkedHashSet<>();
        for (ParsedCodeRelation relation : relations) {
            String targetId = relation.metadata().get(ParsedCodeMetadataKeys.TARGET_TYPE_ID);
            if (targetId == null || targetId.isBlank()) {
                targetId = resolveByName(relation.targetTypeName(), typesById).map(ParsedCodeType::id).orElse("");
            }
            if (!targetId.isBlank()) {
                out.add(relation.sourceTypeId() + "->" + targetId);
            }
        }
        return out;
    }

    private Optional<ParsedCodeType> resolveByName(String targetName, Map<String, ParsedCodeType> typesById) {
        String normalized = normalize(targetName);
        if (normalized.isBlank()) {
            return Optional.empty();
        }
        return typesById.values().stream()
                .filter(type -> type.id().equals(normalized) || type.qualifiedName().equals(normalized)
                        || type.simpleName().equals(normalized))
                .findFirst();
    }

    private Map<String, ParsedCodeType> typesById(List<ParsedCodeType> types) {
        Map<String, ParsedCodeType> out = new LinkedHashMap<>();
        for (ParsedCodeType type : types) {
            out.putIfAbsent(type.id(), type);
        }
        return out;
    }

    private boolean sameLanguage(ParsedCodeType source, ParsedCodeType target) {
        return languageOf(source).equals(languageOf(target));
    }

    private Optional<String> languageOf(ParsedCodeType type) {
        String language = type.metadata().getOrDefault(ParsedCodeMetadataKeys.LANGUAGE, "").strip();
        return language.isBlank() ? Optional.empty() : Optional.of(language);
    }

    private SourceRootKind rootKindOf(ParsedCodeType type) {
        String value = type.metadata().getOrDefault(ParsedCodeMetadataKeys.SOURCE_ROOT_KIND, "").strip().toUpperCase(Locale.ROOT);
        if (value.isBlank()) {
            return SourceRootKind.UNKNOWN;
        }
        try {
            return SourceRootKind.valueOf(value);
        } catch (IllegalArgumentException ignored) {
            return SourceRootKind.UNKNOWN;
        }
    }

    private String roleOf(ParsedCodeType type) {
        return type.metadata().getOrDefault(ParsedCodeMetadataKeys.ROLE, ParsedCodeTypeRole.UNKNOWN.id());
    }

    private String semanticStem(String name) {
        String value = normalize(name).replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
        for (String suffix : STEM_SUFFIXES) {
            if (value.endsWith(suffix) && value.length() > suffix.length() + 1) {
                return value.substring(0, value.length() - suffix.length());
            }
        }
        return value;
    }

    private String sanitize(String value) {
        return normalize(value).replaceAll("[^A-Za-z0-9_.-]+", "-");
    }

    private String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    private record ScoredType(ParsedCodeType type, int score) {
    }

    private record RelationRule(String preferredTargetRole, ParsedCodeRelationKind kind, String reason,
                                String baseDescription, int minimumScore) {
        boolean accepts(ParsedCodeType type) {
            return preferredTargetRole.equals(type.metadata().get(ParsedCodeMetadataKeys.ROLE));
        }

        String description(ParsedCodeType source, ParsedCodeType target) {
            return baseDescription + " Patrón inferido: " + source.simpleName() + " → " + target.simpleName() + ".";
        }
    }
}
