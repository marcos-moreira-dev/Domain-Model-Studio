package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramViewState;
import com.marcosmoreira.domainmodelstudio.domain.layout.*;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.style.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/** Lee layout, estilos y estado visual general de proyectos .dms. */
final class DmsProjectVisualStateJsonReader {

    DiagramLayouts readLayouts(Map<String, Object> object, NotationType metadataActiveNotation) {
        NotationType active = enumValue(
                NotationType.class,
                stringOrDefault(object, "activeNotation", metadataActiveNotation.name()),
                metadataActiveNotation
        );
        Map<NotationType, DiagramLayout> layouts = new EnumMap<>(NotationType.class);
        if (object.containsKey("byNotation")) {
            Map<String, Object> byNotation = object(object, "byNotation");
            for (Map.Entry<String, Object> entry : byNotation.entrySet()) {
                NotationType notation = enumValue(NotationType.class, entry.getKey(), NotationType.CHEN);
                layouts.put(notation, readLayout(notation, asObject(entry.getValue(), "layout")));
            }
        } else {
            for (Map.Entry<String, Object> entry : object.entrySet()) {
                if (!(entry.getValue() instanceof Map<?, ?>)) {
                    continue;
                }
                NotationType notation = enumValue(NotationType.class, entry.getKey(), null);
                if (notation != null) {
                    layouts.put(notation, readLayout(notation, asObject(entry.getValue(), "layout")));
                }
            }
        }
        if (layouts.isEmpty()) {
            layouts.put(active, DiagramLayout.empty(active));
        }
        return new DiagramLayouts(active, layouts);
    }

    private DiagramLayout readLayout(NotationType notation, Map<String, Object> object) {
        List<NodeLayout> nodes = new ArrayList<>();
        Object nodesValue = object.get("nodes");
        if (nodesValue instanceof List<?> nodeList) {
            for (Object nodeValue : nodeList) {
                Map<String, Object> node = asObject(nodeValue, "node");
                nodes.add(readNodeLayout(string(node, "elementId"), node));
            }
        } else if (nodesValue instanceof Map<?, ?> nodeMap) {
            for (Map.Entry<String, Object> entry : asStringObjectMap(nodeMap).entrySet()) {
                nodes.add(readNodeLayout(entry.getKey(), asObject(entry.getValue(), "node")));
            }
        }

        List<ConnectorLayout> connectors = new ArrayList<>();
        Object connectorsValue = object.get("connectors");
        if (connectorsValue instanceof List<?> connectorList) {
            for (Object connectorValue : connectorList) {
                connectors.add(readConnector(asObject(connectorValue, "connector"), null));
            }
        } else if (connectorsValue instanceof Map<?, ?> connectorMap) {
            for (Map.Entry<String, Object> entry : asStringObjectMap(connectorMap).entrySet()) {
                connectors.add(readConnector(asObject(entry.getValue(), "connector"), entry.getKey()));
            }
        }
        return new DiagramLayout(notation, nodes, connectors);
    }

    private NodeLayout readNodeLayout(String elementId, Map<String, Object> node) {
        return new NodeLayout(
                DiagramElementId.of(elementId),
                DiagramPoint.of(number(node, "x"), number(node, "y")),
                DiagramSize.of(number(node, "width"), number(node, "height")),
                boolOrDefault(node, "visible", true),
                boolOrDefault(node, "locked", false)
        );
    }

    private ConnectorLayout readConnector(Map<String, Object> object) {
        return readConnector(object, null);
    }

    private ConnectorLayout readConnector(Map<String, Object> object, String fallbackConnectorId) {
        List<BendPoint> bendPoints = new ArrayList<>();
        if (object.containsKey("bendPoints")) {
            for (Object bendValue : array(object, "bendPoints")) {
                Map<String, Object> bend = asObject(bendValue, "bendPoint");
                bendPoints.add(BendPoint.of(number(bend, "x"), number(bend, "y")));
            }
        }
        return new ConnectorLayout(
                DiagramElementId.of(stringOrDefault(object, "connectorId", fallbackConnectorId == null ? "connector" : fallbackConnectorId)),
                DiagramElementId.of(stringOrDefault(object, "sourceElementId", stringOrDefault(object, "source", ""))),
                DiagramElementId.of(stringOrDefault(object, "targetElementId", stringOrDefault(object, "target", ""))),
                enumValue(AnchorSide.class, stringOrDefault(object, "sourceAnchor", "AUTO"), AnchorSide.AUTO),
                enumValue(AnchorSide.class, stringOrDefault(object, "targetAnchor", "AUTO"), AnchorSide.AUTO),
                enumValue(ConnectorPathKind.class, stringOrDefault(object, "pathKind", "STRAIGHT"), ConnectorPathKind.STRAIGHT),
                bendPoints,
                enumValue(ConnectorMarker.class, stringOrDefault(object, "sourceMarker", "NONE"), ConnectorMarker.NONE),
                enumValue(ConnectorMarker.class, stringOrDefault(object, "targetMarker", "NONE"), ConnectorMarker.NONE),
                enumValue(MarkerOrientation.class, stringOrDefault(object, "sourceMarkerOrientation", "AUTO"), MarkerOrientation.AUTO),
                enumValue(MarkerOrientation.class, stringOrDefault(object, "targetMarkerOrientation", "AUTO"), MarkerOrientation.AUTO),
                numberOrDefault(object, "labelOffsetX", 0.0),
                numberOrDefault(object, "labelOffsetY", 0.0),
                boolOrDefault(object, "visible", true)
        );
    }

    DiagramStyleSheet readStyleSheet(Map<String, Object> object) {
        ElementStyle defaultStyle = object.containsKey("default")
                ? readElementStyle(asObject(object.get("default"), "defaultStyle"))
                : ElementStyle.defaultElement();
        DiagramAppearance appearance = object.containsKey("appearance")
                ? readAppearance(asObject(object.get("appearance"), "appearance"))
                : DiagramAppearance.defaults();
        Map<DiagramElementId, ElementStyle> styles = new LinkedHashMap<>();
        if (object.containsKey("elements")) {
            Map<String, Object> elements = object(object, "elements");
            for (Map.Entry<String, Object> entry : elements.entrySet()) {
                styles.put(DiagramElementId.of(entry.getKey()), readElementStyle(asObject(entry.getValue(), "elementStyle")));
            }
        }
        return new DiagramStyleSheet(defaultStyle, styles, appearance);
    }

    private DiagramAppearance readAppearance(Map<String, Object> object) {
        return new DiagramAppearance(
                RgbaColor.fromHex(stringOrDefault(object, "workspaceBackground", "#EEF2F6")),
                RgbaColor.fromHex(stringOrDefault(object, "diagramBackground", "#FFFFFF"))
        );
    }

    private ElementStyle readElementStyle(Map<String, Object> object) {
        RgbaColor fill = RgbaColor.fromHex(stringOrDefault(object, "fill", "#FFFFFF"));
        RgbaColor stroke = RgbaColor.fromHex(stringOrDefault(object, "stroke", "#505050"));
        RgbaColor text = RgbaColor.fromHex(stringOrDefault(object, "textColor", "#232323"));
        double strokeWidth = numberOrDefault(object, "strokeWidth", 1.0);
        StrokePattern strokePattern = readStrokePattern(stringOrDefault(object, "strokePattern", "solid"));
        String fontFamily = stringOrDefault(object, "fontFamily", "Segoe UI");
        double fontSize = numberOrDefault(object, "fontSize", 12.0);
        return new ElementStyle(
                FillStyle.of(fill),
                new StrokeStyle(stroke, strokeWidth, strokePattern),
                new TextStyle(fontFamily, fontSize, text, null, null)
        );
    }

    DiagramViewState readViewState(Map<String, Object> object) {
        LinkedHashSet<String> collapsedGroups = new LinkedHashSet<>();
        for (Object value : array(object, "collapsedGroups")) {
            String normalized = String.valueOf(value).trim();
            if (!normalized.isEmpty()) {
                collapsedGroups.add(normalized);
            }
        }

        LinkedHashMap<String, Boolean> filters = new LinkedHashMap<>();
        if (object.containsKey("filters")) {
            Map<String, Object> rawFilters = object(object, "filters");
            for (Map.Entry<String, Object> entry : rawFilters.entrySet()) {
                filters.put(entry.getKey(), booleanValue(entry.getValue()));
            }
        }

        return new DiagramViewState(
                numberOrDefault(object, "zoomFactor", 1.0),
                numberOrDefault(object, "viewportX", 0.0),
                numberOrDefault(object, "viewportY", 0.0),
                stringOrDefault(object, "viewMode", DiagramViewState.DEFAULT_VIEW_MODE),
                collapsedGroups,
                filters
        );
    }




    private StrokePattern readStrokePattern(String value) {
        String normalized = value == null ? "" : value.trim().toLowerCase(java.util.Locale.ROOT);
        return switch (normalized) {
            case "dash", "dashed" -> StrokePattern.DASHED;
            case "dot", "dotted" -> StrokePattern.DOTTED;
            default -> StrokePattern.SOLID;
        };
    }

    private Map<String, Object> object(Map<String, Object> parent, String key) {
        return asObject(parent.get(key), key);
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

    private Map<String, Object> asStringObjectMap(Map<?, ?> value) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            result.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return result;
    }

    private Map<String, Object> asObject(Object value, String context) {
        if (value instanceof Map<?, ?> map) {
            return asStringObjectMap(map);
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

    private double number(Map<String, Object> object, String key) {
        Object value = object.get(key);
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(String.valueOf(value));
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

    private boolean booleanValue(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    private boolean boolOrDefault(Map<String, Object> object, String key, boolean defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : booleanValue(value);
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
