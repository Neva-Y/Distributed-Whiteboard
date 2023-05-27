plugins {
    id("java")
    id("io.spring.dependency-management") version Versions.SPRING_DEPENDENCY_MANAGEMENT_VERSION
    id("org.springframework.boot") version Versions.SPRING_BOOT_VERSION apply false
    id ("org.openjfx.javafxplugin") version Versions.JAVA_FX_VERSION
    `java-library`
    application
}

group = "com.project"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

java.sourceCompatibility = JavaVersion.VERSION_11

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.antlr:antlr4-runtime:${Versions.ANTLR_RUNTIME_VERSION}")
        classpath("org.openjfx:javafx-plugin:${Versions.JAVA_FX_VERSION}")
    }
}

javafx {
    modules = mutableListOf("javafx.controls", "javafx.fxml")
    version = "11.0.2"
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java-library")
    apply(plugin = "org.openjfx.javafxplugin")

    java {
        withSourcesJar()
    }
    configurations {
        all {
            exclude("org.springframework.boot", "spring-boot-starter-logging")
        }
    }
}
