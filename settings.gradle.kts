pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "krypton"

include("api")
include("server")
include("generators")
include("annotation-processor")
