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
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.9.0.202009080501-r")
}
