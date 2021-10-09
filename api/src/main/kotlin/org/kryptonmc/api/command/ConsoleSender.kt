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
 * The sender for the [org.kryptonmc.api.Server]. This is the sender that is
 * used by the console.
 *
 * This purely exists so it can be `is` checked if necessary, to determine
 * whether a given [Sender] is the console or not.
 */
public interface ConsoleSender : Sender
