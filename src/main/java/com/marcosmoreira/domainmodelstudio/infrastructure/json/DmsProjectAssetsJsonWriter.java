package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetCatalog;
import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetReference;

/** Escribe el bloque assets del formato .dms v3. */
final class DmsProjectAssetsJsonWriter {

    void write(ProjectAssetCatalog catalog, StringBuilder json, int level) {
        ProjectAssetCatalog safeCatalog = catalog == null ? ProjectAssetCatalog.empty() : catalog;
        indent(json, level).append("\"assets\": {\n");
        numberField(json, level + 1, "schemaVersion", 1, true);
        indent(json, level + 1).append("\"items\": [\n");
        for (int i = 0; i < safeCatalog.references().size(); i++) {
            writeReference(safeCatalog.references().get(i), json, level + 2, i + 1 < safeCatalog.references().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}");
    }

    private void writeReference(ProjectAssetReference reference, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", reference.id(), true);
        field(json, level + 1, "kind", reference.kind().name(), true);
        field(json, level + 1, "displayName", reference.displayName(), true);
        field(json, level + 1, "relativePath", reference.relativePath(), true);
        field(json, level + 1, "mimeType", reference.mimeType(), true);
        field(json, level + 1, "purpose", reference.purpose(), true);
        field(json, level + 1, "checksum", reference.checksum(), true);
        field(json, level + 1, "notes", reference.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void field(StringBuilder json, int level, String name, String value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(quote(value)).append(comma ? "," : "").append("\n");
    }

    private void numberField(StringBuilder json, int level, String name, int value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(value).append(comma ? "," : "").append("\n");
    }

    private static StringBuilder indent(StringBuilder json, int level) {
        return json.append("  ".repeat(Math.max(0, level)));
    }

    private static String quote(String value) {
        String raw = value == null ? "" : value;
        return "\"" + raw.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
}
