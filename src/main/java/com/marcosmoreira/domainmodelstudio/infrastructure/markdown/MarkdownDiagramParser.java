package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectMetadata;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectType;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.Cardinality;
import com.marcosmoreira.domainmodelstudio.domain.er.ParticipationType;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleSheet;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Parser de Markdown estructurado para la primera gramática MVP.
 *
 * <p>La implementación es deliberadamente estricta y pequeña. No pretende ser un parser
 * Markdown completo; solo interpreta el subconjunto documentado para Domain Model Studio.</p>
 */
public final class MarkdownDiagramParser implements MarkdownModelParser {

    private enum Section {
        NONE,
        ENTITIES,
        RELATIONSHIPS
    }

    private final MarkdownAttributeParser attributeParser = new MarkdownAttributeParser();

    @Override
    public DiagramProject parse(Path markdownFile) throws IOException, MarkdownModelParsingException {
        Objects.requireNonNull(markdownFile, "El archivo Markdown no puede ser null");
        String content = Files.readString(markdownFile, StandardCharsets.UTF_8);
        return parse(content, markdownFile.toString());
    }

    @Override
    public DiagramProject parse(String markdownContent, String sourceName) throws MarkdownModelParsingException {
        Objects.requireNonNull(markdownContent, "El contenido Markdown no puede ser null");
        MarkdownImportDocument importDocument = MarkdownImportDocument.parse(markdownContent);
        MarkdownFrontMatter frontMatter = importDocument.frontMatter();
        ensureConceptualDiagramType(frontMatter);
        ParsedBlocks blocks = readBlocks(importDocument.body().lines().toList());

        DiagramModel model = buildModel(blocks);
        ProjectMetadata metadata = buildMetadata(frontMatter, sourceName);
        return new DiagramProject(
                metadata,
                model,
                DiagramLayouts.forNotation(metadata.activeNotation()),
                DiagramStyleSheet.defaults()
        );
    }


    private void ensureConceptualDiagramType(MarkdownFrontMatter frontMatter) throws MarkdownModelParsingException {
        String raw = frontMatter.valueOrDefault("diagram_type", "");
        if (raw.isBlank()) {
            return;
        }
        String normalized = raw.strip()
                .replace("\"", "")
                .toLowerCase(Locale.ROOT)
                .replace('_', '-')
                .replace(' ', '-');
        if (!normalized.equals("conceptual-model")
                && !normalized.equals("modelo-conceptual")
                && !normalized.equals("conceptualmodel")) {
            throw new MarkdownModelParsingException("Este Markdown declara el tipo " + raw
                    + ", pero este lector solo interpreta modelos conceptuales.");
        }
    }

    private ParsedBlocks readBlocks(List<String> lines) throws MarkdownModelParsingException {
        Section section = Section.NONE;
        ParsedEntityBlock currentEntity = null;
        ParsedRelationshipBlock currentRelationship = null;
        ParsedBlocks blocks = new ParsedBlocks();

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isBlank() || line.equals("---") || line.startsWith("<!--")) {
                continue;
            }
            if (line.startsWith("# ")) {
                section = parseSection(line);
                currentEntity = null;
                currentRelationship = null;
                continue;
            }
            if (line.startsWith("## ")) {
                currentEntity = null;
                currentRelationship = null;
                if (section == Section.ENTITIES) {
                    currentEntity = new ParsedEntityBlock(line.substring(3).trim());
                    blocks.entities.add(currentEntity);
                } else if (section == Section.RELATIONSHIPS) {
                    currentRelationship = new ParsedRelationshipBlock(line.substring(3).trim());
                    blocks.relationships.add(currentRelationship);
                }
                continue;
            }

            if (section == Section.ENTITIES && currentEntity != null) {
                readEntityLine(currentEntity, line);
            } else if (section == Section.RELATIONSHIPS && currentRelationship != null) {
                readRelationshipLine(currentRelationship, line);
            }
        }

        if (blocks.entities.isEmpty()) {
            throw new MarkdownModelParsingException("El Markdown no contiene entidades en la sección # Entidades.");
        }
        return blocks;
    }

    private Section parseSection(String line) {
        String normalized = line.substring(2).trim().toLowerCase(Locale.ROOT);
        if (normalized.equals("entidades")) {
            return Section.ENTITIES;
        }
        if (normalized.equals("relaciones")) {
            return Section.RELATIONSHIPS;
        }
        return Section.NONE;
    }

    private void readEntityLine(ParsedEntityBlock entity, String line) throws MarkdownModelParsingException {
        if (line.startsWith("-")) {
            entity.addAttribute(attributeParser.parse(entity.id(), line));
        } else if (MarkdownTextUtils.isPropertyLine(line)) {
            entity.applyProperty(MarkdownTextUtils.keyBeforeColon(line), MarkdownTextUtils.valueAfterColon(line));
        }
    }

    private void readRelationshipLine(ParsedRelationshipBlock relationship, String line) {
        if (MarkdownTextUtils.isPropertyLine(line)) {
            relationship.applyProperty(MarkdownTextUtils.keyBeforeColon(line), MarkdownTextUtils.valueAfterColon(line));
        }
    }

    private DiagramModel buildModel(ParsedBlocks blocks) throws MarkdownModelParsingException {
        List<EntityElement> entities = new ArrayList<>();
        Map<String, DiagramElementId> entityIdsByHeaderOrId = new LinkedHashMap<>();

        for (ParsedEntityBlock block : blocks.entities) {
            EntityElement entity = new EntityElement(
                    DiagramElementId.of(block.id()),
                    block.headerName(),
                    parseEntityKind(block.kind()),
                    block.module(),
                    block.description(),
                    block.attributes()
            );
            entities.add(entity);
            entityIdsByHeaderOrId.put(block.headerName().toLowerCase(Locale.ROOT), entity.id());
            entityIdsByHeaderOrId.put(entity.id().value().toLowerCase(Locale.ROOT), entity.id());
        }

        List<RelationshipElement> relationships = new ArrayList<>();
        for (ParsedRelationshipBlock block : blocks.relationships) {
            relationships.add(buildRelationship(block, entityIdsByHeaderOrId));
        }
        return new DiagramModel(entities, relationships);
    }

    private RelationshipElement buildRelationship(
            ParsedRelationshipBlock block,
            Map<String, DiagramElementId> entityIdsByHeaderOrId
    ) throws MarkdownModelParsingException {
        DiagramElementId from = resolveEntity(block.valueOrDefault("from", ""), entityIdsByHeaderOrId, block.headerName());
        DiagramElementId to = resolveEntity(block.valueOrDefault("to", ""), entityIdsByHeaderOrId, block.headerName());
        String fromCardinality = block.valueOrDefault("from_cardinality", "");
        String toCardinality = block.valueOrDefault("to_cardinality", "");
        if (!Cardinality.isSupported(fromCardinality) || !Cardinality.isSupported(toCardinality)) {
            throw new MarkdownModelParsingException("Cardinalidad inválida en relación " + block.headerName()
                    + ": from_cardinality=" + fromCardinality + ", to_cardinality=" + toCardinality);
        }
        return new RelationshipElement(
                DiagramElementId.of(block.valueOrDefault("id", MarkdownTextUtils.toStableId(block.headerName()))),
                block.headerName(),
                from,
                to,
                Cardinality.of(fromCardinality),
                Cardinality.of(toCardinality),
                parseRelationshipKind(block.valueOrDefault("kind", "regular")),
                parseParticipation(block.valueOrDefault("from_participation", "unspecified")),
                parseParticipation(block.valueOrDefault("to_participation", "unspecified")),
                block.valueOrDefault("description", "")
        );
    }

    private DiagramElementId resolveEntity(
            String rawReference,
            Map<String, DiagramElementId> entityIdsByHeaderOrId,
            String relationshipName
    ) throws MarkdownModelParsingException {
        if (rawReference == null || rawReference.isBlank()) {
            throw new MarkdownModelParsingException("La relación " + relationshipName + " no define from/to completo.");
        }
        DiagramElementId id = entityIdsByHeaderOrId.get(rawReference.trim().toLowerCase(Locale.ROOT));
        if (id == null) {
            throw new MarkdownModelParsingException("La relación " + relationshipName
                    + " referencia una entidad no declarada: " + rawReference);
        }
        return id;
    }

    private ProjectMetadata buildMetadata(MarkdownFrontMatter frontMatter, String sourceName) {
        String title = frontMatter.value("title")
                .or(() -> frontMatter.value("name"))
                .filter(value -> !value.isBlank())
                .orElse("Modelo importado desde Markdown");
        String id = frontMatter.valueOrDefault("id", MarkdownTextUtils.toStableId(title));
        return new ProjectMetadata(
                id,
                title,
                ProjectType.fromStoredValue(frontMatter.valueOrDefault("project_type", "CONCEPTUAL_MODEL")),
                frontMatter.valueOrDefault("version", "0.1.0"),
                frontMatter.valueOrDefault("status", "draft"),
                parseNotation(frontMatter.valueOrDefault("notation", "chen")),
                sourceName == null ? "" : sourceName,
                frontMatter.valueOrDefault("description", "")
        );
    }

    private NotationType parseNotation(String raw) {
        String normalized = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
        if (normalized.equals("crow_foot") || normalized.equals("crows_foot") || normalized.equals("pata_de_gallo")) {
            return NotationType.CROWS_FOOT;
        }
        return NotationType.CHEN;
    }

    private EntityKind parseEntityKind(String raw) {
        return "weak".equalsIgnoreCase(raw) || "debil".equalsIgnoreCase(raw) || "débil".equalsIgnoreCase(raw)
                ? EntityKind.WEAK
                : EntityKind.STRONG;
    }

    private RelationshipKind parseRelationshipKind(String raw) {
        String normalized = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "identifying", "identificadora" -> RelationshipKind.IDENTIFYING;
            case "associative", "asociativa" -> RelationshipKind.ASSOCIATIVE;
            default -> RelationshipKind.REGULAR;
        };
    }

    private ParticipationType parseParticipation(String raw) {
        String normalized = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "partial", "parcial" -> ParticipationType.PARTIAL;
            case "total" -> ParticipationType.TOTAL;
            default -> ParticipationType.UNSPECIFIED;
        };
    }

    private static final class ParsedBlocks {
        private final List<ParsedEntityBlock> entities = new ArrayList<>();
        private final List<ParsedRelationshipBlock> relationships = new ArrayList<>();
    }
}
