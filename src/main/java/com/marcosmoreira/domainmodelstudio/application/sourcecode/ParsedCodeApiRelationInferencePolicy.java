package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Infiere relaciones Frontend → Backend a partir de rutas HTTP detectadas en servicios TypeScript
 * y controladores Java/Spring sin acoplar el dominio UML a esos frameworks.
 */
public final class ParsedCodeApiRelationInferencePolicy {
    private static final Pattern REQUEST_VALUE_PATTERN = Pattern.compile(
            "(?:value\\s*=\\s*)?[\\\"']([^\\\"']+)[\\\"']");
    private static final Map<String, String> SPRING_METHODS = Map.of(
            "GetMapping", "GET",
            "PostMapping", "POST",
            "PutMapping", "PUT",
            "DeleteMapping", "DELETE",
            "PatchMapping", "PATCH");

    public List<ParsedCodeRelation> inferApiRelations(ParsedCodeProject project) {
        if (project == null || project.types().isEmpty()) {
            return List.of();
        }
        List<ApiEndpoint> backendEndpoints = backendEndpoints(project.types());
        List<ApiCall> frontendCalls = frontendCalls(project.types());
        ArrayList<ParsedCodeRelation> relations = new ArrayList<>();
        Set<String> semanticKeys = new LinkedHashSet<>();
        for (ApiCall call : frontendCalls) {
            bestEndpointFor(call, backendEndpoints).ifPresent(endpoint -> {
                String key = call.source().id() + "|" + endpoint.controller().id() + "|" + call.httpMethod() + "|" + endpoint.path();
                if (semanticKeys.add(key)) {
                    relations.add(toRelation(call, endpoint, relations.size()));
                }
            });
        }
        return List.copyOf(relations);
    }

    private ParsedCodeRelation toRelation(ApiCall call, ApiEndpoint endpoint, int index) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ParsedCodeMetadataKeys.INFERRED, "true");
        metadata.put(ParsedCodeMetadataKeys.INFERENCE_REASON, "Coincidencia de ruta HTTP frontend-backend");
        metadata.put(ParsedCodeMetadataKeys.SOURCE_ROLE, roleOf(call.source()));
        metadata.put(ParsedCodeMetadataKeys.TARGET_ROLE, roleOf(endpoint.controller()));
        metadata.put(ParsedCodeMetadataKeys.API_HTTP_METHOD, call.httpMethod());
        metadata.put(ParsedCodeMetadataKeys.API_PATH, endpoint.path());
        metadata.put(ParsedCodeMetadataKeys.API_SOURCE_ROUTE, call.path());
        metadata.put(ParsedCodeMetadataKeys.API_TARGET_ROUTE, endpoint.path());
        metadata.put(ParsedCodeMetadataKeys.API_MATCH_KIND, matchKind(call, endpoint));
        String id = call.source().id() + ":api:" + sanitize(call.httpMethod() + "-" + endpoint.path()) + ":" + index;
        String description = "Consumo API inferido: " + call.httpMethod() + " " + endpoint.path() + ".";
        return new ParsedCodeRelation(id, call.source().id(), endpoint.controller().simpleName(),
                ParsedCodeRelationKind.API_CALL, description, metadata);
    }

    private Optional<ApiEndpoint> bestEndpointFor(ApiCall call, List<ApiEndpoint> endpoints) {
        return endpoints.stream()
                .filter(endpoint -> httpCompatible(call.httpMethod(), endpoint.httpMethod()))
                .filter(endpoint -> pathCompatible(call.path(), endpoint.path()))
                .findFirst();
    }

    private boolean httpCompatible(String frontendMethod, String backendMethod) {
        if (backendMethod.isBlank() || "ANY".equals(backendMethod)) {
            return true;
        }
        return frontendMethod.equals(backendMethod);
    }

    private boolean pathCompatible(String frontendPath, String backendPath) {
        String left = normalizePath(frontendPath);
        String right = normalizePath(backendPath);
        return !left.isBlank() && !right.isBlank()
                && (left.equals(right) || left.endsWith(right) || right.endsWith(left) || sameLastSegments(left, right));
    }

    private boolean sameLastSegments(String left, String right) {
        String[] a = left.split("/");
        String[] b = right.split("/");
        if (a.length == 0 || b.length == 0) {
            return false;
        }
        String lastA = a[a.length - 1];
        String lastB = b[b.length - 1];
        return !lastA.isBlank() && lastA.equals(lastB);
    }

    private String matchKind(ApiCall call, ApiEndpoint endpoint) {
        String left = normalizePath(call.path());
        String right = normalizePath(endpoint.path());
        return left.equals(right) ? "exact" : "suffix";
    }

    private List<ApiEndpoint> backendEndpoints(List<ParsedCodeType> types) {
        ArrayList<ApiEndpoint> endpoints = new ArrayList<>();
        for (ParsedCodeType type : types) {
            if (!isBackendController(type)) {
                continue;
            }
            String basePath = basePathFor(type.annotations());
            for (ParsedCodeMember member : type.members()) {
                endpoints.addAll(endpointFor(type, member, basePath));
            }
            if (endpoints.stream().noneMatch(endpoint -> endpoint.controller().id().equals(type.id()))) {
                endpoints.add(new ApiEndpoint(type, "ANY", normalizePath(basePath)));
            }
        }
        return List.copyOf(endpoints);
    }

    private List<ApiEndpoint> endpointFor(ParsedCodeType controller, ParsedCodeMember member, String basePath) {
        ArrayList<ApiEndpoint> endpoints = new ArrayList<>();
        for (String annotation : member.annotations()) {
            Mapping mapping = mappingOf(annotation);
            if (mapping.present()) {
                endpoints.add(new ApiEndpoint(controller, mapping.httpMethod(), joinPaths(basePath, mapping.path())));
            }
        }
        return endpoints;
    }

    private List<ApiCall> frontendCalls(List<ParsedCodeType> types) {
        ArrayList<ApiCall> calls = new ArrayList<>();
        for (ParsedCodeType type : types) {
            if (!isFrontendApiClient(type)) {
                continue;
            }
            String encoded = type.metadata().getOrDefault(ParsedCodeMetadataKeys.API_CLIENT_ROUTES, "");
            for (String item : encoded.split(";")) {
                String[] parts = item.strip().split("\\s+", 2);
                if (parts.length == 2 && !parts[1].isBlank()) {
                    calls.add(new ApiCall(type, parts[0].toUpperCase(Locale.ROOT), normalizePath(parts[1])));
                }
            }
        }
        return List.copyOf(calls);
    }

    private boolean isBackendController(ParsedCodeType type) {
        String role = roleOf(type);
        return ParsedCodeTypeRole.BACKEND_CONTROLLER.id().equals(role)
                || type.annotations().stream().anyMatch(annotation -> annotation.contains("RestController") || annotation.contains("Controller"));
    }

    private boolean isFrontendApiClient(ParsedCodeType type) {
        String role = roleOf(type);
        String routes = type.metadata().getOrDefault(ParsedCodeMetadataKeys.API_CLIENT_ROUTES, "");
        return !routes.isBlank() && (ParsedCodeTypeRole.FRONTEND_SERVICE.id().equals(role)
                || type.simpleName().toLowerCase(Locale.ROOT).contains("service"));
    }

    private String roleOf(ParsedCodeType type) {
        return type.metadata().getOrDefault(ParsedCodeMetadataKeys.ROLE, ParsedCodeTypeRole.UNKNOWN.id());
    }

    private String basePathFor(List<String> annotations) {
        for (String annotation : annotations) {
            Mapping mapping = mappingOf(annotation);
            if (mapping.present()) {
                return mapping.path();
            }
        }
        return "";
    }

    private Mapping mappingOf(String annotation) {
        if (annotation == null || annotation.isBlank()) {
            return Mapping.empty();
        }
        String annotationName = annotationName(annotation);
        if ("RequestMapping".equals(annotationName)) {
            return new Mapping("ANY", pathFromAnnotation(annotation));
        }
        String method = SPRING_METHODS.get(annotationName);
        if (method != null) {
            return new Mapping(method, pathFromAnnotation(annotation));
        }
        return Mapping.empty();
    }

    private String annotationName(String annotation) {
        String cleaned = annotation.strip();
        if (cleaned.startsWith("@")) {
            cleaned = cleaned.substring(1);
        }
        int paren = cleaned.indexOf('(');
        if (paren >= 0) {
            cleaned = cleaned.substring(0, paren);
        }
        int dot = cleaned.lastIndexOf('.');
        return dot >= 0 ? cleaned.substring(dot + 1) : cleaned;
    }

    private String pathFromAnnotation(String annotation) {
        Matcher matcher = REQUEST_VALUE_PATTERN.matcher(annotation == null ? "" : annotation);
        return matcher.find() ? normalizePath(matcher.group(1)) : "";
    }

    private String joinPaths(String basePath, String methodPath) {
        String base = normalizePath(basePath);
        String method = normalizePath(methodPath);
        if (base.isBlank()) {
            return method.isBlank() ? "/" : method;
        }
        if (method.isBlank() || "/".equals(method)) {
            return base;
        }
        return normalizePath(base + "/" + method);
    }

    private String normalizePath(String value) {
        String path = value == null ? "" : value.strip();
        if (path.isBlank()) {
            return "";
        }
        path = path.replace('\\', '/');
        path = path.replaceAll("https?://[^/]+", "");
        path = path.replaceAll("\\{[^}]+}", "{}");
        path = path.replaceAll(":([A-Za-z0-9_]+)", "{}");
        path = path.replaceAll("/+$", "");
        path = path.replaceAll("/+/", "/");
        return path.startsWith("/") ? path : "/" + path;
    }

    private String sanitize(String value) {
        return value == null ? "api" : value.strip().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]+", "-").replaceAll("^-+|-+$", "");
    }

    private record ApiEndpoint(ParsedCodeType controller, String httpMethod, String path) {
    }

    private record ApiCall(ParsedCodeType source, String httpMethod, String path) {
    }

    private record Mapping(String httpMethod, String path) {
        boolean present() {
            return !httpMethod.isBlank();
        }

        static Mapping empty() {
            return new Mapping("", "");
        }
    }
}
