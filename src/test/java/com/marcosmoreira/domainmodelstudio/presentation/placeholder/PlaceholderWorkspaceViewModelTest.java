package com.marcosmoreira.domainmodelstudio.presentation.placeholder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class PlaceholderWorkspaceViewModelTest {

    @Test
    void placeholderActionsFollowDescriptorCapabilities() {
        var descriptor = new DefaultDiagramTypeRegistry()
                .findById(DiagramTypeId.ADMIN_MODULE_MAP)
                .orElseThrow();

        PlaceholderWorkspaceViewModel viewModel = PlaceholderWorkspaceViewModel.from(
                descriptor,
                "Modelado de datos");

        assertTrue(viewModel.allowedActions().contains(PlaceholderAction.SHOW_THEORY));
        assertTrue(viewModel.allowedActions().contains(PlaceholderAction.EXPORT_AI_RESOURCES));
        assertTrue(viewModel.allowedActions().contains(PlaceholderAction.BACK_TO_NEW_PROJECT));
    }
}
