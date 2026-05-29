# Checklist corto — Tanda 39 App-image/MSI/RC post-UX

## Previo

- [ ] Parches 28–38 aplicados.
- [ ] `scripts\00-verificar-entorno.bat` OK.
- [ ] `scripts\02-ejecutar-tests.bat` OK.
- [ ] `scripts\13-revalidacion-local-completa.bat` OK o justificación formal.
- [ ] Smoke manual Tanda 38 sin bloqueadores críticos.

## App-image

- [ ] `scripts\14-app-image-completa.bat` OK.
- [ ] `dist\staging\APP_IMAGE_MANIFEST.txt` existe.
- [ ] `Domain Model Studio.exe` abre desde `dist\staging`.
- [ ] CSS/recursos IA/ayudas cargan.
- [ ] F11 funciona.
- [ ] Guía académica abre.
- [ ] Ayuda operativa del SideDock sigue disponible.
- [ ] Importa ejemplos UENS principales.
- [ ] Guarda/reabre `.dms`.
- [ ] Exporta Markdown/SVG/PNG/PDF según tipo.

## MSI

- [ ] `scripts\15-msi-completo.bat` OK.
- [ ] `dist\installer\*.msi` existe.
- [ ] `dist\installer\MSI_MANIFEST.txt` existe.
- [ ] MSI instala.
- [ ] App instalada abre.
- [ ] App instalada conserva recursos IA/CSS/ayudas.
- [ ] App instalada guarda/reabre `.dms`.
- [ ] App instalada exporta entregables básicos.
- [ ] MSI desinstala.

## Release Candidate

- [ ] `scripts\16-release-candidate.bat` OK.
- [ ] `dist\release\RELEASE_CANDIDATE_MANIFEST.txt` existe.
- [ ] Reporte Tanda 39 completado.
- [ ] Limitaciones conocidas revisadas.
- [ ] RC aprobado o rechazado explícitamente.
