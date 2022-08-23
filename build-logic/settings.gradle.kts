rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories {
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
    // You're a great IDE IntelliJ, but sometimes you really annoy me. >:(
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    versionCatalogs {
        register("libs") { from(files("../gradle/libs.versions.toml")) }
        register("global") { from(files("../gradle/global.versions.toml")) }
    }
}
