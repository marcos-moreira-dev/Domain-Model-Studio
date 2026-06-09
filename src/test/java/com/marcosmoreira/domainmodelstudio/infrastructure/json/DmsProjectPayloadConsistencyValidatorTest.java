package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import org.junit.jupiter.api.Test;

class DmsProjectPayloadConsistencyValidatorTest {

    @Test
    void shouldRejectSpecializedProjectWithoutItsPrimaryDocument() {
        DiagramProject invalid = DiagramProject.blank("mapa_vacio", "Mapa vacío", DiagramTypeId.ADMIN_MODULE_MAP);

        assertThrows(IllegalArgumentException.class, () -> new DmsProjectJsonWriter().write(invalid));
    }

    @Test
    void shouldRejectFreeGraphWithoutItsPrimaryDocument() {
        DiagramProject invalid = DiagramProject.blank("grafo_vacio", "Grafo vacío", DiagramTypeId.FREE_GRAPH);

        assertThrows(IllegalArgumentException.class, () -> new DmsProjectJsonWriter().write(invalid));
    }

    @Test
    void shouldRejectLogicalBusinessProjectWithoutItsPrimaryDocument() {
        DiagramProject invalid = DiagramProject.blank("levantamiento_vacio", "Levantamiento vacío",
                DiagramTypeId.LOGICAL_BUSINESS_INTAKE);

        assertThrows(IllegalArgumentException.class, () -> new DmsProjectJsonWriter().write(invalid));
    }

    @Test
    void shouldAcceptLogicalBusinessGraphWithRegisteredPayload() {
        DiagramProject valid = DiagramProject.blank("grafo_logico", "Grafo lógico",
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH)
                .withLogicalBusinessGraphDocument(LogicalBusinessGraphDocument.blank("Grafo lógico"));

        new DmsProjectJsonWriter().write(valid);
    }

    @Test
    void shouldRejectLogicalBusinessGraphWithoutRegisteredPayload() {
        DiagramProject invalid = DiagramProject.blank("grafo_logico_vacio", "Grafo lógico vacío",
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH);

        assertThrows(IllegalArgumentException.class, () -> new DmsProjectJsonWriter().write(invalid));
    }

    @Test
    void shouldRejectConceptualProjectWithLogicalBusinessPayload() {
        DiagramProject invalid = DiagramProject.blank("conceptual_con_levantamiento", "Conceptual con levantamiento",
                DiagramTypeId.CONCEPTUAL_MODEL)
                .withLogicalBusinessDocument(LogicalBusinessDocument.blank("Levantamiento escondido"));

        assertThrows(IllegalArgumentException.class, () -> new DmsProjectJsonWriter().write(invalid));
    }

    @Test
    void shouldRejectConceptualProjectWithSpecializedPayload() {
        DiagramProject invalid = DiagramProject.blank("conceptual_confuso", "Conceptual confuso", DiagramTypeId.CONCEPTUAL_MODEL)
                .withModuleMap(new com.marcosmoreira.domainmodelstudio.application.modulemap.CreateModuleMapUseCase()
                        .createBlank("Mapa escondido"));

        assertThrows(IllegalArgumentException.class, () -> new DmsProjectJsonWriter().write(invalid));
    }

    @Test
    void shouldRejectConceptualProjectWithFreeGraphPayload() {
        DiagramProject invalid = DiagramProject.blank("conceptual_con_grafo", "Conceptual con grafo", DiagramTypeId.CONCEPTUAL_MODEL)
                .withFreeGraph(FreeGraphDocument.blank("Grafo escondido"));

        assertThrows(IllegalArgumentException.class, () -> new DmsProjectJsonWriter().write(invalid));
    }
}
