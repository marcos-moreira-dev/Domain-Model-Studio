# Ejemplos inválidos

Estos archivos existen para pruebas futuras del parser Markdown.

No deben importarse correctamente. Cada uno representa un error que la aplicación debe detectar y reportar con mensaje humano.

Casos incluidos:

- `entidad_inexistente.md`: relación apunta a una entidad no declarada.
- `cardinalidad_invalida.md`: cardinalidad no pertenece a la taxonomía permitida.
- `frontmatter_incompleto.md`: faltan metadatos obligatorios.
- `tag_desconocido.md`: atributo usa tag no permitido.
