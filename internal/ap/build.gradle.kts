plugins {
    id("krypton.basic-conventions")
}

dependencies {
    implementation(libs.ksp)
    implementation(projects.internalAnnotations)
    implementation(libs.annotations)
}
