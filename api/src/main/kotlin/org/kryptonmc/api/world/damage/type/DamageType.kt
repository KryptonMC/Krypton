/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.damage.type

import net.kyori.adventure.builder.AbstractBuilder
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A type of damage to something.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(DamageTypes::class)
public interface DamageType : Keyed {

    /**
     * The translation key used in the death message that is created from this
     * damage type.
     */
    @get:JvmName("translationKey")
    public val translationKey: String

    /**
     * If this damage type will damage its target's helmet.
     */
    @get:JvmName("damagesHelmet")
    public val damagesHelmet: Boolean

    /**
     * If this damage type bypasses protection from the target's armour.
     */
    @get:JvmName("bypassesArmor")
    public val bypassesArmor: Boolean

    /**
     * If this damage type bypasses invulnerability protection, such as a
     * player being in creative.
     */
    @get:JvmName("bypassesInvulnerability")
    public val bypassesInvulnerability: Boolean

    /**
     * If this damage type bypasses any protection offered by magic, such as
     * potions.
     */
    @get:JvmName("bypassesMagic")
    public val bypassesMagic: Boolean

    /**
     * The amount of exhaustion that this damage type will inflict upon a
     * target.
     */
    @get:JvmName("exhaustion")
    public val exhaustion: Float

    /**
     * If this damage type causes fire damage.
     */
    public val isFire: Boolean

    /**
     * If this damage type causes projectile damage.
     */
    public val isProjectile: Boolean

    /**
     * If this damage type's effects are increased as the difficulty increases.
     */
    @get:JvmName("scalesWithDifficulty")
    public val scalesWithDifficulty: Boolean

    /**
     * If this damage type causes magic damage.
     */
    public val isMagic: Boolean

    /**
     * If this damage type causes explosion damage.
     */
    public val isExplosion: Boolean

    /**
     * If this damage type causes fall damage.
     */
    public val isFall: Boolean

    /**
     * If this damage type causes thorns damage.
     */
    public val isThorns: Boolean

    /**
     * If the target this damage type is applied to will be aggravated by the
     * damage caused by applying this damage type.
     */
    @get:JvmName("aggravatesTarget")
    public val aggravatesTarget: Boolean

    /**
     * A builder for damage types.
     */
    public interface Builder : AbstractBuilder<DamageType> {

        /**
         * Sets the key for the damage type to the given [key].
         *
         * @param key the key
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun key(key: Key): Builder

        /**
         * Sets the key used for constructing the translation message for the
         * damage type to the given [translationKey].
         *
         * @param translationKey the translation key
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun translationKey(translationKey: String): Builder

        /**
         * Sets whether the damage type causes helmet damage to the given
         * [value].
         *
         * @param value whether the damage type damages helmets
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun damagesHelmet(value: Boolean): Builder

        /**
         * Makes the damage type cause helmet damage.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun damagesHelmet(): Builder = damagesHelmet(true)

        /**
         * Sets whether the damage type bypasses armour protection to the given
         * [value].
         *
         * @param value whether the damage type bypasses armour
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun bypassesArmor(value: Boolean): Builder

        /**
         * Makes the damage type bypass armour protection.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun bypassesArmor(): Builder = bypassesArmor(true)

        /**
         * Sets whether the damage type bypasses invulnerability to the given
         * [value].
         *
         * @param value whether the damage type bypasses invulnerability
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun bypassesInvulnerability(value: Boolean): Builder

        /**
         * Makes the damage type bypass invulnerability.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun bypassesInvulnerability(): Builder = bypassesInvulnerability(true)

        /**
         * Sets whether the damage type bypasses magic protection to the given
         * [value].
         *
         * @param value whether the damage type bypasses magic
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun bypassesMagic(value: Boolean): Builder

        /**
         * Makes the damage type bypass magic protection.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun bypassesMagic(): Builder = bypassesMagic(true)

        /**
         * Sets the exhaustion that the damage type will cause when applied
         * to the given [value].
         *
         * @param value the exhaustion amount
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun exhaustion(value: Float): Builder

        /**
         * Sets whether the damage type causes fire damage to the given
         * [value].
         *
         * @param value whether the damage type causes fire damage
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fire(value: Boolean): Builder

        /**
         * Makes the damage type cause fire damage.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fire(): Builder = fire(true)

        /**
         * Sets whether the damage type causes projectile damage to the given
         * [value].
         *
         * @param value whether the damage type causes projectile damage
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun projectile(value: Boolean): Builder

        /**
         * Makes the damage type cause projectile damage.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun projectile(): Builder = projectile(true)

        /**
         * Sets whether the damage type scales with difficulty to the given
         * [value].
         *
         * @param value whether the damage type scales with difficulty
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun scalesWithDifficulty(value: Boolean): Builder

        /**
         * Makes the damage type scale with difficulty.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun scalesWithDifficulty(): Builder = scalesWithDifficulty(true)

        /**
         * Sets whether the damage type causes magic damage to the given
         * [value].
         *
         * @param value whether the damage type causes magic damage
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun magic(value: Boolean): Builder

        /**
         * Makes the damage type cause magic damage.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun magic(): Builder = magic(true)

        /**
         * Sets whether the damage type causes explosion damage to the given
         * [value].
         *
         * @param value whether the damage type causes explosion damage
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun explosion(value: Boolean): Builder

        /**
         * Makes the damage type cause explosion damage.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun explosion(): Builder = explosion(true)

        /**
         * Sets whether the damage type causes fall damage to the given
         * [value].
         *
         * @param value whether the damage type causes fall damage
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fall(value: Boolean): Builder

        /**
         * Makes the damage type cause fall damage.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fall(): Builder = fall(true)

        /**
         * Sets whether the damage type causes thorns damage to the given
         * [value].
         *
         * @param value whether the damage type causes thorns damage
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun thorns(value: Boolean): Builder

        /**
         * Makes the damage type cause thorns damage.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun thorns(): Builder = thorns(true)

        /**
         * Sets whether the damage type aggravates targets to the given
         * [value].
         *
         * @param value whether the damage type aggravates targets
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun aggravatesTarget(value: Boolean): Builder

        /**
         * Makes the damage type aggravate targets.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun aggravatesTarget(): Builder = aggravatesTarget(true)
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(key: Key, translationKey: String): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder to build a dimension type.
         *
         * @param key the key
         * @param translationKey the translation key
         * @return a new builder
         */
        @JvmStatic
        public fun builder(key: Key, translationKey: String): Builder = FACTORY.builder(key, translationKey)
    }
}
