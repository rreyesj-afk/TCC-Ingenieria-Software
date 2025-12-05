-- ============================================
-- Script de Creación de Tablas
-- Sistema de Reservas de Restaurante
-- ============================================

USE reservas;

-- ============================================
-- Tabla: clientes
-- ============================================
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(30) NOT NULL,
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabla: mesas
-- ============================================
CREATE TABLE IF NOT EXISTS mesas (
    id_mesa BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_mesa INT NOT NULL UNIQUE,
    capacidad INT NOT NULL CHECK (capacidad >= 1 AND capacidad <= 20),
    estado VARCHAR(15) NOT NULL DEFAULT 'DISPONIBLE',
    INDEX idx_numero_mesa (numero_mesa),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabla: reservas
-- ============================================
CREATE TABLE IF NOT EXISTS reservas (
    id_reserva BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    id_cliente BIGINT NOT NULL,
    id_mesa BIGINT NOT NULL,
    estado VARCHAR(15) NOT NULL DEFAULT 'ACTIVA',
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    FOREIGN KEY (id_mesa) REFERENCES mesas(id_mesa) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    INDEX idx_fecha (fecha),
    INDEX idx_mesa_fecha_hora (id_mesa, fecha, hora),
    INDEX idx_cliente (id_cliente),
    INDEX idx_estado (estado),
    -- Índice compuesto para búsquedas de disponibilidad
    UNIQUE KEY uk_mesa_fecha_hora_activa (id_mesa, fecha, hora, estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Verificar tablas creadas
-- ============================================
SHOW TABLES;

-- ============================================
-- Mostrar estructura de las tablas
-- ============================================
DESCRIBE clientes;
DESCRIBE mesas;
DESCRIBE reservas;

-- ============================================
-- Mensaje de confirmación
-- ============================================
SELECT '¡Tablas creadas exitosamente!' AS Mensaje;

