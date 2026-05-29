package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.deleteBendPointAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.layerOrderActions;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.sizeAdjustmentActions;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.presentation.capabilities.DiagramCapabilityPresentationPolicy;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfileResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Proveedor contextual de toolbar.
 *
 * <p>R1: la lista concreta de acciones ya no vive aquí; se delega a contributors por familia para evitar
 * que el shell visual tenga que crecer cada vez que se agrega un tipo de diagrama.</p>
 */
public final class DefaultDiagramToolbarActionProvider implements DiagramToolbarActionProvider {

    private final DiagramCapabilityPresentationPolicy capabilityPolicy;
    private final DiagramToolbarInteractionPolicy interactionPolicy;
    private final DiagramToolbarContributorRegistry contributorRegistry;

    public DefaultDiagramToolbarActionProvider() {
        this(new DiagramCapabilityPresentationPolicy(), new DiagramToolbarInteractionPolicy());
    }

    public DefaultDiagramToolbarActionProvider(DiagramCapabilityPresentationPolicy capabilityPolicy) {
        this(capabilityPolicy, new DiagramToolbarInteractionPolicy());
    }

    public DefaultDiagramToolbarActionProvider(
            DiagramCapabilityPresentationPolicy capabilityPolicy,
            DiagramToolbarInteractionPolicy interactionPolicy
    ) {
        this(capabilityPolicy, interactionPolicy, DiagramToolbarContributorRegistry.createDefault());
    }

    DefaultDiagramToolbarActionProvider(
            DiagramCapabilityPresentationPolicy capabilityPolicy,
            DiagramToolbarInteractionPolicy interactionPolicy,
            DiagramToolbarContributorRegistry contributorRegistry
    ) {
        this.capabilityPolicy = Objects.requireNonNull(capabilityPolicy, "capabilityPolicy");
        this.interactionPolicy = Objects.requireNonNull(interactionPolicy, "interactionPolicy");
        this.contributorRegistry = Objects.requireNonNull(contributorRegistry, "contributorRegistry");
    }

    @Override
    public List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        if (diagramTypeId == null) {
            return List.of();
        }
        return supportedActions(diagramTypeId, contributorRegistry.actionsFor(diagramTypeId));
    }

    private List<DiagramToolbarAction> supportedActions(DiagramTypeId diagramTypeId, List<DiagramToolbarAction> actions) {
        DiagramInteractionProfile profile = DiagramInteractionProfileResolver.resolve(diagramTypeId);
        List<DiagramToolbarAction> supported = new ArrayList<>(actions.stream()
                .filter(action -> capabilityPolicy.shouldExposeToolbarAction(diagramTypeId, action.id()))
                .filter(action -> interactionPolicy.shouldExpose(profile, action.id()))
                .toList());
        if (profile.supportsBendPoints()
                && supported.stream().noneMatch(action -> action.id() == DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT)
                && capabilityPolicy.shouldExposeToolbarAction(diagramTypeId, DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT)
                && interactionPolicy.shouldExpose(profile, DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT)) {
            supported.add(deleteBendPointAction());
        }
        if (sizeAdjustmentAvailable(diagramTypeId, profile)) {
            for (DiagramToolbarAction action : sizeAdjustmentActions()) {
                if (supported.stream().noneMatch(existing -> existing.id() == action.id())
                        && capabilityPolicy.shouldExposeToolbarAction(diagramTypeId, action.id())
                        && interactionPolicy.shouldExpose(profile, action.id())) {
                    supported.add(action);
                }
            }
        }
        if (layerOrderAvailable(diagramTypeId, profile)) {
            for (DiagramToolbarAction action : layerOrderActions()) {
                if (supported.stream().noneMatch(existing -> existing.id() == action.id())
                        && capabilityPolicy.shouldExposeToolbarAction(diagramTypeId, action.id())
                        && interactionPolicy.shouldExpose(profile, action.id())) {
                    supported.add(action);
                }
            }
        }
        return List.copyOf(supported);
    }

    private boolean sizeAdjustmentAvailable(DiagramTypeId diagramTypeId, DiagramInteractionProfile profile) {
        return !DiagramTypeId.CONCEPTUAL_MODEL.equals(diagramTypeId)
                && profile.supportsNodeDragging()
                && !profile.supportsDocumentEditing()
                && !profile.supportsMatrixEditing();
    }

    private boolean layerOrderAvailable(DiagramTypeId diagramTypeId, DiagramInteractionProfile profile) {
        return !DiagramTypeId.CONCEPTUAL_MODEL.equals(diagramTypeId)
                && profile.supportsNodeDragging()
                && !profile.supportsDocumentEditing()
                && !profile.supportsMatrixEditing();
    }
}
