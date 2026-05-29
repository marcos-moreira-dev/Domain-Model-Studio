package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetCatalog;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldVisibility;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramViewState;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectMetadata;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectType;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.Cardinality;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.ParticipationType;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorMarker;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorPathKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramSize;
import com.marcosmoreira.domainmodelstudio.domain.layout.MarkerOrientation;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleStatus;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleSheet;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramAppearance;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.FillStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.RgbaColor;
import com.marcosmoreira.domainmodelstudio.domain.style.StrokePattern;
import com.marcosmoreira.domainmodelstudio.domain.style.StrokeStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.TextStyle;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.*;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.*;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.*;
import com.marcosmoreira.domainmodelstudio.domain.behavior.*;
import com.marcosmoreira.domainmodelstudio.domain.architecture.*;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.*;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Lector JSON manual para proyectos {@code .dms} generados por Domain Model Studio.
 *
 * <p>Reconstruye metadatos, modelo conceptual, documentos especializados, layout,
 * estilos y assets. Si la versión del archivo es futura o el payload es inconsistente,
 * debe fallar de forma explícita para no abrir un proyecto degradado.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> al abrir un {@code .dms} del Grafo lógico,
 * este lector reconstruye el documento especializado y el layout. Si el documento no
 * puede reconstruirse, es preferible fallar antes de mostrar un canvas inconsistente.</p>
 */
public final class DmsProjectJsonReader {

    public DiagramProject read(String jsonText) {
        Object rootValue = SimpleJsonParser.parse(jsonText);
        Map<String, Object> root = asObject(rootValue, "root");
        int formatVersion = readFormatVersion(root);
        if (formatVersion > DmsProjectFormat.CURRENT_FORMAT_VERSION) {
            throw new IllegalArgumentException("Versión .dms no soportada: " + formatVersion);
        }
        ProjectMetadata metadata = readMetadata(root, object(root, "project"));
        Map<String, Object> modelObject = object(root, "model");
        DiagramModel model = new DmsProjectConceptualModelJsonReader().readModel(modelObject);
        DmsProjectSpecializedPayload specializedPayload = new DmsProjectSpecializedPayloadReader().read(modelObject);
        DmsProjectVisualStateJsonReader visualReader = new DmsProjectVisualStateJsonReader();
        DiagramLayouts layouts = visualReader.readLayouts(object(root, "layouts"), metadata.activeNotation());
        DiagramStyleSheet styleSheet = root.containsKey("styles")
                ? visualReader.readStyleSheet(object(root, "styles"))
                : DiagramStyleSheet.defaults();
        DiagramViewState viewState = root.containsKey("view")
                ? visualReader.readViewState(object(root, "view"))
                : DiagramViewState.defaults();
        ProjectAssetCatalog assetCatalog = root.containsKey("assets")
                ? new DmsProjectAssetsJsonReader().read(root.get("assets"))
                : ProjectAssetCatalog.empty();
        DiagramProject project = new DiagramProject(metadata, model, layouts, styleSheet, viewState,
                specializedPayload.dataDictionary(), specializedPayload.moduleMap(), specializedPayload.umlClassDiagram(),
                specializedPayload.rolesPermissions(), specializedPayload.screenFlow(), specializedPayload.wireframe(),
                specializedPayload.behaviorDiagram(), specializedPayload.architectureDiagram(), specializedPayload.freeGraph(),
                specializedPayload.logicalBusinessDocument(), specializedPayload.logicalBusinessGraphDocument(), assetCatalog);
        new DmsProjectPayloadConsistencyValidator().validate(project);
        return project;
    }

    private int readFormatVersion(Map<String, Object> root) {
        Object value = root.get("formatVersion");
        if (value == null) {
            return DmsProjectFormat.LEGACY_FORMAT_VERSION;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("formatVersion inválido: " + value, exception);
        }
    }

    private ProjectMetadata readMetadata(Map<String, Object> root, Map<String, Object> object) {
        ProjectType projectType = ProjectType.fromStoredValue(stringOrDefault(object, "projectType", "CONCEPTUAL_MODEL"));
        DiagramTypeId diagramTypeId = readDiagramTypeId(object, projectType);
        return new ProjectMetadata(
                string(object, "id"),
                string(object, "title"),
                projectType,
                diagramTypeId,
                stringOrDefault(object, "version", "0.1.0"),
                stringOrDefault(object, "status", "draft"),
                readActiveNotation(root, object),
                readSourceMarkdownPath(root, object),
                stringOrDefault(object, "description", "")
        );
    }

    private NotationType readActiveNotation(Map<String, Object> root, Map<String, Object> projectObject) {
        String direct = stringOrDefault(projectObject, "activeNotation", "");
        if (!direct.isBlank()) {
            return enumValue(NotationType.class, direct, NotationType.CHEN);
        }
        if (root.containsKey("notations")) {
            Map<String, Object> notations = object(root, "notations");
            return enumValue(NotationType.class, stringOrDefault(notations, "active", "CHEN"), NotationType.CHEN);
        }
        return NotationType.CHEN;
    }

    private String readSourceMarkdownPath(Map<String, Object> root, Map<String, Object> projectObject) {
        String direct = stringOrDefault(projectObject, "sourceMarkdownPath", "");
        if (!direct.isBlank()) {
            return direct;
        }
        if (root.containsKey("source")) {
            return stringOrDefault(object(root, "source"), "path", "");
        }
        return "";
    }

    private DiagramTypeId readDiagramTypeId(Map<String, Object> object, ProjectType projectType) {
        String stored = stringOrDefault(object, "diagramTypeId", "");
        if (stored == null || stored.isBlank()) {
            return projectType.diagramTypeId();
        }
        return DiagramTypeId.of(stored);
    }

    private Map<String, Object> object(Map<String, Object> parent, String key) {
        if (!parent.containsKey(key)) {
            throw new IllegalArgumentException("Falta objeto requerido: " + key);
        }
        return asObject(parent.get(key), key);
    }


    @SuppressWarnings("unchecked")
    private Map<String, Object> asObject(Object value, String context) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException("Se esperaba objeto JSON en: " + context);
    }

    private String string(Map<String, Object> object, String key) {
        if (!object.containsKey(key)) {
            throw new IllegalArgumentException("Falta string requerido: " + key);
        }
        return String.valueOf(object.get(key));
    }

    private String stringOrDefault(Map<String, Object> object, String key, String defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }

    private <E extends Enum<E>> E enumValue(Class<E> enumType, String rawValue, E fallback) {
        if (rawValue == null || rawValue.isBlank()) {
            return fallback;
        }
        try {
            return Enum.valueOf(enumType, rawValue.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }
}
