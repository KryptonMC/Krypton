/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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

import org.kryptonmc.krypton.adventure.toPlainText
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.world.chunk.ChunkStatus

class ChunkProgressListener(radius: Int) {

    private val area = (radius * 2 + 1) * (radius * 2 + 1)
    private var count = 0
    private var startTime = 0L
    private var nextTickTime = Long.MAX_VALUE
    private val progress: Int
        get() = Maths.floor(count.toFloat() * FRACTION_TO_PERCENTAGE / area.toFloat())

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
            nextTickTime += MILLISECONDS_PER_TICK
            LOGGER.info(TranslationBootstrap.render(Messages.PREPARING_SPAWN.build(Maths.clamp(progress, 0, 100))).toPlainText())
        }
    }

    companion object {

        private val LOGGER = logger<ChunkProgressListener>()
        private const val MILLISECONDS_PER_TICK = 50L
        private const val FRACTION_TO_PERCENTAGE = 100F
    }
}
