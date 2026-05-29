package com.marcosmoreira.domainmodelstudio.bootstrap;

import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;
import com.marcosmoreira.domainmodelstudio.application.services.*;
import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramCategoryCatalog;
import com.marcosmoreira.domainmodelstudio.application.behavior.*;
import com.marcosmoreira.domainmodelstudio.application.architecture.*;
import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramCategoryCatalog;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.catalog.ListDiagramCategoriesUseCase;
import com.marcosmoreira.domainmodelstudio.application.catalog.ListDiagramTypesUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.DefaultCreateDataDictionaryUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.ValidateDataDictionaryUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.RemoveDataDictionaryItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.UpdateDataDictionaryFieldUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.UpdateDataDictionaryEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.AddDataDictionaryFieldUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.AddDataDictionaryEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddAttributeUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddRelationshipUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.DuplicateEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveDiagramElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeConnectorAnchorsUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeConnectorMarkerOrientationUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeDiagramAppearanceUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeElementStyleUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveConnectorLabelUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RenameElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.UpdateNodeLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.UpdateRelationshipCardinalityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.UpdateElementDescriptionUseCase;
import com.marcosmoreira.domainmodelstudio.application.export.ExportSvgUseCase;
import com.marcosmoreira.domainmodelstudio.application.export.ExportMarkdownUseCase;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelUseCase;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialCrowsFootLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.AddModuleMapDependencyUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.AddModuleMapModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.CreateModuleMapUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.RemoveModuleMapItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.UpdateModuleMapDependencyUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.UpdateModuleMapModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.ValidateModuleMapUseCase;
import com.marcosmoreira.domainmodelstudio.application.notation.SwitchNotationUseCase;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProjectNormalizer;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.PreviewSourceCodeImportUseCase;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeParserRegistry;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeProjectParserUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.CreateUmlClassDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.GenerateUmlClassDiagramFromSourceCodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.SourceCodeToUmlClassDiagramMapper;
import com.marcosmoreira.domainmodelstudio.application.umlclass.RemoveUmlClassDiagramItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.ValidateUmlClassDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.*;
import com.marcosmoreira.domainmodelstudio.application.screenflow.*;
import com.marcosmoreira.domainmodelstudio.application.wireframe.*;
import com.marcosmoreira.domainmodelstudio.application.project.OpenProjectUseCase;
import com.marcosmoreira.domainmodelstudio.application.project.SaveProjectUseCase;
import com.marcosmoreira.domainmodelstudio.application.validation.ValidateProjectUseCase;
import com.marcosmoreira.domainmodelstudio.application.workspace.DefaultCreateWorkspaceUseCase;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.FileSystemSourceDirectoryScanner;
import com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.java.JavaSourceCodeParserAdapter;
import com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript.TypeScriptSourceCodeParserAdapter;
import java.util.List;
import java.util.Objects;

/**
 * Factory de casos de uso de la capa application.
 *
 * <p>El bootstrap conoce la infraestructura concreta y compone las familias de
 * {@code ApplicationServices}. La capa de presentación recibe la fachada ya armada;
 * no debe instanciar repositorios, parsers ni servicios de infraestructura por su cuenta.</p>
 */

public final class ApplicationServicesFactory {

    private final InfrastructureServices infrastructureServices;

    public ApplicationServicesFactory(InfrastructureServices infrastructureServices) {
        this.infrastructureServices = Objects.requireNonNull(infrastructureServices, "infrastructureServices");
    }

    public ApplicationServices create() {
        DiagramProjectValidator validator = new DiagramProjectValidator();
        GenerateInitialChenLayoutUseCase chenLayoutUseCase = new GenerateInitialChenLayoutUseCase();
        GenerateInitialCrowsFootLayoutUseCase crowsFootLayoutUseCase = new GenerateInitialCrowsFootLayoutUseCase();
        DiagramCategoryCatalog categoryCatalog = new DefaultDiagramCategoryCatalog();
        DiagramTypeRegistry diagramTypeRegistry = new DefaultDiagramTypeRegistry();
        FileSystemSourceDirectoryScanner sourceDirectoryScanner = new FileSystemSourceDirectoryScanner();
        GenerateUmlClassDiagramFromSourceCodeUseCase sourceCodeUmlImportUseCase =
                createSourceCodeUmlImportUseCase(sourceDirectoryScanner);
        PreviewSourceCodeImportUseCase sourceCodeImportPreviewUseCase =
                new PreviewSourceCodeImportUseCase(sourceDirectoryScanner);
        ImportMarkdownModelUseCase importMarkdownModelUseCase = new ImportMarkdownModelUseCase(
                infrastructureServices.markdownModelParser(), validator);
        MarkdownBatchImportUseCase markdownBatchImportUseCase = new MarkdownBatchImportUseCase(
                infrastructureServices.markdownBatchCandidateReader(),
                importMarkdownModelUseCase,
                diagramTypeRegistry);

        return new ApplicationServices(
                new ProjectApplicationServices(
                        new SaveProjectUseCase(infrastructureServices.projectRepository()),
                        new OpenProjectUseCase(infrastructureServices.projectRepository(), validator),
                        infrastructureServices.sourceMarkdownSynchronizer(),
                        new ValidateProjectUseCase(validator)),
                new ImportApplicationServices(
                        importMarkdownModelUseCase,
                        markdownBatchImportUseCase,
                        sourceCodeUmlImportUseCase,
                        sourceCodeImportPreviewUseCase),
                new ExportApplicationServices(
                        new ExportSvgUseCase(infrastructureServices.svgDiagramExporter()),
                        new ExportMarkdownUseCase(infrastructureServices.markdownDiagramExporter()),
                        infrastructureServices.exportDataDictionaryPdfUseCase(),
                        infrastructureServices.exportDataDictionaryMarkdownUseCase(),
                        infrastructureServices.exportOpenProjectsForClientUseCase()),
                new CatalogApplicationServices(
                        new ListDiagramCategoriesUseCase(categoryCatalog),
                        new ListDiagramTypesUseCase(diagramTypeRegistry),
                        new DefaultCreateWorkspaceUseCase(diagramTypeRegistry)),
                new VisualApplicationServices(
                        chenLayoutUseCase,
                        crowsFootLayoutUseCase,
                        new SwitchNotationUseCase(chenLayoutUseCase, crowsFootLayoutUseCase),
                        new MoveElementUseCase(),
                        new AddBendPointUseCase(),
                        new MoveBendPointUseCase(),
                        new MoveConnectorLabelUseCase(),
                        new RemoveBendPointUseCase(),
                        new UpdateNodeLayoutUseCase(),
                        new ChangeElementStyleUseCase(),
                        new ChangeDiagramAppearanceUseCase(),
                        new ChangeConnectorAnchorsUseCase(),
                        new ChangeConnectorMarkerOrientationUseCase()),
                new DocumentationApplicationServices(infrastructureServices.exportAiResourcesUseCase()),
                new ConceptualModelApplicationServices(
                        new AddEntityUseCase(),
                        new AddAttributeUseCase(),
                        new AddRelationshipUseCase(),
                        new DuplicateEntityUseCase(),
                        new RemoveDiagramElementUseCase(),
                        new RenameElementUseCase(),
                        new UpdateElementDescriptionUseCase(),
                        new UpdateRelationshipCardinalityUseCase()),
                new DataDictionaryApplicationServices(
                        new DefaultCreateDataDictionaryUseCase(),
                        new AddDataDictionaryEntityUseCase(),
                        new AddDataDictionaryFieldUseCase(),
                        new UpdateDataDictionaryEntityUseCase(),
                        new UpdateDataDictionaryFieldUseCase(),
                        new RemoveDataDictionaryItemUseCase(),
                        new ValidateDataDictionaryUseCase()),
                new ModuleMapApplicationServices(
                        new CreateModuleMapUseCase(),
                        new AddModuleMapModuleUseCase(),
                        new AddModuleMapDependencyUseCase(),
                        new UpdateModuleMapModuleUseCase(),
                        new UpdateModuleMapDependencyUseCase(),
                        new RemoveModuleMapItemUseCase(),
                        new ValidateModuleMapUseCase()),
                new UmlClassApplicationServices(
                        new CreateUmlClassDiagramUseCase(),
                        new AddUmlModuleUseCase(),
                        new AddUmlClassUseCase(),
                        new AddUmlMemberUseCase(),
                        new AddUmlRelationUseCase(),
                        new UpdateUmlModuleUseCase(),
                        new UpdateUmlClassUseCase(),
                        new UpdateUmlMemberUseCase(),
                        new UpdateUmlRelationUseCase(),
                        new RemoveUmlClassDiagramItemUseCase(),
                        new ValidateUmlClassDiagramUseCase()),
                new RolesPermissionsApplicationServices(
                        new CreateRolesPermissionsUseCase(),
                        new AddRoleUseCase(),
                        new AddPermissionUseCase(),
                        new AddPermissionAssignmentUseCase(),
                        new UpdateRoleUseCase(),
                        new UpdatePermissionUseCase(),
                        new UpdatePermissionAssignmentUseCase(),
                        new RemoveRolesPermissionsItemUseCase(),
                        new ValidateRolesPermissionsUseCase()),
                new ScreenFlowApplicationServices(
                        new CreateScreenFlowUseCase(),
                        new AddScreenUseCase(),
                        new AddScreenTransitionUseCase(),
                        new UpdateScreenUseCase(),
                        new UpdateScreenTransitionUseCase(),
                        new RemoveScreenFlowItemUseCase(),
                        new ValidateScreenFlowUseCase()),
                new WireframeApplicationServices(
                        new CreateWireframeUseCase(),
                        new AddWireframeScreenUseCase(),
                        new AddWireframeComponentUseCase(),
                        new UpdateWireframeScreenUseCase(),
                        new UpdateWireframeComponentUseCase(),
                        new RemoveWireframeItemUseCase(),
                        new ValidateWireframeUseCase()),
                new BehaviorApplicationServices(
                        new CreateBehaviorDiagramUseCase(),
                        new AddBehaviorNodeUseCase(),
                        new AddBehaviorEdgeUseCase(),
                        new UpdateBehaviorNodeUseCase(),
                        new UpdateBehaviorEdgeUseCase(),
                        new RemoveBehaviorItemUseCase(),
                        new ValidateBehaviorDiagramUseCase()),
                new ArchitectureApplicationServices(
                        new CreateArchitectureDiagramUseCase(),
                        new AddArchitectureNodeUseCase(),
                        new AddArchitectureEdgeUseCase(),
                        new UpdateArchitectureNodeUseCase(),
                        new UpdateArchitectureEdgeUseCase(),
                        new RemoveArchitectureItemUseCase(),
                        new ValidateArchitectureDiagramUseCase())
        );
    }


    private GenerateUmlClassDiagramFromSourceCodeUseCase createSourceCodeUmlImportUseCase(
            FileSystemSourceDirectoryScanner sourceDirectoryScanner
    ) {
        SourceCodeParserRegistry parserRegistry = new SourceCodeParserRegistry(List.of(
                new JavaSourceCodeParserAdapter(),
                new TypeScriptSourceCodeParserAdapter()));
        SourceCodeProjectParserUseCase parserUseCase = new SourceCodeProjectParserUseCase(
                sourceDirectoryScanner,
                parserRegistry,
                new ParsedCodeProjectNormalizer());
        return new GenerateUmlClassDiagramFromSourceCodeUseCase(
                parserUseCase,
                new SourceCodeToUmlClassDiagramMapper());
    }

}
