package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassSourceFileResolutionTest {

    @Test
    void explainsWhenSourceMetadataIsMissing() {
        UmlClassNode node = nodeWithDescription("Clase manual", "", "");

        UmlClassSourceFileResolution resolution = new UmlClassSourceFileResolver().inspect(node);

        assertEquals(UmlClassSourceFileResolution.Status.NO_SOURCE_METADATA, resolution.status());
        assertFalse(resolution.resolved());
        assertTrue(resolution.userMessage("Cliente").contains("no tiene metadatos de archivo fuente"));
    }

    @Test
    void reportsMissingCandidatePathWhenImportedFileWasMoved() {
        Path missing = Path.of(System.getProperty("user.home", "/tmp")).resolve("archivo-inexistente-dms.java");
        UmlClassNode node = nodeWithDescription("Clase movida", "Origen: java; ruta absoluta: " + missing, "");

        UmlClassSourceFileResolution resolution = new UmlClassSourceFileResolver().inspect(node);

        assertEquals(UmlClassSourceFileResolution.Status.CANDIDATE_NOT_FOUND, resolution.status());
        assertTrue(resolution.candidates().contains(missing.normalize()));
        assertTrue(resolution.userMessage("Cliente").contains("ya no existe"));
    }

    @Test
    void reportsIncompleteRootRelativeMetadata() {
        UmlClassNode node = nodeWithDescription("Clase incompleta", "Origen: java; ruta: src/Cliente.java", "");

        UmlClassSourceFileResolution resolution = new UmlClassSourceFileResolver().inspect(node);

        assertEquals(UmlClassSourceFileResolution.Status.INCOMPLETE_ROOT_RELATIVE_METADATA, resolution.status());
        assertTrue(resolution.userMessage("Cliente").contains("metadatos incompletos"));
    }

    private static UmlClassNode nodeWithDescription(String name, String description, String notes) {
        return new UmlClassNode("cliente", "mod", name, "demo", UmlClassKind.CLASS, UmlVisibility.PUBLIC,
                "", description, List.of(), notes);
    }
}
