package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderMetadata;
import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderPolicy;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfileResolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;

/**
 * Exportador PNG común para diagramas montados sobre {@link InteractiveCanvasAdapter}.
 *
 * <p>No conoce módulos, UML, C4, BPMN ni wireframes. Recibe un adaptador y un
 * render kit específico, monta una escena JavaFX limpia para resolver CSS,
 * mide los límites reales renderizados y genera una imagen con fondo explícito.
 * Esta ruta evita snapshots negros o vacíos cuando el nodo exportable está fuera
 * de una escena normal del desktop.</p>
 */
public final class InteractiveCanvasPngExporter {

    private static final double MIN_EXPORT_SIZE = 1.0;
    private static final String DEFAULT_EXPORT_STYLE_CLASS = "interactive-canvas-export-surface";
    private static final String APP_LIGHT_CSS = "/css/app-light.css";
    private static final long MAX_RAW_SNAPSHOT_BYTES = 900L * 1024L * 1024L;
    private static final double DEFAULT_PNG_SCALE = 2.0;
    private static final double MAX_PNG_SCALE = 3.0;
    private static final String PNG_SCALE_PROPERTY = "domainmodelstudio.export.png.scale";

    private final InteractiveCanvasAdapter adapter;
    private final InteractiveCanvasRenderKit renderKit;
    private final DiagramDrawingFacade drawingFacade;
    private final CanvasLayeringPolicy layeringPolicy;
    private final String exportStyleClass;
    private final Supplier<DiagramExportHeaderMetadata> headerMetadataSupplier;

    public InteractiveCanvasPngExporter(
            InteractiveCanvasAdapter adapter,
            InteractiveCanvasRenderKit renderKit
    ) {
        this(adapter, renderKit, DEFAULT_EXPORT_STYLE_CLASS);
    }

    public InteractiveCanvasPngExporter(
            InteractiveCanvasAdapter adapter,
            InteractiveCanvasRenderKit renderKit,
            String exportStyleClass
    ) {
        this(adapter, renderKit, exportStyleClass,
                () -> DiagramExportHeaderPolicy.forDiagramType(
                        Objects.requireNonNull(adapter, "adapter").diagramTypeId(),
                        "Diagrama exportado"));
    }

    public InteractiveCanvasPngExporter(
            InteractiveCanvasAdapter adapter,
            InteractiveCanvasRenderKit renderKit,
            String exportStyleClass,
            Supplier<DiagramExportHeaderMetadata> headerMetadataSupplier
    ) {
        this.adapter = Objects.requireNonNull(adapter, "adapter");
        this.renderKit = Objects.requireNonNull(renderKit, "renderKit");
        this.drawingFacade = DiagramDrawingFacade.defaults();
        this.layeringPolicy = CanvasLayeringPolicy.standard();
        this.exportStyleClass = normalizeStyleClass(exportStyleClass);
        this.headerMetadataSupplier = headerMetadataSupplier == null
                ? () -> DiagramExportHeaderPolicy.forDiagramType(this.adapter.diagramTypeId(), "Diagrama exportado")
                : headerMetadataSupplier;
    }

    public void export(Path targetFile) throws IOException {
        Path normalized = normalizeTarget(targetFile);
        InteractiveCanvasModel model = InteractiveCanvasModel.from(adapter);
        Group diagram = renderDiagram(model);

        Pane measureRoot = newExportRoot(diagram);
        Scene measureScene = new Scene(measureRoot);
        addStylesheet(measureScene);
        applySceneLayout(measureRoot);

        CanvasExportSurface exportSurface = CanvasExportSurface.of(measuredBounds(diagram, model));
        measureRoot.getChildren().clear();

        DiagramExportHeaderMetadata headerMetadata = headerMetadata();
        double headerHeight = DiagramExportHeaderPolicy.PNG_HEADER_HEIGHT;
        double exportWidth = exportSurface.exportWidth();
        double exportHeight = exportSurface.exportHeight() + headerHeight;
        diagram.setTranslateX(-exportSurface.exportX());
        diagram.setTranslateY(headerHeight - exportSurface.exportY());
        Pane exportRoot = newExportRoot(
                background(exportWidth, exportHeight),
                exportHeader(headerMetadata, exportWidth),
                diagram);
        sizeRoot(exportRoot, exportWidth, exportHeight);
        Scene exportScene = new Scene(
                exportRoot,
                exportDimension(exportWidth),
                exportDimension(exportHeight),
                exportBackgroundColor()
        );
        addStylesheet(exportScene);
        applySceneLayout(exportRoot);

        int logicalExportWidth = exportDimension(exportWidth);
        int logicalExportHeight = exportDimension(exportHeight);
        double exportScale = exportScale();
        int pixelWidth = scaledDimension(logicalExportWidth, exportScale);
        int pixelHeight = scaledDimension(logicalExportHeight, exportScale);
        assertReasonableSnapshotSize(pixelWidth, pixelHeight);
        WritableImage image = new WritableImage(pixelWidth, pixelHeight);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(exportBackgroundColor());
        parameters.setTransform(Transform.scale(exportScale, exportScale));
        parameters.setViewport(new Rectangle2D(0.0, 0.0, logicalExportWidth, logicalExportHeight));
        exportRoot.snapshot(parameters, image);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", normalized.toFile());
    }

    private Group renderDiagram(InteractiveCanvasModel model) {
        Group diagram = new Group();
        CanvasRenderedNodeLayers renderedNodes = renderLayeredNodes(model);
        diagram.getChildren().addAll(renderedNodes.backgroundNodes());
        diagram.getChildren().addAll(renderConnectors(model));
        diagram.getChildren().addAll(renderedNodes.foregroundNodes());
        diagram.getChildren().addAll(renderConnectorLabels(model));
        return diagram;
    }

    private List<Node> renderConnectors(InteractiveCanvasModel model) {
        return model.visibleConnectors().stream()
                .map(connector -> {
                    Node rendered = renderKit.renderConnector(connector, adapter, false, drawingFacade);
                    explicitStyleFor(connector.id()).ifPresent(style -> CanvasStyleApplier.applyConnectorStyle(rendered, style));
                    return rendered;
                })
                .toList();
    }

    private List<Node> renderConnectorLabels(InteractiveCanvasModel model) {
        List<Node> labels = new ArrayList<>();
        for (InteractiveCanvasConnector connector : model.visibleConnectors()) {
            Node label = renderConnectorLabel(connector, model);
            if (label != null) {
                labels.add(label);
            }
        }
        return labels;
    }

    private Node renderConnectorLabel(InteractiveCanvasConnector connector, InteractiveCanvasModel model) {
        if (connector.label().isBlank() || !commonConnectorLabelOverlayEnabled()) {
            return null;
        }
        List<Point2D> route = connectorRoute(connector, model);
        if (route.isEmpty()) {
            return null;
        }
        ConnectorLayout layout = model.layoutForConnector(connector.id()).orElse(null);
        Point2D point = CanvasConnectorLabelPositioner.labelPoint(route, layout);
        Node label = CanvasConnectorLabelNodeFactory.createExportLabel(connector.label(), point, connector.kind());
        explicitStyleFor(connector.id()).ifPresent(style -> CanvasStyleApplier.applyTextStyle(label, style));
        return label;
    }

    private boolean commonConnectorLabelOverlayEnabled() {
        DiagramInteractionProfile profile = DiagramInteractionProfileResolver.resolve(adapter.diagramTypeId());
        return profile.supportsCommonConnectorLabelOverlay();
    }

    private List<Point2D> connectorRoute(InteractiveCanvasConnector connector, InteractiveCanvasModel model) {
        return model.layoutForNode(connector.sourceNodeId())
                .flatMap(source -> model.layoutForNode(connector.targetNodeId())
                        .map(target -> CanvasConnectorGeometry.edgeToEdgePoints(
                                source,
                                target,
                                model.layoutForConnector(connector.id()),
                                null)))
                .orElseGet(List::of);
    }

    private CanvasRenderedNodeLayers renderLayeredNodes(InteractiveCanvasModel model) {
        return CanvasRenderedNodeLayers.from(
                layeringPolicy.orderNodes(model.visibleNodes(), model),
                model,
                layeringPolicy,
                this::renderNode);
    }

    private Node renderNode(InteractiveCanvasNode node, NodeLayout layout) {
        Node rendered = renderKit.renderNode(node, CanvasBounds.from(layout), false, drawingFacade);
        explicitStyleFor(node.id()).ifPresent(style -> CanvasStyleApplier.applyNodeStyle(rendered, style));
        return rendered;
    }


    private java.util.Optional<ElementStyle> explicitStyleFor(String elementId) {
        return adapter instanceof CanvasStylePort stylePort
                ? stylePort.explicitStyleForElement(elementId)
                : java.util.Optional.empty();
    }

    private Bounds measuredBounds(Group diagram, InteractiveCanvasModel model) {
        Bounds rendered = diagram.getBoundsInParent();
        Bounds semantic = model.contentBounds()
                .map(InteractiveCanvasPngExporter::toBounds)
                .orElseGet(() -> toBounds(adapter.contentBounds()));
        if (usable(rendered) && usable(semantic)) {
            return union(rendered, semantic);
        }
        if (usable(rendered)) {
            return rendered;
        }
        return semantic;
    }

    private Pane newExportRoot(Node... children) {
        Pane root = new Pane(children);
        root.getStyleClass().add("root");
        root.getStyleClass().add(DEFAULT_EXPORT_STYLE_CLASS);
        if (!DEFAULT_EXPORT_STYLE_CLASS.equals(exportStyleClass)) {
            root.getStyleClass().add(exportStyleClass);
        }
        root.setMinSize(MIN_EXPORT_SIZE, MIN_EXPORT_SIZE);
        root.setPrefSize(MIN_EXPORT_SIZE, MIN_EXPORT_SIZE);
        root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return root;
    }

    private static void sizeRoot(Pane root, double width, double height) {
        root.setMinSize(width, height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);
    }

    private Rectangle background(double width, double height) {
        Rectangle background = new Rectangle(0.0, 0.0, width, height);
        background.setFill(exportBackgroundColor());
        background.setMouseTransparent(true);
        return background;
    }

    private Group exportHeader(DiagramExportHeaderMetadata metadata, double width) {
        Group header = new Group();
        header.getStyleClass().add("canvas-export-header");
        double height = DiagramExportHeaderPolicy.PNG_HEADER_HEIGHT;
        Rectangle panel = new Rectangle(0.0, 0.0, width, height);
        panel.getStyleClass().add("canvas-export-header-panel");
        panel.setFill(Color.WHITE);
        panel.setStroke(Color.web("#D0D5DD"));
        panel.setStrokeWidth(1.0);
        panel.setMouseTransparent(true);

        Text title = new Text(24.0, 30.0, shorten(metadata.title(), 96));
        title.getStyleClass().add("canvas-export-header-title");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: 700;");
        title.setFill(Color.web("#1F2933"));

        Text subtitle = new Text(24.0, 52.0, shorten(metadata.compactSubtitle(), 132));
        subtitle.getStyleClass().add("canvas-export-header-subtitle");
        subtitle.setStyle("-fx-font-size: 12px;");
        subtitle.setFill(Color.web("#667085"));

        header.getChildren().addAll(panel, title, subtitle);
        return header;
    }

    private DiagramExportHeaderMetadata headerMetadata() {
        try {
            DiagramExportHeaderMetadata metadata = headerMetadataSupplier.get();
            return metadata == null
                    ? DiagramExportHeaderPolicy.forDiagramType(adapter.diagramTypeId(), "Diagrama exportado")
                    : metadata;
        } catch (RuntimeException exception) {
            return DiagramExportHeaderPolicy.forDiagramType(adapter.diagramTypeId(), "Diagrama exportado");
        }
    }

    private static void applySceneLayout(Pane root) {
        root.applyCss();
        root.autosize();
        root.layout();
    }

    private static Bounds toBounds(CanvasBounds bounds) {
        CanvasBounds safeBounds = bounds == null ? CanvasBounds.of(0.0, 0.0, 880.0, 600.0) : bounds;
        return new BoundingBox(safeBounds.x(), safeBounds.y(), safeBounds.width(), safeBounds.height());
    }

    private static Bounds union(Bounds first, Bounds second) {
        double minX = Math.min(first.getMinX(), second.getMinX());
        double minY = Math.min(first.getMinY(), second.getMinY());
        double maxX = Math.max(first.getMaxX(), second.getMaxX());
        double maxY = Math.max(first.getMaxY(), second.getMaxY());
        return new BoundingBox(minX, minY, Math.max(MIN_EXPORT_SIZE, maxX - minX), Math.max(MIN_EXPORT_SIZE, maxY - minY));
    }

    private static boolean usable(Bounds bounds) {
        return bounds != null
                && Double.isFinite(bounds.getMinX())
                && Double.isFinite(bounds.getMinY())
                && Double.isFinite(bounds.getWidth())
                && Double.isFinite(bounds.getHeight())
                && bounds.getWidth() > 0.0
                && bounds.getHeight() > 0.0;
    }

    private static void assertReasonableSnapshotSize(int width, int height) throws IOException {
        long rawBytes = Math.multiplyExact(Math.max(1, width), Math.max(1, height)) * 4L;
        if (rawBytes > MAX_RAW_SNAPSHOT_BYTES) {
            throw new IOException("La imagen PNG sería demasiado grande para exportar de forma segura: "
                    + width + " x " + height + " px; RAM bruta aproximada sin compresión: "
                    + formatMib(rawBytes) + ". Reduce zoom/contenido visible o exporta SVG.");
        }
    }

    private static String formatMib(long bytes) {
        return String.format(java.util.Locale.ROOT, "%.1f MiB", bytes / 1024.0 / 1024.0);
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

    private static int exportDimension(double value) {
        return (int) Math.ceil(Math.max(MIN_EXPORT_SIZE, value));
    }

    private static int scaledDimension(int logicalPixels, double exportScale) {
        return (int) Math.ceil(Math.max(MIN_EXPORT_SIZE, logicalPixels * exportScale));
    }

    private static double exportScale() {
        String configured = System.getProperty(PNG_SCALE_PROPERTY, "").strip();
        if (configured.isBlank()) {
            return DEFAULT_PNG_SCALE;
        }
        try {
            double parsed = Double.parseDouble(configured);
            if (!Double.isFinite(parsed)) {
                return DEFAULT_PNG_SCALE;
            }
            return Math.max(1.0, Math.min(MAX_PNG_SCALE, parsed));
        } catch (NumberFormatException ignored) {
            return DEFAULT_PNG_SCALE;
        }
    }

    private static Color exportBackgroundColor() {
        return Color.WHITE;
    }

    private static String normalizeStyleClass(String value) {
        if (value == null || value.isBlank()) {
            return DEFAULT_EXPORT_STYLE_CLASS;
        }
        return value.trim();
    }

    private static String shorten(String value, int maxCharacters) {
        String normalized = value == null ? "" : value.strip().replaceAll("\\s+", " ");
        if (normalized.length() <= maxCharacters) {
            return normalized;
        }
        return normalized.substring(0, Math.max(1, maxCharacters - 1)).stripTrailing() + "…";
    }

    private static void addStylesheet(Scene scene) {
        var css = InteractiveCanvasPngExporter.class.getResource(APP_LIGHT_CSS);
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
    }
}
