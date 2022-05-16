package org.kryptonmc.krypton.command

import com.mojang.brigadier.tree.LiteralCommandNode
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.Sender

class KryptonBrigadierCommand(override val node: LiteralCommandNode<Sender>) : BrigadierCommand {

    object Factory : BrigadierCommand.Factory {

        override fun of(node: LiteralCommandNode<Sender>): KryptonBrigadierCommand = KryptonBrigadierCommand(node)
    }
}
