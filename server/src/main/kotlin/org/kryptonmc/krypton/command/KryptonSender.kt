package org.kryptonmc.krypton.command

import org.kryptonmc.krypton.api.command.Sender

abstract class KryptonSender : Sender {

    override val permissions = mutableMapOf<String, Boolean>()

    override fun hasPermission(permission: String) = permission in permissions && permissions.getValue(permission)

    override fun grant(permission: String) {
        permissions[permission] = true
    }

    override fun grant(vararg permissions: String) = permissions.forEach { grant(it) }

    override fun grant(permissions: Iterable<String>) = permissions.forEach { grant(it) }

    override fun revoke(permission: String) {
        permissions[permission] = false
    }

    override fun revoke(vararg permissions: String) = permissions.forEach { revoke(it) }

    override fun revoke(permissions: Iterable<String>) = permissions.forEach { revoke(it) }
}