package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Exportador PNG para matrices documentales/tabulares.
 *
 * <p>Esta ruta es deliberadamente distinta a la exportación de canvas: roles y
 * permisos se entrega como matriz administrativa, por lo que se captura el nodo
 * tabular completo y no el viewport visible de un lienzo.</p>
 */
public final class MatrixSnapshotExporter {

    private static final double MIN_SIZE = 1.0;

    public void export(Node matrixRoot, Path targetFile) throws IOException {
        if (matrixRoot == null) {
            throw new IOException("No hay matriz lista para exportar PNG.");
        }
        Path normalized = normalizeTarget(targetFile);
        matrixRoot.applyCss();
        matrixRoot.autosize();
        if (matrixRoot instanceof Parent parent) {
            parent.layout();
        }

        Bounds bounds = matrixRoot.getBoundsInLocal();
        double width = dimension(bounds.getWidth());
        double height = dimension(bounds.getHeight());
        WritableImage image = new WritableImage((int) Math.ceil(width), (int) Math.ceil(height));
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.WHITE);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));
        matrixRoot.snapshot(parameters, image);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", normalized.toFile());
    }

    private static double dimension(double value) {
        return Double.isFinite(value) && value > MIN_SIZE ? value : MIN_SIZE;
    }

    private static Path normalizeTarget(Path targetFile) throws IOException {
        if (targetFile == null) {
            throw new IOException("No se indicó archivo destino.");
        }
        Path normalized = targetFile.toAbsolutePath().normalize();
        Path parent = normalized.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        return normalized;
    }
}
