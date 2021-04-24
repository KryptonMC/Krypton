package org.kryptonmc.krypton.api.event

/**
 * An [Event] that can be explicitly cancelled.
 */
abstract class CancellableEvent : Event {

    /**
     * Whether or not this [Event] has been cancelled
     */
    @Volatile var isCancelled = false

    /**
     * Cancel this [Event]
     */
    fun cancel() {
        isCancelled = true
    }
}
