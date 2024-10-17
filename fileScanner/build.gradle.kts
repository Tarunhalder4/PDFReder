plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.filescanner"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("androidx.camera:camera-core:1.1.0")
    implementation ("androidx.camera:camera-camera2:1.1.0")
    implementation ("androidx.camera:camera-lifecycle:1.1.0")
    // CameraX View class (this includes PreviewView)
    implementation ("androidx.camera:camera-view:1.1.0")

    // Optional - CameraX extensions (for additional features like HDR, etc.)
    implementation ("androidx.camera:camera-extensions:1.1.0")

    implementation ("com.google.mlkit:text-recognition:16.0.0")

    implementation("com.pspdfkit:pspdfkit:2024.6.1")
  //  implementation ("org.apache.pdfbox:pdfbox:2.0.27")



}