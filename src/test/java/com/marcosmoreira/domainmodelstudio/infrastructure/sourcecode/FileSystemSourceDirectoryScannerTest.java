package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSystemSourceDirectoryScannerTest {
    private final FileSystemSourceDirectoryScanner scanner = new FileSystemSourceDirectoryScanner();

    @TempDir
    Path tempDir;

    @Test
    void shouldScanJavaAndTypescriptFilesByDetectedSourceRoot() throws Exception {
        Path backend = Files.createDirectories(tempDir.resolve("backend/src/main/java/com/acme"));
        Path frontend = Files.createDirectories(tempDir.resolve("frontend/src/app/features/products"));
        Files.writeString(tempDir.resolve("backend/pom.xml"), "<project />");
        Files.writeString(tempDir.resolve("frontend/angular.json"), "{}");
        Files.writeString(backend.resolve("PedidoService.java"), "class PedidoService {}");
        Files.writeString(frontend.resolve("pedido.service.ts"), "export class PedidoService {}");

        var result = scanner.scan(SourceCodeImportRequest.flexible(tempDir));

        assertEquals(2, result.sourceRoots().size());
        assertEquals(1, result.filesForLanguage(SourceLanguage.JAVA).size());
        assertEquals(1, result.filesForLanguage(SourceLanguage.TYPESCRIPT).size());
        assertTrue(result.hasFiles());
        assertTrue(result.files().stream().anyMatch(file -> file.relativePath().endsWith("PedidoService.java")));
    }

    @Test
    void shouldIgnoreHeavyFoldersAndTestsByDefault() throws Exception {
        Files.createDirectories(tempDir.resolve("backend/src/main/java/com/acme"));
        Files.createDirectories(tempDir.resolve("backend/src/test/java/com/acme"));
        Files.createDirectories(tempDir.resolve("backend/target/generated-sources"));
        Files.writeString(tempDir.resolve("backend/pom.xml"), "<project />");
        Files.writeString(tempDir.resolve("backend/src/main/java/com/acme/PedidoService.java"), "class PedidoService {}");
        Files.writeString(tempDir.resolve("backend/src/test/java/com/acme/PedidoServiceTest.java"), "class PedidoServiceTest {}");
        Files.writeString(tempDir.resolve("backend/target/generated-sources/Generated.java"), "class Generated {}");

        var result = scanner.scan(SourceCodeImportRequest.flexible(tempDir));

        assertEquals(1, result.files().size());
        assertTrue(result.files().getFirst().relativePath().endsWith("PedidoService.java"));
        assertFalse(result.ignoredPaths().isEmpty());
        assertTrue(result.ignoredPaths().stream().anyMatch(path -> path.toString().contains("target")));
        assertTrue(result.ignoredPaths().stream().anyMatch(path -> path.toString().contains("PedidoServiceTest")));
    }

    @Test
    void shouldIncludeTestsWhenRequested() throws Exception {
        Files.createDirectories(tempDir.resolve("backend/src/main/java/com/acme"));
        Files.createDirectories(tempDir.resolve("backend/src/test/java/com/acme"));
        Files.writeString(tempDir.resolve("backend/pom.xml"), "<project />");
        Files.writeString(tempDir.resolve("backend/src/main/java/com/acme/PedidoService.java"), "class PedidoService {}");
        Files.writeString(tempDir.resolve("backend/src/test/java/com/acme/PedidoServiceTest.java"), "class PedidoServiceTest {}");

        var result = scanner.scan(new SourceCodeImportRequest(tempDir, java.util.List.of(), true));

        assertEquals(2, result.filesForLanguage(SourceLanguage.JAVA).size());
    }

    @Test
    void shouldKeepMostSpecificRootWhenWorkspaceAndChildOverlap() throws Exception {
        Files.createDirectories(tempDir.resolve("frontend/src/app"));
        Files.writeString(tempDir.resolve("package.json"), "{}");
        Files.writeString(tempDir.resolve("frontend/package.json"), "{}");
        Files.writeString(tempDir.resolve("frontend/src/app/producto.component.ts"), "export class ProductoComponent {}");

        var result = scanner.scan(SourceCodeImportRequest.flexible(tempDir));

        assertEquals(1, result.filesForLanguage(SourceLanguage.TYPESCRIPT).size());
        String rootId = result.filesForLanguage(SourceLanguage.TYPESCRIPT).getFirst().sourceRootId();
        assertTrue(rootId.startsWith("frontend-typescript"));
    }

    @Test
    void shouldScanSelectedJavaModuleFolderWithoutPomOrSrcMarkers() throws Exception {
        Path module = Files.createDirectories(tempDir.resolve("inventario"));
        Files.writeString(module.resolve("ProductoService.java"), "class ProductoService {}");
        Files.writeString(module.resolve("ProductoRepository.java"), "class ProductoRepository {}");

        var result = scanner.scan(SourceCodeImportRequest.flexible(module));

        assertEquals(1, result.sourceRoots().size());
        assertEquals(2, result.filesForLanguage(SourceLanguage.JAVA).size());
        assertTrue(result.files().stream().anyMatch(file -> file.relativePath().endsWith("ProductoService.java")));
    }

}
