package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ParsedCodeProjectNormalizerTest {

    @Test
    void shouldCompleteModulesRolesAndResolvedRelationMetadata() {
        ParsedCodeSourceRoot backend = root("backend", SourceRootKind.BACKEND, SourceLanguage.JAVA);
        ParsedCodeType service = typeWithoutModule("backend", "com.acme.pedidos.PedidoService", "PedidoService",
                "com.acme.pedidos", Path.of("src/main/java/com/acme/pedidos/PedidoService.java"),
                List.of(new ParsedCodeMember("m1", ParsedCodeMemberKind.FIELD, "repository", "PedidoRepository",
                        "repository: PedidoRepository", ParsedCodeVisibility.PRIVATE, List.of(), Map.of())),
                Map.of(ParsedCodeMetadataKeys.LANGUAGE, "java"));
        ParsedCodeType repository = typeWithoutModule("backend", "com.acme.pedidos.PedidoRepository", "PedidoRepository",
                "com.acme.pedidos", Path.of("src/main/java/com/acme/pedidos/PedidoRepository.java"),
                List.of(), Map.of(ParsedCodeMetadataKeys.LANGUAGE, "java"));
        ParsedCodeRelation relation = new ParsedCodeRelation("r1", service.id(), "PedidoRepository",
                ParsedCodeRelationKind.ASSOCIATION, "Campo repository.", Map.of());
        ParsedCodeProject raw = new ParsedCodeProject("Cedro Damasco", List.of(backend), List.of(),
                List.of(service, repository), List.of(relation, relation), List.of());

        ParsedCodeProject normalized = new ParsedCodeProjectNormalizer().normalize(raw);

        assertEquals(1, normalized.modules().size());
        assertEquals("com.acme.pedidos", normalized.modules().getFirst().qualifiedName());
        assertEquals(2, normalized.types().size());
        assertTrue(normalized.types().stream().allMatch(type -> !type.moduleId().isBlank()));
        assertEquals(ParsedCodeTypeRole.BACKEND_SERVICE.id(), metadataFor(normalized, service.id(), ParsedCodeMetadataKeys.ROLE));
        assertEquals(ParsedCodeTypeRole.BACKEND_REPOSITORY.id(), metadataFor(normalized, repository.id(), ParsedCodeMetadataKeys.ROLE));
        assertEquals(1, normalized.relations().size(), "Las relaciones duplicadas deben consolidarse.");
        assertEquals("true", normalized.relations().getFirst().metadata().get(ParsedCodeMetadataKeys.RESOLVED));
        assertEquals(repository.id(), normalized.relations().getFirst().metadata().get(ParsedCodeMetadataKeys.TARGET_TYPE_ID));
    }

    @Test
    void shouldPreserveFrontendRootAndClassifyAngularTypes() {
        ParsedCodeSourceRoot frontend = root("frontend", SourceRootKind.FRONTEND, SourceLanguage.TYPESCRIPT);
        ParsedCodeType component = typeWithoutModule("frontend", "ProductoListComponent", "ProductoListComponent", "",
                Path.of("src/app/features/productos/producto-list.component.ts"), List.of(),
                Map.of(ParsedCodeMetadataKeys.LANGUAGE, "typescript", ParsedCodeMetadataKeys.FRAMEWORK_HINT, "angular:Component"));
        ParsedCodeType model = typeWithoutModule("frontend", "ProductoModel", "ProductoModel", "",
                Path.of("src/app/features/productos/models/producto.model.ts"), List.of(),
                Map.of(ParsedCodeMetadataKeys.LANGUAGE, "typescript"));
        ParsedCodeProject raw = new ParsedCodeProject("Cedro Damasco", List.of(frontend), List.of(),
                List.of(component, model), List.of(), List.of());

        ParsedCodeProject normalized = new ParsedCodeProjectNormalizer().normalize(raw);

        assertFalse(normalized.modules().isEmpty());
        assertEquals(ParsedCodeTypeRole.FRONTEND_COMPONENT.id(), metadataFor(normalized, component.id(), ParsedCodeMetadataKeys.ROLE));
        assertEquals(ParsedCodeTypeRole.FRONTEND_MODEL.id(), metadataFor(normalized, model.id(), ParsedCodeMetadataKeys.ROLE));
        assertTrue(normalized.types().stream().allMatch(type -> type.metadata().containsKey(ParsedCodeMetadataKeys.SOURCE_ROOT_KIND)));
    }

    @Test
    void shouldAddPrudentInferredInternalRelationsAfterNormalization() {
        ParsedCodeSourceRoot backend = root("backend", SourceRootKind.BACKEND, SourceLanguage.JAVA);
        ParsedCodeType controller = typeWithoutModule("backend", "com.acme.pedidos.PedidoController", "PedidoController",
                "com.acme.pedidos", Path.of("src/main/java/com/acme/pedidos/PedidoController.java"),
                List.of(), Map.of(ParsedCodeMetadataKeys.LANGUAGE, "java"));
        ParsedCodeType service = typeWithoutModule("backend", "com.acme.pedidos.PedidoService", "PedidoService",
                "com.acme.pedidos", Path.of("src/main/java/com/acme/pedidos/PedidoService.java"),
                List.of(), Map.of(ParsedCodeMetadataKeys.LANGUAGE, "java"));
        ParsedCodeProject raw = new ParsedCodeProject("Cedro Damasco", List.of(backend), List.of(),
                List.of(controller, service), List.of(), List.of());

        ParsedCodeProject normalized = new ParsedCodeProjectNormalizer().normalize(raw);

        assertEquals(1, normalized.relations().size());
        assertEquals("true", normalized.relations().getFirst().metadata().get(ParsedCodeMetadataKeys.INFERRED));
        assertEquals(service.id(), normalized.relations().getFirst().metadata().get(ParsedCodeMetadataKeys.TARGET_TYPE_ID));
    }

    private String metadataFor(ParsedCodeProject project, String typeId, String key) {
        return project.typeById(typeId).orElseThrow().metadata().get(key);
    }

    private static ParsedCodeSourceRoot root(String id, SourceRootKind kind, SourceLanguage language) {
        return new ParsedCodeSourceRoot(id, id, Path.of(id), kind, List.of(SourceLanguageVersion.flexible(language)));
    }

    private static ParsedCodeType typeWithoutModule(String rootId, String qualifiedName, String simpleName,
                                                    String packageName, Path path,
                                                    List<ParsedCodeMember> members, Map<String, String> metadata) {
        return new ParsedCodeType(rootId + ":" + qualifiedName, rootId, "", qualifiedName, simpleName,
                ParsedCodeTypeKind.CLASS, path, packageName, members, List.of(), metadata);
    }
}
