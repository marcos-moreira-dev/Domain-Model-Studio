package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleStatus;
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

/** Importa Markdown oficial de mapa de módulos hacia un proyecto editable. */
public final class ModuleMapMarkdownParser implements MarkdownModelParser {

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
        String title = frontMatter.valueOrDefault("name", "Mapa de módulos importado");
        Map<String, ModuleNode> modules = new LinkedHashMap<>();
        List<ModuleDependency> dependencies = new ArrayList<>();
        parseBody(importDocument.body(), modules, dependencies);
        if (modules.isEmpty()) {
            throw new MarkdownModelParsingException("El mapa de módulos no contiene módulos reconocibles.");
        }
        ModuleMapDocument document;
        try {
            document = new ModuleMapDocument(
                    title,
                    frontMatter.valueOrDefault("version", "borrador"),
                    LocalDate.now(),
                    new ArrayList<>(modules.values()),
                    dependencies,
                    "Importado desde Markdown oficial de mapa de módulos.");
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir el mapa de módulos: " + exception.getMessage(), exception);
        }
        return MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title), title, DiagramTypeId.ADMIN_MODULE_MAP).withModuleMap(document),
                sourceName);
    }

    private static void parseBody(
            String body,
            Map<String, ModuleNode> modules,
            List<ModuleDependency> dependencies
    ) throws MarkdownModelParsingException {
        String currentRootId = "";
        String pendingRootTitle = "";
        String pendingRootId = "";
        String pendingResponsibility = "";
        String pendingDescription = "";
        boolean inDependencies = false;
        boolean inSubmodules = false;
        int dependencyCounter = 1;
        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank() || line.startsWith(">")) {
                continue;
            }
            String lower = line.toLowerCase(Locale.ROOT);
            if (line.startsWith("# ")) {
                inDependencies = lower.contains("dependencia");
                inSubmodules = false;
                if (inDependencies) {
                    if (!pendingRootTitle.isBlank()) {
                        currentRootId = flushRoot(modules, pendingRootTitle, pendingRootId, pendingResponsibility, pendingDescription);
                        pendingRootTitle = pendingRootId = pendingResponsibility = pendingDescription = "";
                    }
                }
                continue;
            }
            if (line.startsWith("## ")) {
                if (!pendingRootTitle.isBlank()) {
                    currentRootId = flushRoot(modules, pendingRootTitle, pendingRootId, pendingResponsibility, pendingDescription);
                }
                pendingRootTitle = line.substring(3).strip();
                pendingRootId = "";
                pendingResponsibility = "";
                pendingDescription = "";
                inDependencies = false;
                inSubmodules = false;
                continue;
            }
            if (line.startsWith("### ")) {
                if (!pendingRootTitle.isBlank()) {
                    currentRootId = flushRoot(modules, pendingRootTitle, pendingRootId, pendingResponsibility, pendingDescription);
                    pendingRootTitle = pendingRootId = pendingResponsibility = pendingDescription = "";
                }
                inSubmodules = lower.contains("submodulo") || lower.contains("submódulo");
                continue;
            }
            if (!pendingRootTitle.isBlank() && MarkdownTextUtils.isPropertyLine(line)) {
                String key = MarkdownTextUtils.keyBeforeColon(line);
                String value = MarkdownTextUtils.valueAfterColon(line);
                if (key.equals("id")) {
                    pendingRootId = value;
                } else if (key.equals("responsabilidad")) {
                    pendingResponsibility = value;
                } else if (key.equals("descripcion") || key.equals("descripción")) {
                    pendingDescription = value;
                }
                continue;
            }
            if (line.startsWith("- ")) {
                if (inDependencies) {
                    dependencies.add(parseDependency(line.substring(2).strip(), modules, dependencyCounter++));
                } else if (inSubmodules && !currentRootId.isBlank()) {
                    ModuleNode child = parseSubmodule(line.substring(2).strip(), currentRootId);
                    putModule(modules, child);
                }
            }
        }
        if (!pendingRootTitle.isBlank()) {
            flushRoot(modules, pendingRootTitle, pendingRootId, pendingResponsibility, pendingDescription);
        }
    }

    private static String flushRoot(
            Map<String, ModuleNode> modules,
            String title,
            String rawId,
            String responsibility,
            String description
    ) throws MarkdownModelParsingException {
        String id = rawId == null || rawId.isBlank() ? MarkdownTextUtils.toStableId(title) : MarkdownTextUtils.toStableId(rawId);
        ModuleNode module = new ModuleNode(
                id,
                title,
                "",
                ModuleKind.MAIN,
                ModuleStatus.PLANNED,
                responsibility,
                description,
                List.of(),
                "");
        putModule(modules, module);
        return id;
    }

    private static ModuleNode parseSubmodule(String text, String parentId) {
        int colon = text.indexOf(':');
        String rawId = colon >= 0 ? text.substring(0, colon).strip() : text;
        String description = colon >= 0 ? text.substring(colon + 1).strip() : "";
        String id = MarkdownTextUtils.toStableId(parentId + "_" + rawId);
        String displayName = displayName(rawId);
        return new ModuleNode(
                id,
                displayName,
                parentId,
                ModuleKind.SUPPORT,
                ModuleStatus.PLANNED,
                description,
                description,
                List.of(),
                "");
    }

    private static ModuleDependency parseDependency(
            String text,
            Map<String, ModuleNode> modules,
            int counter
    ) throws MarkdownModelParsingException {
        int arrow = text.indexOf("->");
        if (arrow < 0) {
            throw new MarkdownModelParsingException("Dependencia sin flecha '->': " + text);
        }
        String rawSource = text.substring(0, arrow).strip();
        String right = text.substring(arrow + 2).strip();
        int colon = right.indexOf(':');
        String rawTarget = colon >= 0 ? right.substring(0, colon).strip() : right;
        String description = colon >= 0 ? right.substring(colon + 1).strip() : "";
        String source = resolveModuleId(rawSource, modules);
        String target = resolveModuleId(rawTarget, modules);
        return new ModuleDependency("dep_" + counter, source, target, DependencyKind.USES, description, "");
    }

    private static String resolveModuleId(String raw, Map<String, ModuleNode> modules) throws MarkdownModelParsingException {
        String stable = MarkdownTextUtils.toStableId(raw);
        if (modules.containsKey(stable)) {
            return stable;
        }
        for (ModuleNode module : modules.values()) {
            if (module.displayName().equalsIgnoreCase(raw.strip())) {
                return module.id();
            }
        }
        throw new MarkdownModelParsingException("La dependencia referencia un módulo inexistente: " + raw);
    }

    private static void putModule(Map<String, ModuleNode> modules, ModuleNode module) throws MarkdownModelParsingException {
        if (modules.containsKey(module.id())) {
            throw new MarkdownModelParsingException("Módulo duplicado en Markdown: " + module.id());
        }
        modules.put(module.id(), module);
    }

    private static String displayName(String value) {
        String text = value == null ? "" : value.strip().replace('_', ' ');
        if (text.isBlank()) {
            return "Submódulo";
        }
        return text.substring(0, 1).toUpperCase(Locale.ROOT) + text.substring(1);
    }


    private static String stableProjectId(String title) {
        return "mapa_modulos_" + MarkdownTextUtils.toStableId(title);
    }
}
