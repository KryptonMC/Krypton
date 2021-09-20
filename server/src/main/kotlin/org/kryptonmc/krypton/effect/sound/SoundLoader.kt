package org.kryptonmc.krypton.effect.sound

import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.util.KryptonDataLoader

object SoundLoader : KryptonDataLoader("sounds") {

    override fun load(data: JsonObject) {
        data.keySet().forEach {
            val key = Key.key(it)
            if (InternalRegistries.SOUND_EVENT.contains(key)) return@forEach
            KryptonRegistryManager.register(InternalRegistries.SOUND_EVENT, key, KryptonSoundEvent(key))
        }
    }
}
