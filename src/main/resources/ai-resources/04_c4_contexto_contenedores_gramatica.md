# Gramática IA — C4 Contexto y Contenedores

Estado: gramática Markdown importable.  
Importable por la app: sí.  
Salida visual implementada: sí, para C4 Contexto y C4 Contenedores.  
Uso recomendado: generar diagramas C4 desde una conversación con IA y corregirlos mínimamente en la app.

## C4 Contexto

```md
# C4 Contexto: <nombre del sistema>

## Sistema principal
- Nombre: <sistema>
- Propósito: <para qué existe>
- Cliente/organización: <cliente>

## Personas
### Persona: <nombre>
- Rol: <rol de negocio>
- Necesidad: <qué espera del sistema>

## Sistemas externos
### Sistema externo: <nombre>
- Propósito: <qué aporta>
- Tipo de integración: manual | API | archivo | base de datos | no aplica

## Relaciones
- <Persona/Sistema> -> <Sistema principal>: <motivo de la relación>
```

## C4 Contenedores

```md
# C4 Contenedores: <nombre del sistema>

## Contenedores
### Contenedor: <nombre>
- Tipo: desktop | backend | base de datos | servicio externo | worker | reporte
- Tecnología sugerida: <JavaFX, Spring Boot, PostgreSQL, etc.>
- Responsabilidad: <una responsabilidad principal>

## Relaciones
- <Contenedor A> -> <Contenedor B>: <protocolo/dato/responsabilidad>
```

## Reglas

- No mezclar C4 con diagrama de clases.
- C4 Contexto muestra el sistema desde afuera.
- C4 Contenedores muestra partes ejecutables o almacenamientos principales.
- Para apps autocontenidas, indicar si backend/base de datos son locales o remotos.
