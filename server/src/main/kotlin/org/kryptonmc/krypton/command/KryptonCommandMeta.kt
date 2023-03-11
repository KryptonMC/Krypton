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
