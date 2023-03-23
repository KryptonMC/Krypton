plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

dependencies {
    // Dependencies for downloads plugin
    implementation(libs.apache.httpclient)
    implementation(libs.gson)
    implementation(libs.grgit)

    // Plugins
    implementation(libs.plugin.licenser)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.dokka)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.vanillaGradle)
    implementation(libs.plugin.shadow)
    implementation(libs.plugin.ksp)
}

dependencies {
    compileOnly(files(libs::class.java.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        create("downloads-upload") {
            id = "org.kryptonmc.downloads.upload"
            implementationClass = "org.kryptonmc.downloads.UploadToApiPlugin"
        }
    }
}
