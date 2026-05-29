package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.List;
import org.junit.jupiter.api.Test;

class SpecializedArchitectureSvgLayeringTest {

    @Test
    void architectureZonesAreWrittenBehindConnectorsAndRegularNodes() {
        DiagramElementId networkId = DiagramElementId.of("architecture-node:red-admin");
        DiagramElementId serviceId = DiagramElementId.of("architecture-node:api-uens");
        SpecializedSvgModel model = new SpecializedSvgModel(
                DiagramTypeId.TECHNICAL_DEPLOYMENT,
                "Despliegue UENS",
                "Despliegue técnico",
                List.of(
                        new SpecializedSvgNode(serviceId, "API UENS", "Servicio", List.of(), "node-architecture node-architecture-service"),
                        new SpecializedSvgNode(networkId, "Red administrativa", "Red", List.of(), "node-architecture node-architecture-network")
                ),
                List.of());
        DiagramLayout layout = new DiagramLayout(
                NotationType.CHEN,
                List.of(
                        NodeLayout.at(serviceId.value(), 180, 160, 180, 90),
                        NodeLayout.at(networkId.value(), 100, 100, 420, 250)
                ),
                List.of());

        String svg = new SpecializedVisualSvgWriter().write(model, layout);

        int backgroundLayer = svg.indexOf("id=\"background-nodes\"");
        int connectorsLayer = svg.indexOf("id=\"connectors\"");
        int nodesLayer = svg.indexOf("id=\"nodes\"");
        int networkText = svg.indexOf("Red administrativa");
        int serviceText = svg.indexOf("API UENS");
        assertTrue(backgroundLayer >= 0 && backgroundLayer < connectorsLayer,
                "Las zonas de arquitectura deben escribirse antes que conectores para quedar al fondo documental.");
        assertTrue(connectorsLayer < nodesLayer,
                "Los nodos normales deben seguir encima de conectores en el SVG documental.");
        assertTrue(networkText > backgroundLayer && networkText < connectorsLayer,
                "La red debe quedar en la capa de fondo.");
        assertTrue(serviceText > nodesLayer,
                "El servicio debe quedar en la capa de nodos normales.");
    }
}
