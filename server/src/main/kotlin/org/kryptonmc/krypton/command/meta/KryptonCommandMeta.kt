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
import kotlinx.collections.immutable.persistentSetOf
import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.api.command.meta.SimpleCommandMeta

@JvmRecord
data class KryptonCommandMeta(override val name: String, override val aliases: ImmutableSet<String>) : CommandMeta {

    override fun toBuilder(): CommandMeta.Builder = Builder(this)

    /**
     * This builder allows us to reduce repetition between both of its
     * implementations, and keep them separate. It also avoids the need to add
     * an abstract builder to the API.
     */
    @Suppress("UNCHECKED_CAST")
    abstract class AbstractBuilder<B : AbstractBuilder<B>>(protected var name: String) : CommandMeta.Builder {

        protected val aliases = persistentSetOf<String>().builder()

        final override fun name(name: String): B = apply { this.name = name } as B

        final override fun alias(alias: String): B = apply { aliases.add(alias) } as B

        final override fun aliases(aliases: Iterable<String>): B = apply { this.aliases.addAll(aliases) } as B

        final override fun aliases(vararg aliases: String): B = aliases(aliases.asIterable())
    }

    class Builder(name: String) : AbstractBuilder<Builder>(name) {

        constructor(meta: CommandMeta) : this(meta.name) {
            aliases.addAll(meta.aliases)
        }

        override fun build(): CommandMeta = KryptonCommandMeta(name, aliases.build())
    }

    object Factory : CommandMeta.Factory {

        override fun builder(name: String): CommandMeta.Builder = Builder(name)

        override fun simpleBuilder(name: String): SimpleCommandMeta.Builder = KryptonSimpleCommandMeta.Builder(name)
    }
}
