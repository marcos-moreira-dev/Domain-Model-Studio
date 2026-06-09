param(
    [Parameter(Mandatory = $true)]
    [string]$ArtifactPath,

    [Parameter(Mandatory = $true)]
    [string]$ManifestPath,

    [Parameter(Mandatory = $true)]
    [string]$Label
)

$ErrorActionPreference = 'Stop'

$item = Get-Item -LiteralPath $ArtifactPath
$hash = Get-FileHash -LiteralPath $item.FullName -Algorithm SHA256
$manifest = Resolve-Path -LiteralPath $ManifestPath

$lines = @(
    '',
    "[$Label]",
    "${Label}_PATH=$($item.FullName)",
    "${Label}_BYTES=$($item.Length)",
    "${Label}_SHA256=$($hash.Hash)",
    "${Label}_LAST_WRITE_UTC=$($item.LastWriteTimeUtc.ToString('o'))"
)

Add-Content -LiteralPath $manifest -Encoding UTF8 -Value $lines
