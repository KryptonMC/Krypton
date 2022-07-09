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
package org.kryptonmc.krypton.network.chat

import org.kryptonmc.krypton.util.serialization.CompoundEncoder
import org.kryptonmc.krypton.util.serialization.encode
import org.kryptonmc.nbt.compound

@JvmRecord
data class ChatType(val chat: TextDisplay?, val overlay: TextDisplay?, val narration: Narration?) {

    @JvmRecord
    data class TextDisplay(val decoration: ChatDecoration?) {

        companion object {

            @JvmField
            val ENCODER: CompoundEncoder<TextDisplay> = CompoundEncoder {
                compound {
                    encode(ChatDecoration.ENCODER, "decoration", it.decoration)
                }
            }
        }
    }

    @JvmRecord
    data class Narration(val decoration: ChatDecoration?, val priority: Priority) {

        enum class Priority {

            CHAT,
            SYSTEM
        }

        companion object {

            @JvmField
            val ENCODER: CompoundEncoder<Narration> = CompoundEncoder {
                compound {
                    encode(ChatDecoration.ENCODER, "decoration", it.decoration)
                    string("priority", it.priority.name.lowercase())
                }
            }
        }
    }

    companion object {

        @JvmField
        val ENCODER: CompoundEncoder<ChatType> = CompoundEncoder {
            compound {
                encode(TextDisplay.ENCODER, "chat", it.chat)
                encode(TextDisplay.ENCODER, "overlay", it.overlay)
                encode(Narration.ENCODER, "narration", it.narration)
            }
        }
    }
}
