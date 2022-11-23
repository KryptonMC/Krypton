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
package org.kryptonmc.krypton.util.executor

abstract class ReentrantBlockableEventLoop<R : Runnable>(name: String) : BlockableEventLoop<R>(name) {

    private var reentrantCount = 0

    override fun scheduleExecutables(): Boolean = runningTask() || super.scheduleExecutables()

    protected fun runningTask(): Boolean = reentrantCount != 0

    override fun doRunTask(task: R) {
        ++reentrantCount
        try {
            super.doRunTask(task)
        } finally {
            --reentrantCount
        }
    }
}
