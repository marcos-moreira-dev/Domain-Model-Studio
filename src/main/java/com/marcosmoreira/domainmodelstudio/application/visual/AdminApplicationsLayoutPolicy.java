package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenKind;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Política de layout inicial para la familia de aplicaciones administrativas.
 *
 * <p>Estos proyectos no responden a una notación académica única como UML o BPMN.
 * Su valor está en que el usuario entienda módulos funcionales, navegación, permisos
 * y maquetas de pantalla. Por eso esta política evita la grilla genérica y crea
 * posiciones iniciales que parecen mapas de negocio: agrupadores, recorridos de
 * pantallas y componentes dentro de una pantalla wireframe.</p>
 */
public final class AdminApplicationsLayoutPolicy {

    private static final int ORDER_BUCKET = 1000;
    private static final int MODULE_CHILDREN_PER_ROW = 2;

    private final DependencyDrivenOrderingPolicy dependencyOrdering = new DependencyDrivenOrderingPolicy();
    private final WireframeVisualTextMetricsPolicy wireframeMetrics = new WireframeVisualTextMetricsPolicy();
    private final VisualTextFitPolicy textFitPolicy = new VisualTextFitPolicy();

    public List<VisualNodeReference> moduleMapReferences(ModuleMapDocument document, int startIndex) {
        Objects.requireNonNull(document, "document");
        List<VisualNodeReference> references = new ArrayList<>();
        List<ModuleNode> rootModules = orderedRootModules(document);
        Map<String, Integer> groupIndexes = groupIndexes(rootModules);
        Map<String, Integer> childCounters = new HashMap<>();

        int looseGroupIndex = rootModules.size();
        Map<String, ModuleGroupFootprint> childFootprints = moduleGroupFootprints(document);
        for (ModuleNode module : rootModules) {
            int groupIndex = groupIndexOrNext(groupIndexes, module.id(), looseGroupIndex);
            if (!groupIndexes.containsKey(module.id())) {
                looseGroupIndex++;
            }
            int children = descendantCount(document, module.id());
            ModuleGroupFootprint footprint = childFootprints.getOrDefault(module.id(), ModuleGroupFootprint.empty());
            VisualTextFitPolicy.BoxSize size = textFitPolicy.fitLargeCard(
                    new VisualTextFitPolicy.BoxSize(
                            moduleGroupWidth(children, footprint.maxChildWidth()),
                            moduleGroupHeight(children, footprint.maxChildHeight())),
                    module.displayName(),
                    detailFor(module));
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.module(module.id()),
                    size.width(),
                    size.height(),
                    encodeOrder(startIndex + groupIndex, 0)));
        }
        for (ModuleNode module : orderedDescendants(document, rootModules)) {
            String rootId = rootId(document, module.id());
            int groupIndex = groupIndexOrNext(groupIndexes, rootId, looseGroupIndex);
            if (!groupIndexes.containsKey(rootId)) {
                looseGroupIndex++;
            }
            int localIndex = childCounters.merge(rootId, 1, Integer::sum);
            VisualTextFitPolicy.BoxSize size = fittedModuleChildSize(module);
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.module(module.id()),
                    size.width(),
                    size.height(),
                    encodeOrder(startIndex + groupIndex, localIndex)));
        }
        return references;
    }

    public List<VisualNodeReference> screenFlowReferences(ScreenFlowDocument document, int startIndex) {
        Objects.requireNonNull(document, "document");
        List<VisualNodeReference> references = new ArrayList<>();
        Map<String, Integer> moduleIndexes = moduleIndexes(document.screens());
        Map<String, Integer> localCounters = new HashMap<>();
        for (ScreenNode screen : document.screens()) {
            String moduleKey = moduleKey(screen.moduleName());
            int groupIndex = moduleIndexes.getOrDefault(moduleKey, 0);
            int localIndex = localCounters.merge(moduleKey, 1, Integer::sum) - 1;
            ScreenSize baseSize = screenSize(screen.kind());
            VisualTextFitPolicy.BoxSize size = textFitPolicy.fitCard(
                    new VisualTextFitPolicy.BoxSize(baseSize.width(), baseSize.height()),
                    screen.displayName(),
                    screen.purpose().isBlank() ? screen.route() : screen.purpose());
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.screen(screen.id()),
                    size.width(),
                    size.height(),
                    encodeOrder(startIndex + groupIndex, localIndex)));
        }
        return references;
    }

    public List<VisualNodeReference> wireframeReferences(WireframeDocument document, int startIndex) {
        Objects.requireNonNull(document, "document");
        List<VisualNodeReference> references = new ArrayList<>();
        Map<String, Integer> screenIndexes = new LinkedHashMap<>();
        int screenIndex = 0;
        Map<String, Integer> componentCountByScreen = wireframeComponentCounts(document);
        for (WireframeScreen screen : document.screens()) {
            screenIndexes.put(screen.id(), screenIndex);
            WireframeVisualTextMetricsPolicy.ScreenSize size = wireframeMetrics.screenSize(
                    screen,
                    componentCountByScreen.getOrDefault(screen.id(), 0));
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.wireframeScreen(screen.id()),
                    size.width(),
                    size.height(),
                    encodeOrder(startIndex + screenIndex, 0)));
            screenIndex++;
        }
        Map<String, Integer> componentCounters = new HashMap<>();
        int orphanScreenIndex = screenIndexes.size();
        for (WireframeComponent component : document.components()) {
            int groupIndex = screenIndexes.getOrDefault(component.screenId(), orphanScreenIndex);
            if (!screenIndexes.containsKey(component.screenId())) {
                orphanScreenIndex++;
            }
            int localIndex = component.orderIndex() > 0
                    ? component.orderIndex()
                    : componentCounters.merge(component.screenId(), 1, Integer::sum);
            WireframeVisualTextMetricsPolicy.ScreenSize size = wireframeMetrics.componentSize(component);
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.wireframeComponent(component.id()),
                    size.width(),
                    size.height(),
                    encodeOrder(startIndex + groupIndex, localIndex)));
        }
        return references;
    }

    private Map<String, ModuleGroupFootprint> moduleGroupFootprints(ModuleMapDocument document) {
        Map<String, ModuleGroupFootprint> footprints = new HashMap<>();
        for (ModuleNode module : document.modules()) {
            if (module.rootModule()) {
                continue;
            }
            String rootId = rootId(document, module.id());
            VisualTextFitPolicy.BoxSize size = fittedModuleChildSize(module);
            footprints.merge(rootId, ModuleGroupFootprint.of(size), ModuleGroupFootprint::merge);
        }
        return footprints;
    }

    private VisualTextFitPolicy.BoxSize fittedModuleChildSize(ModuleNode module) {
        return textFitPolicy.fitCompactCard(
                new VisualTextFitPolicy.BoxSize(190.0, 78.0),
                module.displayName(),
                detailFor(module));
    }

    private static String detailFor(ModuleNode module) {
        if (module == null) {
            return "";
        }
        return module.description().isBlank() ? module.responsibility() : module.description();
    }

    private static Map<String, Integer> wireframeComponentCounts(WireframeDocument document) {
        Map<String, Integer> counts = new HashMap<>();
        for (WireframeComponent component : document.components()) {
            counts.merge(component.screenId(), 1, Integer::sum);
        }
        return counts;
    }

    private List<ModuleNode> orderedRootModules(ModuleMapDocument document) {
        List<ModuleNode> roots = rootModules(document);
        Map<String, ModuleNode> byId = modulesById(roots);
        List<String> orderedIds = dependencyOrdering.order(
                roots.stream().map(ModuleNode::id).toList(),
                document.dependencies().stream()
                        .map(dependency -> new DependencyDrivenOrderingPolicy.DirectedDependency(
                                rootId(document, dependency.sourceModuleId()),
                                rootId(document, dependency.targetModuleId())))
                        .toList());
        return orderedIds.stream().map(byId::get).filter(Objects::nonNull).toList();
    }

    private List<ModuleNode> orderedDescendants(ModuleMapDocument document, List<ModuleNode> orderedRoots) {
        List<ModuleNode> ordered = new ArrayList<>();
        for (ModuleNode root : orderedRoots) {
            List<ModuleNode> descendants = document.modules().stream()
                    .filter(module -> !module.rootModule())
                    .filter(module -> root.id().equals(rootId(document, module.id())))
                    .toList();
            Map<String, ModuleNode> byId = modulesById(descendants);
            List<String> orderedIds = dependencyOrdering.order(
                    descendants.stream().map(ModuleNode::id).toList(),
                    document.dependencies().stream()
                            .map(dependency -> new DependencyDrivenOrderingPolicy.DirectedDependency(
                                    dependency.sourceModuleId(), dependency.targetModuleId()))
                            .toList());
            orderedIds.stream().map(byId::get).filter(Objects::nonNull).forEach(ordered::add);
        }
        return ordered;
    }

    private static List<ModuleNode> rootModules(ModuleMapDocument document) {
        List<ModuleNode> roots = document.rootModules();
        if (!roots.isEmpty()) {
            return roots;
        }
        return document.modules().stream().filter(ModuleNode::rootModule).toList();
    }

    private static Map<String, ModuleNode> modulesById(List<ModuleNode> modules) {
        Map<String, ModuleNode> byId = new LinkedHashMap<>();
        for (ModuleNode module : modules) {
            byId.put(module.id(), module);
        }
        return byId;
    }

    private static String rootId(ModuleMapDocument document, String moduleId) {
        String current = moduleId == null ? "" : moduleId.strip();
        Map<String, ModuleNode> modules = modulesById(document.modules());
        for (int guard = 0; guard < document.modules().size(); guard++) {
            ModuleNode module = modules.get(current);
            if (module == null || module.parentId().isBlank()) {
                return module == null ? current : module.id();
            }
            current = module.parentId();
        }
        return current;
    }


    private static int groupIndexOrNext(Map<String, Integer> groupIndexes, String key, int nextIndex) {
        Integer value = groupIndexes.get(key);
        return value == null ? nextIndex : value;
    }

    private static Map<String, Integer> groupIndexes(List<ModuleNode> roots) {
        Map<String, Integer> indexes = new LinkedHashMap<>();
        int index = 0;
        for (ModuleNode root : roots) {
            indexes.put(root.id(), index++);
        }
        return indexes;
    }

    private static int descendantCount(ModuleMapDocument document, String moduleId) {
        return (int) document.modules().stream()
                .filter(module -> !module.rootModule())
                .filter(module -> moduleId.equals(rootId(document, module.id())))
                .count();
    }

    private static double moduleGroupWidth(int children, double maxChildWidth) {
        int columns = Math.min(MODULE_CHILDREN_PER_ROW, Math.max(1, children));
        double childWidth = Math.max(210.0, maxChildWidth);
        return Math.max(340.0, 96.0 + columns * childWidth + Math.max(0, columns - 1) * 42.0);
    }

    private static double moduleGroupHeight(int children, double maxChildHeight) {
        int rows = Math.max(1, (int) Math.ceil(Math.max(1, children) / (double) MODULE_CHILDREN_PER_ROW));
        double childHeight = Math.max(110.0, maxChildHeight);
        return Math.max(210.0, 126.0 + rows * childHeight + Math.max(0, rows - 1) * 38.0);
    }

    private static Map<String, Integer> moduleIndexes(List<ScreenNode> screens) {
        Map<String, Integer> indexes = new LinkedHashMap<>();
        for (ScreenNode screen : screens) {
            indexes.computeIfAbsent(moduleKey(screen.moduleName()), ignored -> indexes.size());
        }
        return indexes;
    }

    private static String moduleKey(String moduleName) {
        return moduleName == null || moduleName.isBlank() ? "Sin modulo" : moduleName.strip();
    }

    private static ScreenSize screenSize(ScreenKind kind) {
        return switch (kind == null ? ScreenKind.OTHER : kind) {
            case LOGIN -> new ScreenSize(178.0, 82.0);
            case DASHBOARD -> new ScreenSize(230.0, 98.0);
            case LIST -> new ScreenSize(218.0, 90.0);
            case FORM -> new ScreenSize(210.0, 92.0);
            case DETAIL -> new ScreenSize(210.0, 88.0);
            case REPORT -> new ScreenSize(224.0, 94.0);
            case SETTINGS -> new ScreenSize(218.0, 92.0);
            case OTHER -> new ScreenSize(200.0, 84.0);
        };
    }


    private static int encodeOrder(int groupIndex, int localIndex) {
        return Math.max(0, groupIndex) * ORDER_BUCKET + Math.max(0, localIndex);
    }

    private record ModuleGroupFootprint(double maxChildWidth, double maxChildHeight) {
        static ModuleGroupFootprint empty() {
            return new ModuleGroupFootprint(0.0, 0.0);
        }

        static ModuleGroupFootprint of(VisualTextFitPolicy.BoxSize size) {
            return new ModuleGroupFootprint(size.width(), size.height());
        }

        ModuleGroupFootprint merge(ModuleGroupFootprint other) {
            return new ModuleGroupFootprint(
                    Math.max(maxChildWidth, other.maxChildWidth),
                    Math.max(maxChildHeight, other.maxChildHeight));
        }
    }

    private record ScreenSize(double width, double height) { }
}
