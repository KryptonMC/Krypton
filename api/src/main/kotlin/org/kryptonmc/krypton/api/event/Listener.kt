package org.kryptonmc.krypton.api.event

/**
 * This is used to indicate that the target function is a listener
 * for an event.
 *
 * @param priority the priority of the event (defaults to [ListenerPriority.MEDIUM])
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Listener(val priority: Byte = ListenerPriority.MEDIUM)
