package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.application.visual.DefaultVisualLayoutGenerator;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutSpecificationFactory;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;

/**
 * Prepara el layout inicial de un Grafo libre sin depender de JavaFX.
 *
 * <p>El caso de uso materializa posiciones y conectores en {@link com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts}.
 * No dibuja el canvas ni activa la UI; solo deja el proyecto listo para que una futura vista visual pueda renderizarlo.</p>
 */
public final class GenerateInitialFreeGraphLayoutUseCase {

    private final VisualLayoutService visualLayoutService;

    public GenerateInitialFreeGraphLayoutUseCase() {
        this(new VisualLayoutService(new VisualLayoutSpecificationFactory(), new DefaultVisualLayoutGenerator()));
    }

    public GenerateInitialFreeGraphLayoutUseCase(VisualLayoutService visualLayoutService) {
        this.visualLayoutService = Objects.requireNonNull(visualLayoutService, "visualLayoutService");
    }

    /** Asegura layout para nodos y relaciones faltantes, preservando posiciones existentes. */
    public DiagramProject ensureLayout(DiagramProject project) {
        ensureFreeGraphProject(project);
        return visualLayoutService.ensureVisualLayout(project);
    }

    /** Recalcula todo el layout visual del grafo, útil para una acción futura de autoorganizar. */
    public DiagramProject regenerateLayout(DiagramProject project) {
        ensureFreeGraphProject(project);
        return visualLayoutService.regenerateVisualLayout(project);
    }

    private static void ensureFreeGraphProject(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        if (!DiagramTypeId.FREE_GRAPH.equals(project.metadata().diagramTypeId())) {
            throw new IllegalArgumentException("El layout solicitado no corresponde a un Grafo libre.");
        }
        if (project.freeGraph().isEmpty()) {
            throw new IllegalArgumentException("El proyecto no contiene documento de Grafo libre.");
        }
    }
}
