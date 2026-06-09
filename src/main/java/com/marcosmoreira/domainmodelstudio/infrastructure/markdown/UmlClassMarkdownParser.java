package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Importa Markdown oficial de UML Clases hacia un proyecto editable. */
public final class UmlClassMarkdownParser implements MarkdownModelParser {

    @Override
    public DiagramProject parse(Path markdownFile) throws IOException, MarkdownModelParsingException {
        Objects.requireNonNull(markdownFile, "markdownFile");
        return parse(Files.readString(markdownFile, StandardCharsets.UTF_8), markdownFile.toString());
    }
    @Override
    public DiagramProject parse(String markdownContent, String sourceName) throws MarkdownModelParsingException {
        Objects.requireNonNull(markdownContent, "markdownContent");
        MarkdownImportDocument importDocument = MarkdownImportDocument.parse(markdownContent);
        MarkdownFrontMatter frontMatter = importDocument.frontMatter();
        String title = frontMatter.valueOrDefault("name", "UML Clases importado");
        Map<String, UmlModuleGroup> modules = new LinkedHashMap<>();
        Map<String, UmlClassNode> classes = new LinkedHashMap<>();
        List<UmlClassRelation> relations = new ArrayList<>();
        List<UmlClassDiagramView> views = new ArrayList<>();
        parseBody(importDocument.body(), modules, classes, relations, views);
        if (classes.isEmpty()) {
            throw new MarkdownModelParsingException("El diagrama UML Clases no contiene clases reconocibles.");
        }
        UmlClassDiagramDocument document;
        try {
            document = new UmlClassDiagramDocument(
                    title,
                    frontMatter.valueOrDefault("version", "borrador"),
                    LocalDate.now(),
                    new ArrayList<>(modules.values()),
                    new ArrayList<>(classes.values()),
                    relations,
                    views,
                    "Importado desde Markdown oficial de UML Clases.");
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir UML Clases: " + exception.getMessage(), exception);
        }
        return MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title), title, DiagramTypeId.UML_CLASS)
                        .withUmlClassDiagram(document),
                sourceName);
    }

    private static void parseBody(
            String body,
            Map<String, UmlModuleGroup> modules,
            Map<String, UmlClassNode> classes,
            List<UmlClassRelation> relations,
            List<UmlClassDiagramView> views
    ) throws MarkdownModelParsingException {
        Section section = Section.NONE;
        PendingModule pendingModule = null;
        PendingClass pendingClass = null;
        UmlClassMarkdownViewSectionParser.PendingView pendingView = null;
        Subsection classSubsection = Subsection.NONE;
        int relationCounter = 1;

        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank() || line.startsWith(">")) {
                continue;
            }
            String lower = line.toLowerCase(Locale.ROOT);
            if (line.startsWith("# ")) {
                if (pendingModule != null) {
                    putModule(modules, pendingModule.toModule());
                    pendingModule = null;
                }
                if (pendingClass != null) {
                    putClass(classes, pendingClass.toClass(modules));
                    pendingClass = null;
                }
                if (pendingView != null) {
                    UmlClassMarkdownViewSectionParser.putView(views, pendingView);
                    pendingView = null;
                }
                classSubsection = Subsection.NONE;
                if (lower.contains("paquete") || lower.contains("módulo") || lower.contains("modulo")) {
                    section = Section.MODULES;
                } else if (lower.contains("clase") || lower.contains("uml clases")) {
                    section = Section.CLASSES;
                } else if (lower.contains("vista")) {
                    section = Section.VIEWS;
                } else if (lower.contains("relacion") || lower.contains("relación")) {
                    section = Section.RELATIONS;
                } else {
                    section = Section.NONE;
                }
                continue;
            }
            if (line.startsWith("## ")) {
                if (pendingModule != null) {
                    putModule(modules, pendingModule.toModule());
                    pendingModule = null;
                }
                if (pendingClass != null) {
                    putClass(classes, pendingClass.toClass(modules));
                    pendingClass = null;
                }
                if (pendingView != null) {
                    UmlClassMarkdownViewSectionParser.putView(views, pendingView);
                    pendingView = null;
                }
                classSubsection = Subsection.NONE;
                String title = line.substring(3).strip();
                if (section == Section.MODULES) {
                    pendingModule = new PendingModule(title);
                } else if (section == Section.CLASSES) {
                    pendingClass = new PendingClass(title);
                } else if (section == Section.VIEWS) {
                    pendingView = UmlClassMarkdownViewSectionParser.pending(title);
                }
                continue;
            }
            if (section == Section.CLASSES && pendingClass != null && MarkdownTextUtils.isPropertyLine(line)) {
                String key = normalizeKey(MarkdownTextUtils.keyBeforeColon(line));
                String value = MarkdownTextUtils.valueAfterColon(line);
                if (key.equals("id")) {
                    pendingClass.id = MarkdownTextUtils.toStableId(value);
                } else if (key.equals("paquete") || key.equals("modulo") || key.equals("módulo")) {
                    pendingClass.moduleId = value;
                } else if (key.equals("tipo")) {
                    pendingClass.kind = parseClassKind(value);
                } else if (key.equals("visibilidad")) {
                    pendingClass.visibility = parseVisibility(value);
                } else if (key.equals("responsabilidad")) {
                    pendingClass.responsibility = value;
                } else if (key.equals("descripcion") || key.equals("descripción")) {
                    pendingClass.description = value;
                } else if (key.equals("notas")) {
                    pendingClass.notes = value;
                } else if (key.equals("atributos")) {
                    classSubsection = Subsection.ATTRIBUTES;
                } else if (key.equals("metodos") || key.equals("métodos")) {
                    classSubsection = Subsection.METHODS;
                }
                continue;
            }
            if (section == Section.VIEWS && pendingView != null && MarkdownTextUtils.isPropertyLine(line)) {
                UmlClassMarkdownViewSectionParser.applyProperty(pendingView, line);
                continue;
            }
            if (section == Section.MODULES && pendingModule != null && MarkdownTextUtils.isPropertyLine(line)) {
                String key = normalizeKey(MarkdownTextUtils.keyBeforeColon(line));
                String value = MarkdownTextUtils.valueAfterColon(line);
                if (key.equals("id")) {
                    pendingModule.id = MarkdownTextUtils.toStableId(value);
                } else if (key.equals("ruta") || key.equals("path")) {
                    pendingModule.path = value;
                } else if (key.equals("proposito") || key.equals("propósito") || key.equals("responsabilidad")) {
                    pendingModule.description = value;
                } else if (key.equals("notas")) {
                    pendingModule.notes = value;
                }
                continue;
            }
            if (section == Section.CLASSES && pendingClass != null && line.startsWith("- ")) {
                String item = line.substring(2).strip();
                if (classSubsection == Subsection.ATTRIBUTES) {
                    pendingClass.members.add(parseMember(item, UmlMemberKind.ATTRIBUTE, pendingClass.members.size() + 1));
                } else if (classSubsection == Subsection.METHODS) {
                    pendingClass.members.add(parseMember(item, UmlMemberKind.METHOD, pendingClass.members.size() + 1));
                }
                continue;
            }
            if (section == Section.RELATIONS && line.startsWith("- ")) {
                relations.add(parseRelation(line.substring(2).strip(), classes, relationCounter++));
            }
        }
        if (pendingModule != null) {
            putModule(modules, pendingModule.toModule());
        }
        if (pendingClass != null) {
            putClass(classes, pendingClass.toClass(modules));
        }
        if (pendingView != null) {
            UmlClassMarkdownViewSectionParser.putView(views, pendingView);
        }
    }
    private static UmlClassMember parseMember(String text, UmlMemberKind defaultKind, int counter) {
        String trimmed = text.strip();
        UmlVisibility visibility = parseLeadingVisibility(trimmed);
        trimmed = removeLeadingVisibility(trimmed);
        UmlMemberKind kind = defaultKind;
        String name;
        String type = "";
        String signature = "";
        if (trimmed.contains("(")) {
            kind = UmlMemberKind.METHOD;
            int colon = trimmed.lastIndexOf(':');
            signature = colon >= 0 ? trimmed.substring(0, colon).strip() : trimmed;
            name = signature.substring(0, Math.max(0, signature.indexOf('('))).strip();
            type = colon >= 0 ? trimmed.substring(colon + 1).strip() : "";
        } else {
            int colon = trimmed.indexOf(':');
            name = colon >= 0 ? trimmed.substring(0, colon).strip() : trimmed;
            type = colon >= 0 ? trimmed.substring(colon + 1).strip() : "";
        }
        String stableName = MarkdownTextUtils.toStableId(name.isBlank() ? "miembro" + counter : name);
        return new UmlClassMember(stableName + "_" + counter, kind, name, type, signature, visibility, false, "");
    }

    private static UmlClassRelation parseRelation(
            String text,
            Map<String, UmlClassNode> classes,
            int counter
    ) throws MarkdownModelParsingException {
        RelationToken token = relationToken(text);
        String rawSource = token.source();
        String right = token.right();
        int colon = right.indexOf(':');
        String rawTarget = colon >= 0 ? right.substring(0, colon).strip() : right.strip();
        String label = colon >= 0 ? right.substring(colon + 1).strip() : "";
        String source = resolveClassId(rawSource, classes);
        String target = resolveClassId(rawTarget, classes);
        return new UmlClassRelation("rel_" + counter, source, target, token.kind(), label, label, "");
    }
    private static RelationToken relationToken(String text) throws MarkdownModelParsingException {
        String[] arrows = {"<|..", "<|--", "*--", "o--", "-->", "->", "..>", "--"};
        for (String arrow : arrows) {
            int index = text.indexOf(arrow);
            if (index >= 0) {
                String source = text.substring(0, index).strip();
                String right = text.substring(index + arrow.length()).strip();
                UmlRelationKind kind = switch (arrow) {
                    case "<|--" -> UmlRelationKind.INHERITANCE;
                    case "<|.." -> UmlRelationKind.IMPLEMENTATION;
                    case "*--" -> UmlRelationKind.COMPOSITION;
                    case "o--" -> UmlRelationKind.AGGREGATION;
                    case "-->", "->", "..>" -> UmlRelationKind.DEPENDENCY;
                    default -> UmlRelationKind.ASSOCIATION;
                };
                return new RelationToken(source, right, kind);
            }
        }
        throw new MarkdownModelParsingException("Relación UML sin conector reconocido: " + text);
    }
    private static String resolveClassId(String raw, Map<String, UmlClassNode> classes) throws MarkdownModelParsingException {
        String stable = MarkdownTextUtils.toStableId(raw);
        if (classes.containsKey(stable)) {
            return stable;
        }
        for (UmlClassNode node : classes.values()) {
            if (node.displayName().equalsIgnoreCase(raw.strip())) {
                return node.id();
            }
        }
        throw new MarkdownModelParsingException("La relación referencia una clase inexistente: " + raw);
    }
    private static void putModule(Map<String, UmlModuleGroup> modules, UmlModuleGroup module) throws MarkdownModelParsingException {
        if (modules.containsKey(module.id())) {
            throw new MarkdownModelParsingException("Módulo/paquete duplicado en UML Clases: " + module.id());
        }
        modules.put(module.id(), module);
    }

    private static void putClass(Map<String, UmlClassNode> classes, UmlClassNode node) throws MarkdownModelParsingException {
        if (classes.containsKey(node.id())) {
            throw new MarkdownModelParsingException("Clase duplicada en UML Clases: " + node.id());
        }
        classes.put(node.id(), node);
    }


    private static UmlClassKind parseClassKind(String value) {
        String normalized = MarkdownTextUtils.toStableId(value);
        for (UmlClassKind kind : UmlClassKind.values()) {
            if (MarkdownTextUtils.toStableId(kind.name()).equals(normalized)
                    || MarkdownTextUtils.toStableId(kind.displayName()).equals(normalized)) {
                return kind;
            }
        }
        return UmlClassKind.CLASS;
    }

    private static UmlVisibility parseVisibility(String value) {
        String normalized = value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
        for (UmlVisibility visibility : UmlVisibility.values()) {
            if (visibility.name().equalsIgnoreCase(normalized)
                    || visibility.displayName().equalsIgnoreCase(normalized)
                    || visibility.symbol().equals(normalized)) {
                return visibility;
            }
        }
        return UmlVisibility.PUBLIC;
    }

    private static UmlVisibility parseLeadingVisibility(String value) {
        String trimmed = value == null ? "" : value.strip();
        if (trimmed.startsWith("-")) return UmlVisibility.PRIVATE;
        if (trimmed.startsWith("#")) return UmlVisibility.PROTECTED;
        if (trimmed.startsWith("~")) return UmlVisibility.PACKAGE;
        if (trimmed.startsWith("+")) return UmlVisibility.PUBLIC;
        return UmlVisibility.PUBLIC;
    }

    private static String removeLeadingVisibility(String value) {
        String trimmed = value == null ? "" : value.strip();
        if (trimmed.startsWith("+") || trimmed.startsWith("-") || trimmed.startsWith("#") || trimmed.startsWith("~")) {
            return trimmed.substring(1).strip();
        }
        return trimmed;
    }


    private static String stableProjectId(String title) {
        return "uml_clases_" + MarkdownTextUtils.toStableId(title);
    }

    private static String normalizeKey(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
    }


    private enum Section { NONE, MODULES, CLASSES, VIEWS, RELATIONS }
    private enum Subsection { NONE, ATTRIBUTES, METHODS }
    private record RelationToken(String source, String right, UmlRelationKind kind) { }

    private static final class PendingModule {
        private final String title;
        private String id;
        private String path;
        private String description;
        private String notes;

        private PendingModule(String title) {
            this.title = title;
            this.id = MarkdownTextUtils.toStableId(title);
            this.path = title;
            this.description = "";
            this.notes = "";
        }

        private UmlModuleGroup toModule() {
            return new UmlModuleGroup(id, title, path, description, notes);
        }
    }

    private static final class PendingClass {
        private final String title;
        private String id;
        private String moduleId;
        private UmlClassKind kind = UmlClassKind.CLASS;
        private UmlVisibility visibility = UmlVisibility.PUBLIC;
        private String responsibility = "";
        private String description = "";
        private String notes = "";
        private final List<UmlClassMember> members = new ArrayList<>();

        private PendingClass(String title) {
            this.title = title;
            this.id = MarkdownTextUtils.toStableId(title);
        }

        private UmlClassNode toClass(Map<String, UmlModuleGroup> modules) {
            String resolvedModuleId = resolveModuleId(moduleId, modules);
            return new UmlClassNode(
                    id,
                    resolvedModuleId,
                    title,
                    modules.containsKey(resolvedModuleId) ? modules.get(resolvedModuleId).path() : moduleId,
                    kind,
                    visibility,
                    responsibility,
                    description,
                    members,
                    notes);
        }

        private String resolveModuleId(String raw, Map<String, UmlModuleGroup> modules) {
            if (raw == null || raw.isBlank()) {
                return "";
            }
            String stable = MarkdownTextUtils.toStableId(raw);
            if (modules.containsKey(stable)) {
                return stable;
            }
            for (UmlModuleGroup module : modules.values()) {
                if (module.displayName().equalsIgnoreCase(raw.strip()) || module.path().equalsIgnoreCase(raw.strip())) {
                    return module.id();
                }
            }
            return stable;
        }
    }
}
