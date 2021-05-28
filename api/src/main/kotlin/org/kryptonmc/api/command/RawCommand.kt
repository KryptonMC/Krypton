/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

/**
 * A command that passes its arguments as a single string, without processing the input.
 *
 * This is useful for attaching external command frameworks to Krypton, and allowing them
 * to do their own processing.
 */
interface RawCommand : InvocableCommand<String> {

    /**
     * The primary name of this command
     */
    val name: String

    /**
     * A list of aliases that resolve to this command
     */
    val aliases: List<String>
}
