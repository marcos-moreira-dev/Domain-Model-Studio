package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 26: la raíz de scripts debe permanecer pequeña y vigente. */
class ScriptsPublicSurfaceSourceTest {

    private static final Path SCRIPTS = Path.of("scripts");
    private static final Set<String> PUBLIC_SCRIPTS = Set.of(
            "00-verificar-entorno.bat",
            "01-ejecutar-app.bat",
            "02-ejecutar-tests.bat",
            "06-medir-refactor.bat",
            "13-revalidacion-local-completa.bat",
            "14-app-image-completa.bat",
            "15-msi-completo.bat",
            "16-release-candidate.bat",
            "31-generar-javadoc.bat");

    @Test
    void scriptsRootShouldExposeOnlyCurrentOperationalEntryPoints() throws IOException {
        try (Stream<Path> paths = Files.list(SCRIPTS)) {
            Set<String> actual = paths
                    .filter(path -> path.getFileName().toString().endsWith(".bat"))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toCollection(TreeSet::new));

            assertEquals(new TreeSet<>(PUBLIC_SCRIPTS), actual,
                    "La raíz scripts/ debe contener solo entry points vigentes.");
        }
    }

    @Test
    void publicScriptsReadmeShouldDocumentCurrentSurfaceAndRejectHistoricalAccumulation() throws IOException {
        String readme = Files.readString(SCRIPTS.resolve("README.md"), StandardCharsets.UTF_8);
        String tanda = Files.readString(Path.of("docs/desarrollo/TANDA_026_LIMPIEZA_CONTROLADA_SCRIPTS.md"), StandardCharsets.UTF_8);
        String joined = readme + "\n" + tanda;

        for (String script : PUBLIC_SCRIPTS) {
            assertTrue(joined.contains(script), () -> "Falta script vigente en documentación: " + script);
        }

        assertTrue(joined.contains("no se conservan por defecto"));
        assertTrue(joined.contains("superficie pública"));
        assertTrue(joined.contains("scripts de tandas pasadas"));
        assertFalse(readme.contains("29-validar-tanda31-release-candidate-local.bat"));
        assertFalse(readme.contains("38-validar-jd6-javadoc-ejemplos-pedagogicos.bat"));
    }

    @Test
    void packageFlowsShouldCallInternalHelpersInsteadOfRemovedWrappers() throws IOException {
        String revalidation = Files.readString(SCRIPTS.resolve("13-revalidacion-local-completa.bat"), StandardCharsets.UTF_8);
        String appImage = Files.readString(SCRIPTS.resolve("14-app-image-completa.bat"), StandardCharsets.UTF_8);
        String msi = Files.readString(SCRIPTS.resolve("15-msi-completo.bat"), StandardCharsets.UTF_8);

        assertTrue(revalidation.contains("scripts\\internal\\run-render-smoke.bat"));
        assertTrue(revalidation.contains("scripts\\06-medir-refactor.bat"));
        assertTrue(appImage.contains("scripts\\internal\\create-app-image.bat"));
        assertTrue(appImage.contains("scripts\\internal\\verify-staged-app.bat"));
        assertTrue(msi.contains("scripts\\internal\\create-msi-installer.bat"));
        assertTrue(msi.contains("scripts\\internal\\verify-msi-installer.bat"));
    }
}
