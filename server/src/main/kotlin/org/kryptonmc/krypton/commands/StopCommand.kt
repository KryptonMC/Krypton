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
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.locale.CommandMessages
import kotlin.system.exitProcess

object StopCommand {

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literalCommand("stop") {
            requiresPermission(KryptonPermission.STOP)
            runs {
                it.source.sendSuccess(CommandMessages.STOP, true)
                // We use exit rather than stop because if this is executed from the console, the server will shut down before
                // it is finished, due to the console handler thread being a daemon thread.
                // Therefore, to avoid this, we use exit, which will call the shutdown hook that calls stop.
                exitProcess(0)
            }
        })
    }
}
