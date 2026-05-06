# ============================================================
# build.ps1
# Fuehrt mvn clean install aus und kopiert die fertige JAR
# aus target/ auf die Root-Ebene des Projekts.
#
# Verwendung: .\build.ps1
# ============================================================

$PROJEKTDIR = $PSScriptRoot
$POMDIR     = $PROJEKTDIR
$TARGETJAR  = "$PROJEKTDIR\target\gro-pro-main-1.0-SNAPSHOT.jar"
$DESTJAR    = "$PROJEKTDIR\gro-pro-main.jar"

# --- Maven pruefen ---
$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvn) {
    Write-Host "[FEHLER] mvn nicht gefunden. Bitte Maven installieren und zum PATH hinzufuegen." -ForegroundColor Red
    exit 1
}

# --- Build ---
Write-Host ">> mvn clean install" -ForegroundColor Cyan
Push-Location $POMDIR
mvn clean install
$code = $LASTEXITCODE
Pop-Location

if ($code -ne 0) {
    Write-Host "[FEHLER] Build fehlgeschlagen (Exit-Code: $code)." -ForegroundColor Red
    exit $code
}

# --- JAR kopieren ---
if (-not (Test-Path $TARGETJAR)) {
    # Fallback: erste JAR in target suchen
    $found = Get-ChildItem "$PROJEKTDIR\target" -Filter "*.jar" -ErrorAction SilentlyContinue |
             Where-Object { $_.Name -notlike "*sources*" -and $_.Name -notlike "*javadoc*" } |
             Select-Object -First 1
    if ($found) { $TARGETJAR = $found.FullName }
    else {
        Write-Host "[FEHLER] Keine JAR in target\ gefunden." -ForegroundColor Red
        exit 1
    }
}

Copy-Item $TARGETJAR $DESTJAR -Force
Write-Host "[OK] JAR kopiert nach: $DESTJAR" -ForegroundColor Green
