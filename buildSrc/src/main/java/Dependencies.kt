@file:Suppress("ClassName")

import java.util.*

@Suppress("unused")
object Args {
    const val coroutines = "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    const val default = "-Xjvm-default=compatibility"
}

@Suppress("unused")
object Versions {
    const val compileSdk = 32
    const val minSdk = 23
    const val targetSdk = 32

    const val appcompat = "1.4.1"
    const val base = "0.2.1"
    const val constraintLayout = "2.1.3"
    const val coreKtx = "1.7.0"
    const val coreTesting = "2.1.0"
    const val coroutines = "1.5.2"
    const val datastore = "1.0.0"
    const val desugar = "1.1.5"
    const val espresso = "3.4.0"
    const val exoplayer = "2.16.1"
    const val firebase = "29.0.4"
    const val fragmentKtx = "1.4.1"
    const val glide = "4.12.0"
    const val gradle = "4.2.1"
    const val googleTruth = "1.1.2"
    const val hilt = "2.38.1"
    const val hiltWorker = "1.0.0"
    const val junit = "4.13.2"
    const val junitAndroid = "1.1.3"
    const val legacy = "1.0.0"
    const val lifecycle = "2.4.0"
    const val lottie = "4.2.2"
    const val material = "1.5.0"
    const val navigation = "2.4.0"
    const val recyclerView = "1.2.1"
    const val retrofit = "2.9.0"
    const val robolectric = "4.7.3"
    const val room = "2.4.1"
    const val sdp_ssp = "1.0.6"
    const val swipeRefreshLayout = "1.1.0"
    const val timber = "4.7.1"
    const val viewpager = "1.0.0"
    const val worker = "2.7.1"
}

@Suppress("unused")
object Deps {
    // region coreLibraryDesugaring
    const val desugar = "com.android.tools:desugar_jdk_libs:${Versions.desugar}"
    // endregion

    // region kapt
    const val roomKapt = "androidx.room:room-compiler:${Versions.room}"
    const val glideKapt = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val hiltKapt = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltWorkerKapt = "androidx.hilt:hilt-compiler:${Versions.hiltWorker}"
    // endregion

    // region implementation
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val swipeRefreshLayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"

    //legacy
    const val legacy = "androidx.legacy:legacy-support-v4:${Versions.legacy}"
    //sdp ssp
    const val sdp = "com.intuit.sdp:sdp-android:${Versions.sdp_ssp}"
    const val ssp = "com.intuit.ssp:ssp-android:${Versions.sdp_ssp}"
    //fragmentKtx
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    //lifecycle
    const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"

    //navigation
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    //recyclerview
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    // exoplayer
    const val exoCore = "com.google.android.exoplayer:exoplayer-core:${Versions.exoplayer}"
    const val exoUi = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoplayer}"
    //room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    //viewpager
    const val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewpager}"

    // coroutines
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"

    //glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    //retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    // DataStore
    const val datastore = "androidx.datastore:datastore-preferences:${Versions.datastore}"
    //timber
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // firebase
    const val firebase = "com.google.firebase:firebase-bom:${Versions.firebase}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"

    //worker
    const val worker = "androidx.work:work-runtime-ktx:${Versions.worker}"

    //hilt
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltWorker = "androidx.hilt:hilt-work:${Versions.hiltWorker}"

    // lottie
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"

    // endregion

    // region testing
    //testImplementation
    const val junit = "junit:junit:${Versions.junit}"
    const val truth = "com.google.truth:truth:${Versions.googleTruth}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val hiltTest = "com.google.dagger:hilt-android-testing:${Versions.hilt}"

    //androidTestImplementation
    const val androidJunit = "androidx.test.ext:junit:${Versions.junitAndroid}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val androidTruth = "com.google.truth:truth:${Versions.googleTruth}"
    const val coreTest = "androidx.arch.core:core-testing:${Versions.coreTesting}"
    // endregion

    @JvmStatic
    fun isNonStable(version: String): Boolean {
        val stableKeyword =
            listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase(Locale.ROOT).contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }
}

object AppDetail {
    const val appName = "ChargingAnimation"
    const val versionName = "1.0.0"

    // Change these values as needed
    const val previousPath = "/outputs/apk/debug"
    const val targetPath = "newOutput"

    const val previousName = "app-debug.apk"
    const val newApkName = "${appName}_v${versionName}.apk"
}