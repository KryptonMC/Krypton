rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories {
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
    versionCatalogs {
        register("libs") { from(files("../gradle/libs.versions.toml")) }
    }
}
