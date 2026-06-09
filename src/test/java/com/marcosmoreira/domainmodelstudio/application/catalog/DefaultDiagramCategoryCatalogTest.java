package com.marcosmoreira.domainmodelstudio.application.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategory;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import java.util.List;
import org.junit.jupiter.api.Test;

class DefaultDiagramCategoryCatalogTest {

    @Test
    void shouldExposeOfficialCategoriesInStableOrder() {
        DiagramCategoryCatalog catalog = new DefaultDiagramCategoryCatalog();

        List<DiagramCategory> categories = catalog.findAll();

        assertEquals(9, categories.size());
        assertEquals(DiagramCategoryId.BUSINESS_ANALYSIS, categories.get(0).id());
        assertEquals(DiagramCategoryId.DATA_MODELING, categories.get(1).id());
        assertEquals(DiagramCategoryId.BUSINESS_PROCESS, categories.get(2).id());
        assertEquals(DiagramCategoryId.SOFTWARE_ARCHITECTURE, categories.get(3).id());
        assertEquals(DiagramCategoryId.UML_STRUCTURAL, categories.get(4).id());
        assertEquals(DiagramCategoryId.UML_BEHAVIOR, categories.get(5).id());
        assertEquals(DiagramCategoryId.UML_INTERACTION, categories.get(6).id());
        assertEquals(DiagramCategoryId.ADMIN_APPLICATIONS, categories.get(7).id());
        assertEquals(DiagramCategoryId.TECHNICAL_DOCUMENTATION, categories.get(8).id());
    }

    @Test
    void shouldFindCategoryById() {
        DiagramCategoryCatalog catalog = new DefaultDiagramCategoryCatalog();

        assertTrue(catalog.findById(DiagramCategoryId.BUSINESS_ANALYSIS).isPresent());
        assertTrue(catalog.findById(DiagramCategoryId.DATA_MODELING).isPresent());
        assertFalse(catalog.findById(DiagramCategoryId.of("unknown-category")).isPresent());
    }

    @Test
    void shouldRejectDuplicateCategoryIds() {
        DiagramCategory category = new DiagramCategory(
                DiagramCategoryId.DATA_MODELING,
                "Modelado de datos",
                "Propósito de prueba",
                10);

        assertThrows(IllegalArgumentException.class,
                () -> new DefaultDiagramCategoryCatalog(List.of(category, category)));
    }
}
