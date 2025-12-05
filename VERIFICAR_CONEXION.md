# ✅ Verificación de Conexión - Sistema de Reservas

## Estado Actual

✅ **Aplicación corriendo**: Puerto 9090 está activo (proceso 12556)  
⚠️ **Endpoints**: Retornan 404 (puede ser que aún esté arrancando o haya error de BD)

---

## 🔍 Pasos para Verificar la Conexión

### 1. Verificar que MySQL está corriendo

**Opción A: Desde Servicios**
- Presiona `Win + R`
- Escribe: `services.msc`
- Busca "MySQL80" o "MySQL"
- Debe estar en estado "En ejecución"

**Opción B: Desde PowerShell (como Administrador)**
```powershell
Get-Service MySQL80
```

Si está detenido:
```powershell
Start-Service MySQL80
```

---

### 2. Verificar que la base de datos existe

**Opción A: MySQL Workbench**
1. Abre MySQL Workbench
2. Conéctate a tu servidor local
3. Ejecuta:
   ```sql
   SHOW DATABASES;
   ```
4. Debes ver `reservas` en la lista

**Opción B: MySQL Command Line**
```bash
mysql -u root -p
```
Luego:
```sql
SHOW DATABASES;
USE reservas;
SHOW TABLES;
```

---

### 3. Verificar las tablas

Si las tablas no existen, ejecuta el script:
```sql
USE reservas;
-- Copia y pega el contenido de database/crear_tablas.sql
```

O desde archivo:
```bash
mysql -u root -p reservas < database/crear_tablas.sql
```

---

### 4. Verificar configuración en application.properties

Abre: `src/main/resources/application.properties`

Debe tener:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/reservas?serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=  # Vacío si no tienes contraseña, o tu contraseña
```

**Si tu MySQL tiene contraseña**, actualiza:
```properties
spring.datasource.password=TU_CONTRASEÑA_AQUI
```

---

### 5. Reiniciar la aplicación

**Detener la aplicación actual:**
```powershell
# Encuentra el proceso Java
Get-Process -Name java

# Detén el proceso (reemplaza 12556 con el ID correcto)
Stop-Process -Id 12556
```

**Reiniciar:**
```powershell
mvn spring-boot:run
```

**Espera a ver estos mensajes:**
```
✅ Started ReservasApplication
✅ Hibernate creating/updating tables
```

---

### 6. Probar los endpoints

**Opción A: Desde el navegador**
Abre: `http://localhost:9090/api/clientes`

Deberías ver: `[]` (array vacío si no hay datos)

**Opción B: Desde PowerShell**
```powershell
Invoke-WebRequest -Uri "http://localhost:9090/api/clientes" -UseBasicParsing
```

**Opción C: Con Postman**
- Método: GET
- URL: `http://localhost:9090/api/clientes`
- Debe retornar 200 OK con `[]`

---

## 🐛 Solución de Problemas

### Error: "Access denied for user 'root'@'localhost'"
**Solución**: Verifica la contraseña en `application.properties`

### Error: "Unknown database 'reservas'"
**Solución**: 
1. Ejecuta: `database/setup.sql` o
2. Ejecuta: `database/crear_tablas_completo.sql`

### Error: "Can't connect to MySQL server"
**Solución**: 
1. Verifica que el servicio MySQL esté corriendo
2. Verifica que el puerto sea 3306

### Endpoints retornan 404
**Posibles causas:**
1. La aplicación aún está arrancando (espera 30 segundos)
2. Error de conexión a la base de datos (revisa los logs)
3. Las tablas no existen (JPA no puede crear las entidades)

**Solución:**
1. Revisa los logs de la aplicación en la consola
2. Busca errores relacionados con MySQL o JPA
3. Verifica que las tablas existan en MySQL

---

## ✅ Checklist de Verificación

- [ ] Servicio MySQL corriendo
- [ ] Base de datos `reservas` existe
- [ ] Tablas creadas (clientes, mesas, reservas)
- [ ] `application.properties` configurado correctamente
- [ ] Contraseña de MySQL correcta (o vacía)
- [ ] Aplicación Spring Boot ejecutándose
- [ ] Logs muestran "Started ReservasApplication"
- [ ] Endpoint `/api/clientes` responde (200 OK o lista vacía)

---

## 🎯 Próximos Pasos

Una vez que la conexión funcione:

1. **Probar crear un cliente:**
   ```json
   POST http://localhost:9090/api/clientes
   {
     "nombre": "Juan Pérez",
     "telefono": "555-1234"
   }
   ```

2. **Probar crear una mesa:**
   ```json
   POST http://localhost:9090/api/mesas
   {
     "numeroMesa": 1,
     "capacidad": 4,
     "estado": "DISPONIBLE"
   }
   ```

3. **Probar crear una reserva:**
   ```json
   POST http://localhost:9090/api/reservas
   {
     "fecha": "2025-12-10",
     "hora": "19:00:00",
     "cliente": { "idCliente": 1 },
     "mesa": { "idMesa": 1 },
     "estado": "ACTIVA"
   }
   ```

---

## 📝 Notas

- Si JPA tiene `ddl-auto=update`, las tablas se crearán automáticamente
- Si prefieres crear las tablas manualmente, usa los scripts SQL
- Los logs de Spring Boot mostrarán los errores si hay problemas de conexión

