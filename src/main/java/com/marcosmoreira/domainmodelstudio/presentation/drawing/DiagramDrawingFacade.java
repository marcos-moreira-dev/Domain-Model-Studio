package com.marcosmoreira.domainmodelstudio.presentation.drawing;

/**
 * Fachada ligera de dibujo para render kits de diagramas.
 *
 * <p>Centraliza el acceso a primitivas, fábricas y calculadores visuales sin
 * conocer familias concretas de diagramas. Cada render kit decide qué piezas
 * pedir según su teoría visual.</p>
 */
public record DiagramDrawingFacade(
        DiagramDrawingPrimitives primitives,
        DiagramTextFactory text,
        DiagramNodeFactory nodes,
        DiagramConnectorFactory connectors,
        DiagramArrowFactory arrows,
        DiagramSelectionDecorationFactory selection,
        DiagramBoundsCalculator bounds
) {

    public DiagramDrawingFacade {
        primitives = primitives == null ? new DiagramDrawingPrimitives() : primitives;
        text = text == null ? new DiagramTextFactory() : text;
        selection = selection == null ? new DiagramSelectionDecorationFactory() : selection;
        arrows = arrows == null ? new DiagramArrowFactory() : arrows;
        connectors = connectors == null ? new DiagramConnectorFactory(arrows, text) : connectors;
        nodes = nodes == null ? new DiagramNodeFactory(primitives, text, selection) : nodes;
        bounds = bounds == null ? new DiagramBoundsCalculator() : bounds;
    }

    public static DiagramDrawingFacade defaults() {
        DiagramDrawingPrimitives primitives = new DiagramDrawingPrimitives();
        DiagramTextFactory text = new DiagramTextFactory();
        DiagramSelectionDecorationFactory selection = new DiagramSelectionDecorationFactory();
        DiagramArrowFactory arrows = new DiagramArrowFactory();
        return new DiagramDrawingFacade(
                primitives,
                text,
                new DiagramNodeFactory(primitives, text, selection),
                new DiagramConnectorFactory(arrows, text),
                arrows,
                selection,
                new DiagramBoundsCalculator()
        );
    }
}
