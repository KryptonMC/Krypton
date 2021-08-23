package org.kryptonmc.krypton.registry

import com.mojang.serialization.Codec
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

fun <T : Any> KryptonRegistry<T>.encode(elementCodec: Codec<T>): CompoundTag = compound {
    string("type", key.location.asString())
    list("value", CompoundTag.ID, values.map {
        compound {
            string("name", get(it)!!.asString())
            int("id", idOf(it))
            put("element", elementCodec.encodeStart(NBTOps, it).get().orThrow())
        }
    })
}
