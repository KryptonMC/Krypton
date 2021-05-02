package org.kryptonmc.krypton.command

import com.mojang.brigadier.tree.LiteralCommandNode
import org.kryptonmc.krypton.api.command.Sender

interface BrigadierCommand {

    fun command(): LiteralCommandNode<Sender>
}
