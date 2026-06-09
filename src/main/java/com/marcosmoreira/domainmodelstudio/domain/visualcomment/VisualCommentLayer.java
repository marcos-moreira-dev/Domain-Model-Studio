package com.marcosmoreira.domainmodelstudio.domain.visualcomment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Coleccion persistente de notas visuales del proyecto. */
public final class VisualCommentLayer {

    private static final VisualCommentLayer EMPTY = new VisualCommentLayer(List.of());

    private final List<VisualComment> comments;

    public VisualCommentLayer(List<VisualComment> comments) {
        List<VisualComment> copy = new ArrayList<>();
        for (VisualComment comment : comments == null ? List.<VisualComment>of() : comments) {
            if (comment != null && commentById(copy, comment.id()).isEmpty()) {
                copy.add(comment);
            }
        }
        this.comments = List.copyOf(copy);
    }

    public static VisualCommentLayer empty() {
        return EMPTY;
    }

    public List<VisualComment> comments() {
        return comments;
    }

    public boolean isEmpty() {
        return comments.isEmpty();
    }

    public Optional<VisualComment> commentById(String id) {
        return commentById(comments, id);
    }

    public VisualCommentLayer withComment(VisualComment updatedComment) {
        Objects.requireNonNull(updatedComment, "updatedComment");
        List<VisualComment> updated = new ArrayList<>();
        boolean replaced = false;
        for (VisualComment comment : comments) {
            if (comment.id().equals(updatedComment.id())) {
                updated.add(updatedComment);
                replaced = true;
            } else {
                updated.add(comment);
            }
        }
        if (!replaced) {
            updated.add(updatedComment);
        }
        return new VisualCommentLayer(updated);
    }

    public VisualCommentLayer withoutComment(String id) {
        String normalized = normalize(id);
        if (normalized.isBlank()) {
            return this;
        }
        return new VisualCommentLayer(comments.stream()
                .filter(comment -> !comment.id().equals(normalized))
                .toList());
    }

    public String nextCommentId() {
        int index = comments.size() + 1;
        String candidate;
        do {
            candidate = "comment-" + String.format("%03d", index++);
        } while (commentById(candidate).isPresent());
        return candidate;
    }

    private static Optional<VisualComment> commentById(List<VisualComment> source, String id) {
        String normalized = normalize(id);
        if (normalized.isBlank()) {
            return Optional.empty();
        }
        return (source == null ? List.<VisualComment>of() : source).stream()
                .filter(comment -> comment.id().equals(normalized))
                .findFirst();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
