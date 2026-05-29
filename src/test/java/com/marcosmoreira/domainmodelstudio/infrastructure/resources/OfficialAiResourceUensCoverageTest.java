package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.examples.DefaultOfficialExampleCatalog;
import com.marcosmoreira.domainmodelstudio.application.examples.OfficialExampleDescriptor;
import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Verifica que los ejemplos UENS del selector también viajen en el paquete de recursos IA. */
class OfficialAiResourceUensCoverageTest {

    @Test
    void everyOfficialExampleShouldBeExportableAsAiResource() {
        Map<String, AiResourceDescriptor> resourcesByPath = OfficialAiResourceDescriptors.all().stream()
                .collect(Collectors.toMap(
                        AiResourceDescriptor::classpathLocation,
                        Function.identity(),
                        (left, right) -> left));

        for (OfficialExampleDescriptor example : new DefaultOfficialExampleCatalog().findAll()) {
            AiResourceDescriptor resource = resourcesByPath.get(example.classpathLocation());

            assertTrue(resource != null, "Falta recurso IA para ejemplo oficial: " + example.classpathLocation());
            assertEquals(example.diagramTypeId(), resource.diagramTypeId());
            assertTrue(resource.exportable(), example.id() + " debe exportarse en recursos IA.");
            assertTrue(resource.importableByApplication(), example.id() + " debe conservar estado importable.");
        }
    }

    @Test
    void shouldKeepOneUensAiExamplePerOfficialUensType() {
        Set<String> uensResourceTypes = OfficialAiResourceDescriptors.all().stream()
                .filter(OfficialAiResourceUensCoverageTest::isUensResource)
                .map(resource -> resource.diagramTypeId().value())
                .collect(Collectors.toSet());

        Set<String> catalogTypes = new DefaultOfficialExampleCatalog().findAll().stream()
                .filter(example -> !isOpticaExample(example))
                .map(example -> example.diagramTypeId().value())
                .collect(Collectors.toSet());

        assertEquals(catalogTypes, uensResourceTypes);
    }

    @Test
    void shouldKeepLogicalBusinessUensAndOpticaExamplesAsAiResources() {
        Set<String> logicalResourceIds = OfficialAiResourceDescriptors.all().stream()
                .filter(resource -> resource.diagramTypeId().value().equals("logical-business-intake"))
                .map(AiResourceDescriptor::id)
                .collect(Collectors.toSet());

        assertTrue(logicalResourceIds.contains("plantilla-canonica-levantamiento-logico"));
        assertTrue(logicalResourceIds.contains("ejemplo-levantamiento-logico-optica-minimo"));
        assertTrue(logicalResourceIds.contains("ejemplo-levantamiento-logico-optica-gordito"));
        assertTrue(logicalResourceIds.contains("ejemplo-levantamiento-logico-uens-gordito"));
    }

    private static boolean isUensResource(AiResourceDescriptor resource) {
        return resource.id().startsWith("ejemplo-uens-")
                || resource.id().equals("ejemplo-levantamiento-logico-uens-gordito");
    }

    private static boolean isOpticaExample(OfficialExampleDescriptor example) {
        String searchable = (example.id() + " " + example.title() + " " + example.sourceName())
                .toLowerCase(Locale.ROOT);
        return searchable.contains("optica") || searchable.contains("óptica");
    }
}
