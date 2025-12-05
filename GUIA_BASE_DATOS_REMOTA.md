# Guía para Configurar Base de Datos Remota

## Opción 1: PlanetScale (Recomendado - Más Fácil)

### Paso 1: Crear cuenta
1. Ve a https://planetscale.com/
2. Crea una cuenta gratuita (puedes usar GitHub)
3. Verifica tu email

### Paso 2: Crear base de datos
1. En el dashboard, haz clic en "Create database"
2. Nombre: `reservas`
3. Región: Elige la más cercana (ej: `us-east`)
4. Plan: Free
5. Haz clic en "Create database"

### Paso 3: Obtener credenciales
1. Ve a tu base de datos `reservas`
2. Haz clic en "Connect"
3. Selecciona "Java" como lenguaje
4. Copia los valores:
   - **Host**: algo como `aws.connect.psdb.cloud`
   - **Database**: `reservas`
   - **Username**: algo como `abc123def456`
   - **Password**: Genera uno nuevo con "New password"
   - **Port**: `3306`

### Paso 4: Configurar en la aplicación
1. Abre `src/main/resources/application-remote.properties`
2. Reemplaza los valores:
   ```
   spring.datasource.url=jdbc:mysql://TU_HOST:3306/reservas?serverTimezone=UTC&useSSL=true&requireSSL=true
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_PASSWORD
   ```

### Paso 5: Ejecutar con perfil remoto
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=remote
```

---

## Opción 2: Railway (Alternativa)

### Paso 1: Crear cuenta
1. Ve a https://railway.app/
2. Crea cuenta con GitHub

### Paso 2: Crear MySQL
1. Haz clic en "New Project"
2. Selecciona "Provision MySQL"
3. Railway creará automáticamente una base de datos MySQL

### Paso 3: Obtener credenciales
1. Haz clic en tu servicio MySQL
2. Ve a la pestaña "Variables"
3. Copia:
   - `MYSQLHOST` → Host
   - `MYSQLDATABASE` → Database
   - `MYSQLUSER` → Username
   - `MYSQLPASSWORD` → Password
   - `MYSQLPORT` → Port (usualmente 3306)

### Paso 4: Configurar y ejecutar
Igual que PlanetScale, actualiza `application-remote.properties` y ejecuta con el perfil `remote`.

---

## Opción 3: Aiven (Alternativa)

1. Ve a https://aiven.io/
2. Crea cuenta gratuita
3. Crea un servicio MySQL
4. Obtén las credenciales de conexión
5. Configura igual que las opciones anteriores

---

## Compartir credenciales con tu equipo

⚠️ **IMPORTANTE**: No subas las credenciales al repositorio Git.

### Opción A: Variables de entorno (Recomendado)
Cada miembro del equipo configura variables de entorno:

**Windows (PowerShell):**
```powershell
$env:DB_HOST="tu-host"
$env:DB_USER="tu-usuario"
$env:DB_PASSWORD="tu-password"
$env:DB_NAME="reservas"
```

**Linux/Mac:**
```bash
export DB_HOST="tu-host"
export DB_USER="tu-usuario"
export DB_PASSWORD="tu-password"
export DB_NAME="reservas"
```

Luego actualiza `application-remote.properties` para usar variables:
```properties
spring.datasource.url=jdbc:mysql://${DB_HOST}:3306/${DB_NAME}?serverTimezone=UTC&useSSL=true
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

### Opción B: Archivo local (no versionado)
1. Crea `application-secrets.properties` (agregar a `.gitignore`)
2. Cada miembro crea su propio archivo con las credenciales
3. Spring Boot lo cargará automáticamente

---

## Verificar conexión

Una vez configurado, ejecuta:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=remote
```

Si todo está bien, verás:
- ✅ "Started ReservasApplication"
- ✅ Las tablas se crearán automáticamente (gracias a `ddl-auto=update`)

---

## Troubleshooting

**Error: "Access denied"**
- Verifica usuario y contraseña
- En PlanetScale, asegúrate de generar una nueva contraseña

**Error: "SSL required"**
- Agrega `&useSSL=true&requireSSL=true` a la URL

**Error: "Connection timeout"**
- Verifica que el host y puerto sean correctos
- Verifica tu conexión a internet
- Algunos servicios requieren whitelist de IPs

