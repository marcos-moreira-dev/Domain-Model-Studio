package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/** Claves de metadatos compartidas por parsers, normalizador y futuras vistas UML. */
public final class ParsedCodeMetadataKeys {
    public static final String LANGUAGE = "language";
    public static final String SOURCE_PATH = "sourcePath";
    public static final String ABSOLUTE_SOURCE_PATH = "absoluteSourcePath";
    public static final String SOURCE_ROOT_PATH = "sourceRootPath";
    public static final String SOURCE_ROOT_KIND = "sourceRootKind";
    public static final String ROLE = "role";
    public static final String FRAMEWORK_HINT = "frameworkHint";
    public static final String ABSTRACT_TYPE = "abstractType";
    public static final String TARGET_TYPE_ID = "targetTypeId";
    public static final String TARGET_QUALIFIED_NAME = "targetQualifiedName";
    public static final String TARGET_SOURCE_ROOT_ID = "targetSourceRootId";
    public static final String RESOLVED = "resolved";
    public static final String INFERRED = "inferred";
    public static final String INFERENCE_REASON = "inferenceReason";
    public static final String SOURCE_ROLE = "sourceRole";
    public static final String TARGET_ROLE = "targetRole";
    public static final String API_CLIENT_ROUTES = "apiClientRoutes";
    public static final String API_ENDPOINTS = "apiEndpoints";
    public static final String API_HTTP_METHOD = "apiHttpMethod";
    public static final String API_PATH = "apiPath";
    public static final String API_MATCH_KIND = "apiMatchKind";
    public static final String API_SOURCE_ROUTE = "apiSourceRoute";
    public static final String API_TARGET_ROUTE = "apiTargetRoute";
    public static final String RELATION_SOURCE_MEMBER = "relationSourceMember";
    public static final String RELATION_SOURCE_TYPE = "relationSourceType";
    public static final String RELATION_SOURCE_PATTERN = "relationSourcePattern";
    public static final String RELATION_OWNERSHIP_HINT = "relationOwnershipHint";

    private ParsedCodeMetadataKeys() {
    }
}
