package com.marcosmoreira.domainmodelstudio.application.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 30 para conservar el catálogo oficial dividido por familias. */
class CatalogDefinitionsByFamilySourceTest {

    private static final Path CATALOG = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog");
    private static final Path DEFINITIONS = CATALOG.resolve("definitions");

    @Test
    void defaultCatalogShouldRemainSmallAndDelegateToFamilies() throws IOException {
        String source = Files.readString(CATALOG.resolve("DefaultDiagramTypeDefinitions.java"));

        assertTrue(source.contains("BusinessAnalysisDiagramTypeDefinitions.all()"));
        assertTrue(source.contains("DataModelDiagramTypeDefinitions.all()"));
        assertTrue(source.contains("BusinessProcessDiagramTypeDefinitions.all()"));
        assertTrue(source.contains("ArchitectureDiagramTypeDefinitions.all()"));
        assertTrue(source.contains("UmlDiagramTypeDefinitions.all()"));
        assertTrue(source.contains("AdministrativeDiagramTypeDefinitions.all()"));
        assertTrue(source.contains("TechnicalDocumentationDiagramTypeDefinitions.all()"));
        assertTrue(Files.readAllLines(CATALOG.resolve("DefaultDiagramTypeDefinitions.java")).size() < 80,
                "El agregador no debe volver a ser un catálogo monolítico.");
    }

    @Test
    void familiesShouldKeepNineteenOfficialTypesAndUniqueIds() {
        Set<DiagramTypeId> ids = DefaultDiagramTypeDefinitions.all().stream()
                .map(DiagramTypeOfficialDefinition::id)
                .collect(Collectors.toSet());

        assertEquals(19, DefaultDiagramTypeDefinitions.all().size());
        assertEquals(19, ids.size());
        assertTrue(ids.contains(DiagramTypeId.LOGICAL_BUSINESS_INTAKE));
        assertTrue(ids.contains(DiagramTypeId.LOGICAL_BUSINESS_GRAPH));
        assertTrue(ids.contains(DiagramTypeId.UML_CLASS));
        assertTrue(ids.contains(DiagramTypeId.ADMIN_WIREFRAMES));
        assertTrue(ids.contains(DiagramTypeId.FREE_GRAPH));
    }

    @Test
    void capabilityProfilesShouldKeepLogicalBusinessDocumentDocumentalOnly() throws IOException {
        String profiles = Files.readString(DEFINITIONS.resolve("DiagramCapabilityProfiles.java"));
        String logical = slice(profiles, "public static DiagramCapabilitySet logicalBusinessDocument", "public static DiagramCapabilitySet logicalBusinessGraphVisual");

        assertTrue(logical.contains("SHOW_DOCUMENT_OUTPUT"));
        assertTrue(logical.contains("IMPORT_MARKDOWN"));
        assertTrue(logical.contains("EXPORT_MARKDOWN"));
        assertFalse(logical.contains("EXPORT_PNG"));
        assertFalse(logical.contains("EXPORT_SVG"));
        assertFalse(logical.contains("EXPORT_PDF"));
    }

    @Test
    void uensAndSourceCodeContractsShouldRemainInTheCorrectFamilies() throws IOException {
        String factory = Files.readString(DEFINITIONS.resolve("DiagramTypeDefinitionFactory.java"));
        String business = Files.readString(DEFINITIONS.resolve("BusinessAnalysisDiagramTypeDefinitions.java"));
        String process = Files.readString(DEFINITIONS.resolve("BusinessProcessDiagramTypeDefinitions.java"));
        String uml = Files.readString(DEFINITIONS.resolve("UmlDiagramTypeDefinitions.java"));

        assertTrue(factory.contains("logical_business_intake_uens_gordito.md"));
        assertTrue(business.contains("logical_business_graph_uens_gordito.md"));
        assertTrue(process.contains("flujo-operativo-gramatica"));
        assertTrue(uml.contains("DiagramCapabilityProfiles.umlClass()"));
    }

    @Test
    void everyFamilyFileShouldStayHumanReviewable() throws IOException {
        for (Path file : List.of(
                DEFINITIONS.resolve("BusinessAnalysisDiagramTypeDefinitions.java"),
                DEFINITIONS.resolve("DataModelDiagramTypeDefinitions.java"),
                DEFINITIONS.resolve("BusinessProcessDiagramTypeDefinitions.java"),
                DEFINITIONS.resolve("ArchitectureDiagramTypeDefinitions.java"),
                DEFINITIONS.resolve("UmlDiagramTypeDefinitions.java"),
                DEFINITIONS.resolve("AdministrativeDiagramTypeDefinitions.java"),
                DEFINITIONS.resolve("TechnicalDocumentationDiagramTypeDefinitions.java"))) {
            assertTrue(Files.readAllLines(file).size() < 180, file + " no debe crecer como nuevo monolito.");
        }
    }

    private static String slice(String text, String startToken, String endToken) {
        int start = text.indexOf(startToken);
        int end = text.indexOf(endToken, start + startToken.length());
        if (start < 0 || end < 0) {
            return text;
        }
        return text.substring(start, end);
    }
}
