package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Registry runtime por defecto derivado del catálogo oficial vigente. */
public final class DefaultDiagramTypeRuntimeRegistry implements DiagramTypeRuntimeRegistry {

    private final PayloadRuntimeRegistry payloadRuntimeRegistry;
    private final Map<DiagramTypeId, DiagramTypeRuntimeDescriptor> descriptorsByType;

    public DefaultDiagramTypeRuntimeRegistry() {
        this(DefaultDiagramTypeDefinitions.all(), new DefaultPayloadRuntimeRegistry());
    }

    public DefaultDiagramTypeRuntimeRegistry(List<DiagramTypeOfficialDefinition> definitions) {
        this(definitions, new DefaultPayloadRuntimeRegistry());
    }

    public DefaultDiagramTypeRuntimeRegistry(
            List<DiagramTypeOfficialDefinition> definitions,
            PayloadRuntimeRegistry payloadRuntimeRegistry
    ) {
        Objects.requireNonNull(definitions, "definitions");
        this.payloadRuntimeRegistry = Objects.requireNonNull(payloadRuntimeRegistry, "payloadRuntimeRegistry");
        Map<DiagramTypeId, DiagramTypeRuntimeDescriptor> indexed = new LinkedHashMap<>();
        for (DiagramTypeOfficialDefinition definition : definitions) {
            DiagramTypeRuntimeDescriptor previous = indexed.put(definition.id(), fromDefinition(definition, payloadRuntimeRegistry));
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Descriptor runtime duplicado para " + definition.id().value() + ".");
            }
        }
        this.descriptorsByType = Map.copyOf(indexed);
    }

    @Override
    public Optional<DiagramTypeRuntimeDescriptor> find(DiagramTypeId diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        return Optional.ofNullable(descriptorsByType.get(diagramTypeId));
    }

    @Override
    public DiagramTypeRuntimeDescriptor require(DiagramTypeId diagramTypeId) {
        return find(diagramTypeId).orElseThrow(() -> new IllegalArgumentException(
                "No existe descriptor runtime para " + diagramTypeId.value() + "."));
    }

    @Override
    public List<DiagramTypeRuntimeDescriptor> findAll() {
        return List.copyOf(descriptorsByType.values());
    }

    @Override
    public List<DiagramTypeRuntimeDescriptor> availableTypes() {
        return findAll().stream()
                .filter(DiagramTypeRuntimeDescriptor::available)
                .toList();
    }

    @Override
    public List<DiagramTypeRuntimeDescriptor> importableMarkdownTypes() {
        return availableTypes().stream()
                .filter(descriptor -> descriptor.hasCapability(DiagramCapability.IMPORT_MARKDOWN))
                .toList();
    }

    private static DiagramTypeRuntimeDescriptor fromDefinition(
            DiagramTypeOfficialDefinition definition,
            PayloadRuntimeRegistry payloadRuntimeRegistry
    ) {
        return new DiagramTypeRuntimeDescriptor(
                definition,
                markdownImport(definition),
                markdownExport(definition),
                workspace(definition),
                payloadRuntimeRegistry.find(definition.id()),
                activeOutput(definition),
                help(definition),
                officialExample(definition));
    }

    private static Optional<MarkdownImportRuntimeDescriptor> markdownImport(DiagramTypeOfficialDefinition definition) {
        if (!definition.capabilities().has(DiagramCapability.IMPORT_MARKDOWN)) {
            return Optional.empty();
        }
        return Optional.of(new MarkdownImportRuntimeDescriptor(
                definition.id(),
                definition.grammarResourceId(),
                "DiagramMarkdownImportDispatcher"));
    }

    private static Optional<MarkdownExportRuntimeDescriptor> markdownExport(DiagramTypeOfficialDefinition definition) {
        if (!definition.capabilities().has(DiagramCapability.EXPORT_MARKDOWN)) {
            return Optional.empty();
        }
        return Optional.of(new MarkdownExportRuntimeDescriptor(definition.id(), "MarkdownProjectWriter"));
    }

    private static Optional<WorkspaceRuntimeDescriptor> workspace(DiagramTypeOfficialDefinition definition) {
        if (definition.supportStatus() != DiagramSupportStatus.AVAILABLE) {
            return Optional.empty();
        }
        return Optional.of(new WorkspaceRuntimeDescriptor(
                definition.id(),
                definition.workspaceKind(),
                definition.toolbarPolicyId()));
    }

    private static Optional<ActiveOutputRuntimeDescriptor> activeOutput(DiagramTypeOfficialDefinition definition) {
        EnumSet<DiagramCapability> exportCapabilities = EnumSet.noneOf(DiagramCapability.class);
        for (DiagramCapability capability : List.of(
                DiagramCapability.EXPORT_MARKDOWN,
                DiagramCapability.EXPORT_PNG,
                DiagramCapability.EXPORT_SVG,
                DiagramCapability.EXPORT_PDF)) {
            if (definition.capabilities().has(capability)) {
                exportCapabilities.add(capability);
            }
        }
        boolean visual = definition.capabilities().has(DiagramCapability.SHOW_VISUAL_OUTPUT);
        boolean document = definition.capabilities().has(DiagramCapability.SHOW_DOCUMENT_OUTPUT);
        if (!visual && !document && exportCapabilities.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new ActiveOutputRuntimeDescriptor(
                definition.id(),
                visual,
                document,
                exportCapabilities));
    }

    private static Optional<HelpRuntimeDescriptor> help(DiagramTypeOfficialDefinition definition) {
        boolean theory = definition.capabilities().has(DiagramCapability.THEORY_HELP);
        boolean ai = definition.capabilities().has(DiagramCapability.AI_RESOURCES);
        if (!theory && !ai) {
            return Optional.empty();
        }
        return Optional.of(new HelpRuntimeDescriptor(definition.id(), definition.theoryTopicId(), ai));
    }

    private static Optional<OfficialExampleRuntimeDescriptor> officialExample(DiagramTypeOfficialDefinition definition) {
        if (!definition.hasOfficialExample()) {
            return Optional.empty();
        }
        return Optional.of(new OfficialExampleRuntimeDescriptor(
                definition.id(),
                definition.officialExampleId(),
                definition.officialExampleTitle(),
                definition.officialExampleResource(),
                definition.officialExampleImportable()));
    }
}
