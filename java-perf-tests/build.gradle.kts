plugins {
    id("java")
    kotlin("jvm") version "1.9.10"

    id("me.champeau.jmh") version "0.7.1"
    id("io.freefair.lombok") version "8.3"
}

group = "org.bhuber"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.projectlombok:lombok:1.18.30")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    jmh("org.projectlombok:lombok:1.18.30")
}


kotlin {
    jvmToolchain(19)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(19)
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
    //fork = 1
    fork = 0

    //threads = 2
}