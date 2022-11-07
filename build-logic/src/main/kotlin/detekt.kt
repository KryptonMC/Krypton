import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import java.io.File

fun Project.setupDetekt() {
    extensions.configure<DetektExtension>("detekt") {
        buildUponDefaultConfig = true
        config = files(File("${rootProject.projectDir}/config/detekt.yml"), file("config/detekt.yml"))
        baseline = file("config/baseline.xml")
    }
    tasks.named<Detekt>("detekt") {
        reports {
            html.required.set(true)
            xml.required.set(true)
        }
    }
}
