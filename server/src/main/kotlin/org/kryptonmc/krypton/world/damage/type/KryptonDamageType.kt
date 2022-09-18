/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.damage.type

import net.kyori.adventure.key.Key
import org.kryptonmc.api.world.damage.type.DamageType

@JvmRecord
data class KryptonDamageType(
    private val key: Key,
    private val translationKey: String,
    override val damagesHelmet: Boolean,
    override val bypassesArmor: Boolean,
    override val bypassesInvulnerability: Boolean,
    override val bypassesMagic: Boolean,
    override val exhaustion: Float,
    override val isFire: Boolean,
    override val isProjectile: Boolean,
    override val scalesWithDifficulty: Boolean,
    override val isMagic: Boolean,
    override val isExplosion: Boolean,
    override val isFall: Boolean,
    override val isThorns: Boolean,
    override val aggravatesTarget: Boolean,
) : DamageType {

    override fun key(): Key = key

    override fun translationKey(): String = translationKey

    class Builder(private var key: Key, private var translationKey: String) {

        private var damagesHelmet = false
        private var bypassesArmor = false
        private var bypassesInvulnerability = false
        private var bypassesMagic = false
        private var bypassesEnchantments = false
        private var exhaustion = 0.1F
        private var fire = false
        private var projectile = false
        private var scalesWithDifficulty = false
        private var magic = false
        private var explosion = false
        private var fall = false
        private var aggravatesTarget = true
        private var thorns = false

        fun damagesHelmet(): Builder = apply { damagesHelmet = true }

        fun bypassArmor(): Builder = apply {
            bypassesArmor = true
            exhaustion = 0F
        }

        fun bypassInvulnerability(): Builder = apply { bypassesInvulnerability = true }

        fun bypassMagic(): Builder = apply {
            bypassesMagic = true
            exhaustion = 0F
        }

        fun bypassEnchantments(): Builder = apply { bypassesEnchantments = true }

        fun fire(): Builder = apply { fire = true }

        fun projectile(): Builder = apply { projectile = true }

        fun scalesWithDifficulty(): Builder = apply { scalesWithDifficulty = true }

        fun magic(): Builder = apply { magic = true }

        fun explosion(): Builder = apply { explosion = true }

        fun fall(): Builder = apply { fall = true }

        fun noAggravatesTarget(): Builder = apply { aggravatesTarget = false }

        fun thorns(): Builder = apply { thorns = true }

        fun build(): KryptonDamageType = KryptonDamageType(
            key,
            translationKey,
            damagesHelmet,
            bypassesArmor,
            bypassesInvulnerability,
            bypassesMagic,
            exhaustion,
            fire,
            projectile,
            scalesWithDifficulty,
            magic,
            explosion,
            fall,
            thorns,
            aggravatesTarget
        )
    }
}
