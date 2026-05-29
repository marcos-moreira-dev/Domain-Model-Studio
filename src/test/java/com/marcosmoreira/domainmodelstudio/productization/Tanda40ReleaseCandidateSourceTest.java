package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarrail heredado: el release candidate debe tener flujo y reporte verificables. */
class Tanda40ReleaseCandidateSourceTest {

    private static final Path RC_SCRIPT = Path.of("scripts", "16-release-candidate.bat");
    private static final Path VERIFY_SCRIPT = Path.of("scripts", "internal", "verify-release-candidate.bat");
    private static final Path GUIDE = Path.of("docs", "testeo", "INSTALABLE_WINDOWS_RC_GUIA.md");
    private static final Path REPORT = Path.of("docs", "testeo", "reportes", "REPORTE_INSTALABLE_WINDOWS_RC.md");
    private static final Path RELEASE_DOC = Path.of("docs", "release", "RELEASE_CANDIDATE_0_0_1.md");
    private static final Path LIMITS = Path.of("docs", "release", "LIMITACIONES_CONOCIDAS_0_0_1.md");
    private static final Path ROOT_DOC = Path.of("docs", "raiz", "TANDA_40_RELEASE_CANDIDATE.md");
    private static final Path PLAN = Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md");
    private static final Path SCRIPTS_README = Path.of("scripts", "README.md");

    @Test
    void publicReleaseCandidateScriptShouldOrchestrateRequiredStages() throws IOException {
        String script = read(RC_SCRIPT);
        for (String token : new String[] {
                "scripts\\13-revalidacion-local-completa.bat",
                "scripts\\14-app-image-completa.bat",
                "scripts\\15-msi-completo.bat",
                "scripts\\internal\\verify-release-candidate.bat",
                "Release candidate preparado"
        }) {
            assertTrue(script.contains(token), "Falta etapa RC: " + token);
        }
    }

    @Test
    void releaseCandidateVerifierShouldRequireManifestsSmokeRenderAndReports() throws IOException {
        String script = read(VERIFY_SCRIPT);
        for (String token : new String[] {
                "APP_IMAGE_MANIFEST=dist\\staging\\APP_IMAGE_MANIFEST.txt",
                "MSI_MANIFEST=dist\\installer\\MSI_MANIFEST.txt",
                "target\\smoke-render\\SMOKE_RENDER_AUTOMATICO.md",
                "target\\smoke-render\\contact_sheet.html",
                "docs\\testeo\\INSTALABLE_WINDOWS_RC_GUIA.md",
                "docs\\testeo\\reportes\\REPORTE_INSTALABLE_WINDOWS_RC.md",
                "dist\\release\\RELEASE_CANDIDATE_MANIFEST.txt"
        }) {
            assertTrue(script.contains(token), "Falta verificacion RC: " + token);
        }
    }

    @Test
    void releaseDocumentationShouldDeclareScopeLimitsAndNoAdvanceCriteria() throws IOException {
        String joined = read(GUIDE) + "\n" + read(REPORT) + "\n" + read(RELEASE_DOC)
                + "\n" + read(LIMITS) + "\n" + read(ROOT_DOC) + "\n" + read(PLAN)
                + "\n" + read(SCRIPTS_README);
        for (String token : new String[] {
                "SVG vectorial documental",
                "no WYSIWYG",
                "Wireframes",
                "BPMN",
                "MSI",
                "app-image",
                "BUILD SUCCESS",
                "REPORTE_INSTALABLE_WINDOWS_RC.md",
                "scripts\\16-release-candidate.bat"
        }) {
            assertTrue(joined.contains(token), "Falta documento RC: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
