plugins {
    alias(libs.plugins.android.application)
}

android {
<<<<<<< HEAD
    namespace = "com.example.electricbillcalculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.electricbillcalculator"
=======
    namespace = "com.example.project_2024"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.project_2024"
>>>>>>> c2c6e4632cb4291024b0d5196e966d8f13e59b0d
        minSdk = 24
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
<<<<<<< HEAD
=======
    implementation("com.android.volley:volley:1.2.1")
>>>>>>> c2c6e4632cb4291024b0d5196e966d8f13e59b0d
}