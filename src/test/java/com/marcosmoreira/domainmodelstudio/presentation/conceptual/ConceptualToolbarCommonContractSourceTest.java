package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 6: toolbar contextual conceptual y contratos transversales. */
class ConceptualToolbarCommonContractSourceTest {

    private static final Path TOOLBAR_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar");
    private static final Path CONCEPTUAL_CONTRACT = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/toolbar/ConceptualToolbarContract.java");
    private static final Path EXECUTOR = TOOLBAR_PACKAGE.resolve("DiagramToolbarActionExecutor.java");
    private static final Path ACTIVE_OUTPUT_REGISTRY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ActiveOutputContributorRegistry.java");

    @Test
    void conceptualModelHasDedicatedToolbarContributor() throws IOException {
        String registry = Files.readString(TOOLBAR_PACKAGE.resolve("DiagramToolbarContributorRegistry.java"), StandardCharsets.UTF_8);
        String conceptual = Files.readString(TOOLBAR_PACKAGE.resolve("ConceptualToolbarContributor.java"), StandardCharsets.UTF_8);
        String documents = Files.readString(TOOLBAR_PACKAGE.resolve("ConceptualDocumentToolbarContributor.java"), StandardCharsets.UTF_8);

        assertTrue(registry.contains("new ConceptualToolbarContributor()"));
        assertTrue(registry.contains("new ConceptualDocumentToolbarContributor()"));
        assertTrue(conceptual.contains("ConceptualToolbarContract"));
        assertTrue(conceptual.contains("DiagramTypeId.CONCEPTUAL_MODEL.equals(diagramTypeId)"));
        assertFalse(documents.contains("DiagramTypeId.CONCEPTUAL_MODEL.equals"),
                "El modelo conceptual ya no debe compartir contributor con documentos estructurados.");
    }

    @Test
    void conceptualContractListsTheToolbarSurfaceWithoutMigratingCanvas() throws IOException {
        String contract = Files.readString(CONCEPTUAL_CONTRACT, StandardCharsets.UTF_8);
        String conceptual = Files.readString(TOOLBAR_PACKAGE.resolve("ConceptualToolbarContributor.java"), StandardCharsets.UTF_8);

        assertTrue(contract.contains("ADD_ENTITY"));
        assertTrue(contract.contains("ADD_ATTRIBUTE"));
        assertTrue(contract.contains("ADD_RELATIONSHIP"));
        assertTrue(contract.contains("DELETE_ELEMENT"));
        assertTrue(contract.contains("VALIDATE_MODEL"));
        assertTrue(contract.contains("SWITCH_TO_CHEN"));
        assertTrue(contract.contains("SWITCH_TO_CROWS_FOOT"));
        assertTrue(contract.contains("EXPORT_SVG"));
        assertTrue(contract.contains("EXPORT_MARKDOWN"));
        assertTrue(contract.contains("EXPORT_PNG"));
        assertTrue(conceptual.contains("ConceptualToolbarContract.actions()"));
        assertFalse(contract.contains("InteractiveCanvasSurfaceView"));
        assertFalse(conceptual.contains("InteractiveCanvasSurfaceView"));
    }

    @Test
    void everyVisibleConceptualToolbarActionHasExecutorRoute() throws IOException {
        String executor = Files.readString(EXECUTOR, StandardCharsets.UTF_8);

        assertTrue(executor.contains("case ADD_ENTITY -> commandHandler.requestAddEntityTool()"));
        assertTrue(executor.contains("case ADD_ATTRIBUTE -> commandHandler.requestAddAttributeToSelectedEntity()"));
        assertTrue(executor.contains("case ADD_RELATIONSHIP -> commandHandler.requestAddRelationshipTool()"));
        assertTrue(executor.contains("case DUPLICATE_ELEMENT -> commandHandler.requestDuplicateSelectedEntity()"));
        assertTrue(executor.contains("case DELETE_ELEMENT -> commandHandler.requestRemoveSelectedElement()"));
        assertTrue(executor.contains("case VALIDATE_MODEL -> commandHandler.requestValidateProject()"));
        assertTrue(executor.contains("case REORGANIZE_DIAGRAM -> commandHandler.requestRegenerateLayout()"));
        assertTrue(executor.contains("case SWITCH_TO_CHEN -> commandHandler.requestSwitchNotation(NotationType.CHEN)"));
        assertTrue(executor.contains("case SWITCH_TO_CROWS_FOOT -> commandHandler.requestSwitchNotation(NotationType.CROWS_FOOT)"));
        assertTrue(executor.contains("case EXPORT_SVG -> commandHandler.requestExportSvg()"));
        assertTrue(executor.contains("case EXPORT_MARKDOWN -> commandHandler.requestExportMarkdown()"));
        assertTrue(executor.contains("case EXPORT_PNG -> commandHandler.requestExportPng()"));
    }

    @Test
    void conceptualExportContinuesThroughActiveOutputContract() throws IOException {
        String registry = Files.readString(ACTIVE_OUTPUT_REGISTRY, StandardCharsets.UTF_8);
        String contributor = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ConceptualActiveOutputContributor.java"),
                StandardCharsets.UTF_8);

        assertTrue(registry.contains("new ConceptualActiveOutputContributor(canvasViewModel)"));
        assertTrue(contributor.contains("DiagramTypeId.CONCEPTUAL_MODEL.equals"));
        assertTrue(contributor.contains("exportFormatPolicy.formatsForConceptualModel"));
        assertTrue(contributor.contains("canvasViewModel::exportVisibleCanvasAsPng"));
    }
}
