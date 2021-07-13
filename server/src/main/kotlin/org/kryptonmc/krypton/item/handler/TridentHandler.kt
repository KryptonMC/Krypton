package org.kryptonmc.krypton.item.handler

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

object TridentHandler : KryptonItemHandler(ItemTypes.TRIDENT) {

    override fun canAttackBlock(player: Player, world: World, block: Block, position: Vector3i) = player.gamemode != Gamemode.CREATIVE
}
