param(
    [string]$AppName = "AgroERP",
    [string]$AppVersion = "0.0.1",
    [string]$Vendor = "Agro ERP"
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$maven = Join-Path $repoRoot ".tools\apache-maven-3.9.9\bin\mvn.cmd"
$wixRoot = Join-Path $repoRoot ".tools\wix"
$wixPackage = Join-Path $wixRoot "wix.3.11.2.nupkg"
$wixZip = Join-Path $wixRoot "wix.3.11.2.zip"
$wixExtract = Join-Path $wixRoot "pkg"
$wixTools = Join-Path $wixExtract "tools"
$jarName = "agro-business-erp-0.0.1-SNAPSHOT.jar"
$packageInputDir = Join-Path $repoRoot "target\jpackage-input"
$distDir = Join-Path $repoRoot "dist"
$installerDir = Join-Path $repoRoot "installer"

if (-not (Test-Path -LiteralPath $maven)) {
    throw "Bundled Maven was not found at $maven"
}

Push-Location $repoRoot
try {
    & $maven -DskipTests package

    if (-not (Test-Path -LiteralPath (Join-Path $wixTools "candle.exe"))) {
        New-Item -ItemType Directory -Path $wixRoot -Force | Out-Null
        Invoke-WebRequest -Uri "https://www.nuget.org/api/v2/package/WiX/3.11.2" -OutFile $wixPackage
        Copy-Item -LiteralPath $wixPackage -Destination $wixZip -Force
        if (Test-Path -LiteralPath $wixExtract) {
            Remove-Item -LiteralPath $wixExtract -Recurse -Force
        }
        Expand-Archive -LiteralPath $wixZip -DestinationPath $wixExtract -Force
    }

    if (Test-Path -LiteralPath $distDir) {
        Remove-Item -LiteralPath $distDir -Recurse -Force
    }
    if (Test-Path -LiteralPath $installerDir) {
        Remove-Item -LiteralPath $installerDir -Recurse -Force
    }
    if (Test-Path -LiteralPath $packageInputDir) {
        Remove-Item -LiteralPath $packageInputDir -Recurse -Force
    }

    New-Item -ItemType Directory -Path $distDir, $installerDir, $packageInputDir -Force | Out-Null
    Copy-Item -LiteralPath (Join-Path $repoRoot "target\$jarName") -Destination (Join-Path $packageInputDir $jarName)
    $env:PATH = "$wixTools;$env:PATH"

    jpackage `
        --type app-image `
        --name $AppName `
        --app-version $AppVersion `
        --vendor $Vendor `
        --input target\jpackage-input `
        --main-jar $jarName `
        --dest dist `
        --win-console

    jpackage `
        --type exe `
        --name $AppName `
        --app-version $AppVersion `
        --vendor $Vendor `
        --input target\jpackage-input `
        --main-jar $jarName `
        --dest installer `
        --win-console `
        --win-menu `
        --win-shortcut
}
finally {
    Pop-Location
}
