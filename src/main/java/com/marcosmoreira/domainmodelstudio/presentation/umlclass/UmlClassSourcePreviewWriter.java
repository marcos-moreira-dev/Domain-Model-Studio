package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

/** Genera una vista fuente temporal cuando una clase UML no conserva ruta de archivo real. */
final class UmlClassSourcePreviewWriter {

    Path writePreview(UmlClassNode node) throws IOException {
        Objects.requireNonNull(node, "node");
        Path root = Path.of(System.getProperty("java.io.tmpdir", "."), "domain-model-studio", "uml-class-preview");
        Files.createDirectories(root);
        Path file = root.resolve(safeFileName(node.displayName()) + extensionFor(node));
        Files.writeString(file, sourceFor(node), StandardCharsets.UTF_8);
        return file;
    }

    private String sourceFor(UmlClassNode node) {
        StringBuilder builder = new StringBuilder();
        if (!node.packageName().isBlank()) {
            builder.append("package ").append(safePackage(node.packageName())).append(";\n\n");
        }
        builder.append("/**\n")
                .append(" * Vista temporal generada desde el diagrama UML.\n")
                .append(" * No sustituye al archivo fuente real.\n")
                .append(" */\n");
        builder.append(typeDeclaration(node)).append(" {\n");
        for (UmlClassMember member : node.members()) {
            builder.append(memberLine(member));
        }
        builder.append("}\n");
        return builder.toString();
    }

    private String typeDeclaration(UmlClassNode node) {
        String visibility = javaVisibility(node.visibility());
        String name = safeTypeName(node.displayName());
        return switch (node.kind()) {
            case INTERFACE -> visibility + "interface " + name;
            case ENUM -> visibility + "enum " + name;
            case RECORD -> visibility + "record " + name + "()";
            case ABSTRACT_CLASS -> visibility + "abstract class " + name;
            default -> visibility + "class " + name;
        };
    }

    private String memberLine(UmlClassMember member) {
        String visibility = javaVisibility(member.visibility());
        if (member.kind() == UmlMemberKind.CONSTRUCTOR) {
            return "\n    " + visibility + sanitizeSignature(member.signature(), safeTypeName(member.name()) + "()") + " {\n    }\n";
        }
        if (member.kind() == UmlMemberKind.METHOD) {
            String signature = member.signature().isBlank()
                    ? safeType(member.type(), "void") + " " + safeMemberName(member.name()) + "()"
                    : sanitizeSignature(member.signature(), "void " + safeMemberName(member.name()) + "()");
            return "\n    " + visibility + signature + " {\n        // TODO: implementar\n    }\n";
        }
        return "\n    " + visibility + safeType(member.type(), "Object") + " " + safeMemberName(member.name()) + ";\n";
    }

    private String extensionFor(UmlClassNode node) {
        return node.kind() == UmlClassKind.INTERFACE || node.kind() == UmlClassKind.ENUM || node.kind() == UmlClassKind.RECORD
                ? ".java"
                : ".java";
    }

    private String javaVisibility(UmlVisibility visibility) {
        return visibility == UmlVisibility.PRIVATE ? "private "
                : visibility == UmlVisibility.PROTECTED ? "protected "
                : visibility == UmlVisibility.PACKAGE || visibility == UmlVisibility.UNSPECIFIED ? ""
                : "public ";
    }

    private String sanitizeSignature(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.isBlank()) {
            return fallback;
        }
        return normalized.replace(";", "").replace("{", "").replace("}", "");
    }

    private String safeType(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.isBlank()) {
            return fallback;
        }
        return normalized.replaceAll("[^A-Za-z0-9_.$<>\\[\\], ?]", "");
    }

    private String safeTypeName(String value) {
        String normalized = safeIdentifier(value, "ClaseUml");
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }

    private String safeMemberName(String value) {
        return safeIdentifier(value, "miembro");
    }

    private String safePackage(String value) {
        String normalized = value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
        String[] parts = normalized.split("\\.");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (builder.length() > 0) {
                builder.append('.');
            }
            builder.append(safeIdentifier(part, "paquete"));
        }
        return builder.toString();
    }

    private String safeFileName(String value) {
        return safeTypeName(value).replaceAll("[^A-Za-z0-9_.-]", "_");
    }

    private String safeIdentifier(String value, String fallback) {
        String normalized = value == null ? "" : value.strip().replaceAll("[^A-Za-z0-9_]", "_");
        if (normalized.isBlank() || Character.isDigit(normalized.charAt(0))) {
            normalized = fallback + (normalized.isBlank() ? "" : "_" + normalized);
        }
        return normalized;
    }
}
