package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UmlClassSourceFileResolverTest {

    @TempDir
    Path tempDir;

    @Test
    void resolvesAbsoluteSourcePathStoredInImportedClassDescription() throws Exception {
        Path source = tempDir.resolve("ClienteService.java");
        Files.writeString(source, "class ClienteService {}\n");
        UmlClassNode node = new UmlClassNode("cliente_service", "mod", "ClienteService", "demo",
                UmlClassKind.SERVICE, UmlVisibility.PUBLIC, "", "Origen: java; ruta: src/main/java/demo/ClienteService.java; ruta absoluta: "
                + source + "; nombre completo: demo.ClienteService", List.of(), "");

        assertEquals(source, new UmlClassSourceFileResolver().resolve(node).orElseThrow());
    }

    @Test
    void resolvesRootRelativeSourcePathWhenAbsoluteFieldIsNotAvailable() throws Exception {
        Path source = tempDir.resolve("src/main/java/demo/Cliente.java");
        Files.createDirectories(source.getParent());
        Files.writeString(source, "class Cliente {}\n");
        UmlClassNode node = new UmlClassNode("cliente", "mod", "Cliente", "demo",
                UmlClassKind.CLASS, UmlVisibility.PUBLIC, "", "Origen: java; ruta: src/main/java/demo/Cliente.java; nombre completo: demo.Cliente",
                List.of(), "Source root path: " + tempDir);

        assertTrue(new UmlClassSourceFileResolver().resolve(node).isPresent());
    }
}
