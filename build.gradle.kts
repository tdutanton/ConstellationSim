plugins {
    java
    application
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

application {
    mainClass = "constellation.ConstellationApplication"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}