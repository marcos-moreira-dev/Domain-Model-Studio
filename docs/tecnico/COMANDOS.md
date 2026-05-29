# Comandos operativos

Desde la raíz del proyecto, la superficie pública vigente de scripts es pequeña y orientada a uso real.

## Uso diario

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
scripts\01-ejecutar-app.bat
```

## Revalidación y cierre local

```bat
scripts\13-revalidacion-local-completa.bat
```

Este flujo ejecuta verificación de entorno, tests, smoke render automático y métricas de refactor. La revisión visual/manual sigue documentada en `docs\testeo`.

## Empaquetado Windows

```bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

`14` genera y verifica la app-image con manifiesto SHA-256, `15` genera MSI después de app-image validada y `16` orquesta la validación completa de release candidate local copiando logs a `dist\release\logs`.

## Mantenimiento

```bat
scripts\06-medir-refactor.bat
scripts\31-generar-javadoc.bat
```

Los detalles de implementación viven en `scripts\internal\`. No se ejecutan directamente salvo diagnóstico técnico.
