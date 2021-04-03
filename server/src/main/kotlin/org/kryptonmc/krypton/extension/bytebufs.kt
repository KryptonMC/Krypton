package org.kryptonmc.krypton.extension

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.api.effect.particle.*
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.entity.Slot
import org.kryptonmc.krypton.entity.entities.data.VillagerData
import org.kryptonmc.krypton.entity.metadata.Optional
import org.kryptonmc.krypton.space.Angle
import org.kryptonmc.krypton.space.Rotation
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8
import java.time.Duration
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom
import kotlin.experimental.and
import kotlin.math.max
import kotlin.math.min

// Allows us to write a byte without having to convert it to an integer every time
fun ByteBuf.writeByte(byte: Byte) {
    writeByte(byte.toInt())
}

fun ByteBuf.writeUByte(byte: UByte) {
    writeByte(byte.toInt())
}

fun ByteBuf.writeShort(short: Short) {
    writeShort(short.toInt())
}

fun ByteBuf.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Byte
    do {
        read = readByte()
        val value = (read and 127).toInt()
        result = result or (value shl 7 * numRead)
        numRead++
        if (numRead > 5) throw RuntimeException("VarInt is too big")
    } while (read and 128.toByte() != 0.toByte())

    return result
}

fun ByteBuf.writeVarInt(varInt: Int) {
    var i = varInt
    while (i and -128 != 0) {
        writeByte(i and 127 or 128)
        i = i ushr 7
    }

    writeByte(i)
}

fun ByteBuf.writeVarLong(varLong: Long) {
    var i = varLong
    while (i and -128 != 0L) {
        writeByte((i and 127 or 128).toInt())
        i = i ushr 7
    }

    writeByte(i.toInt())
}

fun ByteBuf.readString(maxLength: Short = Short.MAX_VALUE): String {
    val length = readVarInt()

    return when {
        length > maxLength * 4 ->
            throw IOException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")")
        length < 0 ->
            throw IOException("The received encoded string buffer length is less than zero! Weird string!")
        else -> {
            val array = ByteArray(length)
            readBytes(array)
            val s = String(array, UTF_8)
            if (s.length > maxLength) {
                throw IOException("The received string length is longer than maximum allowed ($length > $maxLength)")
            } else {
                s
            }
        }
    }
}

fun ByteBuf.writeString(string: String, maxLength: Short = Short.MAX_VALUE) {
    val bytes = string.toByteArray(Charsets.UTF_8)
    if (bytes.size > maxLength) {
        throw EncoderException("String too big (was " + bytes.size + " bytes encoded, max " + maxLength + ")")
    } else {
        writeVarInt(bytes.size)
        writeBytes(bytes)
    }
}

fun ByteBuf.readVarIntByteArray(): ByteArray {
    val length = readVarInt()
    val readable = readableBytes()
    if (length > readable) {
        throw DecoderException("Not enough bytes to read. Expected Length: $length, actual length: $readable")
    }
    val bytes = ByteArray(length)
    readBytes(bytes)
    return bytes
}

fun ByteBuf.readAvailableBytes(length: Int): ByteArray {
    if (hasArray()) return readBytes(length).array()

    val bytes = ByteArray(length)
    readBytes(bytes, readerIndex(), length)
    return bytes
}

fun ByteBuf.readAllAvailableBytes(): ByteArray {
    val length = readableBytes()
    if (hasArray()) return array()

    val bytes = ByteArray(length)
    readBytes(bytes, readerIndex(), length)
    return bytes
}

fun ByteBuf.writeLongArray(array: LongArray) {
    writeVarInt(array.size)
    array.forEach { writeLong(it) }
}

fun ByteBuf.writeOptionalVarInt(varInt: Optional<Int>) {
    if (varInt.value != null) {
        writeBoolean(true)
        writeVarInt(varInt.value)
    } else {
        writeBoolean(false)
    }
}

fun ByteBuf.writeUUID(uuid: UUID) {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
}

fun ByteBuf.writeOptionalUUID(uuid: Optional<UUID>) {
    if (uuid.value != null) {
        writeBoolean(true)
        writeUUID(uuid.value)
    } else {
        writeBoolean(false)
    }
}

fun ByteBuf.writeNBTCompound(tag: CompoundBinaryTag) {
    val outputStream = ByteArrayOutputStream()
    BinaryTagIO.writer().write(tag, outputStream)
    writeBytes(outputStream.toByteArray())
}

fun ByteBuf.writeChat(component: Component) {
    writeString(GsonComponentSerializer.gson().serialize(component))
}

fun ByteBuf.writeOptionalChat(component: Optional<Component>) {
    if (component.value != null) {
        writeBoolean(true)
        writeChat(component.value)
    } else {
        writeBoolean(false)
    }
}

fun ByteBuf.writeSlot(slot: Slot) {
    writeBoolean(slot.isPresent)
    if (slot.isPresent) {
        writeVarInt(slot.itemId)
        writeByte(slot.itemCount)
        writeNBTCompound(slot.nbt)
    }
}

fun ByteBuf.writeRotation(rotation: Rotation) {
    writeFloat(rotation.x)
    writeFloat(rotation.y)
    writeFloat(rotation.z)
}

fun ByteBuf.writePosition(position: Vector) {
    writeLong(position.toProtocol())
}

fun ByteBuf.writeOptionalPosition(position: Optional<Vector>) {
    if (position.value != null) {
        writeBoolean(true)
        writePosition(position.value)
    } else {
        writeBoolean(false)
    }
}

fun ByteBuf.writeParticle(particle: ParticleEffect, location: Location) {
    writeInt(particle.type.id)
    writeBoolean(particle.longDistance)

    val data = particle.data

    // Write location. If the particle is directional, colorable, or a note then we need to manually apply the offsets first
    when (data) {
        is DirectionalParticleData, is ColorParticleData, is NoteParticleData -> {
            val random = ThreadLocalRandom.current()
            writeDouble(location.x + particle.offset.x * random.nextGaussian())
            writeDouble(location.y + particle.offset.y * random.nextGaussian())
            writeDouble(location.z + particle.offset.z * random.nextGaussian())
        }
        else -> {
            writeDouble(location.x)
            writeDouble(location.y)
            writeDouble(location.z)
        }
    }

    // Write offsets depending on what type of particle it is
    when (data) {
        // Particle is directional, the offset fields are used for the direction
        is DirectionalParticleData -> {
            val random = ThreadLocalRandom.current()
            val direction = data.direction ?: Vector(random.nextGaussian(), random.nextGaussian(), random.nextGaussian())

            writeFloat(direction.x.toFloat())
            writeFloat(direction.y.toFloat())
            writeFloat(direction.z.toFloat())
        }
        // Particle is colorable, the offset fields are used to define the color
        is ColorParticleData -> {
            writeFloat(data.red.toFloat() / 255.0F)
            writeFloat(data.green.toFloat() / 255.0F)
            writeFloat(data.blue.toFloat() / 255.0F)
        }
        // Particle is a note, the offset fields are used to define the note value (only the x field)
        is NoteParticleData -> {
            writeFloat(data.note.toFloat() / 24.0F)
            writeFloat(0.0F)
            writeFloat(0.0F)
        }
        // Particle is normal, let the client handle the offsets
        else -> {
            writeFloat(particle.offset.x.toFloat())
            writeFloat(particle.offset.y.toFloat())
            writeFloat(particle.offset.z.toFloat())
        }
    }

    // Write the extra data
    when (data) {
        // Used for the velocity
        is DirectionalParticleData -> writeFloat(data.velocity)
        // Default to 1 otherwise
        else -> writeFloat(1.0F)
    }

    // Write the count
    when (data) {
        // Count needs to be set to 0 so the special particle properties get applied
        is DirectionalParticleData, is ColorParticleData, is NoteParticleData -> writeInt(0)
        // Count is set to whatever is defined
        else -> writeInt(particle.quantity)
    }

    // Write the data, if applicable
    when (data) {
        is DustParticleData -> {
            // If the red value is exactly 0, it will be displayed as 255 in the client. We can use a really small value
            // to bypass this.
            writeFloat(if (data.color.red == 0.toUByte()) Float.MIN_VALUE else data.color.red.toFloat() / 255.0F)
            writeFloat(data.color.green.toFloat() / 255.0F)
            writeFloat(data.color.blue.toFloat() / 255.0F)
            writeFloat(max(0.01F, min(4.0F, data.scale)))
        }
        is BlockParticleData -> writeVarInt(data.id)
        is ItemParticleData -> writeSlot(Slot(true, data.id, 1)) // TODO: Item
    }
}

fun ByteBuf.writeVillagerData(data: VillagerData) {
    writeVarInt(data.type.id)
    writeVarInt(data.profession.id)
    writeVarInt(data.level)
}

fun ByteBuf.writeAngle(angle: Angle) {
    writeUByte(angle.value)
}

fun ByteBuf.writeKey(key: NamespacedKey) {
    writeString(key.toString())
}

fun ByteBuf.writeDuration(duration: Duration) {
    writeInt(duration.seconds.toInt() * 20)
}

fun Int.varIntSize(): Int {
    for (i in 1 until 5) {
        if ((this and (-1 shl i * 7)) != 0) continue
        return i
    }
    return 5
}