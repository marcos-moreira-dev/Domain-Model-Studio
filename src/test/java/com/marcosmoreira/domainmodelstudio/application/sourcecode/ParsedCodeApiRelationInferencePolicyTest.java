package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ParsedCodeApiRelationInferencePolicyTest {

    @Test
    void shouldInferFrontendBackendApiRelationsFromRoutes() {
        ParsedCodeType controller = type("backend", "com.acme.productos.ProductoController", "ProductoController",
                SourceRootKind.BACKEND, ParsedCodeTypeRole.BACKEND_CONTROLLER,
                List.of(member("listar", "List<ProductoDto>", List.of("@GetMapping(\"/productos\")"))),
                List.of("@RestController", "@RequestMapping(\"/api\")"), Map.of());
        ParsedCodeType apiService = type("frontend", "src.app.productos.ProductoApiService", "ProductoApiService",
                SourceRootKind.FRONTEND, ParsedCodeTypeRole.FRONTEND_SERVICE, List.of(), List.of(),
                Map.of(ParsedCodeMetadataKeys.API_CLIENT_ROUTES, "GET /api/productos"));
        ParsedCodeProject project = project(controller, apiService);

        List<ParsedCodeRelation> relations = new ParsedCodeApiRelationInferencePolicy().inferApiRelations(project);

        assertEquals(1, relations.size());
        ParsedCodeRelation relation = relations.getFirst();
        assertEquals(ParsedCodeRelationKind.API_CALL, relation.kind());
        assertEquals(apiService.id(), relation.sourceTypeId());
        assertEquals("ProductoController", relation.targetTypeName());
        assertEquals("GET", relation.metadata().get(ParsedCodeMetadataKeys.API_HTTP_METHOD));
        assertEquals("/api/productos", relation.metadata().get(ParsedCodeMetadataKeys.API_PATH));
        assertEquals("exact", relation.metadata().get(ParsedCodeMetadataKeys.API_MATCH_KIND));
    }

    @Test
    void shouldNotInferApiRelationsWhenHttpMethodDoesNotMatch() {
        ParsedCodeType controller = type("backend", "com.acme.productos.ProductoController", "ProductoController",
                SourceRootKind.BACKEND, ParsedCodeTypeRole.BACKEND_CONTROLLER,
                List.of(member("crear", "ProductoDto", List.of("@PostMapping(\"/productos\")"))),
                List.of("@RestController", "@RequestMapping(\"/api\")"), Map.of());
        ParsedCodeType apiService = type("frontend", "src.app.productos.ProductoApiService", "ProductoApiService",
                SourceRootKind.FRONTEND, ParsedCodeTypeRole.FRONTEND_SERVICE, List.of(), List.of(),
                Map.of(ParsedCodeMetadataKeys.API_CLIENT_ROUTES, "GET /api/productos"));
        ParsedCodeProject project = project(controller, apiService);

        List<ParsedCodeRelation> relations = new ParsedCodeApiRelationInferencePolicy().inferApiRelations(project);

        assertTrue(relations.isEmpty());
    }

    @Test
    void normalizerShouldIncludeResolvedApiRelations() {
        ParsedCodeType controller = type("backend", "com.acme.productos.ProductoController", "ProductoController",
                SourceRootKind.BACKEND, ParsedCodeTypeRole.BACKEND_CONTROLLER,
                List.of(member("listar", "List<ProductoDto>", List.of("@GetMapping(\"/productos\")"))),
                List.of("@RestController", "@RequestMapping(\"/api\")"), Map.of());
        ParsedCodeType apiService = type("frontend", "src.app.productos.ProductoApiService", "ProductoApiService",
                SourceRootKind.FRONTEND, ParsedCodeTypeRole.FRONTEND_SERVICE, List.of(), List.of(),
                Map.of(ParsedCodeMetadataKeys.API_CLIENT_ROUTES, "GET /api/productos"));

        ParsedCodeProject normalized = new ParsedCodeProjectNormalizer().normalize(project(controller, apiService));

        assertTrue(normalized.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.API_CALL
                && relation.metadata().get(ParsedCodeMetadataKeys.TARGET_TYPE_ID).equals(controller.id())
                && relation.metadata().get(ParsedCodeMetadataKeys.RESOLVED).equals("true")));
    }

    private ParsedCodeProject project(ParsedCodeType... types) {
        return new ParsedCodeProject("Cedro Damasco", List.of(
                new ParsedCodeSourceRoot("backend", "Backend", Path.of("backend"), SourceRootKind.BACKEND,
                        List.of(SourceLanguageVersion.flexible(SourceLanguage.JAVA))),
                new ParsedCodeSourceRoot("frontend", "Frontend", Path.of("frontend"), SourceRootKind.FRONTEND,
                        List.of(SourceLanguageVersion.flexible(SourceLanguage.TYPESCRIPT)))
        ), List.of(), List.of(types), List.of(), List.of());
    }

    private ParsedCodeType type(String rootId, String qualifiedName, String simpleName, SourceRootKind rootKind,
                                ParsedCodeTypeRole role, List<ParsedCodeMember> members,
                                List<String> annotations, Map<String, String> extraMetadata) {
        Map<String, String> metadata = new java.util.LinkedHashMap<>();
        metadata.put(ParsedCodeMetadataKeys.LANGUAGE, rootKind == SourceRootKind.BACKEND ? "java" : "typescript");
        metadata.put(ParsedCodeMetadataKeys.SOURCE_ROOT_KIND, rootKind.name().toLowerCase());
        metadata.put(ParsedCodeMetadataKeys.ROLE, role.id());
        metadata.putAll(extraMetadata);
        return new ParsedCodeType(rootId + ":" + qualifiedName, rootId, "", qualifiedName, simpleName,
                ParsedCodeTypeKind.CLASS, Path.of(simpleName + ".java"), "", members, annotations, metadata);
    }

    private ParsedCodeMember member(String name, String type, List<String> annotations) {
        return new ParsedCodeMember("method_" + name, ParsedCodeMemberKind.METHOD, name, type,
                name + "(): " + type, ParsedCodeVisibility.PUBLIC, annotations, Map.of());
    }
}
