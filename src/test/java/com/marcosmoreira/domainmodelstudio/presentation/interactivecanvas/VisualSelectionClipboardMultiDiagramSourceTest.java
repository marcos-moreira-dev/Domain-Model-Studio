package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente para el portapapeles visual transversal de diagramas. */
class VisualSelectionClipboardMultiDiagramSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void projectClipboardServiceShouldCoverInteractiveVisualDiagramFamilies() throws IOException {
        String service = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/visualtransfer/ProjectVisualSelectionTransferService.java");
        List<String> supportedTypes = List.of(
                "CONCEPTUAL_MODEL",
                "ADMIN_MODULE_MAP",
                "SCREEN_FLOW",
                "ADMIN_WIREFRAMES",
                "UML_CLASS",
                "UML_USE_CASE",
                "UML_ACTIVITY",
                "UML_SEQUENCE",
                "UML_STATE",
                "BPMN_BASIC",
                "OPERATIONAL_FLOW",
                "C4_CONTEXT",
                "C4_CONTAINERS",
                "TECHNICAL_DEPLOYMENT"
        );
        for (String type : supportedTypes) {
            assertTrue(service.contains("DiagramTypeId." + type), "Debe soportar " + type);
        }
        assertFalse(service.contains("DiagramTypeId.DATA_DICTIONARY"), "El diccionario no es canvas visual transferible");
        assertFalse(service.contains("DiagramTypeId.LOGICAL_BUSINESS_INTAKE"), "El levantamiento lógico es documental");
    }

    @Test
    void visualAdaptersShouldImplementClipboardPort() throws IOException {
        List<String> adapters = List.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/ConceptualCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java"
        );
        for (String adapterPath : adapters) {
            String adapter = read(adapterPath);
            assertTrue(adapter.contains("CanvasSelectionClipboardPort"), adapterPath + " debe exponer puerto de portapapeles");
            assertTrue(adapter.contains("ProjectVisualSelectionTransferService.copySelectionToClipboard"), adapterPath + " debe copiar selección");
            assertTrue(adapter.contains("ProjectVisualSelectionTransferService.pasteSelectionFromClipboard"), adapterPath + " debe pegar selección");
        }
    }

    @Test
    void conceptualCanvasViewModelShouldApplyPasteAsUndoableStructuralEdit() throws IOException {
        String viewModel = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasViewModel.java");
        assertTrue(viewModel.contains("applyVisualSelectionPaste"));
        assertTrue(viewModel.contains("beginUndoableEdit()"));
        assertTrue(viewModel.contains("structuralEditListener.accept(project)"));
        assertTrue(viewModel.contains("selectionModel.clearSelection()"));
    }

    @Test
    void documentationShouldStateCoverageAndLimits() throws IOException {
        String doc = read("docs/desarrollo/TANDA_VIS_COPY_003_CLIPBOARD_MULTI_DIAGRAMA.md");
        assertTrue(doc.contains("Modelo conceptual"));
        assertTrue(doc.contains("C4 Contexto"));
        assertTrue(doc.contains("UML Clases"));
        assertTrue(doc.contains("Diccionario de datos"));
        assertTrue(doc.contains("Levantamiento lógico"));
        assertTrue(doc.contains("VIS-COPY-4"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(ROOT.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
