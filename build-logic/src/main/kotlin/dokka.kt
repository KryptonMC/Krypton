import org.jetbrains.dokka.gradle.GradleDokkaSourceSetBuilder

/**
 * For the newer Javadocs that use element-list as the name of the file with the package list in it rather
 * than package-list.
 */
fun GradleDokkaSourceSetBuilder.modernJavadocLink(url: String) {
    externalDocumentationLink(url, "${url}element-list")
}
