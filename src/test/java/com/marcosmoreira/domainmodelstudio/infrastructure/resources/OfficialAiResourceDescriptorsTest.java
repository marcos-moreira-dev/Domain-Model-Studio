package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class OfficialAiResourceDescriptorsTest {

    @Test
    void shouldProvideClasspathFileForEachExportableResource() {
        ClassLoader classLoader = OfficialAiResourceDescriptorsTest.class.getClassLoader();

        for (AiResourceDescriptor resource : OfficialAiResourceDescriptors.all()) {
            assertNotNull(
                    classLoader.getResource(resource.classpathLocation()),
                    "No existe recurso classpath: " + resource.classpathLocation());
        }
    }

    @Test
    void futureResourcesShouldNotBeMarkedAsImportableByApplication() {
        for (AiResourceDescriptor resource : OfficialAiResourceDescriptors.all()) {
            if (!resource.diagramTypeId().value().equals("conceptual-model")
                    && !resource.diagramTypeId().value().equals("data-dictionary")
                    && !resource.diagramTypeId().value().equals("admin-module-map")
                    && !resource.diagramTypeId().value().equals("roles-permissions-map")
                    && !resource.diagramTypeId().value().equals("screen-flow")
                    && !resource.diagramTypeId().value().equals("admin-wireframes")
                    && !resource.diagramTypeId().value().equals("uml-class")
                    && !resource.diagramTypeId().value().equals("c4-context")
                    && !resource.diagramTypeId().value().equals("c4-containers")
                    && !resource.diagramTypeId().value().equals("technical-deployment")
                    && !resource.diagramTypeId().value().equals("bpmn-basic")
                    && !resource.diagramTypeId().value().equals("operational-flow")
                    && !resource.diagramTypeId().value().equals("uml-use-case")
                    && !resource.diagramTypeId().value().equals("uml-activity")
                    && !resource.diagramTypeId().value().equals("uml-sequence")
                    && !resource.diagramTypeId().value().equals("uml-state")
                    && !resource.diagramTypeId().value().equals("free-graph")
                    && !resource.diagramTypeId().value().equals("logical-business-graph")
                    && !resource.diagramTypeId().value().equals("logical-business-intake")) {
                assertFalse(resource.importableByApplication(), resource.id() + " no debe figurar como importable.");
            }
        }
    }

    @Test
    void diagramTypesWithGrammarResourceIdShouldHaveRegisteredAiResource() {
        Set<String> resourceIds = OfficialAiResourceDescriptors.all().stream()
                .map(AiResourceDescriptor::id)
                .collect(Collectors.toSet());

        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            if (!type.grammarResourceId().isBlank()) {
                assertTrue(
                        resourceIds.contains(type.grammarResourceId()),
                        type.id().value() + " referencia recurso IA no registrado: " + type.grammarResourceId());
            }
        }
    }
}
