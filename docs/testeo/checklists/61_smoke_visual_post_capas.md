# Checklist — Tanda 61 Smoke visual post-capas

## Base

- [ ] `scripts\02-ejecutar-tests.bat` termina en `BUILD SUCCESS`.
- [ ] La aplicación abre con `scripts\01-ejecutar-app.bat`.
- [ ] Se validan ejemplos oficiales o proyectos representativos.

## Canvas y capas

- [ ] Las tarjetas/nodos operables quedan por encima de relaciones cuando corresponde.
- [ ] Las relaciones no bloquean el drag de tarjetas/nodos.
- [ ] Los labels, bendpoints, resize handles y overlays quedan encima de nodos y relaciones.
- [ ] Los contenedores semánticos de arquitectura/despliegue quedan como fondo visual.
- [ ] Los contenedores handle-only se arrastran solo desde título/handle.

## Orden visual T59

- [ ] **Traer al frente** cambia el orden visual del elemento seleccionado.
- [ ] **Enviar al fondo** cambia el orden visual del elemento seleccionado.
- [ ] **Subir una capa** cambia el orden visual incrementalmente.
- [ ] **Bajar una capa** cambia el orden visual incrementalmente.
- [ ] Las acciones no aparecen en documentos o matrices donde no aplican.

## Persistencia y salida

- [ ] Guardar/reabrir `.dms` conserva el orden visual.
- [ ] PNG respeta el orden visual en tipos que lo soportan.
- [ ] SVG especializado conserva la política documental sin tapar elementos críticos.
- [ ] Batch/export no se ve afectado por la política de capas.

## Tipos mínimos

- [ ] Flujo de pantallas.
- [ ] Mapa de módulos.
- [ ] Arquitectura/C4/despliegue.
- [ ] UML Clases.
- [ ] Comportamiento/BPMN/UML visual.
- [ ] Wireframes administrativos.
- [ ] Grafo libre.
- [ ] Grafo lógico del negocio.

## Resultado

- [ ] Aprobado sin observaciones.
- [ ] Aprobado con observaciones no bloqueantes.
- [ ] Rechazado por bloqueador.
