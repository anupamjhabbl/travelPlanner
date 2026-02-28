import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.bbltripplanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bbltripplanner"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        val localProperties = Properties()
        rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use {
            localProperties.load(it)
        }
        val locationApiKey: String = localProperties.getProperty("LOCATION_API_KEY") ?: "null"
        buildConfigField("String", "LOCATION_API_KEY", "\"$locationApiKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.google.fonts)
    implementation(libs.io.coil)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.io.insert.koin)
    implementation(libs.io.koin.compose)
    implementation(libs.koin.annotations)
    implementation(libs.com.squareup.retrofit)
    implementation(libs.com.squareup.convertor)
    implementation(libs.gson.impl)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.navigation.compose)
    implementation(libs.android.material)
    implementation(libs.androidx.window)
    implementation(libs.google.accompanist)
    implementation(libs.androidx.security.crypto)
    implementation(libs.core.splashscreen)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.compose.material3)
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterialIcons)
    ksp(libs.ksp.room)
    ksp(libs.koin.ksp.compiler)
    testImplementation(libs.io.insert.koin.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}