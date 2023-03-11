/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.network.chat

import com.google.common.primitives.Ints
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.util.crypto.SignatureUpdater
import org.kryptonmc.krypton.util.crypto.SignatureValidator
import org.kryptonmc.krypton.util.uuid.UUIDUtil
import java.time.Duration
import java.time.Instant
import java.util.UUID

@JvmRecord
data class PlayerChatMessage(
    val link: SignedMessageLink,
    val signature: MessageSignature?,
    val signedBody: SignedMessageBody,
    val unsignedContent: Component?,
    val filterMask: FilterMask
) {

    fun withUnsignedContent(unsigned: Component): PlayerChatMessage {
        val newContent = if (unsigned != Component.text(signedContent())) unsigned else null
        return PlayerChatMessage(link, signature, signedBody, newContent, filterMask)
    }

    fun removeUnsignedContent(): PlayerChatMessage {
        if (unsignedContent == null) return this
        return PlayerChatMessage(link, signature, signedBody, null, filterMask)
    }

    fun filter(mask: FilterMask): PlayerChatMessage {
        if (filterMask == mask) return this
        return PlayerChatMessage(link, signature, signedBody, unsignedContent, mask)
    }

    fun filter(state: Boolean): PlayerChatMessage = filter(if (state) filterMask else FilterMask.PASS_THROUGH)

    fun verify(validator: SignatureValidator): Boolean = signature != null && signature.verify(validator) { updateSignature(it, link, signedBody) }

    private fun signedContent(): String = signedBody.content

    fun decoratedContent(): Component = unsignedContent ?: Component.text(signedContent())

    fun timestamp(): Instant = signedBody.timestamp

    fun salt(): Long = signedBody.salt

    fun hasExpired(time: Instant): Boolean = time.isAfter(timestamp().plus(MESSAGE_EXPIRES_AFTER))

    fun sender(): UUID = link.sender

    fun isSystem(): Boolean = sender() == SYSTEM_SENDER

    fun hasSignature(): Boolean = signature != null

    fun hasSignatureFrom(id: UUID): Boolean = hasSignature() && link.sender == id

    fun isFullyFiltered(): Boolean = filterMask.isFullyFiltered()

    companion object {

        private val SYSTEM_SENDER = UUIDUtil.NIL_UUID
        private val MESSAGE_EXPIRES_AFTER = Duration.ofMinutes(5L)

        @JvmStatic
        fun system(content: String): PlayerChatMessage = unsigned(SYSTEM_SENDER, content)

        @JvmStatic
        fun unsigned(sender: UUID, content: String): PlayerChatMessage =
            PlayerChatMessage(SignedMessageLink.unsigned(sender), null, SignedMessageBody.unsigned(content), null, FilterMask.PASS_THROUGH)

        @JvmStatic
        fun updateSignature(output: SignatureUpdater.Output, link: SignedMessageLink, body: SignedMessageBody) {
            output.update(Ints.toByteArray(1))
            link.updateSignature(output)
            body.updateSignature(output)
        }
    }
}
