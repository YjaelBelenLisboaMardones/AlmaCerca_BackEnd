## 📡 Documentación de la API

A continuación se detallan los endpoints disponibles.

> **🔐 Nota de Seguridad:** Para las rutas protegidas (**ADMIN** y **BUYER**), es obligatorio enviar el Header: `userId: [ID_DEL_USUARIO]`.

### 1. Autenticación (Público)
| Método | Endpoint | Descripción | Body (JSON) |
| :---: | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Registra un Comprador. | `{"email": "...", "password": "..."}` |
| `POST` | `/api/auth/login` | Inicia sesión (Devuelve ID y Rol). | `{"email": "...", "password": "..."}` |

### 2. Gestión de Productos (Rol: ADMIN)
> Requiere Header: `userId: [ID_ADMIN]`

| Método | Endpoint | Descripción | Body (JSON) |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/admin/products` | Lista todos los productos. | - |
| `POST` | `/api/admin/products` | Crea un producto nuevo. | `{"name": "...", "description": "...", "price": 0.0}` |
| `PUT` | `/api/admin/products/{id}` | Actualiza un producto. | `{"name": "...", ...}` |
| `DELETE` | `/api/admin/products/{id}` | Elimina un producto. | - |

### 3. Carrito de Compras (Rol: BUYER)
> Requiere Header: `userId: [ID_BUYER]`

| Método | Endpoint | Descripción | Parámetros (Query) |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/cart` | Muestra el carrito del usuario. | - |
| `POST` | `/api/cart` | Agrega un ítem al carrito. | `?productId=1&quantity=2` |
| `DELETE` | `/api/cart/items/{id}` | Elimina un ítem específico. | - |