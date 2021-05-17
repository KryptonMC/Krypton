package org.kryptonmc.krypton.config.serializer

import org.kryptonmc.api.world.Gamemode
import org.spongepowered.configurate.serialize.ScalarSerializer
import org.spongepowered.configurate.serialize.SerializationException
import java.lang.reflect.Type
import java.util.function.Predicate

object GamemodeTypeSerializer : ScalarSerializer<Gamemode>(Gamemode::class.java) {

    override fun serialize(item: Gamemode, typeSupported: Predicate<Class<*>>) = item.name.lowercase()

    override fun deserialize(type: Type, source: Any): Gamemode = try {
        when (source) {
            is Int -> Gamemode.fromId(source) ?: throw SerializationException("$source is not a valid gamemode ID!")
            is String -> Gamemode.valueOf(source.uppercase())
            else -> throw SerializationException("Expected either an integer or a string for this gamemode, got ${source::class.simpleName}")
        }
    } catch (exception: Exception) {
        when (exception) {
            is IllegalArgumentException, is ArrayIndexOutOfBoundsException -> throw SerializationException("Unknown gamemode $source")
            else -> throw exception
        }
    }
}
