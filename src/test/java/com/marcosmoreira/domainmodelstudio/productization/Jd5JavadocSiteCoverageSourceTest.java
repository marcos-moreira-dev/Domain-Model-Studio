package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente de la tanda JD-5 para sitio JavaDoc y cobertura gradual. */
class Jd5JavadocSiteCoverageSourceTest {

    private static final Path ROOT = Path.of("");
    private static final Pattern PUBLIC_TYPE_PATTERN = Pattern.compile(
            "(?m)^\\s*public\\s+(?:(?:abstract|final|sealed|non-sealed|static)\\s+)*(?:class|interface|enum|record|@interface)\\s+[A-Za-z_][A-Za-z0-9_]*");

    @Test
    void javadocPluginAndSinglePublicScriptShouldGenerateTheSite() throws IOException {
        assertContains("pom.xml",
                "maven-javadoc-plugin", "Domain Model Studio JavaDoc", "doclint", "detectJavaApiLink");
        assertContains("scripts/31-generar-javadoc.bat",
                "mvn -DskipTests javadoc:javadoc", "target\\site\\apidocs\\index.html");
        assertContains("scripts/README.md",
                "31-generar-javadoc.bat", "scripts de tandas pasadas");
    }

    @Test
    void javadocSiteGuideShouldExplainNavigationAndStudyUse() throws IOException {
        assertContains("docs/desarrollo/JAVADOC_SITIO_GUIA.md",
                "target\\site\\apidocs\\index.html", "Search", "paquetes", "Dominio → aplicación → infraestructura → presentación");
        assertContains("docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md",
                "Sitio JavaDoc", "JAVADOC_SITIO_GUIA.md", "target\\site\\apidocs\\index.html");
        assertContains("docs/calidad/AUDITORIA_JAVADOC_JD5.md",
                "Tipos públicos detectados", "98.8%", ">= 95%", "No se activa umbral de métodos públicos global todavía");
    }

    @Test
    void publicTypeJavadocCoverageShouldStayAboveGradualThreshold() throws IOException {
        Coverage coverage = publicTypeCoverage();
        double ratio = coverage.documented() * 100.0 / Math.max(1, coverage.total());

        assertTrue(ratio >= 95.0,
                () -> "Cobertura JavaDoc de tipos públicos insuficiente: " + ratio + "% ("
                        + coverage.documented() + "/" + coverage.total() + ")");
    }

    @Test
    void javadocPlanShouldMarkJd5AsAppliedAndKeepRemainingTandas() throws IOException {
        assertContains("docs/calidad/PLAN_TANDAS_JAVADOC.md",
                "Estado: aplicada en JD-5", "JD-6", "JD-7", "JD-8", "JD-9");
        assertContains("docs/implementacion/TANDA_JD_005_JAVADOC_SITIO_COBERTURA.md",
                "Cobertura gradual", "Tipos públicos documentados >= 95%", "No se modificó lógica funcional");
    }

    private static Coverage publicTypeCoverage() throws IOException {
        int total = 0;
        int documented = 0;

        try (Stream<Path> paths = Files.walk(ROOT.resolve("src/main/java"))) {
            for (Path path : paths.filter(p -> p.toString().endsWith(".java")).toList()) {
                String text = Files.readString(path);
                Matcher matcher = PUBLIC_TYPE_PATTERN.matcher(text);
                while (matcher.find()) {
                    total++;
                    if (hasJavadocBefore(text, matcher.start())) {
                        documented++;
                    }
                }
            }
        }
        return new Coverage(total, documented);
    }

    private static boolean hasJavadocBefore(String text, int typeStart) {
        String[] lines = text.substring(0, typeStart).split("\\R", -1);
        int index = lines.length - 1;
        while (index >= 0 && lines[index].isBlank()) {
            index--;
        }
        if (index < 0) {
            return false;
        }
        return lines[index].trim().endsWith("*/");
    }

    private static void assertContains(String relativePath, String... requiredFragments) throws IOException {
        String text = Files.readString(ROOT.resolve(relativePath));
        for (String fragment : requiredFragments) {
            assertTrue(text.contains(fragment), () -> relativePath + " no contiene: " + fragment);
        }
    }

    private record Coverage(int total, int documented) {}
}
