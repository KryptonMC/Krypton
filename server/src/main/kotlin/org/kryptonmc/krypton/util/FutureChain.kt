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
package org.kryptonmc.krypton.util

import org.apache.logging.log4j.LogManager
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException
import java.util.concurrent.Executor

class FutureChain(executor: Executor) : TaskChainer, AutoCloseable {

    private val checkedExecutor = Executor { if (!closed) executor.execute(it) }
    private var head: CompletableFuture<*> = CompletableFuture.completedFuture(null)
    @Volatile
    private var closed = false

    override fun append(task: TaskChainer.DelayedTask) {
        head = head.thenComposeAsync({ task.submit(checkedExecutor) }, checkedExecutor).exceptionally {
            var temp = it
            if (temp is CompletionException) temp = temp.cause!!
            if (temp is CancellationException) throw temp
            LOGGER.error("Chain link failed! Continuing to next one...", temp)
            null
        }
    }

    override fun close() {
        closed = true
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
