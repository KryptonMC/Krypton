package org.kryptonmc.krypton.api.command

import net.kyori.adventure.audience.Audience

/**
 * A sender is an interface representing the sender of a command.
 */
interface Sender : Audience {

    /**
     * The sender's name
     */
    val name: String

    /**
     * The sender's permissions
     */
    val permissions: Map<String, Boolean>

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
    fun grant(vararg permissions: String)

    /**
     * Grant the sender the specified [permissions]
     *
     * @param permissions the permissions to grant
     */
    fun grant(permissions: Iterable<String>)

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
    fun revoke(vararg permissions: String)

    /**
     * Revoke the specified [permissions] from the sender
     *
     * @param permissions the permissions to revoke
     */
    fun revoke(permissions: Iterable<String>)
}