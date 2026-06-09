package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 23: inferencia prudente de agregación/composición desde miembros de código. */
class Tanda23SourceCodeRelationSemanticPolicyTest {

    @Test
    void shouldDetectCompositionFromLifecycleAnnotations() {
        assertEquals(ParsedCodeRelationKind.COMPOSITION,
                ParsedCodeRelationSemanticPolicy.kindForField("detalles", "List<FacturaDetalle>",
                        List.of("@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)")));
    }

    @Test
    void shouldDetectAggregationFromCollectionOrJpaReference() {
        assertEquals(ParsedCodeRelationKind.AGGREGATION,
                ParsedCodeRelationSemanticPolicy.kindForField("pagos", "List<Pago>", List.of()));
        assertEquals(ParsedCodeRelationKind.AGGREGATION,
                ParsedCodeRelationSemanticPolicy.kindForField("cliente", "Cliente", List.of("@ManyToOne")));
    }

    @Test
    void shouldKeepInjectedServicesAsDependencies() {
        assertEquals(ParsedCodeRelationKind.DEPENDENCY,
                ParsedCodeRelationSemanticPolicy.kindForField("clienteRepository", "ClienteRepository", List.of()));
        assertEquals(ParsedCodeRelationKind.DEPENDENCY,
                ParsedCodeRelationSemanticPolicy.kindForField("servicio", "ClienteService", List.of("@Inject")));
    }
}
