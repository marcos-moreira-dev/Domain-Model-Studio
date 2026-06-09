package com.marcosmoreira.domainmodelstudio.application.visualcomment;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramSize;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualComment;
import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualCommentLayer;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/** Operaciones transversales sobre notas visuales y sus layouts. */
public final class VisualCommentProjectService {

    public AddResult addComment(DiagramProject project, double canvasX, double canvasY) {
        Objects.requireNonNull(project, "project");
        String commentId = project.visualComments().nextCommentId();
        VisualComment comment = VisualComment.blank(commentId);
        DiagramSize size = VisualCommentPolicy.preferredSize(comment.visibleTitle());
        NodeLayout layout = new NodeLayout(
                VisualElementLayoutIds.visualComment(commentId),
                DiagramPoint.of(Math.max(0.0, canvasX - size.width() / 2.0), Math.max(0.0, canvasY - 24.0)),
                size,
                true,
                false);
        DiagramProject updated = project
                .withVisualComments(project.visualComments().withComment(comment))
                .withLayouts(project.layouts().withLayout(project.layouts().activeLayout().withNode(layout)));
        return new AddResult(updated, layout.elementId().value());
    }

    public DiagramProject updateTitle(DiagramProject project, String commentLayoutId, String title) {
        VisualComment comment = requireComment(project, commentLayoutId);
        String normalizedTitle = VisualCommentPolicy.normalizeTitle(title);
        DiagramProject updated = project.withVisualComments(
                project.visualComments().withComment(comment.withTitle(normalizedTitle)));
        return resizeToTitle(updated, commentLayoutId, normalizedTitle);
    }

    public DiagramProject updateDescription(DiagramProject project, String commentLayoutId, String description) {
        VisualComment comment = requireComment(project, commentLayoutId);
        return project.withVisualComments(project.visualComments().withComment(
                comment.withDescription(VisualCommentPolicy.normalizeDescription(description))));
    }

    public DiagramProject removeComment(DiagramProject project, String commentLayoutId) {
        Objects.requireNonNull(project, "project");
        String commentId = rawCommentId(commentLayoutId);
        if (commentId.isBlank()) {
            return project;
        }
        DiagramProject withoutComment = project.withVisualComments(project.visualComments().withoutComment(commentId));
        DiagramLayout layout = withoutComment.layouts().activeLayout()
                .withoutNode(VisualElementLayoutIds.visualComment(commentId));
        return withoutComment.withLayouts(withoutComment.layouts().withLayout(layout));
    }

    public DiagramProject moveCommentTo(DiagramProject project, String commentLayoutId, double x, double y) {
        Objects.requireNonNull(project, "project");
        DiagramElementId layoutId = requireCommentLayoutId(commentLayoutId);
        DiagramLayout layout = project.layouts().activeLayout().moveNode(layoutId, x, y);
        return project.withLayouts(project.layouts().withLayout(layout));
    }

    public DiagramProject moveCommentsBy(DiagramProject project, Collection<String> commentLayoutIds, double deltaX, double deltaY) {
        Objects.requireNonNull(project, "project");
        DiagramProject updated = project;
        for (String commentLayoutId : commentLayoutIds == null ? Set.<String>of() : commentLayoutIds) {
            DiagramElementId layoutId = requireCommentLayoutId(commentLayoutId);
            NodeLayout current = updated.layouts().activeLayout().nodeFor(layoutId)
                    .orElseThrow(() -> new IllegalArgumentException("No existe layout para la nota visual: " + commentLayoutId));
            updated = moveCommentTo(updated, commentLayoutId, current.x() + deltaX, current.y() + deltaY);
        }
        return updated;
    }

    public DiagramProject resizeCommentTo(DiagramProject project, String commentLayoutId, double width, double height) {
        Objects.requireNonNull(project, "project");
        DiagramElementId layoutId = requireCommentLayoutId(commentLayoutId);
        NodeLayout current = project.layouts().activeLayout().nodeFor(layoutId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la nota visual: " + commentLayoutId));
        DiagramLayout layout = project.layouts().activeLayout().withNode(current.resizedTo(
                Math.max(VisualCommentPolicy.MIN_WIDTH, Math.min(VisualCommentPolicy.MAX_WIDTH, width)),
                VisualCommentPolicy.clampHeight(height)));
        return project.withLayouts(project.layouts().withLayout(layout));
    }

    public DiagramProject reorderComments(DiagramProject project, Collection<String> commentLayoutIds, VisualLayerOrderCommand command) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(command, "command");
        Set<DiagramElementId> selected = (commentLayoutIds == null ? Set.<String>of() : commentLayoutIds).stream()
                .map(this::requireCommentLayoutId)
                .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
        if (selected.isEmpty()) {
            return project;
        }
        DiagramLayout activeLayout = project.layouts().activeLayout();
        DiagramLayout updated = switch (command) {
            case BRING_TO_FRONT -> activeLayout.bringNodesToFront(selected);
            case SEND_TO_BACK -> activeLayout.sendNodesToBack(selected);
            case RAISE -> activeLayout.raiseNodes(selected);
            case LOWER -> activeLayout.lowerNodes(selected);
        };
        return project.withLayouts(project.layouts().withLayout(updated));
    }

    private DiagramProject resizeToTitle(DiagramProject project, String commentLayoutId, String title) {
        DiagramElementId layoutId = requireCommentLayoutId(commentLayoutId);
        NodeLayout current = project.layouts().activeLayout().nodeFor(layoutId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la nota visual: " + commentLayoutId));
        double width = VisualCommentPolicy.preferredWidth(title);
        DiagramLayout layout = project.layouts().activeLayout().withNode(current.resizedTo(width, current.height()));
        return project.withLayouts(project.layouts().withLayout(layout));
    }

    private VisualComment requireComment(DiagramProject project, String commentLayoutId) {
        Objects.requireNonNull(project, "project");
        String commentId = rawCommentId(commentLayoutId);
        if (commentId.isBlank()) {
            throw new IllegalArgumentException("El identificador no pertenece a una nota visual: " + commentLayoutId);
        }
        VisualCommentLayer comments = project.visualComments();
        return comments.commentById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("No existe nota visual: " + commentId));
    }

    private DiagramElementId requireCommentLayoutId(String commentLayoutId) {
        String commentId = rawCommentId(commentLayoutId);
        if (commentId.isBlank()) {
            throw new IllegalArgumentException("El identificador no pertenece a una nota visual: " + commentLayoutId);
        }
        return VisualElementLayoutIds.visualComment(commentId);
    }

    private String rawCommentId(String commentLayoutId) {
        return VisualElementLayoutIds.rawVisualCommentId(commentLayoutId);
    }

    public record AddResult(DiagramProject project, String layoutId) {
    }
}
