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
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/InvalidPluginException.java
 */
package org.kryptonmc.api.plugin

/**
 * Thrown when a plugin is invalid in some way.
 */
public class InvalidPluginException : Exception {

    public constructor() : super()

    public constructor(message: String) : super(message)

    public constructor(cause: Throwable) : super(cause)

    public constructor(message: String, cause: Throwable) : super(message, cause)
}
