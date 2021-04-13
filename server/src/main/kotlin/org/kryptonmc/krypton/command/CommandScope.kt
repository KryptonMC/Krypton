package org.kryptonmc.krypton.command

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import org.kryptonmc.krypton.concurrent.NamedThreadFactory
import java.util.concurrent.Executors

/**
 * A custom coroutine scope for executing commands on.
 *
 * Subject to change if commands are no longer asynchronous by default
 *
 * @author Callum Seabrook
 */
object CommandScope : CoroutineScope {

    override val coroutineContext = Executors.newFixedThreadPool(
        2,
        NamedThreadFactory("Command Handler #%d")
    ).asCoroutineDispatcher()
}