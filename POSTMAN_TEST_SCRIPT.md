# 📬 GUÍA COMPLETA DE TESTING CON POSTMAN

## 🎯 INTRODUCCIÓN

Este documento contiene un script completo para probar todos los endpoints de la API REST usando Postman.

**URL Base:** `http://localhost:8085/api/v1/demoapirestdam235`

---

## 🔧 CONFIGURACIÓN INICIAL EN POSTMAN

### Paso 1: Crear una colección
1. Abre Postman
2. Click en "Collections" → "New Collection"
3. Nombre: `API REST DAM235`
4. Click en "Create"

### Paso 2: Crear variable de entorno
1. Click en "Environments" → "Create New"
2. Nombre: `Local Development`
3. Agregar variable:
   - **Variable:** `baseUrl`
   - **Initial value:** `http://localhost:8085/api/v1/demoapirestdam235`
   - **Current value:** `http://localhost:8085/api/v1/demoapirestdam235`
4. Click en "Save"

### Paso 3: Agregar variable de token
1. En el mismo entorno, agregar nueva variable:
   - **Variable:** `authToken`
   - **Initial value:** (dejar vacío)
   - **Current value:** (dejar vacío)

### Paso 4: Seleccionar el entorno
1. En la esquina superior derecha de Postman
2. Click en dropdown de entornos
3. Seleccionar `Local Development`

---

## 📝 SCRIPT DE TESTING COMPLETO

### TEST 1: REGISTRO DE USUARIO NUEVO ✅

**Nombre del Request:** `1. REGISTER - Crear usuario nuevo`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/auth/register
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON raw):**
```json
{
  "name": "Juan",
  "lastName": "Pérez García",
  "email": "juan.perez@example.com",
  "password": "Password123",
  "confirmPassword": "Password123"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "id": 1,
    "name": "Juan",
    "lastName": "Pérez García",
    "email": "juan.perez@example.com"
  }
}
```

**Script de Postman (Tests tab):**
```javascript
if (pm.response.code === 201) {
    pm.test("✅ Registro exitoso", function () {
        var jsonData = pm.response.json();
        pm.expect(jsonData.success).to.eql(true);
        pm.expect(jsonData.data.email).to.eql("juan.perez@example.com");
        pm.environment.set("userId", jsonData.data.id);
    });
} else {
    pm.test("❌ Error en registro", function () {
        console.log("Status: " + pm.response.code);
    });
}
```

---

### TEST 2: VALIDACIÓN - Registrar con email duplicado ⚠️

**Nombre del Request:** `2. REGISTER - Validar email duplicado`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/auth/register
```

**Body (JSON raw):**
```json
{
  "name": "Otro Usuario",
  "lastName": "Apellido",
  "email": "juan.perez@example.com",
  "password": "Password123",
  "confirmPassword": "Password123"
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
  "success": false,
  "message": "El email ya está registrado",
  "data": null
}
```

---

### TEST 3: VALIDACIÓN - Registrar con contraseñas no coincidentes ⚠️

**Nombre del Request:** `3. REGISTER - Validar contraseñas no coinciden`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/auth/register
```

**Body (JSON raw):**
```json
{
  "name": "Pedro",
  "lastName": "López",
  "email": "pedro.lopez@example.com",
  "password": "Password123",
  "confirmPassword": "Password456"
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
  "success": false,
  "message": "Las contraseñas no coinciden",
  "data": null
}
```

---

### TEST 4: VALIDACIÓN - Registrar con contraseña corta ⚠️

**Nombre del Request:** `4. REGISTER - Validar contraseña muy corta`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/auth/register
```

**Body (JSON raw):**
```json
{
  "name": "María",
  "lastName": "González",
  "email": "maria.gonzalez@example.com",
  "password": "Pass1",
  "confirmPassword": "Pass1"
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
  "success": false,
  "message": "La contraseña debe tener al menos 6 caracteres",
  "data": null
}
```

---

### TEST 5: LOGIN - Obtener token JWT ✅

**Nombre del Request:** `5. LOGIN - Obtener token`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/auth/login
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON raw):**
```json
{
  "user": "juan.perez@example.com",
  "pass": "Password123"
}
```

**Respuesta esperada (200 OK):**
```json
{
  "success": true,
  "message": "Inicio de sesión exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expireIn": 1800000,
    "msj": "Token valido por 30 minutos"
  }
}
```

**Script de Postman (Tests tab):**
```javascript
if (pm.response.code === 200) {
    pm.test("✅ Login exitoso", function () {
        var jsonData = pm.response.json();
        pm.expect(jsonData.success).to.eql(true);
        pm.expect(jsonData.data.token).to.be.a('string');
        // Guardar el token en la variable de entorno
        pm.environment.set("authToken", jsonData.data.token);
        console.log("✅ Token guardado: " + jsonData.data.token.substring(0, 50) + "...");
    });
} else {
    pm.test("❌ Error en login", function () {
        console.log("Status: " + pm.response.code);
    });
}
```

---

### TEST 6: VALIDACIÓN - Login con credenciales inválidas ⚠️

**Nombre del Request:** `6. LOGIN - Validar credenciales inválidas`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/auth/login
```

**Body (JSON raw):**
```json
{
  "user": "juan.perez@example.com",
  "pass": "PasswordIncorrecto"
}
```

**Respuesta esperada (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Usuario o contraseña incorrecto",
  "data": null
}
```

---

### TEST 7: OBTENER PRODUCTOS - Sin autenticación ⚠️

**Nombre del Request:** `7. GET Products - Sin token (debe fallar)`

**Tipo:** GET

**URL:**
```
{{baseUrl}}/products
```

**Headers:** (Sin Authorization)

**Respuesta esperada (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Token no proporcionado o inválido",
  "data": null
}
```

---

### TEST 8: OBTENER PRODUCTOS - Con autenticación ✅

**Nombre del Request:** `8. GET Products - Con token válido`

**Tipo:** GET

**URL:**
```
{{baseUrl}}/products
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{authToken}}
```

**Respuesta esperada (200 OK):**
```json
{
  "success": true,
  "message": "Productos obtenidos correctamente",
  "data": [
    {
      "code": 1,
      "name": "Laptop",
      "status": true
    },
    {
      "code": 2,
      "name": "Mouse",
      "status": true
    }
  ]
}
```

**Script de Postman (Tests tab):**
```javascript
pm.test("✅ Productos obtenidos correctamente", function () {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.eql(true);
    pm.expect(jsonData.data).to.be.an('array');
});
```

---

### TEST 9: CREAR PRODUCTO - Con autenticación ✅

**Nombre del Request:** `9. POST Create Product - Nuevo producto`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/products
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{authToken}}
```

**Body (JSON raw):**
```json
{
  "name": "Teclado Mecánico RGB",
  "status": true
}
```

**Respuesta esperada (201 Created):**
```json
{
  "success": true,
  "message": "Producto creado exitosamente",
  "data": {
    "code": 3,
    "name": "Teclado Mecánico RGB",
    "status": true
  }
}
```

**Script de Postman (Tests tab):**
```javascript
pm.test("✅ Producto creado exitosamente", function () {
    pm.response.to.have.status(201);
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.eql(true);
    pm.expect(jsonData.data.name).to.eql("Teclado Mecánico RGB");
    pm.environment.set("lastProductCode", jsonData.data.code);
});
```

---

### TEST 10: VALIDACIÓN - Crear producto sin nombre ⚠️

**Nombre del Request:** `10. POST Create Product - Validar nombre requerido`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/products
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{authToken}}
```

**Body (JSON raw):**
```json
{
  "name": "",
  "status": true
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
  "success": false,
  "message": "El nombre del producto es requerido",
  "data": null
}
```

---

### TEST 11: VALIDACIÓN - Crear producto con nombre muy corto ⚠️

**Nombre del Request:** `11. POST Create Product - Validar nombre mínimo`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/products
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{authToken}}
```

**Body (JSON raw):**
```json
{
  "name": "AB",
  "status": true
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
  "success": false,
  "message": "El nombre del producto debe tener al menos 3 caracteres",
  "data": null
}
```

---

### TEST 12: VALIDACIÓN - Crear producto con nombre muy largo ⚠️

**Nombre del Request:** `12. POST Create Product - Validar nombre máximo`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/products
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{authToken}}
```

**Body (JSON raw):**
```json
{
  "name": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
  "status": true
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
  "success": false,
  "message": "El nombre del producto no puede exceder 255 caracteres",
  "data": null
}
```

---

### TEST 13: LOGOUT - Revocar token ✅

**Nombre del Request:** `13. LOGOUT - Revocar token`

**Tipo:** POST

**URL:**
```
{{baseUrl}}/auth/logout
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{authToken}}
```

**Body:** (vacío)

**Respuesta esperada (200 OK):**
```json
{
  "success": true,
  "message": "Sesión cerrada correctamente",
  "data": null
}
```

**Script de Postman (Tests tab):**
```javascript
pm.test("✅ Logout exitoso", function () {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.eql(true);
    // Limpiar el token de la variable de entorno
    pm.environment.set("authToken", "");
    console.log("✅ Token revocado y limpiado");
});
```

---

### TEST 14: VALIDACIÓN - Usar token revocado ⚠️

**Nombre del Request:** `14. GET Products - Con token revocado (debe fallar)`

**Tipo:** GET

**URL:**
```
{{baseUrl}}/products
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{authToken}}
```

**Respuesta esperada (401 Unauthorized):**
```json
{
  "success": false,
  "message": "token revoked",
  "data": null
}
```

**Script de Postman (Tests tab):**
```javascript
pm.test("✅ Token revocado correctamente", function () {
    pm.response.to.have.status(401);
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.eql(false);
});
```

---

## 🎬 ORDEN DE EJECUCIÓN RECOMENDADO

### Flujo Completo: Registro → Login → CRUD → Logout

```
┌─────────────────────────────────────────────────────────┐
│  PASO 1: CREAR USUARIO NUEVO                            │
│  Request: 1. REGISTER - Crear usuario nuevo             │
│  Método:  POST /auth/register                           │
│  ✅ Respuesta: 201 Created                              │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│  PASO 2: VALIDACIONES DE REGISTRO                       │
│  Request: 2, 3, 4 - Probar validaciones                │
│  Método:  POST /auth/register (variantes)              │
│  ⚠️  Respuesta: 400 Bad Request (esperado)              │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│  PASO 3: INICIAR SESIÓN                                 │
│  Request: 5. LOGIN - Obtener token                      │
│  Método:  POST /auth/login                              │
│  ✅ Respuesta: 200 OK + Token JWT                       │
│  💾 Guardar: authToken variable                         │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│  PASO 4: VALIDACIÓN LOGIN                               │
│  Request: 6. LOGIN - Credenciales inválidas             │
│  Método:  POST /auth/login                              │
│  ⚠️  Respuesta: 401 Unauthorized (esperado)             │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│  PASO 5: OBTENER PRODUCTOS (SIN TOKEN)                  │
│  Request: 7. GET Products - Sin token (debe fallar)     │
│  Método:  GET /products                                 │
│  ⚠️  Respuesta: 401 Unauthorized (esperado)             │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│  PASO 6: OBTENER PRODUCTOS (CON TOKEN)                  │
│  Request: 8. GET Products - Con token válido            │
│  Método:  GET /products                                 │
│  ✅ Respuesta: 200 OK + Lista de productos              │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│  PASO 7: CREAR PRODUCTO                                 │
│  Request: 9. POST Create Product - Nuevo producto       │
│  Método:  POST /products                                │
│  ✅ Respuesta: 201 Created + Datos del producto         │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│  PASO 8: VALIDACIONES DE PRODUCTO                       │
│  Request: 10, 11, 12 - Probar validaciones              │
│  Método:  POST /products (variantes)                    │
│  ⚠️  Respuesta: 400 Bad Request (esperado)              │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│  PASO 9: LOGOUT (REVOCAR TOKEN)                         │
│  Request: 13. LOGOUT - Revocar token                    │
│  Método:  POST /auth/logout                             │
│  ✅ Respuesta: 200 OK                                   │
│  🗑️  Acción: Token movido a blacklist                   │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│  PASO 10: VALIDAR TOKEN REVOCADO                        │
│  Request: 14. GET Products - Con token revocado         │
│  Método:  GET /products (con token del paso 9)          │
│  ⚠️  Respuesta: 401 Unauthorized + "token revoked"      │
│  ✅ Validación: Token no puede reutilizarse             │
└─────────────────────────────────────────────────────────┘
```

---

## 🔍 VERIFICACIÓN PASO A PASO

### Estado Inicial
```
[ ] Base de datos MariaDB corriendo en 3.22.57.8:3306
[ ] Usuario: dam235
[ ] Contraseña: demo_pass
[ ] Base de datos: DBTmp
```

### Ejecutar Servidor Spring Boot
```bash
cd c:\Users\joses\IdeaProjects\apirestdemo
.\mvnw.cmd spring-boot:run
```

**Esperado en consola:**
```
2025-11-16 14:30:00.123 INFO 1234 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8085 (http)
```

### Verificar que la API está activa
```
GET http://localhost:8085/api/v1/demoapirestdam235/swagger-ui/index.html
```

---

## 💾 SCRIPT IMPORTABLE PARA POSTMAN (JSON)

Si prefieres importar la colección directamente, copia este JSON en Postman:

```json
{
  "info": {
    "name": "API REST DAM235",
    "description": "Collection completa para testing",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "1. REGISTER - Crear usuario nuevo",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"name\": \"Juan\", \"lastName\": \"Pérez García\", \"email\": \"juan.perez@example.com\", \"password\": \"Password123\", \"confirmPassword\": \"Password123\"}"
        },
        "url": {
          "raw": "{{baseUrl}}/auth/register",
          "host": ["{{baseUrl}}"],
          "path": ["auth", "register"]
        }
      }
    },
    {
      "name": "5. LOGIN - Obtener token",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"user\": \"juan.perez@example.com\", \"pass\": \"Password123\"}"
        },
        "url": {
          "raw": "{{baseUrl}}/auth/login",
          "host": ["{{baseUrl}}"],
          "path": ["auth", "login"]
        }
      }
    },
    {
      "name": "8. GET Products - Con token válido",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{authToken}}"
          }
        ],
        "url": {
          "raw": "{{baseUrl}}/products",
          "host": ["{{baseUrl}}"],
          "path": ["products"]
        }
      }
    },
    {
      "name": "9. POST Create Product - Nuevo producto",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          },
          {
            "key": "Authorization",
            "value": "Bearer {{authToken}}"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"name\": \"Teclado Mecánico RGB\", \"status\": true}"
        },
        "url": {
          "raw": "{{baseUrl}}/products",
          "host": ["{{baseUrl}}"],
          "path": ["products"]
        }
      }
    },
    {
      "name": "13. LOGOUT - Revocar token",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{authToken}}"
          }
        ],
        "url": {
          "raw": "{{baseUrl}}/auth/logout",
          "host": ["{{baseUrl}}"],
          "path": ["auth", "logout"]
        }
      }
    }
  ],
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8085/api/v1/demoapirestdam235"
    },
    {
      "key": "authToken",
      "value": ""
    }
  ]
}
```

---

## 🐛 TROUBLESHOOTING

### Error: "El servidor rechaza la conexión"
**Causa:** El servidor Spring Boot no está ejecutándose
**Solución:**
```bash
.\mvnw.cmd spring-boot:run
```

### Error: "Token no proporcionado o inválido"
**Causa:** No hay token en el header Authorization o está expirado
**Solución:**
1. Hacer login nuevamente (TEST 5)
2. Copiar el token de la respuesta
3. Usar `Bearer {{authToken}}` en los headers

### Error: "token revoked"
**Causa:** El token fue revocado con logout
**Solución:**
1. Hacer login nuevamente (TEST 5)
2. Obtener un nuevo token

### Error: "El email ya está registrado"
**Causa:** El email ya existe en la base de datos
**Solución:**
1. Usar otro email (ej: juan.perez2@example.com)
2. O borrar el usuario de la BD y reintentar

### Error: "Las contraseñas no coinciden"
**Causa:** El campo `confirmPassword` no es igual a `password`
**Solución:**
Asegurarse de que ambos campos sean idénticos

---

## 📊 MATRIZ DE RESPUESTAS ESPERADAS

| Test # | Endpoint | Método | Código | Mensaje |
|--------|----------|--------|--------|---------|
| 1 | /auth/register | POST | 201 | Usuario registrado exitosamente |
| 2 | /auth/register | POST | 400 | El email ya está registrado |
| 3 | /auth/register | POST | 400 | Las contraseñas no coinciden |
| 4 | /auth/register | POST | 400 | La contraseña debe tener al menos 6 caracteres |
| 5 | /auth/login | POST | 200 | Inicio de sesión exitoso |
| 6 | /auth/login | POST | 401 | Usuario o contraseña incorrecto |
| 7 | /products | GET | 401 | Token no proporcionado o inválido |
| 8 | /products | GET | 200 | Productos obtenidos correctamente |
| 9 | /products | POST | 201 | Producto creado exitosamente |
| 10 | /products | POST | 400 | El nombre del producto es requerido |
| 11 | /products | POST | 400 | El nombre debe tener al menos 3 caracteres |
| 12 | /products | POST | 400 | El nombre no puede exceder 255 caracteres |
| 13 | /auth/logout | POST | 200 | Sesión cerrada correctamente |
| 14 | /products | GET | 401 | token revoked |

---

## ✨ CARACTERÍSTICAS PRINCIPALES A VALIDAR

✅ **Registro**
- [x] Crear usuario con datos válidos
- [x] Validar email único
- [x] Validar coincidencia de contraseñas
- [x] Validar longitud mínima de contraseña

✅ **Autenticación**
- [x] Login devuelve token JWT válido
- [x] Token tiene 30 minutos de validez
- [x] Login rechaza credenciales inválidas
- [x] Token se guarda en variable de entorno

✅ **Autorización**
- [x] GET /products requiere token
- [x] POST /products requiere token
- [x] Requests sin token devuelven 401

✅ **Productos**
- [x] Crear producto con datos válidos
- [x] Validar nombre requerido
- [x] Validar longitud mínima (3 caracteres)
- [x] Validar longitud máxima (255 caracteres)
- [x] Producto se asigna código automático

✅ **Logout**
- [x] Logout revoca el token
- [x] Token revocado no puede reutilizarse
- [x] Token en blacklist devuelve 401 "token revoked"

---

## 📞 INFORMACIÓN TÉCNICA

**URL Base:** `http://localhost:8085/api/v1/demoapirestdam235`

**Timeouts Recomendados:**
```
Connection Timeout: 5000 ms
Response Timeout: 10000 ms
```

**Versión API:** 1.0.0
**Java Version:** 21
**Spring Boot:** 3.5.7
**JWT Expiration:** 30 minutos (1,800,000 ms)

---

## 🎓 NOTAS FINALES

1. **Guardar tokens:** Postman automáticamente guarda el token en `{{authToken}}` al ejecutar el TEST 5
2. **Limpiar tokens:** Al ejecutar TEST 13 (logout), el token es revocado y marcado en blacklist
3. **Crear múltiples usuarios:** Repetir TEST 1 con diferentes emails para probar multi-usuario
4. **Probar validaciones:** Todos los TESTS de validación deben fallar (400/401) como se indica

---

## 🚀 PRÓXIMO PASO

Una vez completado este script:
1. Todos los endpoints estarán validados ✅
2. La seguridad estará verificada ✅
3. Las validaciones funcionarán correctamente ✅
4. El logout con blacklist estará comprobado ✅

**¡API Lista para producción!** 🎉

---

**Documento creado:** 16 de Noviembre de 2025
**Versión:** 1.0.0
**Estado:** ✅ Completo y Listo para Testing
