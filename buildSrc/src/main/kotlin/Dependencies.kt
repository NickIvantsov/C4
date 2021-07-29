object PluginVersions {
    const val kotlin = "1.5.10"
    const val navigationSafeArgsGradlePlugin = "2.3.5"
}

object AppConfig {
    const val compileSdk = 30
    const val minSdk = 21
    const val targetSdk = 30
    const val versionCode = 3
    const val versionName = "1.1.1"

    const val androidTestInstrumentation = "androidx.test.runner.AndroidJUnitRunner"
    const val applicationId =  "rewheeldev.tappycosmicevasion"
}

object Versions {

    const val gradle = "4.2.2"
    const val coroutines = "1.5.0-native-mt"
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

    const val jvmTarget = "1.8"

}

object Libs {
    val plugins = listOf(
        "org.jetbrains.kotlin.jvm" to PluginVersions.kotlin
    )
    val implementations = listOf(
        "androidx.appcompat:appcompat:${Versions.appcompat}",
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}",
        "com.google.code.gson:gson:${Versions.gson}",
        "com.google.http-client:google-http-client-android:${Versions.googleHttpClientAndroid}",
        "com.google.api-client:google-api-client-android:${Versions.googleApiClientAndroid}",
        "com.google.api-client:google-api-client-gson:${Versions.googleApiClientGson}",
        "org.jetbrains.kotlin:kotlin-stdlib:${PluginVersions.kotlin}",
        "androidx.legacy:legacy-support-v4:${Versions.legacySupportV4}",
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleLivedataKtx}",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModelKtx}",
        "com.jakewharton.timber:timber:${Versions.timber}",
        //retrofit
        "com.squareup.retrofit2:retrofit:${Versions.retrofit}",
        "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}",
        "com.squareup.retrofit2:converter-gson:${Versions.retrofit}",
        //okhttp
        "com.squareup.okhttp3:okhttp:${Versions.okhttp3}",
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp3}",
        //DI
        "com.google.dagger:dagger:${Versions.dagger}",
        "com.google.dagger:dagger-android-support:${Versions.dagger}",

// Coroutines for asynchronous calls (and Deferred’s adapter)
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}",
        // lifecycle
        "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycleCommonJava8}",
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModelKtx}",
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleLivedataKtx}",
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}",
        //WorkerManager Kotlin + coroutines
        "androidx.work:work-runtime-ktx:${Versions.workRuntimeKtx}",
//Navigation
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}",
        "androidx.navigation:navigation-ui-ktx:${Versions.navigation}",
        "androidx.room:room-runtime:${Versions.roomVersion}",
        // optional - Kotlin Extensions and Coroutines support for Room
        "androidx.room:room-ktx:${Versions.roomVersion}",
        "androidx.multidex:multidex:${Versions.multidex}"
        )

    val kapt = listOf(
        "com.google.dagger:dagger-android-processor:${Versions.dagger}",
        "com.google.dagger:dagger-compiler:${Versions.dagger}",
        "androidx.room:room-compiler:${Versions.roomVersion}"
    )
    val testImplementations = listOf(
        "junit:junit:${Versions.junit}",
        "androidx.room:room-testing:${Versions.roomVersion}"
    )
    val androidTestImplementation = listOf(
        "androidx.test.ext:junit:${Versions.androidxJunit}",
        "androidx.test.espresso:espresso-core:${Versions.androidxEspressoCore}"
    )
   val atomicfuCommon =  mapOf(
    "group" to "org.jetbrains.kotlinx",
    "module" to "atomicfu-common"
    )
}
object Plugins{
    val plugins = listOf(
        "com.android.application",
        "kotlin-android",
        "kotlin-kapt",
        "androidx.navigation.safeargs.kotlin"
    )
}
object PackagingOptions{
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

object TopLevelDependencies{
    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
    val dependencies = listOf(
        "com.android.tools.build:gradle:${Versions.gradle}",
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersions.kotlin}",
        "androidx.navigation:navigation-safe-args-gradle-plugin:${PluginVersions.navigationSafeArgsGradlePlugin}"
    )
}

