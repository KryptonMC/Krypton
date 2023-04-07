plugins {
    id("krypton.common-conventions")
}

dependencies {
    implementation(libs.ksp)
    implementation(projects.internalAnnotations)
    implementation(libs.annotations)
}
