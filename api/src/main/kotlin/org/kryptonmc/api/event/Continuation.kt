/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/1761755d4dfc16cd020aee90c48761d98552531b/api/src/main/java/com/velocitypowered/api/event/Continuation.java
 */
package org.kryptonmc.api.event

/**
 * A continuation of a paused event handler. This can be used to resume event
 * handling after a pause.
 *
 * Note: The resume functions can only be used once, else an
 * [IllegalStateException] will be thrown.
 */
public interface Continuation {

    /**
     * Resumes the continuation, allowing execution to resume from where it
     * was suspended.
     *
     * @throws IllegalStateException if the continuation has already been
     * resumed
     */
    public fun resume()

    /**
     * Resumes the continuation, allowing execution to resume from where it
     * was suspended, with the given [exception] as the reason for the
     * executed task failing.
     *
     * @throws IllegalStateException if the continuation has already been
     * resumed
     */
    public fun resumeWithException(exception: Throwable)
}
