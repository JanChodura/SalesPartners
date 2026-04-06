import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

group = "com.jzchodura.salespartners"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.13"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.21.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.openapitools:jackson-databind-nullable:0.2.7")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    dependsOn("openApiGenerate")
}

openApiGenerate {
    generatorName.set("spring")

    inputSpec.set("$projectDir/src/main/resources/openapi.yaml")
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)

    apiPackage.set("$group.generated.api")
    modelPackage.set("$group.generated.dto")

    configOptions.set(
        mapOf(
            "interfaceOnly" to "false",
            "delegatePattern" to "true",
            "useTags" to "true",
            "useSpringBoot3" to "true",
            "useSpringController" to "true",
            "useResponseEntity" to "true"
        )
    )
}

sourceSets {
    named("main") {
        java.srcDir(layout.buildDirectory.dir("generated/src/main/java"))
    }
}