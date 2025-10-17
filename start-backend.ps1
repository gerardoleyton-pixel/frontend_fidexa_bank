# start-backend.ps1 - intenta ejecutar mvn si está en PATH, y da instrucciones si no
$ErrorActionPreference = 'Stop'

Write-Host "Iniciando script de arranque del backend..."

function Has-Command($name) {
    try {
        Get-Command $name -ErrorAction Stop | Out-Null
        return $true
    } catch {
        return $false
    }
}

if (Has-Command mvn) {
    Write-Host "Se encontró 'mvn'. Ejecutando: mvn -DskipTests spring-boot:run"
    mvn -DskipTests spring-boot:run
} else {
    Write-Host "No se encontró 'mvn' en PATH. Opciones:" -ForegroundColor Yellow
    Write-Host "  1) Instalar Maven (recomendado)."
    Write-Host "  2) Pedir al desarrollador que agregue 'mvnw' (Maven Wrapper) al repositorio para ejecutar sin instalar Maven."
    Write-Host "Si deseas que lo haga yo, responde en la conversación 'añade mvnw' y lo agrego al repo."
}
