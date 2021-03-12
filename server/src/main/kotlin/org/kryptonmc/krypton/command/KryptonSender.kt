package org.kryptonmc.krypton.command

import org.kryptonmc.krypton.api.command.Sender

abstract class KryptonSender : Sender {

    private val permissions = mutableListOf<String>()

    override fun hasPermission(permission: String) = permission in permissions

    override fun grant(permission: String) {
        permissions += permission
    }

    override fun grant(vararg permissions: String) = permissions.forEach { grant(it) }

    override fun grant(permissions: Iterable<String>) = permissions.forEach { grant(it) }

    override fun revoke(permission: String) {
        permissions -= permission
    }

    override fun revoke(vararg permissions: String) = permissions.forEach { revoke(it) }

    override fun revoke(permissions: Iterable<String>) = permissions.forEach { revoke(it) }
}