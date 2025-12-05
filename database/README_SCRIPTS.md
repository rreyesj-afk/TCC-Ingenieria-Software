# 📋 Guía de Scripts SQL

Este directorio contiene scripts SQL para crear y gestionar la base de datos del sistema de reservas.

## 📁 Archivos Disponibles

### 1. `setup.sql`
**Propósito**: Crear solo la base de datos (sin tablas)  
**Cuándo usar**: Si solo necesitas crear la base de datos vacía

```sql
CREATE DATABASE reservas;
```

---

### 2. `crear_tablas.sql` ⭐ **RECOMENDADO**
**Propósito**: Crear todas las tablas con estructura completa  
**Cuándo usar**: Cuando ya tienes la base de datos creada y quieres crear las tablas

**Incluye**:
- Tabla `clientes`
- Tabla `mesas`
- Tabla `reservas`
- Claves foráneas
- Índices para optimización
- Restricciones de integridad

**Cómo ejecutar**:
```bash
mysql -u root -p reservas < crear_tablas.sql
```

O en MySQL Workbench:
1. Abre el archivo
2. Selecciona todo (Ctrl+A)
3. Ejecuta (F9)

---

### 3. `crear_tablas_completo.sql`
**Propósito**: Crear TODO desde cero (base de datos + tablas)  
**Cuándo usar**: Si quieres empezar completamente de cero

**Incluye**:
- Creación de base de datos
- Todas las tablas
- Comentarios detallados
- Verificación de estructura
- Información de claves foráneas

**Cómo ejecutar**:
```bash
mysql -u root -p < crear_tablas_completo.sql
```

---

### 4. `insertar_datos_prueba.sql`
**Propósito**: Insertar datos de ejemplo para probar la aplicación  
**Cuándo usar**: Después de crear las tablas, para tener datos de prueba

**Incluye**:
- 5 clientes de ejemplo
- 10 mesas de ejemplo
- 4 reservas de ejemplo (con fechas dinámicas)

**Cómo ejecutar**:
```bash
mysql -u root -p reservas < insertar_datos_prueba.sql
```

---

## 🚀 Flujo Recomendado

### Opción 1: Crear todo manualmente
```bash
# Paso 1: Crear base de datos
mysql -u root -p < setup.sql

# Paso 2: Crear tablas
mysql -u root -p reservas < crear_tablas.sql

# Paso 3 (Opcional): Insertar datos de prueba
mysql -u root -p reservas < insertar_datos_prueba.sql
```

### Opción 2: Todo en uno
```bash
# Crea base de datos + tablas en un solo paso
mysql -u root -p < crear_tablas_completo.sql

# Opcional: Agregar datos de prueba
mysql -u root -p reservas < insertar_datos_prueba.sql
```

### Opción 3: Desde MySQL Workbench
1. Abre MySQL Workbench
2. Conéctate a tu servidor
3. Abre el archivo `crear_tablas.sql` o `crear_tablas_completo.sql`
4. Ejecuta (F9)

---

## 📊 Estructura de las Tablas

### Tabla: `clientes`
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id_cliente | BIGINT | PK, Auto-increment |
| nombre | VARCHAR(100) | Nombre del cliente |
| telefono | VARCHAR(30) | Teléfono de contacto |

### Tabla: `mesas`
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id_mesa | BIGINT | PK, Auto-increment |
| numero_mesa | INT | Número único de la mesa |
| capacidad | INT | Capacidad (1-20 personas) |
| estado | VARCHAR(15) | DISPONIBLE u OCUPADA |

### Tabla: `reservas`
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id_reserva | BIGINT | PK, Auto-increment |
| fecha | DATE | Fecha de la reserva |
| hora | TIME | Hora de la reserva |
| id_cliente | BIGINT | FK → clientes |
| id_mesa | BIGINT | FK → mesas |
| estado | VARCHAR(15) | ACTIVA o CANCELADA |

---

## 🔍 Verificar que Funcionó

Después de ejecutar los scripts, verifica:

```sql
USE reservas;

-- Ver tablas creadas
SHOW TABLES;

-- Ver estructura de una tabla
DESCRIBE clientes;
DESCRIBE mesas;
DESCRIBE reservas;

-- Ver datos (si insertaste datos de prueba)
SELECT * FROM clientes;
SELECT * FROM mesas;
SELECT * FROM reservas;
```

---

## ⚠️ Notas Importantes

1. **Orden de ejecución**: Siempre crea las tablas en este orden:
   - Primero `clientes` y `mesas` (tablas independientes)
   - Luego `reservas` (depende de las anteriores)

2. **Claves foráneas**: Las tablas tienen restricciones `ON DELETE RESTRICT`, lo que significa:
   - No puedes eliminar un cliente si tiene reservas activas
   - No puedes eliminar una mesa si tiene reservas activas

3. **Índices**: Se crearon índices para optimizar:
   - Búsquedas por nombre de cliente
   - Búsquedas por número de mesa
   - Búsquedas de disponibilidad (mesa + fecha + hora)

4. **JPA vs SQL Manual**:
   - Si usas `ddl-auto=update` en Spring Boot, las tablas se crearán automáticamente
   - Los scripts SQL son útiles si quieres control total o crear la estructura manualmente

---

## 🆘 Solución de Problemas

### Error: "Table already exists"
**Solución**: Las tablas ya existen. Si quieres recrearlas:
```sql
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS mesas;
DROP TABLE IF EXISTS clientes;
```
Luego ejecuta `crear_tablas.sql` de nuevo.

### Error: "Foreign key constraint fails"
**Solución**: Asegúrate de crear las tablas en el orden correcto (clientes y mesas primero, luego reservas).

### Error: "Unknown database 'reservas'"
**Solución**: Primero ejecuta `setup.sql` o `crear_tablas_completo.sql` para crear la base de datos.

---

## ✅ Checklist

- [ ] Base de datos `reservas` creada
- [ ] Tabla `clientes` creada
- [ ] Tabla `mesas` creada
- [ ] Tabla `reservas` creada con claves foráneas
- [ ] Índices creados correctamente
- [ ] (Opcional) Datos de prueba insertados
- [ ] Verificación exitosa con `SHOW TABLES`

---

¡Listo! Ahora puedes ejecutar tu aplicación Spring Boot y debería conectarse correctamente a la base de datos.

