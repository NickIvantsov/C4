plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = AppConfig.compileSdkVer
    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdkVer
        targetSdk = AppConfig.targetSdkVer
        versionCode = AppConfig.versionCodeVer
        versionName = AppConfig.versionNameVer
        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        multiDexEnabled = true
    }
    buildFeatures {
        android.buildFeatures.viewBinding = true
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
    packagingOptions {
        PackagingOptions.excludeList.forEach {
            exclude(it)
        }
    }
}

dependencies {
    implementation(project(":core-utils"))
    implementation(project(":model"))
    implementation(project(":db"))
    implementation(project(":repository"))
    implementation(project(":feature-settings"))
    implementation(project(":feature-game"))
    implementation(project(":core"))
    implementation(project(":rewheeldevsdk"))

    Libs.implementations.forEach {
        implementation(it)
    }
    Libs.testImplementations.forEach {
        testImplementation(it)
    }
    implementation ("com.google.dagger:dagger:${Versions.dagger}")
    implementation ("com.google.dagger:dagger-android:${Versions.dagger}")
    implementation ("com.google.dagger:dagger-android-support:${Versions.dagger}")
    kapt("com.google.dagger:dagger-android-processor:${Versions.dagger}")
    kapt ("com.google.dagger:dagger-compiler:${Versions.dagger}")
    Libs.androidTestImplementation.forEach {
        androidTestImplementation(it)
    }
//    configurations {
//        compile.exclude(Libs.atomicfuCommon)
//    }
}
