package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 19: entidades, atributos y relaciones son candidatos visibles, no modelo físico final. */
class LogicalBusinessCandidateEntitiesVisibilitySourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void entitiesSideDockShouldExposeCandidatesAndNestedNavigation() throws IOException {
        String panel = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEntitiesPanel.java");

        assertTrue(panel.contains("Entidades, atributos y relaciones"));
        assertTrue(panel.contains("Candidatos lógicos del expediente"));
        assertTrue(panel.contains("no son tablas, columnas ni llaves físicas"));
        assertTrue(panel.contains("Resumen de candidatos"));
        assertTrue(panel.contains("searchField"));
        assertTrue(panel.contains("entityDisclosure"));
        assertTrue(panel.contains("matchesEntityTree"));
        assertTrue(panel.contains("Atributos candidatos"));
        assertTrue(panel.contains("Relaciones candidatas"));
        assertTrue(panel.contains("selectAttribute"));
        assertTrue(panel.contains("selectRelationship"));
        assertTrue(panel.contains("logical-business-candidate-card"));
        assertTrue(panel.contains("logical-business-attribute-button"));
        assertTrue(panel.contains("logical-business-relationship-button"));
        assertFalse(panel.contains("entidades candidatas derivadas"));
    }

    @Test
    void centralDocumentShouldExposeEntityChildrenAsCandidateLinks() throws IOException {
        String documentView = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDocumentView.java");
        String linkedRows = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessLinkedRows.java");

        assertTrue(documentView.contains("Entidad candidata del negocio"));
        assertTrue(documentView.contains("Todavía no es tabla final ni decisión física"));
        assertTrue(documentView.contains("metric(\"Atributos\""));
        assertTrue(documentView.contains("metric(\"Relaciones\""));
        assertTrue(documentView.contains("LogicalBusinessLinkedRows.addEntityCandidateChildren"));
        assertTrue(documentView.contains("Todavía no es columna final ni tipo físico definitivo"));
        assertTrue(documentView.contains("Todavía no es llave foránea ni cardinalidad física aprobada"));
        assertTrue(linkedRows.contains("Atributos candidatos navegables"));
        assertTrue(linkedRows.contains("Relaciones candidatas navegables"));
        assertTrue(linkedRows.contains("viewModel.selectAttribute"));
        assertTrue(linkedRows.contains("viewModel.selectRelationship"));
    }

    @Test
    void domainLanguageShouldKeepCandidatesSeparatedFromPhysicalModel() throws IOException {
        String document = readJava("com/marcosmoreira/domainmodelstudio/domain/logicalbusiness/LogicalBusinessDocument.java");
        String entity = readJava("com/marcosmoreira/domainmodelstudio/domain/logicalbusiness/LogicalBusinessEntityCandidate.java");
        String attribute = readJava("com/marcosmoreira/domainmodelstudio/domain/logicalbusiness/LogicalBusinessAttributeCandidate.java");
        String relationship = readJava("com/marcosmoreira/domainmodelstudio/domain/logicalbusiness/LogicalBusinessRelationshipCandidate.java");

        assertTrue(document.contains("fuente lógica canónica"));
        assertTrue(document.contains("sin prometer generación automática"));
        assertTrue(entity.contains("sin tratarlo todavía como tabla final"));
        assertTrue(attribute.contains("trazas internas"));
        assertTrue(relationship.contains("todavía no representa una llave física"));
        assertFalse(entity.contains("Entidad candidata derivada"));
        assertFalse(relationship.contains("entidades derivadas"));
    }

    @Test
    void cssShouldExposeStraightCandidateCards() throws IOException {
        String baseCss = Files.readString(RESOURCES.resolve("css/logical-business-side-panels.css"), StandardCharsets.UTF_8);
        String extraCss = Files.readString(RESOURCES.resolve("css/logical-business-side-panels-extra.css"), StandardCharsets.UTF_8);
        String assembler = Files.readString(RESOURCES.resolve("css/logical-business.css"), StandardCharsets.UTF_8);
        String css = baseCss + "\n" + extraCss;

        assertTrue(css.contains("logical-business-candidate-summary"));
        assertTrue(css.contains("logical-business-disclosure"));
        assertTrue(css.contains("logical-business-search-field"));
        assertTrue(css.contains("logical-business-candidate-card"));
        assertTrue(css.contains("logical-business-entity-candidate-card"));
        assertTrue(css.contains("logical-business-attribute-button"));
        assertTrue(css.contains("logical-business-relationship-button"));
        assertTrue(assembler.contains("logical-business-side-panels-extra.css"));
        assertFalse(css.contains("-fx-border-radius"));
        assertFalse(css.contains("-fx-background-radius"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
