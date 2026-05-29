package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.batch;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.text.Normalizer;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/** Resuelve sugerencias de tipo a partir de sufijos humanos en nombres de archivo. */
public final class MarkdownFileTypeSuffixResolver {

    private final Map<String, DiagramTypeId> suffixes = new LinkedHashMap<>();

    public MarkdownFileTypeSuffixResolver() {
        register("levantamiento-logico", DiagramTypeId.LOGICAL_BUSINESS_INTAKE);
        register("grafo-logico", DiagramTypeId.LOGICAL_BUSINESS_GRAPH);
        register("grafo-logico-negocio", DiagramTypeId.LOGICAL_BUSINESS_GRAPH);
        register("modelo-conceptual", DiagramTypeId.CONCEPTUAL_MODEL);
        register("diccionario-datos", DiagramTypeId.DATA_DICTIONARY);
        register("bpmn-basico", DiagramTypeId.BPMN_BASIC);
        register("flujo-operativo", DiagramTypeId.OPERATIONAL_FLOW);
        register("c4-contexto", DiagramTypeId.C4_CONTEXT);
        register("c4-contenedores", DiagramTypeId.C4_CONTAINERS);
        register("despliegue", DiagramTypeId.TECHNICAL_DEPLOYMENT);
        register("despliegue-tecnico", DiagramTypeId.TECHNICAL_DEPLOYMENT);
        register("uml-clases", DiagramTypeId.UML_CLASS);
        register("uml-casos-uso", DiagramTypeId.UML_USE_CASE);
        register("uml-actividad", DiagramTypeId.UML_ACTIVITY);
        register("uml-secuencia", DiagramTypeId.UML_SEQUENCE);
        register("uml-estados", DiagramTypeId.UML_STATE);
        register("mapa-modulos", DiagramTypeId.ADMIN_MODULE_MAP);
        register("roles-permisos", DiagramTypeId.ROLES_PERMISSIONS_MAP);
        register("flujo-pantallas", DiagramTypeId.SCREEN_FLOW);
        register("wireframes", DiagramTypeId.ADMIN_WIREFRAMES);
        register("wireframes-administrativos", DiagramTypeId.ADMIN_WIREFRAMES);
        register("grafo-libre", DiagramTypeId.FREE_GRAPH);
    }

    public Optional<DiagramTypeId> resolve(String fileName) {
        String normalized = normalize(fileName);
        if (normalized.endsWith(".markdown")) {
            normalized = normalized.substring(0, normalized.length() - ".markdown".length()) + ".md";
        }
        for (Map.Entry<String, DiagramTypeId> entry : suffixes.entrySet()) {
            if (normalized.endsWith("-" + entry.getKey() + ".md") || normalized.equals(entry.getKey() + ".md")) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    private void register(String suffix, DiagramTypeId diagramTypeId) {
        suffixes.put(suffix, diagramTypeId);
    }

    private String normalize(String value) {
        return Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replace('_', '-')
                .replace(' ', '-');
    }
}
