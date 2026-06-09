package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.editing.AddAttributeUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddRelationshipUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.DuplicateEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeConnectorAnchorsUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveConnectorLabelUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveDiagramElementUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.selection.DiagramSelectionModel;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Contrato mínimo del flujo PNG desde presentación.
 *
 * <p>La generación real del PNG depende del snapshot JavaFX del canvas, por eso el
 * test no intenta abrir una ventana. Aquí se valida que el ViewModel obligue a tener
 * proyecto cargado y un exportador registrado, y que delegue correctamente en la acción
 * registrada por la vista.</p>
 */
class DiagramCanvasViewModelPngExportContractTest {

    @Test
    void pngExportRequiresLoadedProject() throws Exception {
        DiagramCanvasViewModel viewModel = newViewModel();
        Path target = Files.createTempFile("dms-no-project-", ".png");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> viewModel.exportVisibleCanvasAsPng(target)
        );
        assertTrue(exception.getMessage().contains("No hay proyecto"));
    }

    @Test
    void pngExportRequiresRegisteredCanvasAction() throws Exception {
        DiagramCanvasViewModel viewModel = newViewModel();
        viewModel.showImportedProject(DiagramProject.blank("png_contract", "PNG Contract"));
        Path target = Files.createTempFile("dms-no-action-", ".png");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> viewModel.exportVisibleCanvasAsPng(target)
        );
        assertTrue(exception.getMessage().contains("exportador PNG"));
    }

    @Test
    void pngExportDelegatesToRegisteredCanvasAction() throws Exception {
        DiagramCanvasViewModel viewModel = newViewModel();
        byte[] fakePngHeader = new byte[] {(byte) 0x89, 'P', 'N', 'G'};
        Path target = Files.createTempFile("dms-png-contract-", ".png");

        viewModel.showImportedProject(DiagramProject.blank("png_contract", "PNG Contract"));
        viewModel.registerPngExportAction(file -> Files.write(file, fakePngHeader));
        viewModel.exportVisibleCanvasAsPng(target);

        assertArrayEquals(fakePngHeader, Files.readAllBytes(target));
    }

    private static DiagramCanvasViewModel newViewModel() {
        return new DiagramCanvasViewModel(
                new DiagramSelectionModel(),
                new AddEntityUseCase(),
                new AddAttributeUseCase(),
                new AddRelationshipUseCase(),
                new DuplicateEntityUseCase(),
                new RemoveDiagramElementUseCase(),
                new MoveElementUseCase(),
                new AddBendPointUseCase(),
                new MoveBendPointUseCase(),
                new MoveConnectorLabelUseCase(),
                new RemoveBendPointUseCase(),
                new ChangeConnectorAnchorsUseCase(),
                ignored -> { }
        );
    }
}
