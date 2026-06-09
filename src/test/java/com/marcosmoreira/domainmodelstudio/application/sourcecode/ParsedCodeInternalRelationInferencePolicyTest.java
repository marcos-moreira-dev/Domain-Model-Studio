package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ParsedCodeInternalRelationInferencePolicyTest {

    private final ParsedCodeInternalRelationInferencePolicy policy = new ParsedCodeInternalRelationInferencePolicy();

    @Test
    void shouldInferBackendLayerRelationsByRoleAndNameStem() {
        ParsedCodeProject project = normalizedProject(SourceRootKind.BACKEND, SourceLanguage.JAVA, List.of(
                type("backend", "com.acme.pedidos.PedidoController", "PedidoController", "pedidos",
                        ParsedCodeTypeRole.BACKEND_CONTROLLER),
                type("backend", "com.acme.pedidos.PedidoService", "PedidoService", "pedidos",
                        ParsedCodeTypeRole.BACKEND_SERVICE),
                type("backend", "com.acme.pedidos.PedidoRepository", "PedidoRepository", "pedidos",
                        ParsedCodeTypeRole.BACKEND_REPOSITORY),
                type("backend", "com.acme.pedidos.PedidoEntity", "PedidoEntity", "pedidos",
                        ParsedCodeTypeRole.BACKEND_ENTITY)));

        List<ParsedCodeRelation> relations = policy.inferInternalRelations(project);

        assertTrue(relations.stream().anyMatch(relation -> relation.sourceTypeId().endsWith("PedidoController")
                && relation.targetTypeName().endsWith("PedidoService")));
        assertTrue(relations.stream().anyMatch(relation -> relation.sourceTypeId().endsWith("PedidoService")
                && relation.targetTypeName().endsWith("PedidoRepository")));
        assertTrue(relations.stream().anyMatch(relation -> relation.sourceTypeId().endsWith("PedidoRepository")
                && relation.targetTypeName().endsWith("PedidoEntity")
                && relation.kind() == ParsedCodeRelationKind.ASSOCIATION));
        assertTrue(relations.stream().allMatch(relation -> "true".equals(relation.metadata().get(ParsedCodeMetadataKeys.INFERRED))));
    }

    @Test
    void shouldInferFrontendComponentServiceAndModelRelations() {
        ParsedCodeProject project = normalizedProject(SourceRootKind.FRONTEND, SourceLanguage.TYPESCRIPT, List.of(
                type("frontend", "src.app.productos.ProductoListComponent", "ProductoListComponent", "productos",
                        ParsedCodeTypeRole.FRONTEND_COMPONENT),
                type("frontend", "src.app.productos.ProductoService", "ProductoService", "productos",
                        ParsedCodeTypeRole.FRONTEND_SERVICE),
                type("frontend", "src.app.productos.ProductoModel", "ProductoModel", "productos",
                        ParsedCodeTypeRole.FRONTEND_MODEL)));

        List<ParsedCodeRelation> relations = policy.inferInternalRelations(project);

        assertTrue(relations.stream().anyMatch(relation -> relation.sourceTypeId().endsWith("ProductoListComponent")
                && relation.targetTypeName().endsWith("ProductoService")));
        assertTrue(relations.stream().anyMatch(relation -> relation.sourceTypeId().endsWith("ProductoService")
                && relation.targetTypeName().endsWith("ProductoModel")));
    }

    @Test
    void shouldNotCreateApiRelationsAcrossDifferentSourceRoots() {
        ParsedCodeProject project = new ParsedCodeProject("Cedro", List.of(
                root("backend", SourceRootKind.BACKEND, SourceLanguage.JAVA),
                root("frontend", SourceRootKind.FRONTEND, SourceLanguage.TYPESCRIPT)), List.of(), List.of(
                type("backend", "com.acme.ProductoController", "ProductoController", "productos",
                        ParsedCodeTypeRole.BACKEND_CONTROLLER),
                type("frontend", "src.app.ProductoService", "ProductoService", "productos",
                        ParsedCodeTypeRole.FRONTEND_SERVICE)), List.of(), List.of());

        List<ParsedCodeRelation> relations = policy.inferInternalRelations(project);

        assertEquals(0, relations.size(), "La integración frontend-backend queda reservada para la tanda API.");
    }

    @Test
    void shouldNotDuplicateExistingSourceTargetRelation() {
        ParsedCodeType service = type("backend", "com.acme.PedidoService", "PedidoService", "pedidos",
                ParsedCodeTypeRole.BACKEND_SERVICE);
        ParsedCodeType repository = type("backend", "com.acme.PedidoRepository", "PedidoRepository", "pedidos",
                ParsedCodeTypeRole.BACKEND_REPOSITORY);
        ParsedCodeRelation explicit = new ParsedCodeRelation("r1", service.id(), repository.qualifiedName(),
                ParsedCodeRelationKind.ASSOCIATION, "Campo repository.", Map.of(
                ParsedCodeMetadataKeys.TARGET_TYPE_ID, repository.id()));
        ParsedCodeProject project = normalizedProject(SourceRootKind.BACKEND, SourceLanguage.JAVA,
                List.of(service, repository), List.of(explicit));

        List<ParsedCodeRelation> relations = policy.inferInternalRelations(project);

        assertEquals(0, relations.size());
    }

    private ParsedCodeProject normalizedProject(SourceRootKind kind, SourceLanguage language, List<ParsedCodeType> types) {
        return normalizedProject(kind, language, types, List.of());
    }

    private ParsedCodeProject normalizedProject(SourceRootKind kind, SourceLanguage language, List<ParsedCodeType> types,
                                                List<ParsedCodeRelation> relations) {
        return new ParsedCodeProject("Cedro", List.of(root(kind == SourceRootKind.FRONTEND ? "frontend" : "backend", kind, language)),
                List.of(), types, relations, List.of());
    }

    private ParsedCodeSourceRoot root(String id, SourceRootKind kind, SourceLanguage language) {
        return new ParsedCodeSourceRoot(id, id, Path.of(id), kind, List.of(SourceLanguageVersion.flexible(language)));
    }

    private ParsedCodeType type(String rootId, String qualifiedName, String simpleName, String module,
                                ParsedCodeTypeRole role) {
        return new ParsedCodeType(rootId + ":" + qualifiedName, rootId, rootId + ":" + module,
                qualifiedName, simpleName, ParsedCodeTypeKind.CLASS, Path.of(module, simpleName + ".txt"), module,
                List.of(), List.of(), Map.of(
                ParsedCodeMetadataKeys.ROLE, role.id(),
                ParsedCodeMetadataKeys.LANGUAGE, rootId.equals("frontend") ? "typescript" : "java",
                ParsedCodeMetadataKeys.SOURCE_ROOT_KIND, rootId));
    }
}
