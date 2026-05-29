package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Tanda22BendPointToolbarExposureSourceTest {

    @Test
    void toolbarProviderAddsDeleteBendPointForBendpointProfiles() {
        DefaultDiagramToolbarActionProvider provider = new DefaultDiagramToolbarActionProvider();
        for (DiagramTypeId typeId : Set.of(
                DiagramTypeId.ADMIN_MODULE_MAP,
                DiagramTypeId.UML_CLASS,
                DiagramTypeId.SCREEN_FLOW,
                DiagramTypeId.BPMN_BASIC,
                DiagramTypeId.OPERATIONAL_FLOW,
                DiagramTypeId.UML_USE_CASE,
                DiagramTypeId.UML_ACTIVITY,
                DiagramTypeId.UML_STATE,
                DiagramTypeId.C4_CONTEXT,
                DiagramTypeId.C4_CONTAINERS,
                DiagramTypeId.TECHNICAL_DEPLOYMENT,
                DiagramTypeId.FREE_GRAPH)) {
            Set<DiagramToolbarActionId> actions = provider.actionsFor(typeId).stream()
                    .map(DiagramToolbarAction::id)
                    .collect(Collectors.toSet());
            assertTrue(actions.contains(DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT),
                    typeId + " debe exponer Quitar punto cuando su perfil soporta puntos intermedios.");
        }
    }

    @Test
    void deleteBendPointIsNoLongerConceptualOnly() throws IOException {
        String capability = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/capabilities/DiagramCapabilityPresentationPolicy.java");
        String provider = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DefaultDiagramToolbarActionProvider.java");

        assertTrue(!capability.substring(capability.indexOf("CONCEPTUAL_CANVAS_ONLY_ACTIONS"), capability.indexOf("private final DiagramCapabilityCatalog")).contains("DELETE_SELECTED_BEND_POINT"),
                "Quitar punto no debe quedar encerrado como acción exclusiva del modelo conceptual.");
        assertTrue(provider.contains("deleteBendPointAction()"),
                "El proveedor debe agregar Quitar punto transversalmente cuando el perfil lo permite.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
