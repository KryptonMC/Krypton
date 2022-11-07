plugins {
    kotlin("jvm")
    id("org.cadixdev.licenser")
}

license {
    header(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine(false)
}
