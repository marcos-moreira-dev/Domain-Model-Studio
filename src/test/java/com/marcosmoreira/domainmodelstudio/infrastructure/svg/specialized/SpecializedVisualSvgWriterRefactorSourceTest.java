package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente de R4: el exportador SVG especializado queda dividido por responsabilidades. */
class SpecializedVisualSvgWriterRefactorSourceTest {

    private static final Path PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized"
    );

    @Test
    void specializedWriterShouldDelegateToSmallCollaborators() throws IOException {
        String writer = read("SpecializedVisualSvgWriter.java");

        assertTrue(writer.contains("SpecializedSvgDocumentWriter"),
                "El writer debe delegar encabezado, estilos y defs del SVG.");
        assertTrue(writer.contains("SpecializedSvgGeometry"),
                "El writer debe delegar bounds, rutas, edge points y midpoint.");
        assertTrue(writer.contains("SpecializedSvgNodeWriter"),
                "El writer debe delegar el renderizado de nodos.");
        assertTrue(writer.contains("SpecializedSvgConnectorWriter"),
                "El writer debe delegar conectores y etiquetas.");
        assertTrue(writer.contains("SpecializedSvgSequenceWriter"),
                "El writer debe delegar UML Secuencia como caso temporal especializado.");
        assertLineCountAtMost("SpecializedVisualSvgWriter.java", 160);
    }

    @Test
    void collaboratorsShouldPreserveSvgSemantics() throws IOException {
        String document = read("SpecializedSvgDocumentWriter.java");
        String geometry = read("SpecializedSvgGeometry.java");
        String connector = read("SpecializedSvgConnectorWriter.java");
        String sequence = read("SpecializedSvgSequenceWriter.java");

        assertTrue(document.contains("uml-inheritance")
                        && document.contains("uml-composition")
                        && document.contains("uml-aggregation"),
                "Los marcadores UML deben seguir existiendo como defs vectoriales.");
        assertTrue(geometry.contains("edgePoint") && geometry.contains("double totalLength"),
                "La geometría debe conservar puntos de borde y midpoint por longitud real de ruta.");
        assertTrue(connector.contains("connector-label-box") && connector.contains("renderSelfLoopConnector"),
                "Los conectores deben preservar etiquetas comunes y autorrelaciones.");
        assertTrue(sequence.contains("sequence-message-self")
                        && sequence.contains("source.elementId().equals(target.elementId())"),
                "UML Secuencia debe conservar auto-mensajes como geometría temporal propia.");
    }

    private static void assertLineCountAtMost(String fileName, long limit) throws IOException {
        long lines = Files.lines(PACKAGE.resolve(fileName)).count();
        assertTrue(lines <= limit, fileName + " debe quedar bajo " + limit + " líneas tras R4; líneas actuales: " + lines);
    }

    private static String read(String fileName) throws IOException {
        return Files.readString(PACKAGE.resolve(fileName), StandardCharsets.UTF_8);
    }
}
