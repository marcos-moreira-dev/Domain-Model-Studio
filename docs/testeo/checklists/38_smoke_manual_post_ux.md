# Checklist corto — Smoke manual post-UX Tanda 38

Usar este checklist cuando no se necesite el plan detallado completo.

## Previo

- [ ] Parches 28–37 aplicados en orden.
- [ ] `scripts\00-verificar-entorno.bat` OK.
- [ ] `scripts\02-ejecutar-tests.bat` OK.
- [ ] `scripts\01-ejecutar-app.bat` OK.

## UX transversal

- [ ] F11 entra/sale de pantalla completa.
- [ ] `Ayuda → Guía académica` visible y abre con F1.
- [ ] Ayuda operativa sigue en SideDock.
- [ ] SideDock sin doble scroll innecesario.
- [ ] ListView/TableView con mínimo visual de 8 filas, sin filas falsas.
- [ ] Hover negro/texto blanco en botones comunes.
- [ ] No-radius ornamental aplicado sin romper geometría semántica.

## Tipos críticos

- [ ] Grafo lógico UENS importa y muestra leyenda/estructura clara.
- [ ] Levantamiento lógico UENS importa como documento estructurado.
- [ ] Arquitectura/despliegue: `BOUNDARY`, `ENVIRONMENT`, `NETWORK` contienen/mueven hijos.
- [ ] Diccionario se ve como documento técnico, no canvas.
- [ ] Roles/permisos usa `ADMIN` y `SECRETARIA` como roles implementados.
- [ ] UML Estados usa ciclo de `reporte_solicitud_queue`.
- [ ] Wireframes no se comportan como Figma ni usan controles reales.

## Exportación

- [ ] SVG con header documental y sin `<image>` como sustituto principal.
- [ ] PNG con header sin solapar contenido.
- [ ] PDF de diccionario OK.
- [ ] Markdown exportable/importable OK.
- [ ] `.dms` guarda/reabre OK.
- [ ] Batch export crea `input/`, `editable/`, `output/`.

## Cierre

- [ ] Reporte completado.
- [ ] Bloqueadores documentados.
- [ ] Si no hay bloqueadores, pasar a Tanda 39.
