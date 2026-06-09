package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceCatalog;
import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceExportResult;
import com.marcosmoreira.domainmodelstudio.application.resources.ExportAiResourcesUseCase;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Exporta recursos IA registrados desde el classpath hacia una carpeta elegida por el usuario.
 *
 * <p>Genera un índice canónico y copia gramáticas, plantillas, ejemplos y prompts sin
 * alterar su contenido. Este exportador no importa proyectos: entrega materiales para
 * que una IA produzca Markdown compatible y para que el usuario revise el contrato.</p>
 */
public final class ClasspathAiResourceExporter implements ExportAiResourcesUseCase {

    public static final String INDEX_FILE_NAME = "00_indice_recursos_ia.md";

    private final AiResourceCatalog catalog;
    private final ClassLoader classLoader;

    public ClasspathAiResourceExporter(AiResourceCatalog catalog) {
        this(catalog, ClasspathAiResourceExporter.class.getClassLoader());
    }

    public ClasspathAiResourceExporter(AiResourceCatalog catalog, ClassLoader classLoader) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
        this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
    }

    @Override
    public AiResourceExportResult exportTo(Path destinationFolder) throws IOException {
        Objects.requireNonNull(destinationFolder, "destinationFolder");
        Files.createDirectories(destinationFolder);

        List<AiResourceDescriptor> resources = catalog.findAllExportable();
        List<String> exportedFiles = new ArrayList<>();

        Path indexFile = destinationFolder.resolve(INDEX_FILE_NAME);
        Files.writeString(indexFile, buildIndex(resources), StandardCharsets.UTF_8);
        exportedFiles.add(INDEX_FILE_NAME);

        for (AiResourceDescriptor resource : resources) {
            copyResource(resource, destinationFolder.resolve(resource.fileName()));
            exportedFiles.add(resource.fileName());
        }

        return new AiResourceExportResult(destinationFolder, exportedFiles);
    }

    private void copyResource(AiResourceDescriptor resource, Path destinationFile) throws IOException {
        Path parent = destinationFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (InputStream stream = classLoader.getResourceAsStream(resource.classpathLocation())) {
            if (stream == null) {
                throw new IOException("Recurso interno no encontrado: " + resource.classpathLocation());
            }
            Files.copy(stream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static String buildIndex(List<AiResourceDescriptor> resources) {
        StringBuilder builder = new StringBuilder();
        builder.append("# Recursos IA de Domain Model Studio\n\n");
        builder.append("Paquete productizado de gramáticas, plantillas y ejemplos Markdown para trabajar con IA. ");
        builder.append("Cada entrada declara su tipo de recurso, uso recomendado y contrato de importación.\n\n");
        builder.append("## Contrato del paquete\n\n");
        builder.append("- Una plantilla IA puede requerir completar campos antes de importarse.\n");
        builder.append("- Un ejemplo oficial importable debe abrirse con el `diagram_type` indicado.\n");
        builder.append("- El levantamiento lógico es fuente lógica canónica; cualquier uso posterior produce borradores revisables bajo revisión humana.\n");
        builder.append("- No asumir importabilidad si este índice dice `no`.\n\n");
        for (Map.Entry<AiResourceKind, List<AiResourceDescriptor>> entry : groupByKind(resources).entrySet()) {
            builder.append("## ").append(entry.getKey().displayName()).append("\n\n");
            for (AiResourceDescriptor resource : entry.getValue()) {
                appendResource(builder, resource);
            }
        }
        return builder.toString();
    }

    private static Map<AiResourceKind, List<AiResourceDescriptor>> groupByKind(List<AiResourceDescriptor> resources) {
        Map<AiResourceKind, List<AiResourceDescriptor>> grouped = new LinkedHashMap<>();
        for (AiResourceKind kind : AiResourceKind.values()) {
            grouped.put(kind, new ArrayList<>());
        }
        for (AiResourceDescriptor resource : resources) {
            grouped.get(AiResourceProductizationPolicy.kindOf(resource)).add(resource);
        }
        grouped.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        return grouped;
    }

    private static void appendResource(StringBuilder builder, AiResourceDescriptor resource) {
        builder.append("- `").append(resource.fileName()).append("` — ").append(resource.description()).append("\n");
        builder.append("  - Recurso: `").append(resource.id()).append("`\n");
        builder.append("  - Tipo de diagrama: `").append(resource.diagramTypeId().value()).append("`\n");
        builder.append("  - Tipo de recurso: ").append(AiResourceProductizationPolicy.kindOf(resource).displayName()).append("\n");
        builder.append("  - Uso recomendado: ").append(AiResourceProductizationPolicy.recommendedUse(resource)).append("\n");
        builder.append("  - Importable por la aplicación: ").append(resource.importableByApplication() ? "sí" : "no").append("\n");
        builder.append("  - Contrato de importación: ").append(AiResourceProductizationPolicy.importContract(resource)).append("\n");
    }
}