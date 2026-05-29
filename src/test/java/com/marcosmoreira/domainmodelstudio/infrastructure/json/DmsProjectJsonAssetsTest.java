package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetKind;
import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetReference;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import java.time.LocalDate;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import org.junit.jupiter.api.Test;

class DmsProjectJsonAssetsTest {

    @Test
    void shouldPersistProjectAssetsInsideDmsProject() {
        DiagramProject project = DiagramProject.blank("diccionario", "Diccionario", DiagramTypeId.DATA_DICTIONARY)
                .withDataDictionary(DataDictionaryDocument.blank("Diccionario", LocalDate.of(2026, 1, 1)))
                .withAssetCatalog(com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetCatalog.empty()
                        .withReference(new ProjectAssetReference(
                                "logo-cliente",
                                ProjectAssetKind.LOGO,
                                "Logo del cliente",
                                "assets/logos/logo-cliente.png",
                                "image/png",
                                "Portada del diccionario",
                                "sha256:demo",
                                "Copiado junto al .dms")));

        String json = new DmsProjectJsonWriter().write(project);
        DiagramProject reopened = new DmsProjectJsonReader().read(json);

        assertEquals(1, reopened.assetCatalog().size());
        assertTrue(reopened.assetCatalog().byId("logo-cliente").isPresent());
        assertEquals("assets/logos/logo-cliente.png",
                reopened.assetCatalog().byId("logo-cliente").get().relativePath());
    }
}
