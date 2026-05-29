package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda34MinorTechnicalDebtSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void modelTreeAvoidsUncheckedVarargsAddAll() throws IOException {
        String source = Files.readString(ROOT.resolve("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidebar/ModelTreeViewModel.java"));

        assertFalse(source.contains("getChildren().addAll("));
    }

    @Test
    void jsonWritersUseSharedEscaper() throws IOException {
        assertUsesSharedEscaper("DmsProjectJsonWriter.java");
        assertUsesSharedEscaper("DmsProjectCoreSpecializedJsonWriter.java");
        assertUsesSharedEscaper("DmsProjectAuxiliarySpecializedJsonWriter.java");
        assertUsesSharedEscaper("DmsProjectLogicalBusinessJsonWriter.java");
    }

    @Test
    void windowsPackagingUsesAlignedVersionAndOfficialIcon() throws IOException {
        String appImage = Files.readString(ROOT.resolve("scripts/internal/create-app-image.bat"));
        String msi = Files.readString(ROOT.resolve("scripts/internal/create-msi-installer.bat"));

        assertTrue(appImage.contains("set APP_VERSION=0.0.1"));
        assertTrue(msi.contains("set APP_VERSION=0.0.1"));
        assertTrue(appImage.contains("domain-model-studio-icon.ico"));
        assertTrue(msi.contains("domain-model-studio-icon.ico"));
        assertTrue(Files.exists(ROOT.resolve("src/main/resources/branding/domain-model-studio-icon.ico")));
    }

    @Test
    void technicalDebtTandaIsDocumented() throws IOException {
        String doc = Files.readString(ROOT.resolve("docs/desarrollo/TANDA_34_LIMPIEZA_TECNICA_MENOR.md"));

        assertTrue(doc.contains("ModelTreeViewModel"));
        assertTrue(doc.contains("JsonStringEscaper"));
        assertTrue(doc.contains("0.0.1"));
        assertTrue(doc.contains("domain-model-studio-icon.ico"));
    }

    private void assertUsesSharedEscaper(String fileName) throws IOException {
        String source = Files.readString(ROOT.resolve("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/" + fileName));
        assertTrue(source.contains("JsonStringEscaper.quote(value)"));
        assertFalse(source.contains("private String escape("));
    }
}
