plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

android {
    namespace = "com.example.playlist__maker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.playlist__maker"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.appcompat.v170)
    implementation(libs.material.v1120)
    implementation(libs.androidx.constraintlayout.v220)
    implementation(libs.androidx.contentpager)
    implementation(libs.androidx.activity.v192)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // подключаем библиотеку retrofit и конвертер converter_gson
    implementation(libs.gson.v210)
    implementation(libs.retrofit)
    implementation (libs.converter.gson)

    // подключаем библиотеку koin
    implementation (libs.koin.android)

    // ViewPager2
    implementation (libs.androidx.viewpager2)
    implementation(libs.androidx.fragment.ktx)

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)

    //coroutines
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}