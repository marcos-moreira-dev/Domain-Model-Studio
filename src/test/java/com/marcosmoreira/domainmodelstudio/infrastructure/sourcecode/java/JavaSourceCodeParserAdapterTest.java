package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMemberKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelationKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeVisibility;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeParseRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceFileCandidate;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguageVersion;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootKind;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JavaSourceCodeParserAdapterTest {
    private final JavaSourceCodeParserAdapter parser = new JavaSourceCodeParserAdapter();

    @TempDir
    Path tempDir;

    @Test
    void shouldParseJavaClassesInterfacesEnumsMembersAndRelationsFromAst() throws Exception {
        Path sourceFile = Files.createDirectories(tempDir.resolve("src/main/java/com/acme/pedidos"))
                .resolve("PedidoService.java");
        Files.writeString(sourceFile, """
                package com.acme.pedidos;

                import java.util.List;

                @RestController
                public class PedidoService extends BaseService implements PedidoPort {
                    private final PedidoRepository repository;
                    protected List<Pedido> pedidos;

                    public PedidoService(PedidoRepository repository) {
                        this.repository = repository;
                    }

                    public Pedido crear(Pedido pedido) {
                        return pedido;
                    }
                }

                interface PedidoPort {
                    void validar(Pedido pedido);
                }

                class BaseService {
                }

                enum EstadoPedido {
                    ABIERTO, CERRADO
                }
                """);

        var result = parser.parse(parseRequest(sourceFile));

        assertTrue(result.warnings().isEmpty(), () -> "No se esperaban warnings de parser: " + result.warnings());
        assertEquals(4, result.types().size());
        assertEquals(1, result.modules().size());
        assertTrue(result.modules().getFirst().qualifiedName().equals("com.acme.pedidos"));
        assertTrue(result.types().stream().anyMatch(type -> type.simpleName().equals("PedidoService")
                && type.kind() == ParsedCodeTypeKind.CLASS
                && type.packageName().equals("com.acme.pedidos")
                && type.annotations().contains("@RestController")));
        assertTrue(result.types().stream().anyMatch(type -> type.simpleName().equals("PedidoPort")
                && type.kind() == ParsedCodeTypeKind.INTERFACE));
        assertTrue(result.types().stream().anyMatch(type -> type.simpleName().equals("EstadoPedido")
                && type.kind() == ParsedCodeTypeKind.ENUM));

        var pedidoService = result.types().stream()
                .filter(type -> type.simpleName().equals("PedidoService"))
                .findFirst()
                .orElseThrow();
        assertTrue(pedidoService.members().stream().anyMatch(member -> member.name().equals("repository")
                && member.kind() == ParsedCodeMemberKind.FIELD
                && member.visibility() == ParsedCodeVisibility.PRIVATE
                && member.type().equals("PedidoRepository")));
        assertTrue(pedidoService.members().stream().anyMatch(member -> member.name().equals("crear")
                && member.kind() == ParsedCodeMemberKind.METHOD
                && member.visibility() == ParsedCodeVisibility.PUBLIC
                && member.signature().contains("Pedido pedido")));
        assertTrue(pedidoService.members().stream().anyMatch(member -> member.name().equals("PedidoService")
                && member.kind() == ParsedCodeMemberKind.CONSTRUCTOR));

        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.EXTENDS
                && relation.targetTypeName().equals("BaseService")));
        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.IMPLEMENTS
                && relation.targetTypeName().equals("PedidoPort")));
        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.DEPENDENCY
                && relation.targetTypeName().equals("PedidoRepository")));
        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.AGGREGATION
                && relation.targetTypeName().equals("Pedido")));
    }

    @Test
    void shouldInferCompositionFromStrongJavaOwnershipAnnotations() throws Exception {
        Path sourceFile = Files.createDirectories(tempDir.resolve("src/main/java/com/acme/facturas"))
                .resolve("Factura.java");
        Files.writeString(sourceFile, """
                package com.acme.facturas;

                import java.util.List;

                public class Factura {
                    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
                    private List<FacturaDetalle> detalles;
                }

                class FacturaDetalle {
                }
                """);

        var result = parser.parse(parseRequest(sourceFile));

        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.COMPOSITION
                && relation.targetTypeName().equals("FacturaDetalle")
                && relation.metadata().getOrDefault("relationOwnershipHint", "").equals("strong-lifecycle")));
    }

    @Test
    void shouldSupportFlexibleJavaVersionsWithoutHardCouplingToJava21() {
        assertTrue(parser.supports(SourceLanguage.JAVA, SourceLanguageVersion.hinted(SourceLanguage.JAVA, "8")));
        assertTrue(parser.supports(SourceLanguage.JAVA, SourceLanguageVersion.hinted(SourceLanguage.JAVA, "21")));
        assertFalse(parser.supports(SourceLanguage.TYPESCRIPT,
                SourceLanguageVersion.flexible(SourceLanguage.TYPESCRIPT)));
    }

    private SourceCodeParseRequest parseRequest(Path sourceFile) {
        SourceRoot root = new SourceRoot("backend-java", "Backend Java", tempDir, SourceRootKind.BACKEND,
                List.of(SourceLanguageVersion.flexible(SourceLanguage.JAVA)));
        SourceFileCandidate candidate = new SourceFileCandidate(sourceFile, tempDir.relativize(sourceFile),
                SourceLanguage.JAVA, root.id());
        return new SourceCodeParseRequest(root, SourceLanguageVersion.hinted(SourceLanguage.JAVA, "17"),
                List.of(candidate));
    }
}
