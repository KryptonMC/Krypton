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
package org.kryptonmc.krypton.entity.system

import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAction
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.Action
import org.kryptonmc.krypton.util.GameModes
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.spongepowered.math.vector.Vector3i

class PlayerGameModeSystem(private val player: KryptonPlayer) {

    var gameMode: GameMode = GameMode.SURVIVAL
        private set
    var previousGameMode: GameMode? = null
        private set

    private var currentTick = 0
    private var isDestroying = false
    private var startingDestroyProgress = 0
    private var destroyingPosition = Vector3i.ZERO
    private var hasDelayedDestroy = false
    private var delayedDestroyingPosition = Vector3i.ZERO
    private var delayedDestroyTickStart = 0
    private var lastSentState = -1

    fun tick() {
        currentTick++
        if (hasDelayedDestroy) {
            val block = player.world.getBlock(delayedDestroyingPosition)
            if (block.isAir) {
                hasDelayedDestroy = false
                return
            }
            if (incrementDestroyProgress(block, delayedDestroyingPosition, delayedDestroyTickStart) >= 1F) {
                hasDelayedDestroy = false
                destroyBlock(delayedDestroyingPosition)
            }
            return
        }
        if (isDestroying) {
            val block = player.world.getBlock(destroyingPosition)
            if (!block.isAir) {
                incrementDestroyProgress(block, destroyingPosition, startingDestroyProgress)
                return
            }
            player.world.broadcastBlockDestroyProgress(player.id, destroyingPosition, -1)
            lastSentState = -1
            isDestroying = false
        }
    }

    fun changeGameMode(mode: GameMode): Boolean {
        if (mode == gameMode) return false
        setGameMode(mode, gameMode)
        return true
    }

    fun setGameMode(mode: GameMode, previousMode: GameMode?) {
        previousGameMode = previousMode
        gameMode = mode
        GameModes.updatePlayerAbilities(mode, player.abilities)
        player.onAbilitiesUpdate()
        player.server.sessionManager.sendGrouped(PacketOutPlayerInfo(Action.UPDATE_GAMEMODE, player))
    }

    fun handleBlockBreak(packet: PacketInPlayerAction) {
        handleBlockBreak(packet.position, packet.action, player.world.maximumBuildHeight)
    }

    private fun handleBlockBreak(position: Vector3i, action: PacketInPlayerAction.Action, maxHeight: Int) {
        val distanceX = player.location.x() - (position.x().toDouble() + 0.5)
        val distanceY = player.location.y() - (position.y().toDouble() + 0.5) + 1.5
        val distanceZ = player.location.z() - (position.z().toDouble() + 0.5)
        val distanceSquared = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ
        if (distanceSquared > MAXIMUM_DISTANCE_FROM_BLOCK) {
            logBlockBreakUpdate(position, false, "cannot break blocks further than 6 blocks away")
            return
        }
        if (position.y() >= maxHeight) {
            logBlockBreakUpdate(position, false, "cannot break blocks above $maxHeight")
            return
        }
        when (action) {
            PacketInPlayerAction.Action.START_DIGGING -> {
                if (!player.world.canInteract(player, position.x(), position.z())) {
                    sendSpawnProtectionMessage()
                    logBlockBreakUpdate(position, false, "cannot break blocks in spawn protection")
                    return
                }
                if (player.gameMode == GameMode.CREATIVE) {
                    destroyAndAcknowledge(position, "creative block break")
                    return
                }
                if (player.isBlockActionRestricted(position)) {
                    player.session.send(PacketOutBlockUpdate(player.world.getBlock(position), position))
                    logBlockBreakUpdate(position, false, "cannot break blocks while in this state!")
                    return
                }
                startingDestroyProgress = currentTick
                var destroyProgress = 1F
                val block = player.world.getBlock(position)
                if (!block.isAir) {
                    block.attack(player.world, position, player)
                    destroyProgress = block.getDestroyProgress(player, player.world, position)
                }

                if (!block.isAir && destroyProgress >= 1F) {
                    destroyAndAcknowledge(position, "instant mine")
                    return
                }
                if (isDestroying) {
                    player.session.send(PacketOutBlockUpdate(player.world.getBlock(destroyingPosition), destroyingPosition))
                    val reason = "aborted because another block is already being destroyed (client instantly mined block, we didn't like that)"
                    logBlockBreakUpdate(position, false, reason)
                }
                isDestroying = true
                destroyingPosition = position
                val state = (destroyProgress * 10F).toInt()
                player.world.broadcastBlockDestroyProgress(player.id, position, state)
                logBlockBreakUpdate(position, true, "started breaking block")
                lastSentState = state
            }
            PacketInPlayerAction.Action.FINISH_DIGGING -> {
                if (position != destroyingPosition) return
                val tickDifference = currentTick - startingDestroyProgress
                val block = player.world.getBlock(position)
                if (block.isAir) return
                val destroyProgress = block.getDestroyProgress(player, player.world, position) * (tickDifference + 1).toFloat()
                if (destroyProgress >= MAXIMUM_DESTROY_PROGRESS) {
                    isDestroying = false
                    player.world.broadcastBlockDestroyProgress(player.id, position, -1)
                    destroyAndAcknowledge(position, "block broke")
                    return
                }
                if (!hasDelayedDestroy) {
                    isDestroying = false
                    hasDelayedDestroy = true
                    delayedDestroyingPosition = position
                    delayedDestroyTickStart = startingDestroyProgress
                }
                logBlockBreakUpdate(position, true, "stopped breaking block")
            }
            PacketInPlayerAction.Action.CANCEL_DIGGING -> {
                isDestroying = false
                if (position != destroyingPosition) {
                    LOGGER.warn("Mismatched destroy position! Expected $destroyingPosition and got $position!")
                    player.world.broadcastBlockDestroyProgress(player.id, destroyingPosition, -1)
                    logBlockBreakUpdate(position, true, "aborted mismatched block breaking")
                }
                player.world.broadcastBlockDestroyProgress(player.id, position, -1)
                logBlockBreakUpdate(position, true, "aborted block breaking")
            }
            else -> Unit
        }
    }

    private fun sendSpawnProtectionMessage() {
        if (player.server.config.world.sendSpawnProtectionMessage) player.sendActionBar(player.server.config.world.spawnProtectionMessage)
    }

    private fun incrementDestroyProgress(block: KryptonBlockState, position: Vector3i, startTick: Int): Float {
        val tickDifference = currentTick - startTick
        val progress = block.getDestroyProgress(player, player.world, position) * (tickDifference + 1).toFloat()
        val state = (progress * 10F).toInt()
        if (state != lastSentState) {
            player.world.broadcastBlockDestroyProgress(player.id, position, state)
            lastSentState = state
        }
        return progress
    }

    private fun destroyAndAcknowledge(position: Vector3i, message: String) {
        val success = destroyBlock(position)
        logBlockBreakUpdate(position, success, message)
        if (success) player.session.send(PacketOutBlockUpdate(player.world.getBlock(position), position))
    }

    private fun destroyBlock(position: Vector3i): Boolean {
        val state = player.world.getBlock(position)
        val block = state.block

        // Check some conditions first
        if (!player.inventory.mainHand.type.handler().canAttackBlock(player, player.world, state, position.x(), position.y(), position.z())) {
            return false
        }
        if (!player.canUseGameMasterBlocks) return false // FIXME: Check if is instance of GameMasterBlock
        if (player.isBlockActionRestricted(position)) return false

        // Call pre-destroy, try and remove the block, and if we changed the block, call destroy
        block.playerWillDestroy(player.world, position, state, player)
        val hasChanged = player.world.removeBlock(position.x(), position.y(), position.z())
        if (hasChanged) block.destroy(player.world, position, state)

        if (player.gameMode == GameMode.CREATIVE) return true // We're done, since the bit after this is for mining, which doesn't happen in creative
        val item = player.inventory.mainHand
        val hasCorrectTool = player.hasCorrectTool(state)
        item.type.handler().mineBlock(player, item, player.world, state, position.x(), position.y(), position.z())
        if (hasChanged && hasCorrectTool) block.playerDestroy(player.world, player, position, state, null, item)
        return true
    }

    private fun logBlockBreakUpdate(position: Vector3i, success: Boolean, message: String) {
        if (success) {
            LOGGER.debug("Player ${player.profile.name} updated block break progress at $position: $message")
        } else {
            LOGGER.debug("Player ${player.profile.name} attempted to break block at $position but could not. Reason: $message")
        }
    }

    companion object {

        private const val MAXIMUM_DISTANCE_FROM_BLOCK = 6.0 * 6.0
        private const val MAXIMUM_DESTROY_PROGRESS = 0.7F
        private val LOGGER = logger<PlayerGameModeSystem>()
    }
}
