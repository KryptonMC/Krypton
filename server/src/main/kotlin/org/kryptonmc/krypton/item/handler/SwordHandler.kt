package org.kryptonmc.krypton.item.handler

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.world.block.isLeaves
import org.kryptonmc.krypton.world.block.isPlant
import org.kryptonmc.krypton.world.block.isReplaceablePlant
import org.kryptonmc.krypton.world.block.isVegetable
import org.spongepowered.math.vector.Vector3i

sealed class SwordHandler(type: ItemType) : KryptonItemHandler(type) {

    override fun canAttackBlock(player: Player, world: World, block: Block, position: Vector3i) = player.gamemode != Gamemode.CREATIVE

    override fun getDestroySpeed(item: ItemStack, block: Block): Float {
        if (block.id == Blocks.COBWEB.id) return 15F
        if (block.isPlant || block.isReplaceablePlant || block.isLeaves || block.isVegetable) return 1.5F
        return 1F
    }

    override fun isCorrectTool(block: Block) = block.id == Blocks.COBWEB.id

    override fun mineBlock(item: ItemStack, player: Player, world: World, block: Block, position: Vector3i) = true
}

object WoodenSwordHandler : SwordHandler(ItemTypes.WOODEN_SWORD)

object StoneSwordHandler : SwordHandler(ItemTypes.STONE_SWORD)

object GoldenSwordHandler : SwordHandler(ItemTypes.GOLDEN_SWORD)

object IronSwordHandler : SwordHandler(ItemTypes.IRON_SWORD)

object DiamondSwordHandler : SwordHandler(ItemTypes.DIAMOND_SWORD)

object NetheriteSwordHandler : SwordHandler(ItemTypes.NETHERITE_SWORD)
