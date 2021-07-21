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
package org.kryptonmc.krypton.resource.reload

import com.google.common.base.Stopwatch
import org.kryptonmc.krypton.resource.ResourceManager
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.profiling.LiveProfiler
import org.kryptonmc.krypton.util.profiling.results.ProfileResults
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class ProfiledReloadInstance(
    resourceManager: ResourceManager,
    listeners: List<ReloadListener>,
    executor: Executor,
    syncExecutor: Executor,
    task: CompletableFuture<Unit>
) : SimpleReloadInstance<ProfiledReloadInstance.State>(executor, syncExecutor, resourceManager, listeners, task, { barrier, manager, listener, exec, syncExec ->
    val preparationNanos = AtomicLong()
    val reloadNanos = AtomicLong()
    val preparationProfiler = LiveProfiler({ 0 }, false)
    val reloadProfiler = LiveProfiler({ 0 }, false)
    val reload = listener.reload(barrier, manager, preparationProfiler, reloadProfiler, {
        exec.execute {
            val startTime = System.nanoTime()
            it.run()
            preparationNanos.addAndGet(System.nanoTime() - startTime)
        }
    }, {
        syncExec.execute {
            val startTime = System.nanoTime()
            it.run()
            reloadNanos.addAndGet(System.nanoTime() - startTime)
        }
    })
    reload.thenApplyAsync({ State(listener.name, preparationProfiler.results, reloadProfiler.results, preparationNanos, reloadNanos) }, syncExecutor)
}) {

    private val total = Stopwatch.createUnstarted()

    init {
        total.start()
        allDone.thenAcceptAsync(::finish, syncExecutor)
    }

    private fun finish(states: List<State>) {
        total.stop()
        var totalBlockingTime = 0
        LOGGER.info("Resources reloaded in ${total.elapsed(TimeUnit.MILLISECONDS)}ms!")
        states.forEach { (name, _, _, preparationNanos, reloadNanos) ->
            val preparationMillis = (preparationNanos.get().toDouble() / 1000000.0).toInt()
            val reloadMillis = (reloadNanos.get().toDouble() / 1000000.0).toInt()
            val total = preparationMillis + reloadMillis
            LOGGER.info("$name took approximately $total ms ($preparationMillis ms preparing, $reloadMillis ms applying)")
            totalBlockingTime += reloadMillis
        }
        LOGGER.info("Total blocking time: $totalBlockingTime ms")
    }

    data class State(
        val name: String,
        val preparationResult: ProfileResults,
        val reloadResult: ProfileResults,
        val preparationNanos: AtomicLong,
        val reloadNanos: AtomicLong
    )

    companion object {

        private val LOGGER = logger<ProfiledReloadInstance>()
    }
}
