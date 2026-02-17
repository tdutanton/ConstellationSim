plugins {
    java
    id("org.springframework.boot") version "3.3.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("jacoco")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")

    // Lombok (production)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // ТЕСТЫ
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // ЯВНО УКАЗЫВАЕМ РЕАЛИЗАЦИЮ ТЕСТОВОГО ФРЕЙМВОРКА (устраняет предупреждение)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // Lombok для тестов
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

springBoot {
    mainClass = "constellation.App.ConstellationApplication"
}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile> {
    options.compilerArgs.add("-Xlint:-deprecation")
}

tasks.withType<JavaExec> {
    systemProperty("file.encoding", "UTF-8")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    systemProperty("file.encoding", "UTF-8")
    jvmArgs("-Dfile.encoding=UTF-8")

    finalizedBy(tasks.jacocoTestReport)
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }
}

// Jacoco
jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)
    }
}

tasks.bootRun {
    systemProperty("file.encoding", "UTF-8")
}