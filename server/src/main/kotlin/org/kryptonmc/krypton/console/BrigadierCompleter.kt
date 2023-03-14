/*
 * This file is part of the Krypton project, and originates from the Sponge project,
 * licensed under the terms of the Apache License v2.0
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
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
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/SpongePowered/Sponge/blob/api-8/vanilla/src/main/java/org/spongepowered/vanilla/chat/console/BrigadierJLineCompleter.java
 */
package org.kryptonmc.krypton.console

import org.apache.logging.log4j.LogManager
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.KryptonCommandManager
import java.util.concurrent.ExecutionException
import java.util.function.Supplier

/**
 * Used for providing Brigadier completions for the console.
 */
class BrigadierCompleter(
    private val commandManager: KryptonCommandManager,
    private val commandSourceProvider: Supplier<CommandSourceStack>
) : Completer {

    override fun complete(reader: LineReader, line: ParsedLine, candidates: MutableList<Candidate>) {
        val input = line.line()
        val parseResults = commandManager.parse(commandSourceProvider.get(), input)
        val suggestions = commandManager.suggest(parseResults, line.cursor())

        try {
            suggestions.get().list.forEach {
                if (it.text.isEmpty()) return@forEach
                candidates.add(Candidate(it.text, it.text, null, it.tooltip?.string, null, null, parseResults.exceptions.isEmpty()))
            }
        } catch (exception: InterruptedException) {
            Thread.currentThread().interrupt()
        } catch (exception: ExecutionException) {
            LOGGER.error("Exception caught whilst trying to suggest completions for command $input!", exception)
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
