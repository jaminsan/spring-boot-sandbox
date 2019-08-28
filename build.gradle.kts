import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

	val kotlinVersion by extra { "1.3.31" }
	val junitPlatformVersion by extra { "1.0.3" }
	val junitJupiterVersion by extra { "5.0.3" }
	val log4jVersion by extra { "2.9.0" }
	val arrowVersion by extra { "0.9.1-SNAPSHOT"}

	repositories {
		jcenter()
	}

	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("org.junit.platform:junit-platform-gradle-plugin:$junitPlatformVersion")
	}

}


plugins {
	base
	java
	id("com.vanniktech.dependency.graph.generator") version "0.2.0"

	id("org.springframework.boot") version "2.1.5.RELEASE" apply false
	id("io.spring.dependency-management") version "1.0.7.RELEASE" apply false
	kotlin("jvm") version "1.3.31" apply false
	kotlin("plugin.spring") version "1.3.31" apply false
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}

allprojects {

	val kotlinVersion: String by rootProject.extra
	val junitJupiterVersion: String by rootProject.extra
	val log4jVersion: String by rootProject.extra

	apply {
		plugin("kotlin")
		plugin("org.junit.platform.gradle.plugin")
	}

	repositories {
		jcenter()
		mavenCentral()
		maven("https://oss.sonatype.org/content/repositories/snapshots")
		maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
		maven("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
	}

	dependencies {
		implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
		implementation("io.github.microutils:kotlin-logging:1.6.24")

		// JUnit Jupiter API and TestEngine implementation
		testCompile("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
		testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")

		// To use Log4J's LogManager
		testRuntime("org.apache.logging.log4j:log4j-core:$log4jVersion")
		testRuntime("org.apache.logging.log4j:log4j-jul:$log4jVersion")

		testCompile("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
		testCompile("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
		testCompile("io.mockk:mockk:1.8.12.kotlin13")
	}
}

tasks.withType<Wrapper> {
	gradleVersion = "5.4.1"
}

