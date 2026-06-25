plugins {
    id("java")
    id("application")
    id("org.javamodularity.moduleplugin") version "2.0.1"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "4.0.2"
}

group = "pl.miodun"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(26)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("pl.miodun.oopfx")
    mainClass.set("pl.miodun.oopfx.HelloApplication")
}

javafx {
    version = "26.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.53.2.0")
    implementation("org.jetbrains:annotations:26.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.1.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
