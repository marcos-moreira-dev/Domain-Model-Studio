package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

/** Construye acciones de toolbar reutilizadas por los proveedores contextuales. */
final class DiagramToolbarActionFactory {

    private DiagramToolbarActionFactory() {
    }

    static DiagramToolbarAction fitToContentAction() {
        return action(DiagramToolbarActionId.FIT_TO_CONTENT, "Ajustar",
                "Ajusta zoom y encuadre para que el contenido del diagrama quepa en la vista.",
                ToolbarIcon.FIT_VIEW, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.NORMAL);
    }

    static DiagramToolbarAction centerDiagramAction() {
        return action(DiagramToolbarActionId.CENTER_DIAGRAM, "Centrar",
                "Centra el viewport sobre el contenido del diagrama sin cambiar el zoom actual.",
                ToolbarIcon.CENTER_SELECTION, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.NORMAL);
    }

    static DiagramToolbarAction reorganizeAction(String tooltip) {
        return action(DiagramToolbarActionId.REORGANIZE_DIAGRAM, "Autoorganizar", tooltip,
                ToolbarIcon.REORGANIZE_DIAGRAM, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE);
    }


    static java.util.List<DiagramToolbarAction> sizeAdjustmentActions() {
        return java.util.List.of(
                action(DiagramToolbarActionId.GROW_SELECTED_FIGURE, "Agrandar figura",
                        "Aumenta la figura seleccionada para que el texto tenga más espacio visual.",
                        ToolbarIcon.ZOOM_IN, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.SHRINK_SELECTED_FIGURE, "Achicar figura",
                        "Reduce la figura seleccionada con límite de legibilidad para el texto.",
                        ToolbarIcon.ZOOM_OUT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE)
        );
    }

    static java.util.List<DiagramToolbarAction> layerOrderActions() {
        return java.util.List.of(
                action(DiagramToolbarActionId.BRING_SELECTION_TO_FRONT, "Frente",
                        "Trae el nodo, tarjeta o rectángulo seleccionado al frente de su capa visual.",
                        ToolbarIcon.BRING_TO_FRONT, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.SEND_SELECTION_TO_BACK, "Fondo",
                        "Envía el nodo, tarjeta o rectángulo seleccionado al fondo de su capa visual.",
                        ToolbarIcon.SEND_TO_BACK, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.RAISE_SELECTION_LAYER, "Subir",
                        "Sube una capa el nodo, tarjeta o rectángulo seleccionado.",
                        ToolbarIcon.RAISE_LAYER, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.LOWER_SELECTION_LAYER, "Bajar",
                        "Baja una capa el nodo, tarjeta o rectángulo seleccionado.",
                        ToolbarIcon.LOWER_LAYER, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.SMALL)
        );
    }


    static DiagramToolbarAction transferSelectionAction() {
        return action(DiagramToolbarActionId.TRANSFER_VISUAL_SELECTION, "Transferir",
                "Copia la selección visual a otro proyecto abierto compatible del mismo tipo.",
                ToolbarIcon.DUPLICATE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE);
    }

    static DiagramToolbarAction deleteBendPointAction() {
        return action(DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT, "Quitar punto",
                "Quita el punto intermedio seleccionado de una relación editable.",
                ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE);
    }

    static DiagramToolbarAction action(
            DiagramToolbarActionId id,
            String text,
            String tooltip,
            ToolbarIcon icon,
            DiagramToolbarSection section,
            DiagramToolbarAction.Width width
    ) {
        return new DiagramToolbarAction(
                id,
                text,
                DiagramToolbarTooltipCatalog.describe(id, text, tooltip, section),
                icon,
                section,
                width);
    }
}
