import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.example"
version = "0.0.1-SNAPSHOT"

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.retry:spring-retry:1.2.4.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
}