/*
 * This file is part of the Krypton project, and originates from the Sponge project,
 * licensed under the terms of the GNU General Public License v3.0
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
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/SpongePowered/Sponge/blob/api-8/vanilla/src/main/java/org/spongepowered/vanilla/chat/console/BrigadierJLineCompleter.java
 */
package org.kryptonmc.krypton.console

import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine
import org.kryptonmc.krypton.util.logger
import java.util.concurrent.ExecutionException

/**
 * Used for providing Brigadier completions for the console.
 */
class BrigadierCompleter(private val console: KryptonConsole) : Completer {

    override fun complete(reader: LineReader, line: ParsedLine, candidates: MutableList<Candidate>) {
        val input = line.line()
        val parseResults = console.server.commandManager.parse(console, input)
        val suggestions = console.server.commandManager.suggest(parseResults, line.cursor())

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

        private val LOGGER = logger<BrigadierCompleter>()
    }
}
