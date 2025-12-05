-- ============================================
-- Script de Datos de Prueba
-- Sistema de Reservas de Restaurante
-- 
-- Este script inserta datos de ejemplo para probar la aplicación
-- ============================================

USE reservas;

-- ============================================
-- Insertar Clientes de Prueba
-- ============================================
INSERT INTO clientes (nombre, telefono) VALUES
('Juan Pérez', '555-1234'),
('María García', '555-5678'),
('Carlos Rodríguez', '555-9012'),
('Ana Martínez', '555-3456'),
('Luis López', '555-7890');

-- ============================================
-- Insertar Mesas de Prueba
-- ============================================
INSERT INTO mesas (numero_mesa, capacidad, estado) VALUES
(1, 2, 'DISPONIBLE'),
(2, 4, 'DISPONIBLE'),
(3, 4, 'DISPONIBLE'),
(4, 6, 'DISPONIBLE'),
(5, 2, 'DISPONIBLE'),
(6, 8, 'DISPONIBLE'),
(7, 4, 'DISPONIBLE'),
(8, 2, 'DISPONIBLE'),
(9, 6, 'DISPONIBLE'),
(10, 4, 'DISPONIBLE');

-- ============================================
-- Insertar Reservas de Prueba
-- ============================================
-- Nota: Ajusta las fechas según la fecha actual
-- Las fechas deben ser futuras o del día actual

INSERT INTO reservas (fecha, hora, id_cliente, id_mesa, estado) VALUES
-- Reserva para mañana a las 19:00
(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '19:00:00', 1, 1, 'ACTIVA'),
-- Reserva para mañana a las 20:00
(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '20:00:00', 2, 2, 'ACTIVA'),
-- Reserva para pasado mañana a las 19:30
(DATE_ADD(CURDATE(), INTERVAL 2 DAY), '19:30:00', 3, 3, 'ACTIVA'),
-- Reserva cancelada (ejemplo)
(DATE_ADD(CURDATE(), INTERVAL 3 DAY), '20:00:00', 4, 4, 'CANCELADA');

-- ============================================
-- Verificar datos insertados
-- ============================================
SELECT '=== CLIENTES ===' AS '';
SELECT * FROM clientes;

SELECT '=== MESAS ===' AS '';
SELECT * FROM mesas;

SELECT '=== RESERVAS ===' AS '';
SELECT 
    r.id_reserva,
    r.fecha,
    r.hora,
    c.nombre AS cliente,
    m.numero_mesa,
    r.estado
FROM reservas r
JOIN clientes c ON r.id_cliente = c.id_cliente
JOIN mesas m ON r.id_mesa = m.id_mesa
ORDER BY r.fecha, r.hora;

-- ============================================
-- Estadísticas
-- ============================================
SELECT 
    (SELECT COUNT(*) FROM clientes) AS total_clientes,
    (SELECT COUNT(*) FROM mesas) AS total_mesas,
    (SELECT COUNT(*) FROM reservas WHERE estado = 'ACTIVA') AS reservas_activas,
    (SELECT COUNT(*) FROM reservas WHERE estado = 'CANCELADA') AS reservas_canceladas;

