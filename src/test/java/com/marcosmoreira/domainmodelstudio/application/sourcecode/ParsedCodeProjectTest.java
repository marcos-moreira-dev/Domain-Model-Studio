package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ParsedCodeProjectTest {

    @Test
    void parsedProjectShouldKeepTypesSeparatedBySourceRoot() {
        ParsedCodeSourceRoot backend = root("backend", SourceRootKind.BACKEND, SourceLanguage.JAVA);
        ParsedCodeSourceRoot frontend = root("frontend", SourceRootKind.FRONTEND, SourceLanguage.TYPESCRIPT);
        ParsedCodeType service = type("backend", "PedidoService", SourceLanguage.JAVA);
        ParsedCodeType component = type("frontend", "PedidoComponent", SourceLanguage.TYPESCRIPT);

        ParsedCodeProject project = new ParsedCodeProject("Cedro Damasco", List.of(backend, frontend),
                List.of(), List.of(service, component), List.of(), List.of());

        assertEquals(1, project.typesForRoot("backend").size());
        assertEquals(1, project.typesForRoot("frontend").size());
        assertEquals("PedidoService", project.typesForRoot("backend").getFirst().simpleName());
    }

    @Test
    void parsedTypeShouldExposeSafeTruncatedDisplayName() {
        ParsedCodeType type = type("backend", "ProductoAdministracionAvanzadaService", SourceLanguage.JAVA);

        assertEquals("ProductoAdminis...", type.displayName(17));
        assertEquals("ProductoAdministracionAvanzadaService", type.displayName(80));
    }

    private static ParsedCodeSourceRoot root(String id, SourceRootKind kind, SourceLanguage language) {
        return new ParsedCodeSourceRoot(id, id, Path.of(id), kind, List.of(SourceLanguageVersion.flexible(language)));
    }

    private static ParsedCodeType type(String rootId, String name, SourceLanguage language) {
        return new ParsedCodeType(rootId + ":" + name, rootId, rootId + ":module", name, name,
                ParsedCodeTypeKind.CLASS, Path.of(name + language.fileExtensions().stream().findFirst().orElse("")),
                rootId + ".package", List.of(), List.of(), Map.of("language", language.id()));
    }
}
