/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.locale.MinecraftTranslationManager
import org.kryptonmc.krypton.world.chunk.data.ChunkStatus

class ChunkProgressListener(radius: Int) {

    private val area = (radius * 2 + 1) * (radius * 2 + 1)
    private var count = 0
    private var startTime = 0L
    private var nextTickTime = Long.MAX_VALUE

    private fun calculateProgress(): Int = Maths.floor(count.toFloat() * FRACTION_TO_PERCENTAGE / area.toFloat())

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
        val progress = calculateProgress()
        if (System.currentTimeMillis() > nextTickTime) {
            nextTickTime += MILLISECONDS_PER_TICK
            val progressText = Component.text(Maths.clamp(progress, 0, 100))
            val message = MinecraftTranslationManager.render(Component.translatable("menu.preparingSpawn", progressText))
            LOGGER.info(PlainTextComponentSerializer.plainText().serialize(message))
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private const val MILLISECONDS_PER_TICK = 50L
        private const val FRACTION_TO_PERCENTAGE = 100F
    }
}
