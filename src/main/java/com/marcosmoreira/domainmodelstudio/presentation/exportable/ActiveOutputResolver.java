package com.marcosmoreira.domainmodelstudio.presentation.exportable;

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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/** Resuelve la salida final de la pestaña activa sin depender de editores residuales. */
public final class ActiveOutputResolver implements ActiveOutputProvider {

    private final Supplier<Optional<DiagramProject>> activeProjectSupplier;
    private final ProjectExportFormatPolicy exportFormatPolicy;
    private final ActiveOutputContributorRegistry contributorRegistry;

    public ActiveOutputResolver(
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
            LogicalBusinessGraphViewModel logicalBusinessGraphViewModel,
            Supplier<Optional<DiagramProject>> activeProjectSupplier
    ) {
        this.activeProjectSupplier = Objects.requireNonNull(activeProjectSupplier, "activeProjectSupplier");
        this.exportFormatPolicy = new ProjectExportFormatPolicy();
        this.contributorRegistry = ActiveOutputContributorRegistry.defaultRegistry(
                canvasViewModel,
                dataDictionaryViewModel,
                moduleMapViewModel,
                umlClassDiagramViewModel,
                rolesPermissionsViewModel,
                screenFlowViewModel,
                wireframeViewModel,
                behaviorDiagramViewModel,
                architectureDiagramViewModel,
                freeGraphViewModel,
                logicalBusinessViewModel,
                logicalBusinessGraphViewModel);
    }

    @Override
    public Optional<ExportableOutput> activeOutput() {
        return activeProjectSupplier.get()
                .flatMap(activeProject -> contributorRegistry.resolve(activeProject, exportFormatPolicy));
    }
}
