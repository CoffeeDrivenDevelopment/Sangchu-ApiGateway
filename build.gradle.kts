plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("jacoco")
}

group = "com.cdd"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory.set(layout.buildDirectory.dir("reports/jacoco"))
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

extra["springCloudVersion"] = "2023.0.0"

dependencies {
    /* Spring Cloud Api Gateway */
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    /* Spring Cloud */
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    /* Spring Cloud Config */
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    /* Filter */
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    /* Logging */
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    /* Sangchu */
    implementation("com.github.CoffeeDrivenDevelopment:sangchu-common:0.0.3")
    /* Testing */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

/* Jacoco Start */
tasks.jacocoTestReport {
    dependsOn(":copyOasToSwagger")
    reports {
        html.required = true
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco/index.xml")
    }


    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it).apply {
                exclude(
                    "**/*Application*",
                    "**/*Exception*",
                    "**/BaseEntity*",
                    "**/ControllerAdvice*",
                    "**/dto/**",
                    "**/cond/**",
                )
            }
        })
    )


    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    dependsOn(":copyOasToSwagger")
    violationRules {
        rule {
            enabled = true
            element = "CLASS"

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal.valueOf(0)
            }

            excludes = listOf(
                "**/*Application*",
                "**/*Exception*",
                "**/BaseEntity*",
                "**/ControllerAdvice*",
                "**/dto/**",
                "**/cond/**",
            )
        }
    }
}
/* Jacoco End */