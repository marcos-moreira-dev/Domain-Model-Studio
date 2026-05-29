package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl para mantener la importación UML desde código con una sola notificación de fallo. */
class ImportCommandHandlerSourceFailureStatusTest {

    private static final Path IMPORT_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java");

    @Test
    void sourceImportFailureShouldReportStatusOnlyOnce() throws Exception {
        String source = Files.readString(IMPORT_HANDLER, StandardCharsets.UTF_8);
        String method = between(
                source,
                "private void handleSourceImportFailure(Throwable throwable)",
                "private boolean isOutOfMemory(Throwable throwable)");

        assertEquals(1,
                count(method, "context.shellState().updateStatus(\"No se pudo importar código fuente: \" + message);"),
                "El fallo de importación UML desde código debe actualizar la barra de estado una sola vez.");
        assertEquals(1,
                count(method, "showErrorDialog(\"No se pudo importar código fuente\", message);"),
                "El fallo de importación UML desde código debe mostrar un único diálogo de error genérico.");
        assertTrue(method.contains("if (isOutOfMemory(throwable))"),
                "Los errores de memoria deben conservar una rama separada antes del error genérico.");
        assertTrue(method.contains("return;"),
                "La rama de memoria debe salir antes de emitir el estado genérico.");
    }

    private static String between(String source, String start, String end) {
        int from = source.indexOf(start);
        int to = source.indexOf(end, from + start.length());
        assertTrue(from >= 0, "No se encontró el inicio del método esperado.");
        assertTrue(to > from, "No se encontró el final del método esperado.");
        return source.substring(from, to);
    }

    private static int count(String text, String needle) {
        int matches = 0;
        int index = 0;
        while ((index = text.indexOf(needle, index)) >= 0) {
            matches++;
            index += needle.length();
        }
        return matches;
    }
}
