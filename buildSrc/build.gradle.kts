plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        setUrl("https://repo.gradle.org/gradle/repo")
    }
}

dependencies {
    implementation("org.gradle:gradle-tooling-api:7.1.1")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.4.0.202211300538-r")
}
