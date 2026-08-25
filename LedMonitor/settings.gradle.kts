plugins {
    id("com.android.application") version "8.1.4"
    id("org.jetbrains.kotlin.android") version "1.9.0"
}

android {
    namespace = "com.led.monitor"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.led.monitor"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}
