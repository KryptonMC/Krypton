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
package org.kryptonmc.krypton.util.thread

import com.mojang.datafixers.util.Either
import java.util.concurrent.CompletableFuture

interface Scheduler<T> : AutoCloseable {

    val name: String

    fun submit(task: T)

    fun <S> submit(producer: (Scheduler<S>) -> T): CompletableFuture<S> {
        val future = CompletableFuture<S>()
        val task = producer(create("Submit future scheduler", future::complete))
        submit(task)
        return future
    }

    fun <S> submitEither(producer: (Scheduler<Either<S, Exception>>) -> T): CompletableFuture<S> {
        val future = CompletableFuture<S>()
        val task = producer(create("Submit future scheduler") {
            it.ifLeft(future::complete)
            it.ifRight(future::completeExceptionally)
        })
        submit(task)
        return future
    }

    override fun close() = Unit

    companion object {

        fun <T> create(name: String, onSubmit: (T) -> Unit) = object : Scheduler<T> {
            override val name = name
            override fun submit(task: T) = onSubmit(task)
            override fun toString() = name
        }
    }
}
