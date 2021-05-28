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
 * A command styled after the convention popularised by Bukkit and BungeeCord.
 *
 * It is highly recommended to use the new Brigadier command system. See [BrigadierCommand].
 *
 * @param name the primary name of the command
 * @param permission the permission required to execute the command (`null` for no permission)
 * @param aliases other aliases that will resolve to this command
 */
abstract class SimpleCommand @JvmOverloads constructor(
    val name: String,
    val permission: String? = null,
    val aliases: List<String> = emptyList()
) : InvocableCommand<Array<String>> {

    /**
     * Construct a legacy command using a vararg instead of a list.
     *
     * @param name the primary name of the command
     * @param permission the permission required to execute the command (`null` for no permission)
     * @param aliases other aliases that will resolve to this command
     */
    constructor(name: String, permission: String? = null, vararg aliases: String) : this(name, permission, aliases.toList())
}
