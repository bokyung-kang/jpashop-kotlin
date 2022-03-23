import org.springframework.boot.gradle.tasks.bundling.BootJar

val querydslVersion: String by System.getProperties()

plugins {
    kotlin("kapt")
    kotlin("plugin.jpa")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    val kapt by configurations
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("com.querydsl:querydsl-jpa:$querydslVersion")
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.0")
    runtimeOnly("org.postgresql:postgresql")
}

tasks.withType<Jar> {
    enabled = true
}

tasks.withType<BootJar> {
    enabled = false
}
