package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualComment;
import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualCommentLayer;

/** Escribe la capa opcional de notas visuales libres del proyecto. */
final class DmsProjectVisualCommentsJsonWriter {

    void write(VisualCommentLayer visualComments, StringBuilder json, int level) {
        VisualCommentLayer layer = visualComments == null ? VisualCommentLayer.empty() : visualComments;
        indent(json, level).append("\"visualComments\": {\n");
        indent(json, level + 1).append("\"comments\": [\n");
        for (int i = 0; i < layer.comments().size(); i++) {
            VisualComment comment = layer.comments().get(i);
            indent(json, level + 2).append("{\n");
            field(json, level + 3, "id", comment.id(), true);
            field(json, level + 3, "title", comment.title(), true);
            field(json, level + 3, "description", comment.description(), false);
            indent(json, level + 2).append("}").append(i + 1 < layer.comments().size() ? "," : "").append("\n");
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}");
    }

    private void field(StringBuilder json, int level, String name, String value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(quote(value)).append(comma ? "," : "").append("\n");
    }

    private StringBuilder indent(StringBuilder json, int level) {
        return json.append("  ".repeat(Math.max(0, level)));
    }

    private String quote(String value) {
        return JsonStringEscaper.quote(value);
    }
}
