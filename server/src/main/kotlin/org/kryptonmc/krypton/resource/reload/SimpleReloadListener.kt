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
import org.kryptonmc.krypton.util.profiling.Profiler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

abstract class SimpleReloadListener<R> : ReloadListener {

    protected abstract fun prepare(manager: ResourceManager, profiler: Profiler): R

    protected abstract fun apply(resources: R, manager: ResourceManager, profiler: Profiler)

    override fun reload(
        barrier: ReloadListener.Barrier,
        manager: ResourceManager,
        preparationProfiler: Profiler,
        reloadProfiler: Profiler,
        executor: Executor,
        syncExecutor: Executor
    ): CompletableFuture<Void> = CompletableFuture.supplyAsync({ prepare(manager, preparationProfiler) }, executor)
        .thenCompose(barrier::wait)
        .thenAcceptAsync({ apply(it, manager, reloadProfiler) }, syncExecutor)
}
