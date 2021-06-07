import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.cadixdev.licenser")
    id("info.solidsoft.pitest")
    id("io.gitlab.arturbosch.detekt")
}

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
    maven("https://repo.bristermitten.me/repository/maven-public/")
    jcenter()
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt", "detekt-formatting", "1.17.1")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.platform", "junit-platform-runner", Versions.JUNIT_PLATFORM_RUNNER)
    testImplementation("io.mockk", "mockk", Versions.MOCKK)
    testRuntimeOnly("net.bytebuddy", "byte-buddy", Versions.BYTEBUDDY)
}

configurations.all {
    exclude("org.checkerframework", "checker-qual")
    exclude("junit")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

pitest {
    junit5PluginVersion.set("0.12")
    pitestVersion.set("1.6.7")
    avoidCallsTo.set(setOf(
        "kotlin.jvm.internal",
        "java.util.logging",
        "org.slf4j",
        "org.apache.logging.log4j"
    ))
    mutators.set(setOf(
        "CONDITIONALS_BOUNDARY",
        "INCREMENTS",
        "INVERT_NEGS",
        "MATH",
        "NEGATE_CONDITIONALS",
        "VOID_METHOD_CALLS",
        "EMPTY_RETURNS",
        "FALSE_RETURNS",
        "TRUE_RETURNS",
        "PRIMITIVE_RETURNS"
    ))
    targetClasses.set(setOf("org.kryptonmc.*"))
    targetTests.set(setOf("org.kryptonmc.*"))
    threads.set(Runtime.getRuntime().availableProcessors())
    outputFormats.set(setOf("HTML", "XML"))
    excludedMethods.set(setOf("equals", "hashCode", "toString", "emptyList"))
}

detekt {
    buildUponDefaultConfig = true
    config = files("$projectDir/config/detekt.yml")
    baseline = file("$projectDir/config/baseline.xml")

    reports {
        html.enabled = true
        xml.enabled = true
    }
}

val compiler: Provider<JavaCompiler> = javaToolchains.compilerFor {
    languageVersion.set(JavaLanguageVersion.of(16))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
            freeCompilerArgs = listOf("-Xjvm-default=all")
            jdkHome = compiler.get().metadata.installationPath.asFile.absolutePath
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

tasks["build"].dependsOn(tasks["test"])
