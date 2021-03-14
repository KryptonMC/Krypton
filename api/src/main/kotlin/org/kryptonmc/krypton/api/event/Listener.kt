package org.kryptonmc.krypton.api.event

/**
 * This is used to indicate that the target function is a listener
 * for an event.
 *
 * @param priority the priority of the event (defaults to [ListenerPriority.MEDIUM])
 * @author Callum Seabrook
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Listener(val priority: Int = ListenerPriority.MEDIUM)