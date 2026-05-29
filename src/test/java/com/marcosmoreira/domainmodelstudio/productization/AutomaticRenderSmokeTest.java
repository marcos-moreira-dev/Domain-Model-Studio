package com.marcosmoreira.domainmodelstudio.productization;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.examples.DefaultOfficialExampleCatalog;
import com.marcosmoreira.domainmodelstudio.application.examples.OfficialExampleDescriptor;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DiagramMarkdownImportDispatcher;
import com.marcosmoreira.domainmodelstudio.infrastructure.svg.MultiNotationSvgDiagramExporter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Smoke automatico de render documental SVG sobre ejemplos oficiales.
 *
 * <p>No reemplaza el smoke visual humano: genera evidencias reproducibles en
 * {@code target/smoke-render} para revisar dimensiones, vacios, raster falso y
 * legibilidad inicial de salidas vectoriales.</p>
 */
final class AutomaticRenderSmokeTest {

    private static final Pattern SVG_TAG_PATTERN = Pattern.compile("<svg\\b[^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern WIDTH_PATTERN = Pattern.compile("\\bwidth=\\\"([0-9.]+)");
    private static final Pattern HEIGHT_PATTERN = Pattern.compile("\\bheight=\\\"([0-9.]+)");
    private static final Pattern VIEWBOX_PATTERN = Pattern.compile("\\bviewBox=\\\"[^\\\"]*?([0-9.]+)\\s+([0-9.]+)\\\"", Pattern.CASE_INSENSITIVE);

    @Test
    void officialVisualExamplesShouldGenerateVectorSmokeEvidence() throws Exception {
        DiagramTypeRegistry registry = new DefaultDiagramTypeRegistry();
        DiagramMarkdownImportDispatcher dispatcher = new DiagramMarkdownImportDispatcher(registry);
        MultiNotationSvgDiagramExporter exporter = new MultiNotationSvgDiagramExporter();
        Path outputDirectory = Path.of("target", "smoke-render");
        resetDirectory(outputDirectory);

        List<RenderSmokeResult> results = new ArrayList<>();
        for (OfficialExampleDescriptor example : new DefaultOfficialExampleCatalog(registry).findImportable()) {
            DiagramTypeDescriptor descriptor = registry.findById(example.diagramTypeId()).orElseThrow();
            if (!descriptor.supports(DiagramCapability.EXPORT_SVG)) {
                results.add(RenderSmokeResult.skipped(example, "El tipo no declara EXPORT_SVG."));
                continue;
            }
            results.add(renderExample(example, dispatcher, exporter, outputDirectory));
        }

        writeMarkdownReport(results, outputDirectory.resolve("SMOKE_RENDER_AUTOMATICO.md"));
        writeContactSheet(results, outputDirectory.resolve("contact_sheet.html"));
        writeCsvReport(results, outputDirectory.resolve("smoke-render.csv"));

        List<RenderSmokeResult> rendered = results.stream().filter(RenderSmokeResult::rendered).toList();
        assertTrue(rendered.size() >= 16, "Debe renderizar al menos los tipos visuales oficiales con SVG.");
        List<String> violations = rendered.stream()
                .filter(result -> !result.ok())
                .map(RenderSmokeResult::summary)
                .toList();
        assertTrue(violations.isEmpty(), "Problemas de smoke render: " + violations);
    }

    private RenderSmokeResult renderExample(
            OfficialExampleDescriptor example,
            DiagramMarkdownImportDispatcher dispatcher,
            MultiNotationSvgDiagramExporter exporter,
            Path outputDirectory
    ) {
        try {
            String markdown = readClasspathResource(example.classpathLocation());
            DiagramProject imported = dispatcher.parse(markdown, example.sourceName());
            DiagramProject prepared = prepareLayout(imported);
            String svg = exporter.export(prepared);
            SvgStats stats = SvgStats.from(svg);
            String fileName = fileStem(example) + ".svg";
            Files.writeString(outputDirectory.resolve(fileName), svg, StandardCharsets.UTF_8);
            return RenderSmokeResult.rendered(example, fileName, stats);
        } catch (IOException | MarkdownModelParsingException | RuntimeException exception) {
            return RenderSmokeResult.failed(example, exception.getClass().getSimpleName() + ": " + exception.getMessage());
        }
    }

    private DiagramProject prepareLayout(DiagramProject project) {
        if (DiagramTypeId.CONCEPTUAL_MODEL.equals(project.metadata().diagramTypeId())) {
            return new GenerateInitialChenLayoutUseCase().generate(project);
        }
        return new VisualLayoutService().ensureVisualLayout(project);
    }

    private String readClasspathResource(String location) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream stream = loader.getResourceAsStream(location)) {
            if (stream == null) {
                throw new IOException("No se encontro recurso classpath: " + location);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void resetDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            try (var paths = Files.walk(directory)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(path);
                }
            }
        }
        Files.createDirectories(directory);
    }

    private void writeMarkdownReport(List<RenderSmokeResult> results, Path target) throws IOException {
        StringBuilder report = new StringBuilder(24_000);
        report.append("# Smoke render automatico\n\n");
        report.append("Generado por `AutomaticRenderSmokeTest`. Este reporte valida salidas SVG vectoriales documentales sobre ejemplos oficiales importables.\n\n");
        report.append("## Resumen\n\n");
        report.append("- Renderizados: **").append(results.stream().filter(RenderSmokeResult::rendered).count()).append("**\n");
        report.append("- Omitidos: **").append(results.stream().filter(RenderSmokeResult::skipped).count()).append("**\n");
        report.append("- Fallidos: **").append(results.stream().filter(RenderSmokeResult::failed).count()).append("**\n\n");
        report.append("## Resultados\n\n");
        report.append("| Estado | Tipo | Ejemplo | Archivo | Dimensiones | Nodos aprox. | Conectores aprox. | Observacion |\n");
        report.append("|---|---|---|---|---:|---:|---:|---|\n");
        for (RenderSmokeResult result : results) {
            report.append("| ").append(result.status()).append(" | ")
                    .append(escapeTable(result.example().diagramTypeId().value())).append(" | ")
                    .append(escapeTable(result.example().title())).append(" | ")
                    .append(result.fileName().map(name -> "`" + name + "`").orElse("—")).append(" | ")
                    .append(result.dimensions()).append(" | ")
                    .append(result.stats().map(SvgStats::nodeCount).orElse(0)).append(" | ")
                    .append(result.stats().map(SvgStats::connectorCount).orElse(0)).append(" | ")
                    .append(escapeTable(result.observation())).append(" |\n");
        }
        report.append("\n## Criterios automaticos\n\n");
        report.append("- El SVG debe contener etiqueta `<svg>`.\n");
        report.append("- No debe contener `<image>` ni `data:image`.\n");
        report.append("- Debe declarar dimensiones o `viewBox` no triviales.\n");
        report.append("- La salida debe quedar en `target/smoke-render/` para revision humana.\n");
        Files.writeString(target, report.toString(), StandardCharsets.UTF_8);
    }

    private void writeContactSheet(List<RenderSmokeResult> results, Path target) throws IOException {
        StringBuilder html = new StringBuilder(18_000);
        html.append("<!doctype html><html lang=\"es\"><head><meta charset=\"utf-8\">");
        html.append("<title>Smoke render automatico</title>");
        html.append("<style>body{font-family:Segoe UI,Arial,sans-serif;margin:24px;background:#f7f7f7;color:#1f2933}");
        html.append(".grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(360px,1fr));gap:16px}");
        html.append(".card{background:white;border:1px solid #cfd7df;padding:12px}.frame{height:280px;border:1px solid #d8dee5;background:white;overflow:auto}");
        html.append("iframe{width:100%;height:100%;border:0}.meta{font-size:12px;color:#4b5563}</style></head><body>");
        html.append("<h1>Smoke render automatico</h1><p>SVG vectorial documental generado desde ejemplos oficiales importables.</p><div class=\"grid\">");
        for (RenderSmokeResult result : results.stream().filter(RenderSmokeResult::rendered).toList()) {
            html.append("<article class=\"card\"><h2>").append(escapeHtml(result.example().diagramTypeName())).append("</h2>")
                    .append("<p class=\"meta\">").append(escapeHtml(result.example().title())).append("<br>")
                    .append(escapeHtml(result.dimensions())).append("</p>")
                    .append("<div class=\"frame\"><iframe src=\"").append(escapeHtml(result.fileName().orElse(""))).append("\"></iframe></div>")
                    .append("</article>");
        }
        html.append("</div></body></html>");
        Files.writeString(target, html.toString(), StandardCharsets.UTF_8);
    }

    private void writeCsvReport(List<RenderSmokeResult> results, Path target) throws IOException {
        StringBuilder csv = new StringBuilder("status,type,title,file,width,height,nodes,connectors,observation\n");
        for (RenderSmokeResult result : results) {
            csv.append(csv(result.status())).append(',')
                    .append(csv(result.example().diagramTypeId().value())).append(',')
                    .append(csv(result.example().title())).append(',')
                    .append(csv(result.fileName().orElse(""))).append(',')
                    .append(result.stats().map(SvgStats::width).orElse(0.0)).append(',')
                    .append(result.stats().map(SvgStats::height).orElse(0.0)).append(',')
                    .append(result.stats().map(SvgStats::nodeCount).orElse(0)).append(',')
                    .append(result.stats().map(SvgStats::connectorCount).orElse(0)).append(',')
                    .append(csv(result.observation())).append('\n');
        }
        Files.writeString(target, csv.toString(), StandardCharsets.UTF_8);
    }

    private String fileStem(OfficialExampleDescriptor example) {
        return sanitize(example.diagramTypeId().value() + "__" + example.id());
    }

    private String sanitize(String value) {
        return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9._-]+", "-").replaceAll("-+", "-");
    }

    private String escapeTable(String value) {
        return value == null ? "" : value.replace("|", "\\|").replace("\n", " ");
    }

    private String escapeHtml(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private String csv(String value) {
        String safe = value == null ? "" : value.replace("\"", "\"\"");
        return "\"" + safe + "\"";
    }

    private record RenderSmokeResult(
            OfficialExampleDescriptor example,
            String status,
            Optional<String> fileName,
            Optional<SvgStats> stats,
            String observation
    ) {
        static RenderSmokeResult rendered(OfficialExampleDescriptor example, String fileName, SvgStats stats) {
            return new RenderSmokeResult(example, stats.ok() ? "OK" : "REVISAR", Optional.of(fileName), Optional.of(stats), stats.observation());
        }

        static RenderSmokeResult skipped(OfficialExampleDescriptor example, String reason) {
            return new RenderSmokeResult(example, "OMITIDO", Optional.empty(), Optional.empty(), reason);
        }

        static RenderSmokeResult failed(OfficialExampleDescriptor example, String reason) {
            return new RenderSmokeResult(example, "FALLO", Optional.empty(), Optional.empty(), reason);
        }

        boolean rendered() {
            return stats.isPresent();
        }

        boolean skipped() {
            return "OMITIDO".equals(status);
        }

        boolean failed() {
            return "FALLO".equals(status);
        }

        boolean ok() {
            return !failed() && stats.map(SvgStats::ok).orElse(true);
        }

        String dimensions() {
            return stats.map(value -> Math.round(value.width()) + " x " + Math.round(value.height())).orElse("—");
        }

        String summary() {
            return example.diagramTypeId().value() + " / " + example.title() + ": " + observation;
        }
    }

    private record SvgStats(
            double width,
            double height,
            int nodeCount,
            int connectorCount,
            boolean hasSvg,
            boolean hasRasterImage,
            boolean hasDataImage
    ) {
        static SvgStats from(String svg) {
            String svgTag = svgTag(svg);
            double width = numericAttribute(svgTag, WIDTH_PATTERN).orElseGet(() -> viewBoxDimension(svgTag, true));
            double height = numericAttribute(svgTag, HEIGHT_PATTERN).orElseGet(() -> viewBoxDimension(svgTag, false));
            return new SvgStats(
                    width,
                    height,
                    count(svg, "<rect") + count(svg, "<ellipse") + count(svg, "<circle") + count(svg, "<polygon"),
                    count(svg, "<line") + count(svg, "<path") + count(svg, "<polyline"),
                    SVG_TAG_PATTERN.matcher(svg).find(),
                    Pattern.compile("<image\\b", Pattern.CASE_INSENSITIVE).matcher(svg).find(),
                    svg.toLowerCase(Locale.ROOT).contains("data:image"));
        }

        boolean ok() {
            return hasSvg
                    && !hasRasterImage
                    && !hasDataImage
                    && width >= 120.0
                    && height >= 120.0
                    && width <= 10_000.0
                    && height <= 10_000.0;
        }

        String observation() {
            if (!hasSvg) return "No contiene etiqueta svg.";
            if (hasRasterImage) return "Contiene elemento image; revisar raster fallback.";
            if (hasDataImage) return "Contiene data:image; revisar raster incrustado.";
            if (width < 120.0 || height < 120.0) return "Dimensiones demasiado pequenas.";
            if (width > 10_000.0 || height > 10_000.0) return "Dimensiones extremas; revisar layout/exportacion.";
            return "SVG vectorial documental generado.";
        }

        private static String svgTag(String svg) {
            Matcher matcher = SVG_TAG_PATTERN.matcher(svg);
            return matcher.find() ? matcher.group() : "";
        }

        private static Optional<Double> numericAttribute(String svgTag, Pattern pattern) {
            Matcher matcher = pattern.matcher(svgTag);
            if (!matcher.find()) {
                return Optional.empty();
            }
            return Optional.of(Double.parseDouble(matcher.group(1)));
        }

        private static double viewBoxDimension(String svgTag, boolean width) {
            Matcher matcher = VIEWBOX_PATTERN.matcher(svgTag);
            if (!matcher.find()) {
                return 0.0;
            }
            return Double.parseDouble(matcher.group(width ? 1 : 2));
        }

        private static int count(String text, String token) {
            int count = 0;
            int from = 0;
            while ((from = text.indexOf(token, from)) >= 0) {
                count++;
                from += token.length();
            }
            return count;
        }
    }
}
