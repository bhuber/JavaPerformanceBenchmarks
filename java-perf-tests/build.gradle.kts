plugins {
    id("java")
    kotlin("jvm") version "1.9.10"

    id("me.champeau.jmh") version "0.7.1"
}

group = "org.bhuber"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}


kotlin {
    jvmToolchain(21)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// This version of kotlin doesn't support targeting Java 21, so we do a bunch of shenanigans.  Can probably remove
// once kotlin supports jdk-21
val JDK_21_PATH = "/usr/local/jdk-21"

tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    sourceCompatibility = "19"
    targetCompatibility = "19"
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.UsesKotlinJavaToolchain>().configureEach {
    kotlinJavaToolchain.jdk.use(JDK_21_PATH, JavaVersion.VERSION_19)
}

tasks.withType()

tasks.test {
    useJUnitPlatform()
}


jmh {
    warmupIterations = 1
    iterations = 1
    fork = 1
}