package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.Objects;
import java.util.Optional;

/**
 * Vista runtime, declarativa y verificable, de un tipo oficial de proyecto.
 *
 * <p>Esta primera versión no sustituye los registries existentes de infraestructura o presentación;
 * los indexa como contrato de producto para que las siguientes tandas puedan migrar parser,
 * workspace, salida activa y persistencia sin volver a duplicar decisiones por tipo.</p>
 */
public record DiagramTypeRuntimeDescriptor(
        DiagramTypeOfficialDefinition definition,
        Optional<MarkdownImportRuntimeDescriptor> markdownImport,
        Optional<MarkdownExportRuntimeDescriptor> markdownExport,
        Optional<WorkspaceRuntimeDescriptor> workspace,
        Optional<PayloadRuntimeDescriptor> payload,
        Optional<ActiveOutputRuntimeDescriptor> activeOutput,
        Optional<HelpRuntimeDescriptor> help,
        Optional<OfficialExampleRuntimeDescriptor> officialExample
) {

    public DiagramTypeRuntimeDescriptor {
        Objects.requireNonNull(definition, "definition");
        markdownImport = requireOptional(markdownImport, "markdownImport");
        markdownExport = requireOptional(markdownExport, "markdownExport");
        workspace = requireOptional(workspace, "workspace");
        payload = requireOptional(payload, "payload");
        activeOutput = requireOptional(activeOutput, "activeOutput");
        help = requireOptional(help, "help");
        officialExample = requireOptional(officialExample, "officialExample");
    }

    public DiagramTypeId id() {
        return definition.id();
    }

    public String displayName() {
        return definition.displayName();
    }

    public DiagramSupportStatus supportStatus() {
        return definition.supportStatus();
    }

    public DiagramCapabilitySet capabilities() {
        return definition.capabilities();
    }

    public DiagramWorkspaceKind workspaceKind() {
        return definition.workspaceKind();
    }

    public boolean hasCapability(DiagramCapability capability) {
        return capabilities().has(capability);
    }

    public boolean available() {
        return supportStatus() == DiagramSupportStatus.AVAILABLE;
    }

    private static <T> Optional<T> requireOptional(Optional<T> value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        return value;
    }
}
