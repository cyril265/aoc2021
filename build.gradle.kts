plugins {
    kotlin("jvm") version "1.6.10"
}

repositories {
    mavenCentral()
}

tasks {

    wrapper {
        gradleVersion = "7.3"
    }
}
