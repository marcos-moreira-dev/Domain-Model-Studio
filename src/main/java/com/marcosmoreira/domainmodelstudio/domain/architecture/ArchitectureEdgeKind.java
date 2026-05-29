package com.marcosmoreira.domainmodelstudio.domain.architecture;

/** Tipos de relación para C4 y despliegue técnico. */
public enum ArchitectureEdgeKind {
    USES("Usa"),
    DEPENDS_ON("Depende de"),
    INTEGRATES_WITH("Integra con"),
    CALLS("Llama"),
    READS_WRITES("Lee/escribe"),
    PUBLISHES("Publica"),
    SUBSCRIBES("Consume"),
    DEPLOYS_TO("Se despliega en"),
    CONNECTS_TO("Conecta con"),
    HOSTS("Aloja");

    private final String displayName;
    ArchitectureEdgeKind(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }
    @Override public String toString() { return displayName; }
}
