package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Extrae y explica la ruta de archivo fuente guardada en clases UML importadas desde código. */
final class UmlClassSourceFileResolver {
    private static final Pattern ABSOLUTE_PATH = Pattern.compile("ruta absoluta:\\s*([^;\\r\\n]+)");
    private static final Pattern RELATIVE_PATH = Pattern.compile("ruta:\\s*([^;\\r\\n]+)");
    private static final Pattern ROOT_PATH = Pattern.compile("Source root path:\\s*([^|\\r\\n]+)");

    Optional<Path> resolve(UmlClassNode node) {
        return inspect(node).path();
    }

    UmlClassSourceFileResolution inspect(UmlClassNode node) {
        if (node == null) {
            return UmlClassSourceFileResolution.noSourceMetadata();
        }
        List<Path> candidates = new ArrayList<>();
        try {
            Optional<Path> absolute = absolutePathCandidate(node);
            if (absolute.isPresent()) {
                candidates.add(absolute.get());
                if (Files.isRegularFile(absolute.get())) {
                    return UmlClassSourceFileResolution.resolved(absolute.get());
                }
            }
            Optional<Path> rootRelative = rootRelativePathCandidate(node);
            if (rootRelative.isPresent()) {
                candidates.add(rootRelative.get());
                if (Files.isRegularFile(rootRelative.get())) {
                    return UmlClassSourceFileResolution.resolved(rootRelative.get());
                }
            }
        } catch (InvalidPathException exception) {
            return UmlClassSourceFileResolution.invalidMetadata(exception.getInput());
        }
        if (!candidates.isEmpty()) {
            UmlClassSourceFileResolution resolution = UmlClassSourceFileResolution.missingCandidates(candidates);
            if (resolution.status() == UmlClassSourceFileResolution.Status.CANDIDATE_NOT_FOUND) {
                return resolution;
            }
            return resolution;
        }
        boolean hasAnySourceHint = firstMatch(ABSOLUTE_PATH, node.description()).isPresent()
                || firstMatch(RELATIVE_PATH, node.description()).isPresent()
                || firstMatch(ROOT_PATH, node.notes()).isPresent();
        if (hasAnySourceHint) {
            return UmlClassSourceFileResolution.incompleteRootRelativeMetadata();
        }
        return UmlClassSourceFileResolution.noSourceMetadata();
    }

    private Optional<Path> absolutePathCandidate(UmlClassNode node) {
        return firstMatch(ABSOLUTE_PATH, node.description())
                .map(this::expandPrivatePath)
                .map(Path::of)
                .filter(Path::isAbsolute)
                .map(Path::normalize);
    }

    private Optional<Path> rootRelativePathCandidate(UmlClassNode node) {
        Optional<String> root = firstMatch(ROOT_PATH, node.notes());
        Optional<String> relative = firstMatch(RELATIVE_PATH, node.description());
        if (root.isEmpty() || relative.isEmpty()) {
            return Optional.empty();
        }
        Path rootPath = Path.of(expandPrivatePath(root.get().strip()));
        return rootPath.isAbsolute()
                ? Optional.of(rootPath.resolve(relative.get().strip()).normalize())
                : Optional.empty();
    }

    private String expandPrivatePath(String value) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.startsWith("${USER_HOME}")) {
            return System.getProperty("user.home", "") + normalized.substring("${USER_HOME}".length());
        }
        if (normalized.startsWith("%USERPROFILE%")) {
            return System.getProperty("user.home", "") + normalized.substring("%USERPROFILE%".length());
        }
        if (normalized.startsWith("~")) {
            return System.getProperty("user.home", "") + normalized.substring(1);
        }
        return normalized;
    }

    private Optional<String> firstMatch(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text == null ? "" : text);
        return matcher.find() ? Optional.of(matcher.group(1).strip()) : Optional.empty();
    }
}
