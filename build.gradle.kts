// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
    id("org.jetbrains.kotlin.kapt") version "2.0.0-Beta1" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.9.20" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.1" apply false
}

buildscript{
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}