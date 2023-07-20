plugins {
    kotlin("js") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.21"
}

group = "ru.vandiagnost"
version = "0.0.1"
val d2vVersion: String by project
val chartsVersion: String by project

repositories {
    mavenCentral()
    jcenter()
    maven{
        url = uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/dev")
    }
    maven{
        url = uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/public")
    }
    maven {
      // url=uri("https://github.com/reactchartjs/react-chartjs-2")
        //url = uri("https://jitpack.io")
    url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
      //  maven { }
    }
    flatDir {
        dirs("C:\\vandiagnost\\model1\\build\\libs")
    }
    flatDir {
    dirs("C:\\vandiagnost\\build\\libs")}

}

dependencies {
    testImplementation(kotlin("test"))
   //
    implementation ("io.data2viz.d2v:d2v-core-js:$d2vVersion")
    implementation ("io.data2viz.charts:core:$chartsVersion")
    //implementation("ru.vandiagnost.ktor-sample:ru.vandiagnost.ktor-sample-0.0.1")
   // implementation("org.jetbrains.kotlinx:kotlinx-css-js:1.4.2-pre.132-kotlin-1.5.30")
    implementation("ru.vandiagnost.ktor-sample:model1-js-0.0.1")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-redux:4.1.2-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-redux:7.2.6-pre.346")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.3")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-tanstack-react-query:4.10.1-pre.400")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-tanstack-react-query-devtools:4.12.0-pre.408")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.10-kotlin-1.2.20")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.0-pre.229-kotlin-1.5.21")
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-react:1.5.2")

    implementation(npm("axios", "0.24.0"))
    implementation(npm("cross-fetch", "3.1.5"))
    implementation(npm("axios", "0.24.0"))
}
kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
}

tasks.register<Copy>("copyBuild") {
    from("/build/distributions/client1.js", "/build/distributions/client1.js.map")
    into("../src/main/resources/")
}
tasks.register<Copy>("copyBuildToBuild") {
    from("/build/distributions/client1.js", "/build/distributions/client1.js.map")
    into("../build/resources/main/")
}




tasks.named("build") { finalizedBy("copyBuild") }
tasks.named("build") { finalizedBy("copyBuildToBuild") }
tasks.named("browserDevelopmentWebpack") { finalizedBy("copyBuild") }
tasks.named("browserDevelopmentWebpack") { finalizedBy("copyBuildToBuild") }