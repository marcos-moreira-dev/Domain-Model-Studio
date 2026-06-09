package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl R3: MainShellCommandHandler conserva fachada pública y delega comandos por familia. */
class MainShellCommandFamiliesRefactorSourceTest {

    private static final Path SHELL_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell");
    private static final Path MAIN_SHELL = SHELL_PACKAGE.resolve("MainShellCommandHandler.java");

    @Test
    void mainShellMustDelegateSpecializedFamiliesToDedicatedCommandObjects() throws IOException {
        String shell = read("MainShellCommandHandler.java");

        assertTrue(shell.contains("private final ConceptualModelShellCommands conceptualModelCommands;"));
        assertTrue(shell.contains("private final DataDictionaryShellCommands dataDictionaryCommands;"));
        assertTrue(shell.contains("private final ModuleMapShellCommands moduleMapCommands;"));
        assertTrue(shell.contains("private final UmlClassShellCommands umlClassCommands;"));
        assertTrue(shell.contains("private final AdministrativeWorkspaceShellCommands administrativeWorkspaceCommands;"));
        assertTrue(shell.contains("private final BehaviorDiagramShellCommands behaviorDiagramCommands;"));
        assertTrue(shell.contains("private final ArchitectureDiagramShellCommands architectureDiagramCommands;"));
        assertTrue(shell.contains("private final FreeGraphShellCommands freeGraphCommands;"));

        assertTrue(shell.contains("umlClassCommands.requestAddClass()"));
        assertTrue(shell.contains("administrativeWorkspaceCommands.requestAddWireframeButton()"));
        assertTrue(shell.contains("behaviorDiagramCommands.requestAddSequenceMessage()"));
        assertTrue(shell.contains("architectureDiagramCommands.requestAddC4Container()"));
        assertTrue(shell.contains("freeGraphCommands.requestAddNode()"));
    }

    @Test
    void mainShellMustNoLongerOwnConcreteSpecializedViewModelCommands() throws IOException {
        String shell = read("MainShellCommandHandler.java");

        assertFalse(shell.contains("dataDictionaryViewModel.addEntity()"));
        assertFalse(shell.contains("moduleMapViewModel.addModule()"));
        assertFalse(shell.contains("umlClassDiagramViewModel.addClass("));
        assertFalse(shell.contains("rolesPermissionsViewModel.addRole()"));
        assertFalse(shell.contains("screenFlowViewModel.addScreen()"));
        assertFalse(shell.contains("wireframeViewModel.addComponent("));
        assertFalse(shell.contains("behaviorDiagramViewModel.addNode("));
        assertFalse(shell.contains("architectureDiagramViewModel.addNode("));
        assertFalse(shell.contains("freeGraphViewModel.addNode()"));
    }

    @Test
    void familyCommandClassesMustOwnTheirConcreteCommands() throws IOException {
        assertTrue(read("ConceptualModelShellCommands.java").contains("beginAddEntityTool"));
        assertTrue(read("DataDictionaryShellCommands.java").contains("viewModel.addEntity()"));
        assertTrue(read("ModuleMapShellCommands.java").contains("viewModel.addModule()"));
        assertTrue(read("UmlClassShellCommands.java").contains("viewModel.addClass(kind)"));
        assertTrue(read("AdministrativeWorkspaceShellCommands.java").contains("wireframeViewModel.addComponent(kind)"));
        assertTrue(read("BehaviorDiagramShellCommands.java").contains("viewModel.addNode(kind)"));
        assertTrue(read("ArchitectureDiagramShellCommands.java").contains("viewModel.addEdge(kind)"));
        assertTrue(read("FreeGraphShellCommands.java").contains("viewModel.addNode()"));
    }

    @Test
    void mainShellShouldKeepShrinkingAfterCommandFamilyRefactor() throws IOException {
        long lines = Files.lines(MAIN_SHELL).count();
        assertTrue(lines < 1100, "MainShellCommandHandler debe bajar de 1100 líneas tras R3; líneas actuales: " + lines);
    }

    private String read(String fileName) throws IOException {
        return Files.readString(SHELL_PACKAGE.resolve(fileName), StandardCharsets.UTF_8);
    }
}
