package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ManualFigureNoPlaceholderSourceTest {

    private static final Path MAIN = Path.of("src", "main", "java");
    private static final Path BASE = MAIN.resolve("com/marcosmoreira/domainmodelstudio".replace('/', java.io.File.separatorChar));
    private static final Pattern FIGURE = Pattern.compile("figure\\(\\\"([^\\\"]+)\\\"");
    private static final Pattern CASE = Pattern.compile("case \\\"([^\\\"]+)\\\"");

    @Test
    void everyRegisteredAcademicFigureShouldHaveAConcreteDrawingCase() throws IOException {
        Set<String> registered = figureIds();
        Set<String> drawn = drawnFigureIds();
        List<String> missing = registered.stream()
                .filter(id -> !drawn.contains(id))
                .sorted()
                .toList();

        assertTrue(missing.isEmpty(), "Figuras académicas registradas sin dibujo real:\n" + String.join("\n", missing));
    }

    @Test
    void manualFigureFactoryShouldNotRenderTheOldFigureIdPlaceholder() throws IOException {
        Path factory = BASE.resolve("presentation/dialogs/ManualFigureNodeFactory.java".replace('/', java.io.File.separatorChar));
        String source = Files.readString(factory, StandardCharsets.UTF_8);

        assertTrue(!source.contains("Figura: "),
                "La guía académica no debe volver al placeholder textual 'Figura: <id>'.");
    }

    private static Set<String> figureIds() throws IOException {
        Path catalog = BASE.resolve("application/theory/DefaultTheoryFigureCatalog.java".replace('/', java.io.File.separatorChar));
        return matches(FIGURE, Files.readString(catalog, StandardCharsets.UTF_8));
    }

    private static Set<String> drawnFigureIds() throws IOException {
        Set<String> result = new HashSet<>();
        Path dialogs = BASE.resolve("presentation/dialogs".replace('/', java.io.File.separatorChar));
        try (Stream<Path> files = Files.list(dialogs)) {
            for (Path file : files.filter(path -> path.getFileName().toString().startsWith("ManualFigure"))
                    .filter(path -> path.getFileName().toString().endsWith("Figures.java"))
                    .toList()) {
                result.addAll(matches(CASE, Files.readString(file, StandardCharsets.UTF_8)));
            }
        }
        return result;
    }

    private static Set<String> matches(Pattern pattern, String source) {
        Set<String> result = new HashSet<>();
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }
}
