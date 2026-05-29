package com.marcosmoreira.domainmodelstudio.architecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Auditoría arquitectónica reforzada para proteger la separación de capas y la
 * trazabilidad humana antes de pasar a staging.
 */
class ArchitectureStrongAuditTest {

    private static final Path MAIN_SOURCES = Path.of("src", "main", "java");
    private static final Path RESOURCES = Path.of("src", "main", "resources");
    private static final String BASE_PACKAGE = "com.marcosmoreira.domainmodelstudio";
    private static final int MAX_CLASS_LINES_SOFT_LIMIT = 650;
    private static final int MAX_METHOD_LINES_SOFT_LIMIT = 95;
    private static final Set<String> KNOWN_REFACTOR_DEBT = Set.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/ApplicationServices.java",
            "src/main/java/com/marcosmoreira/domainmodelstudio/bootstrap/ApplicationServicesFactory.java",
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java",
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasViewModel.java",
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorViewModel.java",
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/PresentationCompositionRoot.java",
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java",
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionExecutor.java"
    );

    @Test
    void internalLayersMustRemainFreeOfJavaFx() throws IOException {
        List<String> violations = new ArrayList<>();
        for (String layer : List.of("domain", "application", "infrastructure")) {
            for (Path file : javaFilesUnder(layer)) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                if (source.contains("import javafx.")) {
                    violations.add(relative(file) + " importa javafx.*");
                }
            }
        }
        assertNoViolations(violations);
    }

    @Test
    void presentationMustNotBypassApplicationByImportingInfrastructure() throws IOException {
        List<String> violations = new ArrayList<>();
        for (Path file : javaFilesUnder("presentation")) {
            String source = Files.readString(file, StandardCharsets.UTF_8);
            if (source.contains("import " + BASE_PACKAGE + ".infrastructure.")) {
                violations.add(relative(file) + " importa infrastructure directamente");
            }
        }
        assertNoViolations(violations);
    }

    @Test
    void sourceFilesAndMethodsShouldRemainReviewableByHumans() throws IOException {
        List<String> warningsAsViolations = new ArrayList<>();
        for (Path file : allMainJavaFiles()) {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            String relativePath = relative(file);
            if (KNOWN_REFACTOR_DEBT.contains(relativePath)) {
                continue;
            }
            if (lines.size() > MAX_CLASS_LINES_SOFT_LIMIT) {
                warningsAsViolations.add(relativePath + " tiene " + lines.size()
                        + " líneas; revisar si requiere división.");
            }
            int longestMethod = longestMethodApproximation(lines);
            if (longestMethod > MAX_METHOD_LINES_SOFT_LIMIT) {
                warningsAsViolations.add(relativePath + " contiene un método aproximado de "
                        + longestMethod + " líneas; revisar responsabilidad única.");
            }
        }
        assertNoViolations(warningsAsViolations);
    }

    @Test
    void mainToolbarActionsMustBeBackedByViewModelMethods() throws IOException {
        Path toolbar = MAIN_SOURCES.resolve("com/marcosmoreira/domainmodelstudio/presentation/toolbar/GlobalToolbarView.java");
        Path viewModel = MAIN_SOURCES.resolve("com/marcosmoreira/domainmodelstudio/presentation/toolbar/MainToolbarViewModel.java");
        String toolbarSource = Files.readString(toolbar, StandardCharsets.UTF_8);
        String viewModelSource = Files.readString(viewModel, StandardCharsets.UTF_8);

        Matcher matcher = Pattern.compile("(?:button|notationButton)\\(\"([^\"]+)\",\\s*\"[^\"]*\",\\s*viewModel::([a-zA-Z0-9_]+)").matcher(toolbarSource);
        List<String> violations = new ArrayList<>();
        int count = 0;
        while (matcher.find()) {
            count++;
            String label = matcher.group(1);
            String method = matcher.group(2);
            if (!viewModelSource.contains("void " + method + "()")) {
                violations.add("Botón '" + label + "' referencia viewModel::" + method
                        + " pero no existe método público equivalente.");
            }
        }
        assertTrue(count >= 7, "La toolbar global debería conservar las acciones principales del MVP.");
        assertNoViolations(violations);
    }

    @Test
    void visibleStartupTextShouldAvoidScaffoldingVocabulary() throws IOException {
        List<String> violations = new ArrayList<>();
        List<Path> visibleTextFiles = List.of(
                MAIN_SOURCES.resolve("com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasViewModel.java"),
                MAIN_SOURCES.resolve("com/marcosmoreira/domainmodelstudio/presentation/dialogs/AboutDialog.java"),
                MAIN_SOURCES.resolve("com/marcosmoreira/domainmodelstudio/presentation/statusbar/StatusBarViewModel.java")
        );
        for (Path file : visibleTextFiles) {
            String source = Files.readString(file, StandardCharsets.UTF_8).toLowerCase();
            if (source.contains("placeholder") || source.contains("tanda ")) {
                violations.add(relative(file) + " contiene jerga de andamiaje visible potencial.");
            }
        }
        assertNoViolations(violations);
    }

    @Test
    void welcomeWorkspaceStylesMustExist() throws IOException {
        Path css = RESOURCES.resolve("css/app-light.css");
        String source = Files.readString(css, StandardCharsets.UTF_8);
        assertTrue(source.contains(".welcome-panel"), "Debe existir estilo para el workspace de bienvenida.");
        assertTrue(source.contains(".welcome-title"), "Debe existir estilo para el título de bienvenida.");
        assertTrue(source.contains(".welcome-note"), "Debe existir estilo para nota de bienvenida.");
    }

    private static List<Path> javaFilesUnder(String layer) throws IOException {
        Path layerPath = MAIN_SOURCES
                .resolve("com")
                .resolve("marcosmoreira")
                .resolve("domainmodelstudio")
                .resolve(layer);
        if (!Files.isDirectory(layerPath)) {
            return List.of();
        }
        try (Stream<Path> stream = Files.walk(layerPath)) {
            return stream.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static List<Path> allMainJavaFiles() throws IOException {
        try (Stream<Path> stream = Files.walk(MAIN_SOURCES)) {
            return stream.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static int longestMethodApproximation(List<String> lines) {
        int longest = 0;
        int current = 0;
        int depth = 0;
        boolean insideMethod = false;
        for (String rawLine : lines) {
            String line = rawLine.strip();
            if (!insideMethod && looksLikeMethodStart(line)) {
                insideMethod = true;
                current = 0;
                depth = 0;
            }
            if (insideMethod) {
                current++;
                depth += count(line, '{');
                depth -= count(line, '}');
                if (depth <= 0 && line.contains("}")) {
                    longest = Math.max(longest, current);
                    insideMethod = false;
                }
            }
        }
        return longest;
    }

    private static boolean looksLikeMethodStart(String line) {
        return (line.startsWith("public ") || line.startsWith("private ") || line.startsWith("protected "))
                && line.contains("(")
                && line.contains(")")
                && line.contains("{")
                && !line.contains(" class ")
                && !line.contains(" interface ")
                && !line.contains(" enum ");
    }

    private static int count(String value, char target) {
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == target) {
                count++;
            }
        }
        return count;
    }

    private static void assertNoViolations(List<String> violations) {
        assertTrue(violations.isEmpty(), "Problemas detectados:\n" + String.join("\n", violations));
    }

    private static String relative(Path file) {
        return Path.of("").toAbsolutePath().relativize(file.toAbsolutePath()).toString().replace('\\', '/');
    }
}
