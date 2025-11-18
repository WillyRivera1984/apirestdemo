# 🎹 SCRIPT PARA CREAR TECLADO MECÁNICO EN POSTMAN

## 📋 REQUISITOS PREVIOS

Antes de crear un producto, **DEBES estar autenticado**. Sigue estos pasos:

### PASO 1: Registrar Usuario (si no tienes)
```
POST http://localhost:8085/api/v1/demoapirestdam235/auth/register

Headers:
Content-Type: application/json

Body (raw - JSON):
{
  "name": "Admin",
  "lastName": "Usuario",
  "email": "admin@empresa.com",
  "password": "Admin123456",
  "confirmPassword": "Admin123456"
}
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "id": 1,
    "name": "Admin",
    "lastName": "Usuario",
    "email": "admin@empresa.com"
  }
}
```

---

### PASO 2: Hacer Login (obtener token)
```
POST http://localhost:8085/api/v1/demoapirestdam235/auth/login

Headers:
Content-Type: application/json

Body (raw - JSON):
{
  "user": "admin@empresa.com",
  "pass": "Admin123456"
}
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Inicio de sesión exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBlbXByZXNhLmNvbSIsImlhdCI6MTczMTg0ODQ0MCwiZXhwIjoxNzMxODUwMjQwLCJhdXRob3JpdGllcyI6W119.abc123...",
    "expireIn": 1800000,
    "msj": "Token valido por 30 minutos"
  }
}
```

**⚠️ IMPORTANTE:** Copia el valor del token completo (la cadena larga de `eyJ...`)

---

## 🎹 PASO 3: CREAR TECLADO MECÁNICO (CON TOKEN)

### URL:
```
POST http://localhost:8085/api/v1/demoapirestdam235/products
```

### Headers:
```
Content-Type: application/json
Authorization: Bearer {pegar_token_aqui}
```

### Body (raw - JSON):
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
    "code": 1,
    "name": "Teclado Mecánico RGB",
    "status": true
  }
}
```

---

## 📊 RESUMEN VISUAL DEL FLUJO

```
┌────────────────────────────────────────────────────────┐
│ 1️⃣  REGISTRAR USUARIO                                 │
├────────────────────────────────────────────────────────┤
│ POST /auth/register                                    │
│ (SIN autenticación)                                    │
│                                                        │
│ Body:                                                  │
│ {                                                      │
│   "name": "Admin",                                     │
│   "lastName": "Usuario",                               │
│   "email": "admin@empresa.com",                        │
│   "password": "Admin123456",                           │
│   "confirmPassword": "Admin123456"                     │
│ }                                                      │
│                                                        │
│ ✅ Respuesta: 201 Created                             │
└────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────┐
│ 2️⃣  HACER LOGIN                                       │
├────────────────────────────────────────────────────────┤
│ POST /auth/login                                       │
│ (SIN autenticación)                                    │
│                                                        │
│ Body:                                                  │
│ {                                                      │
│   "user": "admin@empresa.com",                         │
│   "pass": "Admin123456"                                │
│ }                                                      │
│                                                        │
│ ✅ Respuesta: 200 OK + Token JWT                      │
│ 💾 GUARDAR el token                                    │
└────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────┐
│ 3️⃣  CREAR TECLADO MECÁNICO                             │
├────────────────────────────────────────────────────────┤
│ POST /products                                         │
│ (CON autenticación - Bearer Token)                     │
│                                                        │
│ Headers:                                               │
│ Authorization: Bearer eyJ...{token_aqui}...            │
│ Content-Type: application/json                         │
│                                                        │
│ Body:                                                  │
│ {                                                      │
│   "name": "Teclado Mecánico RGB",                      │
│   "status": true                                       │
│ }                                                      │
│                                                        │
│ ✅ Respuesta: 201 Created + Producto creado           │
└────────────────────────────────────────────────────────┘
```

---

## 🎹 VARIANTES: DIFERENTES TIPOS DE TECLADOS

### Opción 1: Teclado Gaming RGB
```json
{
  "name": "Teclado Gaming RGB Mecánico Inalámbrico",
  "status": true
}
```

### Opción 2: Teclado Mecánico 75%
```json
{
  "name": "Teclado Mecánico 75% Compacto",
  "status": true
}
```

### Opción 3: Teclado Mecánico 65%
```json
{
  "name": "Teclado Mecánico 65% Keycaps Personalizados",
  "status": true
}
```

### Opción 4: Teclado Mecánico Inalámbrico
```json
{
  "name": "Teclado Mecánico RGB Inalámbrico Bluetooth",
  "status": true
}
```

### Opción 5: Teclado Mecánico TKL
```json
{
  "name": "Teclado Mecánico TKL RGB Switches Azules",
  "status": true
}
```

### Opción 6: Teclado Inactivo
```json
{
  "name": "Teclado Mecánico Descontinuado",
  "status": false
}
```

---

## ⚠️ ERRORES COMUNES Y SOLUCIONES

### Error: "Token no proporcionado o inválido"
**Causa:** No agregaste el token en el header Authorization
**Solución:** 
1. Copia el token del paso 2 (Login)
2. En Headers agrega: `Authorization: Bearer {token_aqui}`

### Error: "El nombre del producto es requerido"
**Causa:** El campo `name` está vacío
**Solución:** Asegúrate que `"name"` tenga valor

### Error: "El nombre del producto debe tener al menos 3 caracteres"
**Causa:** El nombre es muy corto (menos de 3 caracteres)
**Solución:** Escribe un nombre con al menos 3 caracteres

### Error: "El nombre del producto no puede exceder 255 caracteres"
**Causa:** El nombre es muy largo (más de 255 caracteres)
**Solución:** Reduce el nombre a máximo 255 caracteres

### Error: "401 Unauthorized"
**Causa:** 
- El token expiró (tiene 30 minutos de validez)
- El token está revocado (hiciste logout)
**Solución:** Haz login nuevamente para obtener un nuevo token

---

## 💾 SCRIPT COMPLETO DE COPIAR Y PEGAR

```
═══════════════════════════════════════════════════════════════

PASO 1: REGISTRAR USUARIO
═══════════════════════════════════════════════════════════════

POST http://localhost:8085/api/v1/demoapirestdam235/auth/register

Headers:
Content-Type: application/json

Body:
{
  "name": "Admin",
  "lastName": "Usuario",
  "email": "admin@empresa.com",
  "password": "Admin123456",
  "confirmPassword": "Admin123456"
}

═══════════════════════════════════════════════════════════════

PASO 2: HACER LOGIN
═══════════════════════════════════════════════════════════════

POST http://localhost:8085/api/v1/demoapirestdam235/auth/login

Headers:
Content-Type: application/json

Body:
{
  "user": "admin@empresa.com",
  "pass": "Admin123456"
}

GUARDAR el token de la respuesta

═══════════════════════════════════════════════════════════════

PASO 3: CREAR TECLADO MECÁNICO
═══════════════════════════════════════════════════════════════

POST http://localhost:8085/api/v1/demoapirestdam235/products

Headers:
Content-Type: application/json
Authorization: Bearer {pegar_token_aqui}

Body:
{
  "name": "Teclado Mecánico RGB",
  "status": true
}

═══════════════════════════════════════════════════════════════
```

---

## 🔄 SECUENCIA AUTOMÁTICA EN POSTMAN

Para automatizar todo el proceso, ve a cada request en la tab **Tests** y agrega:

### En el request de LOGIN (Tests):
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("authToken", jsonData.data.token);
    console.log("✅ Token guardado automáticamente");
}
```

### En el request de CREATE PRODUCT (Headers):
En lugar de pegar manualmente el token, usa:
```
Authorization: Bearer {{authToken}}
```

---

## 📊 VERIFICACIÓN

Después de crear el teclado, puedes verificar que se guardó:

```
GET http://localhost:8085/api/v1/demoapirestdam235/products

Headers:
Authorization: Bearer {{authToken}}

Respuesta esperada:
{
  "success": true,
  "message": "Productos obtenidos correctamente",
  "data": [
    {
      "code": 1,
      "name": "Teclado Mecánico RGB",
      "status": true
    }
  ]
}
```

---

## ✅ CHECKLIST FINAL

- [ ] ¿Registraste el usuario correctamente?
- [ ] ¿Obtuviste el token del login?
- [ ] ¿Copiaste el token completo?
- [ ] ¿Agregaste el header `Authorization: Bearer {token}`?
- [ ] ¿El nombre del teclado tiene al menos 3 caracteres?
- [ ] ¿El nombre no excede 255 caracteres?
- [ ] ¿El status es `true` o `false`?
- [ ] ¿Recibiste respuesta 201 Created?

---

**¿Necesitas ayuda con algo más?** 😊
