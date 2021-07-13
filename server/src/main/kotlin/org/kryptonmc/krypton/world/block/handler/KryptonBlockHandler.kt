package org.kryptonmc.krypton.world.block.handler

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockFace
import org.kryptonmc.api.block.BlockHandler
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

open class KryptonBlockHandler : BlockHandler {

    override fun getDestroyProgress(player: Player, world: World, block: Block, position: Vector3i): Float {
        val hardness = block.hardness
        if (hardness == -1.0) return 0F
        val factor = if (player.hasCorrectTool(block)) 30 else 100
        return player.getDestroySpeed(block) / hardness.toFloat() / factor.toFloat()
    }

    override fun attack(player: Player, world: World, block: Block, position: Vector3i) = Unit

    override fun interact(player: Player, world: World, block: Block, position: Vector3i, hand: Hand) = InteractionResult.PASS

    override fun onPlace(player: Player, block: Block, position: Vector3i, face: BlockFace) = Unit

    override fun onDestroy(player: Player, block: Block, position: Vector3i, item: ItemStack) = Unit
}
