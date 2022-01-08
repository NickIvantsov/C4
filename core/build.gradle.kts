import Libs.ANDROIDX_JUNIT
import Libs.DAGGER
import Libs.DAGGER_COMPILER
import Libs.ESPRESSO_CORE
import Libs.JUNIT
import Libs.KOTLINX_COROUTINES_ANDROID
import Libs.KOTLINX_COROUTINES_CORE

plugins {
    id("com.android.library")
    id("kotlin-android")
    id ("kotlin-kapt")
}

android {
    compileSdk = AppConfig.compileSdkVer

    defaultConfig {
        minSdk = AppConfig.minSdkVer
        targetSdk = AppConfig.targetSdkVer
//        versionCode = 1

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
//            minifyEnabled(false)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }
}

dependencies {

    implementation (project(":repository"))
    implementation (project(":core-utils"))


    implementation (KOTLINX_COROUTINES_CORE)
    implementation (KOTLINX_COROUTINES_ANDROID)

    testImplementation (JUNIT)
    androidTestImplementation (ANDROIDX_JUNIT)
    androidTestImplementation (ESPRESSO_CORE)

    implementation (DAGGER)
    kapt (DAGGER_COMPILER)
}