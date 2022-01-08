plugins{
    id("com.android.library")
    id ("kotlin-android")
    id ("kotlin-android-extensions")
    id ("kotlin-kapt")
//    id ("androidx.navigation.safeargs")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = AppConfig.compileSdkVer
    sourceSets {
        getByName("main").java.srcDirs("build/generated/source/navigation-args")
    }
    defaultConfig {
        minSdk = AppConfig.minSdkVer
        targetSdk = AppConfig.targetSdkVer
//        versionCode (1)
//        versionName ("1.0")

        testInstrumentationRunner = ("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFiles ("consumer-rules.pro")
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
}

dependencies {

    implementation (project(":repository"))
    implementation (project(":core"))
    implementation (project(":core-utils"))
    implementation (project(":rewheeldevsdk"))

    implementation (Libs.APPCOMPAT)
    implementation (Libs.ANDROID_MATERIAL)
    implementation (Libs.LEGACY_SUPPORT_V_4)
    implementation (Libs.LIFECYCLE_LIVEDATA_KTX)
    implementation (Libs.LIFECYCLE_VIEWMODEL_KTX)
    testImplementation (Libs.JUNIT)
    androidTestImplementation (Libs.ANDROIDX_JUNIT)
    androidTestImplementation (Libs.ESPRESSO_CORE)

    implementation (Libs.DAGGER)
    implementation (Libs.DAGGER_ANDROID_SUPPORT)
    kapt (Libs.DAGGER_COMPILER)

    kapt("com.google.dagger:dagger-android-processor:${Versions.dagger}")

    implementation (Libs.NAVIGATION_FRAGMENT_KTX)
    implementation (Libs.NAVIGATION_UI_KTX)

    implementation(Libs.CORE_KTX)
    implementation (Libs.KOTLINX_COROUTINES_CORE)
    runtimeOnly (Libs.KOTLINX_COROUTINES_ANDROID)
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

}