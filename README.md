# Dashboard0x Android App

Aplicación Android completa con Jetpack Compose que se conecta al backend Dashboard2 en Netlify.

## Características

- ✅ Autenticación con tokens (30 min expiración)
- ✅ Gestión completa de TODOs (CRUD) compartidos con la web
- ✅ Dashboard privado con precios de criptomonedas (BTC, SOL)
- ✅ Dashboard público con estadísticas de redes sociales
- ✅ Arquitectura MVVM + Repository Pattern
- ✅ UI moderna con Material 3

## Requisitos

- Android Studio Hedgehog o superior
- JDK 11 o superior
- Android SDK 30 o superior
- Dispositivo/Emulador con Android 11+ (API 30+)

## Configuración e Instalación

### 1. Abrir el Proyecto

```bash
# Abrir Android Studio
# File > Open > Seleccionar la carpeta Dashboard0x
```

### 2. Sync Gradle

**IMPORTANTE:** Después de abrir el proyecto, debes sincronizar Gradle:

1. Haz click en **"Sync Now"** en la barra superior (aparece automáticamente)
2. O ve a: **File > Sync Project with Gradle Files**
3. Espera a que termine la sincronización (puede tomar 2-5 minutos la primera vez)

### 3. Compilar

```bash
# Opción 1: Desde Android Studio
Build > Make Project (o Ctrl+F9)

# Opción 2: Desde terminal
./gradlew assembleDebug
```

### 4. Ejecutar

1. Conecta un dispositivo Android vía USB (con depuración USB habilitada)
   - O inicia un emulador desde AVD Manager
2. Haz click en el botón **Run** (▶️) o presiona **Shift+F10**

## Configuración del Backend

La aplicación se conecta a:
```
https://dashboard0x.netlify.app/
```

Necesitarás la **password** del backend para hacer login.

## Estructura del Proyecto

```
app/src/main/java/com/example/dashboard0x/
├── data/
│   ├── model/           # Modelos de datos (auth, todo, dashboard)
│   ├── remote/          # API Retrofit + RetrofitClient
│   ├── local/           # TokenManager con DataStore
│   └── repository/      # Repositorios (Auth, Todo, Dashboard)
├── domain/
│   └── util/            # Resource wrapper, Validators
├── presentation/
│   ├── navigation/      # NavGraph y rutas
│   ├── auth/            # Login (Screen + ViewModel)
│   ├── todo/            # TODOs (Screen + ViewModel + componentes)
│   ├── dashboard/       # Dashboards (Screens + ViewModels)
│   └── components/      # Componentes reutilizables
├── util/                # Constants
└── MainActivity.kt
```

## Solución de Problemas

### Error: "BuildConfig cannot be resolved"

**Solución:** Ya está configurado `buildConfig = true` en `build.gradle.kts`. Solo necesitas hacer **Sync Gradle**.

### Error: "JAVA_HOME is not set"

**Solución:**
1. Instala JDK 11 o superior
2. Configura JAVA_HOME:
   ```bash
   # Windows
   setx JAVA_HOME "C:\Program Files\Java\jdk-11"

   # Linux/Mac
   export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
   ```

### Error: Gradle sync failed

**Solución:**
1. **Invalidate Caches:** File > Invalidate Caches > Invalidate and Restart
2. Elimina carpetas `.gradle` y `.idea` del proyecto
3. Vuelve a abrir el proyecto en Android Studio

### Error: Cannot resolve symbol 'androidx'

**Solución:**
1. File > Sync Project with Gradle Files
2. Build > Clean Project
3. Build > Rebuild Project

## Testing

### Tests Unitarios

```bash
# Ejecutar tests unitarios
./gradlew test

# Desde Android Studio
# Click derecho en test > Run 'ValidatorsTest'
```

### Tests de Integración

```bash
# Ejecutar tests instrumentados (requiere dispositivo/emulador)
./gradlew connectedAndroidTest
```

## Checklist de Funcionalidades

**Autenticación:**
- [ ] Login con password correcta funciona
- [ ] Login con password incorrecta muestra error
- [ ] Token persiste al cerrar/reabrir app
- [ ] Auto-logout después de expiración (30 min)

**TODOs:**
- [ ] Lista se carga correctamente
- [ ] Crear TODO funciona
- [ ] Toggle completado funciona
- [ ] Editar TODO funciona
- [ ] Eliminar TODO funciona
- [ ] Pull-to-refresh recarga lista
- [ ] Validación: texto vacío rechazado
- [ ] Validación: > 500 chars rechazado

**Dashboard Privado:**
- [ ] Muestra precios BTC y SOL
- [ ] Cambios positivos en verde con ↑
- [ ] Cambios negativos en rojo con ↓
- [ ] Pull-to-refresh actualiza datos

**Dashboard Público:**
- [ ] Muestra stats de YouTube
- [ ] Muestra stats de Twitter
- [ ] Muestra stats de Instagram
- [ ] Muestra info de GitHub
- [ ] Muestra extensiones
- [ ] Pull-to-refresh funciona

## Build Release

Para crear un APK de release:

```bash
# Debug APK (sin firmar)
./gradlew assembleDebug
# APK en: app/build/outputs/apk/debug/

# Release APK (requiere keystore)
./gradlew assembleRelease
# APK en: app/build/outputs/apk/release/
```

**Nota:** El build de release tiene ProGuard habilitado para ofuscación y optimización.

## Tecnologías Utilizadas

- **UI:** Jetpack Compose + Material 3
- **Arquitectura:** MVVM + Repository Pattern
- **Async:** Kotlin Coroutines + StateFlow
- **Network:** Retrofit 2.9.0 + OkHttp
- **Storage:** DataStore Preferences
- **Navigation:** Jetpack Navigation Compose
- **Seguridad:** Network Security Config + HTTPS only

## Contacto y Soporte

Para reportar bugs o solicitar features, crea un issue en el repositorio del proyecto.

## Licencia

Este proyecto es parte de Dashboard0x ecosystem.
