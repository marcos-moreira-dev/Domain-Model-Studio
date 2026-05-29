package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Guardarraíl editorial RIA-1: recursos IA listos para exportación de producción. */
class AiResourceEditorialQualitySourceTest {

    private static final Path AI_ROOT = Path.of("src", "main", "resources", "ai-resources");
    private static final Path AI_TEMPLATES = AI_ROOT.resolve(Path.of("official-markdown", "plantillas"));
    private static final Path PUBLIC_TEMPLATES = Path.of("examples", "markdown", "plantillas");
    private static final Path AI_LOGICAL_INTAKE = AI_ROOT.resolve(Path.of("official-markdown", "levantamiento-logico"));
    private static final Path PUBLIC_LOGICAL_INTAKE_TEMPLATE = PUBLIC_TEMPLATES.resolve("logical_business_intake.md");

    private static final List<String> OBSOLETE_PRODUCT_PHRASES = List.of(
            "importable experimental",
            "vista visual derivada",
            "Vista lógica derivada",
            "Concepto persistible derivado del levantamiento lógico",
            "Salida informativa derivada de datos y reglas",
            "futuro módulo de Levantamiento lógico",
            "Entidades derivadas",
            "antes de derivar",
            "Derivar modelo conceptual",
            "Derivar grafo lógico",
            "Fuente de derivación",
            "Pedir a la IA que derive",
            "derivación técnica",
            "se pueden derivar hacia",
            "fuente madre",
            "vistas derivadas");

    @Test
    void conceptualTemplateShouldBeImportableAndAlignedWithDescriptor() throws IOException {
        AiResourceDescriptor descriptor = OfficialAiResourceDescriptors.all().stream()
                .filter(resource -> resource.id().equals("plantilla-oficial-conceptual-model"))
                .findFirst()
                .orElseThrow();
        Map<String, String> metadata = frontMatter(AI_TEMPLATES.resolve("conceptual_model.md"));

        assertTrue(descriptor.importableByApplication(), "La plantilla conceptual debe coincidir con el parser activo.");
        assertEquals("conceptual-model", metadata.get("diagram_type"));
        assertEquals("true", metadata.get("importable"));
        assertEquals("importable-template", metadata.get("status"));
    }

    @Test
    void publicTemplatesShouldMirrorAiTemplates() throws IOException {
        try (Stream<Path> files = Files.list(AI_TEMPLATES)) {
            for (Path aiTemplate : files
                    .filter(path -> path.getFileName().toString().endsWith(".md"))
                    .toList()) {
                Path publicTemplate = PUBLIC_TEMPLATES.resolve(aiTemplate.getFileName().toString());
                assertTrue(Files.isRegularFile(publicTemplate), "Falta plantilla pública para " + aiTemplate);
                assertEquals(
                        normalized(Files.readString(aiTemplate, StandardCharsets.UTF_8)),
                        normalized(Files.readString(publicTemplate, StandardCharsets.UTF_8)),
                        "La plantilla pública debe ser espejo exacto del recurso IA: " + aiTemplate.getFileName());
            }
        }
    }

    @Test
    void logicalBusinessIntakeTemplateShouldMirrorPublicAliasAndHaveProductFrontMatter() throws IOException {
        Path aiTemplate = AI_LOGICAL_INTAKE.resolve("logical_business_intake_template.md");
        Map<String, String> metadata = frontMatter(aiTemplate);

        assertEquals("1", metadata.get("dms_version"));
        assertEquals("logical-business-intake", metadata.get("diagram_type"));
        assertEquals("Plantilla maestra — Levantamiento lógico de negocio", metadata.get("name"));
        assertEquals("template", metadata.get("sample_kind"));
        assertEquals("importable-template", metadata.get("status"));
        assertEquals("logical-business-master-v1", metadata.get("canonical_contract"));
        assertEquals("true", metadata.get("importable"));
        assertEquals("expediente lógico documental", metadata.get("intended_output"));
        assertEquals(
                normalized(Files.readString(aiTemplate, StandardCharsets.UTF_8)),
                normalized(Files.readString(PUBLIC_LOGICAL_INTAKE_TEMPLATE, StandardCharsets.UTF_8)),
                "La plantilla pública de levantamiento lógico debe espejar la plantilla IA canónica.");
    }

    @Test
    void logicalBusinessIntakeResourcesShouldHaveCompleteProductFrontMatter() throws IOException {
        try (Stream<Path> files = Files.list(AI_LOGICAL_INTAKE)) {
            for (Path resource : files.filter(path -> path.getFileName().toString().endsWith(".md")).toList()) {
                Map<String, String> metadata = frontMatter(resource);

                assertEquals("1", metadata.get("dms_version"), resource.toString());
                assertEquals("logical-business-intake", metadata.get("diagram_type"), resource.toString());
                assertFalse(metadata.getOrDefault("name", "").isBlank(), resource + " debe declarar name.");
                assertFalse(metadata.getOrDefault("domain", "").isBlank(), resource + " debe declarar domain.");
                assertFalse(metadata.getOrDefault("status", "").isBlank(), resource + " debe declarar status.");
                assertEquals("logical-business-master-v1", metadata.get("canonical_contract"), resource.toString());
                assertEquals("true", metadata.get("importable"), resource.toString());
                assertEquals("expediente lógico documental", metadata.get("intended_output"), resource.toString());
            }
        }
    }

    @Test
    void exportedAiResourcesShouldNotUseObsoleteDerivationOrExperimentalLanguage() throws IOException {
        Set<Path> auditedRoots = Set.of(
                AI_ROOT,
                Path.of("examples", "markdown", "plantillas"),
                Path.of("examples", "markdown", "diagramas"),
                Path.of("examples", "markdown", "levantamiento-logico"));

        for (Path root : auditedRoots) {
            try (Stream<Path> files = Files.walk(root)) {
                for (Path markdown : files.filter(path -> path.getFileName().toString().endsWith(".md")).toList()) {
                    String content = Files.readString(markdown, StandardCharsets.UTF_8);
                    for (String phrase : OBSOLETE_PRODUCT_PHRASES) {
                        assertFalse(content.contains(phrase), markdown + " conserva lenguaje obsoleto: " + phrase);
                    }
                }
            }
        }
    }

    private static Map<String, String> frontMatter(Path markdownFile) throws IOException {
        String content = Files.readString(markdownFile, StandardCharsets.UTF_8);
        String[] lines = content.split("\\R");
        assertTrue(lines.length > 2, markdownFile + " debe contener front matter.");
        assertEquals("---", lines[0].strip(), markdownFile + " debe iniciar con front matter.");

        Map<String, String> metadata = new HashMap<>();
        for (int index = 1; index < lines.length; index++) {
            String line = lines[index].strip();
            if ("---".equals(line)) {
                return metadata;
            }
            if (line.contains(":")) {
                String key = line.substring(0, line.indexOf(':')).strip();
                String value = line.substring(line.indexOf(':') + 1).strip();
                metadata.put(key, unquote(value));
            }
        }
        throw new AssertionError(markdownFile + " no cierra el front matter.");
    }

    private static String unquote(String value) {
        String cleaned = value.strip();
        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            return cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned;
    }

    private static String normalized(String value) {
        return value.replace("\r\n", "\n").stripTrailing();
    }
}
