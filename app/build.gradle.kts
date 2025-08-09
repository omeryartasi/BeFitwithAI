plugins {
    alias(libs.plugins.android.application)
    //alias(libs.plugins.google.gms.google.services)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.fitmybody"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fitmybody"
        minSdk = 31
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.yalantis:ucrop:2.2.6")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    //yapay zekaya sorgu
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.google.android.gms:play-services-fitness:21.2.0")
    implementation("com.google.android.gms:play-services-auth:21.4.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}