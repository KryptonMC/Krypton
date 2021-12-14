package org.kryptonmc.krypton.commands.krypton

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.kryptonmc.api.command.Sender

fun interface KryptonSubCommand {

    val aliases: Sequence<String>
        get() = emptySequence()

    fun register(): LiteralArgumentBuilder<Sender>
}
