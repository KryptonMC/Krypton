rootProject.name = "krypton"

setOf("api", "bootstrap", "server").forEach {
    include(it)
    findProject(":$it")?.name = "${rootProject.name}-$it"
}
