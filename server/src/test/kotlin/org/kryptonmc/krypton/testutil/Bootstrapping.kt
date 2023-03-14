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
package org.kryptonmc.krypton.testutil

import org.kryptonmc.krypton.server.Bootstrap
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
