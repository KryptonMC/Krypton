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
package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentParser
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery

/**
 * Parses game profiles as entity selectors for single player targets.
 */
object GameProfileArgument : ArgumentType<EntityQuery> {

    override fun parse(reader: StringReader): EntityQuery {
        if (reader.canRead() && reader.peek() == EntityArgumentParser.SELECTOR_CHAR) {
            reader.skip()
            return EntityArgumentParser.parse(reader, true, false)
        }
        return EntityQuery(EntityQuery.Selector.PLAYER, StringReading.readNonSpaceString(reader))
    }

    @JvmStatic
    fun <S : CommandExecutionContext> get(context: CommandContext<S>, name: String): List<GameProfile> =
        context.getArgument(name, EntityQuery::class.java).getProfiles(context.source as CommandSourceStack)
}
