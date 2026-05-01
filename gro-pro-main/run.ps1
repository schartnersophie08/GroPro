# ============================================================
# run.ps1
# Fuehrt die JAR mit einer einzelnen Eingabedatei aus.
#
# Verwendung: .\run.ps1 <dateiname>
# Beispiel:   .\run.ps1 testfaelle\Test
# ============================================================

param(
    [Parameter(Mandatory = $true, HelpMessage = "Pfad zur Eingabedatei")]
    [string]$Datei
)

$PROJEKTDIR = $PSScriptRoot
$PARENTDIR  = Split-Path $PROJEKTDIR -Parent
$JAR        = "$PROJEKTDIR\gro-pro-main.jar"

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

# --- Eingabedatei als absoluten Pfad aufloesen ---
# Wird ein relativer Pfad uebergeben, wird er relativ zum Parent-Verzeichnis aufgeloest,
# damit gro-pro-main/output korrekt geschrieben wird.
if ([System.IO.Path]::IsPathRooted($Datei)) {
    $eingabe = $Datei
} else {
    $eingabe = "gro-pro-main\$Datei"
}

# --- Ausfuehren ---
Write-Host ">> $eingabe" -ForegroundColor Yellow
Push-Location $PARENTDIR
& $JAVA -jar $JAR $eingabe
$code = $LASTEXITCODE
Pop-Location

if ($code -eq 0) { Write-Host "[OK]" -ForegroundColor Green }
else             { Write-Host "[FEHLER] Exit-Code: $code" -ForegroundColor Red }
