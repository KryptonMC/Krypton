package org.kryptonmc.krypton.console

import com.mojang.brigadier.CommandDispatcher
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.util.logger
import java.util.concurrent.ExecutionException

class BrigadierCompleter(
    private val console: KryptonConsole,
    private val dispatcher: CommandDispatcher<Sender>
) : Completer {

    override fun complete(reader: LineReader, line: ParsedLine, candidates: MutableList<Candidate>) {
        val input = line.line()
        val parseResults = dispatcher.parse(input, console)
        val suggestions = dispatcher.getCompletionSuggestions(parseResults, line.cursor())

        try {
            suggestions.get().list.forEach {
                if (it.text.isEmpty()) return@forEach
                candidates.add(Candidate(
                    it.text,
                    it.text,
                    null,
                    it.tooltip?.string,
                    null,
                    null,
                    parseResults.exceptions.isEmpty()
                ))
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
