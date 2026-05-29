package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de la tanda 13: toolbar y exportación activa deben compartir la misma promesa real. */
class Tanda13ExportPolicyCoherenceSourceTest {

    private static final Path EXPORT_POLICY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ProjectExportFormatPolicy.java");
    private static final Path CONTRIBUTOR_REGISTRY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ActiveOutputContributorRegistry.java");
    private static final Path EXPORT_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ExportCommandHandler.java");
    private static final Path CAPABILITY_PRESENTATION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/capabilities/DiagramCapabilityPresentationPolicy.java");

    @Test
    void exportPolicyMustCoverAllActiveWorkspaceFamilies() throws IOException {
        String source = read(EXPORT_POLICY);

        assertTrue(source.contains("formatsForConceptualModel"));
        assertTrue(source.contains("formatsForDataDictionary"));
        assertTrue(source.contains("formatsForModuleMap"));
        assertTrue(source.contains("formatsForUmlClass"));
        assertTrue(source.contains("formatsForRolesPermissions"));
        assertTrue(source.contains("formatsForScreenFlow"));
        assertTrue(source.contains("formatsForWireframe"));
        assertTrue(source.contains("formatsForFreeGraph"));
        assertTrue(source.contains("formatsForLogicalBusiness"));
        assertTrue(source.contains("formatsForBehavior"));
        assertTrue(source.contains("formatsForArchitecture"));
        assertTrue(source.contains("DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(typeId)"));
    }

    @Test
    void activeOutputRegistryMustHaveContributorForEveryExportableWorkspaceFamily() throws IOException {
        String source = read(CONTRIBUTOR_REGISTRY);

        assertTrue(source.contains("new ConceptualActiveOutputContributor(canvasViewModel)"));
        assertTrue(source.contains("new DataDictionaryActiveOutputContributor(dataDictionaryViewModel)"));
        assertTrue(source.contains("new ModuleMapActiveOutputContributor(moduleMapViewModel)"));
        assertTrue(source.contains("new UmlClassActiveOutputContributor(umlClassDiagramViewModel)"));
        assertTrue(source.contains("new RolesPermissionsActiveOutputContributor(rolesPermissionsViewModel)"));
        assertTrue(source.contains("new ScreenFlowActiveOutputContributor(screenFlowViewModel)"));
        assertTrue(source.contains("new WireframeActiveOutputContributor(wireframeViewModel)"));
        assertTrue(source.contains("new FreeGraphActiveOutputContributor(freeGraphViewModel)"));
        assertTrue(source.contains("new LogicalBusinessActiveOutputContributor(logicalBusinessViewModel)"));
        assertTrue(source.contains("new BehaviorActiveOutputContributor(behaviorDiagramViewModel)"));
        assertTrue(source.contains("new ArchitectureActiveOutputContributor(architectureDiagramViewModel)"));
    }

    @Test
    void exportCommandsMustResolveAndVerifyTheActiveOutputBeforeShowingFileChooser() throws IOException {
        String source = read(EXPORT_HANDLER);

        assertTrue(source.contains("Optional<ExportableOutput> activeOutput = activeOutputFor(ExportFormat.SVG);"));
        assertTrue(source.contains("Optional<ExportableOutput> activeOutput = activeOutputFor(ExportFormat.MARKDOWN);"));
        assertTrue(source.contains("Optional<ExportableOutput> activeOutput = activeOutputFor(ExportFormat.PNG);"));
        assertTrue(source.contains("Optional<ExportableOutput> activeOutput = activeOutputFor(ExportFormat.PDF);"));
        assertTrue(source.contains("context.activeOutputProvider().activeOutput()"));
        assertTrue(source.contains("!output.get().supports(format)"));
        assertTrue(source.contains("targetPathPolicy.ensurePngExtension"));
    }

    @Test
    void capabilityPresentationMustMapToolbarExportsToOfficialCapabilities() throws IOException {
        String source = read(CAPABILITY_PRESENTATION);

        assertTrue(source.contains("case EXPORT_SVG -> Optional.of(DiagramCapability.EXPORT_SVG);"));
        assertTrue(source.contains("case EXPORT_PNG -> Optional.of(DiagramCapability.EXPORT_PNG);"));
        assertTrue(source.contains("case EXPORT_MARKDOWN -> Optional.of(DiagramCapability.EXPORT_MARKDOWN);"));
        assertTrue(source.contains("case EXPORT_DICTIONARY_PDF -> Optional.of(DiagramCapability.EXPORT_PDF);"));
        assertTrue(source.contains("case SVG -> Optional.of(DiagramCapability.EXPORT_SVG);"));
        assertTrue(source.contains("case PNG -> Optional.of(DiagramCapability.EXPORT_PNG);"));
        assertTrue(source.contains("case PDF -> Optional.of(DiagramCapability.EXPORT_PDF);"));
        assertTrue(source.contains("case MARKDOWN -> Optional.of(DiagramCapability.EXPORT_MARKDOWN);"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
