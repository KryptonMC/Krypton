pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "krypton"

sequenceOf("api", "server").forEach {
    include(it)
    findProject(":$it")?.name = "${rootProject.name}-$it"
}
include("code-generators")
