package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Política de layout inicial para que UML Clases sea mapa estructural, no grilla genérica. */
public final class UmlClassLayoutPolicy {

    private static final int ORDER_BUCKET = 1000;
    private static final double MODULE_MIN_WIDTH = 720.0;
    private static final double MODULE_MIN_HEIGHT = 330.0;
    private static final double MODULE_HORIZONTAL_PADDING = 128.0;
    private static final double MODULE_VERTICAL_PADDING = 170.0;
    private static final double CLASS_COLUMN_GAP = 96.0;
    private static final double CLASS_ROW_GAP = 82.0;
    private static final int CLASSES_PER_ROW = 3;

    private final UmlClassBoxMetricsCalculator metricsCalculator = new UmlClassBoxMetricsCalculator();
    private final UmlClassModuleGroupingPolicy moduleGrouping = new UmlClassModuleGroupingPolicy();
    private final UmlClassNodeOrderingPolicy nodeOrdering = new UmlClassNodeOrderingPolicy();
    private final UmlSourceImportRenderProfile renderProfile;

    public UmlClassLayoutPolicy() {
        this(UmlSourceImportRenderProfile.safeDefault());
    }

    public UmlClassLayoutPolicy(UmlSourceImportRenderProfile renderProfile) {
        this.renderProfile = renderProfile == null ? UmlSourceImportRenderProfile.safeDefault() : renderProfile;
    }

    public UmlSourceImportRenderProfile renderProfile() {
        return renderProfile;
    }

    public List<VisualNodeReference> visualReferences(UmlClassDiagramDocument document, int startIndex) {
        Objects.requireNonNull(document, "document");
        ArrayList<VisualNodeReference> references = new ArrayList<>();
        List<UmlModuleGroup> orderedModules = orderedModules(document);
        Map<String, Integer> moduleIndexes = moduleIndexes(orderedModules);
        int moduleIndex = 0;
        for (UmlModuleGroup module : orderedModules) {
            ModuleBounds bounds = moduleBounds(document, module.id());
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.umlModule(module.id()),
                    bounds.width(),
                    bounds.height(),
                    encodeOrder(startIndex + moduleIndex, 0)));
            moduleIndex++;
        }
        Map<String, Integer> localIndexes = new HashMap<>();
        int looseIndex = 0;
        for (UmlClassNode umlClass : orderedClasses(document, orderedModules)) {
            int groupIndex = moduleIndexes.getOrDefault(umlClass.moduleId(), document.modules().size());
            int classIndex = localIndexes.merge(umlClass.moduleId(), 1, Integer::sum) - 1;
            if (umlClass.moduleId().isBlank() || !moduleIndexes.containsKey(umlClass.moduleId())) {
                classIndex = looseIndex++;
            }
            UmlClassBoxMetrics metrics = metricsCalculator.calculate(umlClass, renderProfile);
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.umlClass(umlClass.id()),
                    metrics.width(),
                    metrics.height(),
                    encodeOrder(startIndex + groupIndex, classIndex + 1)));
        }
        return references;
    }

    public UmlClassBoxMetrics metricsFor(UmlClassNode node) {
        return metricsCalculator.calculate(node, renderProfile);
    }

    private ModuleBounds moduleBounds(UmlClassDiagramDocument document, String moduleId) {
        List<UmlClassBoxMetrics> metrics = document.classes().stream()
                .filter(node -> node.moduleId().equals(moduleId))
                .map(node -> metricsCalculator.calculate(node, renderProfile))
                .toList();
        if (metrics.isEmpty()) {
            return new ModuleBounds(MODULE_MIN_WIDTH, MODULE_MIN_HEIGHT);
        }
        int columns = columnsForClassCount(metrics.size());
        int rows = Math.max(1, (int) Math.ceil(metrics.size() / (double) columns));
        double width = 0.0;
        for (int column = 0; column < columns; column++) {
            double columnWidth = 0.0;
            for (int index = column; index < metrics.size(); index += columns) {
                columnWidth = Math.max(columnWidth, metrics.get(index).width());
            }
            width += columnWidth;
            if (column + 1 < columns) {
                width += CLASS_COLUMN_GAP;
            }
        }
        double height = 0.0;
        for (int row = 0; row < rows; row++) {
            double rowHeight = 0.0;
            for (int column = 0; column < columns; column++) {
                int index = row * columns + column;
                if (index < metrics.size()) {
                    rowHeight = Math.max(rowHeight, metrics.get(index).height());
                }
            }
            height += rowHeight;
            if (row + 1 < rows) {
                height += CLASS_ROW_GAP;
            }
        }
        return new ModuleBounds(
                Math.max(MODULE_MIN_WIDTH, width + MODULE_HORIZONTAL_PADDING),
                Math.max(MODULE_MIN_HEIGHT, height + MODULE_VERTICAL_PADDING));
    }

    private List<UmlModuleGroup> orderedModules(UmlClassDiagramDocument document) {
        return moduleGrouping.orderModules(document);
    }

    private List<UmlClassNode> orderedClasses(UmlClassDiagramDocument document, List<UmlModuleGroup> orderedModules) {
        ArrayList<UmlClassNode> ordered = new ArrayList<>();
        for (UmlModuleGroup module : orderedModules) {
            ordered.addAll(orderedClassesInGroup(document, module.id()));
        }
        ordered.addAll(orderedClassesInGroup(document, ""));
        return ordered;
    }

    private List<UmlClassNode> orderedClassesInGroup(UmlClassDiagramDocument document, String moduleId) {
        List<UmlClassNode> classes = document.classes().stream()
                .filter(node -> node.moduleId().equals(moduleId))
                .toList();
        return nodeOrdering.orderClasses(classes, document.relations());
    }

    private static Map<String, Integer> moduleIndexes(List<UmlModuleGroup> modules) {
        Map<String, Integer> indexes = new HashMap<>();
        int index = 0;
        for (UmlModuleGroup module : modules) {
            indexes.put(module.id(), index++);
        }
        return indexes;
    }

    private static int columnsForClassCount(int classCount) {
        if (classCount <= 4) {
            return 1;
        }
        if (classCount <= 14) {
            return 2;
        }
        return CLASSES_PER_ROW;
    }

    private static int encodeOrder(int groupIndex, int localIndex) {
        return Math.max(0, groupIndex) * ORDER_BUCKET + Math.max(0, localIndex);
    }

    private record ModuleBounds(double width, double height) {
    }
}
