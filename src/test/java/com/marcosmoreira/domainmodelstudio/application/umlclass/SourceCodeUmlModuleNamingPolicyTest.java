package com.marcosmoreira.domainmodelstudio.application.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeModule;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SourceCodeUmlModuleNamingPolicyTest {

    @Test
    void shouldKeepModuleTitleCompactButPreservePackageAndPathInDescription() {
        ParsedCodeModule module = new ParsedCodeModule("backend:pedidos", "backend",
                "com.cedrodamasco.ventas.pedidos", "com.cedrodamasco.ventas.pedidos",
                Path.of("src/main/java/com/cedrodamasco/ventas/pedidos"));

        SourceCodeUmlModuleNamingPolicy policy = new SourceCodeUmlModuleNamingPolicy();

        assertEquals("pedidos", policy.displayName(module));
        assertTrue(policy.description(module).contains("com.cedrodamasco.ventas.pedidos"));
        assertTrue(policy.description(module).contains("src/main/java"));
    }
}
