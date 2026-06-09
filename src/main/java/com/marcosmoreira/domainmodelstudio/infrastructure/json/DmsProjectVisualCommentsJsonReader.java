package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualComment;
import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualCommentLayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Lee la seccion opcional de notas visuales libres. */
final class DmsProjectVisualCommentsJsonReader {

    VisualCommentLayer read(Object value) {
        if (!(value instanceof Map<?, ?> rawMap)) {
            return VisualCommentLayer.empty();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> object = (Map<String, Object>) rawMap;
        Object commentsValue = object.get("comments");
        if (!(commentsValue instanceof List<?> list)) {
            return VisualCommentLayer.empty();
        }
        List<VisualComment> comments = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Map<?, ?> commentMap) {
                try {
                    VisualComment comment = readComment(commentMap);
                    if (!comment.id().isBlank()) {
                        comments.add(comment);
                    }
                } catch (IllegalArgumentException ignored) {
                    // La capa de comentarios es auxiliar; entradas corruptas no invalidan el proyecto.
                }
            }
        }
        return new VisualCommentLayer(comments);
    }

    private VisualComment readComment(Map<?, ?> commentMap) {
        String id = stringOrDefault(commentMap, "id", "");
        String title = stringOrDefault(commentMap, "title", "");
        String description = stringOrDefault(commentMap, "description", "");
        return new VisualComment(id, title, description);
    }

    private String stringOrDefault(Map<?, ?> object, String key, String defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }
}
