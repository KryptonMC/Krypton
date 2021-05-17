package org.kryptonmc.krypton.command.arguments.coordinates

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.adventure.toMessage
import org.kryptonmc.api.entity.entities.Player
import org.kryptonmc.api.space.Vector
import org.spongepowered.math.vector.Vector2d

sealed interface Coordinates {

    val relativeX: Boolean get() = true

    val relativeY: Boolean get() = true

    val relativeZ: Boolean get() = true

    fun position(player: Player): Vector

    fun rotation(player: Player): Vector2d
}

val ERROR_EXPECTED_DOUBLE = SimpleCommandExceptionType(Component.translatable("argument.pos.missing.double").toMessage())
val ERROR_EXPECTED_INTEGER = SimpleCommandExceptionType(Component.translatable("argument.pos.missing.int").toMessage())
