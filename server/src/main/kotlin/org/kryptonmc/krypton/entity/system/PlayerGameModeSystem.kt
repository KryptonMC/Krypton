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

import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.event.player.ChangeGameModeEvent
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.player.KryptonChangeGameModeEvent
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAction
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.Action
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.GameModes
import org.kryptonmc.krypton.world.block.state.KryptonBlockState

class PlayerGameModeSystem(private val player: KryptonPlayer) {

    var gameMode: GameMode = GameMode.SURVIVAL
        private set
    var previousGameMode: GameMode? = null
        private set

    private var currentTick = 0
    private var isDestroying = false
    private var startDestroyProgress = 0
    private var destroyPos = BlockPos.ZERO
    private var hasDelayedDestroy = false
    private var delayedDestroyPos = BlockPos.ZERO
    private var delayedTickStart = 0
    private var lastSentState = -1

    fun tick() {
        currentTick++
        if (hasDelayedDestroy) {
            val block = player.world.getBlock(delayedDestroyPos)
            if (block.isAir) {
                hasDelayedDestroy = false
                return
            }
            if (incrementDestroyProgress(block, delayedDestroyPos, delayedTickStart) >= 1F) {
                hasDelayedDestroy = false
                destroyBlock(delayedDestroyPos)
            }
            return
        }
        if (isDestroying) {
            val block = player.world.getBlock(destroyPos)
            if (!block.isAir) {
                incrementDestroyProgress(block, destroyPos, startDestroyProgress)
                return
            }
            player.world.broadcastBlockDestroyProgress(player.id, destroyPos, -1)
            lastSentState = -1
            isDestroying = false
        }
    }

    fun changeGameMode(mode: GameMode, cause: ChangeGameModeEvent.Cause): ChangeGameModeEvent? {
        if (mode == gameMode) return null
        val event = player.server.eventManager.fireSync(KryptonChangeGameModeEvent(player, gameMode, mode, cause))
        if (!event.result.isAllowed) return event
        setGameMode(mode, gameMode)
        return event
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

    private fun handleBlockBreak(pos: BlockPos, action: PacketInPlayerAction.Action, maxHeight: Int) {
        val dx = player.position.x - (pos.x.toDouble() + 0.5)
        val dy = player.position.y - (pos.y.toDouble() + 0.5) + 1.5
        val dz = player.position.z - (pos.z.toDouble() + 0.5)
        val squareDistance = dx * dx + dy * dy + dz * dz
        if (squareDistance > MAXIMUM_DISTANCE_FROM_BLOCK) {
            logBlockBreakUpdate(pos, false, "cannot break blocks further than 6 blocks away")
            return
        }
        if (pos.y >= maxHeight) {
            logBlockBreakUpdate(pos, false, "cannot break blocks above $maxHeight")
            return
        }
        when (action) {
            PacketInPlayerAction.Action.START_DIGGING -> {
                if (!player.world.canInteract(player, pos.x, pos.z)) {
                    sendSpawnProtectionMessage()
                    logBlockBreakUpdate(pos, false, "cannot break blocks in spawn protection")
                    return
                }
                if (player.gameMode == GameMode.CREATIVE) {
                    destroyAndAcknowledge(pos, "creative block break")
                    return
                }
                if (player.isBlockActionRestricted(pos)) {
                    player.session.send(PacketOutBlockUpdate(pos, player.world.getBlock(pos)))
                    logBlockBreakUpdate(pos, false, "cannot break blocks while in this state!")
                    return
                }
                startDestroyProgress = currentTick
                var destroyProgress = 1F
                val block = player.world.getBlock(pos)
                if (!block.isAir) {
                    block.attack(player.world, pos, player)
                    destroyProgress = block.getDestroyProgress(player, player.world, pos)
                }

                if (!block.isAir && destroyProgress >= 1F) {
                    destroyAndAcknowledge(pos, "instant mine")
                    return
                }
                if (isDestroying) {
                    player.session.send(PacketOutBlockUpdate(destroyPos, player.world.getBlock(destroyPos)))
                    val reason = "aborted because another block is already being destroyed (client instantly mined block, we didn't like that)"
                    logBlockBreakUpdate(pos, false, reason)
                }
                isDestroying = true
                destroyPos = pos.immutable()
                val state = (destroyProgress * 10F).toInt()
                player.world.broadcastBlockDestroyProgress(player.id, pos, state)
                logBlockBreakUpdate(pos, true, "started breaking block")
                lastSentState = state
            }
            PacketInPlayerAction.Action.FINISH_DIGGING -> {
                if (pos != destroyPos) return
                val tickDifference = currentTick - startDestroyProgress
                val block = player.world.getBlock(pos)
                if (block.isAir) return
                val destroyProgress = block.getDestroyProgress(player, player.world, pos) * (tickDifference + 1).toFloat()
                if (destroyProgress >= MAXIMUM_DESTROY_PROGRESS) {
                    isDestroying = false
                    player.world.broadcastBlockDestroyProgress(player.id, pos, -1)
                    destroyAndAcknowledge(pos, "block broke")
                    return
                }
                if (!hasDelayedDestroy) {
                    isDestroying = false
                    hasDelayedDestroy = true
                    delayedDestroyPos = pos
                    delayedTickStart = startDestroyProgress
                }
                logBlockBreakUpdate(pos, true, "stopped breaking block")
            }
            PacketInPlayerAction.Action.CANCEL_DIGGING -> {
                isDestroying = false
                if (pos != destroyPos) {
                    LOGGER.warn("Mismatched destroy position! Expected $destroyPos and got $pos!")
                    player.world.broadcastBlockDestroyProgress(player.id, destroyPos, -1)
                    logBlockBreakUpdate(pos, true, "aborted mismatched block breaking")
                }
                player.world.broadcastBlockDestroyProgress(player.id, pos, -1)
                logBlockBreakUpdate(pos, true, "aborted block breaking")
            }
            else -> Unit
        }
    }

    private fun sendSpawnProtectionMessage() {
        if (player.server.config.world.sendSpawnProtectionMessage) player.sendActionBar(player.server.config.world.spawnProtectionMessage)
    }

    private fun incrementDestroyProgress(block: KryptonBlockState, pos: BlockPos, startTick: Int): Float {
        val tickDifference = currentTick - startTick
        val progress = block.getDestroyProgress(player, player.world, pos) * (tickDifference + 1).toFloat()
        val state = (progress * 10F).toInt()
        if (state != lastSentState) {
            player.world.broadcastBlockDestroyProgress(player.id, pos, state)
            lastSentState = state
        }
        return progress
    }

    private fun destroyAndAcknowledge(pos: BlockPos, message: String) {
        val success = destroyBlock(pos)
        logBlockBreakUpdate(pos, success, message)
        if (success) player.session.send(PacketOutBlockUpdate(pos, player.world.getBlock(pos)))
    }

    private fun destroyBlock(pos: BlockPos): Boolean {
        val state = player.world.getBlock(pos)
        val block = state.block
        if (!player.inventory.mainHand.type.handler().canAttackBlock(player, player.world, state, pos)) return false

        // Check some conditions first
        if (!player.canUseGameMasterBlocks) { // FIXME: Check if is instance of GameMasterBlock
            player.world.sendBlockUpdated(pos, state, state)
            return false
        }
        if (player.isBlockActionRestricted(pos)) return false

        // Call pre-destroy, try and remove the block, and if we changed the block, call destroy
        block.playerWillDestroy(player.world, pos, state, player)
        val hasChanged = player.world.removeBlock(pos, false)
        if (hasChanged) block.destroy(player.world, pos, state)

        if (player.gameMode == GameMode.CREATIVE) return true // We're done, since the bit after this is for mining, which doesn't happen in creative
        val item = player.inventory.mainHand
        val hasCorrectTool = player.hasCorrectTool(state)
        item.type.handler().mineBlock(player, item, player.world, state, pos)
        if (hasChanged && hasCorrectTool) block.playerDestroy(player.world, player, pos, state, null, item)
        return true
    }

    private fun logBlockBreakUpdate(position: BlockPos, success: Boolean, message: String) {
        if (success) {
            LOGGER.debug("Player ${player.profile.name} updated block break progress at $position: $message")
        } else {
            LOGGER.debug("Player ${player.profile.name} attempted to break block at $position but could not. Reason: $message")
        }
    }

    companion object {

        private const val MAXIMUM_DISTANCE_FROM_BLOCK = 6.0 * 6.0
        private const val MAXIMUM_DESTROY_PROGRESS = 0.7F
        private val LOGGER = LogManager.getLogger()
    }
}
