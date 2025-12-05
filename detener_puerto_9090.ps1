# Script para detener el proceso que usa el puerto 9090
# Ejecutar como Administrador: clic derecho -> "Ejecutar con PowerShell"

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Deteniendo proceso en puerto 9090" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Buscar procesos usando el puerto 9090
$ports = netstat -ano | findstr :9090
if ($ports) {
    $pids = $ports | ForEach-Object {
        if ($_ -match '\s+(\d+)$') {
            $matches[1]
        }
    } | Select-Object -Unique
    
    foreach ($pid in $pids) {
        if ($pid -and $pid -ne "0") {
            try {
                $proc = Get-Process -Id $pid -ErrorAction SilentlyContinue
                if ($proc) {
                    Write-Host "Proceso encontrado:" -ForegroundColor Yellow
                    Write-Host "  PID: $pid" -ForegroundColor White
                    Write-Host "  Nombre: $($proc.ProcessName)" -ForegroundColor White
                    Write-Host "  Deteniendo..." -ForegroundColor Yellow
                    
                    Stop-Process -Id $pid -Force -ErrorAction Stop
                    Write-Host "  ✓ Proceso $pid detenido exitosamente" -ForegroundColor Green
                }
            } catch {
                Write-Host "  ✗ Error al detener proceso $pid: $_" -ForegroundColor Red
                Write-Host "  Intenta ejecutar este script como Administrador" -ForegroundColor Yellow
            }
        }
    }
    
    Write-Host ""
    Start-Sleep -Seconds 1
    
    # Verificar si el puerto está libre
    $check = netstat -ano | findstr :9090
    if ($check) {
        Write-Host "⚠ El puerto 9090 aún está en uso" -ForegroundColor Yellow
        Write-Host "  Puede ser necesario ejecutar este script como Administrador" -ForegroundColor Yellow
    } else {
        Write-Host "✓ Puerto 9090 liberado" -ForegroundColor Green
    }
} else {
    Write-Host "✓ No hay procesos usando el puerto 9090" -ForegroundColor Green
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan

