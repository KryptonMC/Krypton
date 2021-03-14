package org.kryptonmc.krypton.command

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import org.kryptonmc.krypton.concurrent.NamedThreadFactory
import java.util.concurrent.Executors

object CommandScope : CoroutineScope {

    override val coroutineContext = Executors.newFixedThreadPool(
        8,
        NamedThreadFactory("Command Handler #%d")
    ).asCoroutineDispatcher()
}