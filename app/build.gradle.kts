plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "com.aldera.multitasker"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    signingConfigs {
        create("release") {
            storeFile = file("../signing/release.jks")
            keyAlias = "release"
            keyPassword = "_mDK7FE6"
            storePassword = "_mDK7FE6"
        }
        getByName("debug") {
            storeFile = file("../signing/debug.jks")
            keyAlias = "debug"
            keyPassword = "f$4EU5!K"
            storePassword = "f$4EU5!K"
        }
    }

    buildTypes {
        getByName("release") {
            buildConfigField(
                "String",
                "SERVER_URL",
                project.properties["release_server_url"] as String
            )

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = true
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            buildConfigField(
                "String",
                "SERVER_URL",
                project.properties["debug_server_url"] as String
            )
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

    kotlinOptions {
        jvmTarget = "1.8"
        this.jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(Libs.CORE_KTX)

    // UI
    implementation(Libs.ACTIVITY_KTX)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.FRAGMENT_KTX)
    implementation(Libs.CARD_VIEW)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.MATERIAL)
    implementation(Libs.VIEWPAGER2)
    implementation(Libs.VIEW_BINDING)
    implementation(Libs.ADAPTER_DELEGATE)
    implementation(Libs.ADAPTER_DELEGATE_VB)
    implementation(Libs.ADAPTER_DELEGATES_PAGING)
    implementation(Libs.SWIPE_TO_REFRESH)

    // Coroutines
    implementation(Libs.COROUTINES)

    // Utils
    implementation(Libs.TIMBER)

    // Time
    implementation(Libs.THREETENABP)

    // Arch
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    implementation(Libs.LIFECYCLE_RUNTIME_KTX)
    implementation(Libs.LIFECYCLE_VIEW_MODEL_KTX)
    implementation(Libs.PREFERENCES)
    implementation(Libs.PAGING)
    kapt(Libs.LIFECYCLE_COMPILER)

    // Navigation
    implementation(Libs.NAVIGATION_FRAGMENT_KTX)
    implementation(Libs.NAVIGATION_UI_KTX)

    // DI - Hilt
    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)

    // Network
    implementation(Libs.OKHTTP_BOM)
    implementation(Libs.OKHTTP)
    implementation(Libs.OKHTTP_LOGGING_INTERCEPTOR)
    implementation(Libs.RETROFIT)
    implementation(Libs.RETROFIT_MOSHI)

    // Json
    implementation(Libs.MOSHI)
    implementation(Libs.MOSHI_ADAPTERS)
    kapt(Libs.MOSHI_KAPT)

    // Glide
    implementation(Libs.GLIDE)
    implementation(Libs.GLIDE_TRANSFORMATION)
    kapt(Libs.GLIDE_COMPILER)

    // Firebase
    implementation(platform(Libs.FIREBASE_BOM))
//    implementation(Libs.FIREBASE_CRASHLYTICS)

    // DateTime
    implementation(Libs.THREETENABP)

    //Tests
    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKK)
    testImplementation(Libs.COROUTINE_TEST)
    androidTestImplementation(Libs.ESPRESSO_CORE)
    androidTestImplementation(Libs.EXT_JUNIT)
    androidTestImplementation(Libs.MOCKITO)
    testImplementation(Libs.HILT_ANDROID_TESTING)
    kaptTest(Libs.HILT_COMPILER)
}
