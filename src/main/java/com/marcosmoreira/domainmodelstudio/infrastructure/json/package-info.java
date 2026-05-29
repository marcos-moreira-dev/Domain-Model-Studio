/**
 * Serialización y deserialización del formato de proyecto {@code .dms}.
 *
 * <p>El JSON {@code .dms} conserva el estado durable de un proyecto: metadatos,
 * documentos especializados, layout visual, estilos, assets y rutas de origen. La
 * regla de esta capa es roundtrip estricto: guardar y abrir no debe cambiar la
 * intención del proyecto ni degradar documentos especializados como el Grafo lógico.</p>
 *
 * <p>Desde la Tanda 31, los readers/writers principales son coordinadores de formato.
 * La lectura del modelo conceptual y de los payloads especializados se delega a piezas
 * más pequeñas, pero no se cambian claves JSON, no se introducen migraciones y no se
 * altera el contrato de compatibilidad de archivos existentes.</p>
 */
package com.marcosmoreira.domainmodelstudio.infrastructure.json;
