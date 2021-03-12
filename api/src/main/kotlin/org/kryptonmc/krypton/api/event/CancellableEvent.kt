package org.kryptonmc.krypton.api.event

/**
 * An [Event] that can be explicitly cancelled.
 *
 * @author Callum Seabrook
 */
abstract class CancellableEvent : Event {

    /**
     * Whether or not this [Event] has been cancelled
     */
    @Volatile
    var isCancelled = false
}