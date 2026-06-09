package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.util.List;
import java.util.Objects;

/** Catálogo de ayuda operativa por tipo/familia real de workspace. */
final class OperationalHelpCatalog {

    private OperationalHelpCatalog() {
    }

    static OperationalHelpProfile profile(WorkspaceKind kind, String title, String subtitle) {
        Objects.requireNonNull(kind, "kind");
        return switch (kind) {
            case DATA_DICTIONARY_DOCUMENT -> dataDictionary(title, subtitle);
            case LOGICAL_BUSINESS_DOCUMENT -> logicalBusiness(title, subtitle);
            case ROLES_PERMISSIONS_MATRIX -> rolesPermissions(title, subtitle);
            case WIREFRAME_DIAGRAM -> wireframes(title, subtitle);
            case MODULE_MAP_DIAGRAM -> moduleMap(title, subtitle);
            case UML_CLASS_DIAGRAM -> umlClass(title, subtitle);
            case SCREEN_FLOW_DIAGRAM -> screenFlow(title, subtitle);
            case ARCHITECTURE_DIAGRAM -> architecture(title, subtitle);
            case BEHAVIOR_DIAGRAM -> behavior(title, subtitle);
            case FREE_GRAPH_DIAGRAM -> freeGraph(title, subtitle);
            case LOGICAL_BUSINESS_GRAPH_DIAGRAM -> logicalBusinessGraph(title, subtitle);
            default -> generic(title, subtitle);
        };
    }

    private static OperationalHelpProfile dataDictionary(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Revisa entidades, campos, nombres técnicos, restricciones y descripciones.",
                        "Edita metadatos del documento cuando prepares una entrega para cliente.",
                        "Usa la exportación PDF/Markdown solo después de revisar entidades y campos."),
                OperationalHelpSection.of("Límites del módulo",
                        "No es un ERD ni un modelo físico de base de datos.",
                        "No representa relaciones visuales; documenta el vocabulario de datos."),
                OperationalHelpSection.of("IA y revisión",
                        "Los recursos IA pueden generar un primer Markdown estructurado.",
                        "Reimporta solo documentos que respeten la plantilla de diccionario de datos."));
    }

    private static OperationalHelpProfile logicalBusiness(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Navega reglas, precondiciones, invariantes, acciones, entidades y preguntas pendientes.",
                        "Corrige el expediente importado antes de usarlo como fuente revisable.",
                        "Usa validación y trazas internas para detectar vacíos o referencias pendientes."),
                OperationalHelpSection.of("Artefactos compatibles",
                        "El levantamiento puede servir como fuente para otros Markdown, pero la selección, generación y revisión corresponden al usuario y a la IA.",
                        "Exporta el Markdown canónico del levantamiento y úsalo como insumo cuando decidas construir otros artefactos compatibles."),
                OperationalHelpSection.of("Criterio de uso",
                        "Este módulo es la fuente lógica; otros artefactos pueden reutilizar sus IDs, nombres y reglas bajo revisión del usuario y la IA.",
                        "La guía académica explica teoría; esta ayuda explica cómo operar la herramienta."));
    }

    private static OperationalHelpProfile rolesPermissions(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Revisa roles, permisos y asignaciones desde la matriz central.",
                        "Usa las pestañas laterales para localizar roles, permisos y decisiones específicas.",
                        "Edita condiciones o alcances antes de exportar la matriz."),
                OperationalHelpSection.of("Límites del módulo",
                        "No conviertas esta matriz en un grafo; su lectura principal es rol contra permiso.",
                        "Para matrices grandes, revisa por módulos o bloques de permisos."),
                OperationalHelpSection.of("Exportación",
                        "SVG/PNG funcionan como salida visual documental.",
                        "Markdown conserva una estructura revisable e importable."));
    }

    private static OperationalHelpProfile wireframes(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Selecciona una pantalla o componente antes de editar propiedades.",
                        "Mantén los wireframes como maquetas estructurales de campos, botones, tablas y estados.",
                        "Usa la lámina general para revisar composición y navegación administrativa."),
                OperationalHelpSection.of("Alcance conservador",
                        "No es Figma, no es prototipado interactivo y no promete diseño visual final.",
                        "Prioriza claridad de estructura sobre decoración."),
                OperationalHelpSection.of("Exportación",
                        "Exporta como vista documental general cuando necesites respaldar pantallas.",
                        "La exportación por pantalla individual queda reservada para una mejora simple posterior."));
    }

    private static OperationalHelpProfile moduleMap(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Revisa módulos, submódulos y dependencias como arquitectura funcional de alto nivel.",
                        "Usa la pestaña Dependencias para auditar acoplamientos entre módulos.",
                        "Edita responsabilidades, estado y observaciones antes de exportar."),
                OperationalHelpSection.of("Lectura del diagrama",
                        "Cada caja representa un módulo o submódulo operativo.",
                        "Cada conector representa uso, dependencia o relación funcional."));
    }

    private static OperationalHelpProfile umlClass(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Revisa clases, interfaces, atributos, métodos y relaciones estructurales.",
                        "Usa filtros y estructura lateral para no saturar diagramas importados desde código.",
                        "Exporta vistas revisadas, no necesariamente el universo completo del código."),
                OperationalHelpSection.of("Criterio técnico",
                        "La vista activa puede ser un subconjunto filtrado para documentación.",
                        "El .dms conserva el proyecto editable completo."));
    }

    private static OperationalHelpProfile screenFlow(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Revisa pantallas, rutas y transiciones de navegación administrativa.",
                        "Selecciona pantallas o conectores desde el lienzo o estructura lateral.",
                        "Agrupa rutas por módulo cuando el flujo crezca."),
                OperationalHelpSection.of("Lectura del flujo",
                        "Una pantalla representa una vista o formulario del sistema.",
                        "Una transición representa navegación, acción o cambio de estado de interfaz."));
    }

    private static OperationalHelpProfile architecture(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("C4 contexto",
                        "Revisa personas, sistemas externos y el sistema principal.",
                        "Comprueba que las relaciones expliquen quién usa o intercambia información."),
                OperationalHelpSection.of("C4 contenedores",
                        "Documenta aplicaciones, servicios, bases de datos y responsabilidades principales.",
                        "Evita prometer nivel de componente si el diagrama solo muestra contenedores."),
                OperationalHelpSection.of("Despliegue técnico",
                        "Registra nodos de ejecución, entornos, servicios y dependencias de infraestructura.",
                        "Usa relaciones para explicar comunicación técnica, no decoración."),
                OperationalHelpSection.of("Exportación",
                        "SVG es vectorial documental; no promete copia exacta WYSIWYG del canvas."));
    }

    private static OperationalHelpProfile behavior(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("BPMN básico y flujo operativo",
                        "Usa inicio, actividades, decisiones, eventos finales y flujos claros.",
                        "Mantén el alcance básico: documentación de proceso, no BPMN industrial completo."),
                OperationalHelpSection.of("UML actividad",
                        "Revisa acciones, decisiones y continuidad del procedimiento.",
                        "Comprueba que cada bifurcación tenga sentido operativo."),
                OperationalHelpSection.of("UML secuencia",
                        "Verifica participantes, mensajes y orden temporal.",
                        "Un mensaje ilegible o fuera de orden daña la lectura del diagrama."),
                OperationalHelpSection.of("UML estados",
                        "Revisa estados, transiciones, eventos y condiciones de cambio.",
                        "Las transiciones son tan importantes como los estados."),
                OperationalHelpSection.of("UML casos de uso",
                        "Comprueba actores, casos de uso y asociaciones funcionales.",
                        "Usa include/extend solo cuando esté justificado por la historia funcional."));
    }

    private static OperationalHelpProfile freeGraph(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Crea nodos y relaciones libres para explorar dependencias o asociaciones.",
                        "Usa nombres cortos y conectores claros para evitar que el grafo se vuelva ambiguo.",
                        "Ordena el grafo antes de exportar."),
                OperationalHelpSection.of("Límites del módulo",
                        "No sustituye UML, BPMN, C4 ni modelo conceptual.",
                        "Úsalo cuando la relación no encaje todavía en una notación formal."));
    }


    private static OperationalHelpProfile logicalBusinessGraph(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Selecciona nodos o relaciones desde el lienzo o desde Estructura.",
                        "Edita título, descripción, estado y referencias desde Propiedades.",
                        "Mueve nodos en el canvas común para ordenar macroflujos, microflujos, casos de uso y elementos lógicos."),
                OperationalHelpSection.of("Leyenda y códigos",
                        "La pestaña Leyenda explica MF, FL, CU, ACC, RN, PRE, INV, POST, ENT, EST, REP, RISK y PEND.",
                        "Usa códigos estables para mantener trazabilidad con el Levantamiento lógico del negocio."),
                OperationalHelpSection.of("Relaciones y validación",
                        "Revisa relaciones como contiene, usa, ejecuta, requiere, protege, garantiza, crea, consulta y bloquea.",
                        "La validación lateral avisa cuando una relación no es semánticamente esperada."),
                OperationalHelpSection.of("Alcance de herramienta",
                        "Este proyecto es una vista visual compatible con la lógica del negocio, no un grafo libre genérico.",
                        "La guía académica explica los conceptos; esta ayuda explica cómo operar el proyecto."));
    }

    private static OperationalHelpProfile generic(String title, String subtitle) {
        return profile(title, subtitle,
                OperationalHelpSection.of("Uso operativo",
                        "Revisa el artefacto activo desde su vista principal.",
                        "Usa los módulos laterales disponibles según el tipo de proyecto.",
                        "Guarda o exporta cuando el contenido esté revisado."),
                OperationalHelpSection.of("Capas de ayuda",
                        "La guía académica explica teoría y notación.",
                        "Esta ayuda explica uso de herramienta y operación del módulo."));
    }

    private static OperationalHelpProfile profile(
            String title,
            String subtitle,
            OperationalHelpSection first,
            OperationalHelpSection... rest
    ) {
        List<OperationalHelpSection> sections = new java.util.ArrayList<>();
        sections.add(first);
        sections.addAll(List.of(rest));
        return new OperationalHelpProfile(title, subtitle, sections);
    }
}
