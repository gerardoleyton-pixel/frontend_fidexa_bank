# start-frontend.ps1 - instala deps si faltan y arranca nodemon
$ErrorActionPreference = 'Stop'

Write-Host "Iniciando frontend..."

if (-not (Test-Path "node_modules")) {
    Write-Host "node_modules no encontrado. Ejecutando 'npm install'..."
    npm install
}

Write-Host "Arrancando servidor (npm run dev)..."
npm run dev
