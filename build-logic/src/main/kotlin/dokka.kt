import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskContainer
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.GradleDokkaSourceSetBuilder

/**
 * For the newer Javadocs that use element-list as the name of the file with the package list in it rather
 * than package-list.
 */
fun GradleDokkaSourceSetBuilder.modernJavadocLink(url: String) {
    externalDocumentationLink(url, "${url}element-list")
}

val TaskContainer.dokkaTasks: TaskCollection<DokkaTask>
    get() = withType(DokkaTask::class.java)

fun DokkaTask.configureSourceSets(action: GradleDokkaSourceSetBuilder.() -> Unit) {
    dokkaSourceSets.named("main").configure(action)
}
