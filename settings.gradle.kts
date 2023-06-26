pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
        maven { url=uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}

rootProject.name = "KPLC-Outage-App"
include(":app-android")
// include(":app-desktop")
include(":shared")
