package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguageVersion;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootKind;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSystemSourceRootDetectorTest {
    private final FileSystemSourceRootDetector detector = new FileSystemSourceRootDetector();

    @TempDir
    Path tempDir;

    @Test
    void shouldDetectBackendJavaAndFrontendTypescriptRootsInFullStackProject() throws Exception {
        Path backend = Files.createDirectories(tempDir.resolve("backend/src/main/java/com/acme"));
        Path frontend = Files.createDirectories(tempDir.resolve("frontend/src/app"));
        Files.writeString(backend.getParent().getParent().getParent().resolve("pom.xml"), "<project />");
        Files.writeString(frontend.getParent().getParent().resolve("angular.json"), "{}");

        var result = detector.detect(SourceCodeImportRequest.flexible(tempDir));

        assertEquals(2, result.sourceRoots().size());
        assertEquals(1, result.rootsFor(SourceLanguage.JAVA).size());
        assertEquals(1, result.rootsFor(SourceLanguage.TYPESCRIPT).size());
        assertEquals(SourceRootKind.BACKEND, result.rootsFor(SourceLanguage.JAVA).getFirst().kind());
        assertEquals(SourceRootKind.FRONTEND, result.rootsFor(SourceLanguage.TYPESCRIPT).getFirst().kind());
        assertTrue(result.hasMultipleSourceRoots());
    }

    @Test
    void shouldDetectTwoLanguageRootsInSingleDirectoryWithoutMixingVersions() throws Exception {
        Files.createDirectories(tempDir.resolve("src/main/java"));
        Files.createDirectories(tempDir.resolve("src/app"));
        Files.writeString(tempDir.resolve("pom.xml"), "<project />");
        Files.writeString(tempDir.resolve("package.json"), "{}");

        SourceCodeImportRequest request = new SourceCodeImportRequest(tempDir,
                List.of(SourceLanguageVersion.hinted(SourceLanguage.JAVA, "17")), false);

        var result = detector.detect(request);

        assertEquals(2, result.sourceRoots().size());
        assertTrue(result.rootsFor(SourceLanguage.JAVA).getFirst().id().startsWith("root-java"));
        assertTrue(result.rootsFor(SourceLanguage.TYPESCRIPT).getFirst().id().startsWith("root-typescript"));
        assertEquals("17", result.rootsFor(SourceLanguage.JAVA).getFirst().languageVersions().getFirst().versionHint());
        assertEquals("", result.rootsFor(SourceLanguage.TYPESCRIPT).getFirst().languageVersions().getFirst().versionHint());
    }

    @Test
    void shouldIgnoreHeavyFoldersAsRootCandidates() throws Exception {
        Files.createDirectories(tempDir.resolve("node_modules/src/app"));
        Files.writeString(tempDir.resolve("node_modules/package.json"), "{}");
        Files.createDirectories(tempDir.resolve("target/src/main/java"));
        Files.writeString(tempDir.resolve("target/pom.xml"), "<project />");

        var result = detector.detect(SourceCodeImportRequest.flexible(tempDir));

        assertTrue(result.sourceRoots().isEmpty());
        assertFalse(result.warnings().isEmpty());
    }
    @Test
    void shouldTreatSelectedJavaPackageFolderAsImportableRootWhenNoProjectMarkersExist() throws Exception {
        Path packageFolder = Files.createDirectories(tempDir.resolve("com/acme/inventario"));
        Files.writeString(packageFolder.resolve("ProductoService.java"), "class ProductoService {}");

        var result = detector.detect(SourceCodeImportRequest.flexible(tempDir));

        assertEquals(1, result.sourceRoots().size());
        assertEquals(1, result.rootsFor(SourceLanguage.JAVA).size());
        assertEquals(SourceRootKind.BACKEND, result.rootsFor(SourceLanguage.JAVA).getFirst().kind());
        assertTrue(result.warnings().stream().anyMatch(warning -> warning.contains("se tratará como raíz importable")));
    }

}
