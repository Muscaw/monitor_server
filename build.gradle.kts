plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("com.diffplug.spotless") version "6.20.0"
}

group = "dev.muscaw"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.apache.xmlgraphics:batik-svggen:1.16")
    implementation("org.apache.xmlgraphics:batik-dom:1.16")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.16")
    implementation("org.apache.xmlgraphics:batik-codec:1.16")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:3.+")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

spotless {
    java {
        importOrder()
        removeUnusedImports()

        cleanthat()

        googleJavaFormat()

        formatAnnotations()
    }
}



tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}