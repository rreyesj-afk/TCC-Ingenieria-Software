-- ============================================
-- Script de creación de Base de Datos MySQL
-- Sistema de Reservas de Restaurante
-- ============================================

-- Paso 1: Crear la base de datos
CREATE DATABASE IF NOT EXISTS reservas
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Paso 2: Usar la base de datos
USE reservas;

-- Paso 3: Crear usuario (opcional, si quieres un usuario específico)
-- Si prefieres usar 'root', puedes saltarte este paso
CREATE USER IF NOT EXISTS 'reservas_user'@'localhost' IDENTIFIED BY 'reservas123';

-- Paso 4: Dar permisos al usuario
GRANT ALL PRIVILEGES ON reservas.* TO 'reservas_user'@'localhost';
FLUSH PRIVILEGES;

-- ============================================
-- NOTA: Las tablas se crearán automáticamente
-- gracias a JPA con ddl-auto=update
-- ============================================

