plugins {
    id("com.android.library")
    id ("kotlin-android")
    id ("kotlin-android-extensions")
    id ("kotlin-kapt")
}

android {
    compileSdkVersion( 30)

    defaultConfig {
        minSdkVersion (21)
        targetSdkVersion (30)
        versionCode (1)
        versionName ("1.0")

        testInstrumentationRunner ("androidx.test.runner.AndroidJUnitRunner")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }
}

dependencies {
    api (project(":model"))
    implementation (project(":core-utils"))

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