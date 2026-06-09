package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Parser específico para líneas de atributos dentro de una entidad. */
final class MarkdownAttributeParser {

    private static final Pattern TAG_PATTERN = Pattern.compile("\\[([^\\]]+)]");

    AttributeElement parse(String entityId, String attributeLine) throws MarkdownModelParsingException {
        String content = attributeLine.substring(1).trim();
        Set<AttributeTag> tags = EnumSet.noneOf(AttributeTag.class);
        Matcher matcher = TAG_PATTERN.matcher(content);
        while (matcher.find()) {
            tags.add(parseTag(matcher.group(1).trim()));
        }

        String visibleName = TAG_PATTERN.matcher(content).replaceAll("").trim();
        if (visibleName.toLowerCase(Locale.ROOT).startsWith("pk ")) {
            tags.add(AttributeTag.PRIMARY_KEY);
            visibleName = visibleName.substring(3).trim();
        }
        if (visibleName.isBlank()) {
            throw new MarkdownModelParsingException("Atributo vacío en entidad " + entityId + ": " + attributeLine);
        }

        String attributeId = buildAttributeId(entityId, visibleName, tags);
        String label = tags.contains(AttributeTag.PRIMARY_KEY) && "id".equalsIgnoreCase(visibleName)
                ? "pk id"
                : visibleName;
        return new AttributeElement(DiagramElementId.of(attributeId), label, tags, "");
    }

    private AttributeTag parseTag(String rawTag) throws MarkdownModelParsingException {
        String tag = rawTag.toLowerCase(Locale.ROOT).replace('-', '_');
        return switch (tag) {
            case "pk", "primary_key", "primarykey" -> AttributeTag.PRIMARY_KEY;
            case "partial_key", "partialkey", "clave_parcial", "claveparcial" -> AttributeTag.PARTIAL_KEY;
            case "optional", "opcional" -> AttributeTag.OPTIONAL;
            case "derived", "derivado", "computed", "calculado" -> AttributeTag.DERIVED;
            case "unique", "unico", "único" -> AttributeTag.UNIQUE;
            case "multivalued", "multivaluado", "multi_valued" -> AttributeTag.MULTIVALUED;
            case "composite", "compuesto" -> AttributeTag.COMPOSITE;
            case "sensitive", "sensible" -> AttributeTag.SENSITIVE;
            default -> throw new MarkdownModelParsingException("Tag de atributo no soportado: [" + rawTag + "]");
        };
    }

    private String buildAttributeId(String entityId, String visibleName, Set<AttributeTag> tags) {
        String base = MarkdownTextUtils.toStableId(visibleName);
        if ((tags.contains(AttributeTag.PRIMARY_KEY) || tags.contains(AttributeTag.PARTIAL_KEY)) && "id".equals(base)) {
            return entityId + "_id";
        }
        return entityId + "_" + base;
    }
}
