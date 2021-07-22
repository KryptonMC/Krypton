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

import org.kryptonmc.krypton.resource.ResourceManager
import org.kryptonmc.krypton.util.profiling.DeadProfiler
import org.kryptonmc.krypton.util.sequenceFailFast
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicInteger

@Suppress("UNCHECKED_CAST")
open class SimpleReloadInstance<S> protected constructor(
    private val executor: Executor,
    private val syncExecutor: Executor,
    private val resourceManager: ResourceManager,
    listeners: List<ReloadListener>,
    task: CompletableFuture<Unit>,
    private val factory: StateFactory<S>
) : ReloadInstance {

    protected val allPreparations = CompletableFuture<Unit>()
    protected val allDone: CompletableFuture<List<S>>
    private val listenerCount = listeners.size
    private val preparingListeners: MutableSet<ReloadListener>

    private var startedReloads = 0
    private var finishedReloads = 0
    private val startedTaskCounter = AtomicInteger()
    private val finishedTaskCounter = AtomicInteger()

    init {
        startedTaskCounter.incrementAndGet()
        task.thenRun(finishedTaskCounter::incrementAndGet)
        val futures = mutableListOf<CompletableFuture<S>>()
        var future = task
        preparingListeners = listeners.toMutableSet()

        listeners.forEach { listener ->
            val taskFinal = future
            val futureTask = factory.create(object : ReloadListener.Barrier {
                override fun <T> wait(value: T): CompletableFuture<T> {
                    syncExecutor.execute {
                        preparingListeners.remove(listener)
                        if (preparingListeners.isEmpty()) allPreparations.complete(Unit)
                    }
                    return allPreparations.thenCombine(taskFinal) { _, _ -> value }
                }
            }, resourceManager, listener, {
                startedTaskCounter.incrementAndGet()
                executor.execute {
                    it.run()
                    finishedTaskCounter.incrementAndGet()
                }
            }, {
                startedReloads++
                syncExecutor.execute {
                    it.run()
                    ++finishedReloads
                }
            })
            futures.add(futureTask)
            future = futureTask as CompletableFuture<Unit>
        }
        allDone = futures.sequenceFailFast()
    }

    override fun done(): CompletableFuture<Unit> = allDone.thenApply {}

    override fun checkExceptions() {
        if (allDone.isCompletedExceptionally) allDone.join()
    }

    override val progress: Float
        get() {
            val prepared = listenerCount - preparingListeners.size
            val finishedPercent = finishedTaskCounter.get().toFloat() * 2 + finishedReloads * 2 + prepared
            val startedPercent = startedTaskCounter.get().toFloat() * 2 + startedReloads * 2 + listenerCount
            return finishedPercent / startedPercent
        }

    override val isDone: Boolean
        get() = allDone.isDone

    protected fun interface StateFactory<S> {

        fun create(barrier: ReloadListener.Barrier, manager: ResourceManager, listener: ReloadListener, executor: Executor, syncExecutor: Executor): CompletableFuture<S>
    }

    companion object {

        fun of(
            manager: ResourceManager,
            listeners: List<ReloadListener>,
            executor: Executor,
            syncExecutor: Executor,
            task: CompletableFuture<Unit>
        ) = SimpleReloadInstance(executor, syncExecutor, manager, listeners, task) { barrier, manager1, listener, _, _ ->
            listener.reload(barrier, manager1, DeadProfiler, DeadProfiler, executor, syncExecutor)
        }
    }
}
