# Script de prueba de conexión
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Probando Conexión a la API" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar si el puerto está escuchando
Write-Host "1. Verificando puerto 9090..." -ForegroundColor Yellow
$port = netstat -ano | findstr :9090
if ($port) {
    Write-Host "   ✓ Puerto 9090 está activo" -ForegroundColor Green
} else {
    Write-Host "   ✗ Puerto 9090 no está activo" -ForegroundColor Red
    exit
}

Write-Host ""
Write-Host "2. Probando endpoint de clientes..." -ForegroundColor Yellow

try {
    $response = Invoke-WebRequest -Uri "http://localhost:9090/api/clientes" -Method GET -UseBasicParsing -ErrorAction Stop
    Write-Host "   ✓ Conexión exitosa!" -ForegroundColor Green
    Write-Host "   Status Code: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "   Respuesta: $($response.Content)" -ForegroundColor Gray
} catch {
    Write-Host "   ✗ Error al conectar" -ForegroundColor Red
    Write-Host "   Mensaje: $($_.Exception.Message)" -ForegroundColor Red
    
    # Intentar con otros endpoints
    Write-Host ""
    Write-Host "3. Probando otros endpoints..." -ForegroundColor Yellow
    
    $endpoints = @(
        "http://localhost:9090/actuator/health",
        "http://localhost:9090/api/mesas",
        "http://localhost:9090/api/reservas"
    )
    
    foreach ($endpoint in $endpoints) {
        try {
            $test = Invoke-WebRequest -Uri $endpoint -Method GET -UseBasicParsing -ErrorAction Stop
            Write-Host "   ✓ $endpoint - OK" -ForegroundColor Green
        } catch {
            Write-Host "   ✗ $endpoint - Error" -ForegroundColor Red
        }
    }
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Prueba completada" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan


