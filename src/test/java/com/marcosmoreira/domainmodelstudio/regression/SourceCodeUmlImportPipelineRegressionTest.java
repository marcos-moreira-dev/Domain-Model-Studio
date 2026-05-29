package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProjectNormalizer;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeParserRegistry;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeProjectParserUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.GenerateUmlClassDiagramFromSourceCodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.SourceCodeToUmlClassDiagramMapper;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.FileSystemSourceDirectoryScanner;
import com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.java.JavaSourceCodeParserAdapter;
import com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript.TypeScriptSourceCodeParserAdapter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SourceCodeUmlImportPipelineRegressionTest {

    @TempDir
    Path tempDir;

    @Test
    void importsFullStackProjectWithoutBreakingAiMarkdownPath() throws Exception {
        writeCedroLikeProject(tempDir);
        GenerateUmlClassDiagramFromSourceCodeUseCase useCase = new GenerateUmlClassDiagramFromSourceCodeUseCase(
                new SourceCodeProjectParserUseCase(
                        new FileSystemSourceDirectoryScanner(),
                        new SourceCodeParserRegistry(List.of(
                                new JavaSourceCodeParserAdapter(),
                                new TypeScriptSourceCodeParserAdapter())),
                        new ParsedCodeProjectNormalizer()),
                new SourceCodeToUmlClassDiagramMapper());

        UmlClassDiagramDocument document = useCase.generate(SourceCodeImportRequest.flexible(tempDir));

        assertTrue(document.projectName().contains("UML desde código"));
        assertContainsClass(document, "ProductoController", UmlClassKind.CONTROLLER);
        assertContainsClass(document, "ProductoService", UmlClassKind.SERVICE);
        assertContainsClass(document, "ProductoRepository", UmlClassKind.REPOSITORY);
        assertContainsClass(document, "ProductoApiService", UmlClassKind.SERVICE);
        assertContainsClass(document, "ProductoListComponent", UmlClassKind.COMPONENT);
        assertContainsView(document, UmlClassDiagramViewKind.SUMMARY);
        assertContainsView(document, UmlClassDiagramViewKind.BACKEND);
        assertContainsView(document, UmlClassDiagramViewKind.FRONTEND);
        assertContainsView(document, UmlClassDiagramViewKind.INTEGRATION);
        assertContainsView(document, UmlClassDiagramViewKind.FULL);
        assertApiRelationExists(document);
        assertNoClassNamed(document, "Generated");
        assertNoClassNamed(document, "ProductoApiServiceSpec");
        assertFalse(document.notes().contains("Markdown generado por IA"),
                "La importación desde código no debe suplantar el camino Markdown de IA.");
    }

    private void assertNoClassNamed(UmlClassDiagramDocument document, String displayName) {
        assertTrue(document.classes().stream().noneMatch(current -> current.displayName().equals(displayName)),
                "No debe importarse la clase filtrada: " + displayName);
    }

    private void assertContainsClass(UmlClassDiagramDocument document, String displayName, UmlClassKind kind) {
        UmlClassNode node = document.classes().stream()
                .filter(current -> current.displayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No se generó la clase esperada: " + displayName));
        assertEquals(kind, node.kind());
        assertTrue(node.description().contains("Origen:"));
    }

    private void assertContainsView(UmlClassDiagramDocument document, UmlClassDiagramViewKind kind) {
        assertTrue(document.views().stream().anyMatch(view -> view.kind() == kind),
                "No se generó la vista interna esperada: " + kind);
    }

    private void assertApiRelationExists(UmlClassDiagramDocument document) {
        Set<String> namesById = document.classes().stream()
                .collect(Collectors.toMap(UmlClassNode::id, UmlClassNode::displayName))
                .entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.toSet());
        assertFalse(namesById.isEmpty());
        List<UmlClassRelation> apiRelations = document.relations().stream()
                .filter(relation -> relation.kind() == UmlRelationKind.DEPENDENCY)
                .filter(relation -> relation.notes().contains("api") || relation.notes().contains("API"))
                .toList();
        assertTrue(apiRelations.stream().anyMatch(relation ->
                        className(document, relation.sourceClassId()).equals("ProductoApiService")
                                && className(document, relation.targetClassId()).equals("ProductoController")),
                "Debe inferirse una relación API desde el servicio Angular hacia el controlador Spring.");
    }

    private String className(UmlClassDiagramDocument document, String classId) {
        return document.classById(classId).map(UmlClassNode::displayName).orElse("");
    }

    private void writeCedroLikeProject(Path root) throws Exception {
        Path backend = Files.createDirectories(root.resolve("backend/src/main/java/com/cedro/productos"));
        Path frontend = Files.createDirectories(root.resolve("frontend/src/app/features/productos"));
        Files.writeString(root.resolve("backend/pom.xml"), "<project />");
        Files.writeString(root.resolve("frontend/angular.json"), "{}");
        Files.createDirectories(root.resolve("backend/target/generated-sources"));
        Files.writeString(root.resolve("backend/target/generated-sources/Generated.java"), "class Generated {}");
        Files.writeString(root.resolve("frontend/src/app/features/productos/producto-api.service.spec.ts"),
                "export class ProductoApiServiceSpec {}");

        Files.writeString(backend.resolve("ProductoController.java"), """
                package com.cedro.productos;
                @RestController
                @RequestMapping("/api/productos")
                public class ProductoController {
                    private final ProductoService service;
                    public ProductoController(ProductoService service) { this.service = service; }
                    @GetMapping
                    public List<ProductoDto> listar() { return service.listar(); }
                }
                """);
        Files.writeString(backend.resolve("ProductoService.java"), """
                package com.cedro.productos;
                public class ProductoService {
                    private ProductoRepository repository;
                    public List<ProductoDto> listar() { return List.of(); }
                }
                """);
        Files.writeString(backend.resolve("ProductoRepository.java"), """
                package com.cedro.productos;
                public interface ProductoRepository { Producto findById(Long id); }
                """);
        Files.writeString(backend.resolve("Producto.java"), """
                package com.cedro.productos;
                public class Producto { private Long id; private String nombre; }
                """);
        Files.writeString(backend.resolve("ProductoDto.java"), """
                package com.cedro.productos;
                public record ProductoDto(Long id, String nombre) {}
                """);

        Files.writeString(frontend.resolve("producto-api.service.ts"), """
                @Injectable()
                export class ProductoApiService {
                    listar() { return this.http.get('/api/productos'); }
                }
                """);
        Files.writeString(frontend.resolve("producto-list.component.ts"), """
                @Component({ selector: 'app-productos', template: '' })
                export class ProductoListComponent {
                    api!: ProductoApiService;
                }
                """);
        Files.writeString(frontend.resolve("producto.model.ts"), """
                export interface ProductoDto { id: number; nombre: string; }
                """);
    }
}
