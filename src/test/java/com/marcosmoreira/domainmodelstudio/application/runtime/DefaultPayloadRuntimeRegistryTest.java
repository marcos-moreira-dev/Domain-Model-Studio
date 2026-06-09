package com.marcosmoreira.domainmodelstudio.application.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.application.modulemap.CreateModuleMapUseCase;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class DefaultPayloadRuntimeRegistryTest {

    @Test
    void shouldExposePayloadDescriptorForEveryOfficialType() {
        DefaultPayloadRuntimeRegistry registry = new DefaultPayloadRuntimeRegistry();
        Set<DiagramTypeId> officialIds = DefaultDiagramTypeDefinitions.all().stream()
                .map(DiagramTypeOfficialDefinition::id)
                .collect(Collectors.toSet());
        Set<DiagramTypeId> payloadIds = registry.findAll().stream()
                .map(PayloadRuntimeDescriptor::diagramTypeId)
                .collect(Collectors.toSet());

        assertEquals(officialIds, payloadIds);
        assertEquals(19, registry.findAll().size());
    }

    @Test
    void conceptualModelShouldHaveCorePayloadDescriptorButNotSpecializedPayload() {
        PayloadRuntimeDescriptor descriptor = new DefaultPayloadRuntimeRegistry()
                .require(DiagramTypeId.CONCEPTUAL_MODEL);

        assertEquals("conceptual-er-model", descriptor.payloadKind());
        assertFalse(descriptor.specializedPayload());
        assertTrue(descriptor.presentIn(DiagramProject.blank("conceptual", "Conceptual")));
    }

    @Test
    void specializedPayloadDetectionShouldReturnOnlyActualPayloadTypes() {
        DiagramProject moduleMap = DiagramProject.blank("modulos", "Mapa", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(new CreateModuleMapUseCase().createBlank("Mapa"));

        assertEquals(
                ListOf.of(DiagramTypeId.ADMIN_MODULE_MAP),
                new DefaultPayloadRuntimeRegistry().detectSpecializedPayloadTypeIds(moduleMap));
    }

    @Test
    void runtimeDescriptorsShouldIncludePayloadContract() {
        DefaultDiagramTypeRuntimeRegistry registry = new DefaultDiagramTypeRuntimeRegistry();

        for (DiagramTypeRuntimeDescriptor descriptor : registry.availableTypes()) {
            assertTrue(descriptor.payload().isPresent(), descriptor.id().value());
            assertEquals(descriptor.id(), descriptor.payload().orElseThrow().diagramTypeId());
        }
    }

    /** Evita depender de imports estáticos en source tests antiguos con presupuestos estrictos. */
    private static final class ListOf {
        private static java.util.List<DiagramTypeId> of(DiagramTypeId id) {
            return java.util.List.of(id);
        }
    }
}
