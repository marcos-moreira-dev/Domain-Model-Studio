package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Alineación 004: catálogo, capacidades, toolbar, workspace y exportación. */
class Alineacion4CatalogCapabilitiesWorkspaceExportContractSourceTest {

    private static final Path CONTRACT = Path.of(
            "docs", "alineacion", "ALINEACION_004_CATALOGO_CAPACIDADES_TOOLBAR_WORKSPACE_EXPORTACION.md");

    @Test
    void catalogContractShouldExistAndDefineSingleTruthChain() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Alineación 004"), "Debe identificar la alineación.");
        assertTrue(contract.contains("catálogo visible = workspace real + capacidades reales + acciones visibles coherentes + salida activa resoluble"),
                "Debe conservar la regla central de la alineación.");
        assertTrue(contract.contains("DefaultDiagramTypeDefinitions"));
        assertTrue(contract.contains("DefaultCreateWorkspaceUseCase"));
        assertTrue(contract.contains("DiagramToolbarContributorRegistry"));
        assertTrue(contract.contains("ActiveOutputResolver"));
        assertTrue(contract.contains("ProjectExportFormatPolicy"));
    }

    @Test
    void catalogContractShouldCaptureCurrentLogicalBusinessGraphHybridState() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Estado actual de catálogo: EXPERIMENTAL"));
        assertTrue(contract.contains("Workspace real: sí"));
        assertTrue(contract.contains("Capacidades declaradas actuales"));
        assertTrue(contract.contains("Capacidades faltantes para cierre productivo"));
        assertTrue(contract.contains("Toolbar contextual propia: pendiente"));
        assertTrue(contract.contains("Active output específico: pendiente"));
    }

    @Test
    void catalogContractShouldDefineTargetCapabilities() throws IOException {
        String contract = read(CONTRACT);

        for (String capability : targetCapabilities()) {
            assertTrue(contract.contains(capability), () -> "Debe mencionar la capacidad objetivo " + capability);
        }
        assertTrue(contract.contains("minimalExampleResource"));
        assertTrue(contract.contains("logical_business_graph_minimo.md"));
        assertTrue(contract.contains("officialExampleResource"));
        assertTrue(contract.contains("logical_business_graph_uens_gordito.md"));
    }

    @Test
    void catalogContractShouldDefineWorkspaceToolbarValidationAndOutputContracts() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("CreateWorkspaceResult.PRODUCT_VIEW"));
        assertTrue(contract.contains("WorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM"));
        assertTrue(contract.contains("LogicalBusinessGraphToolbarContributor"));
        assertTrue(contract.contains("DELETE_SELECTED_BEND_POINT"));
        assertTrue(contract.contains("ProjectValidationCoordinator"));
        assertTrue(contract.contains("LogicalBusinessGraphActiveOutputContributor"));
        assertTrue(contract.contains("ExportableOutput"));
        assertTrue(contract.contains("exportar un grafo vacío no debe producir Markdown inválido"));
    }

    @Test
    void catalogContractShouldDefineDmsPersistencePreconditions() throws IOException {
        String contract = read(CONTRACT);

        assertTrue(contract.contains("Persistencia `.dms`"));
        assertTrue(contract.contains("guardar documento LogicalBusinessGraphDocument"));
        assertTrue(contract.contains("guardar layout especializado"));
        assertTrue(contract.contains("reabrir proyecto con DiagramTypeId.LOGICAL_BUSINESS_GRAPH"));
        assertTrue(contract.contains("conservar cambios de movimiento de nodos"));
    }

    @Test
    void sourceShouldAlreadyExposeLogicalBusinessGraphHooksMentionedByContract() throws IOException {
        String definitions = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "application", "catalog", "definitions", "BusinessAnalysisDiagramTypeDefinitions.java"));
        String workspaceRouting = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "presentation", "workspace", "WorkspaceTypeRoutingPolicy.java"));
        String coordinator = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "presentation", "shell", "SpecializedWorkspaceCoordinator.java"));
        String formatPolicy = read(Path.of("src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "presentation", "exportable", "ProjectExportFormatPolicy.java"));

        assertTrue(definitions.contains("DiagramTypeId.LOGICAL_BUSINESS_GRAPH"));
        assertTrue(definitions.contains("DiagramWorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM"));
        assertTrue(workspaceRouting.contains("LOGICAL_BUSINESS_GRAPH_DIAGRAM"));
        assertTrue(coordinator.contains("DiagramTypeId.LOGICAL_BUSINESS_GRAPH::equals"));
        assertTrue(formatPolicy.contains("formatsForLogicalBusinessGraph"));
    }

    @Test
    void planDocumentsShouldReferenceAlignment4AsCurrentContinuation() throws IOException {
        List<Path> docs = List.of(
                Path.of("docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md"),
                Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md"),
                Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md"));

        for (Path doc : docs) {
            String text = read(doc);
            assertTrue(text.contains("ALINEACION_004_CATALOGO_CAPACIDADES_TOOLBAR_WORKSPACE_EXPORTACION.md"),
                    () -> doc + " debe apuntar al contrato de Alineación 4.");
        }
    }

    private static List<String> targetCapabilities() {
        return List.of(
                "CREATE_PROJECT",
                "IMPORT_MARKDOWN",
                "SHOW_VISUAL_OUTPUT",
                "MANUAL_EDITING",
                "EXPORT_MARKDOWN",
                "EXPORT_PNG",
                "EXPORT_SVG",
                "SAVE_DMS",
                "LOAD_DMS",
                "AI_RESOURCES",
                "THEORY_HELP");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
