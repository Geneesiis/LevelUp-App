plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
}

android {
    namespace = "com.example.levelup"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.levelup"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "META-INF/licenses/ASM"
            excludes += "/META-INF/LICENSE-notice.md"
            excludes += "win32-x86-64/attach_hotspot_windows.dll"
            excludes += "win32-x86/attach_hotspot_windows.dll"
        }
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
            // ✅ Configurar JUnit5 para Unit Tests
            all {
                it.useJUnitPlatform()
            }
        }
        // ✅ Deshabilitar animaciones para tests más estables
        animationsDisabled = true
    }
}

dependencies {
    // ===========================================
    // DEPENDENCIAS PRINCIPALES
    // ===========================================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.navigation.compose)

    // Kotlin-Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

    // ViewModel - Lifecycle - Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.4")

    // Material-Icons-Extended
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // Coil para imágenes
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ===========================================
    // TESTING - Unit Tests (test/)
    // ===========================================

    // JUnit 5 (Base)
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    // Kotest (estilo BDD para lógica de negocio)
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // MockK (Mocking para Kotlin)
    testImplementation("io.mockk:mockk:1.13.8")

    // Coroutines Test (para testear asincronía)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    // ===========================================
    // TESTING - UI Tests (androidTest/)
    // ===========================================

    // JUnit 4 (necesario para ComposeTestRule)
    androidTestImplementation("junit:junit:4.13.2")

    // JUnit para Android
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose UI Test
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.8")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.8")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.8")

    // Navigation Testing
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.6")

    // Kotest para AndroidTest
    androidTestImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    androidTestImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // MockK para AndroidTest
    androidTestImplementation("io.mockk:mockk-android:1.13.8")

    // Coroutines Test para AndroidTest
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}

// ✅ Configurar JUnit 5 para Unit Tests
tasks.withType<Test> {
    useJUnitPlatform()
}
