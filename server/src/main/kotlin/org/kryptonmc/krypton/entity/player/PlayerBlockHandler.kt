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
package org.kryptonmc.krypton.entity.player

import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAction
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockUpdate
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.handler
import org.kryptonmc.krypton.world.block.isGameMasterBlock
import org.kryptonmc.krypton.world.block.KryptonBlock

class PlayerBlockHandler(private val player: KryptonPlayer) {

    private val world: KryptonWorld
        get() = player.world
    private val server: KryptonServer
        get() = player.server
    private val isCreative: Boolean
        get() = player.gameMode == GameMode.CREATIVE

    private var currentTick = 0
    private var isDestroying = false
    private var startingDestroyProgress = 0
    private var destroyingX = 0
    private var destroyingY = 0
    private var destroyingZ = 0
    private var hasDelayedDestroy = false
    private var delayedDestroyingX = 0
    private var delayedDestroyingY = 0
    private var delayedDestroyingZ = 0
    private var delayedDestroyTickStart = 0
    private var lastSentState = -1

    fun tick() {
        currentTick++
        if (hasDelayedDestroy) {
            val x = delayedDestroyingX
            val y = delayedDestroyingY
            val z = delayedDestroyingZ
            val block = world.getBlock(x, y, z)
            if (block.isAir) {
                hasDelayedDestroy = false
                return
            }
            if (incrementDestroyProgress(block, x, y, z, delayedDestroyTickStart) >= 1F) {
                hasDelayedDestroy = false
                destroyBlock(x, y, z)
            }
            return
        }
        if (isDestroying) {
            val block = world.getBlock(destroyingX, destroyingY, destroyingZ)
            if (!block.isAir) {
                incrementDestroyProgress(block, destroyingX, destroyingY, destroyingZ, startingDestroyProgress)
                return
            }
            world.broadcastBlockDestroyProgress(player.id, destroyingX, destroyingY, destroyingZ, -1)
            lastSentState = -1
            isDestroying = false
        }
    }

    fun handleBlockBreak(packet: PacketInPlayerAction) {
        handleBlockBreak(packet.x, packet.y, packet.z, packet.action, world.maximumBuildHeight)
    }

    private fun handleBlockBreak(x: Int, y: Int, z: Int, action: PacketInPlayerAction.Action, maxHeight: Int) {
        val distanceX = player.location.x() - (x.toDouble() + 0.5)
        val distanceY = player.location.y() - (y.toDouble() + 0.5) + 1.5
        val distanceZ = player.location.z() - (z.toDouble() + 0.5)
        val distanceSquared = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ
        if (distanceSquared > MAXIMUM_DISTANCE_FROM_BLOCK) {
            logBlockBreakUpdate(x, y, z, false, "cannot break blocks further than 6 blocks away")
            return
        }
        if (y >= maxHeight) {
            logBlockBreakUpdate(x, y, z, false, "cannot break blocks above $maxHeight")
            return
        }
        when (action) {
            PacketInPlayerAction.Action.START_DIGGING -> {
                if (!world.canInteract(player, x, z)) {
                    if (server.config.world.sendSpawnProtectionMessage) player.sendActionBar(server.config.world.spawnProtectionMessage)
                    logBlockBreakUpdate(x, y, z, false, "cannot break blocks in spawn protection")
                    return
                }
                if (isCreative) {
                    destroyAndAcknowledge(x, y, z, "creative block break")
                    return
                }
                if (player.isBlockActionRestricted(x, y, z)) {
                    logBlockBreakUpdate(x, y, z, false, "cannot break blocks while in this state!")
                    return
                }
                startingDestroyProgress = currentTick
                var destroyProgress = 1F
                val block = world.getBlock(x, y, z)
                if (!block.isAir) {
                    block.handler().attack(player, world, block, x, y, z)
                    destroyProgress = block.handler().calculateDestroyProgress(player, world, block, x, y, z)
                }

                if (!block.isAir && destroyProgress >= 1F) {
                    destroyAndAcknowledge(x, y, z, "instant mine")
                    return
                }
                if (isDestroying) {
                    val reason = "aborted because another block is already being destroyed (client instantly mined block, we didn't like that)"
                    logBlockBreakUpdate(x, y, z, false, reason)
                }
                isDestroying = true
                destroyingX = x
                destroyingY = y
                destroyingZ = z
                val state = (destroyProgress * 10F).toInt()
                world.broadcastBlockDestroyProgress(player.id, x, y, z, state)
                logBlockBreakUpdate(x, y, z, true, "started breaking block")
                lastSentState = state
            }
            PacketInPlayerAction.Action.FINISH_DIGGING -> {
                if (x != destroyingX || y != destroyingY || z != destroyingZ) return
                val tickDifference = currentTick - startingDestroyProgress
                val block = world.getBlock(x, y, z)
                if (block.isAir) return
                val destroyProgress = block.handler().calculateDestroyProgress(player, world, block, x, y, z) * (tickDifference + 1).toFloat()
                if (destroyProgress >= 0.7F) {
                    isDestroying = false
                    world.broadcastBlockDestroyProgress(player.id, x, y, z, -1)
                    destroyAndAcknowledge(x, y, z, "block broke")
                    return
                }
                if (!hasDelayedDestroy) {
                    isDestroying = false
                    hasDelayedDestroy = true
                    delayedDestroyingX = x
                    delayedDestroyingY = y
                    delayedDestroyingZ = z
                    delayedDestroyTickStart = startingDestroyProgress
                }
                logBlockBreakUpdate(x, y, z, true, "stopped breaking block")
            }
            PacketInPlayerAction.Action.CANCEL_DIGGING -> {
                isDestroying = false
                if (x != destroyingX || y != destroyingY || z != destroyingZ) {
                    LOGGER.warn("Mismatched destroy position! Expected $destroyingX, $destroyingY, $destroyingZ and got $x, $y, $z!")
                    world.broadcastBlockDestroyProgress(player.id, destroyingX, destroyingY, destroyingZ, -1)
                    logBlockBreakUpdate(x, y, z, true, "aborted mismatched block breaking")
                }
                world.broadcastBlockDestroyProgress(player.id, x, y, z, -1)
                logBlockBreakUpdate(x, y, z, true, "aborted block breaking")
            }
            else -> Unit
        }
    }

    private fun incrementDestroyProgress(block: KryptonBlock, x: Int, y: Int, z: Int, startTick: Int): Float {
        val tickDifference = currentTick - startTick
        val progress = block.handler().calculateDestroyProgress(player, world, block, x, y, z) * (tickDifference + 1).toFloat()
        val state = (progress * 10F).toInt()
        if (state != lastSentState) {
            player.world.broadcastBlockDestroyProgress(player.id, x, y, z, state)
            lastSentState = state
        }
        return progress
    }

    private fun destroyAndAcknowledge(x: Int, y: Int, z: Int, message: String) {
        val success = destroyBlock(x, y, z)
        logBlockBreakUpdate(x, y, z, success, message)
        if (success) player.session.send(PacketOutBlockUpdate(world.getBlock(x, y, z), x, y, z))
    }

    private fun destroyBlock(x: Int, y: Int, z: Int): Boolean {
        val block = world.getBlock(x, y, z)

        // Check some conditions first
        if (!player.inventory.mainHand.type.handler().canAttackBlock(player, world, block, x, y, z)) return false
        if (block.isGameMasterBlock() && !player.canUseGameMasterBlocks) return false
        if (player.isBlockActionRestricted(x, y, z)) return false

        // Call pre-destroy, try and remove the block, and if we changed the block, call destroy
        block.handler().preDestroy(player, world, block, x, y, z)
        val hasChanged = world.removeBlock(x, y, z)
        if (hasChanged) block.handler().destroy(world, x, y, z, block)

        if (isCreative) return true // We're done, since the bit after this is for mining, which doesn't happen in creative
        val item = player.inventory.mainHand
        val hasCorrectTool = player.hasCorrectTool(block)
        item.type.handler().mineBlock(player, item, world, block, x, y, z)
        if (hasChanged && hasCorrectTool) block.handler().onDestroy(player, block, x, y, z, item)
        return true
    }

    private fun logBlockBreakUpdate(x: Int, y: Int, z: Int, success: Boolean, message: String) {
        if (success) {
            LOGGER.debug("Player ${player.profile.name} updated block break progress at $x, $y, $z: $message")
        } else {
            LOGGER.debug("Player ${player.profile.name} attempted to break block at $x, $y, $z but could not. Reason: $message")
        }
    }

    companion object {

        private const val MAXIMUM_DISTANCE_FROM_BLOCK = 6.0 * 6.0
        private val LOGGER = logger<PlayerBlockHandler>()
    }
}
