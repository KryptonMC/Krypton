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
package org.kryptonmc.krypton.command.meta

import kotlinx.collections.immutable.ImmutableSet
import org.kryptonmc.api.command.meta.SimpleCommandMeta

/**
 * This doesn't extend [KryptonCommandMeta] because we want it to be a record,
 * and records cannot have superclasses.
 */
@JvmRecord
data class KryptonSimpleCommandMeta(
    override val name: String,
    override val aliases: ImmutableSet<String>,
    override val permission: String?
) : SimpleCommandMeta {

    override fun toBuilder(): SimpleCommandMeta.Builder = Builder(this)

    class Builder(name: String) : KryptonCommandMeta.AbstractBuilder<Builder>(name), SimpleCommandMeta.Builder {

        private var permission: String? = null

        constructor(meta: SimpleCommandMeta) : this(meta.name) {
            aliases.addAll(meta.aliases)
            permission = meta.permission
        }

        override fun permission(permission: String?): SimpleCommandMeta.Builder = apply { this.permission = permission }

        override fun build(): SimpleCommandMeta = KryptonSimpleCommandMeta(name, aliases.build(), permission)
    }
}
