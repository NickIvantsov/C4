plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
}

android {
    compileSdk = AppConfig.compileSdkVer

    defaultConfig {
        minSdk = AppConfig.minSdkVer
        targetSdk = AppConfig.targetSdkVer

        testInstrumentationRunner = (AppConfig.androidTestInstrumentation)
        consumerProguardFiles(AppConfig.consumerRulesPro)
    }

    buildTypes {
        getByName(BuildTypes.release) {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile(BuildTypes.proguardAndroid), BuildTypes.proguardRules)
        }
        getByName(BuildTypes.debug) {
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

    implementation(Libs.CORE_KTX)
    testImplementation(Libs.JUNIT)
    androidTestImplementation(Libs.ANDROIDX_JUNIT)
    androidTestImplementation(Libs.ESPRESSO_CORE)
}