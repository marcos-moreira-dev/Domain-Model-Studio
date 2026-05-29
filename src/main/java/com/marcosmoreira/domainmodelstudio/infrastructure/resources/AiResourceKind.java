package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

/** Clasificación editorial de los recursos IA exportados al usuario. */
public enum AiResourceKind {
    GRAMMAR("Gramática", "Copiar a GPT para explicar el dialecto Markdown aceptado por Domain Model Studio."),
    AI_TEMPLATE("Plantilla IA", "Rellenar con información del cliente antes de importar o revisar."),
    PROMPT_GUIDE("Prompt IA", "Copiar a GPT o Claude para producir Markdown compatible con la gramática oficial."),
    MINIMAL_EXAMPLE("Ejemplo mínimo", "Usar como muestra pequeña para validar estructura y primer import."),
    FULL_EXAMPLE("Ejemplo completo", "Usar como referencia rica para comparar cobertura de un levantamiento real."),
    LOGICAL_MASTER_TEMPLATE("Plantilla lógica maestra", "Usar como fuente lógica canónica del levantamiento antes de construir otros artefactos revisables."),
    REFERENCE("Referencia", "Usar como lectura o planificación; no asumir importabilidad automática.");

    private final String displayName;
    private final String recommendedUse;

    AiResourceKind(String displayName, String recommendedUse) {
        this.displayName = displayName;
        this.recommendedUse = recommendedUse;
    }

    public String displayName() {
        return displayName;
    }

    public String recommendedUse() {
        return recommendedUse;
    }
}
