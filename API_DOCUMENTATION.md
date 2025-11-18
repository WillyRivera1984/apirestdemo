# API REST Demo - Documentación

## 📋 Descripción General

API REST desarrollada con Spring Boot 3.5.7 que proporciona autenticación con JWT, gestión de usuarios y gestión de productos.

**Servidor:** `http://localhost:8085`
**Context Path:** `/api/v1/demoapirestdam235`

---

## 🔐 Endpoints de Autenticación

### 1. Login (Iniciar Sesión)

**Endpoint:** `POST /auth/login`

**URL Completa:** `http://localhost:8085/api/v1/demoapirestdam235/auth/login`

**Descripción:** Autentica un usuario y retorna un token JWT válido por 30 minutos.

**Headers:**
```
Content-Type: application/json
```

**Body (Request):**
```json
{
  "user": "juan@example.com",
  "pass": "password123"
}
```

**Response Success (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expireIn": "1800000",
  "msj": null
}
```

**Response Error (401 Unauthorized):**
```
Empty body with 401 status
```

---

### 2. Register (Registrar Nuevo Usuario)

**Endpoint:** `POST /auth/register`

**URL Completa:** `http://localhost:8085/api/v1/demoapirestdam235/auth/register`

**Descripción:** Registra un nuevo usuario en el sistema.

**Headers:**
```
Content-Type: application/json
```

**Body (Request):**
```json
{
  "name": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}
```

**Response Success (201 Created):**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "id": 1,
    "name": "Juan",
    "lastName": "Pérez",
    "email": "juan@example.com"
  }
}
```

**Response Error (400 Bad Request):**
```json
{
  "success": false,
  "message": "El correo electrónico ya está registrado"
}
```

**Validaciones:**
- ✅ Todos los campos son obligatorios
- ✅ Las contraseñas deben coincidir
- ✅ Contraseña mínimo 6 caracteres
- ✅ El email no puede estar registrado
- ✅ La contraseña se encripta antes de guardarse

---

### 3. Logout (Cerrar Sesión)

**Endpoint:** `POST /auth/logout`

**URL Completa:** `http://localhost:8085/api/v1/demoapirestdam235/auth/logout`

**Descripción:** Cierra la sesión del usuario revocando el token JWT agregándolo a una lista negra.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Body (Request):**
```
Empty - El token se envía en el header Authorization
```

**Response Success (200 OK):**
```json
{
  "success": true,
  "message": "Sesión cerrada exitosamente",
  "data": null
}
```

**Response Error (400 Bad Request):**
```json
{
  "success": false,
  "message": "Token no proporcionado"
}
```

**Características:**
- 🔒 Sistema de lista negra (blacklist) de tokens
- 🚫 Los tokens revocados no pueden usarse nuevamente
- ⏱️ Válido solo para tokens no expirados

---

## 📦 Endpoints de Productos

### 1. Obtener Todos los Productos

**Endpoint:** `GET /products`

**URL Completa:** `http://localhost:8085/api/v1/demoapirestdam235/products`

**Descripción:** Obtiene la lista de todos los productos registrados.

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Response Success (200 OK):**
```json
[
  {
    "code": 1,
    "name": "Producto 1",
    "status": true
  },
  {
    "code": 2,
    "name": "Producto 2",
    "status": true
  }
]
```

**Response Empty (204 No Content):**
```
Empty body with 204 status
```

---

### 2. Crear Nuevo Producto

**Endpoint:** `POST /products`

**URL Completa:** `http://localhost:8085/api/v1/demoapirestdam235/products`

**Descripción:** Crea un nuevo producto en el sistema.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Body (Request):**
```json
{
  "name": "Nuevo Producto",
  "status": true
}
```

**Response Success (201 Created):**
```json
{
  "success": true,
  "message": "Producto creado exitosamente",
  "data": {
    "code": 3,
    "name": "Nuevo Producto",
    "status": true
  }
}
```

**Response Error (400 Bad Request):**
```json
{
  "success": false,
  "message": "El nombre del producto es requerido"
}
```

**Validaciones:**
- ✅ Nombre es obligatorio
- ✅ Nombre mínimo 3 caracteres
- ✅ Nombre máximo 255 caracteres
- ✅ Estado (status) es booleano

---

## 🔑 Sistema de Autenticación JWT

### Características:

- ✅ **Token JWT** con duración de 30 minutos (1,800,000 ms)
- ✅ **Encriptación HS256** con clave secreta
- ✅ **Lista Negra (Blacklist)** para tokens revocados
- ✅ **Validación de Token** en cada petición protegida

### Flujo de Autenticación:

1. **Login**: Usuario envía credenciales → Recibe token JWT
2. **Uso**: Token se envía en header `Authorization: Bearer <token>`
3. **Validación**: Cada solicitud valida:
   - ✅ Formato correcto del token
   - ✅ Token no expirado
   - ✅ Token no está en lista negra
4. **Logout**: Token se agrega a la lista negra → No se puede usar más

---

## 🛡️ Seguridad

### Rutas Públicas (Sin Autenticación):
- `POST /auth/login` - Login
- `POST /auth/register` - Registro
- `/swagger-ui/*` - Documentación Swagger
- `/v3/*` - OpenAPI

### Rutas Protegidas (Requieren Token JWT):
- `GET /products` - Obtener productos
- `POST /auth/logout` - Logout

---

## 📊 Modelo de Datos

### User (Usuario)
```
- id: Integer (Auto-incremento)
- name: String (Nombre)
- lastName: String (Apellido)
- email: String (Email - Único)
- password: String (Encriptada)
- active: Boolean (Activo/Inactivo)
```

### Product (Producto)
```
- code: Integer (Auto-incremento)
- name: String (Nombre)
- status: Boolean (Activo/Inactivo)
```

---

## 🧪 Ejemplos con cURL

### 1. Registrar Usuario
```bash
curl -X POST http://localhost:8085/api/v1/demoapirestdam235/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan",
    "lastName": "Pérez",
    "email": "juan@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8085/api/v1/demoapirestdam235/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "user": "juan@example.com",
    "pass": "password123"
  }'
```

### 3. Obtener Productos
```bash
curl -X GET http://localhost:8085/api/v1/demoapirestdam235/products \
  -H "Authorization: Bearer <TOKEN_AQUI>"
```

### 4. Crear Producto
```bash
curl -X POST http://localhost:8085/api/v1/demoapirestdam235/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_AQUI>" \
  -d '{
    "name": "Mi Nuevo Producto",
    "status": true
  }'
```

### 5. Logout
```bash
curl -X POST http://localhost:8085/api/v1/demoapirestdam235/auth/logout \
  -H "Authorization: Bearer <TOKEN_AQUI>"
```

---

## ⚙️ Configuración

**Archivo:** `application.properties`

```properties
server.port=8085
server.servlet.context-path=/api/v1/demoapirestdam235

# Base de Datos
spring.datasource.url=jdbc:mariadb://3.22.57.8:3306/DBTmp
spring.datasource.username=dam235
spring.datasource.password=demo_pass
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.MariaDBDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logs
logging.level.root=INFO
logging.level.org.hibernate.SQL=DEBUG
```

---

## 🔧 Tecnologías

- **Framework:** Spring Boot 3.5.7
- **Seguridad:** Spring Security + JWT (JJWT)
- **Base de Datos:** MariaDB
- **ORM:** Hibernate / JPA
- **Build:** Maven
- **Java:** Version 21

---

## 📝 Notas Importantes

1. **Token Expiration:** Los tokens expiran después de 30 minutos
2. **Blacklist:** La lista negra se mantiene en memoria durante la ejecución
3. **Contraseña:** Se encripta usando Spring Security's `PasswordEncoder`
4. **CORS:** Habilitado para todas las fuentes
5. **Validación:** Email único por usuario

---

## 🐛 Solución de Problemas

### Error: "Credenciales incorrectas"
- Verifica que el usuario esté registrado
- Comprueba que la contraseña es correcta
- Asegúrate que el usuario esté activo

### Error: "No autorizado: Token no es el correcto"
- El token es inválido o ha expirado
- Intenta hacer login nuevamente
- Comprueba el header `Authorization`

### Error: "No autorizado: El token ha sido revocado (logout)"
- Has cerrado sesión con este token
- Debes hacer login nuevamente para obtener un nuevo token

---

Generado: Noviembre 16, 2025
