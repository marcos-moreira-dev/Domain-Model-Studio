package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Formatea el detalle completo de una clase UML para el panel derecho.
 *
 * <p>El lienzo puede mostrar una versión ligera de la caja, pero este formateador
 * siempre recorre el modelo completo conservado en {@link UmlClassNode#members()}.
 * No debe truncar nombres ni firmas: su función es servir como vista de detalle
 * bajo demanda para proyectos importados desde código.</p>
 */
public final class UmlClassFullDetailFormatter {

    public String format(UmlClassNode node) {
        if (node == null) {
            return "Selecciona una clase, interfaz, enum o record para ver aquí su detalle completo.";
        }
        List<String> lines = new ArrayList<>();
        appendHeader(lines, node);
        appendMembers(lines, node.members());
        return String.join(System.lineSeparator(), lines);
    }

    private void appendHeader(List<String> lines, UmlClassNode node) {
        lines.add("Nombre: " + value(node.displayName()));
        lines.add("Tipo: " + node.kind().displayName());
        lines.add("Visibilidad: " + node.visibility().displayName());
        lines.add("Paquete: " + value(node.packageName()));
        lines.add("Módulo: " + value(node.moduleId()));
        lines.add("Responsabilidad: " + value(node.responsibility()));
        lines.add("Descripción/origen: " + value(node.description()));
        lines.add("Notas/metadatos: " + value(node.notes()));
        lines.add("");
        lines.add("Resumen de miembros: " + summary(node.members()));
    }

    private void appendMembers(List<String> lines, List<UmlClassMember> members) {
        lines.add("");
        appendGroup(lines, "Atributos", members, UmlMemberKind.ATTRIBUTE);
        appendGroup(lines, "Constructores", members, UmlMemberKind.CONSTRUCTOR);
        appendGroup(lines, "Métodos", members, UmlMemberKind.METHOD);
    }

    private void appendGroup(List<String> lines, String title, List<UmlClassMember> members, UmlMemberKind kind) {
        List<UmlClassMember> group = members.stream()
                .filter(member -> member.kind() == kind)
                .sorted(Comparator.comparing(UmlClassMember::name, String.CASE_INSENSITIVE_ORDER))
                .toList();
        lines.add(title + " (" + group.size() + ")");
        if (group.isEmpty()) {
            lines.add("  —");
            lines.add("");
            return;
        }
        for (UmlClassMember member : group) {
            lines.add("  - " + member.displayText() + qualifiers(member));
            if (!member.description().isBlank()) {
                lines.add("    Descripción: " + member.description());
            }
        }
        lines.add("");
    }

    private String summary(List<UmlClassMember> members) {
        Map<UmlMemberKind, Long> counts = members.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(UmlClassMember::kind, Collectors.counting()));
        long attributes = counts.getOrDefault(UmlMemberKind.ATTRIBUTE, 0L);
        long constructors = counts.getOrDefault(UmlMemberKind.CONSTRUCTOR, 0L);
        long methods = counts.getOrDefault(UmlMemberKind.METHOD, 0L);
        return members.size() + " total · "
                + attributes + " atributos · "
                + constructors + " constructores · "
                + methods + " métodos";
    }

    private String qualifiers(UmlClassMember member) {
        List<String> parts = new ArrayList<>();
        parts.add(member.visibility().displayName());
        if (member.staticMember()) {
            parts.add("static");
        }
        if (!member.type().isBlank() && (member.kind() == UmlMemberKind.METHOD || member.kind() == UmlMemberKind.CONSTRUCTOR)) {
            parts.add("retorna " + member.type());
        }
        return parts.isEmpty() ? "" : " [" + String.join(", ", parts) + "]";
    }

    private String value(String value) {
        return value == null || value.isBlank() ? "—" : value.strip();
    }
}
