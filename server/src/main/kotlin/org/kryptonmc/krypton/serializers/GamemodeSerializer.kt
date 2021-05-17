package org.kryptonmc.krypton.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.kryptonmc.api.world.Gamemode
import java.lang.reflect.Type

object GamemodeSerializer : IntOrStringEnumSerializer<Gamemode>() {

    override fun fromInt(value: Int) = Gamemode.fromId(value)

    override fun fromString(value: String) = Gamemode.valueOf(value)
}
