package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Exporta el diagrama actual a PNG a partir de un render limpio.
 *
 * <p>La exportación queda fuera de {@link DiagramCanvasView} para mantener separada
 * la interacción del lienzo respecto a la generación de archivos.</p>
 */
final class CanvasPngExporter {

    private static final double EXPORT_PADDING = 48.0;
    private static final long MAX_RAW_SNAPSHOT_BYTES = 900L * 1024L * 1024L;

    private final DiagramCanvasViewModel viewModel;
    private final ChenDiagramRenderer chenRenderer;
    private final CrowsFootDiagramRenderer crowsFootRenderer;

    CanvasPngExporter(
            DiagramCanvasViewModel viewModel,
            ChenDiagramRenderer chenRenderer,
            CrowsFootDiagramRenderer crowsFootRenderer
    ) {
        this.viewModel = viewModel;
        this.chenRenderer = chenRenderer;
        this.crowsFootRenderer = crowsFootRenderer;
    }

    void export(Path targetFile) throws IOException {
        Path normalizedTarget = normalizeTarget(targetFile);
        DiagramProject project = viewModel.currentProject();
        if (project == null) {
            throw new IllegalStateException("No hay proyecto abierto para exportar PNG.");
        }
        Group rendered = switch (project.metadata().activeNotation()) {
            case CHEN -> chenRenderer.render(project, Set.of());
            case CROWS_FOOT -> crowsFootRenderer.render(project, Set.of());
        };

        Group measureRoot = new Group(rendered);
        Scene measureScene = new Scene(measureRoot);
        String css = getClass().getResource("/css/app-light.css") == null
                ? null
                : getClass().getResource("/css/app-light.css").toExternalForm();
        if (css != null) {
            measureScene.getStylesheets().add(css);
        }
        measureRoot.applyCss();
        measureRoot.layout();

        Bounds bounds = measureRoot.getBoundsInLocal();
        double minX = bounds.getMinX() - EXPORT_PADDING;
        double minY = bounds.getMinY() - EXPORT_PADDING;
        double widthValue = Math.max(1.0, bounds.getWidth() + EXPORT_PADDING * 2.0);
        double heightValue = Math.max(1.0, bounds.getHeight() + EXPORT_PADDING * 2.0);
        int width = Math.max(1, (int) Math.ceil(widthValue));
        int height = Math.max(1, (int) Math.ceil(heightValue));
        assertReasonableSnapshotSize(width, height);

        Rectangle background = new Rectangle(minX, minY, widthValue, heightValue);
        background.setFill(exportBackgroundColor());
        Group exportRoot = new Group(background, rendered);
        Scene exportScene = new Scene(exportRoot, width, height);
        if (css != null) {
            exportScene.getStylesheets().add(css);
        }
        exportRoot.applyCss();
        exportRoot.layout();

        WritableImage image = new WritableImage(width, height);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(exportBackgroundColor());
        parameters.setViewport(new Rectangle2D(minX, minY, widthValue, heightValue));
        exportRoot.snapshot(parameters, image);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", normalizedTarget.toFile());
    }


    private static Path normalizeTarget(Path targetFile) throws IOException {
        if (targetFile == null) {
            throw new IOException("No se indicó archivo destino para PNG.");
        }
        Path normalized = targetFile.toAbsolutePath().normalize();
        Path parent = normalized.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        return normalized;
    }

    private static void assertReasonableSnapshotSize(int width, int height) throws IOException {
        long rawBytes = Math.multiplyExact(Math.max(1, width), Math.max(1, height)) * 4L;
        if (rawBytes > MAX_RAW_SNAPSHOT_BYTES) {
            throw new IOException("La imagen PNG conceptual sería demasiado grande para exportar de forma segura: "
                    + width + " x " + height + " px; RAM bruta aproximada sin compresión: "
                    + formatMib(rawBytes) + ". Reduce el contenido exportado o usa SVG.");
        }
    }

    private static String formatMib(long bytes) {
        return String.format(java.util.Locale.ROOT, "%.1f MiB", bytes / 1024.0 / 1024.0);
    }

    private Color exportBackgroundColor() {
        DiagramProject project = viewModel.currentProject();
        if (project == null) {
            return Color.WHITE;
        }
        try {
            return Color.web(project.styleSheet().appearance().diagramBackground().toHex());
        } catch (IllegalArgumentException exception) {
            return Color.WHITE;
        }
    }
}
