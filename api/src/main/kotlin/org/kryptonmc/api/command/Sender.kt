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
     * The sender's name
     */
    val name: String

    /**
     * The sender's permissions
     */
    val permissions: Map<String, Boolean>

    val server: Server

    /**
     * If the sender has the specified [permission]
     *
     * @param permission the permission to check
     */
    fun hasPermission(permission: String): Boolean

    /**
     * Grant the sender the specified [permission]
     *
     * @param permission the permission to grant
     */
    fun grant(permission: String)

    /**
     * Grant the sender the specified [permissions]
     *
     * @param permissions the permission to grant
     */
    fun grant(vararg permissions: String) = permissions.forEach { grant(it) }

    /**
     * Grant the sender the specified [permissions]
     *
     * @param permissions the permissions to grant
     */
    fun grant(permissions: Iterable<String>) = permissions.forEach { grant(it) }

    /**
     * Revoke the specified [permission] from the sender
     *
     * @param permission the permission to revoke
     */
    fun revoke(permission: String)

    /**
     * Revoke the specified [permissions] from the sender
     *
     * @param permissions the permissions to revoke
     */
    fun revoke(vararg permissions: String) = permissions.forEach { revoke(it) }

    /**
     * Revoke the specified [permissions] from the sender
     *
     * @param permissions the permissions to revoke
     */
    fun revoke(permissions: Iterable<String>) = permissions.forEach { revoke(it) }
}
