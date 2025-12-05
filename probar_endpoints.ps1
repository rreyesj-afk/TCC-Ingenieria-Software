# Script para probar todos los endpoints de la API
# Ejecutar: powershell -ExecutionPolicy Bypass -File probar_endpoints.ps1

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "PRUEBA DE ENDPOINTS - Sistema de Reservas" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:9090"

# Función para hacer peticiones
function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Body = $null,
        [string]$Description
    )
    
    Write-Host "`n[$Method] $Description" -ForegroundColor Yellow
    Write-Host "URL: $Url" -ForegroundColor Gray
    
    try {
        $params = @{
            Uri = $Url
            Method = $Method
            UseBasicParsing = $true
            ErrorAction = "Stop"
        }
        
        if ($Body) {
            $params.ContentType = "application/json"
            $params.Body = $Body
        }
        
        $response = Invoke-WebRequest @params
        Write-Host "✓ Status: $($response.StatusCode)" -ForegroundColor Green
        Write-Host "Respuesta:" -ForegroundColor Gray
        Write-Host $response.Content -ForegroundColor White
        return $true
    } catch {
        Write-Host "✗ Error: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            $statusCode = [int]$_.Exception.Response.StatusCode
            Write-Host "  Status Code: $statusCode" -ForegroundColor Red
        }
        return $false
    }
}

# Verificar que la aplicación esté corriendo
Write-Host "1. Verificando que la aplicación esté corriendo..." -ForegroundColor Cyan
try {
    $test = Invoke-WebRequest -Uri "$baseUrl/api/clientes" -UseBasicParsing -ErrorAction Stop
    Write-Host "✓ Aplicación está corriendo" -ForegroundColor Green
} catch {
    Write-Host "✗ La aplicación NO está corriendo" -ForegroundColor Red
    Write-Host "  Ejecuta: mvn spring-boot:run" -ForegroundColor Yellow
    exit 1
}

# Pruebas GET
Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "PRUEBAS GET (Listar/Obtener)" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan

Test-Endpoint -Method "GET" -Url "$baseUrl/api/clientes" -Description "Listar todos los clientes"
Test-Endpoint -Method "GET" -Url "$baseUrl/api/mesas" -Description "Listar todas las mesas"
Test-Endpoint -Method "GET" -Url "$baseUrl/api/reservas" -Description "Listar todas las reservas"
Test-Endpoint -Method "GET" -Url "$baseUrl/api/clientes/1" -Description "Obtener cliente con ID 1"
Test-Endpoint -Method "GET" -Url "$baseUrl/api/reservas/disponibilidad?idMesa=1&fecha=2025-12-10&hora=19:00" -Description "Verificar disponibilidad de mesa"

# Pruebas POST
Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "PRUEBAS POST (Crear)" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan

$nuevoCliente = @{
    nombre = "Cliente de Prueba"
    telefono = "555-TEST"
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/api/clientes" -Body $nuevoCliente -Description "Crear nuevo cliente"

$nuevaMesa = @{
    numeroMesa = 99
    capacidad = 2
    estado = "DISPONIBLE"
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/api/mesas" -Body $nuevaMesa -Description "Crear nueva mesa"

# Resumen
Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "PRUEBAS COMPLETADAS" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Para más pruebas, usa Postman o el navegador:" -ForegroundColor Yellow
Write-Host "  - GET: http://localhost:9090/api/clientes" -ForegroundColor Gray
Write-Host "  - POST: Usa Postman con JSON body" -ForegroundColor Gray
Write-Host ""



