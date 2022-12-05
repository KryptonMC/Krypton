package org.kryptonmc.krypton.testutil

import net.kyori.adventure.text.Component
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.Server
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.KryptonSender
import java.util.ArrayDeque
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TestCommandSender(
    override val name: Component,
    override val server: Server,
    private val permissionFunction: PermissionFunction,
    private val acceptSuccess: Boolean,
    private val acceptFailure: Boolean,
    private val informAdmins: Boolean
) : KryptonSender {

    private val sentMessages = ArrayDeque<Component>()

    override fun acceptsSuccess(): Boolean = acceptSuccess

    override fun acceptsFailure(): Boolean = acceptFailure

    override fun shouldInformAdmins(): Boolean = informAdmins

    override fun sendSystemMessage(message: Component) {
        sentMessages.add(message)
    }

    fun pollMessage(): Component = assertNotNull(sentMessages.poll(), "No messages were sent!")

    fun assertNoMessages() {
        assertTrue(sentMessages.isEmpty(), "Expected no messages to be sent, but ${sentMessages.size} were!")
    }

    override fun createCommandSourceStack(): CommandSourceStack {
        throw UnsupportedOperationException("Cannot create a command source stack for a test command sender!")
    }

    override fun getPermissionValue(permission: String): TriState = permissionFunction.getPermissionValue(permission)
}
