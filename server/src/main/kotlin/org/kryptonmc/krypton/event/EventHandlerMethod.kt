package org.kryptonmc.krypton.event

import java.lang.reflect.Method

/**
 * Represents a method of an event handler.
 *
 * @author Callum Seabrook
 */
class EventHandlerMethod(
    private val listener: Any,
    private val method: Method
) {

    operator fun invoke(event: Any) {
        method.invoke(listener, event)
    }
}