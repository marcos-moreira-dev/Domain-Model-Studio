package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.application.export.SvgDiagramExporter;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import java.util.Objects;

/**
 * Exportador SVG vectorial para documentos visuales especializados.
 *
 * <p>Antes de escribir SVG asegura layout visual mediante la capa de aplicación y luego
 * construye un modelo SVG independiente de JavaFX. Así la exportación es una salida reproducible del proyecto, no una captura accidental de pantalla.</p>
 */
public final class SpecializedVisualSvgDiagramExporter implements SvgDiagramExporter {

    private final VisualLayoutService visualLayoutService;
    private final SpecializedSvgModelFactory modelFactory;
    private final SpecializedVisualSvgWriter writer;

    public SpecializedVisualSvgDiagramExporter() {
        this(new VisualLayoutService(), new SpecializedSvgModelFactory(), new SpecializedVisualSvgWriter());
    }

    public SpecializedVisualSvgDiagramExporter(
            VisualLayoutService visualLayoutService,
            SpecializedSvgModelFactory modelFactory,
            SpecializedVisualSvgWriter writer
    ) {
        this.visualLayoutService = Objects.requireNonNull(visualLayoutService, "visualLayoutService");
        this.modelFactory = Objects.requireNonNull(modelFactory, "modelFactory");
        this.writer = Objects.requireNonNull(writer, "writer");
    }

    @Override
    public String export(DiagramProject project) {
        DiagramProject prepared = visualLayoutService.ensureVisualLayout(Objects.requireNonNull(project, "project"));
        SpecializedSvgModel model = modelFactory.fromProject(prepared);
        DiagramLayout layout = prepared.layouts().activeLayout();
        return writer.write(model, layout);
    }
}
