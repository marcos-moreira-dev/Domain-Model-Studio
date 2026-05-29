package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 27: la documentación viva no debe volver a ser bitácora acumulada. */
class LivingDocumentationCleanupSourceTest {

    @Test
    void repositoryShouldKeepDocumentationSurfaceSmallAndOperational() throws IOException {
        long docsMarkdown;
        try (Stream<Path> files = Files.walk(Path.of("docs"))) {
            docsMarkdown = files.filter(path -> path.toString().endsWith(".md")).count();
        }

        assertTrue(docsMarkdown <= 260, "La documentación viva volvió a crecer como bitácora: " + docsMarkdown);
        assertFalse(Files.exists(Path.of("docs", "post_cierre")));
        assertFalse(Files.exists(Path.of("docs", "pendiente")));
        assertFalse(Files.exists(Path.of("docs", "historico")));
    }

    @Test
    void estadoFolderShouldExposeOnlyCurrentState() throws IOException {
        List<Path> stateDocs;
        try (Stream<Path> files = Files.list(Path.of("docs", "estado"))) {
            stateDocs = files.filter(path -> path.toString().endsWith(".md")).toList();
        }

        assertTrue(stateDocs.size() == 1, "docs/estado debe ser estado vivo, no bitácora: " + stateDocs);
        assertTrue(stateDocs.getFirst().endsWith("ESTADO_ACTUAL.md"));

        String current = Files.readString(stateDocs.getFirst(), StandardCharsets.UTF_8);
        assertTrue(current.contains("vigente después de Tanda 27"));
        assertTrue(current.contains("documentación viva"));
        assertFalse(current.contains("Tanda L0"));
        assertFalse(current.contains("fuente madre"));
    }

    @Test
    void livingMapAndPolicyShouldStateDeletionRule() throws IOException {
        String map = read("docs/documentacion/MAPA_DOCUMENTACION_VIVA.md");
        String policy = read("docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md");
        String readme = read("docs/README.md");
        String root = read("README.md");

        for (String text : List.of(map, policy, readme, root)) {
            assertTrue(text.contains("Tanda 27") || text.contains("tandas pasadas") || text.contains("no se conserva"));
        }
        assertTrue(map.contains("Qué se eliminó en Tanda 27"));
        assertTrue(policy.contains("Se elimina Markdown cuando"));
        assertTrue(readme.contains("No se archiva por defecto"));
        assertTrue(root.contains("Estado documental vivo post Tanda 27"));
    }

    @Test
    void developmentTandaLogsShouldBeConsolidatedInsteadOfAccumulated() throws IOException {
        assertFalse(Files.exists(Path.of("docs", "desarrollo", "TANDA_016_AYUDA_GLOSARIO_OBLIGATORIO_LEVANTAMIENTO_LOGICO.md")));
        assertFalse(Files.exists(Path.of("docs", "desarrollo", "TANDA_023_EJEMPLO_OFICIAL_UENS_ACTUALIZADO.md")));
        assertTrue(Files.exists(Path.of("docs", "desarrollo", "TANDA_027_LIMPIEZA_DOCUMENTAL_VIVA.md")));
        assertTrue(Files.exists(Path.of("docs", "desarrollo", "PLAN_TANDAS_ACTUAL.md")));
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
