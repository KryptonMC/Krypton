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
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.world.damage.type.DamageType

@JvmRecord
data class KryptonDamageType(
    private val key: Key,
    override val translationKey: String,
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

    class Builder(private var key: Key, private var translationKey: String) : DamageType.Builder {

        private var damagesHelmet = false
        private var bypassesArmor = false
        private var bypassesInvulnerability = false
        private var bypassesMagic = false
        private var exhaustion = 0.1F
        private var fire = false
        private var projectile = false
        private var scalesWithDifficulty = false
        private var magic = false
        private var explosion = false
        private var fall = false
        private var thorns = false
        private var aggravatesTarget = false

        constructor(source: KryptonDamageType) : this(source.key(), source.translationKey) {
            translationKey = source.translationKey
            damagesHelmet = source.damagesHelmet
            bypassesArmor = source.bypassesArmor
            bypassesInvulnerability = source.bypassesInvulnerability
            bypassesMagic = source.bypassesMagic
            exhaustion = source.exhaustion
            fire = source.isFire
            projectile = source.isProjectile
            scalesWithDifficulty = source.scalesWithDifficulty
            magic = source.isMagic
            explosion = source.isExplosion
            fall = source.isFall
        }

        override fun key(key: Key): DamageType.Builder = apply { this.key = key }

        override fun translationKey(translationKey: String): DamageType.Builder = apply { this.translationKey = translationKey }

        override fun damagesHelmet(value: Boolean): DamageType.Builder = apply { damagesHelmet = value }

        override fun bypassesArmor(value: Boolean): DamageType.Builder = apply { bypassesArmor = value }

        override fun bypassesInvulnerability(value: Boolean): DamageType.Builder = apply { bypassesInvulnerability = value }

        override fun bypassesMagic(value: Boolean): DamageType.Builder = apply { bypassesMagic = value }

        override fun exhaustion(value: Float): DamageType.Builder = apply { exhaustion = value }

        override fun fire(value: Boolean): DamageType.Builder = apply { fire = value }

        override fun projectile(value: Boolean): DamageType.Builder = apply { projectile = value }

        override fun scalesWithDifficulty(value: Boolean): DamageType.Builder = apply { scalesWithDifficulty = value }

        override fun magic(value: Boolean): DamageType.Builder = apply { magic = value }

        override fun explosion(value: Boolean): DamageType.Builder = apply { explosion = value }

        override fun fall(value: Boolean): DamageType.Builder = apply { fall = value }

        override fun thorns(value: Boolean): DamageType.Builder = apply { thorns = value }

        override fun aggravatesTarget(value: Boolean): DamageType.Builder = apply { aggravatesTarget = value }

        override fun build(): DamageType = KryptonDamageType(
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

    object Factory : DamageType.Factory {

        override fun builder(key: Key, translationKey: String): DamageType.Builder = Builder(key, translationKey)
    }
}
