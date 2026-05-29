package com.marcosmoreira.domainmodelstudio.application.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 28: la fachada global queda como compatibilidad, no como depósito monolítico. */
class ApplicationServicesTanda28RefactorSourceTest {

    private static final Path APPLICATION_SERVICES = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/ApplicationServices.java");
    private static final Path PLAN = Path.of("docs/desarrollo/refactor/PLAN_REFACTOR_SOLID.md");
    private static final Path TANDA = Path.of("docs/desarrollo/TANDA_028_REFACTOR_APPLICATION_SERVICES.md");

    @Test
    void applicationServicesShouldBeComposedByFamilyFacades() throws Exception {
        String source = Files.readString(APPLICATION_SERVICES);
        assertAll(
                () -> assertTrue(source.contains("public ApplicationServices(\n            ProjectApplicationServices projectServices")),
                () -> assertTrue(source.contains("private final ConceptualModelApplicationServices conceptualModelServices")),
                () -> assertTrue(source.contains("private final DataDictionaryApplicationServices dataDictionaryServices")),
                () -> assertTrue(source.contains("private final ModuleMapApplicationServices moduleMapServices")),
                () -> assertTrue(source.contains("private final RolesPermissionsApplicationServices rolesPermissionsServices")),
                () -> assertTrue(source.contains("private final ScreenFlowApplicationServices screenFlowServices")),
                () -> assertTrue(source.contains("private final WireframeApplicationServices wireframeServices")),
                () -> assertTrue(source.contains("private final BehaviorApplicationServices behaviorServices")),
                () -> assertTrue(source.contains("private final ArchitectureApplicationServices architectureServices")));
    }

    @Test
    void familyFacadesShouldDocumentTheirBoundaries() throws Exception {
        String conceptual = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/services/ConceptualModelApplicationServices.java"));
        String dictionary = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/services/DataDictionaryApplicationServices.java"));
        String uml = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/services/UmlClassApplicationServices.java"));
        assertAll(
                () -> assertTrue(conceptual.contains("sin mezclar layout, estilo visual ni persistencia")),
                () -> assertTrue(dictionary.contains("exportación PDF/Markdown sigue viviendo")),
                () -> assertTrue(uml.contains("generación desde código fuente queda en importación")));
    }

    @Test
    void documentationShouldMarkTanda28AsApplied() throws Exception {
        String plan = Files.readString(PLAN);
        String tanda = Files.readString(TANDA);
        assertAll(
                () -> assertTrue(plan.contains("Estado: **aplicada en Tanda 28**")),
                () -> assertTrue(tanda.contains("ApplicationServices queda como fachada de compatibilidad")),
                () -> assertTrue(tanda.contains("sin cambiar comportamiento visible")));
    }
}
