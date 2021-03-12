package org.kryptonmc.krypton.api.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.reflect.KClass

/**
 * The event manager.
 *
 * This is responsible for emitting events to listeners
 *
 * @author Callum Seabrook
 */
interface EventManager {

    /**
     * Call, or "fire", an event
     *
     * This should internally call [MutableSharedFlow.tryEmit] and return
     * the result.
     *
     * @param T the type of the event
     * @param event the event to be fired
     */
    fun <T : Event> call(event: T): Boolean

    /**
     * Listens for an emitted event
     *
     * @param T the type of the event
     * @param class the class of the event
     */
    fun <T : Event> listen(`class`: KClass<T>): Flow<T>
}

inline fun <reified T : Event> EventManager.listen() = listen(T::class)