/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util

import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import java.util.Locale

class ChunkProgressListener(radius: Int) {

    private val area = (radius * 2 + 1) * (radius * 2 + 1)
    private var count = 0
    private var startTime = 0L
    private var nextTickTime = Long.MAX_VALUE
    private val progress: Int
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
            val message = Component.translatable("menu.preparingSpawn", Component.text(progress.clamp(0, 100)))
            LOGGER.info(TranslationBootstrap.RENDERER.render(message, Locale.ENGLISH).toPlainText())
        }
    }

    companion object {

        private val LOGGER = logger<ChunkProgressListener>()
    }
}
