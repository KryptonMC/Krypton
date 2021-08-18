package org.kryptonmc.krypton.entity.player

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.packet.`in`.play.DiggingStatus
import org.kryptonmc.krypton.packet.out.play.PacketOutAcknowledgeDigging
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockChange
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.handler
import org.spongepowered.math.vector.Vector3i

class PlayerGameHandler(val player: KryptonPlayer) {

    private val world: KryptonWorld
        get() = player.world
    private var currentTick = 0

    private var isDestroyingBlock = false
    private var startProgress = 0
    private var destroyingPosition = Vector3i.ZERO
    private var lastSentState = -1

    private var hasDelayedDestroy = false
    private var delayedDestroyPosition = Vector3i.ZERO
    private var delayedStartTick = 0

    fun tick() {
        ++currentTick
        if (hasDelayedDestroy) {
            val block = world.getBlock(destroyingPosition)
            if (block.isAir) {
                hasDelayedDestroy = false
            } else {
                val destroyProgress = incrementProgress(block, delayedDestroyPosition, delayedStartTick)
                if (destroyProgress > 1F) {
                    hasDelayedDestroy = false
                    destroyBlock(delayedDestroyPosition)
                }
            }
        } else if (isDestroyingBlock) {
            val block = world.getBlock(destroyingPosition)
            if (block.isAir) {
                world.destroyBlockProgress(destroyingPosition, player.id, -1)
                lastSentState = -1
                isDestroyingBlock = false
            } else {
                incrementProgress(block, destroyingPosition, startProgress)
            }
        }
    }

    fun handleBreak(position: Vector3i, status: DiggingStatus, direction: Direction, maxBuildHeight: Int) {
        val x = player.location.x - (position.x() + 0.5)
        val y = player.location.y - (position.y() + 0.5) + 1.5
        val z = player.location.z - (position.z() + 0.5)
        val distSq = x * x + y * y + z * z
        if (distSq > 36.0) {
            player.session.sendPacket(PacketOutAcknowledgeDigging(position, world.getBlock(position), status, false))
            return
        }
        if (position.y() >= maxBuildHeight) {
            player.session.sendPacket(PacketOutAcknowledgeDigging(position, world.getBlock(position), status, false))
            return
        }
        when (status) {
            DiggingStatus.STARTED -> {
                if (!world.canInteract(player, position)) {
                    player.session.sendPacket(PacketOutAcknowledgeDigging(position, world.getBlock(position), status, false))
                    return
                }
                if (player.gamemode == Gamemode.CREATIVE) {
                    destroy(position, status)
                    return
                }

                if (player.blockActionRestricted(position)) {
                    player.session.sendPacket(PacketOutAcknowledgeDigging(position, world.getBlock(position), status, false))
                    return
                }

                startProgress = currentTick
                var destroyProgress = 1F
                val block = world.getBlock(position)
                if (!block.isAir) {
                    block.handler.attack(player, world, block, position)
                    destroyProgress = block.handler.getDestroyProgress(player, world, block, position)
                }

                if (!block.isAir && destroyProgress >= 1F) {
                    destroy(position, status)
                    return
                }
                if (isDestroyingBlock) player.session.sendPacket(PacketOutAcknowledgeDigging(destroyingPosition, world.getBlock(destroyingPosition), DiggingStatus.STARTED, false))
                isDestroyingBlock = true
                val state = (destroyProgress * 10F).toInt()
                world.destroyBlockProgress(position, player.id, state)
                player.session.sendPacket(PacketOutAcknowledgeDigging(position, world.getBlock(position), status, true))
                lastSentState = state
            }
            DiggingStatus.FINISHED -> {
                if (position == destroyingPosition) {
                    val currentProgress = currentTick - startProgress
                    val block = world.getBlock(position)
                    if (!block.isAir) {
                        val destroyProgress = block.handler.getDestroyProgress(player, world, block, position) * (currentProgress + 1).toFloat()
                        if (destroyProgress >= 0.7F) {
                            isDestroyingBlock = false
                            world.destroyBlockProgress(position, player.id, -1)
                            destroy(position, status)
                            return
                        }

                        if (!hasDelayedDestroy) {
                            isDestroyingBlock = false
                            hasDelayedDestroy = true
                            delayedDestroyPosition = position
                            delayedStartTick = startProgress
                        }
                    }
                }
                player.session.sendPacket(PacketOutAcknowledgeDigging(position, world.getBlock(position), status, true))
            }
            DiggingStatus.CANCELLED -> {
                isDestroyingBlock = false
                if (destroyingPosition != position) {
                    LOGGER.warn("Mismatch in destroying block position for player ${player.name}! Expected: $destroyingPosition, actual: $position")
                    world.destroyBlockProgress(position, player.id, -1)
                    player.session.sendPacket(PacketOutAcknowledgeDigging(destroyingPosition, world.getBlock(destroyingPosition), status, true))
                }
                world.destroyBlockProgress(position, player.id, -1)
                player.session.sendPacket(PacketOutAcknowledgeDigging(position, world.getBlock(position), status, true))
            }
            else -> Unit
        }
    }

    private fun destroy(position: Vector3i, status: DiggingStatus) {
        player.session.sendPacket(PacketOutAcknowledgeDigging(position, world.getBlock(position), status, destroyBlock(position)))
    }

    private fun destroyBlock(position: Vector3i): Boolean {
        val block = world.getBlock(position)
        if (!player.inventory.mainHand.type.handler.canAttackBlock(player, world, block, position)) return false
        val name = block.key.value()

        // TODO: Check on update
        if ((name == "command_block" || name == "jigsaw" || name == "structure") && !player.canUseGameMasterBlocks) {
            player.session.sendPacket(PacketOutBlockChange(block, position))
            return false
        }
        if (player.blockActionRestricted(position)) return false
        block.handler.preDestroy(player, world, block, position)
        val removed = world.removeBlock(position)
        if (removed) block.handler.onDestroy(player, block, position, player.inventory.mainHand)

        if (player.gamemode == Gamemode.CREATIVE) return true
        val item = player.inventory.mainHand
        val hasCorrectTool = player.hasCorrectTool(block)
        item.type.handler.mineBlock(player, item, world, block, position)
        if (removed && hasCorrectTool) block.handler.onDestroy(player, block, position, item)
        return true
    }

    private fun incrementProgress(block: Block, position: Vector3i, oldProgress: Int): Float {
        val progress = currentTick - oldProgress
        val destroyProgress = block.handler.getDestroyProgress(player, world, block, position) * (progress + 1).toFloat()
        val state = (destroyProgress * 10F).toInt()
        if (state != lastSentState) {
            world.destroyBlockProgress(position, player.id, state)
            lastSentState = state
        }
        return destroyProgress
    }

    companion object {

        private val LOGGER = logger<PlayerGameHandler>()
    }
}
