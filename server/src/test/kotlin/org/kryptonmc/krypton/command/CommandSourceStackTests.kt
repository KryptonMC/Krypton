/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.command

import com.mojang.brigadier.exceptions.CommandSyntaxException
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.util.TriState
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.testutil.Bootstrapping
import org.kryptonmc.krypton.testutil.TestCommandSender
import org.kryptonmc.krypton.util.Vec3dImpl
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CommandSourceStackTests {

    @Test
    fun `ensure to entity throws when entity is null`() {
        val source = createDefaultSource(null)
        assertThrows<CommandSyntaxException> { source.getEntityOrError() }
    }

    @Test
    fun `test player conversions when entity is not player`() {
        val source = createDefaultSource(mockk())
        assertFalse(source.isPlayer())
        assertNull(source.getPlayer())
        assertThrows<CommandSyntaxException> { source.getPlayerOrError() }
    }

    @Test
    fun `test player conversions with player entity`() {
        val player = mockk<KryptonPlayer>()
        val source = createDefaultSource(player)
        assertTrue(source.isPlayer())
        assertEquals(player, source.getPlayer())
        assertEquals(player, source.getPlayerOrError())
    }

    @Test
    fun `test permission checks delegate properly`() {
        val permissions = PermissionFunction {
            when (it) {
                KryptonPermission.GAME_MODE.node -> TriState.TRUE
                KryptonPermission.BROADCAST_ADMIN.node -> TriState.FALSE
                else -> TriState.NOT_SET
            }
        }
        val source = createSource(createSender(true, true, true, permissions), null)
        assertTrue(source.hasPermission(KryptonPermission.GAME_MODE))
        assertFalse(source.hasPermission(KryptonPermission.BROADCAST_ADMIN))
        assertFalse(source.hasPermission(KryptonPermission.BAN))
    }

    @Test
    fun `test system messages send to sender if player is null`() {
        val sender = createDefaultSender()
        val source = createSource(sender, null)
        val message = Component.text("Hello World!")
        source.sendSystemMessage(message)
        assertEquals(message, sender.pollMessage())
    }

    @Test
    fun `test system messages send to player if not null`() {
        var sentMessage: Component? = null
        val player = mockk<KryptonPlayer> {
            every { sendSystemMessage(any()) } answers { sentMessage = arg(0) }
        }
        val source = createDefaultSource(player)
        val message = Component.text("Hello World!")
        source.sendSystemMessage(message)
        assertEquals(sentMessage, message)
    }

    @Test
    fun `test success sent to sender if accepts`() {
        val sender = createSender(true, false, false, PermissionFunction.ALWAYS_TRUE)
        val source = createSource(sender, null)
        val message = Component.text("Hello World!")
        source.sendSuccess(message, false)
        assertEquals(message, sender.pollMessage())
    }

    @Test
    fun `test success not sent to sender if not accepts`() {
        val sender = createSender(false, false, false, PermissionFunction.ALWAYS_TRUE)
        val source = createSource(sender, null)
        val message = Component.text("Hello World!")
        source.sendSuccess(message, false)
        sender.assertNoMessages()
    }

    @Test
    fun `test success sent to admins if logging and inform`() {
        val source = createSendSuccessToAdminsSource(true)
        val message = Component.text("Hello World!")
        source.sendSuccess(message, true)
        verify { source.broadcastToAdmins(any()) }
    }

    @Test
    fun `test success not sent to admins if not logging or inform`() {
        val message = Component.text("Hello World!")

        val informSource = createSendSuccessToAdminsSource(true)
        informSource.sendSuccess(message, false)
        verify(inverse = true) { informSource.broadcastToAdmins(any()) }

        val noInformSource = createSendSuccessToAdminsSource(false)
        noInformSource.sendSuccess(message, true)
        noInformSource.sendSuccess(message, false)
        verify(inverse = true) { noInformSource.broadcastToAdmins(any()) }
    }

    private fun createSendSuccessToAdminsSource(inform: Boolean): CommandSourceStack {
        return spyk(createSource(createSender(false, false, inform, PermissionFunction.ALWAYS_TRUE), null)) {
            every { broadcastToAdmins(any()) } just runs
        }
    }

    @Test
    fun `test failure sent to sender if accepts`() {
        val sender = createSender(false, true, false, PermissionFunction.ALWAYS_TRUE)
        val source = createSource(sender, null)
        val message = Component.text("Hello World!")
        source.sendFailure(message)
        assertEquals(Component.text().color(NamedTextColor.RED).append(message).build(), sender.pollMessage())
    }

    @Test
    fun `test failure not sent to sender if not accepts`() {
        val sender = createSender(false, false, false, PermissionFunction.ALWAYS_TRUE)
        val source = createSource(sender, null)
        val message = Component.text("Hello World!")
        source.sendFailure(message)
        sender.assertNoMessages()
    }

    companion object {

        private val SERVER = mockk<KryptonServer>()

        @JvmStatic
        @BeforeAll
        fun `load translations and factories for vec3d`() {
            Bootstrapping.loadTranslations()
            Bootstrapping.loadFactories()
        }

        @JvmStatic
        private fun createSender(success: Boolean, failure: Boolean, inform: Boolean, permissions: PermissionFunction): TestCommandSender =
            TestCommandSender(Component.empty(), SERVER, permissions, success, failure, inform)

        @JvmStatic
        private fun createDefaultSender(): TestCommandSender = createSender(true, true, true, PermissionFunction.ALWAYS_TRUE)

        @JvmStatic
        private fun createSource(sender: TestCommandSender, entity: KryptonEntity?): CommandSourceStack =
            CommandSourceStack(sender, Vec3dImpl.ZERO, 0F, 0F, mockk(), "", Component.empty(), SERVER, entity)

        @JvmStatic
        private fun createDefaultSource(entity: KryptonEntity?): CommandSourceStack = createSource(createDefaultSender(), entity)
    }
}
