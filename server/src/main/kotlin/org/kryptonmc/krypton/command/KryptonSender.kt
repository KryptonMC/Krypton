package org.kryptonmc.krypton.command

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.event.events.play.PermissionCheckEvent

abstract class KryptonSender(private val server: KryptonServer) : Sender {

    override val permissions = mutableMapOf<String, Boolean>()

    override fun hasPermission(permission: String): Boolean {
        val event = PermissionCheckEvent(this, permission, permission in permissions)
        server.eventBus.call(event)
        return event.result.value
    }

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