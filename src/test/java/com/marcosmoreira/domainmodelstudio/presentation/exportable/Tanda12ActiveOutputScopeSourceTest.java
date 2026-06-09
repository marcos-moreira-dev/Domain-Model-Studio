package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de la tanda 12: la exportación activa debe seguir a la pestaña activa. */
class Tanda12ActiveOutputScopeSourceTest {

    private static final Path ACTIVE_OUTPUT_RESOLVER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ActiveOutputResolver.java");
    private static final Path CONTRIBUTOR_REGISTRY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ActiveOutputContributorRegistry.java");
    private static final Path CONTRIBUTOR_SUPPORT = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ActiveOutputContributorSupport.java");

    private static final List<Path> SCOPED_CONTRIBUTORS = List.of(
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ConceptualActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/DataDictionaryActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ModuleMapActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/UmlClassActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/RolesPermissionsActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ScreenFlowActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/WireframeActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/BehaviorActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ArchitectureActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/FreeGraphActiveOutputContributor.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/LogicalBusinessActiveOutputContributor.java")
    );

    @Test
    void resolverMustAskForTheActiveProjectOnEveryResolution() throws IOException {
        String source = read(ACTIVE_OUTPUT_RESOLVER);

        assertTrue(source.contains("private final Supplier<Optional<DiagramProject>> activeProjectSupplier;"));
        assertTrue(source.contains("return activeProjectSupplier.get()"));
        assertTrue(source.contains(".flatMap(activeProject -> contributorRegistry.resolve(activeProject, exportFormatPolicy))"));
    }

    @Test
    void registryMustRouteByTheActiveProjectTypeAndPassTheSameProjectToTheContributor() throws IOException {
        String source = read(CONTRIBUTOR_REGISTRY);

        assertTrue(source.contains("DiagramTypeId diagramTypeId = activeProject.metadata().diagramTypeId();"));
        assertTrue(source.contains(".filter(contributor -> contributor.supports(diagramTypeId))"));
        assertTrue(source.contains(".flatMap(contributor -> contributor.resolve(activeProject, exportFormatPolicy))"));
        assertTrue(source.contains("new FreeGraphActiveOutputContributor(freeGraphViewModel)"));
        assertTrue(source.contains("new LogicalBusinessActiveOutputContributor(logicalBusinessViewModel)"));
    }

    @Test
    void contributorsMustRejectResidualEditorsFromInactiveTabs() throws IOException {
        for (Path contributor : SCOPED_CONTRIBUTORS) {
            String source = read(contributor);
            assertTrue(source.contains("ActiveOutputContributorSupport.sameActiveProject"),
                    contributor + " debe validar id y tipo contra el proyecto activo.");
        }
    }

    @Test
    void activeOutputProjectComparisonMustUseBothProjectIdAndDiagramType() throws IOException {
        String source = read(CONTRIBUTOR_SUPPORT);

        assertTrue(source.contains("candidate.metadata().id()"));
        assertTrue(source.contains("activeProject.metadata().id()"));
        assertTrue(source.contains("candidate.metadata().diagramTypeId()"));
        assertTrue(source.contains("activeProject.metadata().diagramTypeId()"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
