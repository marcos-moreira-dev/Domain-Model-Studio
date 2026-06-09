package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl DOC-1: la documentación viva de release debe apuntar a scripts públicos vigentes. */
class ReleaseDocumentationCurrentScriptsSourceTest {

    private static final Path[] CURRENT_DOCS = {
            Path.of("README.md"),
            Path.of("docs", "raiz", "README.md"),
            Path.of("docs", "tecnico", "COMANDOS.md"),
            Path.of("docs", "tecnico", "EMPAQUETADO_WINDOWS.md"),
            Path.of("docs", "desarrollo", "EMPAQUETADO_WINDOWS.md"),
            Path.of("docs", "desarrollo", "VALIDACION_LOCAL.md"),
            Path.of("docs", "desarrollo", "validacion.md"),
            Path.of("docs", "desarrollo", "ONBOARDING.md"),
            Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md"),
            Path.of("docs", "estado", "ESTADO_ACTUAL.md"),
            Path.of("docs", "release", "RELEASE_NOTES.md"),
            Path.of("docs", "release", "RELEASE_CANDIDATE_0_0_1.md"),
            Path.of("docs", "release", "LIMITACIONES_CONOCIDAS_0_0_1.md"),
            Path.of("scripts", "README.md")
    };

    @Test
    void currentReleaseDocsShouldMentionPublicPackagingFlow() throws IOException {
        String joined = joinedDocs();
        for (String token : new String[] {
                "scripts\\00-verificar-entorno.bat",
                "scripts\\02-ejecutar-tests.bat",
                "scripts\\13-revalidacion-local-completa.bat",
                "scripts\\14-app-image-completa.bat",
                "scripts\\15-msi-completo.bat",
                "scripts\\16-release-candidate.bat"
        }) {
            assertTrue(joined.contains(token), "Falta script vigente en documentación viva: " + token);
        }
    }

    @Test
    void currentReleaseDocsShouldNotPointToRemovedPublicScripts() throws IOException {
        String joined = joinedDocs();
        for (String token : new String[] {
                "scripts\\03-generar-app-image.bat",
                "scripts\\04-generar-instalador-msi.bat",
                "scripts\\04-generar-app-image.bat",
                "scripts\\05-generar-instalador-msi.bat",
                "scripts\\07-validar-app-image.bat",
                "scripts\\08-validacion-local-completa.bat",
                "scripts\\10-cierre-base.bat",
                "scripts\\11-smoke-visual-uens.bat",
                "scripts\\12-validar-cierre-estatico.bat"
        }) {
            assertFalse(joined.contains(token), "Documento vivo apunta a script público retirado: " + token);
        }
    }

    @Test
    void releaseCandidateScriptShouldUseNeutralInstallerReport() throws IOException {
        String verify = Files.readString(Path.of("scripts", "internal", "verify-release-candidate.bat"), StandardCharsets.UTF_8);
        String script = Files.readString(Path.of("scripts", "16-release-candidate.bat"), StandardCharsets.UTF_8);

        assertTrue(verify.contains("docs\\testeo\\INSTALABLE_WINDOWS_RC_GUIA.md"));
        assertTrue(verify.contains("docs\\testeo\\reportes\\REPORTE_INSTALABLE_WINDOWS_RC.md"));
        assertFalse(verify.contains("REPORTE_RELEASE_CANDIDATE_TANDA_40.md"));
        assertFalse(script.contains("Tanda 40 release candidate"));
        assertTrue(script.contains("release candidate local"));
    }

    private static String joinedDocs() throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Path path : CURRENT_DOCS) {
            builder.append(Files.readString(path, StandardCharsets.UTF_8)).append('\n');
        }
        return builder.toString();
    }
}
