package com.marcosmoreira.domainmodelstudio.application.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMetadataKeys;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeModule;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProject;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelation;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelationKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeSourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeRole;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguageVersion;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootKind;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SourceCodeUmlSummarySelectionPolicyTest {

    @Test
    void shouldKeepSmallProjectsComplete() {
        ParsedCodeProject project = smallProject();
        Map<String, String> typeIds = typeIdMap(project);
        Map<String, String> relationIds = relationIdMap(project);

        SourceCodeUmlSummarySelectionPolicy.Selection selection =
                new SourceCodeUmlSummarySelectionPolicy().select(project, typeIds, relationIds);

        assertEquals(2, selection.classIds().size());
        assertEquals(1, selection.relationIds().size());
        assertTrue(!selection.limited());
        assertTrue(selection.notes(2, 1).contains("completa"));
    }

    @Test
    void shouldLimitLargeProjectsButPrioritizeApiAndImportantTypes() {
        ParsedCodeProject project = largeProject();
        Map<String, String> typeIds = typeIdMap(project);
        Map<String, String> relationIds = relationIdMap(project);

        SourceCodeUmlSummarySelectionPolicy.Selection selection =
                new SourceCodeUmlSummarySelectionPolicy().select(project, typeIds, relationIds);

        assertTrue(selection.limited());
        assertTrue(selection.classIds().size() <= 120);
        assertTrue(selection.classIds().contains("class_pedidocontroller"));
        assertTrue(selection.classIds().contains("class_pedidoservice"));
        assertTrue(selection.classIds().contains("class_pedidoapiservice"));
        assertTrue(selection.relationIds().contains("relation_rel_api"));
        assertTrue(selection.notes(project.types().size(), project.relations().size()).contains("Vista inicial segura"));
    }

    private ParsedCodeProject smallProject() {
        ParsedCodeType controller = type("backend", "mod_backend_pedidos", "PedidoController", "com.acme.pedidos",
                ParsedCodeTypeRole.BACKEND_CONTROLLER);
        ParsedCodeType service = type("backend", "mod_backend_pedidos", "PedidoService", "com.acme.pedidos",
                ParsedCodeTypeRole.BACKEND_SERVICE);
        ParsedCodeRelation relation = relation("rel_service", controller, service, ParsedCodeRelationKind.ASSOCIATION);
        return new ParsedCodeProject("Proyecto pequeño", List.of(root("backend", SourceRootKind.BACKEND)),
                List.of(module("mod_backend_pedidos", "backend", "com.acme.pedidos")),
                List.of(controller, service), List.of(relation), List.of());
    }

    private ParsedCodeProject largeProject() {
        ArrayList<ParsedCodeType> types = new ArrayList<>();
        ParsedCodeType controller = type("backend", "mod_backend_pedidos", "PedidoController", "com.acme.pedidos",
                ParsedCodeTypeRole.BACKEND_CONTROLLER);
        ParsedCodeType service = type("backend", "mod_backend_pedidos", "PedidoService", "com.acme.pedidos",
                ParsedCodeTypeRole.BACKEND_SERVICE);
        ParsedCodeType repository = type("backend", "mod_backend_pedidos", "PedidoRepository", "com.acme.pedidos",
                ParsedCodeTypeRole.BACKEND_REPOSITORY);
        ParsedCodeType apiService = type("frontend", "mod_frontend_pedidos", "PedidoApiService", "src.app.features.pedidos",
                ParsedCodeTypeRole.FRONTEND_SERVICE);
        types.add(controller);
        types.add(service);
        types.add(repository);
        types.add(apiService);
        for (int index = 0; index < 170; index++) {
            String root = index % 3 == 0 ? "frontend" : "backend";
            String module = root.equals("backend") ? "mod_backend_bulk" : "mod_frontend_bulk";
            String packageName = root.equals("backend") ? "com.acme.bulk" : "src.app.bulk";
            ParsedCodeTypeRole role = root.equals("backend")
                    ? ParsedCodeTypeRole.DOMAIN_TYPE
                    : ParsedCodeTypeRole.FRONTEND_MODEL;
            types.add(type(root, module, "AuxiliaryType" + index, packageName, role));
        }
        return new ParsedCodeProject("Proyecto grande",
                List.of(root("backend", SourceRootKind.BACKEND), root("frontend", SourceRootKind.FRONTEND)),
                List.of(module("mod_backend_pedidos", "backend", "com.acme.pedidos"),
                        module("mod_backend_bulk", "backend", "com.acme.bulk"),
                        module("mod_frontend_pedidos", "frontend", "src.app.features.pedidos"),
                        module("mod_frontend_bulk", "frontend", "src.app.bulk")),
                types,
                List.of(relation("rel_service", controller, service, ParsedCodeRelationKind.ASSOCIATION),
                        relation("rel_repository", service, repository, ParsedCodeRelationKind.ASSOCIATION),
                        relation("rel_api", apiService, controller, ParsedCodeRelationKind.API_CALL)),
                List.of());
    }

    private Map<String, String> typeIdMap(ParsedCodeProject project) {
        LinkedHashMap<String, String> out = new LinkedHashMap<>();
        for (ParsedCodeType type : project.types()) {
            out.put(type.id(), "class_" + UmlClassDiagramIds.slug(type.simpleName(), "tipo"));
        }
        return out;
    }

    private Map<String, String> relationIdMap(ParsedCodeProject project) {
        LinkedHashMap<String, String> out = new LinkedHashMap<>();
        for (ParsedCodeRelation relation : project.relations()) {
            out.put(relation.id(), "relation_" + relation.id());
        }
        return out;
    }

    private ParsedCodeRelation relation(String id, ParsedCodeType source, ParsedCodeType target,
                                        ParsedCodeRelationKind kind) {
        return new ParsedCodeRelation(id, source.id(), target.simpleName(), kind, "Relación inferida", Map.of(
                ParsedCodeMetadataKeys.TARGET_TYPE_ID, target.id(),
                ParsedCodeMetadataKeys.TARGET_QUALIFIED_NAME, target.qualifiedName()));
    }

    private ParsedCodeType type(String rootId, String moduleId, String simpleName, String packageName,
                                ParsedCodeTypeRole role) {
        return new ParsedCodeType(rootId + ":" + packageName + "." + simpleName, rootId, moduleId,
                packageName + "." + simpleName, simpleName, ParsedCodeTypeKind.CLASS,
                Path.of(simpleName + (rootId.equals("backend") ? ".java" : ".ts")), packageName,
                List.of(), List.of("@Generated"), Map.of(
                ParsedCodeMetadataKeys.LANGUAGE, rootId,
                ParsedCodeMetadataKeys.ROLE, role.id()));
    }

    private ParsedCodeModule module(String id, String rootId, String qualifiedName) {
        return new ParsedCodeModule(id, rootId, qualifiedName, qualifiedName, Path.of(qualifiedName.replace('.', '/')));
    }

    private ParsedCodeSourceRoot root(String id, SourceRootKind kind) {
        SourceLanguage language = id.equals("backend") ? SourceLanguage.JAVA : SourceLanguage.TYPESCRIPT;
        return new ParsedCodeSourceRoot(id, id, Path.of(id), kind, List.of(SourceLanguageVersion.flexible(language)));
    }
}
