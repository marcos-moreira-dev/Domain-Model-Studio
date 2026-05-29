package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import java.util.ArrayList;
import java.util.List;

/**
 * Redacta ayuda breve para el SideDock según el foco activo del expediente.
 *
 * <p>La ayuda académica extensa vive en el manual. Esta guía solo orienta el
 * trabajo diario: qué mirar, cómo interpretar el nodo seleccionado y qué revisar
 * antes de cerrar el levantamiento.</p>
 */
final class LogicalBusinessContextualHelpGuide {

    HelpContent forSelection(LogicalBusinessSelection selection, LogicalBusinessItem selectedItem) {
        LogicalBusinessSelection focus = selection == null ? LogicalBusinessSelection.none() : selection;
        HelpContent base = switch (focus.kind()) {
            case ITEM -> forItem(selectedItem);
            case ENTITY -> entityHelp();
            case ATTRIBUTE -> attributeHelp();
            case RELATIONSHIP -> relationshipHelp();
            case PENDING_QUESTION -> pendingQuestionHelp();
            case MATURITY -> maturityHelp();
            case GROUP, SECTION -> sectionHelp();
            case DOCUMENT, NONE -> documentHelp();
        };
        return base.withSection(closingSection());
    }

    private HelpContent documentHelp() {
        return new HelpContent(
                "Guía del expediente",
                "El levantamiento lógico es la fuente lógica canónica del negocio: organiza conversación, reglas, acciones, entidades, preguntas y trazas internas.",
                List.of(
                        section("Lectura recomendada",
                                "Empieza por Estructura para ubicar estados, reglas, acciones y entidades. La ficha central muestra el resultado principal del nodo seleccionado."),
                        section("Qué revisar primero",
                                "Mira Validación para detectar bloqueos, Trazas internas para entender conexiones dentro del documento y Ayuda y glosario para recordar el alcance del módulo."),
                        section("Lenguaje de trabajo",
                                "En la interfaz diaria conviene hablar de negocio: estado inicial, acción transformadora, regla aplicada, verdad protegida, cierre verificable y evidencia.")));
    }

    private HelpContent sectionHelp() {
        return new HelpContent(
                "Guía de sección",
                "Una sección agrupa parte del expediente. No es el resultado final: sirve para navegar y revisar coherencia por bloque.",
                List.of(
                        section("Cómo usarla",
                                "Selecciona los hijos del árbol para revisar elementos concretos. Si una sección aparece vacía, puede ser normal en una plantilla, pero no en un levantamiento maduro."),
                        section("Señales visuales",
                                "Los indicadores del árbol resumen si hay contenido, advertencias, bloqueos o trazas internas que requieren revisión.")));
    }

    private HelpContent forItem(LogicalBusinessItem item) {
        if (item == null) {
            return documentHelp();
        }
        return switch (item.kind()) {
            case ACTION -> actionHelp(item);
            case RULE -> ruleHelp(item);
            case PRECONDITION -> preconditionHelp(item);
            case INVARIANT -> invariantHelp(item);
            case POSTCONDITION -> postconditionHelp(item);
            case USE_CASE, MACRO_FLOW, FLOW -> flowHelp(item);
            case REPORT -> reportHelp(item);
            case CALCULATION -> calculationHelp(item);
            case RISK -> riskHelp(item);
            case SUPPORTING_ASSUMPTION -> suppositionHelp(item);
            case ACTOR -> actorHelp(item);
            case STATE -> stateHelp(item);
            case CONCEPT, EVIDENCE, ENTITY, ATTRIBUTE, RELATIONSHIP, PENDING_QUESTION -> genericItemHelp(item);
        };
    }

    private HelpContent actionHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Una acción transformadora cambia el estado del negocio. Debe explicar cuándo inicia, qué cambia, qué reglas aplica y qué deja como cierre.",
                List.of(
                        section("Debe tener",
                                "Estado inicial, precondiciones, transformación, invariantes protegidas, postcondiciones y evidencia."),
                        section("Señal de alerta",
                                "Una acción sin postcondición no tiene cierre verificable. Una acción sin evidencia es difícil de auditar.")));
    }

    private HelpContent ruleHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Una regla de negocio explica una verdad, restricción o decisión operativa que debe respetarse.",
                List.of(
                        section("Cómo leerla",
                                "Busca su lectura humana, su fuente y qué acciones o entidades afecta. No toda regla tiene forma 'si A entonces B'."),
                        section("Buena práctica",
                                "Una regla fuerte debe poder convertirse después en validación, restricción, prueba o criterio de revisión.")));
    }

    private HelpContent preconditionHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Una precondición indica qué debe ser verdadero antes de ejecutar una acción.",
                List.of(
                        section("Pregunta útil",
                                "¿Qué dato, estado, permiso o evidencia debe existir antes de que esta operación sea válida?"),
                        section("Riesgo típico",
                                "Si falta una precondición, el sistema puede permitir operaciones fuera de orden o sin causa válida.")));
    }

    private HelpContent invariantHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Una invariante es una verdad que debe conservarse mientras el negocio cambia de estado.",
                List.of(
                        section("Cómo leerla",
                                "No es una línea de código. Es una verdad del negocio que protege dinero, inventario, trazabilidad, permisos o consistencia."),
                        section("Forma formal opcional",
                                "Puede tener lectura matemática simple, por ejemplo: para toda orden entregada, su cierre debe quedar evidenciado.")));
    }

    private HelpContent postconditionHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Una postcondición declara qué debe quedar verdadero cuando una acción termina correctamente.",
                List.of(
                        section("Qué buscar",
                                "Estado final, dato actualizado, comprobante, historial, responsable o evidencia que permita verificar el cierre."),
                        section("Riesgo típico",
                                "Si una acción no tiene postcondición, no se sabe cuándo terminó bien.")));
    }

    private HelpContent flowHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Un flujo o caso de uso ordena acciones y decisiones desde el punto de vista operativo.",
                List.of(
                        section("Cómo revisarlo",
                                "Comprueba actor responsable, inicio, pasos principales, alternos, excepciones y cierre."),
                        section("Uso como fuente",
                                "Puede servir como fuente para preparar otros Markdown compatibles con ayuda de una IA, siempre bajo revisión humana, reutilizando IDs y nombres canónicos.")));
    }

    private HelpContent reportHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Un reporte representa una consulta o decisión que el negocio quiere tomar con datos confiables.",
                List.of(
                        section("Qué aclarar",
                                "Quién lo usa, qué pregunta responde, qué filtros necesita y qué datos deben ser confiables."),
                        section("Riesgo típico",
                                "Un reporte sin decisión asociada puede convertirse en pantalla decorativa o deuda de mantenimiento.")));
    }

    private HelpContent calculationHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Un cálculo interno expresa una regla cuantitativa, fórmula o lectura matemática que debe validarse antes de usarla en datos, reportes o restricciones.",
                List.of(
                        section("Qué revisar",
                                "Fórmula, datos necesarios, entidades involucradas, reglas relacionadas y riesgo si se calcula mal."),
                        section("Criterio",
                                "Un cálculo sin validación esperada puede producir saldos, totales o rankings incorrectos.")));
    }

    private HelpContent suppositionHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Un supuesto es una afirmación aún no confirmada que se mantiene visible para no convertir una inferencia en verdad de diseño.",
                List.of(
                        section("Qué revisar",
                                "Fuente, estado, impacto sobre reglas, entidades, decisiones internas y pregunta necesaria para confirmarlo."),
                        section("Criterio",
                                "Si el supuesto afecta dinero, permisos, estados o trazas internas, debe validarse antes de cerrar el levantamiento.")));
    }

    private HelpContent riskHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Un riesgo registra una duda, excepción o condición que puede romper el diseño si se ignora.",
                List.of(
                        section("Cómo usarlo",
                                "Vincúlalo con reglas, acciones, entidades o preguntas pendientes. No lo ocultes si afecta decisiones posteriores.")));
    }

    private HelpContent actorHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Un actor representa una persona, rol, área o sistema que participa en acciones del negocio.",
                List.of(
                        section("Qué revisar",
                                "Responsabilidades, permisos, acciones que ejecuta y decisiones que toma.")));
    }

    private HelpContent stateHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Un estado describe cómo queda un objeto del negocio en un momento concreto.",
                List.of(
                        section("Qué revisar",
                                "Qué acción crea el estado, qué acción lo modifica, qué reglas lo permiten y qué estados son inválidos.")));
    }

    private HelpContent genericItemHelp(LogicalBusinessItem item) {
        return itemHelp(item,
                "Este elemento forma parte del expediente lógico y debe leerse junto a su fuente, referencias y estado de madurez.",
                List.of(section("Qué revisar",
                        "Confirma que tenga lectura humana, evidencia suficiente y referencias útiles hacia otros elementos del levantamiento.")));
    }

    private HelpContent entityHelp() {
        return new HelpContent(
                "Guía de entidad candidata",
                "Una entidad candidata no es todavía una tabla final. Es un concepto del negocio que parece necesitar seguimiento por reglas, acciones, reportes o evidencia.",
                List.of(
                        section("Qué revisar",
                                "Justificación lógica, fuentes, acciones que la crean/modifican/consultan, atributos candidatos y relaciones candidatas."),
                        section("Riesgo típico",
                                "Crear entidades sin fuente lógica genera modelos bonitos pero difíciles de defender ante el cliente.")));
    }

    private HelpContent attributeHelp() {
        return new HelpContent(
                "Guía de atributo candidato",
                "Un atributo candidato es un dato que debe recordarse, validarse, calcularse, auditarse o reportarse.",
                List.of(
                        section("Qué revisar",
                                "Razón de existencia, tipo tentativo, si es calculado, fórmula, fuente y riesgo si se modela mal.")));
    }

    private HelpContent relationshipHelp() {
        return new HelpContent(
                "Guía de relación candidata",
                "Una relación candidata explica por qué dos entidades del negocio deben conectarse.",
                List.of(
                        section("Qué revisar",
                                "Origen, destino, nombre, cardinalidad tentativa, justificación y fuente de la relación.")));
    }

    private HelpContent pendingQuestionHelp() {
        return new HelpContent(
                "Guía de pregunta pendiente",
                "Una pregunta pendiente es deuda de análisis visible. Puede bloquear reglas, entidades, flujos o decisiones internas.",
                List.of(
                        section("Qué revisar",
                                "Prioridad, estado, qué elementos afecta y qué decisión humana falta para cerrarla."),
                        section("Criterio",
                                "Si afecta dinero, permisos, estados, auditoría o cierre operativo, debe resolverse o marcarse antes de cerrar el levantamiento.")));
    }

    private HelpContent maturityHelp() {
        return new HelpContent(
                "Guía de madurez",
                "La madurez resume si el levantamiento está inicial, parcial, consistente, usable como fuente o validado.",
                List.of(
                        section("Cómo leerla",
                                "Las fortalezas indican qué ya tiene base; los bloqueos indican qué impide cerrar; los siguientes pasos marcan la ruta de revisión."),
                        section("Cierre",
                                "Un levantamiento usable como fuente todavía no es necesariamente validado: la validación con cliente o responsable debe quedar registrada.")));
    }

    private HelpContent itemHelp(LogicalBusinessItem item, String intro, List<HelpSection> sections) {
        String title = LogicalBusinessStatusFormatter.itemKind(item.kind()) + " · " + item.id();
        return new HelpContent("Guía del elemento", title + ": " + intro, sections);
    }

    private HelpSection closingSection() {
        return section("Cierre documental",
                "Antes de cerrar el levantamiento, revisa que no haya bloqueos graves, que las preguntas críticas estén resueltas o marcadas, que las fuentes sean claras, que el Markdown exportado conserve IDs canónicos y que cualquier uso posterior como fuente mantenga revisión humana, nombres canónicos y responsabilidad del usuario y la IA.");
    }

    private static HelpSection section(String title, String body) {
        return new HelpSection(title, body);
    }

    record HelpContent(String focusTitle, String intro, List<HelpSection> sections) {
        HelpContent withSection(HelpSection section) {
            List<HelpSection> updated = new ArrayList<>(sections);
            updated.add(section);
            return new HelpContent(focusTitle, intro, List.copyOf(updated));
        }
    }

    record HelpSection(String title, String body) {
    }
}
