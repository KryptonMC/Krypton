/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.identity.Identified
import org.kryptonmc.api.Server

/**
 * A sender is an interface representing the sender of a command.
 */
interface Sender : Audience, Identified {

    /**
     * The name of the sender.
     */
    val name: String

    /**
     * The permission map for the sender.
     */
    val permissions: Map<String, Boolean>

    /**
     * The sender's permission level (the equivalent to Minecraft's operator level).
     */
    val permissionLevel: Int

    /**
     * The sender's server
     */
    val server: Server

    /**
     * Returns true if the sender has the specified [permission], false otherwise.
     *
     * @param permission the permission to check
     * @return true if the sender has the permission, false otherwise
     */
    fun hasPermission(permission: String): Boolean

    /**
     * Grants the sender the specified [permission].
     *
     * @param permission the permission to grant
     */
    fun grant(permission: String)

    /**
     * Grants the sender the specified [permissions].
     *
     * @param permissions the permission to grant
     */
    fun grant(vararg permissions: String) = permissions.forEach { grant(it) }

    /**
     * Grants the sender the specified [permissions].
     *
     * @param permissions the permissions to grant
     */
    fun grant(permissions: Iterable<String>) = permissions.forEach { grant(it) }

    /**
     * Revokes the specified [permission] from the sender.
     *
     * @param permission the permission to revoke
     */
    fun revoke(permission: String)

    /**
     * Revokes the specified [permissions] from the sender.
     *
     * @param permissions the permissions to revoke
     */
    fun revoke(vararg permissions: String) = permissions.forEach { revoke(it) }

    /**
     * Revokes the specified [permissions] from the sender.
     *
     * @param permissions the permissions to revoke
     */
    fun revoke(permissions: Iterable<String>) = permissions.forEach { revoke(it) }
}
