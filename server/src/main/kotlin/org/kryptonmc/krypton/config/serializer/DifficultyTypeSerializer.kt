package org.kryptonmc.krypton.config.serializer

import org.kryptonmc.api.world.Difficulty
import org.spongepowered.configurate.serialize.ScalarSerializer
import org.spongepowered.configurate.serialize.SerializationException
import java.lang.reflect.Type
import java.util.function.Predicate

object DifficultyTypeSerializer : ScalarSerializer<Difficulty>(Difficulty::class.java) {

    override fun serialize(item: Difficulty, typeSupported: Predicate<Class<*>>) = item.name.lowercase()

    override fun deserialize(type: Type, source: Any): Difficulty = try {
        when (source) {
            is Int -> Difficulty.fromId(source)
            is String -> Difficulty.valueOf(source.uppercase())
            else -> throw SerializationException("Expected either an integer or a string for this gamemode, got ${source::class.simpleName}")
        }
    } catch (exception: Exception) {
        when (exception) {
            is IllegalArgumentException, is ArrayIndexOutOfBoundsException -> throw SerializationException("Unknown difficulty $source")
            else -> throw exception
        }
    }
}
