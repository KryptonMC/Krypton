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
package org.kryptonmc.krypton.command

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import org.kryptonmc.api.command.CommandMeta

@JvmRecord
data class KryptonCommandMeta(override val name: String, override val aliases: ImmutableSet<String>) : CommandMeta {

    class Builder(private var name: String) : CommandMeta.Builder {

        private val aliases = persistentSetOf<String>().builder()

        override fun name(name: String): Builder = apply { this.name = name }

        override fun alias(alias: String): Builder = apply { aliases.add(alias) }

        override fun aliases(aliases: Collection<String>): Builder = apply { this.aliases.addAll(aliases) }

        override fun build(): CommandMeta = KryptonCommandMeta(name, aliases.build())
    }

    object Factory : CommandMeta.Factory {

        override fun builder(name: String): CommandMeta.Builder = Builder(name)
    }
}
