# 🧪 Guía: Probar la API Manualmente

## Método 1: Desde el Navegador (Más Fácil) 🌐

### Probar Endpoints GET (Listar)

1. **Listar Clientes:**
   ```
   http://localhost:9090/api/clientes
   ```
   - Abre tu navegador
   - Pega la URL en la barra de direcciones
   - Presiona Enter
   - Deberías ver un JSON con la lista de clientes

2. **Listar Mesas:**
   ```
   http://localhost:9090/api/mesas
   ```

3. **Listar Reservas:**
   ```
   http://localhost:9090/api/reservas
   ```

4. **Obtener Cliente por ID:**
   ```
   http://localhost:9090/api/clientes/1
   ```
   (Reemplaza `1` con el ID que quieras)

5. **Verificar Disponibilidad:**
   ```
   http://localhost:9090/api/reservas/disponibilidad?idMesa=1&fecha=2025-12-10&hora=19:00
   ```

---

## Método 2: PowerShell (Windows) 💻

### Probar GET (Listar)

```powershell
# Listar clientes
Invoke-WebRequest -Uri "http://localhost:9090/api/clientes" -UseBasicParsing

# Ver solo el contenido JSON
(Invoke-WebRequest -Uri "http://localhost:9090/api/clientes" -UseBasicParsing).Content

# Listar mesas
Invoke-WebRequest -Uri "http://localhost:9090/api/mesas" -UseBasicParsing

# Listar reservas
Invoke-WebRequest -Uri "http://localhost:9090/api/reservas" -UseBasicParsing
```

### Probar POST (Crear)

```powershell
# Crear un cliente
$body = @{
    nombre = "Pedro Sánchez"
    telefono = "555-9999"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:9090/api/clientes" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body `
    -UseBasicParsing

# Crear una mesa
$body = @{
    numeroMesa = 11
    capacidad = 4
    estado = "DISPONIBLE"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:9090/api/mesas" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body `
    -UseBasicParsing
```

### Probar PUT (Actualizar)

```powershell
# Actualizar cliente
$body = @{
    nombre = "Pedro Sánchez Actualizado"
    telefono = "555-8888"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:9090/api/clientes/1" `
    -Method PUT `
    -ContentType "application/json" `
    -Body $body `
    -UseBasicParsing
```

### Probar DELETE (Eliminar)

```powershell
# Eliminar cliente (cuidado: solo si no tiene reservas activas)
Invoke-WebRequest -Uri "http://localhost:9090/api/clientes/1" `
    -Method DELETE `
    -UseBasicParsing
```

---

## Método 3: cURL (Si está instalado) 🔧

### GET
```bash
curl http://localhost:9090/api/clientes
```

### POST
```bash
curl -X POST http://localhost:9090/api/clientes \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"Pedro Sánchez\",\"telefono\":\"555-9999\"}"
```

### PUT
```bash
curl -X PUT http://localhost:9090/api/clientes/1 \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"Pedro Actualizado\",\"telefono\":\"555-8888\"}"
```

### DELETE
```bash
curl -X DELETE http://localhost:9090/api/clientes/1
```

---

## Método 4: Postman (Recomendado para pruebas completas) 📮

### Configuración Inicial

1. **Descarga Postman**: https://www.postman.com/downloads/
2. **Instala y abre Postman**

### Crear una Colección

1. Clic en **"New"** → **"Collection"**
2. Nombre: `Sistema de Reservas`
3. Clic en **"Add Request"** para cada endpoint

### Endpoints para Probar

#### 1. GET - Listar Clientes
- **Método**: GET
- **URL**: `http://localhost:9090/api/clientes`
- **Headers**: (ninguno necesario)
- **Body**: (ninguno)

#### 2. POST - Crear Cliente
- **Método**: POST
- **URL**: `http://localhost:9090/api/clientes`
- **Headers**: 
  - Key: `Content-Type`
  - Value: `application/json`
- **Body** (raw JSON):
  ```json
  {
    "nombre": "Pedro Sánchez",
    "telefono": "555-9999"
  }
  ```

#### 3. GET - Obtener Cliente por ID
- **Método**: GET
- **URL**: `http://localhost:9090/api/clientes/1`
- (Reemplaza `1` con el ID que quieras)

#### 4. PUT - Actualizar Cliente
- **Método**: PUT
- **URL**: `http://localhost:9090/api/clientes/1`
- **Headers**: 
  - Key: `Content-Type`
  - Value: `application/json`
- **Body** (raw JSON):
  ```json
  {
    "nombre": "Pedro Actualizado",
    "telefono": "555-8888"
  }
  ```

#### 5. DELETE - Eliminar Cliente
- **Método**: DELETE
- **URL**: `http://localhost:9090/api/clientes/1`
- (Solo funciona si no tiene reservas activas)

#### 6. POST - Crear Mesa
- **Método**: POST
- **URL**: `http://localhost:9090/api/mesas`
- **Headers**: 
  - Key: `Content-Type`
  - Value: `application/json`
- **Body** (raw JSON):
  ```json
  {
    "numeroMesa": 11,
    "capacidad": 4,
    "estado": "DISPONIBLE"
  }
  ```

#### 7. POST - Crear Reserva
- **Método**: POST
- **URL**: `http://localhost:9090/api/reservas`
- **Headers**: 
  - Key: `Content-Type`
  - Value: `application/json`
- **Body** (raw JSON):
  ```json
  {
    "fecha": "2025-12-10",
    "hora": "19:00:00",
    "cliente": {
      "idCliente": 1
    },
    "mesa": {
      "idMesa": 1
    },
    "estado": "ACTIVA"
  }
  ```

#### 8. GET - Verificar Disponibilidad
- **Método**: GET
- **URL**: `http://localhost:9090/api/reservas/disponibilidad?idMesa=1&fecha=2025-12-10&hora=19:00`

---

## Método 5: Script de Prueba Automatizado 🚀

He creado un script `test_conexion.ps1` que puedes ejecutar:

```powershell
powershell -ExecutionPolicy Bypass -File test_conexion.ps1
```

---

## ✅ Verificar que la Aplicación Está Corriendo

Antes de probar, verifica que la aplicación esté activa:

```powershell
# Verificar puerto
netstat -ano | findstr :9090

# O probar directamente
Invoke-WebRequest -Uri "http://localhost:9090/api/clientes" -UseBasicParsing
```

Si la aplicación NO está corriendo:
```powershell
mvn spring-boot:run
```

---

## 📋 Ejemplos de Respuestas Esperadas

### GET /api/clientes (Éxito)
```json
[
  {
    "idCliente": 1,
    "nombre": "Juan Pérez",
    "telefono": "555-1234"
  },
  {
    "idCliente": 2,
    "nombre": "María García",
    "telefono": "555-5678"
  }
]
```

### POST /api/clientes (Éxito - 201 Created)
```json
{
  "idCliente": 6,
  "nombre": "Pedro Sánchez",
  "telefono": "555-9999"
}
```

### GET /api/clientes/999 (Error - 404 Not Found)
```json
{
  "mensaje": "Cliente no encontrado con id: 999"
}
```

### DELETE /api/clientes/1 (Error - 409 Conflict si tiene reservas)
```json
{
  "mensaje": "No se puede eliminar el cliente porque tiene reservas activas"
}
```

---

## 🔍 Códigos de Estado HTTP

- **200 OK**: Operación exitosa (GET, PUT)
- **201 Created**: Recurso creado exitosamente (POST)
- **204 No Content**: Recurso eliminado exitosamente (DELETE)
- **400 Bad Request**: Datos inválidos
- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Conflicto (ej: eliminar cliente con reservas activas)

---

## 🎯 Flujo de Prueba Recomendado

1. **Verificar que la app está corriendo**
   ```powershell
   Invoke-WebRequest -Uri "http://localhost:9090/api/clientes" -UseBasicParsing
   ```

2. **Listar clientes existentes**
   - Abre: `http://localhost:9090/api/clientes` en el navegador

3. **Crear un nuevo cliente**
   - Usa Postman o PowerShell con POST

4. **Verificar que se creó**
   - Lista clientes de nuevo y busca el nuevo ID

5. **Actualizar el cliente**
   - Usa PUT con el ID del cliente creado

6. **Probar otros endpoints**
   - Mesas, Reservas, Disponibilidad

---

## 🐛 Solución de Problemas

### Error: "No se puede conectar"
- Verifica que la aplicación esté corriendo: `mvn spring-boot:run`
- Verifica el puerto: `netstat -ano | findstr :9090`

### Error: 404 Not Found
- Verifica la URL (debe ser exacta)
- Verifica que el endpoint exista en el controlador

### Error: 400 Bad Request
- Verifica el formato JSON
- Verifica que todos los campos requeridos estén presentes

### Error: 500 Internal Server Error
- Revisa los logs de la aplicación
- Verifica la conexión a la base de datos

---

¡Listo para probar! 🚀



