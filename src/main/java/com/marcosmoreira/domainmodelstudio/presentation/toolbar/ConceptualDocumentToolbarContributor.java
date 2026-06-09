package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.action;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Acciones de toolbar para documentos estructurados que no usan canvas visual. */
final class ConceptualDocumentToolbarContributor implements DiagramToolbarContributor {

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.DATA_DICTIONARY.equals(diagramTypeId);
    }

    @Override
    public List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        if (DiagramTypeId.DATA_DICTIONARY.equals(diagramTypeId)) {
            return dataDictionaryActions();
        }
        return List.of();
    }

    private List<DiagramToolbarAction> dataDictionaryActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_DICTIONARY_ENTITY, "Entidad",
                        "Agregar entidad al diccionario de datos",
                        ToolbarIcon.ADD_ENTITY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_DICTIONARY_FIELD, "Campo",
                        "Agregar campo a la entidad seleccionada",
                        ToolbarIcon.ADD_ATTRIBUTE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.REMOVE_DICTIONARY_ITEM, "Eliminar",
                        "Eliminar campo seleccionado o entidad seleccionada",
                        ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_DICTIONARY, "Validar",
                        "Revisar consistencia del diccionario de datos",
                        ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.EXPORT_PDF, "PDF",
                        "Exportar diccionario de datos como PDF",
                        ToolbarIcon.EXPORT_PDF, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown",
                        "Exportar diccionario de datos como Markdown",
                        ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE)
        );
    }
}
