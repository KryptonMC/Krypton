import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.named

inline fun Project.configureJarMetadata(moduleName: String, crossinline action: MutableMap<String, String>.() -> Unit) {
    if (!tasks.names.contains("jar")) return
    tasks.named<Jar>("jar").configure {
        manifest {
            val attributes = mutableMapOf("Automatic-Module-Name" to moduleName)
            action(attributes)
            attributes(attributes)
        }
    }
}
