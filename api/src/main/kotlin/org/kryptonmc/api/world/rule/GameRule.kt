/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.rule

import net.kyori.adventure.translation.Translatable
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A rule dictating how a specific aspect of the game functions.
 *
 * @param V the type of the value
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(GameRules::class)
@ImmutableType
public interface GameRule<V> : Translatable {

    /**
     * The name of this rule.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The default value of this rule.
     */
    @get:JvmName("defaultValue")
    public val defaultValue: V

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun <V> of(name: String): GameRule<V>
    }

    public companion object {

        @JvmSynthetic
        internal fun <V> of(name: String): GameRule<V> = Krypton.factory<Factory>().of(name)
    }
}
