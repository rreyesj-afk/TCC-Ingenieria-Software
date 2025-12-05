# Guía de Configuración de MySQL Local

## Paso 1: Instalar MySQL (si no lo tienes)

### Windows:
1. Descarga MySQL Installer desde: https://dev.mysql.com/downloads/installer/
2. Ejecuta el instalador
3. Selecciona "Developer Default" o "Server only"
4. Durante la instalación, configura:
   - **Root Password**: Anota la contraseña que elijas (o déjala vacía)
   - **Puerto**: 3306 (por defecto)
5. Completa la instalación

### Verificar instalación:
Abre PowerShell o CMD y ejecuta:
```bash
mysql --version
```

Si no funciona, agrega MySQL al PATH o usa la ruta completa:
```bash
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" --version
```

---

## Paso 2: Iniciar el servicio MySQL

### Windows (Servicios):
1. Presiona `Win + R`
2. Escribe `services.msc` y presiona Enter
3. Busca "MySQL80" o "MySQL"
4. Haz clic derecho → "Iniciar" (si está detenido)

### O desde PowerShell (como Administrador):
```powershell
Start-Service MySQL80
```

---

## Paso 3: Conectarte a MySQL

Abre PowerShell o CMD y ejecuta:

```bash
mysql -u root -p
```

Te pedirá la contraseña. Si no configuraste contraseña, presiona Enter.

---

## Paso 4: Ejecutar el script de creación

### Opción A: Desde MySQL CLI
1. Conéctate a MySQL: `mysql -u root -p`
2. Ejecuta el script:
   ```sql
   source C:/Users/Steven/Documents/NetBeansProjects/reservas/database/setup.sql
   ```
   (Ajusta la ruta según tu ubicación)

### Opción B: Desde PowerShell
```powershell
cd C:\Users\Steven\Documents\NetBeansProjects\reservas\database
mysql -u root -p < setup.sql
```

### Opción C: Copiar y pegar manualmente
1. Abre `database/setup.sql`
2. Copia todo el contenido
3. Pégalo en MySQL CLI y presiona Enter

---

## Paso 5: Verificar que se creó

En MySQL CLI, ejecuta:
```sql
SHOW DATABASES;
```

Deberías ver `reservas` en la lista.

---

## Paso 6: Configurar application.properties

El archivo ya está configurado para usar:
- **Host**: localhost
- **Puerto**: 3306
- **Base de datos**: reservas
- **Usuario**: root (o cambia a reservas_user)
- **Contraseña**: (vacía por defecto, o la que configuraste)

Si usaste el usuario `reservas_user` con contraseña `reservas123`, actualiza `application.properties`:
```properties
spring.datasource.username=reservas_user
spring.datasource.password=reservas123
```

---

## Paso 7: Probar la conexión

Ejecuta la aplicación:
```bash
mvn spring-boot:run
```

Si todo está bien, verás:
- ✅ "Started ReservasApplication"
- ✅ Las tablas se crearán automáticamente (clientes, mesas, reservas)

---

## Solución de problemas

### Error: "Access denied"
- Verifica usuario y contraseña en `application.properties`
- Si usas `root` sin contraseña, deja `spring.datasource.password=` vacío

### Error: "Unknown database 'reservas'"
- Ejecuta el script `setup.sql` primero

### Error: "Can't connect to MySQL server"
- Verifica que el servicio MySQL esté corriendo
- Verifica que el puerto sea 3306

### Error: "Table already exists"
- Es normal si ya ejecutaste la app antes
- JPA actualizará las tablas automáticamente

---

## Comandos útiles

### Ver tablas creadas:
```sql
USE reservas;
SHOW TABLES;
```

### Ver estructura de una tabla:
```sql
DESCRIBE clientes;
DESCRIBE mesas;
DESCRIBE reservas;
```

### Eliminar y recrear (si necesitas empezar de cero):
```sql
DROP DATABASE IF EXISTS reservas;
-- Luego ejecuta setup.sql de nuevo
```

