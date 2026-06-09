package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 30 para recursos IA oficiales divididos por familias. */
class OfficialAiResourceDescriptorFamiliesSourceTest {

    private static final Path RESOURCES = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources");
    private static final Path DEFINITIONS = RESOURCES.resolve("definitions");

    @Test
    void officialAiResourceDescriptorsShouldRemainSmallAndDelegateToFamilies() throws IOException {
        String source = Files.readString(RESOURCES.resolve("OfficialAiResourceDescriptors.java"));

        assertTrue(source.contains("CoreAiResourceDescriptors.all()"));
        assertTrue(source.contains("OfficialTemplateAiResourceDescriptors.all()"));
        assertTrue(source.contains("OfficialMinimalExampleAiResourceDescriptors.all()"));
        assertTrue(source.contains("OfficialUensExampleAiResourceDescriptors.all()"));
        assertTrue(source.contains("LogicalBusinessGraphAiResourceDescriptors.all()"));
        assertTrue(Files.readAllLines(RESOURCES.resolve("OfficialAiResourceDescriptors.java")).size() < 80,
                "El agregador de recursos IA no debe volver a ser monolítico.");
    }

    @Test
    void resourcesShouldKeepUniqueIdsAndExistingCoverage() {
        List<AiResourceDescriptor> resources = OfficialAiResourceDescriptors.all();
        Set<String> ids = resources.stream().map(AiResourceDescriptor::id).collect(Collectors.toSet());

        assertEquals(resources.size(), ids.size());
        assertTrue(ids.contains("plantilla-canonica-levantamiento-logico"));
        assertTrue(ids.contains("ejemplo-levantamiento-logico-uens-gordito"));
        assertTrue(ids.contains("ejemplo-uens-grafo-logico-negocio"));
        assertTrue(resources.stream().anyMatch(resource -> DiagramTypeId.OPERATIONAL_FLOW.equals(resource.diagramTypeId())
                && resource.id().equals("flujo-operativo-gramatica")));
    }

    @Test
    void coreLogicalBusinessResourcesShouldStayExportableAndImportable() throws IOException {
        String core = Files.readString(DEFINITIONS.resolve("CoreAiResourceDescriptors.java"));
        String factory = Files.readString(DEFINITIONS.resolve("AiResourceDescriptorFactory.java"));

        assertTrue(core.contains("AiResourceDescriptorFactory.logicalBusiness"));
        assertTrue(core.contains("logical_business_intake_template.md"));
        assertTrue(core.contains("logical_business_intake_uens_gordito.md"));
        assertTrue(factory.contains("DiagramTypeId.LOGICAL_BUSINESS_INTAKE"));
        assertTrue(factory.contains("official-markdown/levantamiento-logico"));
    }

    @Test
    void uensFamilyShouldStaySeparateFromMinimalFixtures() throws IOException {
        String uens = Files.readString(DEFINITIONS.resolve("OfficialUensExampleAiResourceDescriptors.java"));
        String minimal = Files.readString(DEFINITIONS.resolve("OfficialMinimalExampleAiResourceDescriptors.java"));

        assertTrue(uens.contains("conceptual_model_uens_gordito_importable.md"));
        assertTrue(uens.contains("uml_sequence_registrar_calificacion_uens_gordito.md"));
        assertFalse(uens.contains("restaurante_minimo"));
        assertTrue(minimal.contains("uml_class_restaurante_minimo.md"));
    }
}
