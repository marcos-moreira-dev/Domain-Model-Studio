package com.marcosmoreira.domainmodelstudio.application;

import com.marcosmoreira.domainmodelstudio.application.batchexport.ExportOpenProjectsForClientUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.*;
import com.marcosmoreira.domainmodelstudio.application.architecture.*;
import com.marcosmoreira.domainmodelstudio.application.catalog.ListDiagramCategoriesUseCase;
import com.marcosmoreira.domainmodelstudio.application.catalog.ListDiagramTypesUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.*;
import com.marcosmoreira.domainmodelstudio.application.editing.*;
import com.marcosmoreira.domainmodelstudio.application.export.*;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelUseCase;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.*;
import com.marcosmoreira.domainmodelstudio.application.modulemap.*;
import com.marcosmoreira.domainmodelstudio.application.notation.SwitchNotationUseCase;
import com.marcosmoreira.domainmodelstudio.application.project.*;
import com.marcosmoreira.domainmodelstudio.application.resources.ExportAiResourcesUseCase;
import com.marcosmoreira.domainmodelstudio.application.services.*;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.PreviewSourceCodeImportUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.*;
import com.marcosmoreira.domainmodelstudio.application.rolespermissions.*;
import com.marcosmoreira.domainmodelstudio.application.screenflow.*;
import com.marcosmoreira.domainmodelstudio.application.wireframe.*;
import com.marcosmoreira.domainmodelstudio.application.validation.ValidateProjectUseCase;
import com.marcosmoreira.domainmodelstudio.application.workspace.CreateWorkspaceUseCase;
import java.util.Objects;

/**
 * Fachada de compatibilidad de casos de uso disponibles para la presentación.
 *
 * <p>Desde la Tanda 28 la composición real vive en fachadas por familia.
 * Esta clase conserva los accesos históricos para no romper pantallas existentes,
 * pero el código nuevo debe depender de {@link #projectServices()},
 * {@link #importServices()}, {@link #exportServices()} o de la familia
 * específica correspondiente.</p>
 *
 * <p>Esta clase no es el lugar para registrar reglas de dominio, abrir ventanas ni
 * construir infraestructura concreta. Su valor post-refactor es mantener compatibilidad
 * mientras los consumidores migran gradualmente hacia familias explícitas.</p>
 */

public final class ApplicationServices {

    private final ProjectApplicationServices projectServices;
    private final ImportApplicationServices importServices;
    private final ExportApplicationServices exportServices;
    private final CatalogApplicationServices catalogServices;
    private final VisualApplicationServices visualServices;
    private final DocumentationApplicationServices documentationServices;
    private final ConceptualModelApplicationServices conceptualModelServices;
    private final DataDictionaryApplicationServices dataDictionaryServices;
    private final ModuleMapApplicationServices moduleMapServices;
    private final UmlClassApplicationServices umlClassServices;
    private final RolesPermissionsApplicationServices rolesPermissionsServices;
    private final ScreenFlowApplicationServices screenFlowServices;
    private final WireframeApplicationServices wireframeServices;
    private final BehaviorApplicationServices behaviorServices;
    private final ArchitectureApplicationServices architectureServices;

    public ApplicationServices(
            ProjectApplicationServices projectServices,
            ImportApplicationServices importServices,
            ExportApplicationServices exportServices,
            CatalogApplicationServices catalogServices,
            VisualApplicationServices visualServices,
            DocumentationApplicationServices documentationServices,
            ConceptualModelApplicationServices conceptualModelServices,
            DataDictionaryApplicationServices dataDictionaryServices,
            ModuleMapApplicationServices moduleMapServices,
            UmlClassApplicationServices umlClassServices,
            RolesPermissionsApplicationServices rolesPermissionsServices,
            ScreenFlowApplicationServices screenFlowServices,
            WireframeApplicationServices wireframeServices,
            BehaviorApplicationServices behaviorServices,
            ArchitectureApplicationServices architectureServices
    ) {
        this.projectServices = Objects.requireNonNull(projectServices, "projectServices");
        this.importServices = Objects.requireNonNull(importServices, "importServices");
        this.exportServices = Objects.requireNonNull(exportServices, "exportServices");
        this.catalogServices = Objects.requireNonNull(catalogServices, "catalogServices");
        this.visualServices = Objects.requireNonNull(visualServices, "visualServices");
        this.documentationServices = Objects.requireNonNull(documentationServices, "documentationServices");
        this.conceptualModelServices = Objects.requireNonNull(conceptualModelServices, "conceptualModelServices");
        this.dataDictionaryServices = Objects.requireNonNull(dataDictionaryServices, "dataDictionaryServices");
        this.moduleMapServices = Objects.requireNonNull(moduleMapServices, "moduleMapServices");
        this.umlClassServices = Objects.requireNonNull(umlClassServices, "umlClassServices");
        this.rolesPermissionsServices = Objects.requireNonNull(rolesPermissionsServices, "rolesPermissionsServices");
        this.screenFlowServices = Objects.requireNonNull(screenFlowServices, "screenFlowServices");
        this.wireframeServices = Objects.requireNonNull(wireframeServices, "wireframeServices");
        this.behaviorServices = Objects.requireNonNull(behaviorServices, "behaviorServices");
        this.architectureServices = Objects.requireNonNull(architectureServices, "architectureServices");
    }

    public ProjectApplicationServices projectServices() {
        return projectServices;
    }

    public ImportApplicationServices importServices() {
        return importServices;
    }

    public ExportApplicationServices exportServices() {
        return exportServices;
    }

    public CatalogApplicationServices catalogServices() {
        return catalogServices;
    }

    public VisualApplicationServices visualServices() {
        return visualServices;
    }

    public DocumentationApplicationServices documentationServices() {
        return documentationServices;
    }

    public ConceptualModelApplicationServices conceptualModelServices() {
        return conceptualModelServices;
    }

    public DataDictionaryApplicationServices dataDictionaryServices() {
        return dataDictionaryServices;
    }

    public ModuleMapApplicationServices moduleMapServices() {
        return moduleMapServices;
    }

    public UmlClassApplicationServices umlClassServices() {
        return umlClassServices;
    }

    public RolesPermissionsApplicationServices rolesPermissionsServices() {
        return rolesPermissionsServices;
    }

    public ScreenFlowApplicationServices screenFlowServices() {
        return screenFlowServices;
    }

    public WireframeApplicationServices wireframeServices() {
        return wireframeServices;
    }

    public BehaviorApplicationServices behaviorServices() {
        return behaviorServices;
    }

    public ArchitectureApplicationServices architectureServices() {
        return architectureServices;
    }

    /** @deprecated Use {@link #importServices()} en código nuevo. */
    @Deprecated(forRemoval = false)
    public ImportMarkdownModelUseCase importMarkdownModelUseCase() {
        return importServices.importMarkdownModelUseCase();
    }

    public MarkdownBatchImportUseCase markdownBatchImportUseCase() {
        return importServices.markdownBatchImportUseCase();
    }

    public GenerateInitialChenLayoutUseCase generateInitialChenLayoutUseCase() {
        return visualServices.generateInitialChenLayoutUseCase();
    }

    public GenerateInitialCrowsFootLayoutUseCase generateInitialCrowsFootLayoutUseCase() {
        return visualServices.generateInitialCrowsFootLayoutUseCase();
    }

    public SwitchNotationUseCase switchNotationUseCase() {
        return visualServices.switchNotationUseCase();
    }

    public AddEntityUseCase addEntityUseCase() {
        return conceptualModelServices.addEntityUseCase();
    }

    public AddAttributeUseCase addAttributeUseCase() {
        return conceptualModelServices.addAttributeUseCase();
    }

    public AddRelationshipUseCase addRelationshipUseCase() {
        return conceptualModelServices.addRelationshipUseCase();
    }

    public DuplicateEntityUseCase duplicateEntityUseCase() {
        return conceptualModelServices.duplicateEntityUseCase();
    }

    public RemoveDiagramElementUseCase removeDiagramElementUseCase() {
        return conceptualModelServices.removeDiagramElementUseCase();
    }

    public MoveElementUseCase moveElementUseCase() {
        return visualServices.moveElementUseCase();
    }

    public AddBendPointUseCase addBendPointUseCase() {
        return visualServices.addBendPointUseCase();
    }

    public MoveBendPointUseCase moveBendPointUseCase() {
        return visualServices.moveBendPointUseCase();
    }

    public MoveConnectorLabelUseCase moveConnectorLabelUseCase() {
        return visualServices.moveConnectorLabelUseCase();
    }

    public RemoveBendPointUseCase removeBendPointUseCase() {
        return visualServices.removeBendPointUseCase();
    }

    public RenameElementUseCase renameElementUseCase() {
        return conceptualModelServices.renameElementUseCase();
    }

    public UpdateNodeLayoutUseCase updateNodeLayoutUseCase() {
        return visualServices.updateNodeLayoutUseCase();
    }

    public UpdateElementDescriptionUseCase updateElementDescriptionUseCase() {
        return conceptualModelServices.updateElementDescriptionUseCase();
    }

    public UpdateRelationshipCardinalityUseCase updateRelationshipCardinalityUseCase() {
        return conceptualModelServices.updateRelationshipCardinalityUseCase();
    }

    public ChangeElementStyleUseCase changeElementStyleUseCase() {
        return visualServices.changeElementStyleUseCase();
    }

    public ChangeDiagramAppearanceUseCase changeDiagramAppearanceUseCase() {
        return visualServices.changeDiagramAppearanceUseCase();
    }

    public ChangeConnectorAnchorsUseCase changeConnectorAnchorsUseCase() {
        return visualServices.changeConnectorAnchorsUseCase();
    }

    public ChangeConnectorMarkerOrientationUseCase changeConnectorMarkerOrientationUseCase() {
        return visualServices.changeConnectorMarkerOrientationUseCase();
    }

    public SaveProjectUseCase saveProjectUseCase() {
        return projectServices.saveProjectUseCase();
    }

    public OpenProjectUseCase openProjectUseCase() {
        return projectServices.openProjectUseCase();
    }

    public SourceMarkdownSynchronizer sourceMarkdownSynchronizer() {
        return projectServices.sourceMarkdownSynchronizer();
    }

    public ExportSvgUseCase exportSvgUseCase() {
        return exportServices.exportSvgUseCase();
    }

    public ExportMarkdownUseCase exportMarkdownUseCase() {
        return exportServices.exportMarkdownUseCase();
    }

    public ValidateProjectUseCase validateProjectUseCase() {
        return projectServices.validateProjectUseCase();
    }

    public ListDiagramCategoriesUseCase listDiagramCategoriesUseCase() {
        return catalogServices.listDiagramCategoriesUseCase();
    }

    public ListDiagramTypesUseCase listDiagramTypesUseCase() {
        return catalogServices.listDiagramTypesUseCase();
    }

    public CreateWorkspaceUseCase createWorkspaceUseCase() {
        return catalogServices.createWorkspaceUseCase();
    }

    public ExportAiResourcesUseCase exportAiResourcesUseCase() {
        return documentationServices.exportAiResourcesUseCase();
    }

    public CreateDataDictionaryUseCase createDataDictionaryUseCase() {
        return dataDictionaryServices.createDataDictionaryUseCase();
    }

    public AddDataDictionaryEntityUseCase addDataDictionaryEntityUseCase() {
        return dataDictionaryServices.addDataDictionaryEntityUseCase();
    }

    public AddDataDictionaryFieldUseCase addDataDictionaryFieldUseCase() {
        return dataDictionaryServices.addDataDictionaryFieldUseCase();
    }

    public UpdateDataDictionaryEntityUseCase updateDataDictionaryEntityUseCase() {
        return dataDictionaryServices.updateDataDictionaryEntityUseCase();
    }

    public UpdateDataDictionaryFieldUseCase updateDataDictionaryFieldUseCase() {
        return dataDictionaryServices.updateDataDictionaryFieldUseCase();
    }

    public RemoveDataDictionaryItemUseCase removeDataDictionaryItemUseCase() {
        return dataDictionaryServices.removeDataDictionaryItemUseCase();
    }

    public ValidateDataDictionaryUseCase validateDataDictionaryUseCase() {
        return dataDictionaryServices.validateDataDictionaryUseCase();
    }

    public ExportDataDictionaryPdfUseCase exportDataDictionaryPdfUseCase() {
        return exportServices.exportDataDictionaryPdfUseCase();
    }

    public ExportDataDictionaryMarkdownUseCase exportDataDictionaryMarkdownUseCase() {
        return exportServices.exportDataDictionaryMarkdownUseCase();
    }

    public CreateModuleMapUseCase createModuleMapUseCase() {
        return moduleMapServices.createModuleMapUseCase();
    }

    public AddModuleMapModuleUseCase addModuleMapModuleUseCase() {
        return moduleMapServices.addModuleMapModuleUseCase();
    }

    public AddModuleMapDependencyUseCase addModuleMapDependencyUseCase() {
        return moduleMapServices.addModuleMapDependencyUseCase();
    }

    public UpdateModuleMapModuleUseCase updateModuleMapModuleUseCase() {
        return moduleMapServices.updateModuleMapModuleUseCase();
    }

    public UpdateModuleMapDependencyUseCase updateModuleMapDependencyUseCase() {
        return moduleMapServices.updateModuleMapDependencyUseCase();
    }

    public RemoveModuleMapItemUseCase removeModuleMapItemUseCase() {
        return moduleMapServices.removeModuleMapItemUseCase();
    }

    public ValidateModuleMapUseCase validateModuleMapUseCase() {
        return moduleMapServices.validateModuleMapUseCase();
    }

    public CreateUmlClassDiagramUseCase createUmlClassDiagramUseCase() {
        return umlClassServices.createUmlClassDiagramUseCase();
    }

    public GenerateUmlClassDiagramFromSourceCodeUseCase generateUmlClassDiagramFromSourceCodeUseCase() {
        return importServices.generateUmlClassDiagramFromSourceCodeUseCase();
    }

    public PreviewSourceCodeImportUseCase previewSourceCodeImportUseCase() {
        return importServices.previewSourceCodeImportUseCase();
    }

    public AddUmlModuleUseCase addUmlModuleUseCase() {
        return umlClassServices.addUmlModuleUseCase();
    }

    public AddUmlClassUseCase addUmlClassUseCase() {
        return umlClassServices.addUmlClassUseCase();
    }

    public AddUmlMemberUseCase addUmlMemberUseCase() {
        return umlClassServices.addUmlMemberUseCase();
    }

    public AddUmlRelationUseCase addUmlRelationUseCase() {
        return umlClassServices.addUmlRelationUseCase();
    }

    public UpdateUmlModuleUseCase updateUmlModuleUseCase() {
        return umlClassServices.updateUmlModuleUseCase();
    }

    public UpdateUmlClassUseCase updateUmlClassUseCase() {
        return umlClassServices.updateUmlClassUseCase();
    }

    public UpdateUmlMemberUseCase updateUmlMemberUseCase() {
        return umlClassServices.updateUmlMemberUseCase();
    }

    public UpdateUmlRelationUseCase updateUmlRelationUseCase() {
        return umlClassServices.updateUmlRelationUseCase();
    }

    public RemoveUmlClassDiagramItemUseCase removeUmlClassDiagramItemUseCase() {
        return umlClassServices.removeUmlClassDiagramItemUseCase();
    }

    public ValidateUmlClassDiagramUseCase validateUmlClassDiagramUseCase() {
        return umlClassServices.validateUmlClassDiagramUseCase();
    }

    public CreateRolesPermissionsUseCase createRolesPermissionsUseCase() {
        return rolesPermissionsServices.createRolesPermissionsUseCase();
    }

    public AddRoleUseCase addRoleUseCase() {
        return rolesPermissionsServices.addRoleUseCase();
    }

    public AddPermissionUseCase addPermissionUseCase() {
        return rolesPermissionsServices.addPermissionUseCase();
    }

    public AddPermissionAssignmentUseCase addPermissionAssignmentUseCase() {
        return rolesPermissionsServices.addPermissionAssignmentUseCase();
    }

    public UpdateRoleUseCase updateRoleUseCase() {
        return rolesPermissionsServices.updateRoleUseCase();
    }

    public UpdatePermissionUseCase updatePermissionUseCase() {
        return rolesPermissionsServices.updatePermissionUseCase();
    }

    public UpdatePermissionAssignmentUseCase updatePermissionAssignmentUseCase() {
        return rolesPermissionsServices.updatePermissionAssignmentUseCase();
    }

    public RemoveRolesPermissionsItemUseCase removeRolesPermissionsItemUseCase() {
        return rolesPermissionsServices.removeRolesPermissionsItemUseCase();
    }

    public ValidateRolesPermissionsUseCase validateRolesPermissionsUseCase() {
        return rolesPermissionsServices.validateRolesPermissionsUseCase();
    }

    public CreateScreenFlowUseCase createScreenFlowUseCase() {
        return screenFlowServices.createScreenFlowUseCase();
    }

    public AddScreenUseCase addScreenUseCase() {
        return screenFlowServices.addScreenUseCase();
    }

    public AddScreenTransitionUseCase addScreenTransitionUseCase() {
        return screenFlowServices.addScreenTransitionUseCase();
    }

    public UpdateScreenUseCase updateScreenUseCase() {
        return screenFlowServices.updateScreenUseCase();
    }

    public UpdateScreenTransitionUseCase updateScreenTransitionUseCase() {
        return screenFlowServices.updateScreenTransitionUseCase();
    }

    public RemoveScreenFlowItemUseCase removeScreenFlowItemUseCase() {
        return screenFlowServices.removeScreenFlowItemUseCase();
    }

    public ValidateScreenFlowUseCase validateScreenFlowUseCase() {
        return screenFlowServices.validateScreenFlowUseCase();
    }

    public CreateWireframeUseCase createWireframeUseCase() {
        return wireframeServices.createWireframeUseCase();
    }

    public AddWireframeScreenUseCase addWireframeScreenUseCase() {
        return wireframeServices.addWireframeScreenUseCase();
    }

    public AddWireframeComponentUseCase addWireframeComponentUseCase() {
        return wireframeServices.addWireframeComponentUseCase();
    }

    public UpdateWireframeScreenUseCase updateWireframeScreenUseCase() {
        return wireframeServices.updateWireframeScreenUseCase();
    }

    public UpdateWireframeComponentUseCase updateWireframeComponentUseCase() {
        return wireframeServices.updateWireframeComponentUseCase();
    }

    public RemoveWireframeItemUseCase removeWireframeItemUseCase() {
        return wireframeServices.removeWireframeItemUseCase();
    }

    public ValidateWireframeUseCase validateWireframeUseCase() {
        return wireframeServices.validateWireframeUseCase();
    }

    public CreateBehaviorDiagramUseCase createBehaviorDiagramUseCase() {
        return behaviorServices.createBehaviorDiagramUseCase();
    }

    public AddBehaviorNodeUseCase addBehaviorNodeUseCase() {
        return behaviorServices.addBehaviorNodeUseCase();
    }

    public AddBehaviorEdgeUseCase addBehaviorEdgeUseCase() {
        return behaviorServices.addBehaviorEdgeUseCase();
    }

    public UpdateBehaviorNodeUseCase updateBehaviorNodeUseCase() {
        return behaviorServices.updateBehaviorNodeUseCase();
    }

    public UpdateBehaviorEdgeUseCase updateBehaviorEdgeUseCase() {
        return behaviorServices.updateBehaviorEdgeUseCase();
    }

    public RemoveBehaviorItemUseCase removeBehaviorItemUseCase() {
        return behaviorServices.removeBehaviorItemUseCase();
    }

    public ValidateBehaviorDiagramUseCase validateBehaviorDiagramUseCase() {
        return behaviorServices.validateBehaviorDiagramUseCase();
    }

    public CreateArchitectureDiagramUseCase createArchitectureDiagramUseCase() {
        return architectureServices.createArchitectureDiagramUseCase();
    }

    public AddArchitectureNodeUseCase addArchitectureNodeUseCase() {
        return architectureServices.addArchitectureNodeUseCase();
    }

    public AddArchitectureEdgeUseCase addArchitectureEdgeUseCase() {
        return architectureServices.addArchitectureEdgeUseCase();
    }

    public UpdateArchitectureNodeUseCase updateArchitectureNodeUseCase() {
        return architectureServices.updateArchitectureNodeUseCase();
    }

    public UpdateArchitectureEdgeUseCase updateArchitectureEdgeUseCase() {
        return architectureServices.updateArchitectureEdgeUseCase();
    }

    public RemoveArchitectureItemUseCase removeArchitectureItemUseCase() {
        return architectureServices.removeArchitectureItemUseCase();
    }

    public ValidateArchitectureDiagramUseCase validateArchitectureDiagramUseCase() {
        return architectureServices.validateArchitectureDiagramUseCase();
    }

    public ExportOpenProjectsForClientUseCase exportOpenProjectsForClientUseCase() {
        return exportServices.exportOpenProjectsForClientUseCase();
    }

}