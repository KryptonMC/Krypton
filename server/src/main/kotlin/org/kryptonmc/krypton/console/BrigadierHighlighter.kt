/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
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
 */
package org.kryptonmc.krypton.console

import com.mojang.brigadier.CommandDispatcher
import org.jline.reader.Highlighter
import org.jline.reader.LineReader
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.util.logger
import java.util.regex.Pattern
import kotlin.math.min

class BrigadierHighlighter(
    private val console: KryptonConsole,
    private val dispatcher: CommandDispatcher<Sender>
) : Highlighter {

    override fun highlight(lineReader: LineReader, buffer: String): AttributedString {
        return try {
            val results = dispatcher.parse(buffer, console)
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

    override fun setErrorPattern(errorPattern: Pattern?) = Unit

    override fun setErrorIndex(errorIndex: Int) = Unit

    companion object {

        private val LOGGER = logger<BrigadierHighlighter>()
        private val ERROR_STYLE = AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)
        private val LITERAL_STYLE = AttributedStyle.DEFAULT
        private val ARGUMENT_STYLES = sequenceOf(
            AttributedStyle.CYAN,
            AttributedStyle.YELLOW,
            AttributedStyle.GREEN,
            AttributedStyle.MAGENTA,
            AttributedStyle.BLUE
        ).map(AttributedStyle.DEFAULT::foreground).toList().toTypedArray()
    }
}
