/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.util.TranslationHolder

/**
 * A type of entity.
 *
 * @param T the type of entity
 * @param key the key for this entity type
 * @param isSummonable if this entity type can be summoned, for example, with
 *        the /summon command or [org.kryptonmc.api.world.World.spawnEntity]
 * @param translation the translation for this entity type
 */
@JvmRecord
public data class EntityType<T : Entity>(
    private val key: Key,
    public val isSummonable: Boolean,
    public override val translation: TranslatableComponent = translatable(
        "entity.${key.namespace()}.${key.value().replace("/", ".")}"
    ),
) : Keyed, TranslationHolder {

    override fun key(): Key = key
}
