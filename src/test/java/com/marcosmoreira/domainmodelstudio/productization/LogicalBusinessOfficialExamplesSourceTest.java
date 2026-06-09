package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.OfficialAiResourceDescriptors;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de L0-B para mantener ejemplos ficticios y no personales del levantamiento lógico. */
class LogicalBusinessOfficialExamplesSourceTest {

    private static final Path TEMPLATE = Path.of(
            "src/main/resources/ai-resources/official-markdown/levantamiento-logico/"
                    + "logical_business_intake_template.md");
    private static final Path MINIMAL_EXAMPLE = Path.of(
            "src/main/resources/ai-resources/official-markdown/levantamiento-logico/"
                    + "logical_business_intake_optica_minimo.md");
    private static final Path COMPLETE_EXAMPLE = Path.of(
            "src/main/resources/ai-resources/official-markdown/levantamiento-logico/"
                    + "logical_business_intake_optica_gordito.md");
    private static final Path AI_DESCRIPTORS = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources/definitions/CoreAiResourceDescriptors.java");

    @Test
    void logicalBusinessTemplateShouldBeCanonicalAndExportable() throws IOException {
        String template = read(TEMPLATE);
        String descriptors = read(AI_DESCRIPTORS);

        assertTrue(template.contains("# Plantilla maestra lógica para levantamiento de negocio"));
        assertTrue(template.contains("estado inicial"));
        assertTrue(template.contains("acción transformadora"));
        assertTrue(template.contains("ENT-XXX"));
        assertTrue(template.contains("ATR-XXX"));
        assertTrue(template.contains("REL-XXX"));
        assertTrue(template.contains("SUP-XXX"));
        assertTrue(template.contains("ACT-XXX"));
        assertTrue(template.contains("EST-XXX"));
        assertTrue(template.contains("CON-XXX"));
        assertTrue(template.contains("EVID-XXX"));
        assertTrue(template.contains("CALC-XXX"));
        assertTrue(template.contains("Este MD es la fuente lógica; otros artefactos pueden reutilizar sus IDs"));
        assertTrue(template.contains("## 14. Entidades candidatas"));
        assertTrue(template.contains("Fuente lógica"));
        assertFalse(template.contains("diagramas son vistas derivadas"));
        assertFalse(template.contains("fuente madre"));
        assertTrue(template.lines().count() >= 320, "La plantilla oficial debe ser suficientemente completa.");

        assertTrue(descriptors.contains("plantilla-canonica-levantamiento-logico"));
        assertTrue(descriptors.contains("logical_business_intake_template.md"));
    }

    @Test
    void logicalBusinessExamplesShouldUseFictitiousOpticalBusiness() throws IOException {
        String minimal = read(MINIMAL_EXAMPLE);
        String complete = read(COMPLETE_EXAMPLE);

        assertTrue(minimal.contains("Óptica Horizonte"));
        assertTrue(complete.contains("Óptica Horizonte"));
        assertTrue(complete.contains("RN-001"));
        assertTrue(complete.contains("PRE-001"));
        assertTrue(complete.contains("INV-001"));
        assertTrue(complete.contains("POST-001"));
        assertTrue(complete.contains("ACC-001"));
        assertTrue(complete.contains("CU-001"));
        assertTrue(complete.contains("ENT-001"));
        assertTrue(complete.contains("ATR-001"));
        assertTrue(complete.contains("REL-001"));
        assertTrue(complete.contains("SUP-001"));
        assertTrue(complete.contains("CALC-001"));
        assertTrue(complete.contains("ACT-001"));
        assertTrue(complete.contains("EST-001"));
        assertTrue(complete.contains("CON-001"));
        assertTrue(complete.contains("EVID-001"));
        assertTrue(complete.contains("PEND-001"));
        assertTrue(complete.contains("canonical_contract: \"logical-business-master-v1\""));
        assertTrue(complete.contains("## 14. Entidades candidatas"));
        assertFalse(complete.contains("## 14. Entidades derivadas candidatas"));
        assertTrue(complete.lines().count() >= 450, "El ejemplo gordito debe tener suficiente profundidad.");
    }

    @Test
    void logicalBusinessExamplesShouldAvoidPersonalBusinessData() throws IOException {
        String joined = read(MINIMAL_EXAMPLE) + "\n" + read(COMPLETE_EXAMPLE);

        assertFalse(joined.contains("Marcos Moreira — proveedor"));
        assertFalse(joined.contains("programalobien"));
        assertFalse(joined.contains("marcosmorira"));
        assertFalse(joined.contains("marcosmoriradev"));
        assertFalse(joined.contains("Sistema administrativo de Marcos"));
    }

    @Test
    void logicalBusinessExamplesShouldBeExportableAndImportableAiResources() throws IOException {
        String descriptors = read(AI_DESCRIPTORS);
        List<AiResourceDescriptor> logicalResources = OfficialAiResourceDescriptors.all().stream()
                .filter(resource -> resource.diagramTypeId().equals(DiagramTypeId.LOGICAL_BUSINESS_INTAKE))
                .toList();

        assertTrue(descriptors.contains("AiResourceDescriptorFactory.logicalBusiness"));
        assertTrue(descriptors.contains("plantilla-canonica-levantamiento-logico"));
        assertTrue(descriptors.contains("ejemplo-levantamiento-logico-optica-minimo"));
        assertTrue(descriptors.contains("ejemplo-levantamiento-logico-optica-gordito"));
        assertTrue(logicalResources.size() >= 3, "Deben existir plantilla y ejemplos IA del levantamiento lógico.");

        for (AiResourceDescriptor resource : logicalResources) {
            assertTrue(resource.exportable(), resource.id() + " debe exportarse como recurso IA.");
            assertTrue(resource.importableByApplication(), resource.id() + " debe importarse desde Tanda 7.");
            assertTrue(resource.classpathLocation().contains("official-markdown/levantamiento-logico/"));
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
