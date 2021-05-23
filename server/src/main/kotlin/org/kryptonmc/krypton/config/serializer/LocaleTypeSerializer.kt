package org.kryptonmc.krypton.config.serializer

import org.spongepowered.configurate.serialize.ScalarSerializer
import org.spongepowered.configurate.serialize.SerializationException
import java.lang.reflect.Type
import java.util.Locale
import java.util.function.Predicate

object LocaleTypeSerializer : ScalarSerializer<Locale>(Locale::class.java) {

    override fun serialize(item: Locale, typeSupported: Predicate<Class<*>>) = item.toString()

    override fun deserialize(type: Type, obj: Any): Locale {
        if (obj !is String) throw SerializationException("Locale must be a string!")
        val split = obj.split("[_-]".toRegex())
        if (split.isEmpty()) throw SerializationException("Locale cannot be empty!")
        if (split.size == 1) return Locale(split[0])
        if (split.size == 2) return Locale(split[0], split[1])
        throw SerializationException("Cannot deserialize $obj into a valid locale!")
    }
}
