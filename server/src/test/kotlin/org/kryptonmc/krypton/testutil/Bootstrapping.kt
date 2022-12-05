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
