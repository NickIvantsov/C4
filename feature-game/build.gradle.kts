plugins {
    id("com.android.library")
    id ("kotlin-android")
    id ("kotlin-android-extensions")
    id ("kotlin-kapt")
    id ("androidx.navigation.safeargs")
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
}

dependencies {

    implementation (project(":repository"))
    implementation (project(":core-utils"))

    implementation ("androidx.core:core-ktx:1.6.0")
    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    implementation ("com.google.dagger:dagger:${Versions.dagger}")
    implementation ("com.google.dagger:dagger-android-support:${Versions.dagger}")
    kapt ("com.google.dagger:dagger-compiler:${Versions.dagger}")

    implementation ("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
    implementation ("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")

}