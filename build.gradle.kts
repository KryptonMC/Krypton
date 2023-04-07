plugins {
    id("krypton.base-conventions")
    id("org.ajoberstar.grgit") version "4.1.1"
}

allprojects {
    group = "org.kryptonmc"
    version = grgit.head().id.take(10)
    description = "Free and open-source Minecraft server software, written from the ground up."
}
