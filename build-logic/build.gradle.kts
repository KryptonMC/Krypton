plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugin.indra)
    implementation(libs.plugin.licenser)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.dokka)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.vanillaGradle)
    implementation(libs.plugin.shadow)
}

dependencies {
    compileOnly(files(libs::class.java.protectionDomain.codeSource.location))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
