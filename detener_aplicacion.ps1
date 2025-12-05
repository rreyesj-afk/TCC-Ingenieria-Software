# Script para detener la aplicación Spring Boot en el puerto 9090

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Deteniendo aplicación en puerto 9090" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Buscar procesos usando el puerto 9090
$processes = netstat -ano | findstr :9090 | ForEach-Object {
    if ($_ -match '\s+(\d+)$') {
        $matches[1]
    }
} | Select-Object -Unique

if ($processes) {
    Write-Host "Procesos encontrados usando el puerto 9090:" -ForegroundColor Yellow
    foreach ($pid in $processes) {
        if ($pid -ne "0") {
            $proc = Get-Process -Id $pid -ErrorAction SilentlyContinue
            if ($proc) {
                Write-Host "  PID: $pid - Nombre: $($proc.ProcessName)" -ForegroundColor White
                Write-Host "  Deteniendo proceso..." -ForegroundColor Yellow
                Stop-Process -Id $pid -Force -ErrorAction SilentlyContinue
                Write-Host "  ✓ Proceso $pid detenido" -ForegroundColor Green
            }
        }
    }
    Write-Host ""
    Write-Host "✓ Puerto 9090 liberado" -ForegroundColor Green
} else {
    Write-Host "No se encontraron procesos usando el puerto 9090" -ForegroundColor Green
}

Write-Host ""
Write-Host "Ahora puedes ejecutar: mvn spring-boot:run" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan


