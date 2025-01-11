buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.4.2")
        val nav_version = "2.8.5"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}

plugins {
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}