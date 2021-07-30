package org.kryptonmc.krypton.util

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.api.util.floor
import org.kryptonmc.krypton.locale.TranslationBootstrap
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import java.util.Locale

class ChunkProgressListener(radius: Int) {

    private val area = (radius * 2 + 1) * (radius * 2 + 1)
    private var count = 0
    private var startTime = 0L
    private var nextTickTime = Long.MAX_VALUE

    val progress: Int
        get() = (count.toFloat() * 100F / area.toFloat()).floor()

    fun stop() {
        LOGGER.info("Time elapsed: ${System.currentTimeMillis() - startTime} ms")
        nextTickTime = Long.MAX_VALUE
    }

    fun tick() {
        nextTickTime = System.currentTimeMillis()
        startTime = nextTickTime
    }

    fun updateStatus(status: ChunkStatus) {
        if (status === ChunkStatus.FULL) ++count
        val progress = progress
        if (System.currentTimeMillis() > nextTickTime) {
            nextTickTime += 50L
            LOGGER.info(TranslationBootstrap.RENDERER.render(translatable("menu.preparingSpawn", text(progress.clamp(0, 100))), Locale.ENGLISH).toPlainText())
        }
    }

    companion object {

        private val LOGGER = logger<ChunkProgressListener>()
    }
}
