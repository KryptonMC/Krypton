/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

        fun build(): KryptonDamageType {
            return KryptonDamageType(key, translationKey, damagesHelmet, bypassesArmor, bypassesInvulnerability, bypassesMagic, exhaustion, fire,
                projectile, scalesWithDifficulty, magic, explosion, fall, thorns, aggravatesTarget)
        }
    }
}
