/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
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
 */
package org.kryptonmc.krypton.util.spark

import kotlinx.collections.immutable.persistentSetOf
import me.lucko.spark.api.Spark
import me.lucko.spark.common.SparkPlatform
import me.lucko.spark.common.SparkPlugin
import me.lucko.spark.common.command.sender.CommandSender
import me.lucko.spark.common.platform.PlatformInfo
import me.lucko.spark.common.sampler.ThreadDumper
import me.lucko.spark.common.tick.TickHook
import me.lucko.spark.common.tick.TickReporter
import me.lucko.spark.common.util.ClassSourceLookup
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.command.SimpleCommand
import org.kryptonmc.api.command.meta.simpleCommandMeta
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.plugin.KryptonPluginDescription
import org.kryptonmc.krypton.util.spark.ticking.KryptonSparkTickHook
import org.kryptonmc.krypton.util.spark.ticking.KryptonSparkTickReporter
import java.nio.file.Path
import java.util.stream.Stream

class KryptonSparkPlugin(
    private val server: KryptonServer,
    private val folder: Path
) : SparkPlugin, SimpleCommand {

    val description: PluginDescription = KryptonPluginDescription(
        "spark",
        "Spark",
        KryptonPlatform.sparkVersion,
        "spark is a performance profiling plugin/mod for Minecraft clients, servers and proxies.",
        persistentSetOf("Luck"),
        emptySet(),
        folder
    )
    private lateinit var platform: SparkPlatform
    private val platformInfo = KryptonSparkPlatformInfo

    lateinit var api: Spark
        private set
    val tickHook: KryptonSparkTickHook by lazy { KryptonSparkTickHook() }
    val tickReporter: KryptonSparkTickReporter by lazy { KryptonSparkTickReporter() }

    fun start() {
        platform = SparkPlatform(this)
        platform.enable()
        server.commandManager.register(this, simpleCommandMeta("spark") {})
    }

    fun stop() {
        platform.disable()
    }

    override fun execute(sender: Sender, args: Array<String>) {
        platform.executeCommand(KryptonSparkCommandSender(sender), args)
    }

    override fun suggest(sender: Sender, args: Array<String>): List<String> = platform.tabCompleteCommand(KryptonSparkCommandSender(sender), args)

    override fun getCommandName(): String = "spark"

    override fun getCommandSenders(): Stream<out CommandSender> = Stream.concat(
        server.players.stream(),
        Stream.of(server.console)
    ).map(::KryptonSparkCommandSender)

    override fun executeAsync(task: Runnable) {
        server.scheduler.run(this) { task.run() }
    }

    override fun getDefaultThreadDumper(): ThreadDumper = ThreadDumper.ALL

    override fun createTickHook(): TickHook = tickHook

    override fun createTickReporter(): TickReporter = tickReporter

    override fun createClassSourceLookup(): ClassSourceLookup = KryptonSparkClassSourceLookup

    override fun getPlatformInfo(): PlatformInfo = platformInfo

    override fun getPluginDirectory(): Path = folder

    override fun getVersion(): String = KryptonPlatform.sparkVersion

    override fun registerApi(api: Spark) {
        this.api = api
    }
}
