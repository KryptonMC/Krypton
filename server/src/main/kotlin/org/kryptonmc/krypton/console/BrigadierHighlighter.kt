/*
 * This file is part of the Krypton project, and originates from the Sponge project,
 * licensed under the terms of the GNU General Public License v3.0
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
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
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/SpongePowered/Sponge/blob/eacd4f1c956cde1c87f2190145ef968ea05f0318/vanilla/src/main/java/org/spongepowered/vanilla/chat/console/BrigadierHighlighter.java
 */
package org.kryptonmc.krypton.console

import org.apache.logging.log4j.LogManager
import org.jline.reader.Highlighter
import org.jline.reader.LineReader
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.KryptonCommandManager
import java.util.function.Supplier
import java.util.regex.Pattern
import java.util.stream.IntStream
import kotlin.math.min

/**
 * Used for doing console highlighting using Brigadier.
 */
class BrigadierHighlighter(
    private val commandManager: KryptonCommandManager,
    private val commandSourceProvider: Supplier<CommandSourceStack>
) : Highlighter {

    override fun highlight(lineReader: LineReader, buffer: String): AttributedString {
        return try {
            val results = commandManager.parse(commandSourceProvider.get(), buffer)
            val reader = results.reader
            val builder = AttributedStringBuilder()

            var lastPos = 0
            var argumentColorIndex = 0
            results.context.lastChild.nodes.forEach {
                val start = min(it.range.start, reader.totalLength)
                val end = min(it.range.end, reader.totalLength)
                if (lastPos < start) builder.append(reader.string, lastPos, start)
                builder.append(reader.string.substring(start, end), ARGUMENT_STYLES[argumentColorIndex])
                argumentColorIndex = (argumentColorIndex + 1) % ARGUMENT_STYLES.size
                lastPos = end
            }

            if (lastPos < reader.totalLength) {
                val style = if (results.exceptions.isEmpty()) LITERAL_STYLE else ERROR_STYLE
                builder.append(reader.string.substring(lastPos), style)
            }
            builder.toAttributedString()
        } catch (exception: Exception) {
            LOGGER.error("Exception caught whilst trying to highlight command $buffer!", exception)
            AttributedString(buffer)
        }
    }

    override fun setErrorPattern(errorPattern: Pattern?) {
        // do nothing
    }

    override fun setErrorIndex(errorIndex: Int) {
        // do nothing
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private val LITERAL_STYLE = AttributedStyle.DEFAULT
        private val ERROR_STYLE = LITERAL_STYLE.foreground(AttributedStyle.RED)
        private val ARGUMENT_STYLES = IntStream.of(
            AttributedStyle.CYAN,
            AttributedStyle.YELLOW,
            AttributedStyle.GREEN,
            AttributedStyle.MAGENTA,
            AttributedStyle.BLUE
        ).mapToObj { LITERAL_STYLE.foreground(it) }.toArray { arrayOfNulls<AttributedStyle>(it) }
    }
}
