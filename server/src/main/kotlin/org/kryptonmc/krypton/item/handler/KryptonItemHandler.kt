package org.kryptonmc.krypton.item.handler

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.InteractionContext
import org.kryptonmc.api.item.ItemHandler
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

open class KryptonItemHandler(override val type: ItemType) : ItemHandler {

    override fun canAttackBlock(player: Player, world: World, block: Block, position: Vector3i) = true

    override fun getDestroySpeed(item: ItemStack, block: Block) = 1F

    override fun interact(player: Player, world: World, hand: Hand) = InteractionResult.PASS to player.inventory.heldItem(hand)

    override fun interactWith(context: InteractionContext) = InteractionResult.PASS

    override fun isCorrectTool(block: Block) = false

    override fun mineBlock(item: ItemStack, player: Player, world: World, block: Block, position: Vector3i) = false
}
