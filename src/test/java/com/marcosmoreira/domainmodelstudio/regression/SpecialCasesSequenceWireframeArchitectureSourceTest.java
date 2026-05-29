package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles para familias que no deben volver a verse como grafo genérico. */
class SpecialCasesSequenceWireframeArchitectureSourceTest {

    @Test
    void sequenceDiagramShouldKeepTemporalGeometry() throws IOException {
        String adapter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceCanvasAdapter.java");
        String renderKit = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceRenderKit.java");
        String geometry = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceMessageGeometry.java");

        assertTrue(adapter.contains("PARTICIPANT_TOP_Y") && adapter.contains("MESSAGE_ROW_GAP"),
                "UML Secuencia debe conservar eje horizontal de participantes y eje vertical temporal.");
        assertTrue(renderKit.contains("SequenceMessageGeometry.route") && renderKit.contains("sequence-message-self"),
                "UML Secuencia debe dibujar mensajes desde geometría temporal, incluyendo auto-mensajes.");
        assertTrue(geometry.contains("selfMessage") && geometry.contains("returnMessage"),
                "La geometría de secuencia debe diferenciar auto-mensaje y retorno.");
    }

    @Test
    void wireframeFiguresShouldStayAsSimpleMockupScaffolding() throws IOException {
        String factory = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeComponentFigureFactory.java");
        String kind = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeFigureKind.java");

        assertTrue(factory.contains("addScreenChrome") && factory.contains("addDashboardStub"),
                "Wireframes deben representar pantalla/maqueta, no cajas genéricas.");
        assertTrue(factory.contains("addModalChrome") && factory.contains("addPagination") && factory.contains("addReport"),
                "Wireframes deben distinguir componentes básicos de UI simulada.");
        assertTrue(kind.contains("isFieldLike") && kind.contains("isPanelLike"),
                "La clasificación de wireframes debe mantenerse explícita y pequeña.");
    }

    @Test
    void architectureShouldDifferentiateC4AndDeploymentVisualSemantics() throws IOException {
        String renderKit = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureRenderKit.java");
        String semantics = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureCanvasSemantics.java");
        String css = read("src/main/resources/css/architecture-diagram.css");

        assertTrue(renderKit.contains("renderZoneNode") && renderKit.contains("ArchitectureCanvasSemantics.nodeFamilyClass"),
                "Arquitectura debe distinguir zonas/límites de nodos C4 o despliegue.");
        assertTrue(semantics.contains("deploymentLike") && semantics.contains("connectorFamilyClass"),
                "C4/despliegue deben conservar clasificación visual mínima.");
        assertTrue(css.contains("architecture-canvas-zone") && css.contains("architecture-canvas-connector-deployment"),
                "CSS debe tener estilos propios para zonas y conectores de despliegue.");
    }

    @Test
    void specializedSvgShouldCarrySpecialCaseSemantics() throws IOException {
        String factory = read("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedSvgModelFactory.java");
        String writer = read("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedVisualSvgWriter.java");

        assertTrue(factory.contains("node-wireframe-component-") && factory.contains("node-architecture-"),
                "SVG especializado debe conservar clases por componente de wireframe y nodo de arquitectura.");
        assertTrue(writer.contains("sequence-message-self") && writer.contains("source.elementId().equals(target.elementId())"),
                "SVG de secuencia debe distinguir auto-mensajes, no solo líneas horizontales genéricas.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
