package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import java.util.Locale;
import java.util.Objects;

/** Política editorial para que Recursos IA sea un contrato de producto y no una carpeta ambigua. */
public final class AiResourceProductizationPolicy {

    private AiResourceProductizationPolicy() {
    }

    public static AiResourceKind kindOf(AiResourceDescriptor resource) {
        Objects.requireNonNull(resource, "resource");
        String id = resource.id().toLowerCase(Locale.ROOT);
        String fileName = resource.fileName().toLowerCase(Locale.ROOT);
        if (id.contains("prompt") || fileName.contains("_prompt_")) {
            return AiResourceKind.PROMPT_GUIDE;
        }
        if (fileName.contains("levantamiento-logico/logical_business_intake_template")) {
            return AiResourceKind.LOGICAL_MASTER_TEMPLATE;
        }
        if (id.contains("gramatica") || fileName.matches("[0-9]{2}_.+_gramatica\\.md")) {
            return AiResourceKind.GRAMMAR;
        }
        if (fileName.contains("official-markdown/plantillas/")) {
            return AiResourceKind.AI_TEMPLATE;
        }
        if (fileName.contains("_gordito") || id.contains("gordito") || id.contains("uens")) {
            return AiResourceKind.FULL_EXAMPLE;
        }
        if (fileName.contains("_minimo") || fileName.contains("_mínimo") || id.contains("minimo")) {
            return AiResourceKind.MINIMAL_EXAMPLE;
        }
        return AiResourceKind.REFERENCE;
    }

    public static String importContract(AiResourceDescriptor resource) {
        Objects.requireNonNull(resource, "resource");
        if (!resource.exportable()) {
            return "alias interno no exportado";
        }
        if (!resource.importableByApplication()) {
            if (kindOf(resource) == AiResourceKind.PROMPT_GUIDE) {
                return "prompt no importable; copiar como instrucción para generar Markdown compatible";
            }
            return "referencia no importable; usar solo como guía para IA";
        }
        return switch (kindOf(resource)) {
            case LOGICAL_MASTER_TEMPLATE -> "importable como expediente lógico documental; revisar antes de reutilizar como fuente";
            case AI_TEMPLATE -> "importable cuando se rellena con datos reales y conserva diagram_type";
            case PROMPT_GUIDE -> "no se importa como proyecto; se usa para generar Markdown compatible";
            case GRAMMAR -> "importable si el Markdown producido respeta el dialecto y front matter del tipo";
            case MINIMAL_EXAMPLE, FULL_EXAMPLE -> "importable como ejemplo oficial del tipo indicado";
            case REFERENCE -> "importable por la aplicación según el descriptor, pero revisar alcance antes de usar";
        };
    }

    public static String recommendedUse(AiResourceDescriptor resource) {
        AiResourceKind kind = kindOf(resource);
        if (kind == AiResourceKind.PROMPT_GUIDE) {
            return "Copiar como instrucción IA; luego importar el Markdown generado, no este archivo.";
        }
        if (kind == AiResourceKind.LOGICAL_MASTER_TEMPLATE) {
            return "Pegar en GPT junto con el levantamiento del cliente; validar ENT/ATR/REL antes de reutilizar como fuente.";
        }
        return kind.recommendedUse();
    }
}
