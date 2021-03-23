rootProject.name = "krypton"

include("api")
include("server")

setOf("api", "server").forEach {
    findProject(":$it")?.name = "${rootProject.name}-$it"
}