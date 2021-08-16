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
package org.kryptonmc.krypton.locale

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.adventure.toSectionText
import org.kryptonmc.krypton.util.thread.GenericThread
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.io.IOException
import java.nio.file.Path
import java.util.UUID

object Messages {

    // Krypton version {0} for Minecraft {1}
    val VERSION_INFO = doubleText("version-info")

    val START = StartMessages
    val AUTOSAVE = AutosaveMessages
    val STOP = StopMessages
    val RESTART = RestartMessages
    val WORLD = WorldMessages
    val REGION = RegionMessages
    val AUTH = AuthMessages
    val COMMAND = CommandMessages
    val COMMANDS = CommandsMessages
    val BUNGEE = BungeeMessages
    val VELOCITY = VelocityMessages
    val NETWORK = NetworkMessages
    val PLUGIN = PluginMessages
    val PROFILER = ProfilerMessages
    val THREAD = ThreadMessages

    // Starting Krypton server version {0} for Minecraft {1}
    val LOAD = doubleText("load")

    // You're starting the server with {0} megabytes of RAM.
    // Consider starting it with more by using \"java -Xmx1024M -Xms1024M -jar Krypton-{1}.jar\" to start it with 1 GB RAM.
    val LOAD_LOW_MEMORY = doubleText("load.low-memory")

    // FAILED TO BIND TO PORT {0}
    // Exception: {1}
    // Perhaps a server is already running on that port?
    val ERROR_BIND = Args2<Int, IOException> { port, exception -> translatable("krypton.error.bind", text(port), text(exception.toString())) }

    // SERVER IS IN OFFLINE MODE! THIS SERVER WILL MAKE NO ATTEMPTS TO AUTHENTICATE USERS!
    // While this may allow players without full Minecraft accounts to connect, it also allows hackers to connect with any username they choose! Beware!
    // To get rid of this message, change online-mode to true in config.conf
    val PIRACY_WARNING = empty("piracy-warning")

    // Woah there! Can't keep up! Running {0}ms ({1} ticks) behind!
    val TICK_OVERLOAD_WARNING = Args2<Long, Long> { ms, ticks -> translatable("krypton.overload-warning", text(ms), text(ticks)) }

    // Krypton and some of its plugins collect metrics and send them to bStats (https://bstats.org).
    // bStats collects some basic information for plugin authors, like how many people use
    // their plugin and their total player count. It's recommended to keep bStats enabled, but
    // if you're not comfortable with this, you can opt-out by editing the config.txt file in
    // the '/plugins/bStats/' folder and setting enabled to false.
    val METRICS_INFO = empty("metrics.info")

    // Could not serialise {0} (Class {1})! Will not be sent to client!
    val ARGUMENT_ERROR = doubleText("argument-error")

    // Plugin {0} generated an exception from task {1}!
    val SCHEDULE_ERROR = Args2<String, Runnable> { name, task -> translatable("krypton.schedule-error", text(name), text(task.toString())) }

    sealed interface Args {

        fun text(component: Component) = PlainTextComponentSerializer.plainText().serialize(TranslationManager.render(component))

        fun send(sender: Sender, component: Component) = sender.sendMessage(TranslationManager.render(component))

        fun print(component: Component) = TranslationManager.render(component).toSectionText().split("\n").forEach { println(it) }

        fun log(logger: Logger, level: Level, component: Component) = TranslationManager.render(component).toSectionText().split("\n").forEach { logger.log(level, it) }

        fun log(logger: Logger, level: Level, component: Component, exception: Throwable) = logger.log(
            level,
            TranslationManager.render(component).toSectionText(),
            exception
        )

        fun info(logger: Logger, component: Component) = log(logger, Level.INFO, component)
        fun warn(logger: Logger, component: Component) = log(logger, Level.WARN, component)
        fun error(logger: Logger, component: Component) = log(logger, Level.ERROR, component)
        fun error(logger: Logger, component: Component, exception: Throwable) = log(logger, Level.ERROR, component, exception)
        fun fatal(logger: Logger, component: Component) = log(logger, Level.FATAL, component)
        fun fatal(logger: Logger, component: Component, exception: Throwable) = log(logger, Level.FATAL, component, exception)
    }

    fun interface Args0 : () -> Component, Args {

        fun text() = text(this())
        fun send(sender: Sender) = send(sender, this())
        fun info(logger: Logger) = info(logger, this())
        fun warn(logger: Logger) = warn(logger, this())
        fun error(logger: Logger) = error(logger, this())
        fun error(logger: Logger, exception: Throwable) = error(logger, this(), exception)
    }

    fun interface Args1<A> : (A) -> Component, Args {

        fun text(arg1: A) = text(this(arg1))
        fun send(sender: Sender, arg1: A) = send(sender, this(arg1))
        fun print(arg1: A) = print(this(arg1))
        fun info(logger: Logger, arg1: A) = info(logger, this(arg1))
        fun warn(logger: Logger, arg1: A) = warn(logger, this(arg1))
        fun error(logger: Logger, arg1: A) = error(logger, this(arg1))
        fun error(logger: Logger, arg1: A, exception: Throwable) = error(logger, this(arg1), exception)
    }

    fun interface Args2<A, B> : (A, B) -> Component, Args {

        fun text(arg1: A, arg2: B) = text(this(arg1, arg2))
        fun send(sender: Sender, arg1: A, arg2: B) = send(sender, this(arg1, arg2))
        fun print(arg1: A, arg2: B) = print(this(arg1, arg2))
        fun info(logger: Logger, arg1: A, arg2: B) = info(logger, this(arg1, arg2))
        fun warn(logger: Logger, arg1: A, arg2: B) = warn(logger, this(arg1, arg2))
        fun error(logger: Logger, arg1: A, arg2: B) = error(logger, this(arg1, arg2))
        fun error(logger: Logger, arg1: A, arg2: B, exception: Throwable) = error(logger, this(arg1, arg2), exception)
    }

    fun interface Args3<A, B, C> : (A, B, C) -> Component, Args {

        fun text(arg1: A, arg2: B, arg3: C) = text(this(arg1, arg2, arg3))
        fun send(sender: Sender, arg1: A, arg2: B, arg3: C) = send(sender, this(arg1, arg2, arg3))
        fun info(logger: Logger, arg1: A, arg2: B, arg3: C) = info(logger, this(arg1, arg2, arg3))
        fun warn(logger: Logger, arg1: A, arg2: B, arg3: C) = warn(logger, this(arg1, arg2, arg3))
        fun error(logger: Logger, arg1: A, arg2: B, arg3: C) = error(logger, this(arg1, arg2, arg3))
        fun error(logger: Logger, arg1: A, arg2: B, arg3: C, exception: Throwable) = error(logger, this(arg1, arg2, arg3), exception)
    }

    object StartMessages {

        // Starting Krypton server on {0}:{1}...
        val INITIAL = Args2<String, Int> { ip, port -> translatable("krypton.start", text(ip), text(port)) }

        // Done ({0})! Type "help" for help.
        val DONE = singleText("start.done")
    }

    object AutosaveMessages {

        // Autosave started
        val STARTED = empty("autosave.started")

        // Autosave finished
        val FINISHED = empty("autosave.finished")
    }

    object StopMessages {

        // Stopping Krypton...
        val INITIAL = empty("stop")

        // Saving player, world and region data...
        val SAVE = empty("stop.save")

        // Shutting down plugins...
        val PLUGINS = empty("stop.plugins")

        // Goodbye
        val GOODBYE = empty("stop.goodbye")
    }

    object RestartMessages {

        // Attempting to restart the server with script {0}...
        val ATTEMPT = singleText("restart.attempt")

        // Unable to find restart script {0}! Refusing to restart.
        val NO_SCRIPT = singleText("restart.no-script")
    }

    object WorldMessages {

        // Loading world {0}...
        val LOAD = singleText("world.load")

        // World loaded!
        val LOADED = empty("world.loaded")

        // Error loading world {0}!
        val LOAD_ERROR = singleText("world.load-error")

        // World with name {0} does not exist!
        val NOT_FOUND = singleText("world.not-found")
    }

    object RegionMessages {

        val SECTOR = SectorMessages
        val CHUNK = ChunkMessages

        // Region file {0} has truncated header: {1}
        val TRUNCATED = Args2<Path, Int> { path, first -> translatable("krypton.region.truncated", text(path.toString()), text(first)) }

        object SectorMessages {

            // Region file {0} has an invalid sector at index {1}. Sector {2} overlaps with header
            val OVERLAP = Args3<Path, Int, Int> { path, index, sector ->
                translatable("krypton.region.sector.overlap", text(path.toString()), text(index), text(sector))
            }

            // Region file {0} has an invalid sector at index {1}. Size has to be > 0
            val SIZE = Args2<Path, Int> { path, index -> translatable("krypton.region.sector.size", text(path.toString()), text(index)) }

            // Region file {0} has an invalid sector at index {1}. Sector {2} is out of bounds
            val OUT_OF_BOUNDS = Args3<Path, Int, Int> { path, index, sector ->
                translatable("krypton.region.sector.out-of-bounds", text(path.toString()), text(index), text(sector))
            }
        }

        object ChunkMessages {

            val EXTERNAL = ExternalMessages

            // Chunk {0}'s header is truncated! Expected {1} but got {2}!
            val TRUNCATED = Args3<ChunkPosition, Long, Int> { position, expected, reality ->
                translatable("krypton.region.chunk.truncated", text(position.toString()), text(expected), text(reality))
            }

            // Chunk {0} is allocated, but stream is missing.
            val NO_STREAM = Args1<ChunkPosition> { position -> translatable("krypton.region.chunk.no-stream", text(position.toString())) }

            // Chunk has both internal and external streams.
            val INTERNAL_EXTERNAL = empty("region.chunk.internal-external")

            // Chunk {0}'s stream is truncated! Expected {1} but got {2}
            val STREAM_TRUNCATED = Args3<ChunkPosition, Int, Int> { position, expected, reality ->
                translatable("krypton.region.chunk.stream-truncated", text(position.toString()), text(expected), text(reality))
            }

            // Declared size {0} of chunk {1} is negative!
            val NEGATIVE = Args2<Int, ChunkPosition> { size, position ->
                translatable("krypton.region.chunk.negative", text(size), text(position.toString()))
            }

            // Chunk {0} has an invalid compression type! Type: {1}
            val INVALID_COMPRESSION_TYPE = Args2<ChunkPosition, Byte> { position, type ->
                translatable("krypton.region.chunk.invalid-compression-type", text(position.toString()), text(type.toInt()))
            }

            object ExternalMessages {

                // Saving oversized chunk {0} ({1} bytes) to external file {2}.
                val SAVE = Args3<ChunkPosition, Int, Path> { position, size, path ->
                    translatable("krypton.region.chunk.external.save", text(position.toString()), text(size), text(path.toString()))
                }

                // External chunk path {0} is not a file!
                val NOT_FILE = Args1<Path> { translatable("krypton.region.chunk.external.not-file", text(it.toString())) }
            }
        }
    }

    object AuthMessages {

        // Failed to verify username {0}!
        val FAIL = singleText("auth.fail")

        // UUID of player {0} is {1}
        val SUCCESS = Args2<String, UUID> { name, uuid -> translatable("krypton.auth.success", text(name), text(uuid.toString())) }
    }

    object CommandMessages {

        // You do not have permission to execute this command!
        val NO_PERMISSION = empty("command.no-permission")

        // Only players can execute this command!
        val ONLY_PLAYERS = empty("command.only-players")
    }

    object CommandsMessages {

        // Failed to save debug dump!
        val DEBUG_SAVE_ERROR = empty("commands.debug-save-error")

        // Stopping server...
        val STOP = empty("commands.stop")

        // Attempting to restart the server...
        val RESTART = empty("commands.restart")
    }

    object BungeeMessages {

        // Please notify the server administrator that they are attempting to use BungeeCord IP forwarding without setting their forwarding mode to LEGACY in their configuration file.
        val NOTIFY = empty("bungee.notify")

        // Could not decode BungeeCord handshake data! Please report this to an administrator!
        val FAIL_DECODE = empty("bungee.fail-decode")

        // Error decoding BungeeCord handshake data! Please report this to Krypton!
        val FAIL_DECODE_ERROR = empty("bungee.fail-decode.error")

        // You are unable to directly connect to this server, as it has BungeeCord enabled in the configuration.
        val DIRECT = empty("bungee.direct")

        // Attempted connection from {0} not from BungeeCord when BungeeCord compatibility enabled.
        val DIRECT_WARN = singleText("bungee.direct.warn")
    }

    object VelocityMessages {

        // Invalid response from Velocity!
        val INVALID_RESPONSE = empty("velocity.invalid-response")
    }

    object NetworkMessages {

        val LOGIN = LoginMessages
        val COMPRESS = CompressMessages

        // {0} tried to change their held item slot to an invalid value!
        val INVALID_HELD_SLOT = singleText("network.invalid-held-slot")

        // Internal Exception:
        val HANDLER_ERROR_DISCONNECT = empty("network.handler-error-disconnect")

        object LoginMessages {

            // Verify tokens did not match!
            val FAIL_VERIFY = empty("network.login.fail-verify")

            // {0} failed verification! Expected {1}, received {2}!
            val FAIL_VERIFY_ERROR = tripleText("network.login.fail-verify.error")
        }

        object CompressMessages {

            // Packet badly compressed! Size of {0} is below threshold of {1}!
            val BELOW_THRESHOLD = Args2<Int, Int> { size, threshold ->
                translatable("krypton.network.compress.below-threshold", text(size), text(threshold))
            }

            // Packet badly compressed! Size of {0} is larger than protocol maximum of {1}!
            val STUPIDLY_LARGE = Args2<Int, Int> { size, maximum ->
                translatable("krypton.network.compress.stupidly-large", text(size), text(maximum))
            }
        }
    }

    object PluginMessages {

        val LOAD = LoadMessages

        val NOT_DIRECTORY = Args1<Path> { translatable("krypton.plugin.not-directory", text(it.toString())) }

        object LoadMessages {

            val ERROR = ErrorMessages

            // Loading plugins...
            val INITIAL = empty("plugin.load")

            // Plugin loading done! Loaded {0} plugins.
            val DONE = Args1<Int> { translatable("krypton.plugin.load.done", text(it)) }

            // Loaded plugin {0} {1} by {2}
            val SUCCESS = tripleText("plugin.load.success")

            // Couldn't load plugins!
            val FAIL = empty("plugin.load.fail")

            object ErrorMessages {

                // Unable to load plugin {0}!
                val UNABLE = Args1<Path> { translatable("krypton.plugin.load.error.unable", text(it.toString())) }

                // Can't load plugin {0} due to missing dependency {1}!
                val MISSING_DEPENDENCY = doubleText("plugin.load.error.missing-dependency")

                // Can't create module for plugin {0}!
                val CREATE_MODULE = singleText("plugin.load.error.create-module")

                // Can't create plugin {0}!
                val CREATE_PLUGIN = singleText("plugin.load.error.create-plugin")
            }
        }
    }

    object ProfilerMessages {

        val ERROR = ErrorMessages

        // push
        val PUSH = empty("profiler.push")

        // pop
        val POP = empty("profiler.pop")

        // Recorded long tick -- wrote info to {0}
        val TICK_RECORDED = Args1<Path> { translatable("krypton.profiler.tick-recorded", text(it.toString())) }

        object ErrorMessages {

            // Profiler tick has already been started! Perhaps we didn't call end?
            val STARTED = empty("profiler.error.started")

            // Profiler tick has already ended! Perhaps we didn't call start?
            val ENDED = empty("profiler.error.ended")

            // Profiler tick was ended before path was fully popped ({0} remaining)! Perhaps push and pop are mismatched?
            val NOT_FULLY_POPPED = singleText("profiler.error.not-fully-popped")

            // You need to start profiling before attempting to {0} data! Ignoring attempt to {0} {1}
            val NOT_STARTED = doubleText("profiler.error.not-started")

            // Tried to pop one too many times! Perhaps push and pop are mismatched?
            val TOO_MANY_POPS = empty("profiler.error.too-many-pops")

            // Something's taking too long! {0} took approximately {1} ms
            val TOO_LONG = Args2<String, Double> { name, time -> translatable("krypton.profiler.error", text(name), text(time)) }
        }
    }

    object ThreadMessages {

        // Thread {0} started
        val START = singleText("thread.start")

        // Waited {0} seconds, attempting to force stop.
        val STOP_FORCE = Args1<Int> { translatable("krypton.thread.stop-force", text(it)) }

        // Thread {0} ({1}) failed to exit after {2} second(s)
        val STOP_FORCE_ERROR = Args3<GenericThread, Thread.State, Int> { thread, state, seconds ->
            translatable("krypton.thread.stop-force.error", text(thread.toString()), text(state.name), text(seconds))
        }

        // Thread {0} stopped
        val STOPPED = singleText("thread.stopped")
    }
}

private fun empty(key: String) = Messages.Args0 { translatable("krypton.$key") }

private fun singleText(key: String) = Messages.Args1<String> { translatable("krypton.$key", text(it)) }

private fun doubleText(key: String) = Messages.Args2<String, String> { a, b -> translatable("krypton.$key", text(a), text(b)) }

private fun tripleText(key: String) = Messages.Args3<String, String, String> { a, b, c ->
    translatable("krypton.$key", text(a), text(b), text(c))
}
