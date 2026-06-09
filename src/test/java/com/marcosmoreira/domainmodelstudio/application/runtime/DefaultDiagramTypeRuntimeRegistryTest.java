package com.marcosmoreira.domainmodelstudio.application.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class DefaultDiagramTypeRuntimeRegistryTest {

    @Test
    void shouldExposeOneRuntimeDescriptorForEveryOfficialType() {
        DefaultDiagramTypeRuntimeRegistry registry = new DefaultDiagramTypeRuntimeRegistry();
        Set<DiagramTypeId> officialIds = DefaultDiagramTypeDefinitions.all().stream()
                .map(DiagramTypeOfficialDefinition::id)
                .collect(Collectors.toSet());
        Set<DiagramTypeId> runtimeIds = registry.findAll().stream()
                .map(DiagramTypeRuntimeDescriptor::id)
                .collect(Collectors.toSet());

        assertEquals(officialIds, runtimeIds);
        assertEquals(19, registry.availableTypes().size());
    }

    @Test
    void importMarkdownCapabilityShouldHaveMarkdownImportRuntimeContract() {
        DefaultDiagramTypeRuntimeRegistry registry = new DefaultDiagramTypeRuntimeRegistry();

        for (DiagramTypeRuntimeDescriptor descriptor : registry.availableTypes()) {
            if (descriptor.hasCapability(DiagramCapability.IMPORT_MARKDOWN)) {
                assertTrue(
                        descriptor.markdownImport().isPresent(),
                        descriptor.id().value() + " declara IMPORT_MARKDOWN sin contrato runtime.");
                assertEquals(
                        "DiagramMarkdownImportDispatcher",
                        descriptor.markdownImport().orElseThrow().dispatcherContract());
            } else {
                assertTrue(descriptor.markdownImport().isEmpty(), descriptor.id().value());
            }
        }
    }

    @Test
    void exportAndOutputCapabilitiesShouldHaveRuntimeContracts() {
        DefaultDiagramTypeRuntimeRegistry registry = new DefaultDiagramTypeRuntimeRegistry();

        for (DiagramTypeRuntimeDescriptor descriptor : registry.availableTypes()) {
            if (descriptor.hasCapability(DiagramCapability.EXPORT_MARKDOWN)) {
                assertTrue(descriptor.markdownExport().isPresent(), descriptor.id().value());
                assertTrue(descriptor.activeOutput().orElseThrow().exports(DiagramCapability.EXPORT_MARKDOWN));
            }
            if (descriptor.hasCapability(DiagramCapability.SHOW_VISUAL_OUTPUT)
                    || descriptor.hasCapability(DiagramCapability.SHOW_DOCUMENT_OUTPUT)) {
                assertTrue(descriptor.activeOutput().isPresent(), descriptor.id().value());
            }
        }
    }

    @Test
    void availableTypesShouldHaveWorkspaceAndSinglePrimaryExample() {
        DefaultDiagramTypeRuntimeRegistry registry = new DefaultDiagramTypeRuntimeRegistry();
        Map<DiagramTypeId, Long> examplesByType = registry.availableTypes().stream()
                .filter(descriptor -> descriptor.officialExample().isPresent())
                .collect(Collectors.groupingBy(DiagramTypeRuntimeDescriptor::id, Collectors.counting()));

        for (DiagramTypeRuntimeDescriptor descriptor : registry.availableTypes()) {
            assertEquals(DiagramSupportStatus.AVAILABLE, descriptor.supportStatus());
            assertTrue(descriptor.workspace().isPresent(), descriptor.id().value());
            assertEquals(1L, examplesByType.getOrDefault(descriptor.id(), 0L), descriptor.id().value());
        }
        assertEquals(1L, examplesByType.get(DiagramTypeId.LOGICAL_BUSINESS_INTAKE));
    }

    @Test
    void conceptualModelShouldBeRegularRuntimeTypeAfterWorkspaceMigration() {
        DefaultDiagramTypeRuntimeRegistry registry = new DefaultDiagramTypeRuntimeRegistry();
        DiagramTypeRuntimeDescriptor conceptual = registry.require(DiagramTypeId.CONCEPTUAL_MODEL);

        assertTrue(conceptual.available());
        assertTrue(conceptual.workspace().isPresent());
        assertTrue(conceptual.markdownImport().isPresent());
        assertTrue(conceptual.markdownExport().isPresent());
        assertFalse(conceptual.definition().toolbarPolicyId().isBlank());
    }
}
