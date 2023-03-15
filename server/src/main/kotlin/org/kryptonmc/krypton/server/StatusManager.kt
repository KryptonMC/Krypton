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
package org.kryptonmc.krypton.server

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.packet.out.status.ServerStatus
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.util.random.RandomSource
import kotlin.math.min

class StatusManager(private val playerManager: PlayerManager, motd: Component, maxPlayers: Int) {

    private val random = RandomSource.create()
    private val status = ServerStatus(motd, ServerStatus.Players(maxPlayers, playerManager.players().size), null)
    private var statusInvalidated = false
    private var statusInvalidatedTime = 0L
    private var lastStatus = 0L

    fun status(): ServerStatus = status

    fun tick(time: Long) {
        if (statusInvalidated && time - statusInvalidatedTime > WAIT_AFTER_INVALID_STATUS_TIME || time - lastStatus >= UPDATE_STATUS_INTERVAL) {
            updateStatus(time)
        }
    }

    private fun updateStatus(time: Long) {
        lastStatus = time
        statusInvalidated = false
        statusInvalidatedTime = 0L
        val playersOnline = playerManager.players().size
        status.players.online = playersOnline
        val sampleSize = min(playersOnline, MAXIMUM_SAMPLED_PLAYERS)
        val playerOffset = Maths.nextInt(random, 0, playersOnline - sampleSize)
        val sample = Array(sampleSize) { playerManager.players().get(it + playerOffset).profile }.apply { shuffle() }
        status.players.sample = sample
    }

    fun invalidateStatus() {
        if (statusInvalidated) return
        statusInvalidated = true
        statusInvalidatedTime = System.currentTimeMillis()
        lastStatus = 0L
    }

    companion object {

        private const val UPDATE_STATUS_INTERVAL = 5000L
        private const val WAIT_AFTER_INVALID_STATUS_TIME = 1000L
        private const val MAXIMUM_SAMPLED_PLAYERS = 12
    }
}
