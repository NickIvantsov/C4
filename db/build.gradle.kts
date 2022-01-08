plugins {
    id("com.android.library")
    id ("kotlin-android")
    id ("kotlin-kapt")
}

android {
    compileSdk = AppConfig.compileSdkVer

    defaultConfig {
        minSdk = AppConfig.minSdkVer
        targetSdk = AppConfig.targetSdkVer
//        versionCode = AppConfig.versionCodeVer
//        versionName =  AppConfig.versionNameVer

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        consumerProguardFiles ("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = Versions.javaVersion
        targetCompatibility = Versions.javaVersion
    }
    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }
}

dependencies {
    api (project(":model"))
    implementation ("androidx.core:core-ktx:1.6.0")
    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    implementation ("androidx.room:room-runtime:${Versions.roomVersion}")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation ("androidx.room:room-ktx:${Versions.roomVersion}")
    kapt ("androidx.room:room-compiler:${Versions.roomVersion}")
    implementation ("com.google.dagger:dagger:${Versions.dagger}")
    kapt ("com.google.dagger:dagger-compiler:${Versions.dagger}")
}