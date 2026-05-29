package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Heurística prudente para clasificar relaciones estructurales detectadas desde miembros de código. */
public final class ParsedCodeRelationSemanticPolicy {

    private ParsedCodeRelationSemanticPolicy() {
    }

    public static ParsedCodeRelationKind kindForField(String memberName, String typeExpression, List<String> annotations) {
        String type = normalize(typeExpression);
        List<String> safeAnnotations = annotations == null ? List.of() : annotations;
        if (looksLikeInjectedDependency(memberName, type, safeAnnotations)) {
            return ParsedCodeRelationKind.DEPENDENCY;
        }
        if (hasCompositionEvidence(type, safeAnnotations)) {
            return ParsedCodeRelationKind.COMPOSITION;
        }
        if (hasAggregationEvidence(type, safeAnnotations)) {
            return ParsedCodeRelationKind.AGGREGATION;
        }
        return ParsedCodeRelationKind.ASSOCIATION;
    }

    public static Map<String, String> metadataForField(String memberName, String typeExpression,
                                                       List<String> annotations, String language) {
        ParsedCodeRelationKind kind = kindForField(memberName, typeExpression, annotations);
        LinkedHashMap<String, String> metadata = new LinkedHashMap<>();
        if (language != null && !language.isBlank()) {
            metadata.put(ParsedCodeMetadataKeys.LANGUAGE, language);
        }
        metadata.put(ParsedCodeMetadataKeys.RELATION_SOURCE_MEMBER, normalize(memberName));
        metadata.put(ParsedCodeMetadataKeys.RELATION_SOURCE_TYPE, normalize(typeExpression));
        metadata.put(ParsedCodeMetadataKeys.RELATION_SOURCE_PATTERN, patternFor(kind, typeExpression, annotations));
        metadata.put(ParsedCodeMetadataKeys.RELATION_OWNERSHIP_HINT, ownershipHint(kind));
        return Map.copyOf(metadata);
    }

    public static String descriptionForField(String memberName, ParsedCodeRelationKind kind) {
        String source = normalize(memberName).isBlank() ? "miembro" : "miembro " + normalize(memberName);
        return switch (kind == null ? ParsedCodeRelationKind.UNKNOWN : kind) {
            case COMPOSITION -> "Composición tentativa detectada desde " + source + " con ciclo de vida fuerte.";
            case AGGREGATION -> "Agregación tentativa detectada desde " + source + " como colección o referencia compartida.";
            case DEPENDENCY -> "Dependencia detectada desde " + source + " inyectado o de infraestructura.";
            default -> "Asociación tentativa detectada desde " + source + ".";
        };
    }

    private static boolean looksLikeInjectedDependency(String memberName, String typeExpression, List<String> annotations) {
        String type = normalize(typeExpression).toLowerCase(Locale.ROOT);
        String name = normalize(memberName).toLowerCase(Locale.ROOT);
        if (annotations.stream().anyMatch(ParsedCodeRelationSemanticPolicy::isInjectionAnnotation)) {
            return true;
        }
        return type.endsWith("service") || type.endsWith("repository") || type.endsWith("client")
                || type.endsWith("mapper") || type.endsWith("gateway") || type.endsWith("port")
                || type.endsWith("adapter") || name.endsWith("service") || name.endsWith("repository")
                || name.endsWith("client") || name.endsWith("mapper") || name.endsWith("gateway");
    }

    private static boolean hasCompositionEvidence(String typeExpression, List<String> annotations) {
        String joined = String.join(" ", annotations == null ? List.of() : annotations).toLowerCase(Locale.ROOT);
        return joined.contains("orphanremoval") && joined.contains("true")
                || joined.contains("cascadetype.all") || joined.contains("cascadetype.remove")
                || joined.contains("@embedded") || joined.contains("@elementcollection")
                || joined.contains("cascade") && (joined.contains("all") || joined.contains("remove"));
    }

    private static boolean hasAggregationEvidence(String typeExpression, List<String> annotations) {
        String type = normalize(typeExpression).toLowerCase(Locale.ROOT);
        String joined = String.join(" ", annotations == null ? List.of() : annotations).toLowerCase(Locale.ROOT);
        return type.contains("list<") || type.contains("set<") || type.contains("collection<")
                || type.contains("map<") || type.endsWith("[]") || type.contains("array<")
                || joined.contains("@onetomany") || joined.contains("@manytoone")
                || joined.contains("@manytomany") || joined.contains("@onetoone");
    }

    private static boolean isInjectionAnnotation(String annotation) {
        String normalized = normalize(annotation).toLowerCase(Locale.ROOT);
        return normalized.contains("@autowired") || normalized.contains("@inject")
                || normalized.contains("@resource") || normalized.contains("@value");
    }

    private static String patternFor(ParsedCodeRelationKind kind, String typeExpression, List<String> annotations) {
        String source = !String.join("", annotations == null ? List.of() : annotations).isBlank() ? "annotation" : "type";
        return (kind == null ? ParsedCodeRelationKind.UNKNOWN : kind).name().toLowerCase(Locale.ROOT) + ":" + source;
    }

    private static String ownershipHint(ParsedCodeRelationKind kind) {
        return switch (kind == null ? ParsedCodeRelationKind.UNKNOWN : kind) {
            case COMPOSITION -> "strong-lifecycle";
            case AGGREGATION -> "shared-reference";
            case DEPENDENCY -> "runtime-use";
            case ASSOCIATION -> "structural-reference";
            default -> "unknown";
        };
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
