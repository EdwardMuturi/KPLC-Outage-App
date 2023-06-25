pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "KPLC-Outage-App"
include(":app-android")
// include(":app-desktop")
include(":shared")
