package com.marcosmoreira.domainmodelstudio.application.theory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import org.junit.jupiter.api.Test;

class DefaultTheoryCatalogFigureCoverageTest {

    @Test
    void eachVisibleDiagramTypeShouldExposeAtLeastOneDidacticFigure() {
        DefaultTheoryCatalog theoryCatalog = new DefaultTheoryCatalog();
        DefaultTheoryFigureCatalog figureCatalog = new DefaultTheoryFigureCatalog();

        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            TheoryTopic topic = theoryCatalog.findByDiagramType(type.id()).orElseThrow();
            boolean hasRegisteredFigure = topic.sections().stream()
                    .flatMap(section -> section.figures().stream())
                    .allMatch(figure -> figureCatalog.contains(figure.figureId()));
            boolean hasAnyFigure = topic.sections().stream().anyMatch(TheorySection::hasFigures);

            assertTrue(hasAnyFigure, "El tema de " + type.displayName() + " no tiene figura didáctica.");
            assertTrue(hasRegisteredFigure, "El tema de " + type.displayName() + " referencia figuras no registradas.");
        }
    }
}
