package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMember;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMemberKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMetadataKeys;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeModule;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelation;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelationKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeVisibility;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceFileCandidate;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRoot;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/** Extrae un modelo neutral desde tokens TypeScript de forma aislada del dominio UML. */
final class TypeScriptModelExtractor {
    private final SourceRoot sourceRoot;
    private final SourceFileCandidate file;
    private final List<TypeScriptToken> tokens;
    private final String source;
    private final Map<String, ParsedCodeModule> modulesById = new LinkedHashMap<>();
    private final List<ParsedCodeType> types = new ArrayList<>();
    private final List<ParsedCodeRelation> relations = new ArrayList<>();
    TypeScriptModelExtractor(SourceRoot sourceRoot, SourceFileCandidate file, List<TypeScriptToken> tokens) {
        this(sourceRoot, file, tokens, "");
    }

    TypeScriptModelExtractor(SourceRoot sourceRoot, SourceFileCandidate file, List<TypeScriptToken> tokens, String source) {
        this.sourceRoot = sourceRoot;
        this.file = file;
        this.tokens = List.copyOf(tokens == null ? List.of() : tokens);
        this.source = source == null ? "" : source;
    }
    Extraction extract() {
        List<String> pendingDecorators = new ArrayList<>();
        int index = 0;
        while (index < tokens.size()) {
            TypeScriptToken token = tokens.get(index);
            if (token.is("@")) {
                String decorator = readDecorator(index);
                if (!decorator.isBlank()) {
                    pendingDecorators.add(decorator);
                }
                index = nextDeclarationCursor(index + 1);
            } else if (isSkippableModifier(token.text())) {
                index++;
            } else if (isTypeDeclaration(token.text())) {
                index = parseDeclaration(index, pendingDecorators);
                pendingDecorators = new ArrayList<>();
            } else {
                pendingDecorators = new ArrayList<>();
                index++;
            }
        }
        return new Extraction(List.copyOf(modulesById.values()), List.copyOf(types), List.copyOf(relations));
    }
    private int parseDeclaration(int start, List<String> decorators) {
        String keyword = tokens.get(start).text();
        ParsedCodeTypeKind kind = kindFor(keyword);
        int nameIndex = nextIdentifier(start + 1);
        if (nameIndex < 0) {
            return start + 1;
        }
        String simpleName = tokens.get(nameIndex).text();
        int bodyStart = findBodyStart(nameIndex + 1, keyword);
        int bodyEnd = bodyStart >= 0 ? findMatching(bodyStart, "{", "}") : -1;
        String moduleName = moduleName();
        ParsedCodeModule module = moduleFor(moduleName);
        String qualifiedName = moduleName.isBlank() ? simpleName : moduleName + "." + simpleName;
        String typeId = typeId(qualifiedName);
        List<ParsedCodeMember> members = bodyStart >= 0 && bodyEnd > bodyStart
                ? membersFor(typeId, simpleName, bodyStart + 1, bodyEnd, kind)
                : List.of();
        Map<String, String> metadata = typeMetadata(keyword, simpleName, bodyStart, bodyEnd, decorators, abstractModifierBefore(start));
        ParsedCodeType type = new ParsedCodeType(typeId, sourceRoot.id(), module.id(), qualifiedName, simpleName,
                kind, file.relativePath(), moduleName, members, decorators, metadata);
        types.add(type);
        collectHeaderRelations(type, nameIndex + 1, bodyStart < 0 ? tokens.size() : bodyStart);
        collectAssociationRelations(type, members);
        if (kind == ParsedCodeTypeKind.TYPE_ALIAS && bodyStart >= 0) {
            int aliasEnd = findAliasEnd(bodyStart + 1);
            collectAliasAssociations(type, bodyStart + 1, aliasEnd);
            return aliasEnd < tokens.size() ? aliasEnd + 1 : aliasEnd;
        }
        return bodyEnd > bodyStart ? bodyEnd + 1 : Math.max(nameIndex + 1, start + 1);
    }
    private List<ParsedCodeMember> membersFor(String typeId, String ownerSimpleName, int start, int end,
                                              ParsedCodeTypeKind typeKind) {
        List<ParsedCodeMember> members = new ArrayList<>();
        int index = start;
        while (index < end) {
            index = skipMemberSeparators(index, end);
            if (index >= end) {
                break;
            }
            int next = parseMember(typeId, ownerSimpleName, index, end, typeKind, members);
            index = Math.max(next, index + 1);
        }
        return members;
    }
    private int parseMember(String typeId, String ownerSimpleName, int start, int end, ParsedCodeTypeKind ownerKind,
                            List<ParsedCodeMember> members) {
        List<String> annotations = new ArrayList<>();
        int index = start;
        while (index < end && tokens.get(index).is("@")) {
            String decorator = readDecorator(index);
            if (!decorator.isBlank()) {
                annotations.add(decorator);
            }
            index = nextDeclarationCursor(index + 1);
        }
        ParsedCodeVisibility visibility = ParsedCodeVisibility.PUBLIC;
        while (index < end && isMemberModifier(tokens.get(index).text())) {
            visibility = visibilityFromModifier(tokens.get(index).text(), visibility);
            index++;
        }
        if (index >= end || !tokens.get(index).isIdentifier()) {
            return advanceToMemberBoundary(index, end);
        }
        String name = tokens.get(index).text();
        if ("constructor".equals(name)) {
            members.add(new ParsedCodeMember(memberId(typeId, "constructor", ownerSimpleName),
                    ParsedCodeMemberKind.CONSTRUCTOR, ownerSimpleName, "", signatureFrom(index, end), visibility,
                    annotations, Map.of("language", "typescript")));
            return advanceAfterCallable(index, end);
        }
        int next = index + 1;
        if (next < end && tokens.get(next).is("?")) {
            next++;
        }
        if (next < end && tokens.get(next).is("(")) {
            members.add(new ParsedCodeMember(memberId(typeId, "method", name), ParsedCodeMemberKind.METHOD,
                    name, returnTypeAfterCallable(next, end), signatureFrom(index, end), visibility, annotations,
                    Map.of("language", "typescript")));
            return advanceAfterCallable(index, end);
        }
        String typeExpression = typeAfterColon(next, end);
        ParsedCodeMemberKind memberKind = ownerKind == ParsedCodeTypeKind.INTERFACE
                ? ParsedCodeMemberKind.PROPERTY
                : ParsedCodeMemberKind.FIELD;
        members.add(new ParsedCodeMember(memberId(typeId, memberKind.name().toLowerCase(), name), memberKind,
                name, typeExpression, propertySignature(name, typeExpression), visibility, annotations,
                Map.of("language", "typescript")));
        return advanceToMemberBoundary(index, end);
    }
    private void collectHeaderRelations(ParsedCodeType type, int start, int end) {
        int index = start;
        while (index < end) {
            String token = tokens.get(index).text();
            if ("extends".equals(token)) {
                index = collectTypeList(type, index + 1, end, ParsedCodeRelationKind.EXTENDS,
                        "Herencia TypeScript detectada desde extends.");
            } else if ("implements".equals(token)) {
                index = collectTypeList(type, index + 1, end, ParsedCodeRelationKind.IMPLEMENTS,
                        "Realización TypeScript detectada desde implements.");
            } else {
                index++;
            }
        }
    }
    private int collectTypeList(ParsedCodeType type, int start, int end, ParsedCodeRelationKind kind,
                                String description) {
        int index = start;
        while (index < end) {
            TypeScriptToken token = tokens.get(index);
            if (token.is("{") || token.is("=") || ";".equals(token.text())
                    || "extends".equals(token.text()) || "implements".equals(token.text())) {
                break;
            }
            if (token.isIdentifier()) {
                addRelation(type, TypeScriptTypeNamePolicy.simpleName(token.text()), kind, description);
            }
            index++;
        }
        return index;
    }
    private void collectAssociationRelations(ParsedCodeType type, List<ParsedCodeMember> members) {
        for (ParsedCodeMember member : members) {
            if (member.kind() != ParsedCodeMemberKind.FIELD && member.kind() != ParsedCodeMemberKind.PROPERTY) {
                continue;
            }
            for (String target : TypeScriptTypeNamePolicy.associationTargets(member.type())) {
                addRelation(type, target, ParsedCodeRelationKind.ASSOCIATION,
                        "Asociación tentativa TypeScript detectada desde miembro " + member.name() + ".");
            }
        }
    }
    private void collectAliasAssociations(ParsedCodeType type, int start, int end) {
        StringBuilder expression = new StringBuilder();
        for (int index = start; index < end; index++) {
            expression.append(tokens.get(index).text()).append(' ');
        }
        for (String target : TypeScriptTypeNamePolicy.associationTargets(expression.toString())) {
            addRelation(type, target, ParsedCodeRelationKind.ASSOCIATION,
                    "Asociación tentativa TypeScript detectada desde type alias.");
        }
    }
    private Map<String, String> typeMetadata(String keyword, String simpleName, int bodyStart, int bodyEnd,
                                             List<String> decorators, boolean abstractType) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ParsedCodeMetadataKeys.LANGUAGE, "typescript");
        metadata.put(ParsedCodeMetadataKeys.SOURCE_PATH, file.relativePath().toString());
        metadata.put(ParsedCodeMetadataKeys.ABSOLUTE_SOURCE_PATH, file.absolutePath().toString());
        metadata.put(ParsedCodeMetadataKeys.SOURCE_ROOT_PATH, sourceRoot.path().toString());
        metadata.put("declaration", keyword);
        metadata.put(ParsedCodeMetadataKeys.FRAMEWORK_HINT, frameworkHint(bodyStart, bodyEnd, decorators));
        if (abstractType) {
            metadata.put(ParsedCodeMetadataKeys.ABSTRACT_TYPE, "true");
        }
        String apiCalls = TypeScriptApiClientRouteExtractor.apiClientRoutesFor(source, tokens, simpleName, bodyStart, bodyEnd);
        if (!apiCalls.isBlank()) {
            metadata.put(ParsedCodeMetadataKeys.API_CLIENT_ROUTES, apiCalls);
        }
        return Map.copyOf(metadata);
    }

    private boolean abstractModifierBefore(int declarationStart) {
        int index = declarationStart - 1;
        while (index >= 0) {
            String text = tokens.get(index).text();
            if ("abstract".equals(text)) {
                return true;
            }
            if (!isSkippableModifier(text)) {
                return false;
            }
            index--;
        }
        return false;
    }

    private String frameworkHint(int bodyStart, int bodyEnd, List<String> decorators) {
        for (String decorator : decorators == null ? List.<String>of() : decorators) {
            String name = decorator.replace("@", "").strip();
            if (List.of("Component", "Injectable", "Directive", "Pipe", "NgModule").contains(name)) {
                return "angular:" + name;
            }
        }
        if (bodyStart <= 0) {
            return "";
        }
        int from = Math.max(0, bodyStart - 12);
        for (int index = from; index < bodyStart; index++) {
            if (tokens.get(index).is("@") && index + 1 < tokens.size()) {
                String name = tokens.get(index + 1).text();
                if (List.of("Component", "Injectable", "Directive", "Pipe", "NgModule").contains(name)) {
                    return "angular:" + name;
                }
            }
        }
        return "";
    }
    private int findAliasEnd(int start) {
        for (int index = start; index < tokens.size(); index++) {
            if (tokens.get(index).is(";")) {
                return index;
            }
        }
        return tokens.size();
    }
    private int findBodyStart(int start, String keyword) {
        String target = "type".equals(keyword) ? "=" : "{";
        for (int index = start; index < tokens.size(); index++) {
            if (tokens.get(index).is(target)) {
                return index;
            }
            if (tokens.get(index).is(";") && !"type".equals(keyword)) {
                return -1;
            }
        }
        return -1;
    }
    private int findMatching(int start, String open, String close) {
        int depth = 0;
        for (int index = start; index < tokens.size(); index++) {
            if (tokens.get(index).is(open)) {
                depth++;
            } else if (tokens.get(index).is(close) && --depth == 0) {
                return index;
            }
        }
        return -1;
    }
    private String typeAfterColon(int start, int end) {
        int colon = -1;
        for (int index = start; index < end; index++) {
            if (tokens.get(index).is(":")) {
                colon = index + 1;
                break;
            }
            if (isMemberBoundaryOrBody(tokens.get(index).text())) {
                return "";
            }
        }
        if (colon < 0) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (int index = colon; index < end && !isMemberBoundaryOrBody(tokens.get(index).text()); index++) {
            if (tokens.get(index).is("=")) {
                break;
            }
            out.append(tokens.get(index).text()).append(' ');
        }
        return out.toString().strip();
    }
    private String returnTypeAfterCallable(int parenStart, int end) {
        int parenEnd = findMatching(parenStart, "(", ")");
        return parenEnd >= 0 ? typeAfterColon(parenEnd + 1, end) : "";
    }
    private String signatureFrom(int start, int end) {
        StringBuilder signature = new StringBuilder();
        for (int index = start; index < end && !isMemberBoundaryOrBody(tokens.get(index).text()); index++) {
            signature.append(tokens.get(index).text());
            if (!List.of("(", ")", ":", "?", ".", ",", "<", ">").contains(tokens.get(index).text())) {
                signature.append(' ');
            }
        }
        return signature.toString().strip();
    }
    private String propertySignature(String name, String type) {
        return type.isBlank() ? name : name + ": " + type;
    }
    private int advanceAfterCallable(int start, int end) {
        for (int index = start; index < end; index++) {
            if (tokens.get(index).is("{")) {
                int close = findMatching(index, "{", "}");
                return close > index ? close + 1 : end;
            }
            if (isMemberBoundary(tokens.get(index).text())) {
                return index + 1;
            }
        }
        return end;
    }
    private int advanceToMemberBoundary(int start, int end) {
        int nested = 0;
        for (int index = start; index < end; index++) {
            String token = tokens.get(index).text();
            if ("(".equals(token) || "<".equals(token) || "[".equals(token)) {
                nested++;
            } else if ((")".equals(token) || ">".equals(token) || "]".equals(token)) && nested > 0) {
                nested--;
            } else if (nested == 0 && isMemberBoundary(token)) {
                return index + 1;
            }
        }
        return end;
    }
    private int skipMemberSeparators(int index, int end) {
        while (index < end && isMemberBoundary(tokens.get(index).text())) {
            index++;
        }
        return index;
    }
    private boolean isMemberBoundary(String token) {
        return ";".equals(token) || ",".equals(token) || "}".equals(token);
    }
    private boolean isMemberBoundaryOrBody(String token) {
        return isMemberBoundary(token) || "{".equals(token);
    }
    private int nextIdentifier(int start) {
        for (int index = start; index < tokens.size(); index++) {
            if (tokens.get(index).isIdentifier()) {
                return index;
            }
        }
        return -1;
    }
    private int nextDeclarationCursor(int start) {
        int index = start;
        while (index < tokens.size() && (tokens.get(index).is(".") || tokens.get(index).isIdentifier())) {
            index++;
        }
        if (index < tokens.size() && tokens.get(index).is("(")) {
            int close = findMatching(index, "(", ")");
            return close > index ? close + 1 : index + 1;
        }
        return index;
    }
    private String readDecorator(int atIndex) {
        if (atIndex + 1 >= tokens.size() || !tokens.get(atIndex + 1).isIdentifier()) {
            return "";
        }
        StringBuilder out = new StringBuilder("@").append(tokens.get(atIndex + 1).text());
        int index = atIndex + 2;
        while (index + 1 < tokens.size() && tokens.get(index).is(".") && tokens.get(index + 1).isIdentifier()) {
            out.append('.').append(tokens.get(index + 1).text());
            index += 2;
        }
        return out.toString();
    }
    private ParsedCodeModule moduleFor(String moduleName) {
        String display = moduleName.isBlank() ? "default" : moduleName;
        String id = sourceRoot.id() + ":" + sanitize(display);
        Path parent = file.relativePath().getParent();
        return modulesById.computeIfAbsent(id, key -> new ParsedCodeModule(key, sourceRoot.id(), moduleName, display, parent));
    }
    private String moduleName() {
        Path parent = file.relativePath().getParent();
        return parent == null ? "" : parent.toString().replace('\\', '.').replace('/', '.');
    }
    private void addRelation(ParsedCodeType source, String targetName, ParsedCodeRelationKind kind, String description) {
        if (targetName == null || targetName.isBlank() || targetName.equals(source.simpleName())) {
            return;
        }
        String id = source.id() + ":" + kind.name().toLowerCase() + ":" + sanitize(targetName) + ":" + relations.size();
        relations.add(new ParsedCodeRelation(id, source.id(), targetName, kind, description, Map.of("language", "typescript")));
    }
    private ParsedCodeTypeKind kindFor(String keyword) {
        return switch (keyword) {
            case "interface" -> ParsedCodeTypeKind.INTERFACE;
            case "enum" -> ParsedCodeTypeKind.ENUM;
            case "type" -> ParsedCodeTypeKind.TYPE_ALIAS;
            default -> ParsedCodeTypeKind.CLASS;
        };
    }
    private boolean isTypeDeclaration(String token) {
        return List.of("class", "interface", "enum", "type").contains(token);
    }
    private boolean isSkippableModifier(String token) {
        return List.of("export", "default", "abstract", "declare", "async", "namespace", "module").contains(token);
    }
    private boolean isMemberModifier(String token) {
        return List.of("public", "private", "protected", "readonly", "static", "abstract", "override", "async").contains(token);
    }
    private ParsedCodeVisibility visibilityFromModifier(String modifier, ParsedCodeVisibility fallback) {
        return switch (modifier) {
            case "private" -> ParsedCodeVisibility.PRIVATE;
            case "protected" -> ParsedCodeVisibility.PROTECTED;
            case "public" -> ParsedCodeVisibility.PUBLIC;
            default -> fallback;
        };
    }
    private String typeId(String qualifiedName) {
        return sourceRoot.id() + ":" + qualifiedName;
    }
    private String memberId(String typeId, String kind, String name) {
        return typeId + ":" + kind + ":" + name;
    }
    private static String sanitize(String value) {
        return value == null ? "default" : value.strip().replaceAll("[^A-Za-z0-9_.-]+", "-");
    }
    record Extraction(List<ParsedCodeModule> modules, List<ParsedCodeType> types, List<ParsedCodeRelation> relations) {
    }
}
