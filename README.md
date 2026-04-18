# LevelUp - Tienda de Videojuegos

## Descripción
LevelUp es una aplicación móvil de comercio electrónico especializada en videojuegos, consolas y accesorios gaming. Desarrollada en Kotlin con Jetpack Compose, ofrece una experiencia de compra moderna y fluida.

## Integrantes
- **[Genesis Flores]** 

## Funcionalidades Principales

### 1. Gestión de Productos
- ✅ Catálogo completo de productos con filtros por categoría
- ✅ Búsqueda en tiempo real
- ✅ Visualización detallada de productos
- ✅ Información de stock y disponibilidad

### 2. Carrito de Compras
- ✅ Agregar/eliminar productos
- ✅ Ajustar cantidades
- ✅ Cálculo automático de totales
- ✅ Persistencia local del carrito

### 3. Sistema de Autenticación
- ✅ Registro de usuarios
- ✅ Login con validación
- ✅ Gestión de sesión
- ✅ Perfil de usuario

### 4. Gestión de Órdenes
- ✅ Creación de órdenes de compra
- ✅ Historial de órdenes
- ✅ Detalles de cada orden
- ✅ Estados de órdenes

## Endpoints Utilizados

### API Externa - Fake Store API
- **Base URL**: `https://fakestoreapi.com`

### Tecnologías Utilizadas
- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Inyección de Dependencias**: Manual
- **Persistencia Local**: Room Database
- **Networking**: Retrofit + OkHttp
- **Imágenes**: Coil
- **Testing**: JUnit5, Kotest, MockK, Compose UI Testing

### Estructura del Proyecto
```
com.example.levelup/
├── model/              # Modelos de datos
├── data/              # Repositorios y fuentes de datos
│   ├── local/        # Room Database
│   └── remote/       # APIs y servicios
├── viewmodel/         # ViewModels
├── ui/               # Interfaces de usuario
│   ├── screens/      # Pantallas de la app
│   └── components/   # Componentes reutilizables
├── navigation/        # Navegación
└── utils/            # Utilidades y helpers

### Prerrequisitos
- Android Studio Hedgehog | 2023.1.1 o superior
- JDK 11 o superior
- SDK de Android (API 24+)
- Emulador o dispositivo físico con Android 7.0+

### Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/Geneesiis/LevelUp-App.git
cd LevelUp
```

2. **Abrir en Android Studio**
   - File > Open
   - Seleccionar la carpeta del proyecto
   - Esperar a que Gradle sincronice

3. **Configurar variables de entorno** (si aplica)
   - Crear archivo `local.properties` en la raíz
   - Agregar las configuraciones necesarias

4. **Ejecutar la aplicación**
   - Click en Run o `Shift + F10`
   - Seleccionar emulador o dispositivo
   - Esperar a que compile e instale

   ### Ejecutar Tests

**Unit Tests:**
```bash
./gradlew test
```

**UI Tests:**
```bash
./gradlew connectedAndroidTest
```

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
