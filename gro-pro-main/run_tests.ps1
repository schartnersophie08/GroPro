# ============================================================
# run_tests.ps1
# Fuehrt die JAR fuer alle Dateien im Ordner testfaelle/ aus.
# Voraussetzung: gro-pro-main.jar muss bereits gebaut vorliegen.
#                JDK mind. 21 muss installiert sein.
# ============================================================

$PROJEKTDIR = $PSScriptRoot
$PARENTDIR  = Split-Path $PROJEKTDIR -Parent
$JAR        = "$PROJEKTDIR\gro-pro-main.jar"
$TESTDIR    = "$PROJEKTDIR\testf" + [char]0x00E4 + "lle"

# --- JAR pruefen ---
if (-not (Test-Path $JAR)) {
    Write-Host "[FEHLER] gro-pro-main.jar nicht gefunden." -ForegroundColor Red
    Write-Host "         Bitte zuerst das Projekt bauen: mvn package" -ForegroundColor Yellow
    exit 1
}

# --- Java automatisch ermitteln ---
function Find-Java {
    if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\java.exe")) { return "$env:JAVA_HOME\bin\java.exe" }
    $j = Get-Command java -ErrorAction SilentlyContinue
    if ($j) { return $j.Source }
    $found = Get-ChildItem "$env:USERPROFILE\.jdks" -Directory -ErrorAction SilentlyContinue |
             Sort-Object Name -Descending |
             ForEach-Object { "$($_.FullName)\bin\java.exe" } |
             Where-Object { Test-Path $_ } |
             Select-Object -First 1
    if ($found) { return $found }
    $ver = (Get-ItemProperty "HKLM:\SOFTWARE\JavaSoft\JDK" -ErrorAction SilentlyContinue).CurrentVersion
    if ($ver) {
        $h = (Get-ItemProperty "HKLM:\SOFTWARE\JavaSoft\JDK\$ver" -ErrorAction SilentlyContinue).JavaHome
        if ($h -and (Test-Path "$h\bin\java.exe")) { return "$h\bin\java.exe" }
    }
    return $null
}

$JAVA = Find-Java
if (-not $JAVA) {
    Write-Host "[FEHLER] Java nicht gefunden. Bitte JDK installieren und JAVA_HOME setzen." -ForegroundColor Red
    exit 1
}

# --- Testfaelle ausfuehren ---
$dateien = Get-ChildItem -Path $TESTDIR -File -ErrorAction SilentlyContinue
if (-not $dateien) {
    Write-Host "[WARNUNG] Keine Dateien in '$TESTDIR' gefunden." -ForegroundColor Yellow
    exit 0
}

Write-Host "Starte $($dateien.Count) Testfall/-faelle..." -ForegroundColor Cyan

Push-Location $PARENTDIR
foreach ($datei in $dateien) {
    $pfad = "gro-pro-main\testf" + [char]0x00E4 + "lle\$($datei.Name)"
    Write-Host ""
    Write-Host ">> $pfad" -ForegroundColor Yellow
    & $JAVA -jar $JAR $pfad
    if ($LASTEXITCODE -eq 0) { Write-Host "   [OK]" -ForegroundColor Green }
    else                      { Write-Host "   [FEHLER] Exit-Code: $LASTEXITCODE" -ForegroundColor Red }
}
Pop-Location

Write-Host ""
Write-Host "Fertig." -ForegroundColor Cyan
