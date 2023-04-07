plugins {
    id("krypton.common-conventions")
}

kotlin {
    explicitApi()
}

dependencies {
    compileOnly(libs.annotations)
}
