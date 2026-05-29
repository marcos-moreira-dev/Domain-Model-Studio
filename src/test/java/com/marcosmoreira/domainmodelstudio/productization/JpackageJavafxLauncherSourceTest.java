package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl RC-1A: jpackage debe usar un launcher que no extienda Application. */
class JpackageJavafxLauncherSourceTest {

    private static final Path LAUNCHER = Path.of("src", "main", "java", "com", "marcosmoreira",
            "domainmodelstudio", "DomainModelStudioLauncher.java");
    private static final Path APP = Path.of("src", "main", "java", "com", "marcosmoreira",
            "domainmodelstudio", "DomainModelStudioApp.java");
    private static final Path APP_IMAGE = Path.of("scripts", "internal", "create-app-image.bat");
    private static final Path MSI = Path.of("scripts", "internal", "create-msi-installer.bat");
    private static final Path POM = Path.of("pom.xml");
    private static final Path DOC = Path.of("docs", "desarrollo", "TANDA_RC_001A_LAUNCHER_JAVAFX_JPACKAGE.md");

    @Test
    void desktopLauncherShouldNotExtendJavafxApplication() throws IOException {
        String launcher = read(LAUNCHER);
        String app = read(APP);

        assertTrue(app.contains("extends Application"), "La aplicación JavaFX real sigue siendo DomainModelStudioApp.");
        assertTrue(launcher.contains("public final class DomainModelStudioLauncher"));
        assertFalse(launcher.contains("extends Application"), "El launcher instalable no debe extender Application.");
        assertTrue(launcher.contains("Application.launch(DomainModelStudioApp.class, args)"));
    }

    @Test
    void jpackageAndMavenShouldUseDesktopLauncherAsMainClass() throws IOException {
        String appImage = read(APP_IMAGE);
        String msi = read(MSI);
        String pom = read(POM);
        String expected = "com.marcosmoreira.domainmodelstudio.DomainModelStudioLauncher";

        assertTrue(appImage.contains("set MAIN_CLASS=" + expected));
        assertTrue(msi.contains("set MAIN_CLASS=" + expected));
        assertTrue(pom.contains("<mainClass>" + expected + "</mainClass>"));
        assertFalse(appImage.contains("set MAIN_CLASS=com.marcosmoreira.domainmodelstudio.DomainModelStudioApp"));
        assertFalse(msi.contains("set MAIN_CLASS=com.marcosmoreira.domainmodelstudio.DomainModelStudioApp"));
    }

    @Test
    void documentationShouldRecordInstalledLauncherSmokeFix() throws IOException {
        String doc = read(DOC);
        for (String token : new String[] {
                "Domain Model Studio.exe",
                "código 1",
                "faltan los componentes de JavaFX runtime",
                "DomainModelStudioLauncher",
                "no extiende `Application`",
                "desinstalar el MSI anterior"
        }) {
            assertTrue(doc.contains(token), "Falta evidencia documental del hotfix launcher: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
