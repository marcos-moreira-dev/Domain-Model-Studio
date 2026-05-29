package com.marcosmoreira.domainmodelstudio.application.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMember;
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
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SourceCodeUmlClassViewBuilderTest {

    @Test
    void shouldGenerateInternalViewsForBackendFrontendIntegrationAndFullDiagram() {
        ParsedCodeProject project = projectWithCrossRootApiRelation();

        UmlClassDiagramDocument document = new SourceCodeToUmlClassDiagramMapper().map(project);

        assertEquals(5, document.viewCount());
        assertTrue(document.views().stream().anyMatch(view -> view.kind() == UmlClassDiagramViewKind.SUMMARY));
        assertTrue(document.views().stream().anyMatch(view -> view.kind() == UmlClassDiagramViewKind.BACKEND
                && view.displayName().contains("Backend")));
        assertTrue(document.views().stream().anyMatch(view -> view.kind() == UmlClassDiagramViewKind.FRONTEND
                && view.displayName().contains("Frontend")));
        assertTrue(document.views().stream().anyMatch(view -> view.kind() == UmlClassDiagramViewKind.INTEGRATION
                && view.displayName().equals("Integración API")));
        assertTrue(document.views().stream().anyMatch(view -> view.kind() == UmlClassDiagramViewKind.FULL
                && view.displayName().equals("Mega vista")));
    }

    @Test
    void shouldFilterBackendAndFrontendClassesUsingGeneratedViews() {
        UmlClassDiagramDocument document = new SourceCodeToUmlClassDiagramMapper().map(projectWithCrossRootApiRelation());

        String backendViewId = document.views().stream()
                .filter(view -> view.kind() == UmlClassDiagramViewKind.BACKEND)
                .findFirst().orElseThrow().id();
        String frontendViewId = document.views().stream()
                .filter(view -> view.kind() == UmlClassDiagramViewKind.FRONTEND)
                .findFirst().orElseThrow().id();
        String integrationViewId = document.views().stream()
                .filter(view -> view.kind() == UmlClassDiagramViewKind.INTEGRATION)
                .findFirst().orElseThrow().id();

        assertEquals(List.of("PedidoController"), document.classesForView(backendViewId).stream()
                .map(node -> node.displayName()).toList());
        assertEquals(List.of("PedidoApiService"), document.classesForView(frontendViewId).stream()
                .map(node -> node.displayName()).toList());
        assertEquals(2, document.classesForView(integrationViewId).size());
        assertEquals(1, document.relationsForView(integrationViewId).size());
    }


    @Test
    void shouldBuildSafeSummarySmallerThanMegaViewForLargeSourceImport() {
        ParsedCodeProject project = largeProjectWithManyTypes();

        UmlClassDiagramDocument document = new SourceCodeToUmlClassDiagramMapper().map(project);

        String summaryViewId = document.views().stream()
                .filter(view -> view.kind() == UmlClassDiagramViewKind.SUMMARY)
                .findFirst().orElseThrow().id();
        String fullViewId = document.views().stream()
                .filter(view -> view.kind() == UmlClassDiagramViewKind.FULL)
                .findFirst().orElseThrow().id();

        List<String> summaryNames = document.classesForView(summaryViewId).stream()
                .map(node -> node.displayName()).toList();
        List<String> fullNames = document.classesForView(fullViewId).stream()
                .map(node -> node.displayName()).toList();

        assertEquals(project.types().size(), fullNames.size());
        assertTrue(summaryNames.size() <= 120, "Resumen debe tener un límite visual seguro.");
        assertTrue(summaryNames.size() < fullNames.size(), "Resumen no debe ser una mega vista disfrazada.");
        assertTrue(summaryNames.contains("PedidoController"));
        assertTrue(summaryNames.contains("PedidoService"));
        assertTrue(summaryNames.contains("PedidoApiService"));
    }

    private ParsedCodeProject projectWithCrossRootApiRelation() {
        ParsedCodeType controller = type("backend", "mod_backend_pedidos", "PedidoController", "com.acme.pedidos");
        ParsedCodeType apiService = type("frontend", "mod_frontend_pedidos", "PedidoApiService", "src.app.features.pedidos");
        ParsedCodeRelation apiCall = new ParsedCodeRelation("api1", apiService.id(), controller.simpleName(),
                ParsedCodeRelationKind.API_CALL, "Servicio Angular consume controlador Spring", Map.of(
                ParsedCodeMetadataKeys.TARGET_TYPE_ID, controller.id(),
                ParsedCodeMetadataKeys.TARGET_QUALIFIED_NAME, controller.qualifiedName()));
        return new ParsedCodeProject("Cedro Damasco",
                List.of(root("backend", SourceRootKind.BACKEND), root("frontend", SourceRootKind.FRONTEND)),
                List.of(module("mod_backend_pedidos", "backend", "com.acme.pedidos"),
                        module("mod_frontend_pedidos", "frontend", "src.app.features.pedidos")),
                List.of(controller, apiService), List.of(apiCall), List.of());
    }


    private ParsedCodeProject largeProjectWithManyTypes() {
        List<ParsedCodeType> types = new java.util.ArrayList<>();
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
        for (int i = 0; i < 160; i++) {
            String root = i % 3 == 0 ? "frontend" : "backend";
            String module = root.equals("backend") ? "mod_backend_bulk" : "mod_frontend_bulk";
            String packageName = root.equals("backend") ? "com.acme.bulk" : "src.app.bulk";
            ParsedCodeTypeRole role = root.equals("backend")
                    ? ParsedCodeTypeRole.DOMAIN_TYPE
                    : ParsedCodeTypeRole.FRONTEND_MODEL;
            types.add(type(root, module, "AuxiliaryType" + i, packageName, role));
        }
        ParsedCodeRelation serviceRelation = new ParsedCodeRelation("rel-service", controller.id(), service.simpleName(),
                ParsedCodeRelationKind.ASSOCIATION, "Controlador usa servicio", Map.of(
                ParsedCodeMetadataKeys.TARGET_TYPE_ID, service.id(),
                ParsedCodeMetadataKeys.TARGET_QUALIFIED_NAME, service.qualifiedName()));
        ParsedCodeRelation repositoryRelation = new ParsedCodeRelation("rel-repository", service.id(), repository.simpleName(),
                ParsedCodeRelationKind.ASSOCIATION, "Servicio usa repositorio", Map.of(
                ParsedCodeMetadataKeys.TARGET_TYPE_ID, repository.id(),
                ParsedCodeMetadataKeys.TARGET_QUALIFIED_NAME, repository.qualifiedName()));
        ParsedCodeRelation apiRelation = new ParsedCodeRelation("rel-api", apiService.id(), controller.simpleName(),
                ParsedCodeRelationKind.API_CALL, "Servicio Angular consume controlador Spring", Map.of(
                ParsedCodeMetadataKeys.TARGET_TYPE_ID, controller.id(),
                ParsedCodeMetadataKeys.TARGET_QUALIFIED_NAME, controller.qualifiedName()));
        return new ParsedCodeProject("Proyecto grande",
                List.of(root("backend", SourceRootKind.BACKEND), root("frontend", SourceRootKind.FRONTEND)),
                List.of(module("mod_backend_pedidos", "backend", "com.acme.pedidos"),
                        module("mod_backend_bulk", "backend", "com.acme.bulk"),
                        module("mod_frontend_pedidos", "frontend", "src.app.features.pedidos"),
                        module("mod_frontend_bulk", "frontend", "src.app.bulk")),
                types, List.of(serviceRelation, repositoryRelation, apiRelation), List.of());
    }

    private ParsedCodeType type(String rootId, String moduleId, String simpleName, String packageName) {
        return new ParsedCodeType(rootId + ":" + packageName + "." + simpleName, rootId, moduleId,
                packageName + "." + simpleName, simpleName, ParsedCodeTypeKind.CLASS,
                Path.of(simpleName + (rootId.equals("backend") ? ".java" : ".ts")), packageName,
                List.of(), List.of(), Map.of(ParsedCodeMetadataKeys.LANGUAGE, rootId));
    }


    private ParsedCodeType type(String rootId, String moduleId, String simpleName, String packageName,
                                ParsedCodeTypeRole role) {
        return new ParsedCodeType(rootId + ":" + packageName + "." + simpleName, rootId, moduleId,
                packageName + "." + simpleName, simpleName, ParsedCodeTypeKind.CLASS,
                Path.of(simpleName + (rootId.equals("backend") ? ".java" : ".ts")), packageName,
                List.of(), List.of(), Map.of(
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
