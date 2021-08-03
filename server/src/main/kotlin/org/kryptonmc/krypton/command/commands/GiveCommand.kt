package org.kryptonmc.krypton.command.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.arguments.itemstack.ItemStackArgument
import org.kryptonmc.krypton.command.arguments.itemstack.ItemStackArgumentType
import org.kryptonmc.krypton.command.arguments.itemstack.itemStackArgument
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutWindowItems
import org.kryptonmc.krypton.util.argument

object GiveCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("give")
            .then(argument<Sender, EntityQuery>("targets", EntityArgument.players())
                .then(argument<Sender, ItemStackArgument>("item", ItemStackArgumentType())
                    .executes {
                        val targets = it.entityArgument("targets").getPlayers(it.source)
                        val item = it.itemStackArgument("item")
                        give(targets, item.item)
                        1
                    }
                    .then(argument<Sender, Int>("count", IntegerArgumentType.integer(1))
                        .executes {
                            val targets = it.entityArgument("targets").getPlayers(it.source)
                            val item = it.itemStackArgument("item")
                            val count = it.argument<Int>("count")
                            give(targets, item.item, count)
                            1
                        }))))
    }

    fun give(targets: List<KryptonPlayer>, item: ItemType, count: Int = 1) {
        for (target in targets) {
            val itemStack = ItemStack.of(item, count)
            target.inventory += itemStack
            target.session.sendPacket(PacketOutWindowItems(target.inventory.id, target.inventory.stateId, target.inventory.networkItems, target.inventory.mainHand))
        }
    }
}
