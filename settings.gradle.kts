rootProject.name = "krypton"

setOf("api", "server").forEach {
    include(it)
    findProject(":$it")?.name = "${rootProject.name}-$it"
}
