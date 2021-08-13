/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import org.kryptonmc.api.command.meta.SimpleCommandMeta

class KryptonSimpleCommandMeta(
    name: String,
    aliases: Set<String>,
    override val permission: String?
) : KryptonCommandMeta(name, aliases), SimpleCommandMeta {

    override fun toBuilder() = Builder(this)

    class Builder(name: String) : KryptonCommandMeta.Builder(name), SimpleCommandMeta.Builder {

        private var permission: String? = null

        constructor(meta: SimpleCommandMeta) : this(meta.name) {
            aliases += meta.aliases
            permission = meta.permission
        }

        override fun permission(permission: String?) = apply { this.permission = permission }

        override fun build() = KryptonSimpleCommandMeta(name, aliases, permission)
    }
}
