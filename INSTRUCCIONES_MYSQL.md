# 🗄️ Guía Completa: Configurar MySQL Local para el Proyecto Reservas

## 📋 Índice
1. [Verificar si MySQL está instalado](#1-verificar-si-mysql-está-instalado)
2. [Instalar MySQL (si no lo tienes)](#2-instalar-mysql-si-no-lo-tienes)
3. [Iniciar el servicio MySQL](#3-iniciar-el-servicio-mysql)
4. [Crear la base de datos](#4-crear-la-base-de-datos)
5. [Configurar la aplicación](#5-configurar-la-aplicación)
6. [Probar la conexión](#6-probar-la-conexión)

---

## 1. Verificar si MySQL está instalado

### Opción A: Desde PowerShell
```powershell
mysql --version
```

### Opción B: Buscar en el sistema
1. Presiona `Win + R`
2. Escribe: `services.msc`
3. Busca "MySQL80" o "MySQL" en la lista

Si ves MySQL en los servicios, está instalado.

---

## 2. Instalar MySQL (si no lo tienes)

### Paso 1: Descargar MySQL
1. Ve a: https://dev.mysql.com/downloads/installer/
2. Descarga **MySQL Installer for Windows**
3. Elige la versión **web installer** (más ligera)

### Paso 2: Instalar
1. Ejecuta el instalador
2. Selecciona **"Developer Default"** o **"Server only"**
3. Sigue el asistente
4. En **"Accounts and Roles"**:
   - **Root Password**: Puedes dejarlo vacío o poner una contraseña
   - **Anota la contraseña** si la pones
5. Completa la instalación

### Paso 3: Agregar MySQL al PATH (si no funciona desde PowerShell)
1. Busca la ruta de MySQL (típicamente): `C:\Program Files\MySQL\MySQL Server 8.0\bin`
2. Copia esa ruta
3. Presiona `Win + X` → **Sistema** → **Configuración avanzada del sistema**
4. Clic en **"Variables de entorno"**
5. En **"Variables del sistema"**, busca **Path** y haz clic en **"Editar"**
6. Clic en **"Nuevo"** y pega la ruta: `C:\Program Files\MySQL\MySQL Server 8.0\bin`
7. Acepta todos los diálogos

---

## 3. Iniciar el servicio MySQL

### Método 1: Desde Servicios (Recomendado)
1. Presiona `Win + R`
2. Escribe: `services.msc` y presiona Enter
3. Busca **"MySQL80"** o **"MySQL"**
4. Si está **Detenido**, haz clic derecho → **Iniciar**

### Método 2: Desde PowerShell (como Administrador)
```powershell
Start-Service MySQL80
```

### Método 3: Verificar estado
```powershell
Get-Service MySQL80
```

---

## 4. Crear la base de datos

Tienes **3 opciones** para crear la base de datos:

### Opción A: Script Automático (Más fácil) ⭐
1. Abre PowerShell en la carpeta del proyecto
2. Ejecuta:
   ```powershell
   cd database
   .\crear_base_datos.bat
   ```
3. Ingresa la contraseña de root (o presiona Enter si no tienes)

### Opción B: MySQL Workbench (Visual)
1. Abre **MySQL Workbench** (viene con MySQL)
2. Conéctate a tu servidor local (puerto 3306)
3. Abre el archivo: `database/crear_base_datos_manual.sql`
4. Selecciona todo el contenido (Ctrl+A)
5. Ejecuta (F9 o botón ⚡)

### Opción C: Línea de comandos MySQL
1. Abre PowerShell
2. Conéctate a MySQL:
   ```powershell
   mysql -u root -p
   ```
   (Si no tienes contraseña, presiona Enter)
3. Ejecuta:
   ```sql
   CREATE DATABASE IF NOT EXISTS reservas
       CHARACTER SET utf8mb4
       COLLATE utf8mb4_unicode_ci;
   ```
4. Verifica:
   ```sql
   SHOW DATABASES;
   ```
5. Sal de MySQL:
   ```sql
   exit;
   ```

---

## 5. Configurar la aplicación

El archivo `src/main/resources/application.properties` ya está configurado, pero verifica:

### Si usas root SIN contraseña:
```properties
spring.datasource.username=root
spring.datasource.password=
```

### Si usas root CON contraseña:
```properties
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA_AQUI
```

### Si creaste un usuario específico:
```properties
spring.datasource.username=reservas_user
spring.datasource.password=reservas123
```

---

## 6. Probar la conexión

### Paso 1: Ejecutar la aplicación
```powershell
mvn spring-boot:run
```

### Paso 2: Verificar en los logs
Deberías ver:
```
✅ Started ReservasApplication
✅ Hibernate creating/updating tables
```

### Paso 3: Verificar en MySQL
1. Conéctate a MySQL:
   ```powershell
   mysql -u root -p
   ```
2. Usa la base de datos:
   ```sql
   USE reservas;
   ```
3. Ver las tablas creadas:
   ```sql
   SHOW TABLES;
   ```
   Deberías ver: `clientes`, `mesas`, `reservas`

---

## 🔧 Solución de Problemas

### Error: "Access denied for user 'root'@'localhost'"
**Solución**: Verifica la contraseña en `application.properties`

### Error: "Unknown database 'reservas'"
**Solución**: Ejecuta el script de creación de base de datos (Paso 4)

### Error: "Can't connect to MySQL server"
**Solución**: 
1. Verifica que el servicio MySQL esté corriendo
2. Verifica que el puerto sea 3306
3. Prueba conectarte manualmente: `mysql -u root -p`

### Error: "Table already exists"
**Solución**: Es normal, JPA actualizará las tablas automáticamente

### MySQL no está en el PATH
**Solución**: 
1. Encuentra la ruta: `C:\Program Files\MySQL\MySQL Server 8.0\bin`
2. Agrega esa ruta a las Variables de Entorno del sistema

---

## ✅ Checklist Final

- [ ] MySQL instalado
- [ ] Servicio MySQL corriendo
- [ ] Base de datos `reservas` creada
- [ ] `application.properties` configurado correctamente
- [ ] Aplicación Spring Boot ejecutándose sin errores
- [ ] Tablas creadas en MySQL (clientes, mesas, reservas)

---

## 🎉 ¡Listo!

Una vez completados estos pasos, tu aplicación estará lista para usar. Las tablas se crearán automáticamente cuando ejecutes la aplicación por primera vez gracias a `ddl-auto=update`.

**Próximo paso**: Probar los endpoints con Postman o crear algunos datos de prueba.

