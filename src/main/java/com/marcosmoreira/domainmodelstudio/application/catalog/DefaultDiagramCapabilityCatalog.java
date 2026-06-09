package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Catálogo de capacidades reales por tipo visible de proyecto.
 *
 * <p>El servicio no inventa capacidades en tiempo de ejecución: indexa las definiciones
 * oficiales y devuelve un conjunto inmutable. Si un tipo promete exportar, importar o abrir
 * ayuda académica, esa promesa debe nacer aquí y estar respaldada por implementación y tests.</p>
 */
public final class DefaultDiagramCapabilityCatalog implements DiagramCapabilityCatalog {

    private static final Map<DiagramTypeId, DiagramCapabilitySet> OFFICIAL_CAPABILITIES = officialCapabilities();

    private final Map<DiagramTypeId, DiagramCapabilitySet> capabilitiesByType;

    public DefaultDiagramCapabilityCatalog() {
        this(OFFICIAL_CAPABILITIES);
    }

    public DefaultDiagramCapabilityCatalog(Map<DiagramTypeId, DiagramCapabilitySet> capabilitiesByType) {
        Objects.requireNonNull(capabilitiesByType, "capabilitiesByType");
        this.capabilitiesByType = Map.copyOf(capabilitiesByType);
    }

    @Override
    public DiagramCapabilitySet capabilitiesOf(DiagramTypeId diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        return capabilitiesByType.getOrDefault(diagramTypeId, DiagramCapabilitySet.empty());
    }

    private static Map<DiagramTypeId, DiagramCapabilitySet> officialCapabilities() {
        List<DiagramTypeOfficialDefinition> definitions = DefaultDiagramTypeDefinitions.all();
        Map<DiagramTypeId, DiagramCapabilitySet> indexed = definitions.stream()
                .collect(Collectors.toMap(
                        DiagramTypeOfficialDefinition::id,
                        DiagramTypeOfficialDefinition::capabilities,
                        duplicateGuard(),
                        LinkedHashMap::new));
        return Map.copyOf(indexed);
    }

    private static <T> java.util.function.BinaryOperator<T> duplicateGuard() {
        return (left, right) -> {
            throw new IllegalArgumentException("Capacidad duplicada en catálogo oficial.");
        };
    }
}
