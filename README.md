# API REST Demo - Spring Boot

## 📖 Descripción

API REST completa para gestión de usuarios y productos con autenticación JWT, registro de usuarios y sistema de logout con lista negra.

**Estado:** ✅ Completado y Compilado
**Última Actualización:** 16 de Noviembre de 2025

---

## 🎯 Características Principales

### ✅ Autenticación
- 🔐 Login con credenciales
- 📝 Registro de nuevos usuarios
- 🚪 Logout con revocación de tokens
- 🔒 Tokens JWT (30 minutos de validez)
- 🚫 Lista negra de tokens revocados

### ✅ Gestión de Usuarios
- 👤 Crear cuenta de usuario
- 🔑 Encriptación de contraseñas
- ✉️ Email como identificador único
- ✔️ Validaciones exhaustivas

### ✅ Gestión de Productos
- 📦 Listar todos los productos
- 🔍 Acceso controlado por autenticación

### ✅ Seguridad
- 🛡️ Spring Security integrado
- 🔐 JWT con HS256
- 🚫 CORS habilitado
- 🔒 Filtro personalizado de seguridad

---

## 🚀 Inicio Rápido

### Requisitos Previos
- Java 21+
- Maven 3.6+
- MariaDB 11+
- Puerto 8085 disponible

### Instalación y Ejecución

```bash
# 1. Clonar/Descargar el proyecto
cd c:\Users\joses\IdeaProjects\apirestdemo

# 2. Compilar el proyecto
.\mvnw.cmd clean compile

# 3. Ejecutar la aplicación
.\mvnw.cmd spring-boot:run

# 4. Verificar que está corriendo
# Deberías ver: "Tomcat started on port 8085"
```

### Acceso a la API

- **URL Base:** `http://localhost:8085/api/v1/demoapirestdam235`
- **Documentación:** Ver archivo `API_DOCUMENTATION.md`

---

## 📚 Documentación

- 📖 **API_DOCUMENTATION.md** - Documentación completa de endpoints
- 📋 **LOGOUT_IMPLEMENTATION.md** - Detalles del sistema de logout
- 📝 **Este archivo (README.md)** - Información general

---

## 🏗️ Estructura del Proyecto

```
apirestdemo/
├── src/
│   └── main/
│       ├── java/
│       │   └── edu/sv/ues/dam235/apirestdemo/
│       │       ├── ApirestdemoApplication.java
│       │       ├── configs/
│       │       │   ├── CustomerDetailServices.java
│       │       │   ├── JwtFilter.java
│       │       │   └── SecurityConfig.java
│       │       ├── controllers/
│       │       │   ├── AuthController.java
│       │       │   └── ProductController.java
│       │       ├── dtos/
│       │       │   ├── LoginDTO.java
│       │       │   ├── RegisterDTO.java
│       │       │   ├── ResponseDTO.java
│       │       │   ├── TokenDTO.java
│       │       │   ├── UserDTO.java
│       │       │   └── ProductsDTO.java
│       │       ├── entities/
│       │       │   ├── User.java
│       │       │   └── Product.java
│       │       ├── implementations/
│       │       │   ├── AuthServicesImpl.java
│       │       │   └── ProductsImpl.java
│       │       ├── repositories/
│       │       │   ├── UserRepository.java
│       │       │   └── ProductRepository.java
│       │       ├── services/
│       │       │   ├── AuthServices.java
│       │       │   ├── ProductServices.java
│       │       │   └── TokenBlacklistService.java
│       │       └── utilities/
│       │           └── JwtUtil.java
│       └── resources/
│           └── application.properties
├── pom.xml
├── mvnw.cmd
├── API_DOCUMENTATION.md
├── LOGOUT_IMPLEMENTATION.md
└── README.md
```

---

## 📡 Endpoints Disponibles

### Autenticación (Sin autenticación requerida)
```
POST   /auth/login              → Iniciar sesión
POST   /auth/register           → Registrar usuario
POST   /auth/logout             → Cerrar sesión
```

### Productos (Requiere JWT)
```
GET    /products                → Obtener todos los productos
```

---

## 🔍 Flujos Principales

### 1️⃣ Flujo de Registro

```
Cliente
  ├─ POST /auth/register
  │  └─ Body: {name, lastName, email, password, confirmPassword}
  │
Usuario creado ✅
  ├─ Email único
  ├─ Contraseña encriptada
  ├─ Usuario activo
  │
Respuesta: 201 Created
  └─ Datos del usuario sin contraseña
```

### 2️⃣ Flujo de Login

```
Cliente
  ├─ POST /auth/login
  │  └─ Body: {user (email), pass}
  │
Validación ✅
  ├─ Usuario existe
  ├─ Contraseña correcta
  ├─ Usuario activo
  │
Token JWT generado
  ├─ Válido 30 minutos
  ├─ HS256 encriptado
  │
Respuesta: 200 OK
  └─ {token, expireIn}
```

### 3️⃣ Flujo de Logout

```
Cliente autenticado
  ├─ POST /auth/logout
  │  └─ Header: Authorization: Bearer <token>
  │
Token validado ✅
  ├─ Formato correcto
  ├─ No expirado
  │
Token revocado
  ├─ Agregado a blacklist
  ├─ No se puede usar más
  │
Respuesta: 200 OK
  └─ {success: true, message}
```

### 4️⃣ Flujo de Acceso a Recursos Protegidos

```
Cliente
  ├─ GET /products
  │  └─ Header: Authorization: Bearer <token>
  │
Validación en JwtFilter ✅
  ├─ Token presente
  ├─ Token en blacklist? → NO
  ├─ Token expirado? → NO
  ├─ Token válido? → SÍ
  │
Acceso permitido ✅
  │
Respuesta: 200 OK
  └─ [Productos...]
```

---

## 🔐 Seguridad Implementada

| Característica | Implementación |
|---|---|
| Autenticación | JWT (JJWT 0.9.1) |
| Encriptación | Spring Security PasswordEncoder |
| Validación | Custom JwtFilter |
| Revocación | TokenBlacklistService (In-Memory Set) |
| CORS | Habilitado (Access-Control-*) |
| HTTPS | Configurar en producción |

---

## 🧪 Testing Manual

### Con cURL

```bash
# 1. Registrar
curl -X POST http://localhost:8085/api/v1/demoapirestdam235/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","lastName":"User","email":"test@mail.com","password":"test123456","confirmPassword":"test123456"}'

# 2. Login
curl -X POST http://localhost:8085/api/v1/demoapirestdam235/auth/login \
  -H "Content-Type: application/json" \
  -d '{"user":"test@mail.com","pass":"test123456"}'

# Guarda el token retornado en una variable
# TOKEN="eyJhbGc..."

# 3. Acceder a recurso protegido
curl -X GET http://localhost:8085/api/v1/demoapirestdam235/products \
  -H "Authorization: Bearer $TOKEN"

# 4. Logout
curl -X POST http://localhost:8085/api/v1/demoapirestdam235/auth/logout \
  -H "Authorization: Bearer $TOKEN"

# 5. Intentar acceder después de logout (debería fallar)
curl -X GET http://localhost:8085/api/v1/demoapirestdam235/products \
  -H "Authorization: Bearer $TOKEN"
```

### Con Postman

1. Crear colección "API REST Demo"
2. Agregar requests:
   - POST /auth/register
   - POST /auth/login
   - GET /products (con token en Authorization)
   - POST /auth/logout
3. Usar ambiente para guardar token entre requests

---

## ⚙️ Configuración

**Archivo:** `src/main/resources/application.properties`

```properties
# Servidor
server.port=8085
server.servlet.context-path=/api/v1/demoapirestdam235

# Base de Datos
spring.datasource.url=jdbc:mariadb://3.22.57.8:3306/DBTmp
spring.datasource.username=dam235
spring.datasource.password=demo_pass
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# JPA
spring.jpa.database-platform=org.hibernate.dialect.MariaDBDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logs
logging.level.root=INFO
logging.level.org.hibernate.SQL=DEBUG
```

---

## 🛠️ Tecnologías Utilizadas

| Componente | Versión |
|---|---|
| Spring Boot | 3.5.7 |
| Spring Security | 6.x |
| Spring Data JPA | 6.x |
| JWT (JJWT) | 0.9.1 |
| MariaDB | 11+ |
| Lombok | 1.18.30 |
| Maven | 3.6+ |
| Java | 21 |

---

## 📊 Modelos de Base de Datos

### Tabla `User`
```sql
CREATE TABLE `User` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255),
  `lastName` VARCHAR(255),
  `email` VARCHAR(255) UNIQUE,
  `password` VARCHAR(255),
  `active` BOOLEAN
);
```

### Tabla `Product`
```sql
CREATE TABLE Product (
  `Code` INT AUTO_INCREMENT PRIMARY KEY,
  `Name` VARCHAR(255),
  `Status` BOOLEAN
);
```

---

## 🐛 Solución de Problemas

### Puerto 8085 ocupado
```bash
# Cambiar puerto en application.properties
server.port=8086
```

### Error de conexión a base de datos
- Verificar que MariaDB esté corriendo
- Confirmar credenciales en application.properties
- Validar dirección IP del servidor

### Token inválido después de logout
- Comportamiento esperado ✅
- El token fue revocado
- Hacer login nuevamente

### Lombok no genera getters/setters
- Verificar que `@Data` está presente
- Compilar nuevamente con `clean compile`
- Verificar configuración del compilador en pom.xml

---

## 📝 Notas de Desarrollo

### Cambios Recientes
- ✅ Implementado endpoint `/auth/logout`
- ✅ Sistema de lista negra para tokens
- ✅ Validación en JwtFilter
- ✅ Documentación completa

### Próximas Mejoras Sugeridas
- [ ] Agregar refresh tokens
- [ ] Implementar roles y permisos (RBAC)
- [ ] Persistencia de blacklist en Redis
- [ ] Auditoría de accesos
- [ ] Endpoints adicionales (perfil, cambiar contraseña)

---

## 👥 Autor/Contribuidor

**Proyecto:** API REST Demo DAM235
**Desarrollo:** Implementación de autenticación y seguridad
**Última actualización:** 16 de Noviembre de 2025

---

## 📄 Licencia

Este proyecto es de uso educativo.

---

## 📞 Contacto/Soporte

Para problemas o preguntas, revisar:
1. `API_DOCUMENTATION.md` - Documentación de endpoints
2. `LOGOUT_IMPLEMENTATION.md` - Detalles técnicos de logout
3. Logs de consola (DEBUG level para Hibernate)

---

**¡Proyecto completamente funcional y listo para usar! 🎉**
