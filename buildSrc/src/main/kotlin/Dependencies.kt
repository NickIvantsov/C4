import org.gradle.api.JavaVersion

object PluginVersions {
    const val kotlin = "1.5.10"
    const val navigationSafeArgsGradlePlugin = "2.3.5"
}

object AppConfig {
    const val compileSdkVer = 31
    const val minSdkVer = 21
    const val targetSdkVer = 31
    const val versionCodeVer = 3
    const val versionNameVer = "1.1.1"

    const val androidTestInstrumentation = "androidx.test.runner.AndroidJUnitRunner"
    const val consumerRulesPro = "consumer-rules.pro"
    const val applicationId = "rewheeldev.tappycosmicevasion"
}

object BuildTypes{
    const val release = "release"
    const val debug = "debug"
    const val proguardAndroid = "proguard-android.txt"
    const val proguardRules = "proguard-rules.pro"
}

object Versions {

    const val gradle = "7.0.4"
    const val coroutines = "1.5.0-native-mt"
    const val kotlinxCoroutinesAndroid = "1.6.0"
    const val appcompat = "1.3.0"
    const val constraintLayout = "2.0.4"
    const val gson = "2.8.7"
    const val googleHttpClientAndroid = "+"
    const val googleApiClientAndroid = "+"
    const val googleApiClientGson = "+"
    const val legacySupportV4 = "1.0.0"
    const val junit = "4.12"
    const val androidxJunit = "1.1.1"
    const val androidxEspressoCore = "3.1.0"
    const val timber = "4.7.1"
    const val retrofit = "2.9.0"
    const val okhttp3 = "5.0.0-alpha.2"
    const val dagger = "2.36"
    const val lifecycleViewModelKtx = "2.3.1"
    const val lifecycleCommonJava8 = "2.3.1"
    const val lifecycleExtensions = "2.2.0"
    const val lifecycleLivedataKtx = "2.3.1"
    const val lifecycleRuntimeKtx = "2.3.1"
    const val workRuntimeKtx = "2.5.0"
    const val navigation = "2.3.5"
    const val roomVersion = "2.3.0"
    const val multidex = "2.0.1"
    const val CORE_KTX = "1.7.0"
    const val ANDROID_MATERIAL = "1.4.0"

    const val jvmTarget = "11"
    val javaVersion = JavaVersion.VERSION_11

}

object Libs {
    val plugins = listOf(
        "org.jetbrains.kotlin.jvm" to PluginVersions.kotlin
    )
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val GOOGLE_HTTP_CLIENT_ANDROID =
        "com.google.http-client:google-http-client-android:${Versions.googleHttpClientAndroid}"
    const val GOOGLE_API_CLIENT_ANDROID =
        "com.google.api-client:google-api-client-android:${Versions.googleApiClientAndroid}"
    const val GOOGLE_API_CLIENT_GSON =
        "com.google.api-client:google-api-client-gson:${Versions.googleApiClientGson}"
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${PluginVersions.kotlin}"
    const val LEGACY_SUPPORT_V_4 = "androidx.legacy:legacy-support-v4:${Versions.legacySupportV4}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val ANDROID_MATERIAL = "com.google.android.material:material:${Versions.ANDROID_MATERIAL}"

    //retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val RETROFIT_2_ADAPTER_RXJAVA_2 =
        "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val RETROFIT_2_CONVERTER_GSON =
        "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    //okhttp
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp3}"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp3}"

    //DI
    const val DAGGER = "com.google.dagger:dagger:${Versions.dagger}"
    const val DAGGER_ANDROID_SUPPORT = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val DAGGER_ANDROID_PROCESSOR = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:${Versions.dagger}"


    //KTX
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"

    //coroutines for asynchronous calls (and Deferred’s adapter)
    const val KOTLINX_COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val KOTLINX_COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinxCoroutinesAndroid}"

    // lifecycle
    const val LIFECYCLE_COMMON_JAVA_8 =
        "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycleCommonJava8}"
    const val LIFECYCLE_EXTENSIONS =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    const val LIFECYCLE_VIEWMODEL_KTX =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModelKtx}"
    const val LIFECYCLE_LIVEDATA_KTX =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleLivedataKtx}"
    const val LIFECYCLE_RUNTIME_KTX =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"

    //WorkerManager Kotlin + coroutines
    const val WORK_RUNTIME_KTX = "androidx.work:work-runtime-ktx:${Versions.workRuntimeKtx}"

    //Navigation
    const val NAVIGATION_FRAGMENT_KTX =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.roomVersion}"

    //Room
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val ROOM_TESTING = "androidx.room:room-testing:${Versions.roomVersion}"

    // optional - Kotlin Extensions and Coroutines support for Room
    const val MULTIDEX = "androidx.multidex:multidex:${Versions.multidex}"

    //JUnit
    const val JUNIT = "junit:junit:${Versions.junit}"
    const val ANDROIDX_JUNIT = "androidx.test.ext:junit:${Versions.androidxJunit}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.androidxEspressoCore}"


    val implementations = listOf(
        APPCOMPAT,
        CONSTRAINT_LAYOUT,
        gson,
        GOOGLE_HTTP_CLIENT_ANDROID,
        GOOGLE_API_CLIENT_ANDROID,
        GOOGLE_API_CLIENT_GSON,
        KOTLIN_STDLIB,
        LEGACY_SUPPORT_V_4,
        LIFECYCLE_LIVEDATA_KTX,
        timber,
        retrofit,
        RETROFIT_2_ADAPTER_RXJAVA_2,
        RETROFIT_2_CONVERTER_GSON,
        okhttp,
        LOGGING_INTERCEPTOR,
        KOTLINX_COROUTINES_CORE,
        KOTLINX_COROUTINES_ANDROID,
        LIFECYCLE_COMMON_JAVA_8,
        LIFECYCLE_EXTENSIONS,
        LIFECYCLE_VIEWMODEL_KTX,
        LIFECYCLE_LIVEDATA_KTX,
        LIFECYCLE_RUNTIME_KTX,
        WORK_RUNTIME_KTX,
        NAVIGATION_FRAGMENT_KTX,
        NAVIGATION_UI_KTX,
        ROOM_RUNTIME,
        ROOM_KTX,
        MULTIDEX
    )

    val testImplementations = listOf(
        JUNIT,
        ROOM_TESTING
    )
    val androidTestImplementation = listOf(
        ANDROIDX_JUNIT,
        ESPRESSO_CORE
    )
    val atomicfuCommon = mapOf(
        "group" to "org.jetbrains.kotlinx",
        "module" to "atomicfu-common"
    )
}

object Plugins {
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val androidxNavigation = "androidx.navigation.safeargs.kotlin"
    const val androidLibrary = "com.android.library"
}

object PackagingOptions {
    val excludeList = listOf(
        "META-INF/DEPENDENCIES",
        "META-INF/LICENSE",
        "META-INF/LICENSE.txt",
        "META-INF/license.txt",
        "META-INF/NOTICE",
        "META-INF/NOTICE.txt",
        "META-INF/notice.txt",
        "META-INF/ASL2.0"
    )
}

object TopLevelDependencies {
    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
    val dependencies = listOf(
        "com.android.tools.build:gradle:${Versions.gradle}",
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersions.kotlin}",
        "androidx.navigation:navigation-safe-args-gradle-plugin:${PluginVersions.navigationSafeArgsGradlePlugin}"
    )
}

