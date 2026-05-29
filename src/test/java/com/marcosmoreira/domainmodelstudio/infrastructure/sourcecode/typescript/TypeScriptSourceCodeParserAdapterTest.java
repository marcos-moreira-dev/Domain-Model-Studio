package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript;

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

class TypeScriptSourceCodeParserAdapterTest {
    private final TypeScriptSourceCodeParserAdapter parser = new TypeScriptSourceCodeParserAdapter();

    @TempDir
    Path tempDir;

    @Test
    void shouldParseTypeScriptClassesInterfacesEnumsAliasesMembersAndRelations() throws Exception {
        Path sourceFile = Files.createDirectories(tempDir.resolve("src/app/features/productos"))
                .resolve("producto.service.ts");
        Files.writeString(sourceFile, """
                import { Injectable } from '@angular/core';

                @Injectable({ providedIn: 'root' })
                export class ProductoService extends BaseService implements CrudPort {
                    private repository: ProductoRepository;
                    productos?: Array<Producto>;

                    constructor(http: HttpClient) {
                    }

                    listar(): Observable<Producto[]> {
                        return this.http.get<Producto[]>('/api/productos');
                    }
                }

                export interface CrudPort {
                    save(producto: Producto): void;
                    name?: string;
                }

                export enum EstadoProducto {
                    ACTIVO,
                    INACTIVO
                }

                export type ProductoResponse = Producto | ErrorResponse;
                """);

        var result = parser.parse(parseRequest(sourceFile));

        assertTrue(result.warnings().isEmpty(), () -> "No se esperaban warnings de parser: " + result.warnings());
        assertEquals(4, result.types().size());
        assertEquals(1, result.modules().size());
        assertEquals("src.app.features.productos", result.modules().getFirst().qualifiedName());
        assertTrue(result.types().stream().anyMatch(type -> type.simpleName().equals("ProductoService")
                && type.kind() == ParsedCodeTypeKind.CLASS
                && type.annotations().contains("@Injectable")
                && type.metadata().get("frameworkHint").equals("angular:Injectable")));
        assertTrue(result.types().stream().anyMatch(type -> type.simpleName().equals("CrudPort")
                && type.kind() == ParsedCodeTypeKind.INTERFACE));
        assertTrue(result.types().stream().anyMatch(type -> type.simpleName().equals("EstadoProducto")
                && type.kind() == ParsedCodeTypeKind.ENUM));
        assertTrue(result.types().stream().anyMatch(type -> type.simpleName().equals("ProductoResponse")
                && type.kind() == ParsedCodeTypeKind.TYPE_ALIAS));

        var service = result.types().stream()
                .filter(type -> type.simpleName().equals("ProductoService"))
                .findFirst()
                .orElseThrow();
        assertTrue(service.members().stream().anyMatch(member -> member.name().equals("repository")
                && member.kind() == ParsedCodeMemberKind.FIELD
                && member.visibility() == ParsedCodeVisibility.PRIVATE
                && member.type().equals("ProductoRepository")));
        assertTrue(service.members().stream().anyMatch(member -> member.name().equals("listar")
                && member.kind() == ParsedCodeMemberKind.METHOD
                && member.type().contains("Observable")));
        assertTrue(service.members().stream().anyMatch(member -> member.name().equals("ProductoService")
                && member.kind() == ParsedCodeMemberKind.CONSTRUCTOR));
        assertTrue(service.metadata().getOrDefault("apiClientRoutes", "").contains("GET /api/productos"));

        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.EXTENDS
                && relation.targetTypeName().equals("BaseService")));
        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.IMPLEMENTS
                && relation.targetTypeName().equals("CrudPort")));
        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.ASSOCIATION
                && relation.targetTypeName().equals("ProductoRepository")));
        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.ASSOCIATION
                && relation.targetTypeName().equals("Producto")));
        assertTrue(result.relations().stream().anyMatch(relation -> relation.kind() == ParsedCodeRelationKind.ASSOCIATION
                && relation.targetTypeName().equals("ErrorResponse")));
    }

    @Test
    void shouldSupportFlexibleTypeScriptVersionsWithoutHardCouplingToModernTypeScript() {
        assertTrue(parser.supports(SourceLanguage.TYPESCRIPT,
                SourceLanguageVersion.hinted(SourceLanguage.TYPESCRIPT, "4.x")));
        assertTrue(parser.supports(SourceLanguage.TYPESCRIPT,
                SourceLanguageVersion.hinted(SourceLanguage.TYPESCRIPT, "5.x")));
        assertFalse(parser.supports(SourceLanguage.JAVA, SourceLanguageVersion.flexible(SourceLanguage.JAVA)));
    }

    private SourceCodeParseRequest parseRequest(Path sourceFile) {
        SourceRoot root = new SourceRoot("frontend-typescript", "Frontend TypeScript", tempDir, SourceRootKind.FRONTEND,
                List.of(SourceLanguageVersion.flexible(SourceLanguage.TYPESCRIPT)));
        SourceFileCandidate candidate = new SourceFileCandidate(sourceFile, tempDir.relativize(sourceFile),
                SourceLanguage.TYPESCRIPT, root.id());
        return new SourceCodeParseRequest(root, SourceLanguageVersion.hinted(SourceLanguage.TYPESCRIPT, "5"),
                List.of(candidate));
    }
}
