package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl: el CRUD documental del levantamiento lógico es controlado, no Markdown libre. */
class LogicalBusinessCrudControlledSourceTest {

    private static final Path MAIN = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio");

    @Test
    void sideDockShouldExposeControlledCreationActions() throws IOException {
        String elements = read("presentation/logicalbusiness/LogicalBusinessElementsPanel.java");
        String entities = read("presentation/logicalbusiness/LogicalBusinessEntitiesPanel.java");

        assertTrue(elements.contains("Crear elemento"));
        assertTrue(elements.contains("Crear pregunta"));
        assertTrue(entities.contains("Crear entidad"));
        assertTrue(entities.contains("Crear atributo"));
        assertTrue(entities.contains("Crear relación"));
        assertTrue(elements.contains("LogicalBusinessCrudOperations"));
        assertTrue(entities.contains("LogicalBusinessCrudOperations"));
    }

    @Test
    void crudShouldUseTypedDialogsAndDomainObjects() throws IOException {
        String dialogs = read("presentation/logicalbusiness/LogicalBusinessCrudDialogs.java");
        String operations = read("presentation/logicalbusiness/LogicalBusinessCrudOperations.java");

        assertTrue(dialogs.contains("Dialog<ButtonType>"));
        assertTrue(dialogs.contains("ComboBox<LogicalBusinessItemKind>"));
        assertTrue(operations.contains("new LogicalBusinessItem"));
        assertTrue(operations.contains("new LogicalBusinessEntityCandidate"));
        assertTrue(operations.contains("new LogicalBusinessAttributeCandidate"));
        assertTrue(operations.contains("new LogicalBusinessRelationshipCandidate"));
        assertTrue(operations.contains("new LogicalBusinessPendingQuestion"));
        assertFalse(dialogs.contains("Markdown"), "El formulario no debe pedir Markdown crudo para crear elementos.");
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
