package com.marcosmoreira.domainmodelstudio.architecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Guardarraíles iniciales de arquitectura.
 *
 * <p>Estas pruebas inspeccionan imports y tamaños básicos de archivos fuente. No buscan reemplazar una
 * revisión humana ni una herramienta como ArchUnit; sirven como alarma temprana para evitar que el
 * proyecto pierda separación de capas mientras crece.
 */
class ArchitectureBoundaryTest {

    private static final Path MAIN_SOURCES = Path.of("src", "main", "java");
    private static final String BASE_PACKAGE = "com.marcosmoreira.domainmodelstudio";
    private static final int MAX_JAVA_FILE_LINES = 450;
    private static final Set<String> KNOWN_REFACTOR_DEBT = Set.of(
            "main/java/com/marcosmoreira/domainmodelstudio/application/ApplicationServices.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasViewModel.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEditorView.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorView.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorViewModel.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapViewModel.java",
            "main/java/com/marcosmoreira/domainmodelstudio/application/visual/DefaultVisualLayoutGenerator.java",
            "main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedVisualSvgWriter.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphCanvasAdapter.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphViewModel.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphViewModel.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java",
            "main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java"
    );

    @Test
    void domainLayerMustNotDependOnJavaFxOrOtherApplicationLayers() throws IOException {
        List<String> violations = new ArrayList<>();

        for (Path file : javaFilesUnder("domain")) {
            String source = Files.readString(file, StandardCharsets.UTF_8);
            assertDoesNotImport(file, source, "javafx.", violations);
            assertDoesNotImport(file, source, BASE_PACKAGE + ".application.", violations);
            assertDoesNotImport(file, source, BASE_PACKAGE + ".infrastructure.", violations);
            assertDoesNotImport(file, source, BASE_PACKAGE + ".presentation.", violations);
        }

        assertNoViolations(violations);
    }

    @Test
    void applicationLayerMustNotDependOnJavaFxInfrastructureOrPresentation() throws IOException {
        List<String> violations = new ArrayList<>();

        for (Path file : javaFilesUnder("application")) {
            String source = Files.readString(file, StandardCharsets.UTF_8);
            assertDoesNotImport(file, source, "javafx.", violations);
            assertDoesNotImport(file, source, BASE_PACKAGE + ".infrastructure.", violations);
            assertDoesNotImport(file, source, BASE_PACKAGE + ".presentation.", violations);
        }

        assertNoViolations(violations);
    }

    @Test
    void infrastructureLayerMustNotDependOnPresentation() throws IOException {
        List<String> violations = new ArrayList<>();

        for (Path file : javaFilesUnder("infrastructure")) {
            String source = Files.readString(file, StandardCharsets.UTF_8);
            assertDoesNotImport(file, source, BASE_PACKAGE + ".presentation.", violations);
        }

        assertNoViolations(violations);
    }

    @Test
    void presentationLayerMustNotImportInfrastructureDirectly() throws IOException {
        List<String> violations = new ArrayList<>();

        for (Path file : javaFilesUnder("presentation")) {
            String source = Files.readString(file, StandardCharsets.UTF_8);
            assertDoesNotImport(file, source, BASE_PACKAGE + ".infrastructure.", violations);
        }

        assertNoViolations(violations);
    }

    @Test
    void sourceFilesShouldStaySmallEnoughForHumanReview() throws IOException {
        List<String> violations = new ArrayList<>();

        for (Path file : allMainJavaFiles()) {
            long lineCount = Files.readAllLines(file, StandardCharsets.UTF_8).size();
            String relativePath = relative(file);
            if (lineCount > MAX_JAVA_FILE_LINES && !KNOWN_REFACTOR_DEBT.contains(relativePath)) {
                violations.add(relativePath + " tiene " + lineCount + " líneas; límite inicial: " + MAX_JAVA_FILE_LINES);
            }
        }

        assertNoViolations(violations);
    }

    @Test
    void expectedMainLayerPackagesMustExist() {
        assertTrue(Files.isDirectory(packagePath("domain")), "Falta paquete domain");
        assertTrue(Files.isDirectory(packagePath("application")), "Falta paquete application");
        assertTrue(Files.isDirectory(packagePath("infrastructure")), "Falta paquete infrastructure");
        assertTrue(Files.isDirectory(packagePath("presentation")), "Falta paquete presentation");
    }

    private static List<Path> javaFilesUnder(String layer) throws IOException {
        Path layerPath = packagePath(layer);
        if (!Files.isDirectory(layerPath)) {
            return List.of();
        }
        try (Stream<Path> stream = Files.walk(layerPath)) {
            return stream
                .filter(path -> path.toString().endsWith(".java"))
                .toList();
        }
    }

    private static List<Path> allMainJavaFiles() throws IOException {
        try (Stream<Path> stream = Files.walk(MAIN_SOURCES)) {
            return stream
                .filter(path -> path.toString().endsWith(".java"))
                .toList();
        }
    }

    private static Path packagePath(String layer) {
        return MAIN_SOURCES
            .resolve("com")
            .resolve("marcosmoreira")
            .resolve("domainmodelstudio")
            .resolve(layer);
    }

    private static void assertDoesNotImport(Path file, String source, String forbiddenImportPrefix, List<String> violations) {
        String importPrefix = "import " + forbiddenImportPrefix;
        if (source.contains(importPrefix)) {
            violations.add(relative(file) + " importa " + forbiddenImportPrefix);
        }
    }

    private static void assertNoViolations(List<String> violations) {
        assertTrue(violations.isEmpty(), "Violaciones de arquitectura:\n" + String.join("\n", violations));
    }

    private static String relative(Path file) {
        return MAIN_SOURCES.getParent().getParent().relativize(file).toString().replace('\\', '/');
    }
}
