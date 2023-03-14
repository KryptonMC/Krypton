/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.util

import com.google.common.collect.Maps
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Rotation
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.item.ItemFactory
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.util.crypto.Crypto
import org.kryptonmc.krypton.util.hit.BlockHitResult
import org.kryptonmc.krypton.util.map.IntBiMap
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import org.kryptonmc.serialization.Decoder
import org.kryptonmc.serialization.Encoder
import org.kryptonmc.serialization.nbt.NbtOps
import java.io.IOException
import java.security.PublicKey
import java.time.Instant
import java.util.Arrays
import java.util.BitSet
import java.util.EnumSet
import java.util.UUID

/*
 * The var int encoding is a variable-length encoding, which consists of a sequence of bytes, with 7 bits representing the value, and the 8th bit
 * indicating whether there is a next byte to read or not.
 *
 * For the reading, we do a simple loop up to the maximum bytes we can read in a var int/long, which is 5 and 10 respectively.
 * We then read each byte individually, shift the value in to the correct position, ignoring the marker bit (value & 0x7F gets rid of the marker
 * bit and << j * 7 shifts left by 7 bits * the byte index for each byte we read).
 * Then, we check if the marker bit is set, and if it isn't, we are done reading, otherwise, we continue reading.
 *
 * For the writing, instead of implementing it using a looping mechanism, we check each value individually for better performance.
 * We check the value against each individual case, by taking the unsigned maximum integer value (all 32 bits set to 1) and shifting it left by
 * the amount of bits we want to check, which leaves us with a value that has only the number of bits we want to check set to 0. By doing an AND
 * against this value and the provided value, we can check if the value has no bits above that number set to 1, as any number & 1 = any.
 *
 * With var longs, these methods are simply extended to 10 bytes.
 */

// https://github.com/jvm-profiling-tools/async-profiler/blob/a38a375dc62b31a8109f3af97366a307abb0fe6f/src/converter/one/jfr/JfrReader.java#L393
@Suppress("MagicNumber")
fun ByteBuf.readVarInt(): Int {
    var result = 0
    var shift = 0
    while (true) {
        val next = readByte()
        result = result or ((next.toInt() and 0x7F) shl shift)
        if (next >= 0) return result
        shift += 7
    }
}

// This way of writing var ints came from an article by Andrew Steinborn of Velocity.
// The article: https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
@Suppress("MagicNumber") // Explained in a comment on readVarInt
fun ByteBuf.writeVarInt(value: Int) {
    when {
        value.toLong() and (0xFFFFFFFF shl 7) == 0L -> writeByte(value)
        value.toLong() and (0xFFFFFFFF shl 14) == 0L -> writeShort(value and 0x7F or 0x80 shl 8 or (value ushr 7))
        value.toLong() and (0xFFFFFFFF shl 21) == 0L -> writeMedium(value and 0x7F or 0x80 shl 16 or
                (value ushr 7 and 0x7F or 0x80 shl 8) or (value ushr 14))
        value.toLong() and (0xFFFFFFFF shl 28) == 0L -> writeInt(value and 0x7F or 0x80 shl 24 or
                (value ushr 7 and 0x7F or 0x80 shl 16) or (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21))
        else -> {
            writeInt(value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or
                    (value shr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80))
            writeByte(value ushr 28)
        }
    }
}

// https://github.com/async-profiler/async-profiler/blob/a38a375dc62b31a8109f3af97366a307abb0fe6f/src/converter/one/jfr/JfrReader.java#L404
@Suppress("MagicNumber")
fun ByteBuf.readVarLong(): Long {
    var result = 0L
    var shift = 0
    while (shift < 56) {
        val next = readByte()
        result = result or ((next.toInt() and 0x7F) shl shift).toLong()
        if (next >= 0) return result
        shift += 7
    }
    return result or ((readByte().toInt() and 0xFF) shl 56).toLong()
}

// This is my own completely crazy extension of the above `writeVarInt` function, created by Andrew Steinborn of Velocity,
// to the 10-bit var long. This is pretty much completely unnecessary, but it functions as expected, and was a challenge in
// the article cited on the above method, so I thought it was worth putting it in here.
@Suppress("MagicNumber")
fun ByteBuf.writeVarLong(value: Long) {
    when {
        value and (0xFFFFFFFF shl 7) == 0L -> writeByte(value.toInt())
        value and (0xFFFFFFFF shl 14) == 0L -> writeShort((value and 0x7F or 0x80 shl 8 or (value ushr 7)).toInt())
        value and (0xFFFFFFFF shl 21) == 0L -> writeMedium((value and 0x7F or 0x80 shl 16 or
                (value ushr 7 and 0x7F or 0x80 shl 8) or (value ushr 14)).toInt())
        value and (0xFFFFFFFF shl 28) == 0L -> writeInt((value and 0x7F or 0x80 shl 24 or
                (value ushr 7 and 0x7F or 0x80 shl 16) or (value ushr 14 and 0x7F or 0x80 shl 8) or
                (value ushr 21)).toInt())
        value and (0xFFFFFFFF shl 35) == 0L -> {
            writeInt((value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or
                    (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80)).toInt())
            writeByte((value ushr 28).toInt())
        }
        value and (0xFFFFFFFF shl 42) == 0L -> {
            writeInt((value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or
                    (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80)).toInt())
            writeShort((value ushr 28 and 0x7F or 0x80 shl 8 or (value ushr 35)).toInt())
        }
        value and (0xFFFFFFFF shl 49) == 0L -> {
            writeInt((value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or
                    (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80)).toInt())
            writeMedium((value ushr 28 and 0x7F or 0x80 shl 16 or (value ushr 35 and 0x7F or 0x80 shl 8) or
                    (value ushr 42)).toInt())
        }
        value and (0xFFFFFFFF shl 56) == 0L -> writeLong(value and 0x7F or 0x80 shl 56 or
                (value ushr 7 and 0x7F or 0x80 shl 48) or (value ushr 14 and 0x7F or 0x80 shl 40) or
                (value ushr 21 and 0x7F or 0x80 shl 32) or (value ushr 28 and 0x7F or 0x80 shl 24) or
                (value ushr 35 and 0x7F or 0x80 shl 16) or (value ushr 42 and 0x7F or 0x80 shl 8) or
                (value ushr 49))
        value and (0xFFFFFFFF shl 63) == 0L -> {
            writeLong(value and 0x7F or 0x80 shl 56 or (value ushr 7 and 0x7F or 0x80 shl 48) or
                    (value ushr 14 and 0x7F or 0x80 shl 40) or (value ushr 21 and 0x7F or 0x80 shl 32) or
                    (value ushr 28 and 0x7F or 0x80 shl 24) or (value ushr 35 and 0x7F or 0x80 shl 16) or
                    (value ushr 42 and 0x7F or 0x80 shl 8) or (value ushr 49 and 0x7F or 0x80))
            writeByte((value ushr 56).toInt())
        }
        value and (0xFFFFFFFF shl 70) == 0L -> {
            writeLong(value and 0x7F or 0x80 shl 56 or (value ushr 7 and 0x7F or 0x80 shl 48) or
                    (value ushr 14 and 0x7F or 0x80 shl 40) or (value ushr 21 and 0x7F or 0x80 shl 32) or
                    (value ushr 28 and 0x7F or 0x80 shl 24) or (value ushr 35 and 0x7F or 0x80 shl 16) or
                    (value ushr 42 and 0x7F or 0x80 shl 8) or (value ushr 49 and 0x7F or 0x80))
            writeShort((value ushr 56 and 0x7F or 0x80 shl 8 or (value ushr 63)).toInt())
        }
    }
}

fun ByteBuf.readString(max: Int): String {
    val length = readVarInt()
    when {
        length < 0 -> throw DecoderException("String cannot be less than 0 in length!")
        length > max * 4 -> throw DecoderException("String too long! Expected maximum length of $max, got length of $length!")
        else -> {
            val string = String(readAvailableBytes(length))
            if (string.length > max) throw DecoderException("String too long! Expected maximum length of $max, got length of ${string.length}")
            return string
        }
    }
}

fun ByteBuf.readString(): String = readString(Short.MAX_VALUE.toInt())

fun ByteBuf.writeString(value: String, max: Int) {
    val bytes = value.encodeToByteArray()
    if (bytes.size > max) throw EncoderException("String too long! Expected maximum size of $max, got length ${value.length}!")
    writeVarInt(bytes.size)
    writeBytes(bytes)
}

fun ByteBuf.writeString(value: String) {
    writeString(value, Short.MAX_VALUE.toInt())
}

fun ByteBuf.readVarIntByteArray(): ByteArray = readAvailableBytes(readVarInt())

fun ByteBuf.readVarIntByteArray(max: Int): ByteArray {
    val length = readVarInt()
    if (length > max) throw DecoderException("Byte array too long! Expected maximum length of $max, got length of $length!")
    return readAvailableBytes(length)
}

fun ByteBuf.readAvailableBytes(length: Int): ByteArray {
    val bytes = ByteArray(length)
    readBytes(bytes)
    return bytes
}

fun ByteBuf.readAllAvailableBytes(): ByteArray = readAvailableBytes(readableBytes())

fun ByteBuf.writeVarIntByteArray(bytes: ByteArray) {
    writeVarInt(bytes.size)
    writeBytes(bytes)
}

fun ByteBuf.writeLongArray(array: LongArray) {
    writeVarInt(array.size)
    for (i in array.indices) {
        writeLong(array[i])
    }
}

fun ByteBuf.readLongArray(): LongArray = LongArray(readVarInt()) { readLong() }

fun ByteBuf.writeUUID(uuid: UUID) {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
}

fun ByteBuf.readUUID(): UUID = UUID(readLong(), readLong())

fun ByteBuf.writeNBT(tag: CompoundTag?) {
    if (tag == null) {
        writeByte(0)
        return
    }
    try {
        TagIO.write(ByteBufOutputStream(this), tag, TagCompression.NONE)
    } catch (exception: IOException) {
        throw EncoderException(exception)
    }
}

fun ByteBuf.readNBT(): CompoundTag {
    val index = readerIndex()
    val type = readByte()
    if (type == EndTag.ID.toByte()) return CompoundTag.EMPTY
    readerIndex(index) // reset the head if it's not an end tag

    try {
        return TagIO.read(ByteBufInputStream(this), TagCompression.NONE)
    } catch (exception: IOException) {
        throw DecoderException(exception)
    }
}

private const val MAXIMUM_COMPONENT_STRING_LENGTH = 262144

fun ByteBuf.writeComponent(component: Component) {
    writeString(GsonComponentSerializer.gson().serialize(component), MAXIMUM_COMPONENT_STRING_LENGTH)
}

fun ByteBuf.readComponent(): Component {
    try {
        return GsonComponentSerializer.gson().deserialize(readString(MAXIMUM_COMPONENT_STRING_LENGTH))
    } catch (exception: Exception) {
        throw DecoderException("Could not decode component!", exception)
    }
}

fun ByteBuf.readItem(): KryptonItemStack {
    if (!readBoolean()) return KryptonItemStack.EMPTY
    val type = readById(KryptonRegistries.ITEM)!!
    val count = readByte()
    val nbt = readNBT()
    return KryptonItemStack(type, count.toInt(), ItemFactory.create(type, nbt))
}

fun ByteBuf.writeItem(item: KryptonItemStack) {
    if (item === KryptonItemStack.EMPTY) {
        writeBoolean(false)
        return
    }
    writeBoolean(true)
    writeId(KryptonRegistries.ITEM, item.type)
    writeByte(item.amount)
    writeNBT(item.meta.data)
}

fun ByteBuf.writeBlockPos(pos: Vec3i) {
    writeLong(BlockPos.pack(pos))
}

fun ByteBuf.readBlockPos(): Vec3i = BlockPos.unpack(readLong())

fun ByteBuf.writeRotation(rotation: Rotation) {
    writeFloat(rotation.x)
    writeFloat(rotation.y)
    writeFloat(rotation.z)
}

fun ByteBuf.readRotation(): Rotation = Rotation(readFloat(), readFloat(), readFloat())

fun ByteBuf.writeVec3d(vector: Vec3d) {
    writeDouble(vector.x)
    writeDouble(vector.y)
    writeDouble(vector.z)
}

fun ByteBuf.readVec3d(): Vec3d = Vec3d(readDouble(), readDouble(), readDouble())

fun ByteBuf.writeKey(key: Key) {
    writeString(key.asString())
}

fun ByteBuf.writeResourceKey(key: ResourceKey<*>) {
    writeKey(key.location)
}

fun ByteBuf.readKey(): Key = Key.key(readString())

inline fun <T> ByteBuf.writeNullable(value: T?, writer: (ByteBuf, T) -> Unit) {
    writeBoolean(value != null)
    if (value != null) writer(this, value)
}

inline fun <T> ByteBuf.readNullable(reader: (ByteBuf) -> T): T? = if (!readBoolean()) null else reader(this)

inline fun <reified T : Enum<T>> ByteBuf.readEnum(): T = T::class.java.enumConstants[readVarInt()]

fun ByteBuf.writeEnum(enum: Enum<*>) {
    writeVarInt(enum.ordinal)
}

fun <E : Enum<E>> ByteBuf.readEnumSet(type: Class<E>): EnumSet<E> {
    val constants = type.enumConstants
    val bits = readFixedBitSet(constants.size)

    val result = EnumSet.noneOf(type)
    for (i in constants.indices) {
        if (bits.get(i)) result.add(constants[i])
    }
    return result
}

fun <E : Enum<E>> ByteBuf.writeEnumSet(set: EnumSet<E>, type: Class<E>) {
    val constants = type.enumConstants
    val bits = BitSet(constants.size)
    for (i in constants.indices) {
        bits.set(i, set.contains(constants[i]))
    }
    writeFixedBitSet(bits, constants.size)
}

inline fun <reified E : Enum<E>> ByteBuf.readEnumSet(): EnumSet<E> = readEnumSet(E::class.java)

inline fun <reified E : Enum<E>> ByteBuf.writeEnumSet(set: EnumSet<E>) {
    writeEnumSet(set, E::class.java)
}

fun ByteBuf.readBitSet(): BitSet = BitSet.valueOf(readLongArray())

fun ByteBuf.readFixedBitSet(fixedBits: Int): BitSet {
    val bytes = ByteArray(Maths.positiveCeilDivide(fixedBits, 8))
    readBytes(bytes)
    return BitSet.valueOf(bytes)
}

fun ByteBuf.writeBitSet(set: BitSet) {
    writeLongArray(set.toLongArray())
}

fun ByteBuf.writeFixedBitSet(set: BitSet, fixedBits: Int) {
    if (set.length() > fixedBits) throw EncoderException("BitSet $set is larger than expected fixed bit size $fixedBits!")
    writeBytes(Arrays.copyOf(set.toByteArray(), Maths.positiveCeilDivide(fixedBits, 8)))
}

inline fun <E> ByteBuf.writeCollection(collection: Collection<E>, action: (E) -> Unit) {
    writeVarInt(collection.size)
    collection.forEach(action)
}

inline fun <C : MutableCollection<E>, E> ByteBuf.readCollection(collectionCreator: (Int) -> C, reader: (ByteBuf) -> E): C {
    val size = readVarInt()
    val collection = collectionCreator(size)
    for (i in 0 until size) {
        collection.add(reader(this))
    }
    return collection
}

inline fun <E> ByteBuf.readList(reader: (ByteBuf) -> E): List<E> = readCollection(::ArrayList, reader)

inline fun <E> ByteBuf.readPersistentList(reader: (ByteBuf) -> E): PersistentList<E> {
    val builder = persistentListOf<E>().builder()
    val size = readVarInt()
    for (i in 0 until size) {
        builder.add(reader(this))
    }
    return builder.build()
}

inline fun <K, V> ByteBuf.writeMap(map: Map<K, V>, keyWriter: (ByteBuf, K) -> Unit, valueWriter: (ByteBuf, V) -> Unit) {
    writeVarInt(map.size)
    map.forEach { (key, value) ->
        keyWriter(this, key)
        valueWriter(this, value)
    }
}

inline fun <K, V> ByteBuf.readMap(keyReader: (ByteBuf) -> K, valueReader: (ByteBuf) -> V): Map<K, V> =
    readMap(Maps::newHashMapWithExpectedSize, keyReader, valueReader)

inline fun <M : MutableMap<K, V>, K, V> ByteBuf.readMap(mapCreator: (Int) -> M, keyReader: (ByteBuf) -> K, valueReader: (ByteBuf) -> V): M {
    val size = readVarInt()
    val map = mapCreator(size)
    for (i in 0 until size) {
        map.put(keyReader(this), valueReader(this))
    }
    return map
}

fun ByteBuf.writeGameProfile(profile: GameProfile) {
    writeUUID(profile.uuid)
    writeString(profile.name)
    writeCollection(profile.properties, ::writeProfileProperty)
}

fun ByteBuf.readGameProfile(): GameProfile = KryptonGameProfile.full(readUUID(), readString(), readPersistentList(ByteBuf::readProfileProperty))

fun ByteBuf.writeProfileProperties(properties: Collection<ProfileProperty>) {
    writeCollection(properties, ::writeProfileProperty)
}

fun ByteBuf.readProfileProperties(): List<ProfileProperty> = readList(ByteBuf::readProfileProperty)

fun ByteBuf.writeProfileProperty(property: ProfileProperty) {
    writeString(property.name)
    writeString(property.value)
    writeNullable(property.signature, ByteBuf::writeString)
}

fun ByteBuf.readProfileProperty(): ProfileProperty = KryptonProfileProperty(readString(), readString(), readNullable(ByteBuf::readString))

fun <T> ByteBuf.writeId(registry: IntBiMap<T>, value: T) {
    val id = registry.getId(value)
    require(id != -1) { "Cannot find ID for value $value in registry $registry!" }
    writeVarInt(id)
}

fun <T> ByteBuf.readById(registry: IntBiMap<T>): T? = registry.get(readVarInt())

inline fun <T> ByteBuf.writeId(registry: IntBiMap<Holder<T>>, entry: Holder<T>, writer: (ByteBuf, T) -> Unit) {
    when (entry.kind()) {
        Holder.Kind.REFERENCE -> {
            val id = registry.getId(entry)
            require(id != -1) { "Cannot find ID for value ${entry.value()} in registry $registry!" }
            writeVarInt(id + 1)
        }
        Holder.Kind.DIRECT -> {
            writeVarInt(0)
            writer(this, entry.value())
        }
    }
}

inline fun <T : Any> ByteBuf.readById(registry: IntBiMap<Holder<T>>, reader: (ByteBuf) -> T): Holder<T> {
    val id = readVarInt()
    if (id == 0) return Holder.Direct(reader(this))
    return requireNotNull(registry.get(id - 1)) { "Cannot find registry element with id $id!" }
}

fun ByteBuf.writeVarIntArray(array: IntArray) {
    writeVarInt(array.size)
    for (i in array.indices) {
        writeVarInt(array[i])
    }
}

fun ByteBuf.readVarIntArray(): IntArray = IntArray(readVarInt()) { readVarInt() }

fun ByteBuf.writeIntIdList(ids: IntList) {
    writeVarInt(ids.size)
    ids.forEach(::writeVarInt)
}

fun ByteBuf.readIntIdList(): IntList {
    val size = readVarInt()
    val result = IntArrayList()
    for (i in 0 until size) {
        result.add(readVarInt())
    }
    return result
}

fun <T> ByteBuf.encode(encoder: Encoder<T>, value: T) {
    val result = encoder.encodeStart(value, NbtOps.INSTANCE)
    result.error().ifPresent { throw EncoderException("Failed to encode: ${it.message} $value") }
    writeNBT(result.result().get() as? CompoundTag)
}

fun <T> ByteBuf.decode(decoder: Decoder<T>): T {
    val nbt = readNBT()
    val result = decoder.read(nbt, NbtOps.INSTANCE)
    result.error().ifPresent { throw DecoderException("Failed to decode: ${it.message} $nbt") }
    return result.result().get()
}

fun ByteBuf.readInstant(): Instant = Instant.ofEpochMilli(readLong())

fun ByteBuf.writeInstant(instant: Instant) {
    writeLong(instant.toEpochMilli())
}

fun ByteBuf.readPublicKey(): PublicKey {
    try {
        return Crypto.bytesToRsaPublicKey(readVarIntByteArray())
    } catch (exception: Exception) {
        throw DecoderException("Failed to decode public key!", exception)
    }
}

fun ByteBuf.writePublicKey(key: PublicKey) {
    writeVarIntByteArray(key.encoded)
}

fun ByteBuf.readBlockHitResult(): BlockHitResult {
    val position = readBlockPos()
    val direction = readEnum<Direction>()
    val cursorX = readFloat().toDouble()
    val cursorY = readFloat().toDouble()
    val cursorZ = readFloat().toDouble()
    val isInside = readBoolean()
    return BlockHitResult(Vec3d(position.x + cursorX, position.y + cursorY, position.z + cursorZ), direction, position, isInside)
}

fun ByteBuf.writeBlockHitResult(hitResult: BlockHitResult) {
    val position = hitResult.position
    writeBlockPos(position)
    writeEnum(hitResult.direction)
    val location = hitResult.location
    writeFloat((location.x - position.x).toFloat())
    writeFloat((location.y - position.y).toFloat())
    writeFloat((location.z - position.z).toFloat())
    writeBoolean(hitResult.isInside)
}

fun ByteBuf.write3EmptyBytes(): Int {
    val index = writerIndex()
    writeMedium(0)
    return index
}

fun ByteBuf.write3ByteVarInt(startIndex: Int, value: Int) {
    val originalIndex = writerIndex()
    writerIndex(startIndex)
    val encoded = value and 0x7F or 0x80 shl 16 or (value ushr 7 and 0x7F or 0x80 shl 8) or (value ushr 14)
    writeMedium(encoded)
    writerIndex(originalIndex)
}
