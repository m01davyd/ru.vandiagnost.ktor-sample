plugins {
    kotlin("multiplatform") version "1.6.10"
    kotlin("plugin.serialization") version "1.8.21"
}

val kotlinVersion = "1.6.10"
val serializationVersion = "1.8.21"

group = "ru.vandiagnost"
version = "0.0.1"

repositories {
    mavenCentral()

    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm {}
    js(LEGACY) {
        browser {}
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")

            }
        }
    }
}