package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassRenderFailureRecoverySourceTest {

    @Test
    void umlCanvasAdapterShouldDelegateRenderFailuresToViewModel() throws Exception {
        String adapterSource = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java"));

        assertTrue(adapterSource.contains("CanvasRenderFailurePort"));
        assertTrue(adapterSource.contains("handleCanvasRenderFailure(CanvasRenderFailureReport report)"));
        assertTrue(adapterSource.contains("viewModel.handleCanvasRenderFailure(report)"));
    }

    @Test
    void viewModelShouldSwitchToSummaryAfterRenderFailure() throws Exception {
        String viewModelSource = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java"));

        assertTrue(viewModelSource.contains("handleCanvasRenderFailure(CanvasRenderFailureReport report)"));
        assertTrue(viewModelSource.contains("switchToSummaryAfterFailure"));
        assertTrue(viewModelSource.contains("UmlClassDiagramViewKind.SUMMARY"));
        assertTrue(viewModelSource.contains("largeFailureAdvisor.renderFailureMessage"));
    }
}
