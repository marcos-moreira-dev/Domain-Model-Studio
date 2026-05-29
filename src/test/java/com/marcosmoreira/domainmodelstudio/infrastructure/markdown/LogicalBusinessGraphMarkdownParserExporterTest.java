package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind;
import org.junit.jupiter.api.Test;

class LogicalBusinessGraphMarkdownParserExporterTest {

    @Test
    void parserShouldImportOfficialTableContractThroughDispatcher() throws Exception {
        DiagramProject project = new DiagramMarkdownImportDispatcher(new DefaultDiagramTypeRegistry())
                .parse(sampleMarkdown(), "sample-logical-business-graph.md");

        assertEquals(DiagramTypeId.LOGICAL_BUSINESS_GRAPH, project.metadata().diagramTypeId());
        LogicalBusinessGraphDocument document = project.logicalBusinessGraphDocument().orElseThrow();
        assertEquals(5, document.nodes().size());
        assertEquals(4, document.edges().size());
        assertEquals(LogicalBusinessGraphNodeKind.MACRO_FLOW, document.nodeByCode("MF-001").orElseThrow().kind());
        assertEquals(LogicalBusinessGraphRelationKind.CONTAINS, document.edges().get(0).relationKind());
        assertEquals("sample-logical-business-graph.md", project.metadata().sourceMarkdownPath());
    }

    @Test
    void exporterShouldKeepLegendAndRoundTripImportability() throws Exception {
        DiagramProject imported = new LogicalBusinessGraphMarkdownParser().parse(sampleMarkdown(), "roundtrip.md");
        String exported = new LogicalBusinessGraphMarkdownExporter().export(imported);

        assertTrue(exported.contains("diagram_type: \"logical-business-graph\""));
        assertTrue(exported.contains("# Leyenda de abreviaciones"));
        assertTrue(exported.contains("MF | Macroflujo"));
        assertTrue(exported.contains("FL | Flujo o microflujo"));
        assertTrue(exported.contains("CU | Caso de uso"));
        assertTrue(exported.contains("ACC | Acción transformadora"));
        assertTrue(exported.contains("INV | Invariante"));

        DiagramProject reparsed = new LogicalBusinessGraphMarkdownParser().parse(exported, "exported.md");
        assertEquals(5, reparsed.logicalBusinessGraphDocument().orElseThrow().nodes().size());
        assertEquals(4, reparsed.logicalBusinessGraphDocument().orElseThrow().edges().size());
    }

    @Test
    void exporterShouldKeepEmptyGraphPlaceholderImportable() throws Exception {
        DiagramProject project = DiagramProject.blank(
                "empty-lbg", "Grafo lógico vacío", DiagramTypeId.LOGICAL_BUSINESS_GRAPH)
                .withLogicalBusinessGraphDocument(LogicalBusinessGraphDocument.blank("Grafo lógico vacío"));

        String exported = new LogicalBusinessGraphMarkdownExporter().export(project);

        assertTrue(exported.contains("| MF-001 | MF | Macroflujo pendiente"));
        assertTrue(exported.contains("| FL-001 | FL | Flujo pendiente"));
        assertTrue(exported.contains("| rel-001 | MF-001 | contiene | FL-001"));
        DiagramProject reparsed = new LogicalBusinessGraphMarkdownParser().parse(exported, "empty-exported.md");
        assertEquals(2, reparsed.logicalBusinessGraphDocument().orElseThrow().nodes().size());
        assertEquals(1, reparsed.logicalBusinessGraphDocument().orElseThrow().edges().size());
    }

    private static String sampleMarkdown() {
        return """
                ---
                diagram_type: "logical-business-graph"
                name: "Grafo lógico — matrícula"
                version: "v0.1"
                document_date: "2026-05-24"
                ---

                # Nodos

                | Código | Tipo | Título | Descripción | Estado | Referencias |
                |---|---|---|---|---|---|
                | MF-001 | MF | Gestión académica | Agrupa matrícula y calificaciones. | validado parcialmente | MF-001 |
                | FL-001 | FL | Registro de matrícula | Microflujo de inscripción y asignación. | en revisión | FL-001 |
                | CU-001 | CU | Registrar estudiante | Caso de uso para crear o actualizar estudiante. | en revisión | CU-001 |
                | ACC-001 | ACC | Guardar matrícula | Acción que persiste matrícula y auditoría. | borrador | ACC-001 |
                | INV-001 | INV | Matrícula única vigente | Evita doble matrícula vigente en el mismo periodo. | borrador | INV-001 |

                # Relaciones

                | ID | Origen | Relación | Destino | Descripción |
                |---|---|---|---|---|
                | rel-001 | MF-001 | contiene | FL-001 | El macroflujo contiene el microflujo. |
                | rel-002 | FL-001 | usa | CU-001 | El flujo usa el caso de uso. |
                | rel-003 | CU-001 | ejecuta | ACC-001 | El caso ejecuta la acción. |
                | rel-004 | ACC-001 | protege | INV-001 | La acción protege la invariante. |

                # Observaciones

                Grafo lógico importable para validar el contrato Markdown.
                """;
    }
}
