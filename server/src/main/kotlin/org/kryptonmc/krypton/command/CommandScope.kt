package org.kryptonmc.krypton.command

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

object CommandScope : CoroutineScope {

    override val coroutineContext = Executors.newFixedThreadPool(8).asCoroutineDispatcher()
}