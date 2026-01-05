// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // This file MUST define the version.
    alias(libs.plugins.android.application) version "8.6.0" apply false
    alias(libs.plugins.jetbrains.kotlin.android) version "1.9.22" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}