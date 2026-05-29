package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.OfficialAiResourceDescriptors;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl anti-fachada: Recursos IA no deben contradecir parser/catálogo/importabilidad. */
class Tanda32AiResourcesAntiFacadeTest {

    private final DefaultDiagramTypeRegistry registry = new DefaultDiagramTypeRegistry();

    @Test
    void everyExportableAiResourceShouldExistAndHaveARegisteredType() {
        for (AiResourceDescriptor resource : OfficialAiResourceDescriptors.all()) {
            assertTrue(
                    registry.findById(resource.diagramTypeId()).isPresent(),
                    () -> "Recurso IA apunta a tipo no registrado: " + resource.id());
            if (resource.exportable()) {
                assertTrue(
                        Files.exists(resourcePath(resource)),
                        () -> "Recurso IA exportable sin archivo classpath: " + resource.id() + " -> " + resource.classpathLocation());
            }
        }
    }

    @Test
    void importableAiResourcesShouldPointToTypesWithMarkdownImportCapability() {
        for (AiResourceDescriptor resource : OfficialAiResourceDescriptors.all()) {
            if (!resource.importableByApplication()) {
                continue;
            }
            boolean supportsMarkdownImport = registry.findById(resource.diagramTypeId())
                    .orElseThrow()
                    .supports(DiagramCapability.IMPORT_MARKDOWN);
            assertTrue(
                    supportsMarkdownImport,
                    () -> "Recurso importable apunta a tipo sin IMPORT_MARKDOWN: " + resource.id());
        }
    }

    @Test
    void resourceTextShouldNotSayImportableWhenDescriptorSaysNotImportable() throws IOException {
        for (AiResourceDescriptor resource : OfficialAiResourceDescriptors.all()) {
            if (!resource.exportable()) {
                continue;
            }
            String text = Files.readString(resourcePath(resource), StandardCharsets.UTF_8);
            boolean textClaimsImportable = text.contains("Importable por la app: sí")
                    || text.contains("importable: true");
            if (textClaimsImportable) {
                assertTrue(
                        resource.importableByApplication(),
                        () -> "El texto dice importable, pero el descriptor lo marca no importable: " + resource.id());
            }
            if (!resource.importableByApplication()) {
                assertFalse(
                        text.contains("Importable por la app: sí"),
                        () -> "Descriptor no importable con texto de importabilidad positiva: " + resource.id());
            }
        }
    }

    @Test
    void everyMarkdownImportableTypeShouldHaveAtLeastOneImportableAiResource() {
        List<AiResourceDescriptor> resources = OfficialAiResourceDescriptors.all();
        registry.findAll().stream()
                .filter(type -> type.supports(DiagramCapability.IMPORT_MARKDOWN))
                .forEach(type -> assertTrue(
                        resources.stream().anyMatch(resource -> resource.diagramTypeId().equals(type.id())
                                && resource.importableByApplication()),
                        () -> "Tipo importable sin recurso IA importable: " + type.id().value()));
    }

    private static Path resourcePath(AiResourceDescriptor resource) {
        return Path.of("src", "main", "resources", resource.classpathLocation());
    }
}
