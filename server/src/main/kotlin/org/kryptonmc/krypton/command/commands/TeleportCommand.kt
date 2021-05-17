package org.kryptonmc.krypton.command.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.entities.Player
import org.kryptonmc.krypton.command.BrigadierCommand
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates

object TeleportCommand : BrigadierCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = dispatcher.register(literal<Sender>("teleport")
            .then(argument<Sender, Coordinates>("location", VectorArgument(true))
                .executes { teleport(it.source, it.getArgument("location", Coordinates::class.java)); 1 }))
        dispatcher.register(literal<Sender>("tp").redirect(node))
    }

    private fun teleport(sender: Sender, location: Coordinates) {
        if (sender !is Player) return
        sender.teleport(location.position(sender))
    }
}
