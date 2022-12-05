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
package org.kryptonmc.krypton.testutil

import org.kryptonmc.krypton.util.Bootstrap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This class is used to separate bootstrapping in to different parts.
 *
 * It also fixes inconsistencies between running individual tests in the IDE and running all the
 * tests under one JVM through Gradle, where the latter would sometimes fail to load factories as
 * they were already loaded by another test, which wouldn't be the case if tests were ran in
 * individual virtual machines, like in the IDE.
 */
object Bootstrapping {

    private val translations = AtomicBoolean()
    private val factories = AtomicBoolean()
    private val registries = AtomicBoolean()
    private val classes = AtomicBoolean()

    @JvmStatic
    fun loadTranslations() {
        if (!translations.compareAndSet(false, true)) return
        Bootstrap.preloadTranslations()
    }

    @JvmStatic
    fun loadFactories() {
        if (!factories.compareAndSet(false, true)) return
        Bootstrap.preloadFactories()
    }

    @JvmStatic
    fun loadRegistries() {
        if (!registries.compareAndSet(false, true)) return
        Bootstrap.preloadRegistries()
    }

    @JvmStatic
    fun loadOtherClasses() {
        if (!classes.compareAndSet(false, true)) return
        Bootstrap.preloadOtherClasses()
    }
}
