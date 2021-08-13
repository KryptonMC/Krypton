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

import org.kryptonmc.api.command.meta.CommandMeta

open class KryptonCommandMeta(
    override val name: String,
    override val aliases: Set<String>
) : CommandMeta {

    override fun toBuilder() = Builder(this)

    open class Builder(protected var name: String) : CommandMeta.Builder {

        protected val aliases = mutableSetOf<String>()

        constructor(meta: CommandMeta) : this(meta.name) {
            aliases += meta.aliases
        }

        override fun name(name: String) = apply { this.name = name }

        override fun alias(alias: String) = apply { aliases += alias }

        override fun aliases(vararg aliases: String) = apply { this.aliases += aliases }

        override fun aliases(aliases: Iterable<String>) = apply { this.aliases += aliases }

        override fun build() = KryptonCommandMeta(name, aliases)
    }

    object Factory : CommandMeta.Factory {

        override fun builder(name: String) = Builder(name)

        override fun simpleBuilder(name: String) = KryptonSimpleCommandMeta.Builder(name)
    }
}
