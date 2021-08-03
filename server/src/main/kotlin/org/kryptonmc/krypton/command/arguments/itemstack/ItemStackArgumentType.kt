package org.kryptonmc.krypton.command.arguments.itemstack

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.util.argument

class ItemStackArgumentType : ArgumentType<ItemStackArgument> {

    override fun parse(reader: StringReader) = ItemStackParser(reader, false).parse()

}

fun CommandContext<Sender>.itemStackArgument(name: String) = argument<ItemStackArgument>(name)
