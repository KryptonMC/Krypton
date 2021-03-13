package org.kryptonmc.krypton.console

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import java.util.concurrent.Executors

object ConsoleScope : CoroutineScope {

    override val coroutineContext = Executors.newSingleThreadExecutor {
        Thread(it, "Console Handler").apply { isDaemon = true }
    }.asCoroutineDispatcher()
}