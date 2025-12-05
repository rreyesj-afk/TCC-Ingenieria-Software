-- ============================================
-- Script COMPLETO de Creación de Base de Datos y Tablas
-- Sistema de Reservas de Restaurante
-- 
-- Este script crea TODO desde cero:
-- 1. Base de datos
-- 2. Todas las tablas
-- 3. Relaciones y restricciones
-- ============================================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS reservas
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE reservas;

-- ============================================
-- ELIMINAR TABLAS SI EXISTEN (CUIDADO: BORRA DATOS)
-- Descomenta las siguientes líneas si quieres empezar de cero
-- ============================================
-- DROP TABLE IF EXISTS reservas;
-- DROP TABLE IF EXISTS mesas;
-- DROP TABLE IF EXISTS clientes;

-- ============================================
-- Tabla: clientes
-- ============================================
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único del cliente',
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre completo del cliente',
    telefono VARCHAR(30) NOT NULL COMMENT 'Número de teléfono de contacto',
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Tabla de clientes del restaurante';

-- ============================================
-- Tabla: mesas
-- ============================================
CREATE TABLE IF NOT EXISTS mesas (
    id_mesa BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único de la mesa',
    numero_mesa INT NOT NULL UNIQUE COMMENT 'Número visible de la mesa',
    capacidad INT NOT NULL COMMENT 'Capacidad máxima de personas (1-20)',
    estado VARCHAR(15) NOT NULL DEFAULT 'DISPONIBLE' COMMENT 'Estado: DISPONIBLE u OCUPADA',
    INDEX idx_numero_mesa (numero_mesa),
    INDEX idx_estado (estado),
    CONSTRAINT chk_capacidad CHECK (capacidad >= 1 AND capacidad <= 20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Tabla de mesas del restaurante';

-- ============================================
-- Tabla: reservas
-- ============================================
CREATE TABLE IF NOT EXISTS reservas (
    id_reserva BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único de la reserva',
    fecha DATE NOT NULL COMMENT 'Fecha de la reserva',
    hora TIME NOT NULL COMMENT 'Hora de la reserva',
    id_cliente BIGINT NOT NULL COMMENT 'Cliente que realiza la reserva',
    id_mesa BIGINT NOT NULL COMMENT 'Mesa reservada',
    estado VARCHAR(15) NOT NULL DEFAULT 'ACTIVA' COMMENT 'Estado: ACTIVA o CANCELADA',
    
    -- Claves foráneas
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    FOREIGN KEY (id_mesa) REFERENCES mesas(id_mesa) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    
    -- Índices para mejorar rendimiento
    INDEX idx_fecha (fecha),
    INDEX idx_mesa_fecha_hora (id_mesa, fecha, hora),
    INDEX idx_cliente (id_cliente),
    INDEX idx_estado (estado),
    
    -- Restricción: No puede haber dos reservas ACTIVAS para la misma mesa, fecha y hora
    -- Nota: Esta restricción puede ser ajustada según necesidades
    INDEX idx_mesa_fecha_hora_estado (id_mesa, fecha, hora, estado)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Tabla de reservas del restaurante';

-- ============================================
-- Verificar creación
-- ============================================
SHOW TABLES;

-- ============================================
-- Mostrar estructura de cada tabla
-- ============================================
SELECT '=== ESTRUCTURA DE CLIENTES ===' AS '';
DESCRIBE clientes;

SELECT '=== ESTRUCTURA DE MESAS ===' AS '';
DESCRIBE mesas;

SELECT '=== ESTRUCTURA DE RESERVAS ===' AS '';
DESCRIBE reservas;

-- ============================================
-- Mostrar claves foráneas
-- ============================================
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    TABLE_SCHEMA = 'reservas'
    AND REFERENCED_TABLE_NAME IS NOT NULL;

-- ============================================
-- Mensaje final
-- ============================================
SELECT '¡Base de datos y tablas creadas exitosamente!' AS Mensaje;
SELECT 'Ahora puedes usar la aplicación Spring Boot.' AS Instruccion;

