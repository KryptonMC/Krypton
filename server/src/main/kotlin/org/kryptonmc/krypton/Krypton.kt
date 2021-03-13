package org.kryptonmc.krypton

import org.kryptonmc.krypton.KryptonServer.*
import org.kryptonmc.krypton.extension.logger

fun main() {
    val logger = logger("Krypton")
    logger.info("Starting Krypton server version ${KryptonServerInfo.version} for Minecraft ${KryptonServerInfo.minecraftVersion}")

    KryptonServer().start()
}