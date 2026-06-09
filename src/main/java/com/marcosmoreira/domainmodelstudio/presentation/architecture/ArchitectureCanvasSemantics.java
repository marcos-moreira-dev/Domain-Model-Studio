package com.marcosmoreira.domainmodelstudio.presentation.architecture;

/** Clasificación visual mínima para no mezclar C4 y despliegue como cajas genéricas. */
final class ArchitectureCanvasSemantics {

    private ArchitectureCanvasSemantics() {
    }

    static boolean boundaryLike(String kind) {
        String normalized = normalize(kind);
        return normalized.equals("boundary") || normalized.equals("environment") || normalized.equals("network");
    }

    static boolean personLike(String kind) {
        String normalized = normalize(kind);
        return normalized.equals("person");
    }

    static boolean deploymentLike(String kind) {
        String normalized = normalize(kind);
        return normalized.equals("environment") || normalized.equals("server") || normalized.equals("network")
                || normalized.equals("artifact") || normalized.equals("service") || normalized.equals("client");
    }

    static String nodeFamilyClass(String kind) {
        if (boundaryLike(kind)) {
            return "architecture-canvas-node-zone";
        }
        if (personLike(kind)) {
            return "architecture-canvas-node-actor";
        }
        if (deploymentLike(kind)) {
            return "architecture-canvas-node-deployment";
        }
        return "architecture-canvas-node-c4";
    }

    static String nodeRoleClass(String kind) {
        String normalized = normalize(kind);
        return switch (normalized) {
            case "software-system" -> "architecture-canvas-node-system-central";
            case "external-system", "external-service" -> "architecture-canvas-node-external";
            case "api" -> "architecture-canvas-node-api";
            case "database" -> "architecture-canvas-node-data-store";
            case "application", "container" -> "architecture-canvas-node-container-role";
            case "environment" -> "architecture-canvas-node-environment-role";
            case "network" -> "architecture-canvas-node-network-role";
            case "server" -> "architecture-canvas-node-server-role";
            case "artifact" -> "architecture-canvas-node-artifact-role";
            case "client" -> "architecture-canvas-node-client-role";
            default -> "architecture-canvas-node-generic-role";
        };
    }

    static String connectorFamilyClass(String kind) {
        String normalized = normalize(kind);
        if (normalized.equals("deploys-to") || normalized.equals("hosts") || normalized.equals("connects-to")) {
            return "architecture-canvas-connector-deployment";
        }
        if (normalized.equals("reads-writes") || normalized.equals("publishes") || normalized.equals("subscribes")) {
            return "architecture-canvas-connector-data";
        }
        return "architecture-canvas-connector-c4";
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip().toLowerCase(java.util.Locale.ROOT).replace('_', '-');
    }
}
