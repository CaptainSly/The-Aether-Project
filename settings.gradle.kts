pluginManagement {

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
    	google()
		mavenCentral()
    	maven { url = uri("https://jitpack.io") }
	}
}



plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "AetherProject"
include("AetherFX", "AetherLink", "AetherServer")
