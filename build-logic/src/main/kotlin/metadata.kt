import net.kyori.indra.git.IndraGitExtension
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.named

fun Project.applySpecJarMetadata(moduleName: String, specTitle: String) {
    configureJarMetadata(moduleName) {
        put("Specification-Title", specTitle)
        put("Specification-Vendor", "KryptonMC")
        put("Specification-Version", version.toString())
    }
}

fun Project.applyImplJarMetadata(moduleName: String, implTitle: String, extra: MutableMap<String, String>.() -> Unit) {
    configureJarMetadata(moduleName) {
        put("Implementation-Title", implTitle)
        put("Implementation-Vendor", "KryptonMC")
        put("Implementation-Version", version.toString())
        extra(this)
    }
}

private inline fun Project.configureJarMetadata(moduleName: String, crossinline action: MutableMap<String, String>.() -> Unit) {
    if (!tasks.names.contains("jar")) return
    tasks.named<Jar>("jar").configure {
        manifest {
            val attributes = mutableMapOf("Automatic-Module-Name" to moduleName)
            action(attributes)
            attributes(attributes)
            extensions.findByType<IndraGitExtension>()?.applyVcsInformationToManifest(this)
        }
    }
}
