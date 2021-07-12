package org.kryptonmc.api.item

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockHitResult
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

interface ItemHandler {

    val type: ItemType

    fun getDestroySpeed(item: ItemStack, block: Block): Float

    fun isCorrectTool(block: Block): Boolean

    fun canAttackBlock(player: Player, world: World, block: Block, position: Vector3i): Boolean

    fun interactWith(context: InteractionContext): InteractionResult

    fun interact(player: Player, world: World, hand: Hand): Pair<InteractionResult, ItemStack>

    fun mineBlock(item: ItemStack, player: Player, world: World, block: Block, position: Vector3i): Boolean
}

class InteractionContext(
    val player: Player,
    val world: World,
    val heldItem: ItemStack,
    val hand: Hand,
    val hitResult: BlockHitResult
) {

    val position = hitResult.position
    val clickedFace = hitResult.direction
    val clickLocation = hitResult.clickLocation
    val isInside = hitResult.isInside

    val horizontalDirection = player.direction
    val pitch = player.location.pitch
}
