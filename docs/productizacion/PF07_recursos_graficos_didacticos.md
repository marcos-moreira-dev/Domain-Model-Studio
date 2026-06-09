# PF07 — Guía de recursos gráficos didácticos para la micro-Wikipedia

Estado: **planificación ejecutada**  
Alcance: **figuras, iconos y diagramas educativos que acompañarán la teoría**

## 1. Decisión visual

Las figuras del centro de ayuda deben ser **herramientas didácticas**, no decoración. Su propósito es fijar la notación en la mente del usuario.

El estilo recomendado es:

- vectorial o pseudo-vectorial;
- fondo transparente o blanco;
- líneas limpias;
- pocas sombras;
- sin saturación visual;
- textos breves dentro de las figuras;
- consistencia entre todos los tipos de diagrama.

## 2. Formato recomendado

| Uso | Formato recomendado | Razón |
|---|---|---|
| Símbolos simples | SVG | Escala bien y permite edición. |
| Ilustraciones generadas | PNG/WebP revisado | Útil si se genera con IA y luego se normaliza. |
| Diagramas completos | SVG o PNG exportado | Deben ser legibles en ventana de ayuda. |
| Iconos de navegación | PNG/SVG | Ya existe carpeta de iconos. |

Preferencia: si el símbolo puede dibujarse con formas simples, usar SVG en vez de imagen raster.

## 3. Convención de nombres

```text
src/main/resources/help/figures/
  conceptual-entidad.svg
  conceptual-atributo.svg
  conceptual-relacion.svg
  conceptual-cardinalidad.svg
  bpmn-evento-inicio.svg
  bpmn-tarea.svg
  bpmn-gateway-exclusivo.svg
  uml-actor.svg
  uml-caso-uso.svg
  uml-clase-basica.svg
  uml-secuencia-linea-vida.svg
  uml-estado-transicion.svg
  c4-persona.svg
  c4-sistema.svg
  c4-contenedor.svg
  despliegue-nodo.svg
  wireframe-login.svg
  wireframe-crud-listado.svg
```

No usar nombres genéricos como `imagen1.png`, `figura_final.png` o `diagrama_nuevo.png`.

## 4. Figuras mínimas por tipo

### Modelo conceptual

- entidad;
- atributo simple;
- atributo compuesto;
- atributo multivaluado;
- atributo derivado;
- relación;
- cardinalidad 1:N;
- comparación Chen vs pata de gallo.

### Diccionario de datos

- tabla documental de campos;
- campo obligatorio/opcional;
- regla de negocio;
- catálogo de valores.

### BPMN básico

- evento de inicio;
- tarea;
- gateway exclusivo;
- gateway paralelo;
- evento final;
- pool/lane;
- flujo completo simple.

### Flujo operativo

- inicio;
- paso operativo;
- decisión;
- documento/dato;
- responsable;
- flujo alternativo.

### UML Casos de uso

- actor;
- caso de uso;
- límite del sistema;
- asociación;
- include;
- extend;
- generalización.

### UML Clases

- clase con compartimentos;
- atributo y método;
- asociación con multiplicidad;
- herencia;
- interfaz;
- agregación;
- composición;
- paquete/módulo.

### UML Actividad

- nodo inicial;
- acción;
- decisión;
- merge;
- fork/join;
- swimlane;
- nodo final.

### UML Secuencia

- participante;
- línea de vida;
- mensaje síncrono;
- mensaje asíncrono;
- retorno;
- activación;
- fragmento `alt`;
- fragmento `loop`.

### UML Estados

- estado;
- estado inicial;
- estado final;
- transición;
- evento/guarda/acción;
- estado compuesto.

### C4 Contexto

- persona;
- sistema bajo estudio;
- sistema externo;
- relación rotulada;
- límite de organización.

### C4 Contenedores

- contenedor aplicación;
- contenedor backend/API;
- contenedor base de datos;
- relación con protocolo/tecnología;
- límite del sistema.

### Despliegue técnico

- nodo servidor;
- PC cliente;
- base de datos;
- servicio externo;
- conexión de red;
- entorno local/pruebas/producción.

### Mapa de módulos

- módulo;
- submódulo;
- dependencia funcional;
- módulo transversal;
- agrupador por área.

### Roles y permisos

- rol;
- permiso;
- recurso;
- restricción;
- matriz simple rol-permiso.

### Flujo de pantallas

- pantalla;
- acción de navegación;
- modal;
- retorno;
- flujo alternativo.

### Wireframes administrativos

- login;
- dashboard;
- listado CRUD;
- formulario;
- detalle;
- reportes;
- roles/permisos;
- modal de confirmación.

## 5. Texto alternativo obligatorio

Cada figura debe registrarse con texto alternativo. Ejemplo:

```text
id: uml-actor
archivo: help/figures/uml-actor.svg
titulo: Actor UML
alt: Figura de palitos que representa un rol externo que interactúa con el sistema.
```

Esto mejora accesibilidad y también ayuda a mantener trazabilidad conceptual.

## 6. Cómo usar imágenes generadas por IA

Las imágenes generadas pueden servir como primera versión, pero no deben entrar sin curaduría.

Proceso recomendado:

1. generar imagen didáctica simple;
2. revisar que la notación no contradiga la teoría;
3. eliminar rótulos innecesarios;
4. normalizar proporciones y estilo;
5. convertir a SVG si el símbolo es simple;
6. guardar con nombre semántico;
7. registrar en el índice de figuras;
8. asociar a una ficha concreta.

## 7. Figuras que no se deben usar

Evitar:

- capturas borrosas;
- dibujos muy artísticos que oculten la notación;
- diagramas con demasiados elementos;
- figuras sin texto alternativo;
- imágenes con rótulos que contradigan la jerga del producto;
- símbolos inventados que parezcan estándares cuando no lo son;
- mezcla de notaciones sin explicación.

## 8. Mini estilo visual sugerido

La micro-Wikipedia puede usar una línea visual sobria:

```text
Borde: gris oscuro o azul grisáceo
Relleno: blanco o gris muy claro
Texto: negro/gris oscuro
Acento: un color único para resaltar el elemento explicado
Tipografía: limpia y legible
```

No hace falta que las figuras sean bonitas; deben ser claras.

## 9. Criterio de cierre gráfico

Una ficha teórica se considera visualmente suficiente cuando:

- tiene por lo menos una figura para su símbolo central;
- los conceptos complejos tienen figura propia;
- los casos especiales más importantes están ilustrados;
- las figuras tienen texto alternativo;
- el estilo no se contradice con las demás fichas;
- la figura ayuda a leer mejor la teoría.
