plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.example.frontendplantas"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.frontendplantas"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {

    // Dependencias comunes
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))  // Esto es para asegurar la versión de Compose

    implementation ("androidx.compose.material:material-icons-extended:1.6.1")
    implementation ("com.google.accompanist:accompanist-permissions:0.28.0")



// Jetpack Compose
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.foundation:foundation:1.7.0")
    implementation("androidx.compose.foundation:foundation-layout:1.7.0")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.tools.core)
    implementation(libs.androidx.compose.material3)

    //Generador de QR´s
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation(libs.androidx.runtime.livedata)
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation(libs.androidx.media3.common.ktx)

    //biometrica
    implementation(libs.androidx.biometric.ktx)

    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    //COIL sad
    implementation("io.coil-kt:coil-compose:2.1.0")

// ViewModel y LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

// Material Design 3
    implementation("androidx.compose.material3:material3")

// Dependencias para Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

// RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
// Lottie
    implementation("com.airbnb.android:lottie-compose:6.0.0")

// Palette
    implementation("androidx.palette:palette:1.0.0")

    implementation ("com.google.accompanist:accompanist-permissions:0.34.0")

// UI Testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.google.mlkit:barcode-scanning:17.2.0")

// Scanner and Play services
    implementation(libs.google.scanner)
    implementation(libs.play.services)

    //implementation("androidx.compose.material3:material3:1.0.0")

// Activity and AppCompat
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

// Test Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    //datePara viejas api
    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.9")
}