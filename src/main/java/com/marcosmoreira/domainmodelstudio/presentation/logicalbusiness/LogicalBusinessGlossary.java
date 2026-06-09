package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import java.util.List;

/**
 * Glosario operativo obligatorio del Levantamiento lógico.
 *
 * <p>Este contenido acompaña la ayuda contextual del SideDock. No reemplaza la
 * guía académica del menú Ayuda: fija, en la operación diaria, abreviaciones,
 * límites del módulo, uso responsable con IA y errores comunes.</p>
 */
final class LogicalBusinessGlossary {

    private LogicalBusinessGlossary() {
    }

    static List<GlossarySection> glossarySections() {
        return List.of(
                quickRead(),
                abbreviations(),
                concepts(),
                limits(),
                aiUsage(),
                commonMistakes());
    }

    private static GlossarySection quickRead() {
        return section("Lectura rápida del módulo",
                "El levantamiento lógico es una fuente lógica canónica del negocio: fija IDs, nombres, reglas, acciones, estados, entidades candidatas y preguntas pendientes para revisar coherencia interna.",
                "logical-business-help-boundary",
                entry("Expediente lógico", "Documento estructurado; no es canvas visual, bloc de notas libre ni modelo físico de base de datos."),
                entry("Coherencia interna", "La validación revisa consistencia del expediente activo; no aprueba automáticamente el negocio real."),
                entry("Fuente revisable", "El documento puede usarse como fuente para otros trabajos, siempre con revisión humana."));
    }

    private static GlossarySection abbreviations() {
        return section("Glosario de abreviaciones",
                "Prefijos canónicos usados por el contrato Markdown logical-business-master-v1.",
                "logical-business-help-glossary",
                entry("MF", "Macroflujo del negocio."),
                entry("FL", "Flujo operativo dentro de un macroflujo."),
                entry("CU", "Caso de uso observable del negocio."),
                entry("ACC", "Acción transformadora que cambia el estado del negocio."),
                entry("RN", "Regla de negocio."),
                entry("PRE", "Precondición que debe cumplirse antes de una acción."),
                entry("INV", "Invariante que protege una verdad durante el cambio."),
                entry("POST", "Postcondición o cierre verificable de una acción."),
                entry("ACT", "Actor, rol, área o sistema participante."),
                entry("CON", "Concepto estable del dominio."),
                entry("EVID", "Evidencia que sustenta una afirmación."),
                entry("SUP", "Supuesto aún no confirmado."),
                entry("ENT", "Entidad candidata del negocio."),
                entry("ATR", "Atributo candidato."),
                entry("REL", "Relación candidata."),
                entry("EST", "Estado observable del negocio."),
                entry("REP", "Reporte, consulta o salida de información."),
                entry("CALC", "Cálculo, fórmula o lectura cuantitativa."),
                entry("RISK", "Riesgo o punto de inconsistencia posible."),
                entry("PEND", "Pregunta pendiente para el cliente o responsable."));
    }

    private static GlossarySection concepts() {
        return section("Glosario conceptual",
                "Conceptos mínimos para leer el expediente sin confundirlo con otros tipos de proyecto.",
                "logical-business-help-glossary",
                entry("Fuente lógica canónica", "Base estable de IDs, nombres y reglas que puede orientar otros artefactos revisables."),
                entry("Acción transformadora", "Operación que parte de un estado, aplica reglas y deja un cierre verificable."),
                entry("Entidad candidata", "Concepto del negocio que parece requerir seguimiento por reglas, acciones, reportes o evidencia; no es tabla final."),
                entry("Traza interna", "Relación entre elementos del mismo levantamiento para explicar origen, impacto o dependencia; no sincroniza proyectos externos."),
                entry("Validación interna", "Revisión de consistencia del expediente activo; no aprueba automáticamente el negocio real."),
                entry("Madurez documental", "Lectura sobre preparación del expediente para revisión o uso como fuente; no sustituye validación humana."),
                entry("Cierre documental", "Estado en que bloqueos, preguntas críticas, fuentes e IDs canónicos fueron revisados explícitamente."));
    }

    private static GlossarySection limits() {
        return section("Límites del módulo",
                "Reglas de alcance que evitan falsas promesas en la operación diaria.",
                "logical-business-help-boundary",
                entry("No genera todo", "No produce automáticamente todos los diagramas, documentos, matrices ni código del sistema."),
                entry("No sincroniza proyectos", "No existe sincronización automática entre proyectos independientes; la alineación semántica la mantiene el usuario y la IA."),
                entry("No valida otros tipos", "El levantamiento valida coherencia interna; UML, C4, diccionario, wireframes o BPMN conservan su propio alcance y validación."),
                entry("No cierra dudas por magia", "Una pregunta pendiente crítica debe resolverse, aplazarse con razón o quedar marcada como bloqueo."));
    }

    private static GlossarySection aiUsage() {
        return section("Uso con IA",
                "La IA puede ayudar a preparar Markdown compatibles, pero debe respetar el contrato semántico del levantamiento.",
                "logical-business-help-ai",
                entry("Reutilizar IDs", "Los Markdown preparados con IA deben reutilizar IDs y nombres canónicos cuando correspondan."),
                entry("Respetar gramáticas", "Cada artefacto destino debe cumplir su propio diagram_type, parser, secciones y validaciones."),
                entry("Revisar borradores", "Todo Markdown producido con IA es revisable; el usuario decide qué artefactos necesita y qué acepta."),
                entry("Conservar dudas", "La IA no debe ocultar PEND, SUP o RISK: debe mantener visibles las incertidumbres relevantes."));
    }

    private static GlossarySection commonMistakes() {
        return section("Errores comunes",
                "Señales de uso incorrecto que deben corregirse antes de cerrar o reutilizar el expediente.",
                "logical-business-help-warning",
                entry("Empezar por tablas", "Las tablas son una decisión posterior; primero deben entenderse reglas, acciones, estados y evidencia."),
                entry("Ocultar preguntas", "Una duda crítica escondida se convierte después en deuda de diseño o implementación."),
                entry("Convertir supuestos en reglas", "Un SUP no confirmado no debe tratarse como RN validada."),
                entry("Tratar candidatas como finales", "ENT, ATR y REL son candidatos lógicos; no obligan a crear tablas, columnas o claves definitivas."),
                entry("Pedir todo demasiado pronto", "No es obligatorio generar todos los artefactos; el usuario decide qué necesita para el caso."),
                entry("Cambiar nombres sin razón", "Renombrar IDs o nombres canónicos entre artefactos rompe la alineación semántica."));
    }

    private static GlossarySection section(String title, String intro, String styleClass, GlossaryEntry... entries) {
        return new GlossarySection(title, intro, List.of(entries), styleClass);
    }

    private static GlossaryEntry entry(String term, String description) {
        return new GlossaryEntry(term, description);
    }

    record GlossarySection(String title, String intro, List<GlossaryEntry> entries, String styleClass) {
    }

    record GlossaryEntry(String term, String description) {
    }
}
