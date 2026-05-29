package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMetadataKeys;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProject;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelation;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelationKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeSourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeRole;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Selecciona una vista Resumen segura para diagramas UML generados desde código fuente.
 *
 * <p>La política se mantiene en la capa de aplicación porque trabaja sobre el modelo neutral
 * de código parseado y no depende de JavaFX, layout visual ni controles de presentación; no cambia el layout visual, la persistencia ni la interacción del usuario. El
 * constructor de vistas solo consume el resultado para armar {@code UmlClassDiagramView}.</p>
 *
 * <p>El criterio prioriza controladores, servicios, repositorios, entidades/modelos, DTOs,
 * componentes frontend y relaciones API. Su objetivo es mantener legible el Resumen en proyectos
 * grandes; no cambia el parsing Java/TypeScript, la inferencia de relaciones ni el layout visual.</p>
 */

final class SourceCodeUmlSummarySelectionPolicy {
    private static final int SUMMARY_MAX_CLASSES = 120;
    private static final int SUMMARY_MAX_RELATIONS = 180;
    private static final int SUMMARY_MIN_CLASSES_PER_ROOT = 8;
    private static final int SUMMARY_MAX_CLASSES_PER_ROOT = 48;

    Selection select(ParsedCodeProject project, Map<String, String> typeIdMap, Map<String, String> relationIdMap) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(typeIdMap, "typeIdMap");
        Objects.requireNonNull(relationIdMap, "relationIdMap");
        if (project.types().size() <= SUMMARY_MAX_CLASSES) {
            return new Selection(orderedMappedTypeIds(project.types(), typeIdMap),
                    summaryRelationIds(project.relations(), typeIdMap.keySet(), relationIdMap, SUMMARY_MAX_RELATIONS),
                    false);
        }

        LinkedHashSet<String> selectedTypeIds = new LinkedHashSet<>();
        Map<String, Integer> relationDegree = relationDegree(project);

        // Las relaciones entre raíces explican integración Backend/Frontend y deben entrar primero.
        for (ParsedCodeRelation relation : project.relations()) {
            ParsedCodeType source = project.typeById(relation.sourceTypeId()).orElse(null);
            ParsedCodeType target = project.typeById(targetTypeId(relation)).orElse(null);
            if (source != null && target != null && !source.sourceRootId().equals(target.sourceRootId())) {
                selectedTypeIds.add(source.id());
                selectedTypeIds.add(target.id());
            }
        }

        int rootCount = Math.max(1, project.sourceRoots().size());
        int perRootLimit = Math.min(SUMMARY_MAX_CLASSES_PER_ROOT,
                Math.max(SUMMARY_MIN_CLASSES_PER_ROOT, SUMMARY_MAX_CLASSES / rootCount));
        for (ParsedCodeSourceRoot root : project.sourceRoots()) {
            addRankedTypes(selectedTypeIds, rankedTypes(project.typesForRoot(root.id()), relationDegree), perRootLimit);
        }
        addRankedTypes(selectedTypeIds, rankedTypes(project.types(), relationDegree), SUMMARY_MAX_CLASSES);

        LinkedHashSet<String> selectedClassIds = new LinkedHashSet<>();
        LinkedHashSet<String> visibleTypeIds = new LinkedHashSet<>();
        for (String typeId : selectedTypeIds) {
            String classId = typeIdMap.get(typeId);
            if (classId != null && !classId.isBlank() && selectedClassIds.add(classId)) {
                visibleTypeIds.add(typeId);
            }
            if (selectedClassIds.size() >= SUMMARY_MAX_CLASSES) {
                break;
            }
        }
        return new Selection(List.copyOf(selectedClassIds),
                summaryRelationIds(project.relations(), visibleTypeIds, relationIdMap, SUMMARY_MAX_RELATIONS),
                true);
    }

    private List<ParsedCodeType> rankedTypes(List<ParsedCodeType> types, Map<String, Integer> relationDegree) {
        return types.stream()
                .sorted(Comparator.comparingInt((ParsedCodeType type) -> score(type, relationDegree)).reversed()
                        .thenComparing(ParsedCodeType::sourceRootId)
                        .thenComparing(ParsedCodeType::moduleId)
                        .thenComparing(ParsedCodeType::simpleName)
                        .thenComparing(ParsedCodeType::id))
                .toList();
    }

    private void addRankedTypes(Set<String> selectedTypeIds, List<ParsedCodeType> rankedTypes, int limit) {
        int added = 0;
        for (ParsedCodeType type : rankedTypes) {
            if (selectedTypeIds.size() >= SUMMARY_MAX_CLASSES || added >= limit) {
                return;
            }
            if (selectedTypeIds.add(type.id())) {
                added++;
            }
        }
    }

    private int score(ParsedCodeType type, Map<String, Integer> relationDegree) {
        return rolePriority(type) + kindPriority(type.kind()) + relationDegree.getOrDefault(type.id(), 0) * 6
                + annotationBonus(type) + nameBonus(type.simpleName());
    }

    private int rolePriority(ParsedCodeType type) {
        String role = type.metadata().getOrDefault(ParsedCodeMetadataKeys.ROLE, ParsedCodeTypeRole.UNKNOWN.id());
        if (ParsedCodeTypeRole.BACKEND_CONTROLLER.id().equals(role)
                || ParsedCodeTypeRole.FRONTEND_COMPONENT.id().equals(role)) {
            return 120;
        }
        if (ParsedCodeTypeRole.BACKEND_SERVICE.id().equals(role)
                || ParsedCodeTypeRole.FRONTEND_SERVICE.id().equals(role)) {
            return 105;
        }
        if (ParsedCodeTypeRole.BACKEND_REPOSITORY.id().equals(role)) {
            return 90;
        }
        if (ParsedCodeTypeRole.BACKEND_ENTITY.id().equals(role)
                || ParsedCodeTypeRole.FRONTEND_MODEL.id().equals(role)
                || ParsedCodeTypeRole.DOMAIN_TYPE.id().equals(role)) {
            return 80;
        }
        if (ParsedCodeTypeRole.DTO.id().equals(role)) {
            return 72;
        }
        if (ParsedCodeTypeRole.MODULE.id().equals(role)
                || ParsedCodeTypeRole.CONFIGURATION.id().equals(role)
                || ParsedCodeTypeRole.GUARD.id().equals(role)
                || ParsedCodeTypeRole.INTERCEPTOR.id().equals(role)) {
            return 60;
        }
        return 35;
    }

    private int kindPriority(ParsedCodeTypeKind kind) {
        return switch (kind == null ? ParsedCodeTypeKind.UNKNOWN : kind) {
            case INTERFACE -> 18;
            case RECORD -> 12;
            case CLASS -> 10;
            case ENUM, TYPE_ALIAS -> 6;
            case UNKNOWN -> 0;
        };
    }

    private int annotationBonus(ParsedCodeType type) {
        return type.annotations().isEmpty() ? 0 : Math.min(18, type.annotations().size() * 3);
    }

    private int nameBonus(String simpleName) {
        String normalized = simpleName == null ? "" : simpleName.toLowerCase(java.util.Locale.ROOT);
        if (normalized.contains("controller") || normalized.contains("component")) {
            return 16;
        }
        if (normalized.contains("service") || normalized.contains("repository")) {
            return 12;
        }
        if (normalized.contains("entity") || normalized.contains("model") || normalized.endsWith("dto")) {
            return 8;
        }
        return 0;
    }

    private Map<String, Integer> relationDegree(ParsedCodeProject project) {
        Map<String, Integer> out = new LinkedHashMap<>();
        for (ParsedCodeRelation relation : project.relations()) {
            out.merge(relation.sourceTypeId(), 1, Integer::sum);
            String targetId = targetTypeId(relation);
            if (!targetId.isBlank()) {
                out.merge(targetId, 1, Integer::sum);
            }
        }
        return out;
    }

    private List<String> orderedMappedTypeIds(List<ParsedCodeType> types, Map<String, String> typeIdMap) {
        return types.stream()
                .map(type -> typeIdMap.get(type.id()))
                .filter(id -> id != null && !id.isBlank())
                .distinct()
                .toList();
    }

    private List<String> summaryRelationIds(List<ParsedCodeRelation> relations, Set<String> selectedTypeIds,
                                            Map<String, String> relationIdMap, int limit) {
        return relations.stream()
                .filter(relation -> selectedTypeIds.contains(relation.sourceTypeId()))
                .filter(relation -> selectedTypeIds.contains(targetTypeId(relation)))
                .sorted(Comparator.comparingInt(this::relationPriority).reversed()
                        .thenComparing(ParsedCodeRelation::id))
                .map(relation -> relationIdMap.get(relation.id()))
                .filter(id -> id != null && !id.isBlank())
                .distinct()
                .limit(limit)
                .toList();
    }

    private int relationPriority(ParsedCodeRelation relation) {
        ParsedCodeRelationKind kind = relation.kind() == null ? ParsedCodeRelationKind.UNKNOWN : relation.kind();
        int base = switch (kind) {
            case API_CALL -> 100;
            case EXTENDS, IMPLEMENTS -> 85;
            case COMPOSITION -> 78;
            case AGGREGATION -> 72;
            case ASSOCIATION -> 65;
            case DEPENDENCY -> 45;
            case UNKNOWN -> 20;
        };
        return Boolean.parseBoolean(relation.metadata().getOrDefault(ParsedCodeMetadataKeys.INFERRED, "false"))
                ? base - 5
                : base;
    }

    private String targetTypeId(ParsedCodeRelation relation) {
        return relation.metadata().getOrDefault(ParsedCodeMetadataKeys.TARGET_TYPE_ID, "");
    }

    record Selection(List<String> classIds, List<String> relationIds, boolean limited) {
        Selection {
            classIds = List.copyOf(classIds == null ? List.of() : classIds);
            relationIds = List.copyOf(relationIds == null ? List.of() : relationIds);
        }

        String notes(int totalClasses, int totalRelations) {
            if (!limited) {
                return "Vista inicial completa porque el proyecto importado es pequeño o mediano.";
            }
            return "Vista inicial segura: muestra " + classIds.size() + " de " + totalClasses
                    + " clases y " + relationIds.size() + " de " + totalRelations
                    + " relaciones. Use Backend/Frontend/Integración API o Mega vista para revisar más detalle.";
        }
    }
}
