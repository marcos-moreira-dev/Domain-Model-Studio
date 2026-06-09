package com.marcosmoreira.domainmodelstudio.presentation.capabilities;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramCapabilityCatalog;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramCapabilityCatalog;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionId;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Política de presentación que traduce capacidades de producto en acciones visibles.
 *
 * <p>Evita que menús y barras contextuales mantengan listas hardcodeadas distintas.
 * Si el catálogo oficial deja de prometer una capacidad, la interfaz deja de ofrecer
 * la acción asociada desde un único punto trazable.</p>
 */
public final class DiagramCapabilityPresentationPolicy {

    private static final Set<DiagramToolbarActionId> CONCEPTUAL_CANVAS_ONLY_ACTIONS = EnumSet.of(
            DiagramToolbarActionId.SWITCH_TO_CHEN,
            DiagramToolbarActionId.SWITCH_TO_CROWS_FOOT,
            DiagramToolbarActionId.ZOOM_IN,
            DiagramToolbarActionId.ZOOM_OUT,
            DiagramToolbarActionId.RESET_ZOOM,
            DiagramToolbarActionId.CENTER_SELECTION);

    private final DiagramCapabilityCatalog capabilityCatalog;

    public DiagramCapabilityPresentationPolicy() {
        this(new DefaultDiagramCapabilityCatalog());
    }

    public DiagramCapabilityPresentationPolicy(DiagramCapabilityCatalog capabilityCatalog) {
        this.capabilityCatalog = Objects.requireNonNull(capabilityCatalog, "capabilityCatalog");
    }

    public boolean supports(DiagramTypeId diagramTypeId, DiagramCapability capability) {
        if (diagramTypeId == null || capability == null) {
            return false;
        }
        return capabilityCatalog.supports(diagramTypeId, capability);
    }

    public boolean supportsExportFormat(DiagramTypeId diagramTypeId, ExportFormat format) {
        return exportCapability(format)
                .map(capability -> supports(diagramTypeId, capability))
                .orElse(false);
    }

    public boolean shouldExposeToolbarAction(DiagramTypeId diagramTypeId, DiagramToolbarActionId actionId) {
        if (diagramTypeId == null || actionId == null) {
            return false;
        }
        if (CONCEPTUAL_CANVAS_ONLY_ACTIONS.contains(actionId)) {
            return DiagramTypeId.CONCEPTUAL_MODEL.equals(diagramTypeId);
        }
        Optional<DiagramCapability> requiredExport = exportCapability(actionId);
        if (requiredExport.isPresent()) {
            return supports(diagramTypeId, requiredExport.get());
        }
        Optional<DiagramCapability> requiredCapability = genericActionCapability(actionId);
        return requiredCapability.map(capability -> supports(diagramTypeId, capability)).orElse(true);
    }

    private Optional<DiagramCapability> genericActionCapability(DiagramToolbarActionId actionId) {
        if (actionId == DiagramToolbarActionId.IMPORT_UML_FROM_SOURCE) {
            return Optional.of(DiagramCapability.IMPORT_SOURCE_CODE);
        }
        if (actionId == DiagramToolbarActionId.OPEN_UML_SOURCE) {
            return Optional.of(DiagramCapability.OPEN_SOURCE_CODE);
        }
        if (actionId.name().startsWith("ADD_")
                || actionId.name().startsWith("REMOVE_")
                || actionId == DiagramToolbarActionId.DELETE_ELEMENT
                || actionId == DiagramToolbarActionId.DUPLICATE_ELEMENT
                || actionId == DiagramToolbarActionId.BRING_SELECTION_TO_FRONT
                || actionId == DiagramToolbarActionId.SEND_SELECTION_TO_BACK
                || actionId == DiagramToolbarActionId.RAISE_SELECTION_LAYER
                || actionId == DiagramToolbarActionId.LOWER_SELECTION_LAYER
                || actionId == DiagramToolbarActionId.GROW_SELECTED_FIGURE
                || actionId == DiagramToolbarActionId.SHRINK_SELECTED_FIGURE) {
            return Optional.of(DiagramCapability.MANUAL_EDITING);
        }
        if (actionId.name().startsWith("VALIDATE_")) {
            return Optional.of(DiagramCapability.MANUAL_EDITING);
        }
        return Optional.empty();
    }

    private Optional<DiagramCapability> exportCapability(DiagramToolbarActionId actionId) {
        return switch (actionId) {
            case EXPORT_SVG -> Optional.of(DiagramCapability.EXPORT_SVG);
            case EXPORT_PNG -> Optional.of(DiagramCapability.EXPORT_PNG);
            case EXPORT_MARKDOWN -> Optional.of(DiagramCapability.EXPORT_MARKDOWN);
            case EXPORT_PDF -> Optional.of(DiagramCapability.EXPORT_PDF);
            case EXPORT_DICTIONARY_PDF -> Optional.of(DiagramCapability.EXPORT_PDF);
            default -> Optional.empty();
        };
    }

    private Optional<DiagramCapability> exportCapability(ExportFormat format) {
        if (format == null) {
            return Optional.empty();
        }
        return switch (format) {
            case SVG -> Optional.of(DiagramCapability.EXPORT_SVG);
            case PNG -> Optional.of(DiagramCapability.EXPORT_PNG);
            case PDF -> Optional.of(DiagramCapability.EXPORT_PDF);
            case MARKDOWN -> Optional.of(DiagramCapability.EXPORT_MARKDOWN);
        };
    }
}
