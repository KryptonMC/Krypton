package org.kryptonmc.krypton.command.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.PlayerAction.UPDATE_GAMEMODE
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.PlayerInfo
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.util.argument

class GamemodeCommand(private val server: KryptonServer) : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = literal<Sender>("gamemode")
        Gamemode.values().forEach { mode ->
            node.then(argument<Sender, String>(mode.name.lowercase(), StringArgumentType.string())
                .executes {
                    updateGameMode(it.source as? KryptonPlayer ?: return@executes 1, Gamemode.valueOf(it.argument<String>(mode.name.lowercase()).uppercase()))
                    1
                })
        }
        dispatcher.register(node)
    }

    private fun updateGameMode(player: KryptonPlayer, mode: Gamemode) {
        player.gamemode = mode
        server.sessionManager.sendPackets(PacketOutPlayerInfo(UPDATE_GAMEMODE, listOf(PlayerInfo(gamemode = mode, profile = player.profile)))) {
            it.currentState == PacketState.PLAY
        }
        player.sendMessage(translatable("commands.gamemode.success.self", listOf(translatable("gameMode.${mode.name.lowercase()}"))))
    }
}
