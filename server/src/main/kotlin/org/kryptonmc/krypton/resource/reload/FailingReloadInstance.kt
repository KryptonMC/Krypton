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

import org.kryptonmc.krypton.pack.PackResources
import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture

class FailingReloadInstance(private val exception: PackLoadException) : ReloadInstance {

    private val failedFuture = CompletableFuture<Unit>().apply { completeExceptionally(exception) }
    override val isDone = true
    override val progress = 0F

    override fun done() = failedFuture

    override fun checkExceptions() = throw exception
}

class PackLoadException(val pack: PackResources, cause: Throwable) : RuntimeException(pack.name, cause)
