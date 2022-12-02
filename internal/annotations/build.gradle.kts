plugins {
    id("krypton.basic-conventions")
}

kotlin {
    explicitApi()
}

dependencies {
    compileOnly(libs.annotations)
}
