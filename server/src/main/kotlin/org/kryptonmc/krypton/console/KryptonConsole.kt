package org.kryptonmc.krypton.console

import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.kryptonmc.krypton.KryptonServer

/**
 * The console handler for this server
 */
class KryptonConsole(private val server: KryptonServer) : SimpleTerminalConsole() {

    override fun isRunning() = server.isRunning

    override fun runCommand(command: String) = server.commandManager.dispatch(server.console, command)

    override fun shutdown() = server.stop()

    override fun buildReader(builder: LineReaderBuilder): LineReader = super.buildReader(
        builder.appName("Krypton Console")
    )
}
