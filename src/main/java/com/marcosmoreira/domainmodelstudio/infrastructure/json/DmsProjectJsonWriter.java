package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramViewState;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramAppearance;
import com.marcosmoreira.domainmodelstudio.domain.style.StrokePattern;
import java.util.Map;

/**
 * Escritor JSON manual del formato {@code .dms}.
 *
 * <p>El archivo resultante es el formato durable de la aplicación: debe preservar el
 * documento especializado, el layout visual y los metadatos necesarios para reabrir la
 * misma intención de proyecto. La escritura es estable para facilitar revisión y
 * diagnóstico de roundtrip.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> guardar un Grafo lógico debe conservar tanto
 * la semántica ({@code CU-001 requiere PRE-001}) como la posición visual del nodo.
 * Por eso el escritor emite modelo especializado y layouts en secciones separadas.</p>
 */
public final class DmsProjectJsonWriter {

    public String write(DiagramProject project) {
        new DmsProjectPayloadConsistencyValidator().validate(project);
        StringBuilder json = new StringBuilder(28_000);
        json.append("{\n");
        numberField(json, 1, "formatVersion", DmsProjectFormat.CURRENT_FORMAT_VERSION, true);
        writeProject(project, json, 1);
        json.append(",\n");
        new DmsProjectConceptualModelJsonWriter().writeModel(project, json, 1);
        json.append(",\n");
        writeLayouts(project, json, 1);
        json.append(",\n");
        writeStyles(project, json, 1);
        json.append(",\n");
        writeView(project.viewState(), json, 1);
        json.append(",\n");
        new DmsProjectAssetsJsonWriter().write(project.assetCatalog(), json, 1);
        json.append("\n}\n");
        return json.toString();
    }

    private void writeProject(DiagramProject project, StringBuilder json, int level) {
        indent(json, level).append("\"project\": {\n");
        field(json, level + 1, "id", project.metadata().id(), true);
        field(json, level + 1, "title", project.metadata().title(), true);
        field(json, level + 1, "projectType", project.metadata().projectType().name(), true);
        field(json, level + 1, "diagramTypeId", project.metadata().diagramTypeId().value(), true);
        field(json, level + 1, "documentVersion", "2", true);
        field(json, level + 1, "version", project.metadata().version(), true);
        field(json, level + 1, "status", project.metadata().status(), true);
        field(json, level + 1, "activeNotation", project.metadata().activeNotation().name(), true);
        field(json, level + 1, "sourceMarkdownPath", project.metadata().sourceMarkdownPath(), true);
        field(json, level + 1, "description", project.metadata().description(), false);
        indent(json, level).append("}");
    }

    private void writeLayouts(DiagramProject project, StringBuilder json, int level) {
        indent(json, level).append("\"layouts\": {\n");
        indent(json, level + 1).append("\"activeNotation\": ").append(quote(project.layouts().activeNotation().name())).append(",\n");
        indent(json, level + 1).append("\"byNotation\": {\n");
        int notationIndex = 0;
        for (Map.Entry<NotationType, DiagramLayout> entry : project.layouts().layoutsByNotation().entrySet()) {
            indent(json, level + 2).append(quote(entry.getKey().name())).append(": {\n");
            writeLayout(entry.getValue(), json, level + 3);
            indent(json, level + 2).append("}").append(++notationIndex < project.layouts().layoutsByNotation().size() ? "," : "").append("\n");
        }
        indent(json, level + 1).append("}\n");
        indent(json, level).append("}");
    }

    private void writeLayout(DiagramLayout layout, StringBuilder json, int level) {
        indent(json, level).append("\"nodes\": [\n");
        for (int i = 0; i < layout.nodes().size(); i++) {
            NodeLayout node = layout.nodes().get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "elementId", node.elementId().value(), true);
            numberField(json, level + 2, "x", node.x(), true);
            numberField(json, level + 2, "y", node.y(), true);
            numberField(json, level + 2, "width", node.width(), true);
            numberField(json, level + 2, "height", node.height(), true);
            booleanField(json, level + 2, "visible", node.visible(), true);
            booleanField(json, level + 2, "locked", node.locked(), false);
            indent(json, level + 1).append("}").append(i + 1 < layout.nodes().size() ? "," : "").append("\n");
        }
        indent(json, level).append("],\n");
        indent(json, level).append("\"connectors\": [\n");
        for (int i = 0; i < layout.connectors().size(); i++) {
            ConnectorLayout connector = layout.connectors().get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "connectorId", connector.connectorId().value(), true);
            field(json, level + 2, "sourceElementId", connector.sourceElementId().value(), true);
            field(json, level + 2, "targetElementId", connector.targetElementId().value(), true);
            field(json, level + 2, "sourceAnchor", connector.sourceAnchor().name(), true);
            field(json, level + 2, "targetAnchor", connector.targetAnchor().name(), true);
            field(json, level + 2, "pathKind", connector.pathKind().name(), true);
            writeBendPoints(connector, json, level + 2, true);
            field(json, level + 2, "sourceMarker", connector.sourceMarker().name(), true);
            field(json, level + 2, "targetMarker", connector.targetMarker().name(), true);
            field(json, level + 2, "sourceMarkerOrientation", connector.sourceMarkerOrientation().name(), true);
            field(json, level + 2, "targetMarkerOrientation", connector.targetMarkerOrientation().name(), true);
            numberField(json, level + 2, "labelOffsetX", connector.labelOffsetX(), true);
            numberField(json, level + 2, "labelOffsetY", connector.labelOffsetY(), true);
            booleanField(json, level + 2, "visible", connector.visible(), false);
            indent(json, level + 1).append("}").append(i + 1 < layout.connectors().size() ? "," : "").append("\n");
        }
        indent(json, level).append("]\n");
    }


    private void writeBendPoints(ConnectorLayout connector, StringBuilder json, int level, boolean comma) {
        indent(json, level).append(quote("bendPoints")).append(": [");
        for (int i = 0; i < connector.bendPoints().size(); i++) {
            var bendPoint = connector.bendPoints().get(i);
            json.append("{\"x\":").append(bendPoint.x()).append(",\"y\":").append(bendPoint.y()).append("}");
            if (i + 1 < connector.bendPoints().size()) {
                json.append(",");
            }
        }
        json.append("]").append(comma ? "," : "").append("\n");
    }

    private void writeStyles(DiagramProject project, StringBuilder json, int level) {
        indent(json, level).append("\"styles\": {\n");
        writeElementStyle("default", project.styleSheet().defaultStyle(), json, level + 1, true);
        writeAppearance(project.styleSheet().appearance(), json, level + 1, true);
        indent(json, level + 1).append("\"elements\": {\n");
        int index = 0;
        for (Map.Entry<?, ElementStyle> entry : project.styleSheet().stylesByElementId().entrySet()) {
            indent(json, level + 2).append(quote(entry.getKey().toString())).append(": ");
            writeElementStyleInline(entry.getValue(), json);
            json.append(++index < project.styleSheet().stylesByElementId().size() ? ",\n" : "\n");
        }
        indent(json, level + 1).append("}\n");
        indent(json, level).append("}");
    }

    private void writeAppearance(DiagramAppearance appearance, StringBuilder json, int level, boolean comma) {
        indent(json, level).append(quote("appearance")).append(": {");
        json.append(quote("workspaceBackground")).append(":")
                .append(quote(appearance.workspaceBackground().toHex())).append(",");
        json.append(quote("diagramBackground")).append(":")
                .append(quote(appearance.diagramBackground().toHex()));
        json.append("}").append(comma ? ",\n" : "\n");
    }

    private void writeElementStyle(String name, ElementStyle style, StringBuilder json, int level, boolean comma) {
        indent(json, level).append(quote(name)).append(": ");
        writeElementStyleInline(style, json);
        json.append(comma ? ",\n" : "\n");
    }

    private void writeElementStyleInline(ElementStyle style, StringBuilder json) {
        json.append("{")
                .append("\"fill\":").append(quote(style.fill().color().toHex())).append(",")
                .append("\"stroke\":").append(quote(style.stroke().color().toHex())).append(",")
                .append("\"strokeWidth\":").append(style.stroke().width()).append(",")
                .append("\"strokePattern\":").append(quote(writeStrokePattern(style.stroke().pattern()))).append(",")
                .append("\"fontFamily\":").append(quote(style.text().fontFamily())).append(",")
                .append("\"fontSize\":").append(style.text().fontSize()).append(",")
                .append("\"textColor\":").append(quote(style.text().color().toHex()))
                .append("}");
    }

    private String writeStrokePattern(StrokePattern pattern) {
        return switch (pattern == null ? StrokePattern.SOLID : pattern) {
            case SOLID -> "solid";
            case DASHED -> "dashed";
            case DOTTED -> "dotted";
        };
    }


    private void writeView(DiagramViewState viewState, StringBuilder json, int level) {
        DiagramViewState state = viewState == null ? DiagramViewState.defaults() : viewState;
        indent(json, level).append("\"view\": {\n");
        numberField(json, level + 1, "zoomFactor", state.zoomFactor(), true);
        numberField(json, level + 1, "viewportX", state.viewportX(), true);
        numberField(json, level + 1, "viewportY", state.viewportY(), true);
        field(json, level + 1, "viewMode", state.viewMode(), true);
        indent(json, level + 1).append("\"collapsedGroups\": [");
        int groupIndex = 0;
        for (String groupId : state.collapsedGroups()) {
            if (groupIndex++ > 0) json.append(", ");
            json.append(quote(groupId));
        }
        json.append("],\n");
        indent(json, level + 1).append("\"filters\": {");
        int filterIndex = 0;
        for (Map.Entry<String, Boolean> entry : state.filters().entrySet()) {
            if (filterIndex++ > 0) json.append(",");
            json.append(quote(entry.getKey())).append(":").append(Boolean.TRUE.equals(entry.getValue()));
        }
        json.append("}\n");
        indent(json, level).append("}");
    }

    private void field(StringBuilder json, int level, String name, String value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(quote(value)).append(comma ? "," : "").append("\n");
    }

    private void numberField(StringBuilder json, int level, String name, double value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(formatNumber(value)).append(comma ? "," : "").append("\n");
    }

    private String formatNumber(double value) {
        if (Double.isFinite(value) && Math.rint(value) == value) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    private void booleanField(StringBuilder json, int level, String name, boolean value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(value).append(comma ? "," : "").append("\n");
    }

    private StringBuilder indent(StringBuilder json, int level) {
        return json.append("  ".repeat(Math.max(0, level)));
    }

    private String quote(String value) {
        return JsonStringEscaper.quote(value);
    }
}
