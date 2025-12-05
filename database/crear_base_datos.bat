@echo off
echo ============================================
echo Creando Base de Datos MySQL - Sistema Reservas
echo ============================================
echo.

REM Verificar si MySQL está instalado
where mysql >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: MySQL no se encuentra en el PATH
    echo.
    echo Por favor, instala MySQL o agrega MySQL al PATH del sistema
    echo.
    echo Ruta típica: C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe
    echo.
    pause
    exit /b 1
)

echo Conectando a MySQL...
echo.
echo IMPORTANTE: Necesitaras ingresar la contraseña de root
echo (Si no tienes contraseña, presiona Enter)
echo.

REM Intentar ejecutar el script SQL
mysql -u root -p < "%~dp0setup.sql"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo ¡Base de datos creada exitosamente!
    echo ============================================
    echo.
    echo La base de datos 'reservas' ha sido creada.
    echo Ahora puedes ejecutar tu aplicación Spring Boot.
    echo.
) else (
    echo.
    echo ============================================
    echo ERROR al crear la base de datos
    echo ============================================
    echo.
    echo Posibles causas:
    echo 1. Contraseña incorrecta
    echo 2. MySQL no está corriendo
    echo 3. Usuario root no tiene permisos
    echo.
)

pause

