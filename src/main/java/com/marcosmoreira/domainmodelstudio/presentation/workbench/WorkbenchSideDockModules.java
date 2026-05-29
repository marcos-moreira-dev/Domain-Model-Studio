package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockContext;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleId;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleRegistry;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.StaticSideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.StandardSideDockModules;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.util.List;
import java.util.Optional;
import javafx.scene.Parent;

/** Registra módulos laterales comunes sin acoplar las vistas a cada variante de workspace. */
final class WorkbenchSideDockModules {

    private WorkbenchSideDockModules() {
    }

    static void registerVisualModules(
            SideDockModuleRegistry registry,
            DiagramWorkbenchDescriptor descriptor,
            Optional<Parent> structurePanel,
            Optional<Parent> propertiesPanel,
            List<SideDockModule> additionalModules
    ) {
        structurePanel.ifPresent(panel -> registry.register(StaticSideDockModule.of(
                SideDockModuleId.STRUCTURE,
                descriptor.structurePanelTitle(),
                panel
        )));
        propertiesPanel.ifPresent(panel -> registry.register(StaticSideDockModule.of(
                SideDockModuleId.PROPERTIES,
                descriptor.propertiesPanelTitle(),
                panel
        )));
        additionalModules.forEach(registry::register);
        registerOperationalHelpIfUseful(registry, descriptor.workspaceKind(), descriptor.title(),
                descriptor.subtitle(), additionalModules);
    }

    static void registerStructuredModules(
            SideDockModuleRegistry registry,
            StructuredWorkbenchDescriptor descriptor,
            SideDockContext context,
            Optional<Parent> structurePanel,
            Optional<Parent> propertiesPanel
    ) {
        registerStructuredModules(registry, descriptor, context, structurePanel, propertiesPanel, List.of());
    }

    static void registerStructuredModules(
            SideDockModuleRegistry registry,
            StructuredWorkbenchDescriptor descriptor,
            SideDockContext context,
            Optional<Parent> structurePanel,
            Optional<Parent> propertiesPanel,
            List<SideDockModule> additionalModules
    ) {
        structurePanel.ifPresent(panel -> registry.register(StaticSideDockModule.of(
                structureModuleId(context),
                structureTitle(descriptor, context),
                panel
        )));
        propertiesPanel.ifPresent(panel -> registry.register(StaticSideDockModule.of(
                propertiesModuleId(context),
                propertiesTitle(descriptor, context),
                panel
        )));
        additionalModules.forEach(registry::register);
        registerOperationalHelpIfUseful(registry, descriptor.workspaceKind(), descriptor.headerState().title(),
                descriptor.headerState().subtitle(), additionalModules);
    }


    private static void registerOperationalHelpIfUseful(
            SideDockModuleRegistry registry,
            WorkspaceKind workspaceKind,
            String title,
            String subtitle,
            List<SideDockModule> additionalModules
    ) {
        if (workspaceKind == WorkspaceKind.CONCEPTUAL_CANVAS || containsHelp(additionalModules)) {
            return;
        }
        registry.register(StandardSideDockModules.operationalHelp(workspaceKind, title, subtitle));
    }

    private static boolean containsHelp(List<SideDockModule> modules) {
        return modules.stream().anyMatch(module -> module.id() == SideDockModuleId.HELP);
    }

    static SideDockModuleId visualModuleId(WorkbenchPanelSlot slot) {
        return slot == WorkbenchPanelSlot.STRUCTURE
                ? SideDockModuleId.STRUCTURE
                : SideDockModuleId.PROPERTIES;
    }

    static SideDockModuleId structuredModuleId(SideDockContext context, WorkbenchPanelSlot slot) {
        return slot == WorkbenchPanelSlot.STRUCTURE
                ? structureModuleId(context)
                : propertiesModuleId(context);
    }

    private static SideDockModuleId structureModuleId(SideDockContext context) {
        return context.matrixLike() ? SideDockModuleId.ROLES : SideDockModuleId.SECTIONS;
    }

    private static SideDockModuleId propertiesModuleId(SideDockContext context) {
        return context.matrixLike() ? SideDockModuleId.PERMISSIONS : SideDockModuleId.PROPERTIES;
    }

    private static String structureTitle(StructuredWorkbenchDescriptor descriptor, SideDockContext context) {
        return context.matrixLike() ? "Roles" : descriptor.structurePanelTitle();
    }

    private static String propertiesTitle(StructuredWorkbenchDescriptor descriptor, SideDockContext context) {
        return context.matrixLike() ? "Permisos" : descriptor.propertiesPanelTitle();
    }
}
