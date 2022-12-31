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
package org.kryptonmc.krypton.pack.resources.reload

import org.kryptonmc.krypton.pack.resources.ResourceManager
import org.kryptonmc.krypton.util.Futures
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicInteger

class SimpleReloadInstance<S>(backgroundExecutor: Executor, mainExecutor: Executor, resourceManager: ResourceManager,
                              preparingListeners: List<ReloadListener>, stateFactory: StateFactory<S>,
                              waitingFor: CompletableFuture<Unit>) : ReloadInstance {

    private val allPreparations = CompletableFuture<Unit>()
    private var allDone: CompletableFuture<List<S>>
    private val preparingListeners = HashSet(preparingListeners)
    private val listenerCount = preparingListeners.size
    private var startedReloads = 0
    private var finishedReloads = 0
    private val startedTaskCounter = AtomicInteger()
    private val doneTaskCounter = AtomicInteger()

    init {
        waitingFor.thenRun { doneTaskCounter.incrementAndGet() }
        val done = mutableListOf<CompletableFuture<S>>()
        var currentFuture: CompletableFuture<*> = waitingFor
        preparingListeners.forEach { listener ->
            val finalFuture = currentFuture
            val future = stateFactory.create(object : ReloadListener.PreparationBarrier {

                override fun <T> wait(backgroundResult: T): CompletableFuture<T> {
                    mainExecutor.execute {
                        this@SimpleReloadInstance.preparingListeners.remove(listener)
                        if (this@SimpleReloadInstance.preparingListeners.isEmpty()) allPreparations.complete(Unit)
                    }
                    return allPreparations.thenCombine(finalFuture) { _, _ -> backgroundResult }
                }
            }, resourceManager, listener, {
                startedTaskCounter.incrementAndGet()
                backgroundExecutor.execute {
                    it.run()
                    doneTaskCounter.incrementAndGet()
                }
            }, {
                ++startedReloads
                mainExecutor.execute {
                    it.run()
                    ++finishedReloads
                }
            })
            done.add(future)
            currentFuture = future
        }
        allDone = Futures.sequenceFailFast(done)
    }

    override fun actualProgress(): Float {
        val removedListenerCount = listenerCount - preparingListeners.size
        val doneProgress = (doneTaskCounter.get() * 2 + finishedReloads * 2 + removedListenerCount * 1).toFloat()
        val startedProgress = (startedTaskCounter.get() * 2 + startedReloads * 2 + listenerCount * 1).toFloat()
        return doneProgress / startedProgress
    }

    override fun done(): CompletableFuture<*> = allDone

    fun interface StateFactory<S> {

        fun create(barrier: ReloadListener.PreparationBarrier, manager: ResourceManager, listener: ReloadListener, backgroundExecutor: Executor,
                   mainExecutor: Executor): CompletableFuture<S>
    }

    companion object {

        @JvmStatic
        fun of(resourceManager: ResourceManager, listeners: List<ReloadListener>, backgroundExecutor: Executor, mainExecutor: Executor,
               waitingFor: CompletableFuture<Unit>): SimpleReloadInstance<Void> {
            val factory = StateFactory { barrier, manager, listener, background, main -> listener.reload(barrier, manager, background, main) }
            return SimpleReloadInstance(backgroundExecutor, mainExecutor, resourceManager, listeners, factory, waitingFor)
        }
    }
}
