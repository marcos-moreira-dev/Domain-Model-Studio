# Tanda 31 — Refactor controlado de persistencia `.dms`

## Objetivo

Modularizar el lector y writer principal del formato `.dms` sin cambiar el contrato JSON ni el comportamiento visible de apertura/guardado.

## Criterio aplicado

La persistencia es zona de riesgo medio-alto. Por eso esta tanda no cambia nombres de campos, no introduce migraciones y no toca payloads especializados. Solo separa responsabilidades ya existentes para que el coordinador principal del formato deje de conocer detalles del modelo conceptual legacy y de cada documento especializado opcional.

## Cambios realizados

- `DmsProjectJsonReader` queda como coordinador de formato:
  - valida `formatVersion`;
  - lee metadatos;
  - coordina modelo, payloads especializados, layout, estilos, vista y assets;
  - valida consistencia final.
- `DmsProjectJsonWriter` queda como coordinador de formato:
  - valida consistencia;
  - escribe cabecera, proyecto, modelo, layouts, estilos, vista y assets;
  - delega el bloque `model`.
- Nuevo `DmsProjectConceptualModelJsonReader`:
  - lee entidades, atributos y relaciones del modelo conceptual legacy.
- Nuevo `DmsProjectConceptualModelJsonWriter`:
  - escribe el bloque `model`, documentos especializados opcionales y modelo conceptual legacy.
- Nuevo `DmsProjectSpecializedPayload`:
  - agrupa los documentos especializados opcionales leídos desde `model`.
- Nuevo `DmsProjectSpecializedPayloadReader`:
  - concentra la decisión de qué payload especializado opcional existe dentro del JSON.

## Contrato preservado

No cambian las claves JSON persistentes:

- `formatVersion`
- `project`
- `model`
- `entities`
- `relationships`
- `layouts`
- `styles`
- `view`
- `assets`

Tampoco cambia el formato de documentos especializados como diccionario, mapa de módulos, UML Clases, Roles/Permisos, Flujo de pantallas, Wireframes, Comportamiento, Arquitectura, Grafo libre, Levantamiento lógico o Grafo lógico.

## Tests

Se agrega `DmsProjectPersistenceSectionRefactorSourceTest` y se actualiza `Tanda19PersistenceExportRefactorSourceTest` para validar que:

- los readers/writers principales son coordinadores;
- el modelo conceptual legacy tiene reader/writer propio;
- los payloads especializados opcionales se agrupan explícitamente;
- las claves persistentes principales siguen presentes;
- no se mezclan de nuevo secciones especializadas dentro del coordinador principal.

## Validación esperada local

```bat
scripts\02-ejecutar-tests.bat
```
