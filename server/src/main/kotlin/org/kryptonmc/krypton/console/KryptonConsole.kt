package org.kryptonmc.krypton.console

import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.kryptonmc.krypton.KryptonServer
import kotlin.system.exitProcess

/**
 * @author Callum Seabrook
 */
class KryptonConsole(private val server: KryptonServer) : SimpleTerminalConsole() {

    override fun isRunning() = server.isRunning

    override fun runCommand(command: String) = server.commandManager.dispatch(server.console, command)

    override fun shutdown() = exitProcess(0)

    override fun buildReader(builder: LineReaderBuilder): LineReader = super.buildReader(
        builder.appName("Krypton Console")
    )
}