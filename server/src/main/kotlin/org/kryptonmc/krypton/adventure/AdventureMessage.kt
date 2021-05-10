package org.kryptonmc.krypton.adventure

import com.mojang.brigadier.Message
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer

class AdventureMessage(private val wrapped: Component) : Message {

    override fun getString() = PlainComponentSerializer.plain().serialize(wrapped)
}

fun Component.toMessage() = AdventureMessage(this)
