package org.kryptonmc.krypton.entity.attribute

import com.google.gson.JsonObject
import me.bardy.gsonkt.fromJson
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.util.KryptonDataLoader
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.block.BlockLoader

object AttributeLoader : KryptonDataLoader("attribute") {

    override fun load(data: JsonObject) {
        data.entrySet().forEach { (key, value) ->
            val namespacedKey = Key.key(key)
            value as JsonObject

            val translationKey = value["translationKey"].asString
            val defaultValue = value["defaultValue"].asDouble
            val clientSync = value["clientSync"].asBoolean
            val range = value["range"].asJsonObject
            val min = range["minValue"].asDouble
            val max = range["maxValue"].asDouble

            if (InternalRegistries.ATTRIBUTE.contains(namespacedKey)) return@forEach
            KryptonRegistryManager.register(
                InternalRegistries.ATTRIBUTE,
                namespacedKey,
                KryptonAttributeType(namespacedKey, defaultValue, min, max, clientSync, Component.translatable(translationKey))
            )
        }
    }
}
