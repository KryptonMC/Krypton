package org.kryptonmc.krypton.util

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import org.kryptonmc.api.util.StringSerializable
import org.kryptonmc.api.util.getIfPresent
import java.awt.Color
import java.util.Optional
import java.util.function.IntFunction
import java.util.function.ToIntFunction

val COLOR_CODEC: Codec<Color> = Codec.INT.xmap(::Color, Color::getRGB)

fun <A> MapCodec<Optional<A>>.xmapOptional(): MapCodec<A?> = xmap({ it.getIfPresent() }, { Optional.ofNullable(it) })

fun <A> Codec<A>.nullableFieldOf(name: String) = optionalFieldOf(name).xmapOptional()

fun <E> Array<E>.codec(nameToValue: (String) -> E?): Codec<E> where E : Enum<E>, E : StringSerializable = object : Codec<E> {

    override fun <T> encode(input: E, ops: DynamicOps<T>, prefix: T) =
        ops.mergeToPrimitive(prefix, if (ops.compressMaps()) ops.createInt(input.ordinal) else ops.createString(input.serialized))

    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<E, T>> = (if (ops.compressMaps()) ops.getNumberValue(input).flatMap { id ->
        this@codec.getOrNull(id.toInt())?.let { DataResult.success(it) } ?: DataResult.error("Unknown element with ID $id!")
    } else ops.getStringValue(input).flatMap { name ->
        nameToValue(name)?.let { DataResult.success(it) } ?: DataResult.error("Unknown element with name $name!")
    }).map { Pair.of(it, ops.empty()) }

    override fun toString() = "Enum & StringSerializable"
}
