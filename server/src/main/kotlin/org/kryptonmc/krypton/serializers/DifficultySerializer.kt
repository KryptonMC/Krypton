package org.kryptonmc.krypton.serializers

import org.kryptonmc.api.world.Difficulty

object DifficultySerializer : IntOrStringEnumSerializer<Difficulty>() {

    override fun fromInt(value: Int) = Difficulty.fromId(value)

    override fun fromString(value: String) = Difficulty.valueOf(value)
}
