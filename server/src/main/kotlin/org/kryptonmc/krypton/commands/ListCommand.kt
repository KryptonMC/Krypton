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
package org.kryptonmc.krypton.commands

import com.mojang.brigadier.CommandDispatcher
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.kryptonmc.api.command.literal
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.locale.CommandMessages

object ListCommand {

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literalCommand("list") {
            requiresPermission(KryptonPermission.LIST)
            runs { context -> sendNames(context.source) { PlainTextComponentSerializer.plainText().serialize(it.displayName) } }
            literal("uuids") {
                runs { context ->
                    sendNames(context.source) { "${PlainTextComponentSerializer.plainText().serialize(it.displayName)} (${it.uuid})" }
                }
            }
        })
    }

    @JvmStatic
    private inline fun sendNames(source: CommandSourceStack, nameGetter: (KryptonPlayer) -> String) {
        val names = source.server.players.map(nameGetter)
        CommandMessages.LIST_PLAYERS.sendSuccess(source, source.server.config.status.maxPlayers, names, false)
    }
}
