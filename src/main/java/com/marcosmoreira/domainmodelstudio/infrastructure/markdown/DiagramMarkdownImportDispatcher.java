package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness.LogicalBusinessProjectMarkdownParser;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Entrada única para importar Markdown oficial hacia proyectos de dominio.
 *
 * <p>Primero identifica el {@code diagram_type}, consulta el catálogo de producto y solo
 * después delega al parser especializado. Este orden es deliberado: el contrato de
 * importación no depende de heurísticas visuales, sino de una declaración explícita y
 * de capacidades reales del tipo.</p>
 *
 * <p>Un error de formato aquí es recuperable para el usuario: se informa qué tipo falta,
 * qué capacidad no existe o qué parser rechazó la estructura. El dispatcher no corrige
 * semántica del dominio; solo enruta el Markdown al adaptador correcto.</p>
 */
public final class DiagramMarkdownImportDispatcher implements MarkdownModelParser {

    private final DiagramTypeRegistry diagramTypeRegistry;
    private final Map<DiagramTypeId, MarkdownModelParser> importersByType;

    public DiagramMarkdownImportDispatcher(DiagramTypeRegistry diagramTypeRegistry) {
        this(diagramTypeRegistry, Map.ofEntries(
                Map.entry(DiagramTypeId.CONCEPTUAL_MODEL, new MarkdownDiagramParser()),
                Map.entry(DiagramTypeId.DATA_DICTIONARY, new DataDictionaryMarkdownParser()),
                Map.entry(DiagramTypeId.ADMIN_MODULE_MAP, new ModuleMapMarkdownParser()),
                Map.entry(DiagramTypeId.ROLES_PERMISSIONS_MAP, new RolesPermissionsMarkdownParser()),
                Map.entry(DiagramTypeId.SCREEN_FLOW, new ScreenFlowMarkdownParser()),
                Map.entry(DiagramTypeId.ADMIN_WIREFRAMES, new WireframeMarkdownParser()),
                Map.entry(DiagramTypeId.UML_CLASS, new UmlClassMarkdownParser()),
                Map.entry(DiagramTypeId.C4_CONTEXT, new ArchitectureMarkdownParser()),
                Map.entry(DiagramTypeId.C4_CONTAINERS, new ArchitectureMarkdownParser()),
                Map.entry(DiagramTypeId.TECHNICAL_DEPLOYMENT, new ArchitectureMarkdownParser()),
                Map.entry(DiagramTypeId.BPMN_BASIC, new BehaviorMarkdownParser()),
                Map.entry(DiagramTypeId.OPERATIONAL_FLOW, new BehaviorMarkdownParser()),
                Map.entry(DiagramTypeId.UML_USE_CASE, new BehaviorMarkdownParser()),
                Map.entry(DiagramTypeId.UML_ACTIVITY, new BehaviorMarkdownParser()),
                Map.entry(DiagramTypeId.UML_SEQUENCE, new BehaviorMarkdownParser()),
                Map.entry(DiagramTypeId.UML_STATE, new BehaviorMarkdownParser()),
                Map.entry(DiagramTypeId.FREE_GRAPH, new FreeGraphMarkdownParser()),
                Map.entry(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, new LogicalBusinessProjectMarkdownParser()),
                Map.entry(DiagramTypeId.LOGICAL_BUSINESS_GRAPH, new LogicalBusinessGraphMarkdownParser())));
    }

    public DiagramMarkdownImportDispatcher(
            DiagramTypeRegistry diagramTypeRegistry,
            Map<DiagramTypeId, MarkdownModelParser> importersByType
    ) {
        this.diagramTypeRegistry = Objects.requireNonNull(diagramTypeRegistry, "diagramTypeRegistry");
        this.importersByType = Map.copyOf(Objects.requireNonNull(importersByType, "importersByType"));
    }

    @Override
    public DiagramProject parse(Path markdownFile) throws IOException, MarkdownModelParsingException {
        Objects.requireNonNull(markdownFile, "El archivo Markdown no puede ser null");
        String content = Files.readString(markdownFile, StandardCharsets.UTF_8);
        return parse(content, markdownFile.toString());
    }

    @Override
    public DiagramProject parse(String markdownContent, String sourceName) throws MarkdownModelParsingException {
        Objects.requireNonNull(markdownContent, "El contenido Markdown no puede ser null");
        MarkdownFrontMatter frontMatter = MarkdownImportDocument.parse(markdownContent).frontMatter();
        DiagramTypeId declaredType = resolveDeclaredType(frontMatter, markdownContent, sourceName);
        DiagramTypeDescriptor descriptor = diagramTypeRegistry.findById(declaredType)
                .orElseThrow(() -> new MarkdownModelParsingException(
                        "El Markdown declara un tipo de diagrama no registrado: " + declaredType.value()
                                + ". Revisa el valor de diagram_type."));

        if (!descriptor.supports(DiagramCapability.IMPORT_MARKDOWN)) {
            throw new MarkdownModelParsingException("El Markdown fue reconocido como \""
                    + descriptor.displayName()
                    + "\", pero ese tipo todavía está en preparación y no puede importarse como proyecto editable. "
                    + "Por ahora úsalo como plantilla o referencia para IA.");
        }

        MarkdownModelParser importer = importersByType.get(declaredType);
        if (importer == null) {
            throw new MarkdownModelParsingException("El Markdown fue reconocido como \""
                    + descriptor.displayName()
                    + "\", pero todavía no tiene importación activa en esta versión.");
        }
        return importer.parse(markdownContent, sourceName);
    }

    private DiagramTypeId resolveDeclaredType(
            MarkdownFrontMatter frontMatter,
            String markdownContent,
            String sourceName
    ) throws MarkdownModelParsingException {
        Optional<String> diagramType = frontMatter.value("diagram_type");
        if (diagramType.isPresent() && !diagramType.get().isBlank()) {
            return DiagramTypeId.of(normalizeDiagramType(diagramType.get()));
        }

        Optional<String> projectType = frontMatter.value("project_type");
        if (projectType.isPresent() && isConceptualAlias(projectType.get())) {
            return DiagramTypeId.CONCEPTUAL_MODEL;
        }

        if (looksLikeLegacyConceptualMarkdown(markdownContent)) {
            return DiagramTypeId.CONCEPTUAL_MODEL;
        }

        String source = sourceName == null || sourceName.isBlank() ? "el archivo" : sourceName;
        throw new MarkdownModelParsingException("No se pudo reconocer el tipo de diagrama de "
                + source
                + ". Agrega en el encabezado: diagram_type: \"conceptual-model\" u otro ID oficial del catálogo.");
    }


    private String normalizeDiagramType(String rawValue) {
        String normalized = unquote(rawValue)
                .strip()
                .toLowerCase(Locale.ROOT)
                .replace('_', '-')
                .replace(' ', '-');
        if (normalized.equals("conceptualmodel")) {
            return DiagramTypeId.CONCEPTUAL_MODEL.value();
        }
        return normalized;
    }

    private boolean isConceptualAlias(String rawValue) {
        String normalized = unquote(rawValue)
                .strip()
                .toLowerCase(Locale.ROOT)
                .replace('_', '-')
                .replace(' ', '-');
        return normalized.equals("conceptual-model")
                || normalized.equals("conceptual")
                || normalized.equals("conceptualmodel")
                || normalized.equals("modelo-conceptual");
    }

    private boolean looksLikeLegacyConceptualMarkdown(String markdownContent) {
        boolean hasEntitiesSection = false;
        for (String rawLine : markdownContent.lines().toList()) {
            String line = rawLine.strip().toLowerCase(Locale.ROOT);
            if (line.equals("# entidades")) {
                hasEntitiesSection = true;
            }
            if (line.equals("# roles")
                    || line.equals("# pantallas")
                    || line.equals("# módulos")
                    || line.equals("# modulos")
                    || line.equals("# wireframes administrativos")
                    || line.equals("# clases")
                    || line.equals("# uml clases")
                    || line.equals("# c4 contexto")
                    || line.equals("# c4 contenedores")
                    || line.equals("# despliegue técnico")
                    || line.equals("# despliegue tecnico")
                    || line.equals("# nodos")
                    || line.equals("# conexiones")
                    || line.equals("# flujo")
                    || line.equals("# proceso")
                    || line.equals("# participantes")
                    || line.equals("# mensajes")
                    || line.equals("# actores")
                    || line.equals("# casos de uso")
                    || line.equals("# estados")
                    || line.equals("# transiciones")) {
                return false;
            }
        }
        return hasEntitiesSection;
    }

    private String unquote(String value) {
        String cleaned = value == null ? "" : value.strip();
        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            return cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned;
    }
}
