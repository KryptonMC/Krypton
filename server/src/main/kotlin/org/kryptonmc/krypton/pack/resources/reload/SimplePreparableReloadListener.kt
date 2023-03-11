/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
