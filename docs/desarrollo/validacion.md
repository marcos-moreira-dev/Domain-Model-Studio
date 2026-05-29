# Guía de validación

## Objetivo

Esta guía define cómo validar Domain Model Studio antes de considerar estable una tanda o cierre local.

## Validación rápida en Windows

Desde la raíz del proyecto:

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
scripts\01-ejecutar-app.bat
```

## Revalidación local completa

```bat
scripts\13-revalidacion-local-completa.bat
```

Este flujo automatiza lo que sí puede automatizarse localmente: entorno, tests, smoke render automático y métricas. La revisión visual sigue siendo humana.

## Requisitos esperados

```txt
JDK: 21, recomendado Eclipse Temurin 21
Maven: 3.9+
JavaFX: gestionado vía Maven
Sistema objetivo: Windows desktop
```

## Validación manual mínima

- Abrir la aplicación.
- Abrir ejemplos oficiales.
- Cambiar entre tabs.
- Verificar que el SideDock cambie según el tab activo.
- Verificar el modelo conceptual como zona protegida/canon visual.
- Abrir Grafo libre y confirmar etiquetas de relaciones.
- Abrir UML Clases y confirmar selección, arrastre y jerarquías visuales.
- Exportar Markdown, SVG y PNG en diagramas visuales que lo soporten.
- Guardar y reabrir un proyecto `.dms`.
- Exportar diccionario a Markdown/PDF si aplica.
- Revisar que las acciones de código fuente solo estén activas cuando haya metadata real de archivo fuente.

## Empaquetado

Validar app-image antes de MSI y RC:

```bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

## Lenguaje visible

Buscar que la interfaz no muestre términos como:

```txt
tanda
demo
placeholder
mock
stub
GPT
ChatGPT
adapter
renderer
infraestructura interna
implementación pendiente
```

La UI debe hablar de:

```txt
diagramas
modelos
vistas
elementos
relaciones
documentos
matrices
exportaciones
ayuda
propiedades
estructura
```

## Exportaciones

Validar:

- no PNG vacío;
- no SVG sin contenido;
- no texto gris ilegible;
- no recortes;
- no espacio blanco absurdo;
- no handles/selección en exportación limpia;
- exportación del tab activo correcto.

## Qué hacer si falla Maven

- Copiar el bloque de error completo.
- Corregir primero errores de compilación.
- No avanzar a nuevas tandas funcionales si compila roto.
- Si el error pertenece a una tanda cerrada, documentar el hotfix correspondiente.
