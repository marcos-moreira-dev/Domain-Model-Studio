# PF09 — Matriz de smoke test visual por tipo

Estado: **planificado**  
Tipo: **checklist manual futuro**

## 1. Uso de esta matriz

Esta matriz debe convertirse en un checklist manual de cierre. No reemplaza pruebas automáticas, pero cubre lo que un test unitario difícilmente ve: que el usuario abra un tipo de diagrama y realmente vea lo que la app promete.

## 2. Checklist común

Para cada tipo visible:

```text
[ ] Crear proyecto nuevo del tipo.
[ ] Verificar título de pestaña correcto.
[ ] Verificar workspace real, no pantalla de inicio equivocada.
[ ] Verificar toolbar específica del tipo.
[ ] Verificar barra superior fija sin cambiar innecesariamente.
[ ] Agregar elemento principal.
[ ] Agregar conexión/relación si aplica.
[ ] Seleccionar elemento.
[ ] Ver panel izquierdo contextual.
[ ] Ver panel derecho contextual.
[ ] Guardar .dms.
[ ] Cerrar y abrir .dms.
[ ] Exportar formato prometido.
[ ] Abrir ayuda teórica del tipo.
[ ] Abrir ejemplo oficial del tipo.
[ ] Cambiar a otra pestaña y volver.
[ ] Confirmar que no se perdió selección ni estado básico.
```

## 3. Casos especiales por tipo

### 3.1 Modelo conceptual

```text
[ ] Crear entidad.
[ ] Crear atributo.
[ ] Crear relación.
[ ] Ver cardinalidad.
[ ] Cambiar vista Chen/pata de gallo si aplica.
[ ] Exportar SVG/PNG/Markdown según promesa real.
```

### 3.2 Diccionario de datos

```text
[ ] Crear entidad documental.
[ ] Crear campo.
[ ] Editar tipo, obligatoriedad y validación.
[ ] Exportar PDF/Markdown.
[ ] Confirmar que no exige lienzo visual si la salida principal es documental.
```

### 3.3 BPMN básico

```text
[ ] Crear evento de inicio.
[ ] Crear actividad.
[ ] Crear gateway.
[ ] Crear evento de fin.
[ ] Conectar flujo.
[ ] Exportar PNG/Markdown.
```

### 3.4 Flujo operativo

```text
[ ] Crear paso operativo.
[ ] Crear responsable.
[ ] Crear decisión simple si aplica.
[ ] Conectar pasos.
[ ] Confirmar lenguaje menos formal que BPMN.
```

### 3.5 UML Casos de uso

```text
[ ] Crear actor.
[ ] Crear caso de uso.
[ ] Crear límite de sistema si aplica.
[ ] Crear asociación.
[ ] Crear include/extend si aplica.
[ ] Confirmar que no aparece pantalla de inicio equivocada.
```

### 3.6 UML Clases

```text
[ ] Crear clase.
[ ] Agregar atributo.
[ ] Agregar método.
[ ] Crear relación.
[ ] Crear agrupador por módulo/paquete si aplica.
```

### 3.7 UML Actividad

```text
[ ] Crear nodo inicial.
[ ] Crear acción.
[ ] Crear decisión.
[ ] Crear nodo final.
[ ] Conectar flujo.
```

### 3.8 UML Secuencia

```text
[ ] Crear participante.
[ ] Crear línea de vida.
[ ] Crear mensaje.
[ ] Ver orden temporal vertical.
[ ] Exportar sin romper alineación.
```

### 3.9 UML Estados

```text
[ ] Crear estado inicial.
[ ] Crear estado normal.
[ ] Crear transición.
[ ] Crear estado final.
[ ] Editar evento/condición de transición si aplica.
```

### 3.10 Mapa de módulos

```text
[ ] Crear módulo.
[ ] Crear submódulo.
[ ] Crear dependencia.
[ ] Ver responsabilidades en propiedades.
```

### 3.11 Roles y permisos

```text
[ ] Crear rol.
[ ] Crear permiso/acción.
[ ] Asociar rol con permiso.
[ ] Confirmar que el diagrama no se vuelve tabla pura si promete salida visual.
```

### 3.12 Flujo de pantallas

```text
[ ] Crear pantalla.
[ ] Crear transición.
[ ] Etiquetar acción de navegación.
[ ] Confirmar flujo legible de inicio a destino.
```

### 3.13 Wireframes administrativos

```text
[ ] Crear pantalla.
[ ] Agregar panel.
[ ] Agregar botón.
[ ] Agregar campo.
[ ] Agregar tabla/listado.
[ ] Confirmar que funciona como maqueta, no como diseñador gráfico complejo.
```

### 3.14 C4 Contexto

```text
[ ] Crear persona/actor.
[ ] Crear sistema.
[ ] Crear sistema externo.
[ ] Crear relación.
[ ] Confirmar nivel de abstracción alto.
```

### 3.15 C4 Contenedores

```text
[ ] Crear aplicación/desktop/web.
[ ] Crear backend/API.
[ ] Crear base de datos.
[ ] Crear servicio externo.
[ ] Crear relación.
```

### 3.16 Despliegue técnico

```text
[ ] Crear nodo físico/lógico.
[ ] Crear servicio.
[ ] Crear base de datos.
[ ] Crear enlace/red.
[ ] Confirmar que representa operación/despliegue, no clases de código.
```

## 4. Criterio de aprobación manual

Un tipo pasa smoke visual si:

```text
crea proyecto + monta workspace correcto + permite operación mínima + guarda/abre + exporta lo prometido + ayuda/ejemplo disponibles
```

Si falla una de esas piezas, el tipo no debe considerarse cerrado.
