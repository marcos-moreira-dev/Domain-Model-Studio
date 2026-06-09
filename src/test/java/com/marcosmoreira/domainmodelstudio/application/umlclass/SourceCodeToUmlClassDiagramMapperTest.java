package com.marcosmoreira.domainmodelstudio.application.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMember;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMemberKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMetadataKeys;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeModule;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProject;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelation;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelationKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeSourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeRole;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeVisibility;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguageVersion;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SourceCodeToUmlClassDiagramMapperTest {

    @Test
    void shouldMapNormalizedSourceCodeProjectToEditableUmlClassDiagram() {
        ParsedCodeProject project = cedroDamascoParsedProject();

        UmlClassDiagramDocument document = new SourceCodeToUmlClassDiagramMapper().map(project);

        assertEquals("Cedro Damasco — UML desde código", document.projectName());
        assertEquals(2, document.moduleCount());
        assertEquals(4, document.classCount());
        assertEquals(3, document.relationCount());
        assertTrue(document.notes().contains("Markdown"));
        assertTrue(document.classes().stream().anyMatch(node -> node.displayName().equals("PedidoController")
                && node.kind() == UmlClassKind.CONTROLLER));
        assertTrue(document.classes().stream().anyMatch(node -> node.displayName().equals("PedidoService")
                && node.kind() == UmlClassKind.SERVICE));
        assertTrue(document.classes().stream().anyMatch(node -> node.displayName().equals("ProductoListComponent")
                && node.kind() == UmlClassKind.COMPONENT));
        assertTrue(document.relations().stream().anyMatch(relation -> relation.kind() == UmlRelationKind.INHERITANCE
                && relation.label().equals("extends")));
        assertTrue(document.relations().stream().anyMatch(relation -> relation.kind() == UmlRelationKind.IMPLEMENTATION
                && relation.label().equals("implements")));
        assertTrue(document.relations().stream().anyMatch(relation -> relation.kind() == UmlRelationKind.ASSOCIATION
                && relation.label().equals("usa")));
    }

    @Test
    void shouldMapMembersVisibilityAndSourceMetadata() {
        UmlClassDiagramDocument document = new SourceCodeToUmlClassDiagramMapper().map(cedroDamascoParsedProject());

        var service = document.classes().stream()
                .filter(node -> node.displayName().equals("PedidoService"))
                .findFirst()
                .orElseThrow();

        assertEquals("com.acme.pedidos", service.packageName());
        assertTrue(service.description().contains("PedidoService.java"));
        assertTrue(service.notes().contains("SpringService"));
        assertTrue(service.members().stream().anyMatch(member -> member.kind() == UmlMemberKind.ATTRIBUTE
                && member.name().equals("repository")
                && member.visibility() == UmlVisibility.PRIVATE));
        assertTrue(service.members().stream().anyMatch(member -> member.kind() == UmlMemberKind.METHOD
                && member.signature().contains("buscarTodos")));
    }


    @Test
    void shouldPreserveCompleteMemberModelForLargeImportedClass() {
        ArrayList<ParsedCodeMember> members = new ArrayList<>();
        for (int index = 0; index < 40; index++) {
            members.add(field("campo" + index, "String"));
            members.add(method("operacion" + index, "void", "operacion" + index + "(): void"));
        }
        ParsedCodeType type = type("backend", "mod_backend_pedidos", "PedidoService", ParsedCodeTypeKind.CLASS,
                ParsedCodeTypeRole.BACKEND_SERVICE, members, "com.acme.pedidos", "PedidoService.java");
        ParsedCodeProject project = new ParsedCodeProject("Demo", List.of(root("backend", SourceRootKind.BACKEND)),
                List.of(module("mod_backend_pedidos", "backend", "com.acme.pedidos")),
                List.of(type), List.of(), List.of());

        UmlClassDiagramDocument document = new SourceCodeToUmlClassDiagramMapper().map(project);

        var service = document.classes().stream()
                .filter(node -> node.displayName().equals("PedidoService"))
                .findFirst()
                .orElseThrow();
        assertEquals(80, service.members().size(),
                "El mapper debe conservar todo el modelo; solo el render puede limitar lo visible.");
    }

    @Test
    void shouldSkipUnresolvedRelationsAndDeduplicateSemanticConnectors() {
        ParsedCodeType source = type("backend", "mod_backend_pedidos", "PedidoService", ParsedCodeTypeKind.CLASS,
                ParsedCodeTypeRole.BACKEND_SERVICE, List.of(), "com.acme.pedidos", "PedidoService.java");
        ParsedCodeRelation unresolved = new ParsedCodeRelation("r1", source.id(), "NoExiste",
                ParsedCodeRelationKind.ASSOCIATION, "No resuelta", Map.of());
        ParsedCodeRelation circular = new ParsedCodeRelation("r2", source.id(), source.qualifiedName(),
                ParsedCodeRelationKind.ASSOCIATION, "Circular", Map.of(ParsedCodeMetadataKeys.TARGET_TYPE_ID, source.id()));
        ParsedCodeProject project = new ParsedCodeProject("Demo", List.of(root("backend", SourceRootKind.BACKEND)),
                List.of(module("mod_backend_pedidos", "backend", "com.acme.pedidos")),
                List.of(source), List.of(unresolved, circular), List.of());

        UmlClassDiagramDocument document = new SourceCodeToUmlClassDiagramMapper().map(project);

        assertEquals(1, document.classCount());
        assertTrue(document.relations().isEmpty(), "No debe crear conectores inválidos ni circulares.");
    }

    @Test
    void shouldMapOwnershipRelationsToUmlAggregationAndComposition() {
        ParsedCodeType invoice = type("backend", "mod_backend_facturas", "Factura", ParsedCodeTypeKind.CLASS,
                ParsedCodeTypeRole.DOMAIN_TYPE, List.of(), "com.acme.facturas", "Factura.java");
        ParsedCodeType detail = type("backend", "mod_backend_facturas", "FacturaDetalle", ParsedCodeTypeKind.CLASS,
                ParsedCodeTypeRole.DOMAIN_TYPE, List.of(), "com.acme.facturas", "FacturaDetalle.java");
        ParsedCodeType payment = type("backend", "mod_backend_facturas", "Pago", ParsedCodeTypeKind.CLASS,
                ParsedCodeTypeRole.DOMAIN_TYPE, List.of(), "com.acme.facturas", "Pago.java");
        ParsedCodeProject project = new ParsedCodeProject("Facturas", List.of(root("backend", SourceRootKind.BACKEND)),
                List.of(module("mod_backend_facturas", "backend", "com.acme.facturas")),
                List.of(invoice, detail, payment), List.of(
                relation("r1", invoice, detail, ParsedCodeRelationKind.COMPOSITION),
                relation("r2", invoice, payment, ParsedCodeRelationKind.AGGREGATION)), List.of());

        UmlClassDiagramDocument document = new SourceCodeToUmlClassDiagramMapper().map(project);

        assertTrue(document.relations().stream().anyMatch(relation -> relation.kind() == UmlRelationKind.COMPOSITION
                && relation.label().equals("compone")));
        assertTrue(document.relations().stream().anyMatch(relation -> relation.kind() == UmlRelationKind.AGGREGATION
                && relation.label().equals("agrega")));
    }

    private ParsedCodeProject cedroDamascoParsedProject() {
        ParsedCodeType base = type("backend", "mod_backend_pedidos", "BaseController", ParsedCodeTypeKind.CLASS,
                ParsedCodeTypeRole.DOMAIN_TYPE, List.of(), "com.acme.pedidos", "BaseController.java");
        ParsedCodeType controller = type("backend", "mod_backend_pedidos", "PedidoController", ParsedCodeTypeKind.CLASS,
                ParsedCodeTypeRole.BACKEND_CONTROLLER, List.of(method("listar", "List<PedidoDto>", "listar(): List<PedidoDto>")),
                "com.acme.pedidos", "PedidoController.java");
        ParsedCodeType service = type("backend", "mod_backend_pedidos", "PedidoService", ParsedCodeTypeKind.CLASS,
                ParsedCodeTypeRole.BACKEND_SERVICE,
                List.of(field("repository", "PedidoRepository"), method("buscarTodos", "List<PedidoDto>", "buscarTodos(): List<PedidoDto>")),
                "com.acme.pedidos", "PedidoService.java");
        ParsedCodeType component = type("frontend", "mod_frontend_productos", "ProductoListComponent", ParsedCodeTypeKind.CLASS,
                ParsedCodeTypeRole.FRONTEND_COMPONENT, List.of(), "src.app.features.productos", "producto-list.component.ts");
        return new ParsedCodeProject("Cedro Damasco", List.of(root("backend", SourceRootKind.BACKEND), root("frontend", SourceRootKind.FRONTEND)),
                List.of(module("mod_backend_pedidos", "backend", "com.acme.pedidos"),
                        module("mod_frontend_productos", "frontend", "src.app.features.productos")),
                List.of(base, controller, service, component), List.of(
                        relation("r1", controller, base, ParsedCodeRelationKind.EXTENDS),
                        relation("r2", controller, service, ParsedCodeRelationKind.IMPLEMENTS),
                        relation("r3", service, controller, ParsedCodeRelationKind.ASSOCIATION)),
                List.of("Relación externa omitida"));
    }

    private ParsedCodeRelation relation(String id, ParsedCodeType source, ParsedCodeType target, ParsedCodeRelationKind kind) {
        return new ParsedCodeRelation(id, source.id(), target.simpleName(), kind, "Relación inferida.", Map.of(
                ParsedCodeMetadataKeys.TARGET_TYPE_ID, target.id(),
                ParsedCodeMetadataKeys.TARGET_QUALIFIED_NAME, target.qualifiedName()));
    }

    private ParsedCodeMember field(String name, String type) {
        return new ParsedCodeMember("field_" + name, ParsedCodeMemberKind.FIELD, name, type,
                name + ": " + type, ParsedCodeVisibility.PRIVATE, List.of(), Map.of());
    }

    private ParsedCodeMember method(String name, String type, String signature) {
        return new ParsedCodeMember("method_" + name, ParsedCodeMemberKind.METHOD, name, type,
                signature, ParsedCodeVisibility.PUBLIC, List.of(), Map.of());
    }

    private ParsedCodeType type(String rootId, String moduleId, String simpleName, ParsedCodeTypeKind kind,
                                ParsedCodeTypeRole role, List<ParsedCodeMember> members,
                                String packageName, String sourcePath) {
        Map<String, String> metadata = Map.of(
                ParsedCodeMetadataKeys.LANGUAGE, rootId.equals("backend") ? "java" : "typescript",
                ParsedCodeMetadataKeys.ROLE, role.id(),
                ParsedCodeMetadataKeys.FRAMEWORK_HINT, rootId.equals("backend") ? "SpringService" : "AngularComponent");
        return new ParsedCodeType(rootId + ":" + packageName + "." + simpleName, rootId, moduleId,
                packageName + "." + simpleName, simpleName, kind, Path.of(sourcePath), packageName,
                members, List.of("@Generated"), metadata);
    }

    private ParsedCodeModule module(String id, String rootId, String qualifiedName) {
        return new ParsedCodeModule(id, rootId, qualifiedName, qualifiedName, Path.of(qualifiedName.replace('.', '/')));
    }

    private ParsedCodeSourceRoot root(String id, SourceRootKind kind) {
        SourceLanguage language = id.equals("backend") ? SourceLanguage.JAVA : SourceLanguage.TYPESCRIPT;
        return new ParsedCodeSourceRoot(id, id, Path.of(id), kind, List.of(SourceLanguageVersion.flexible(language)));
    }
}
