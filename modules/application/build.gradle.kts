plugins {
    id("idea")
    id("java")
    id("java-library")
    id("org.springframework.boot")
    id ("org.openjfx.javafxplugin") version Versions.JAVA_FX_VERSION
    id("io.spring.dependency-management")
    id("application")
}

repositories {
    mavenCentral()
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven ("https://jetbrains.bintray.com/intellij-third-party-dependencies")
}

javafx {
    modules = mutableListOf("javafx.controls", "javafx.fxml", "javafx.swing", "javafx.base")
    version = "11.0.2"
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    runtimeOnly("org.apache.logging.log4j:log4j-api:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-core:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-layout-template-json:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    implementation("org.openjfx:javafx-swing:11-ea+24")
    implementation("org.jeasy:easy-random-core:${Versions.EASY_RANDOM_VERSION}")
    implementation("org.slf4j:slf4j-api:${Versions.SLF4J_VERSION}")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-context:${Versions.SPRING_CONTEXT_VERSION}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON_VERSION}")
    implementation("org.controlsfx:controlsfx:8.0.5")

    configurations {
        all {
            exclude("spring-boot-starter-tomcat", "spring-boot-starter-logging")
        }
    }
}

application {
    mainClass.set("com.project.application.javafx.WhiteboardApp")
//    mainClass.set("com.project.application.javafx.WhiteboardServer")
}

tasks {
    bootJar {
        archiveBaseName.set("WhiteboardApp")
//        archiveBaseName.set("WhiteboardServer")
    }
}
