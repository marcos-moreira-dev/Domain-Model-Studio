package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.util.Locale;

/** Clasifica secciones y líneas específicas de UML Secuencia sin inflar el parser general. */
final class BehaviorSequenceMarkdownClassifier {

    private BehaviorSequenceMarkdownClassifier() {
    }

    static BehaviorNodeKind nodeKindFor(String section, String item) {
        String normalizedSection = normalize(section);
        String normalizedItem = normalize(item);
        if (isParticipantSection(normalizedSection)) {
            return BehaviorNodeKind.PARTICIPANT;
        }
        if (isActivationSection(normalizedSection)) {
            return BehaviorNodeKind.ACTIVATION;
        }
        if (isNoteSection(normalizedSection)) {
            return BehaviorNodeKind.NOTE;
        }
        if (isFragmentSection(normalizedSection) || isExplicitFragmentItem(normalizedItem)) {
            return BehaviorNodeKind.FRAGMENT;
        }
        if (startsWithKeyword(normalizedItem, "activacion") || startsWithKeyword(normalizedItem, "activación")) {
            return BehaviorNodeKind.ACTIVATION;
        }
        if (startsWithKeyword(normalizedItem, "nota")) {
            return BehaviorNodeKind.NOTE;
        }
        return BehaviorNodeKind.PARTICIPANT;
    }

    private static boolean isParticipantSection(String normalizedSection) {
        return normalizedSection.contains("participante")
                || normalizedSection.contains("linea de vida")
                || normalizedSection.contains("lifeline");
    }

    private static boolean isActivationSection(String normalizedSection) {
        return normalizedSection.contains("activacion") || normalizedSection.contains("activación");
    }

    private static boolean isNoteSection(String normalizedSection) {
        return normalizedSection.contains("nota")
                || normalizedSection.contains("observacion")
                || normalizedSection.contains("observación");
    }

    private static boolean isFragmentSection(String normalizedSection) {
        return normalizedSection.contains("fragmento") || normalizedSection.contains("combined fragment");
    }

    private static boolean isExplicitFragmentItem(String normalizedItem) {
        return startsWithKeyword(normalizedItem, "fragmento")
                || startsWithKeyword(normalizedItem, "fragment")
                || startsWithKeyword(normalizedItem, "alt")
                || startsWithKeyword(normalizedItem, "opt")
                || startsWithKeyword(normalizedItem, "loop")
                || startsWithKeyword(normalizedItem, "par")
                || startsWithKeyword(normalizedItem, "break")
                || startsWithKeyword(normalizedItem, "critical")
                || startsWithKeyword(normalizedItem, "critico")
                || startsWithKeyword(normalizedItem, "crítico")
                || startsWithKeyword(normalizedItem, "ref")
                || startsWithKeyword(normalizedItem, "referencia");
    }

    private static boolean startsWithKeyword(String value, String keyword) {
        String normalizedValue = normalize(value);
        String normalizedKeyword = normalize(keyword);
        if (normalizedValue.equals(normalizedKeyword)) {
            return true;
        }
        return normalizedValue.startsWith(normalizedKeyword + " ")
                || normalizedValue.startsWith(normalizedKeyword + ":")
                || normalizedValue.startsWith(normalizedKeyword + "=")
                || normalizedValue.startsWith(normalizedKeyword + "|")
                || normalizedValue.startsWith(normalizedKeyword + "[");
    }

    private static String normalize(String text) {
        return java.text.Normalizer.normalize(text == null ? "" : text, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .strip();
    }
}
