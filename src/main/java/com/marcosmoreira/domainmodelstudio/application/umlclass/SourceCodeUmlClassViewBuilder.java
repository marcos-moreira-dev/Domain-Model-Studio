package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMetadataKeys;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProject;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelation;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeSourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Construye vistas internas del diagrama UML generado desde código fuente multi-raíz. */
final class SourceCodeUmlClassViewBuilder {
    private final SourceCodeUmlSummarySelectionPolicy summarySelectionPolicy = new SourceCodeUmlSummarySelectionPolicy();

    List<UmlClassDiagramView> buildViews(ParsedCodeProject project, Map<String, String> moduleIdMap,
                                         Map<String, String> typeIdMap, Map<String, String> relationIdMap) {
        Objects.requireNonNull(project, "project");
        ArrayList<UmlClassDiagramView> views = new ArrayList<>();
        views.add(summaryView(project, moduleIdMap, typeIdMap, relationIdMap));
        for (ParsedCodeSourceRoot root : project.sourceRoots()) {
            views.add(sourceRootView(project, root, moduleIdMap, typeIdMap, relationIdMap));
        }
        integrationView(project, typeIdMap, relationIdMap).ifPresent(views::add);
        views.add(fullView(project, moduleIdMap, typeIdMap, relationIdMap));
        return views;
    }

    private UmlClassDiagramView summaryView(ParsedCodeProject project, Map<String, String> moduleIdMap,
                                            Map<String, String> typeIdMap, Map<String, String> relationIdMap) {
        SourceCodeUmlSummarySelectionPolicy.Selection selection = summarySelectionPolicy.select(project, typeIdMap, relationIdMap);
        return new UmlClassDiagramView("resumen", UmlClassDiagramViewKind.SUMMARY, "Resumen",
                "Vista inicial segura del sistema importado desde código fuente.",
                project.sourceRoots().stream().map(ParsedCodeSourceRoot::id).toList(),
                summaryModules(moduleIdMap, selection), selection.classIds(), selection.relationIds(),
                selection.notes(project.types().size(), project.relations().size()));
    }

    private List<String> summaryModules(Map<String, String> moduleIdMap,
                                        SourceCodeUmlSummarySelectionPolicy.Selection selection) {
        if (!selection.limited()) {
            return orderedValues(moduleIdMap);
        }
        // En vistas parciales no se guardan módulos como filtro: UmlClassDiagramView.includesClass()
        // incluye todas las clases de un módulo cuando moduleIds contiene ese módulo. Los contenedores
        // visuales se derivan desde las clases visibles en la capa de presentación.
        return List.of();
    }

    private UmlClassDiagramView sourceRootView(ParsedCodeProject project, ParsedCodeSourceRoot root,
                                               Map<String, String> moduleIdMap, Map<String, String> typeIdMap,
                                               Map<String, String> relationIdMap) {
        List<String> classIds = project.typesForRoot(root.id()).stream()
                .map(type -> typeIdMap.get(type.id()))
                .filter(id -> id != null && !id.isBlank())
                .toList();
        Set<String> classSet = new LinkedHashSet<>(classIds);
        List<String> relationIds = project.relations().stream()
                .filter(relation -> classSet.contains(typeIdMap.get(relation.sourceTypeId())))
                .filter(relation -> classSet.contains(typeIdMap.get(targetTypeId(relation))))
                .map(relation -> relationIdMap.get(relation.id()))
                .filter(id -> id != null && !id.isBlank())
                .distinct()
                .toList();
        return new UmlClassDiagramView(viewId(root), viewKind(root.kind()), displayName(root),
                "Vista filtrada para la raíz de código: " + root.displayName(),
                List.of(root.id()), modulesForRoot(project, root.id(), moduleIdMap), classIds, relationIds,
                "Generada desde SourceRoot " + root.id() + " (" + root.kind() + ").");
    }

    private java.util.Optional<UmlClassDiagramView> integrationView(ParsedCodeProject project,
                                                                    Map<String, String> typeIdMap,
                                                                    Map<String, String> relationIdMap) {
        LinkedHashSet<String> classIds = new LinkedHashSet<>();
        LinkedHashSet<String> relationIds = new LinkedHashSet<>();
        for (ParsedCodeRelation relation : project.relations()) {
            ParsedCodeType source = project.typeById(relation.sourceTypeId()).orElse(null);
            ParsedCodeType target = project.typeById(targetTypeId(relation)).orElse(null);
            if (source == null || target == null || source.sourceRootId().equals(target.sourceRootId())) {
                continue;
            }
            addIfPresent(classIds, typeIdMap.get(source.id()));
            addIfPresent(classIds, typeIdMap.get(target.id()));
            addIfPresent(relationIds, relationIdMap.get(relation.id()));
        }
        if (classIds.isEmpty()) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(new UmlClassDiagramView("integracion-api", UmlClassDiagramViewKind.INTEGRATION,
                "Integración API", "Relaciones inferidas entre raíces, por ejemplo Frontend ↔ Backend.",
                project.sourceRoots().stream().map(ParsedCodeSourceRoot::id).toList(), List.of(),
                List.copyOf(classIds), List.copyOf(relationIds),
                "Vista preparada para relaciones API; las inferencias profundas se completan en una tanda posterior."));
    }

    private UmlClassDiagramView fullView(ParsedCodeProject project, Map<String, String> moduleIdMap,
                                         Map<String, String> typeIdMap, Map<String, String> relationIdMap) {
        return new UmlClassDiagramView("mega-vista", UmlClassDiagramViewKind.FULL, "Mega vista",
                "Todos los elementos importados en una sola vista.",
                project.sourceRoots().stream().map(ParsedCodeSourceRoot::id).toList(),
                orderedValues(moduleIdMap), orderedValues(typeIdMap), orderedValues(relationIdMap),
                "Pensada para revisión global; en proyectos grandes debe usarse con lienzo expandible y confirmación explícita.");
    }

    private List<String> modulesForRoot(ParsedCodeProject project, String rootId, Map<String, String> moduleIdMap) {
        return project.modulesForRoot(rootId).stream()
                .map(module -> moduleIdMap.get(module.id()))
                .filter(id -> id != null && !id.isBlank())
                .toList();
    }

    private String viewId(ParsedCodeSourceRoot root) {
        String prefix = switch (root.kind()) {
            case BACKEND -> "backend";
            case FRONTEND -> "frontend";
            case SHARED -> "shared";
            case LIBRARY -> "library";
            case UNKNOWN -> "source";
        };
        return UmlClassDiagramIds.slug(prefix + " " + root.id(), "vista");
    }

    private UmlClassDiagramViewKind viewKind(SourceRootKind kind) {
        return switch (kind == null ? SourceRootKind.UNKNOWN : kind) {
            case BACKEND -> UmlClassDiagramViewKind.BACKEND;
            case FRONTEND -> UmlClassDiagramViewKind.FRONTEND;
            default -> UmlClassDiagramViewKind.SOURCE_ROOT;
        };
    }

    private String displayName(ParsedCodeSourceRoot root) {
        return switch (root.kind()) {
            case BACKEND -> "Backend " + root.displayName();
            case FRONTEND -> "Frontend " + root.displayName();
            case SHARED -> "Compartido " + root.displayName();
            case LIBRARY -> "Librería " + root.displayName();
            case UNKNOWN -> root.displayName();
        };
    }

    private String targetTypeId(ParsedCodeRelation relation) {
        return relation.metadata().getOrDefault(ParsedCodeMetadataKeys.TARGET_TYPE_ID, "");
    }

    private List<String> orderedValues(Map<String, String> values) {
        return values.values().stream().filter(value -> value != null && !value.isBlank()).distinct().toList();
    }

    private void addIfPresent(Set<String> target, String value) {
        if (value != null && !value.isBlank()) {
            target.add(value);
        }
    }

}
