plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(AppConfig.compileSdk)
    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        versionCode(AppConfig.versionCode)
        versionName(AppConfig.versionName)
        testInstrumentationRunner(AppConfig.androidTestInstrumentation)
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    Libs.implementations.forEach {
        implementation(it)
    }
    Libs.testImplementations.forEach {
        testImplementation(it)
    }
    Libs.kapt.forEach {
        kapt(it)
    }
    Libs.androidTestImplementation.forEach {
        androidTestImplementation(it)
    }
    configurations {
        compile.exclude(Libs.atomicfuCommon)
    }
}
