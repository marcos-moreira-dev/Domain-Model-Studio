package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassSearchIndexSourceTest {

    private static final Path UML_CLASS_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass"
    );

    @Test
    void viewModelShouldBuildSearchIndexOncePerDocumentAndReuseItInFilters() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("private UmlClassSearchIndex searchIndex"),
                "El ViewModel debe conservar un índice de búsqueda reutilizable por documento UML.");
        assertTrue(source.contains("rebuildSearchIndex()"),
                "El índice debe reconstruirse explícitamente cuando cambia el documento.");
        assertTrue(source.contains("filterEngine.apply(currentDocument, filterState(), searchIndex)"),
                "El filtrado normal debe reutilizar el índice precomputado.");
        assertTrue(source.contains("relationKindFilter.get()), searchIndex)"),
                "La protección de Mega vista debe estimar usando el mismo índice.");
    }

    @Test
    void filterEngineShouldOfferIndexedApplyOverload() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramFilterEngine.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("UmlClassSearchIndex searchIndex"),
                "El motor de filtros debe aceptar un índice de búsqueda ya construido.");
        assertTrue(source.contains("searchIndex == null ? UmlClassSearchIndex.from(document) : searchIndex"),
                "Debe existir fallback seguro cuando no se pase índice.");
        assertTrue(!source.contains("node.members().stream().anyMatch"),
                "La búsqueda no debe recorrer miembros clase por clase en cada pulsación.");
    }
}
