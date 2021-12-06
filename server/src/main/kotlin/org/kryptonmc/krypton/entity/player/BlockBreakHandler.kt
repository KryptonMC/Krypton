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
package org.kryptonmc.krypton.entity.player

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.GameModes
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerDigging
import org.kryptonmc.krypton.packet.out.play.PacketOutDiggingResponse
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.handler
import org.kryptonmc.krypton.world.block.isGameMasterBlock

class BlockBreakHandler(private val player: KryptonPlayer) {

    private val world: KryptonWorld
        get() = player.world
    private val isCreative: Boolean
        get() = player.gameMode === GameModes.CREATIVE

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
            } else {
                val destroyProgress = incrementDestroyProgress(block, x, y, z, delayedDestroyTickStart)
                if (destroyProgress >= 1F) {
                    hasDelayedDestroy = false
                    destroyBlock(x, y, z)
                }
            }
        } else if (isDestroying) {
            val block = world.getBlock(destroyingX, destroyingY, destroyingZ)
            if (block.isAir) {
                world.broadcastBlockDestroyProgress(player.id, destroyingX, destroyingY, destroyingZ, -1)
                lastSentState = -1
                isDestroying = false
            } else {
                incrementDestroyProgress(block, destroyingX, destroyingY, destroyingZ, startingDestroyProgress)
            }
        }
    }

    fun handleBlockBreak(packet: PacketInPlayerDigging) {
        handleBlockBreak(packet.x, packet.y, packet.z, packet.status, world.maximumBuildHeight)
    }

    private fun handleBlockBreak(x: Int, y: Int, z: Int, status: PacketInPlayerDigging.Status, maxHeight: Int) {
        val distanceX = player.location.x() - (x.toDouble() + 0.5)
        val distanceY = player.location.y() - (y.toDouble() + 0.5) + 1.5
        val distanceZ = player.location.z() - (z.toDouble() + 0.5)
        val distanceSquared = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ
        if (distanceSquared > MAXIMUM_DISTANCE_FROM_BLOCK) {
            player.session.send(PacketOutDiggingResponse(x, y, z, world.getBlock(x, y, z), status, false))
            return
        }
        if (y >= maxHeight) {
            player.session.send(PacketOutDiggingResponse(x, y, z, world.getBlock(x, y, z), status, false))
            return
        }
        when (status) {
            PacketInPlayerDigging.Status.STARTED -> {
                if (!world.canInteract(player, x, z)) {
                    player.session.send(PacketOutDiggingResponse(x, y, z, world.getBlock(x, y, z), status, false))
                    return
                }
                if (isCreative) {
                    destroyAndAcknowledge(x, y, z, status)
                    return
                }
                if (player.isBlockActionRestricted(x, y, z)) {
                    player.session.send(PacketOutDiggingResponse(x, y, z, world.getBlock(x, y, z), status, false))
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
                    destroyAndAcknowledge(x, y, z, status)
                    return
                }
                if (isDestroying) {
                    player.session.send(PacketOutDiggingResponse(
                        destroyingX,
                        destroyingY,
                        destroyingZ,
                        world.getBlock(destroyingX, destroyingY, destroyingZ),
                        PacketInPlayerDigging.Status.STARTED,
                        false
                    ))
                }
                isDestroying = true
                destroyingX = x
                destroyingY = y
                destroyingZ = z
                val state = (destroyProgress * 10F).toInt()
                world.broadcastBlockDestroyProgress(player.id, x, y, z, state)
                player.session.send(PacketOutDiggingResponse(x, y, z, world.getBlock(x, y, z), status, true))
                lastSentState = state
            }
            PacketInPlayerDigging.Status.FINISHED -> {
                if (x == destroyingX && y == destroyingY && z == destroyingZ) {
                    val tickDifference = currentTick - startingDestroyProgress
                    val block = world.getBlock(x, y, z)
                    if (!block.isAir) {
                        val destroyProgress = block.handler().calculateDestroyProgress(player, world, block, x, y, z) * (tickDifference + 1).toFloat()
                        if (destroyProgress >= 0.7F) {
                            isDestroying = false
                            world.broadcastBlockDestroyProgress(player.id, x, y, z, -1)
                            destroyAndAcknowledge(x, y, z, status)
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
                    }
                }
                player.session.send(PacketOutDiggingResponse(x, y, z, world.getBlock(x, y, z), status, true))
            }
            PacketInPlayerDigging.Status.CANCELLED -> {
                isDestroying = false
                if (x != destroyingX || y != destroyingY || z != destroyingZ) {
                    LOGGER.warn("Mismatched destroy position! Expected $destroyingX, $destroyingY, $destroyingZ and got $x, $y, $z!")
                    world.broadcastBlockDestroyProgress(player.id, destroyingX, destroyingY, destroyingZ, -1)
                    player.session.send(PacketOutDiggingResponse(
                        destroyingX,
                        destroyingY,
                        destroyingZ,
                        world.getBlock(destroyingX, destroyingY, destroyingZ),
                        status,
                        true
                    ))
                }
                world.broadcastBlockDestroyProgress(player.id, x, y, z, -1)
                player.session.send(PacketOutDiggingResponse(x, y, z, world.getBlock(x, y, z), status, true))
            }
            else -> Unit
        }
    }

    private fun incrementDestroyProgress(block: Block, x: Int, y: Int, z: Int, startTick: Int): Float {
        val tickDifference = currentTick - startTick
        val progress = block.handler().calculateDestroyProgress(player, world, block, x, y, z) * (tickDifference + 1).toFloat()
        val state = (progress * 10F).toInt()
        if (state != lastSentState) {
            player.world.broadcastBlockDestroyProgress(player.id, x, y, z, state)
            lastSentState = state
        }
        return progress
    }

    private fun destroyAndAcknowledge(x: Int, y: Int, z: Int, status: PacketInPlayerDigging.Status) {
        if (destroyBlock(x, y, z)) {
            player.session.send(PacketOutDiggingResponse(x, y, z, world.getBlock(x, y, z), status, true))
        } else {
            player.session.send(PacketOutDiggingResponse(x, y, z, world.getBlock(x, y, z), status, false))
        }
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

    companion object {

        private const val MAXIMUM_DISTANCE_FROM_BLOCK = 36.0
        private val LOGGER = logger<BlockBreakHandler>()
    }
}
