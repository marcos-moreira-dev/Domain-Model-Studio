package com.marcosmoreira.domainmodelstudio.application.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíles para mantener {@code ApplicationServices} dividido por familias funcionales. */
class ApplicationServicesFamilyFacadeSourceTest {

    private static final Path APPLICATION_SERVICES = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/ApplicationServices.java");
    private static final Path SERVICES_DIR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/services");
    private static final Path SERVICES_FACTORY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/bootstrap/ApplicationServicesFactory.java");
    private static final Path IMPORT_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java");
    private static final Path MARKDOWN_FOLDER_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportCoordinator.java");
    private static final Path EXPORT_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ExportCommandHandler.java");
    private static final Path DIAGRAM_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/DiagramCommandHandler.java");

    private static final List<String> FAMILY_FILES = List.of(
            "ProjectApplicationServices.java",
            "ImportApplicationServices.java",
            "ExportApplicationServices.java",
            "CatalogApplicationServices.java",
            "VisualApplicationServices.java",
            "DocumentationApplicationServices.java",
            "ConceptualModelApplicationServices.java",
            "DataDictionaryApplicationServices.java",
            "ModuleMapApplicationServices.java",
            "UmlClassApplicationServices.java",
            "RolesPermissionsApplicationServices.java",
            "ScreenFlowApplicationServices.java",
            "WireframeApplicationServices.java",
            "BehaviorApplicationServices.java",
            "ArchitectureApplicationServices.java");

    @Test
    void familyFacadesShouldExistAndRemainApplicationOnly() throws Exception {
        for (String fileName : FAMILY_FILES) {
            Path file = SERVICES_DIR.resolve(fileName);
            String source = Files.readString(file);
            assertAll(fileName,
                    () -> assertTrue(Files.exists(file), "Debe existir la fachada funcional " + fileName),
                    () -> assertTrue(source.contains("public final class " + fileName.replace(".java", ""))),
                    () -> assertFalse(source.contains("javafx."),
                            "Las fachadas de application no deben depender de JavaFX: " + fileName),
                    () -> assertFalse(source.contains("presentation."),
                            "Las fachadas de application no deben depender de presentation: " + fileName),
                    () -> assertFalse(source.contains("infrastructure."),
                            "Las fachadas no deben importar adaptadores concretos de infrastructure: " + fileName));
        }
    }

    @Test
    void applicationServicesShouldExposeFamilyAccessorsDuringTransition() throws Exception {
        String source = Files.readString(APPLICATION_SERVICES);
        assertAll(
                () -> assertTrue(source.contains("private final ProjectApplicationServices projectServices")),
                () -> assertTrue(source.contains("private final ImportApplicationServices importServices")),
                () -> assertTrue(source.contains("private final ExportApplicationServices exportServices")),
                () -> assertTrue(source.contains("private final CatalogApplicationServices catalogServices")),
                () -> assertTrue(source.contains("private final VisualApplicationServices visualServices")),
                () -> assertTrue(source.contains("private final DocumentationApplicationServices documentationServices")),
                () -> assertTrue(source.contains("private final ConceptualModelApplicationServices conceptualModelServices")),
                () -> assertTrue(source.contains("private final DataDictionaryApplicationServices dataDictionaryServices")),
                () -> assertTrue(source.contains("private final UmlClassApplicationServices umlClassServices")),
                () -> assertTrue(source.contains("private final ArchitectureApplicationServices architectureServices")),
                () -> assertTrue(source.contains("public ApplicationServices(\n            ProjectApplicationServices projectServices")),
                () -> assertTrue(source.contains("public ProjectApplicationServices projectServices()")),
                () -> assertTrue(source.contains("public DataDictionaryApplicationServices dataDictionaryServices()")),
                () -> assertTrue(source.contains("@Deprecated(forRemoval = false)"),
                        "Los accesos legacy deben quedar marcados como transición, no como destino final."));
    }

    @Test
    void applicationServicesShouldDelegateLegacyGettersToFamilies() throws Exception {
        String source = Files.readString(APPLICATION_SERVICES);
        assertAll(
                () -> assertTrue(source.contains("return importServices.importMarkdownModelUseCase();")),
                () -> assertTrue(source.contains("return projectServices.saveProjectUseCase();")),
                () -> assertTrue(source.contains("return exportServices.exportMarkdownUseCase();")),
                () -> assertTrue(source.contains("return visualServices.moveElementUseCase();")),
                () -> assertTrue(source.contains("return conceptualModelServices.addEntityUseCase();")),
                () -> assertTrue(source.contains("return dataDictionaryServices.createDataDictionaryUseCase();")),
                () -> assertTrue(source.contains("return umlClassServices.createUmlClassDiagramUseCase();")),
                () -> assertFalse(source.contains("private final ImportMarkdownModelUseCase"),
                        "La fachada ya no debe almacenar casos de uso individuales."),
                () -> assertFalse(source.contains("private final CreateDataDictionaryUseCase"),
                        "Los documentos estructurados deben vivir en familias específicas."));
    }

    @Test
    void factoryShouldComposeApplicationServicesThroughFamilies() throws Exception {
        String source = Files.readString(SERVICES_FACTORY);
        assertAll(
                () -> assertTrue(source.contains("return new ApplicationServices(")),
                () -> assertTrue(source.contains("new ProjectApplicationServices(")),
                () -> assertTrue(source.contains("new ImportApplicationServices(")),
                () -> assertTrue(source.contains("new ExportApplicationServices(")),
                () -> assertTrue(source.contains("new DataDictionaryApplicationServices(")),
                () -> assertTrue(source.contains("new ArchitectureApplicationServices(")));
    }

    @Test
    void newShellCommandPathsShouldUseSpecificFamilies() throws Exception {
        String imports = Files.readString(IMPORT_HANDLER);
        String markdownFolder = Files.readString(MARKDOWN_FOLDER_COORDINATOR);
        String exports = Files.readString(EXPORT_HANDLER);
        String diagram = Files.readString(DIAGRAM_HANDLER);

        assertAll(
                () -> assertTrue(imports.contains("importServices().importMarkdownModelUseCase()")),
                () -> assertTrue(imports.contains("importServices().generateUmlClassDiagramFromSourceCodeUseCase()")),
                () -> assertTrue(imports.contains("documentationServices().exportAiResourcesUseCase()")),
                () -> assertTrue(imports.contains("visualServices().generateInitialChenLayoutUseCase()")),
                () -> assertTrue(markdownFolder.contains("importServices().markdownBatchImportUseCase()")),
                () -> assertTrue(exports.contains("exportServices().exportSvgUseCase()")),
                () -> assertTrue(exports.contains("exportServices().exportMarkdownUseCase()")),
                () -> assertTrue(exports.contains("exportServices().exportPdfUseCase()")),
                () -> assertTrue(diagram.contains("projectServices().validateProjectUseCase()")),
                () -> assertTrue(diagram.contains("visualServices().switchNotationUseCase()")));
    }
}
