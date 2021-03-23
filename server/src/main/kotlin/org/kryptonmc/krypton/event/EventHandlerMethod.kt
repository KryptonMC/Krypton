package org.kryptonmc.krypton.event

import java.lang.reflect.Method

class EventHandlerMethod(
    private val listener: Any,
    private val method: Method
) {

    operator fun invoke(event: Any) {
        method.invoke(listener, event)
    }
}