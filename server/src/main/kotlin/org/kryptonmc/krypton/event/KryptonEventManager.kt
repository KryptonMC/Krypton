package org.kryptonmc.krypton.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import org.kryptonmc.krypton.api.event.Event
import org.kryptonmc.krypton.api.event.EventManager
import kotlin.reflect.KClass

class KryptonEventManager : EventManager {

    private val events = MutableSharedFlow<Event>()

    override fun <T : Event> call(event: T) = events.tryEmit(event)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Event> listen(`class`: KClass<T>): Flow<T> = events.filter { it::class == `class` } as Flow<T>
}