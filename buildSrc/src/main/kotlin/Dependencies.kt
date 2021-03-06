/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

@Suppress("SpellCheckingInspection")
object Dependencies {
    object Common {
        const val test = "test-common"
        const val testAnnotations = "test-annotations-common"

        const val koinCore = "org.koin:koin-core:${Versions.koin}"
        const val kermit = "co.touchlab:kermit:${Versions.kermit}"
        const val multiplatformSettings =
            "com.russhwolf:multiplatform-settings:${Versions.multiplatformSettings}"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.dateTime}"
        const val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        const val ktorSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
        const val coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
        const val sqldelightRuntime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
        const val sqldelightCoroutineExtensions =
            "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
    }

    object Android {
        const val androidMaterial =
            "com.google.android.material:material:${Versions.androidMaterial}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

        const val koinAndroidViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

        const val firebaseCrashlytics =
            "com.google.firebase:firebase-crashlytics:${Versions.firebaseCrashlytics}"
        const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
        const val anrWatchDog = "com.github.anrwatchdog:anrwatchdog:${Versions.anrWatchDog}"

        const val multiDex = "androidx.multidex:multidex:${Versions.multidex}"
        const val admob = "com.google.android.gms:play-services-ads:${Versions.admob}"
        const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val playCore = "com.google.android.play:core:${Versions.playCore}"
        const val ktor = "io.ktor:ktor-client-android:${Versions.ktor}"

        // Test
        const val jUnit = "junit:junit:${Versions.jUnit}"
        const val mockK = "io.mockk:mockk:${Versions.mockK}"
        const val archTesting = "android.arch.core:core-testing:${Versions.archTesting}"
        const val coroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val sqlliteDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
    }

    object IOS {
        const val ktor = "io.ktor:ktor-client-ios:${Versions.ktor}"
        const val sqlliteDriver = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
    }

    object JVM {
        const val testJUnit = "test-junit"

        const val ktorCore = "io.ktor:ktor-server-core:${Versions.ktor}"
        const val ktorNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
        const val ktorWebSockets = "io.ktor:ktor-websockets:${Versions.ktor}"
        const val ktorSerialization = "io.ktor:ktor-serialization:${Versions.ktor}"
        const val logBack = "ch.qos.logback:logback-classic:${Versions.logBack}"
        const val sqlliteDriver = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}"
    }

    object JS {
        const val test = "test-js"

        const val kotlinXHtml = "org.jetbrains.kotlinx:kotlinx-html-js:${Versions.kotlinXHtml}"
        const val kotlinReact = "org.jetbrains:kotlin-react:${Versions.react}"
        const val kotlinReactDom = "org.jetbrains:kotlin-react-dom:${Versions.react}"
        const val ktor = "io.ktor:ktor-client-js:${Versions.ktor}"
    }
}
