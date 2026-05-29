package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.Locale;
import java.util.Map;

/** Clasifica tipos parseados sin acoplar el dominio UML a Java, TypeScript o Angular. */
public final class ParsedCodeTypeRoleClassifier {

    public ParsedCodeTypeRole classify(ParsedCodeType type, ParsedCodeSourceRoot root) {
        if (type == null) {
            return ParsedCodeTypeRole.UNKNOWN;
        }
        String name = normalized(type.simpleName());
        String qualified = normalized(type.qualifiedName());
        String packageName = normalized(type.packageName());
        String sourcePath = normalized(metadata(type.metadata(), ParsedCodeMetadataKeys.SOURCE_PATH));
        if (sourcePath.isBlank() && type.sourcePath() != null) {
            sourcePath = normalized(type.sourcePath().toString());
        }
        String frameworkHint = normalized(metadata(type.metadata(), ParsedCodeMetadataKeys.FRAMEWORK_HINT));
        SourceRootKind rootKind = root == null ? SourceRootKind.UNKNOWN : root.kind();

        if (containsAnyIn(name, qualified, packageName, sourcePath, "dto", "request", "response", "payload")) {
            return ParsedCodeTypeRole.DTO;
        }
        if (matchesBackendController(type, name, qualified, packageName, sourcePath, frameworkHint, rootKind)) {
            return ParsedCodeTypeRole.BACKEND_CONTROLLER;
        }
        if (matchesBackendRepository(type, name, qualified, packageName, sourcePath, rootKind)) {
            return ParsedCodeTypeRole.BACKEND_REPOSITORY;
        }
        if (matchesBackendEntity(type, name, qualified, packageName, sourcePath, rootKind)) {
            return ParsedCodeTypeRole.BACKEND_ENTITY;
        }
        if (matchesBackendService(type, name, qualified, packageName, sourcePath, rootKind)) {
            return ParsedCodeTypeRole.BACKEND_SERVICE;
        }
        if (matchesFrontendComponent(name, frameworkHint, sourcePath, rootKind)) {
            return ParsedCodeTypeRole.FRONTEND_COMPONENT;
        }
        if (matchesFrontendService(name, frameworkHint, sourcePath, rootKind)) {
            return ParsedCodeTypeRole.FRONTEND_SERVICE;
        }
        if (containsAnyIn(name, qualified, packageName, sourcePath, "guard")) {
            return ParsedCodeTypeRole.GUARD;
        }
        if (containsAnyIn(name, qualified, packageName, sourcePath, "interceptor")) {
            return ParsedCodeTypeRole.INTERCEPTOR;
        }
        if (name.endsWith("module") || frameworkHint.contains("ngmodule")) {
            return ParsedCodeTypeRole.MODULE;
        }
        if (matchesFrontendModel(type, packageName, sourcePath, rootKind)) {
            return ParsedCodeTypeRole.FRONTEND_MODEL;
        }
        if (containsAnyIn(name, qualified, packageName, sourcePath, "config", "configuration")) {
            return ParsedCodeTypeRole.CONFIGURATION;
        }
        if (type.kind() == ParsedCodeTypeKind.CLASS || type.kind() == ParsedCodeTypeKind.RECORD
                || type.kind() == ParsedCodeTypeKind.INTERFACE || type.kind() == ParsedCodeTypeKind.ENUM) {
            return ParsedCodeTypeRole.DOMAIN_TYPE;
        }
        return ParsedCodeTypeRole.UNKNOWN;
    }

    private boolean matchesBackendController(ParsedCodeType type, String name, String qualified, String packageName,
                                             String sourcePath, String frameworkHint, SourceRootKind rootKind) {
        return rootKind == SourceRootKind.BACKEND && (name.endsWith("controller")
                || hasAnnotation(type, "@RestController", "@Controller")
                || containsAnyIn(qualified, packageName, sourcePath, "", "controller", "controllers")
                || frameworkHint.contains("controller"));
    }

    private boolean matchesBackendRepository(ParsedCodeType type, String name, String qualified,
                                             String packageName, String sourcePath, SourceRootKind rootKind) {
        return rootKind == SourceRootKind.BACKEND && (name.endsWith("repository")
                || hasAnnotation(type, "@Repository")
                || containsAnyIn(qualified, packageName, sourcePath, "", "repository", "repositories"));
    }

    private boolean matchesBackendEntity(ParsedCodeType type, String name, String qualified,
                                         String packageName, String sourcePath, SourceRootKind rootKind) {
        return rootKind == SourceRootKind.BACKEND && (name.endsWith("entity")
                || hasAnnotation(type, "@Entity", "@Table", "@Embeddable")
                || containsAnyIn(qualified, packageName, sourcePath, "", "entity", "entities", "model", "domain"));
    }

    private boolean matchesBackendService(ParsedCodeType type, String name, String qualified,
                                          String packageName, String sourcePath, SourceRootKind rootKind) {
        return rootKind == SourceRootKind.BACKEND && (name.endsWith("service")
                || hasAnnotation(type, "@Service")
                || containsAnyIn(qualified, packageName, sourcePath, "", "service", "services"));
    }

    private boolean matchesFrontendComponent(String name, String frameworkHint, String sourcePath, SourceRootKind rootKind) {
        return rootKind == SourceRootKind.FRONTEND && (name.endsWith("component")
                || frameworkHint.contains("component")
                || sourcePath.contains("component"));
    }

    private boolean matchesFrontendService(String name, String frameworkHint, String sourcePath, SourceRootKind rootKind) {
        return rootKind == SourceRootKind.FRONTEND && (name.endsWith("service")
                || frameworkHint.contains("injectable")
                || sourcePath.endsWith("service.ts")
                || sourcePath.contains("services"));
    }

    private boolean matchesFrontendModel(ParsedCodeType type, String packageName, String sourcePath, SourceRootKind rootKind) {
        return rootKind == SourceRootKind.FRONTEND && (type.kind() == ParsedCodeTypeKind.INTERFACE
                || type.kind() == ParsedCodeTypeKind.TYPE_ALIAS
                || containsAnyText(packageName + " " + sourcePath, "model", "models", "dto", "types", "interfaces"));
    }

    private boolean hasAnnotation(ParsedCodeType type, String... expected) {
        for (String annotation : type.annotations()) {
            String normalized = normalized(annotation);
            for (String item : expected) {
                if (normalized.startsWith(normalized(item))) {
                    return true;
                }
            }
        }
        return false;
    }

    private String metadata(Map<String, String> metadata, String key) {
        return metadata == null ? "" : metadata.getOrDefault(key, "");
    }

    private boolean containsAnyText(String first, String... rest) {
        String source = normalized(first);
        for (String needle : rest) {
            if (source.contains(normalized(needle))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAnyIn(String first, String second, String third, String fourth, String... needles) {
        String haystack = normalized(first) + " " + normalized(second) + " " + normalized(third) + " " + normalized(fourth);
        for (String needle : needles) {
            if (haystack.contains(normalized(needle))) {
                return true;
            }
        }
        return false;
    }

    private String normalized(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
    }
}
