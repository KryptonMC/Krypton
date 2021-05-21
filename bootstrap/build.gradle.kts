import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import io.github.slimjar.task.SlimJar
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    application
}

application.mainClass.set("org.kryptonmc.bootstrap.Main")

java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16

dependencies {
    implementation("org.ow2.asm:asm:9.1")
    implementation("org.ow2.asm:asm-commons:9.1")
    implementation("com.google.code.gson:gson:2.8.6")
}

tasks {
    withType<ShadowJar> {
        archiveFileName.set("Krypton-${project.version}.jar")
        transform(Log4j2PluginsCacheFileTransformer::class.java)
        relocate("org.bstats", "org.kryptonmc.krypton.bstats")
    }
    withType<ProcessResources> {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
    }
    withType<SlimJar> {
        isolate(project(":krypton-api"))
        isolate(project(":krypton-server"))
    }
}
