package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/** Contrato canónico del Markdown madre de Levantamiento lógico. */
final class LogicalBusinessCanonicalMarkdownContract {

    static final String CONTRACT_ID = "logical-business-master-v1";
    static final String CANONICAL_RESOURCE = "ai-resources/official-markdown/levantamiento-logico/logical_business_intake_template.md";
    static final String PUBLIC_TEMPLATE = "examples/markdown/plantillas/logical_business_intake.md";

    private static final Pattern CANONICAL_ID = Pattern.compile(
            "^(RN|PRE|INV|POST|ACC|CU|MF|FL|ENT|ATR|REL|REP|RISK|PEND|ACT|EST|CON|EVID|SUP|CALC)-\\d{3,}$",
            Pattern.CASE_INSENSITIVE);

    private static final Set<String> DOCUMENT_STATES = Set.of(
            "borrador", "validado parcialmente", "validado", "archivado");
    private static final Set<String> ITEM_STATES = Set.of(
            "borrador", "pendiente", "pendiente de validar", "validada", "validado", "descartada", "descartado",
            "revisar", "validado parcialmente", "confirmada", "confirmado", "bloqueante", "derivable",
            "usable como fuente", "fuente revisable", "source ready");

    private static final List<CanonicalSection> SECTIONS = List.of(
            section(0, "Portada lógica del levantamiento"),
            section(1, "Principio lógico central"),
            section(2, "Contexto observado del negocio"),
            section(3, "Sistema de estados del negocio"),
            section(4, "Vocabulario lógico del dominio"),
            section(5, "Predicados, proposiciones y símbolos permitidos"),
            section(6, "Reglas lógicas del negocio"),
            section(7, "Condiciones iniciales / precondiciones"),
            section(8, "Invariantes del negocio"),
            section(9, "Condiciones de cierre / postcondiciones"),
            section(10, "Acciones transformadoras"),
            section(11, "Árbol operativo de macroflujos, flujos y casos de uso"),
            section(12, "Catálogo único de casos de uso"),
            section(13, "Grafo lógico del negocio"),
            section(14, "Entidades candidatas"),
            section(15, "Estados y transiciones"),
            section(16, "Reportes y algoritmos internos"),
            section(17, "Indicadores para diseño de base de datos"),
            section(18, "Riesgos de inconsistencia"),
            section(19, "Uso como fuente para otros artefactos"),
            section(20, "Preguntas pendientes para el cliente"),
            section(21, "Nivel de madurez del levantamiento"),
            section(22, "Cierre del documento"));

    private static final Map<String, Integer> PREFERRED_SECTION_BY_PREFIX = Map.ofEntries(
            Map.entry("RN", 6), Map.entry("PRE", 7), Map.entry("INV", 8), Map.entry("POST", 9),
            Map.entry("ACC", 10), Map.entry("MF", 11), Map.entry("FL", 11), Map.entry("CU", 12),
            Map.entry("ACT", 2), Map.entry("CON", 4), Map.entry("EVID", 2), Map.entry("SUP", 2),
            Map.entry("ENT", 14), Map.entry("ATR", 14), Map.entry("REL", 14), Map.entry("EST", 15),
            Map.entry("REP", 16), Map.entry("CALC", 16), Map.entry("RISK", 18), Map.entry("PEND", 20));

    private LogicalBusinessCanonicalMarkdownContract() {
    }

    static List<CanonicalSection> sections() {
        return SECTIONS;
    }

    static Optional<CanonicalSection> sectionByNumber(int number) {
        return SECTIONS.stream().filter(section -> section.number() == number).findFirst();
    }

    static Optional<CanonicalSection> preferredSectionForId(String id) {
        String prefix = prefixOf(id);
        Integer sectionNumber = PREFERRED_SECTION_BY_PREFIX.get(prefix);
        return sectionNumber == null ? Optional.empty() : sectionByNumber(sectionNumber);
    }

    static boolean isCanonicalId(String value) {
        return value != null && CANONICAL_ID.matcher(value.strip()).matches();
    }

    static boolean isPlaceholderId(String value) {
        return value != null && value.strip().toUpperCase(Locale.ROOT).endsWith("-XXX");
    }

    static boolean isDocumentState(String value) {
        return DOCUMENT_STATES.contains(normalize(value));
    }

    static boolean isItemState(String value) {
        return ITEM_STATES.contains(normalize(value));
    }

    private static CanonicalSection section(int number, String title) {
        return new CanonicalSection(number, title);
    }

    private static String prefixOf(String id) {
        String normalized = id == null ? "" : id.strip().toUpperCase(Locale.ROOT);
        int dash = normalized.indexOf('-');
        return dash < 0 ? normalized : normalized.substring(0, dash);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
    }

    record CanonicalSection(int number, String title) {
        String heading() {
            return number + ". " + title;
        }

        String id() {
            return "sec-" + number;
        }
    }
}
