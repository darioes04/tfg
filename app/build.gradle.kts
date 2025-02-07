import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}




android {
    namespace = "com.myprojects.prueba1"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }



    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("api.properties").inputStream())
        buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))

        applicationId = "com.myprojects.myTickets"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation (libs.material)
    implementation(libs.androidx.material.icons.extended)
    implementation (libs.androidx.navigation.compose)
    implementation(libs.gson.v288)
    implementation(libs.generativeai)
    implementation (libs.androidx.ui.text.google.fonts.v170)
    implementation (libs.gson)  
    implementation (libs.okhttp)
    implementation(libs.text.recognition)
    implementation(libs.coil.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.generativeai)
    implementation(libs.androidx.room.ktx)
    implementation(libs.play.services.analytics.impl)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.datastore.preferences.core.jvm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation (libs.firebase.auth)
    implementation (libs.play.services.auth)
    implementation (libs.firebase.firestore)
    implementation (libs.androidx.ui.text.google.fonts.v150)
    implementation ("androidx.compose.ui:ui-text-google-fonts:1.7.6")
    implementation ("androidx.compose.material3:material3:1.1.0")

}