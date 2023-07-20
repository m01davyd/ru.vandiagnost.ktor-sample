val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version : String by project
val h2_version : String by project

plugins {
    kotlin("jvm") version "1.8.21"
    //kotlin("js") version "1.8.20"
    id("io.ktor.plugin") version "2.3.0"
                id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
}

group = "ru.vandiagnost"
version = "0.0.1"
application {
    mainClass.set("ru.vandiagnost.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
//   maven {
//        url= uri ("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers/org/jetbrains/kotlin-react/16.0.0-pre.13-kotlin-1.2.0/16.0.0-pre.13-kotlin-1.2.0.jar")
//   }

    flatDir {
        dirs("C:\\vandiagnost\\model1\\build\\libs")
    }}
//fun kotlinw(target: String): String =
//    "org.jetbrains.kotlin-wrappers:kotlin-$target"
////val kotlinWrappers = "org.jetbrains.kotlin-wrappers"
//val kotlinWrappersVersion = "16.0.0-pre.13-kotlin-1.2.0"


//implementation(project(mapOf("path" to ":")))
//    implementation(project(mapOf("path" to ":")))
//    implementation(project(mapOf("path" to ":")))
    //    implementation(project(mapOf("path" to ":")))
//    implementation(project(mapOf("path" to ":")))
    //implementation("C:/vandiagnost/build/node_modules/react")
    //testImplementation(kotlin("test"))
//    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
//    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
//    implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
//    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.346")
//    implementation("org.jetbrains.kotlin-wrappers:kotlin-redux:4.1.2-pre.346")
//    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-redux:7.2.6-pre.346")
    dependencies {
        implementation("ru.vandiagnost.ktor-sample:model1-jvm-0.0.1")
        implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
        implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
        implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
        implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
        implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
        implementation("org.postgresql:postgresql:42.2.2")
        implementation("com.h2database:h2:$h2_version")
        implementation("io.ktor:ktor-server-html-builder-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-mustache-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
        implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.129-kotlin-1.4.20")
        implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-cio-jvm:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.2")


    }
