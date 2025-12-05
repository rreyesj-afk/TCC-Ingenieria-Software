-- ============================================
-- Script MANUAL de creación de Base de Datos MySQL
-- Sistema de Reservas de Restaurante
-- 
-- INSTRUCCIONES:
-- 1. Abre MySQL Workbench o MySQL Command Line
-- 2. Conéctate como root
-- 3. Copia y pega este script completo
-- 4. Ejecuta (F9 en Workbench o Enter en CLI)
-- ============================================

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS reservas
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Mostrar mensaje de confirmación
SELECT 'Base de datos "reservas" creada exitosamente!' AS Mensaje;

-- Mostrar todas las bases de datos (para verificar)
SHOW DATABASES;

-- Usar la base de datos
USE reservas;

-- Mostrar mensaje final
SELECT 'Ahora puedes ejecutar tu aplicación Spring Boot. Las tablas se crearán automáticamente.' AS Instruccion;

