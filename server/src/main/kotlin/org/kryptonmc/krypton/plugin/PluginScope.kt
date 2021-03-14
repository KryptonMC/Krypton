package org.kryptonmc.krypton.plugin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import org.kryptonmc.krypton.concurrent.NamedThreadFactory
import java.util.concurrent.Executors

object PluginScope : CoroutineScope {

    override val coroutineContext = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors() * 2,
        NamedThreadFactory("Plugin Handler #%d")
    ).asCoroutineDispatcher()
}