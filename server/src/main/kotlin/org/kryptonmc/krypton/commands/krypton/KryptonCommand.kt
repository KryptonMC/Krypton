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
package org.kryptonmc.krypton.commands.krypton

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack

object KryptonCommand {

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literalCommand("krypton") {
            registerSubCommand(this, PluginsCommand)
            registerSubCommand(this, InfoCommand)
        })
    }

    @JvmStatic
    private fun registerSubCommand(context: LiteralArgumentBuilder<CommandSourceStack>, command: KryptonSubCommand) {
        val node = command.register().build()
        context.then(node)
        command.aliases().forEach { context.then(LiteralArgumentBuilder.literal<CommandSourceStack>(it).redirect(node)) }
    }
}
