package com.marcosmoreira.domainmodelstudio.application.architecture;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Política semántica por nivel de arquitectura.
 *
 * <p>C4 Contexto, C4 Contenedores y Despliegue técnico no son variantes visuales
 * de un mismo grafo: cada uno responde una pregunta distinta. Esta política
 * concentra esas reglas para no inflar el caso de uso de validación.</p>
 */
public final class ArchitectureDiagramValidationPolicy {

    public List<String> validateByDiagramKind(ArchitectureDiagramDocument document) {
        if (document == null) {
            return List.of("No hay diagrama abierto.");
        }
        return switch (document.diagramKind()) {
            case C4_CONTEXT -> validateC4Context(document);
            case C4_CONTAINERS -> validateC4Containers(document);
            case TECHNICAL_DEPLOYMENT -> validateTechnicalDeployment(document);
        };
    }

    private List<String> validateC4Context(ArchitectureDiagramDocument document) {
        ArrayList<String> warnings = new ArrayList<>();
        long systems = count(document, ArchitectureNodeKind.SOFTWARE_SYSTEM);
        long people = count(document, ArchitectureNodeKind.PERSON);
        long externals = count(document, ArchitectureNodeKind.EXTERNAL_SYSTEM);
        if (systems == 0) {
            warnings.add("C4 Contexto debe mostrar el sistema bajo estudio como sistema central.");
        }
        if (people == 0) {
            warnings.add("C4 Contexto normalmente incluye al menos una persona o actor que usa el sistema.");
        }
        if (externals == 0 && document.nodes().size() > 1) {
            warnings.add("C4 Contexto debería indicar sistemas externos cuando existan integraciones relevantes.");
        }
        warnUnsupportedNodes(document, warnings, Set.of(
                ArchitectureNodeKind.CONTAINER,
                ArchitectureNodeKind.APPLICATION,
                ArchitectureNodeKind.API,
                ArchitectureNodeKind.DATABASE,
                ArchitectureNodeKind.EXTERNAL_SERVICE,
                ArchitectureNodeKind.ENVIRONMENT,
                ArchitectureNodeKind.SERVER,
                ArchitectureNodeKind.CLIENT,
                ArchitectureNodeKind.SERVICE,
                ArchitectureNodeKind.NETWORK,
                ArchitectureNodeKind.ARTIFACT
        ), "C4 Contexto no debería bajar al nivel de contenedores, bases de datos o despliegue.");
        return warnings;
    }

    private List<String> validateC4Containers(ArchitectureDiagramDocument document) {
        ArrayList<String> warnings = new ArrayList<>();
        long executableContainers = document.nodes().stream()
                .filter(node -> node.kind() == ArchitectureNodeKind.CONTAINER
                        || node.kind() == ArchitectureNodeKind.APPLICATION
                        || node.kind() == ArchitectureNodeKind.API)
                .count();
        long dataStores = count(document, ArchitectureNodeKind.DATABASE);
        if (executableContainers == 0) {
            warnings.add("C4 Contenedores debe mostrar aplicaciones, APIs o contenedores ejecutables del sistema.");
        }
        if (dataStores == 0) {
            warnings.add("C4 Contenedores suele incluir almacenes de datos cuando el sistema persiste información.");
        }
        warnUnsupportedNodes(document, warnings, Set.of(
                ArchitectureNodeKind.PERSON,
                ArchitectureNodeKind.ENVIRONMENT,
                ArchitectureNodeKind.SERVER,
                ArchitectureNodeKind.CLIENT,
                ArchitectureNodeKind.NETWORK,
                ArchitectureNodeKind.ARTIFACT
        ), "C4 Contenedores no debería mezclarse con actores de contexto ni detalles físicos de despliegue.");
        return warnings;
    }

    private List<String> validateTechnicalDeployment(ArchitectureDiagramDocument document) {
        ArrayList<String> warnings = new ArrayList<>();
        long environments = count(document, ArchitectureNodeKind.ENVIRONMENT);
        long servers = count(document, ArchitectureNodeKind.SERVER);
        long deployableServices = document.nodes().stream()
                .filter(node -> node.kind() == ArchitectureNodeKind.SERVICE || node.kind() == ArchitectureNodeKind.ARTIFACT)
                .count();
        if (environments == 0 && servers == 0) {
            warnings.add("Despliegue técnico debe mostrar al menos un ambiente, servidor o nodo donde corre el sistema.");
        }
        if (deployableServices == 0 && document.nodes().size() > 1) {
            warnings.add("Despliegue técnico debería indicar servicios o artefactos desplegados, no solo zonas vacías.");
        }
        warnUnsupportedNodes(document, warnings, Set.of(
                ArchitectureNodeKind.PERSON,
                ArchitectureNodeKind.SOFTWARE_SYSTEM,
                ArchitectureNodeKind.EXTERNAL_SYSTEM,
                ArchitectureNodeKind.CONTAINER,
                ArchitectureNodeKind.APPLICATION,
                ArchitectureNodeKind.API,
                ArchitectureNodeKind.EXTERNAL_SERVICE
        ), "Despliegue técnico no debería mezclar actores o abstracciones C4 de contexto/contenedores con nodos físicos/lógicos.");
        warnDeploymentEdges(document, warnings);
        return warnings;
    }

    private static long count(ArchitectureDiagramDocument document, ArchitectureNodeKind kind) {
        return document.nodes().stream().filter(node -> node.kind() == kind).count();
    }

    private static void warnUnsupportedNodes(ArchitectureDiagramDocument document, List<String> warnings,
                                             Set<ArchitectureNodeKind> unsupportedKinds, String message) {
        boolean found = document.nodes().stream().map(ArchitectureNode::kind).anyMatch(unsupportedKinds::contains);
        if (found) {
            warnings.add(message);
        }
    }

    private static void warnDeploymentEdges(ArchitectureDiagramDocument document, List<String> warnings) {
        for (ArchitectureEdge edge : document.edges()) {
            boolean deploymentEdge = edge.kind() == ArchitectureEdgeKind.CONNECTS_TO
                    || edge.kind() == ArchitectureEdgeKind.DEPLOYS_TO
                    || edge.kind() == ArchitectureEdgeKind.HOSTS;
            if (deploymentEdge && edge.protocol().isBlank() && edge.label().isBlank()) {
                warnings.add("En despliegue técnico conviene etiquetar conexiones, hosting o despliegues con protocolo, puerto o intención: " + edge.id());
            }
        }
    }
}
