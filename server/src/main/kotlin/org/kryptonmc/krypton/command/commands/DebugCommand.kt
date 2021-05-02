package org.kryptonmc.krypton.command.commands

import com.mojang.brigadier.Message
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.tree.LiteralCommandNode
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.command.BrigadierCommand
import org.kryptonmc.krypton.util.createDirectories
import org.kryptonmc.krypton.util.logger
import java.io.File
import java.io.IOException
import java.nio.file.spi.FileSystemProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DebugCommand(private val server: KryptonServer) : BrigadierCommand {

    override fun command(): LiteralCommandNode<Sender> = literal<Sender>("debug")
        .then(literal<Sender>("start").executes { start(it.source); 1 })
        .then(literal<Sender>("stop").executes { stop(it.source); 1 })
        .then(literal<Sender>("report").executes { report(it.source); 1 })
        .build()

    private fun start(sender: Sender) {
        if (server.continuousProfiler.isEnabled) throw ERROR_ALREADY_RUNNING.create()
        server.startProfiling()
        sender.sendMessage(translatable("commands.debug.started", listOf(text("Started the debug profiler. Type '/debug stop' to stop it."))))
    }

    private fun stop(sender: Sender) {
        if (!server.continuousProfiler.isEnabled) throw ERROR_NOT_RUNNING.create()
        val results = server.finishProfiling()
        val file = File(DEBUG_FOLDER, "profile-results-$TIME_NOW_FORMATTED.txt")
        results.save(file)

        val secondsDuration = results.duration / 1.0E9F
        val ticksPerSecond = results.durationTicks / secondsDuration
        sender.sendMessage(translatable("commands.debug.stopped", listOf(
            text("%.2f".format(Locale.ROOT, secondsDuration)),
            text(results.durationTicks),
            text("%.2f".format(ticksPerSecond))
        )))
    }

    private fun report(sender: Sender) {
        val reportFileName = "debug-report-$TIME_NOW_FORMATTED"
        try {
            val folderPath = DEBUG_FOLDER.toPath().apply { createDirectories() }
            if (ZIP_FILE_SYSTEM_PROVIDER == null) {
                val debugPath = folderPath.resolve(reportFileName)
                server.saveDebugReport(debugPath)
            } else {
                val debugPath = folderPath.resolve("$reportFileName.zip")
                ZIP_FILE_SYSTEM_PROVIDER.newFileSystem(debugPath, mapOf("create" to "true")).use { server.saveDebugReport(it.getPath("/")) }
            }
            sender.sendMessage(translatable("commands.debug.reportSaved", listOf(text(reportFileName))))
        } catch (exception: IOException) {
            LOGGER.error("Failed to save debug dump!", exception)
            sender.sendMessage(translatable("commands.debug.reportFailed"))
        }
    }

    companion object {

        private val ERROR_NOT_RUNNING = SimpleCommandExceptionType(translatable("commands.debug.notRunning").toMessage())
        private val ERROR_ALREADY_RUNNING = SimpleCommandExceptionType(translatable("commands.debug.alreadyRunning").toMessage())
        private val ZIP_FILE_SYSTEM_PROVIDER = FileSystemProvider.installedProviders().firstOrNull { it.scheme.equals("jar", true) }

        private val TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss")
        private val TIME_NOW_FORMATTED: String get() = LocalDateTime.now().format(TIME_FORMAT)

        private val DEBUG_FOLDER = File(CURRENT_DIRECTORY, "debug")
        private val LOGGER = logger<DebugCommand>()
    }
}

private fun Component.toMessage() = AdventureMessage(this)

private class AdventureMessage(private val component: Component) : Message {

    override fun getString() = PlainComponentSerializer.plain().serialize(component)
}
