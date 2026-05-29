# Tanda JD-5 — Sitio JavaDoc y guardarraíl de cobertura

## Objetivo

Cerrar la parte operativa del JavaDoc: generación del sitio, guía de navegación y cobertura mínima gradual para tipos públicos.

## Alcance

No se modificó lógica funcional. La tanda trabaja sobre:

```txt
pom.xml
scripts/31-generar-javadoc.bat
scripts\02-ejecutar-tests.bat
docs/desarrollo/JAVADOC_SITIO_GUIA.md
docs/calidad/AUDITORIA_JAVADOC_JD5.md
src/test/java/.../Jd5JavadocSiteCoverageSourceTest.java
```

## Decisiones

### 1. El sitio JavaDoc es material de estudio

El sitio generado en `target/site/apidocs/index.html` sirve para estudiar contratos técnicos por capas. No reemplaza docs de arquitectura, matrices de producto ni reportes de smoke.

### 2. Cobertura gradual

Se exige:

```txt
Tipos públicos documentados >= 95%.
```

No se exige todavía:

```txt
100% de métodos públicos.
```

La decisión evita comentarios obvios y mantiene JavaDoc como explicación de intención, contrato e invariantes.

### 3. Paquetes antes que clases sueltas

La guía recomienda leer primero los paquetes y después entrar a clases concretas. Esto protege la lectura arquitectónica por capas.

## Validación

Script focalizado:

```bat
scripts\02-ejecutar-tests.bat
```

Validación manual esperada:

```txt
1. Abrir `target\site\apidocs\index.html`.
2. Confirmar título Domain Model Studio JavaDoc.
3. Usar Search para ubicar clases críticas.
4. Navegar paquetes de dominio, aplicación, infraestructura y presentación.
```

## Resultado esperado

La tanda queda cerrada cuando el guardarraíl pasa y el sitio se genera sin errores de JavaDoc.
