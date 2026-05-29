# Gramática IA — Despliegue técnico

Estado: gramática Markdown importable.  
Importable por la app: sí.  
Salida visual implementada: sí.  
Uso recomendado: representar ambientes, nodos técnicos y conexiones de despliegue.

```md
---
dms_version: "1"
diagram_type: "technical-deployment"
name: "Despliegue técnico — <sistema>"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Ambientes

- Desarrollo local
- Piloto interno
- Producción

# Nodos

- Equipo de usuario: ejecuta cliente o navegador.
- Servidor de aplicación: aloja servicios principales.
- Base de datos PostgreSQL: conserva datos persistentes.

# Conexiones

- Equipo de usuario -> Servidor de aplicación: HTTPS.
- Servidor de aplicación -> Base de datos PostgreSQL: conexión privada.
```

## Reglas

- Mantener separados ambientes, nodos y conexiones.
- No incluir credenciales ni secretos.
- Especificar si un nodo es local, piloto o producción cuando sea importante.
