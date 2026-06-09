package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.architecture.ArchitectureDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.behavior.BehaviorDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.datadictionary.DataDictionaryViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.freegraph.FreeGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph.LogicalBusinessGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.modulemap.ModuleMapViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.rolespermissions.RolesPermissionsViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.screenflow.ScreenFlowViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.wireframe.WireframeViewModel;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Registro de contributors que resuelven salidas exportables por familia de workspace. */
final class ActiveOutputContributorRegistry {

    private final List<ActiveOutputContributor> contributors;

    ActiveOutputContributorRegistry(List<ActiveOutputContributor> contributors) {
        this.contributors = List.copyOf(Objects.requireNonNull(contributors, "contributors"));
    }

    static ActiveOutputContributorRegistry defaultRegistry(
            DiagramCanvasViewModel canvasViewModel,
            DataDictionaryViewModel dataDictionaryViewModel,
            ModuleMapViewModel moduleMapViewModel,
            UmlClassDiagramViewModel umlClassDiagramViewModel,
            RolesPermissionsViewModel rolesPermissionsViewModel,
            ScreenFlowViewModel screenFlowViewModel,
            WireframeViewModel wireframeViewModel,
            BehaviorDiagramViewModel behaviorDiagramViewModel,
            ArchitectureDiagramViewModel architectureDiagramViewModel,
            FreeGraphViewModel freeGraphViewModel,
            LogicalBusinessViewModel logicalBusinessViewModel,
            LogicalBusinessGraphViewModel logicalBusinessGraphViewModel
    ) {
        return new ActiveOutputContributorRegistry(List.of(
                new ConceptualActiveOutputContributor(canvasViewModel),
                new DataDictionaryActiveOutputContributor(dataDictionaryViewModel),
                new ModuleMapActiveOutputContributor(moduleMapViewModel),
                new UmlClassActiveOutputContributor(umlClassDiagramViewModel),
                new RolesPermissionsActiveOutputContributor(rolesPermissionsViewModel),
                new ScreenFlowActiveOutputContributor(screenFlowViewModel),
                new WireframeActiveOutputContributor(wireframeViewModel),
                new FreeGraphActiveOutputContributor(freeGraphViewModel),
                new LogicalBusinessActiveOutputContributor(logicalBusinessViewModel),
                new LogicalBusinessGraphActiveOutputContributor(logicalBusinessGraphViewModel),
                new BehaviorActiveOutputContributor(behaviorDiagramViewModel),
                new ArchitectureActiveOutputContributor(architectureDiagramViewModel)
        ));
    }

    Optional<ExportableOutput> resolve(
            DiagramProject activeProject,
            ProjectExportFormatPolicy exportFormatPolicy
    ) {
        if (activeProject == null) {
            return Optional.empty();
        }
        DiagramTypeId diagramTypeId = activeProject.metadata().diagramTypeId();
        return contributors.stream()
                .filter(contributor -> contributor.supports(diagramTypeId))
                .findFirst()
                .flatMap(contributor -> contributor.resolve(activeProject, exportFormatPolicy));
    }
}
