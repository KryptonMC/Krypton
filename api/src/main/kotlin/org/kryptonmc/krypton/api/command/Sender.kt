package org.kryptonmc.krypton.api.command

import net.kyori.adventure.audience.Audience

/**
 * A sender is an interface representing the sender of a command.
 */
interface Sender : Audience {

    val name: String

    fun hasPermission(permission: String): Boolean

    fun grant(permission: String)

    fun grant(vararg permissions: String)

    fun grant(permissions: Iterable<String>)

    fun revoke(permission: String)

    fun revoke(vararg permissions: String)

    fun revoke(permissions: Iterable<String>)
}