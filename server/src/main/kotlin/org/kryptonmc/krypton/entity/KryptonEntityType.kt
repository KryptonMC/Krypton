package org.kryptonmc.krypton.entity

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType

@JvmRecord
data class KryptonEntityType<T : Entity>(
    private val key: Key,
    override val isSummonable: Boolean,
    override val translation: TranslatableComponent
) : EntityType<T> {

    override fun key(): Key = key

    object Factory : EntityType.Factory {

        override fun <T : Entity> of(
            key: Key,
            summonable: Boolean,
            translation: TranslatableComponent
        ): EntityType<T> = KryptonEntityType(key, summonable, translation)

        override fun <T : Entity> of(key: Key, summonable: Boolean): EntityType<T> = KryptonEntityType(
            key,
            summonable,
            Component.translatable("entity.${key.namespace()}.${key.value().replace('/', '.')}")
        )
    }
}
