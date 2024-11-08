plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "thiennh23.potholehunter"
    compileSdk = 34

    defaultConfig {
        applicationId = "thiennh23.potholehunter"
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
    implementation("org.osmdroid:osmdroid-android:6.1.14")
    // Animated bottom navigation bar
    implementation("nl.joery.animatedbottombar:library:1.1.0")
    // Shimmer effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")
}