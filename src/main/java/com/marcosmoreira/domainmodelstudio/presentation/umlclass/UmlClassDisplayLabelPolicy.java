package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Política visual para nombres largos y metadatos de UML Clases importado desde código.
 *
 * <p>El lienzo debe mantenerse legible aunque el tipo real tenga nombres Java/TypeScript extensos.
 * Por eso se muestra una etiqueta corta y los datos completos quedan disponibles en tooltip,
 * panel de propiedades y barra de estado.</p>
 */
public final class UmlClassDisplayLabelPolicy {

    private static final int MODULE_TITLE_MAX = 30;
    private static final int CLASS_TITLE_MAX = 28;
    private static final int MEMBER_LINE_MAX = 76;
    private static final int COMBO_LABEL_MAX = 36;
    private static final String ELLIPSIS = "...";

    public String moduleTitle(UmlModuleGroup module) {
        return module == null ? "Módulo" : truncate(module.displayName(), MODULE_TITLE_MAX);
    }

    public String classTitle(UmlClassNode node) {
        return node == null ? "Clase" : truncate(node.displayName(), CLASS_TITLE_MAX);
    }

    public String comboClassLabel(UmlClassNode node) {
        return node == null ? "" : truncate(node.displayName(), COMBO_LABEL_MAX);
    }

    public String memberLine(UmlClassMember member) {
        return member == null ? "" : truncate(member.displayText(), MEMBER_LINE_MAX);
    }

    public String moduleTooltip(UmlModuleGroup module) {
        if (module == null) {
            return "Módulo UML";
        }
        return lines(
                "Módulo: " + module.displayName(),
                "Ruta/carpeta: " + valueOrDash(privatePath(module.path())),
                "Descripción: " + valueOrDash(module.description()),
                "Notas: " + valueOrDash(module.notes())
        );
    }

    public String classTooltip(UmlClassNode node) {
        if (node == null) {
            return "Clase UML";
        }
        return lines(
                "Nombre completo: " + node.displayName(),
                "Tipo: " + node.kind().displayName(),
                "Paquete/módulo: " + valueOrDash(node.packageName()),
                "Responsabilidad: " + valueOrDash(node.responsibility()),
                "Origen/metadatos: " + sourceSummary(node)
        );
    }

    public String memberTooltip(UmlClassMember member) {
        if (member == null) {
            return "Miembro UML";
        }
        return lines(
                "Miembro: " + member.displayText(),
                "Nombre: " + member.name(),
                "Tipo/retorno: " + valueOrDash(member.type()),
                "Firma: " + valueOrDash(member.signature()),
                "Descripción: " + valueOrDash(member.description())
        );
    }

    public String classMetadataPanel(UmlClassNode node) {
        if (node == null) {
            return "Selecciona una clase para ver nombre completo, ruta y metadatos de origen.";
        }
        return lines(
                "Nombre completo: " + node.displayName(),
                "Paquete: " + valueOrDash(node.packageName()),
                "Tipo: " + node.kind().displayName(),
                "Visibilidad: " + node.visibility().displayName(),
                "Responsabilidad: " + valueOrDash(node.responsibility()),
                "Descripción/origen: " + valueOrDash(privatePath(node.description())),
                "Notas/metadatos: " + valueOrDash(privatePath(node.notes()))
        );
    }

    public String statusForModule(UmlModuleGroup module) {
        if (module == null) {
            return "Módulo UML seleccionado.";
        }
        return "Módulo UML: " + module.displayName() + " · " + valueOrDash(privatePath(module.path()));
    }

    public String statusForClass(UmlClassNode node) {
        if (node == null) {
            return "Clase UML seleccionada.";
        }
        return "Clase UML: " + node.displayName() + " · " + sourceSummary(node);
    }

    static String truncate(String value, int maxLength) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.length() <= maxLength || maxLength < ELLIPSIS.length() + 1) {
            return normalized;
        }
        return normalized.substring(0, maxLength - ELLIPSIS.length()) + ELLIPSIS;
    }

    private String sourceSummary(UmlClassNode node) {
        String description = node.description();
        if (description != null && !description.isBlank()) {
            return privatePath(description);
        }
        String notes = node.notes();
        if (notes != null && !notes.isBlank()) {
            return privatePath(notes);
        }
        return "sin ruta registrada";
    }

    private static String privatePath(String value) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.isBlank()) {
            return "";
        }
        String userHome = System.getProperty("user.home", "");
        if (!userHome.isBlank()) {
            String forwardHome = userHome.replace('\\', '/');
            String forwardValue = normalized.replace('\\', '/');
            if (forwardValue.contains(forwardHome)) {
                return normalized.replace(userHome, "${USER_HOME}");
            }
        }
        return normalized;
    }

    private static String lines(String... values) {
        List<String> lines = new ArrayList<>();
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                lines.add(value);
            }
        }
        return String.join(System.lineSeparator(), lines);
    }

    private static String valueOrDash(String value) {
        return value == null || value.isBlank() ? "—" : value.strip();
    }
}
