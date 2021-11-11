package org.kryptonmc.krypton.entity.animal.cat

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.animal.cat.CatType

@JvmRecord
data class KryptonCatType(private val key: Key) : CatType {

    override fun key(): Key = key

    object Factory : CatType.Factory {

        override fun of(key: Key): CatType = KryptonCatType(key)
    }
}
