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
import org.kryptonmc.krypton.pack.resources.reload.PreparableReloadListener.PreparationBarrier
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

abstract class SimplePreparableReloadListener<T> : PreparableReloadListener {

    protected abstract fun prepare(manager: ResourceManager): T

    protected abstract fun apply(prepared: T, manager: ResourceManager)

    override fun reload(barrier: PreparationBarrier, manager: ResourceManager, backgroundExecutor: Executor,
                        mainExecutor: Executor): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync({ prepare(manager) }, backgroundExecutor)
            .thenCompose { barrier.wait(it) }
            .thenAcceptAsync({ apply(it, manager) }, mainExecutor)
    }
}
