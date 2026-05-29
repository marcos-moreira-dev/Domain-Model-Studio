# Smoke MSI — Tanda 39

## Objetivo

Validar que el instalador MSI generado por `jpackage` instala, abre y desinstala Domain Model Studio sin depender de rutas de desarrollo.

## Precondiciones obligatorias

Antes de ejecutar esta guía debe existir evidencia de:

```bat
scripts\02-ejecutar-tests.bat
scripts\13-revalidacion-local-completa.bat
scripts\14-app-image-completa.bat
```

La app-image debe abrir correctamente antes de generar el MSI. Si la app-image no abre, no se debe generar ni probar el MSI.

## Comandos de generación

```bat
scripts\04-generar-instalador-msi.bat
```

O flujo completo:

```bat
scripts\15-msi-completo.bat
```

Salida esperada:

```txt
dist\installer\*.msi
dist\installer\MSI_MANIFEST.txt
dist\logs\jpackage-msi.log
```

## Smoke manual

1. Abrir `dist\installer`.
2. Ejecutar el archivo `.msi` generado.
3. Instalar en una ruta normal del sistema o en una ruta elegida con el asistente.
4. Abrir Domain Model Studio desde el acceso directo o menú de Windows.
5. Verificar pantalla inicial.
6. Importar un ejemplo oficial Markdown.
7. Guardar un proyecto `.dms` en una carpeta de usuario.
8. Cerrar la app instalada.
9. Reabrir la app instalada.
10. Abrir el `.dms` guardado.
11. Exportar Markdown.
12. Exportar SVG vectorial documental en un diagrama visual.
13. Abrir Recursos IA y verificar que se copian a una carpeta de usuario.
14. Cerrar la app.
15. Desinstalar desde Configuración de Windows o Panel de control.
16. Confirmar que la desinstalación no requiere carpetas del repositorio.

## Criterios de aceptación

- El MSI se genera en `dist\installer`.
- El instalador no depende de `target`, `src` ni rutas de desarrollo.
- La app instalada abre.
- Los recursos embebidos cargan.
- Los ejemplos oficiales importan.
- Guardar y reabrir `.dms` funciona.
- Exportar Markdown y SVG documental funciona.
- Recursos IA se copian desde la app instalada.
- La desinstalación finaliza sin errores.

## Criterio de no avance

No avanzar a Tanda 40 si ocurre cualquiera de estos casos:

- No se genera el MSI.
- El MSI no instala.
- La app instalada no abre.
- Faltan recursos o plantillas IA.
- No se puede guardar o reabrir `.dms`.
- No se puede exportar un entregable básico.
- No se puede desinstalar.
