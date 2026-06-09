package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíles de cierre para la documentación viva post-limpieza. */
class ReleaseClosureDocumentationTest {

    @Test
    void livingEvidenceShouldExistForCurrentProductSurface() {
        List<String> evidencePaths = List.of(
                "docs/README.md",
                "docs/documentacion/MAPA_DOCUMENTACION_VIVA.md",
                "docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md",
                "docs/estado/ESTADO_ACTUAL.md",
                "docs/producto/MATRIZ_CAPACIDADES_REALES.md",
                "docs/testeo/MATRIZ_CASOS_USO_Y_TESTS.md",
                "docs/implementacion/00_indice_implementacion.md",
                "docs/implementacion/tanda_17_smoke_qa_cierre_release/01_matriz_smoke_16_tipos.md",
                "docs/release/RELEASE_CANDIDATE_0_0_1.md",
                "docs/release/LIMITACIONES_CONOCIDAS_0_0_1.md",
                "docs/release/RELEASE_NOTES.md");

        for (String path : evidencePaths) {
            assertTrue(Files.exists(Path.of(path)), "Falta evidencia viva de cierre: " + path);
        }
    }

    @Test
    void releaseSmokeMatrixShouldMentionEveryVisibleType() throws IOException {
        String matrix = Files.readString(
                Path.of("docs/implementacion/tanda_17_smoke_qa_cierre_release/01_matriz_smoke_16_tipos.md"),
                StandardCharsets.UTF_8);

        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            assertTrue(matrix.contains(type.displayName()), "Falta tipo en matriz de smoke: " + type.displayName());
        }
    }

    @Test
    void oldPlanningFoldersShouldNotReturnAsLivingSources() {
        assertFalse(Files.exists(Path.of("docs/planificacion")), "docs/planificacion no debe volver como fuente viva.");
        assertFalse(Files.exists(Path.of("docs/tandas-implementacion")),
                "docs/tandas-implementacion no debe volver como fuente viva.");
        assertFalse(Files.exists(Path.of("docs/tandas-correctivas")),
                "docs/tandas-correctivas no debe volver como fuente viva.");
    }

    @Test
    void operationalScriptsShouldStayMinimalAndDocumented() throws IOException {
        assertTrue(Files.exists(Path.of("scripts/01-ejecutar-app.bat")));
        assertTrue(Files.exists(Path.of("scripts/02-ejecutar-tests.bat")));
        assertTrue(Files.exists(Path.of("scripts/14-app-image-completa.bat")));
        assertTrue(Files.exists(Path.of("scripts/15-msi-completo.bat")));
        assertTrue(Files.exists(Path.of("scripts/16-release-candidate.bat")));

        String scriptsReadme = Files.readString(Path.of("scripts/README.md"), StandardCharsets.UTF_8);
        assertTrue(scriptsReadme.contains("01-ejecutar-app.bat"));
        assertTrue(scriptsReadme.contains("16-release-candidate.bat"));
        assertTrue(scriptsReadme.contains("scripts de tandas pasadas"));
    }
}
