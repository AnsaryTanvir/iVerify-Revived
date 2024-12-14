plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.iverify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iverify"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    ndkVersion = "23.2.8568313"
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.okhttp)

    // https://mvnrepository.com/artifact/com.google.zxing/core
    //noinspection UseTomlInstead
    implementation("com.google.zxing:core:3.5.3")

    // https://mvnrepository.com/artifact/com.journeyapps/zxing-android-embedded
    //noinspection UseTomlInstead
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

}