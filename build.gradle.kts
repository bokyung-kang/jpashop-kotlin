import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion: String by System.getProperties()
    val springBootVersion: String by System.getProperties()

    id("org.springframework.boot") version springBootVersion apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false

    id("org.jlleitschuh.gradle.ktlint") version "10.0.0" apply false

    kotlin("jvm") version kotlinVersion apply false
    kotlin("kapt") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("plugin.jpa") version kotlinVersion apply false
}

allprojects {

    apply {
        plugin("kotlin")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.plugin.spring") // allOpen처리를 위해 모든 프로젝트에 kotlin-spring플러그인 적용
    }

    group = "com.study.jpashop"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        val implementation by configurations
        val testImplementation by configurations
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        testImplementation("com.ninja-squad:springmockk:3.1.0")
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
            exclude(module = "mockito-core")
        }
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        verbose.set(true)
        disabledRules.set(
            setOf(
                "import-ordering",
                "no-wildcard-imports",
                "final-newline",
                "insert_final_newline",
                "max_line_length"
            )
        )
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<KotlinCompile> {
        dependsOn("ktlintCheck")
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
