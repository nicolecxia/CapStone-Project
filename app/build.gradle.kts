import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.cocygo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cocygo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        val localProperties = rootProject.file("local.properties")
        val properties = Properties()

        if (localProperties.exists()) {
            localProperties.inputStream().use { properties.load(it) }
        }

        val apiKey: String = properties.getProperty("apiKey") ?: ""

        buildConfigField("String", "MAPS_API_KEY", "\"$apiKey\"")
        manifestPlaceholders["apiKey"] = apiKey

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:latest_version")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:latest_version")


}

