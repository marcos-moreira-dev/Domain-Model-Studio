package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 5: SideDock conceptual común completo. */
class ConceptualSideDockCommonModulesSourceTest {

    private static final Path CONTRIBUTOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/ConceptualWorkbenchContributor.java");
    private static final Path MODULES = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/sidedock/ConceptualSideDockModules.java");
    private static final Path VALIDATION_PANEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/sidedock/ConceptualValidationPanel.java");
    private static final Path APPEARANCE_PANEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/sidedock/ConceptualAppearancePanel.java");
    private static final Path HELP_PANEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/sidedock/ConceptualOperationalHelpPanel.java");

    @Test
    void conceptualContributorRegistersAdditionalSideDockModules() throws IOException {
        String contributor = Files.readString(CONTRIBUTOR, StandardCharsets.UTF_8);

        assertTrue(contributor.contains("ConceptualSideDockModules.create"));
        assertTrue(contributor.contains("additionalSideDockModules"));
        assertTrue(contributor.contains("List<SideDockModule>"));
        assertFalse(contributor.contains("InteractiveCanvasSurfaceView"),
                "Tanda 5 completa el SideDock; no debe migrar todavía el render conceptual al canvas transversal.");
    }

    @Test
    void conceptualSideDockExposesValidationAppearanceAndHelp() throws IOException {
        String modules = Files.readString(MODULES, StandardCharsets.UTF_8);

        assertTrue(modules.contains("SideDockModuleId.VALIDATION"));
        assertTrue(modules.contains("SideDockModuleId.APPEARANCE"));
        assertTrue(modules.contains("SideDockModuleId.HELP"));
        assertTrue(modules.contains("ConceptualValidationPanel"));
        assertTrue(modules.contains("ConceptualAppearancePanel"));
        assertTrue(modules.contains("ConceptualOperationalHelpPanel"));
    }

    @Test
    void validationPanelUsesDomainValidatorWithoutMutatingProject() throws IOException {
        String validation = Files.readString(VALIDATION_PANEL, StandardCharsets.UTF_8);

        assertTrue(validation.contains("bridge.validation().validateActiveProject()"));
        assertFalse(validation.contains("new DiagramModelValidator"),
                "La validación debe pasar por el bridge híbrido y no crear rutas paralelas en el panel.");
        assertFalse(validation.contains("validator.validate(project.model())"),
                "El panel no debe volver a validar directo ni duplicar el bridge.");
        assertFalse(validation.contains("withModel("));
        assertFalse(validation.contains("withMetadata("));
    }

    @Test
    void appearancePanelDelegatesNotationSwitchToExistingCommand() throws IOException {
        String appearance = Files.readString(APPEARANCE_PANEL, StandardCharsets.UTF_8);

        assertTrue(appearance.contains("Consumer<NotationType>"));
        assertTrue(appearance.contains("notationSwitchAction.accept(notation)"));
        assertTrue(appearance.contains("NotationType.CHEN"));
        assertTrue(appearance.contains("NotationType.CROWS_FOOT"));
    }

    @Test
    void operationalHelpIsToolHelpNotAcademicTheory() throws IOException {
        String help = Files.readString(HELP_PANEL, StandardCharsets.UTF_8);

        assertTrue(help.contains("Ayuda operativa"));
        assertTrue(help.contains("Guía académica"));
        assertTrue(help.contains("SideDock"));
        assertFalse(help.contains("DefaultTheoryCatalog"));
        assertFalse(help.contains("ManualDialog"));
    }
}
