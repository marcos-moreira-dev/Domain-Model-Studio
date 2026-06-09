/**
 * Ensamblaje de dependencias de Domain Model Studio.
 *
 * <p>El paquete bootstrap conecta servicios concretos de infraestructura,
 * aplicación y presentación para que la app arranque con un grafo de dependencias
 * explícito. No debería contener reglas de negocio ni lógica visual compleja.</p>
 *
 * <p>Ruta de estudio JD-7: leer bootstrap al final para comprobar cómo se unen las capas ya estudiadas. Si una dependencia parece cruzar capas de forma rara,
 * revisar primero el contrato de la capa correspondiente antes de modificar este
 * paquete.</p>
 * <p>Ruta JD-8: en recorridos por casos de uso completos, bootstrap se revisa
 * al final para verificar qué implementación concreta conecta cada contrato
 * de aplicación, infraestructura y presentación.</p>
 * <p>Ruta JD-9: bootstrap se lee como punto de ensamblaje para comprobar que una decisión arquitectónica no terminó escondida como acoplamiento accidental entre capas.</p>
 */
package com.marcosmoreira.domainmodelstudio.bootstrap;
