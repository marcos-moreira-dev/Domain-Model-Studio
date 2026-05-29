package com.marcosmoreira.domainmodelstudio.application.architecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ArchitectureDiagramValidationPolicyTest {

    private final ValidateArchitectureDiagramUseCase validator = new ValidateArchitectureDiagramUseCase();

    @Test
    void c4ContextWarnsWhenItDropsToContainersLevel() {
        ArchitectureDiagramDocument document = new ArchitectureDiagramDocument(
                "Sistema", "borrador", LocalDate.now(), ArchitectureDiagramKind.C4_CONTEXT,
                List.of(
                        node("sys", ArchitectureNodeKind.SOFTWARE_SYSTEM),
                        node("api", ArchitectureNodeKind.API)
                ), List.of(), "");

        var result = validator.validate(document);

        assertTrue(result.warnings().stream().anyMatch(text -> text.contains("no debería bajar al nivel")),
                "C4 contexto debe proteger el nivel de abstracción");
    }

    @Test
    void c4ContainersWarnsWhenItUsesDeploymentNodes() {
        ArchitectureDiagramDocument document = new ArchitectureDiagramDocument(
                "Sistema", "borrador", LocalDate.now(), ArchitectureDiagramKind.C4_CONTAINERS,
                List.of(
                        node("backend", ArchitectureNodeKind.API),
                        node("server", ArchitectureNodeKind.SERVER)
                ), List.of(), "");

        var result = validator.validate(document);

        assertTrue(result.warnings().stream().anyMatch(text -> text.contains("detalles físicos de despliegue")),
                "C4 contenedores no debe confundirse con despliegue técnico");
    }

    @Test
    void deploymentWarnsAboutUnlabeledTechnicalConnection() {
        ArchitectureDiagramDocument document = new ArchitectureDiagramDocument(
                "Sistema", "borrador", LocalDate.now(), ArchitectureDiagramKind.TECHNICAL_DEPLOYMENT,
                List.of(
                        node("prod", ArchitectureNodeKind.ENVIRONMENT),
                        node("service", ArchitectureNodeKind.SERVICE)
                ),
                List.of(new ArchitectureEdge("edge-1", "service", "prod", ArchitectureEdgeKind.DEPLOYS_TO, "", "", "")), "");

        var result = validator.validate(document);

        assertTrue(result.warnings().stream().anyMatch(text -> text.contains("protocolo, puerto o intención")),
                "despliegue debe pedir etiquetas técnicas cuando la conexión no explica nada");
    }

    private static ArchitectureNode node(String id, ArchitectureNodeKind kind) {
        return new ArchitectureNode(id, kind, kind.displayName(), "", "", "", "", "", 0);
    }
}
