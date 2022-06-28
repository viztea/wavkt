plugins {
    application
    kotlin("jvm") version "1.7.0"
}

application {
    mainClass.set("wav.WavKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.soywiz.korlibs.korio:korio-jvm:2.7.0")
}
