package org.kryptonmc.krypton.serializers

import org.kryptonmc.api.world.Gamemode

object GamemodeSerializer : IntOrStringEnumSerializer<Gamemode>() {

    override fun fromInt(value: Int) = Gamemode.fromId(value)

    override fun fromString(value: String) = Gamemode.valueOf(value)
}
