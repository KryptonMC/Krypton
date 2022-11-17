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
package org.kryptonmc.krypton.locale

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.locale.Message.Args0
import org.kryptonmc.krypton.locale.Message.Args1
import org.kryptonmc.krypton.locale.Message.Args2
import org.kryptonmc.krypton.locale.Message.Args3
import org.kryptonmc.krypton.server.ban.KryptonBan
import java.time.OffsetDateTime

object Messages {

    @JvmField
    val GAME_MODE_CHANGED: Args1<GameMode> = Args1 { translatable("gameMode.changed", translatable(it.translationKey())) }
    @JvmField
    val CHAT_TYPE_EMOTE: Args2<Component, String> = Args2 { a, b -> translatable("chat.type.emote", a, text(b)) }
    @JvmField
    val CHAT_TYPE_ANNOUNCEMENT: Args2<Component, String> = Args2 { a, b -> translatable("chat.type.announcement", a, text(b)) }
    @JvmField
    val CHAT_TYPE_TEXT: Args2<KryptonPlayer, String> = Args2 { player, message ->
        val name = player.profile.name
        val displayName = player.displayName.style { it.insertion(name).clickEvent(ClickEvent.suggestCommand("/msg $name")).hoverEvent(player) }
        translatable("chat.type.text", displayName, text(message))
    }
    @JvmField
    val SQUARE_BRACKETS: Args1<Component> = Args1 { translatable("chat.square_brackets", it) }
    @JvmField
    val PREPARING_SPAWN: Args1<Int> = Args1 { translatable("menu.preparingSpawn", text(it)) }
    @JvmField
    val PLAYER_TEXT: Args1<Component> = Args1 { translatable("multiplayer.player.text", NamedTextColor.YELLOW, it) }
    @JvmField
    val PLAYER_JOINED: Args1<Component> = Args1 { translatable("multiplayer.player.joined", NamedTextColor.YELLOW, it) }
    @JvmField
    val PLAYER_JOINED_RENAMED: Args1<Component> = Args1 { translatable("multiplayer.player.joined.renamed", NamedTextColor.YELLOW, it) }

    object Commands {

        @JvmField
        val BAN_SUCCESS: Args2<String, String> = Args2 { a, b -> translatable("commands.ban.success", text(a), text(b)) }
        @JvmField
        val BAN_IP_SUCCESS: Args2<String, Component> = Args2 { a, b -> translatable("commands.banip.success", text(a), b) }

        @JvmField
        val CLEAR_SINGLE_SUCCESS: Args2<String, Component> = Args2 { a, b -> translatable("commands.clear.success.single", text(a), b) }
        @JvmField
        val CLEAR_MULTIPLE_SUCCESS: Args2<String, Int> =
            Args2 { a, b -> translatable("commands.clear.success.multiple", text(a), text(b.toString())) }

        @JvmField
        val DIFFICULTY_QUERY: Args1<Difficulty> = Args1 { translatable("commands.difficulty.query", translatable(it)) }
        @JvmField
        val DIFFICULTY_SUCCESS: Args1<Difficulty> = Args1 { translatable("commands.difficulty.success", translatable(it)) }

        @JvmField
        val GAME_MODE_SUCCESS: Args2<Component,  GameMode> = Args2 { a, b -> translatable("commands.gamemode.success.other", a, translatable(b)) }

        @JvmField
        val GAMERULE_QUERY: Args2<String, Any> = Args2 { a, b -> translatable("commands.gamerule.query", text(a), text(b.toString())) }
        @JvmField
        val GAMERULE_SET: Args2<String, Any> = Args2 { a, b -> translatable("commands.gamerule.set", text(a), text(b.toString())) }

        @JvmField
        val LIST_PLAYERS: Args3<Int, Int, String> = Args3 { a, b, c -> translatable("commands.list.players", text(a), text(b), text(c)) }

        @JvmField
        val OUTGOING_MESSAGE: Args2<Component, Component> = Args2 { a, b -> translatable("commands.message.display.outgoing", a, b) }
        @JvmField
        val INCOMING_MESSAGE: Args2<Component, Component> = Args2 { a, b -> translatable("commands.message.display.incoming", a, b) }

        @JvmField
        val PARDON_SUCCESS: Args1<String> = Args1 { translatable("commands.pardon.success", text(it)) }
        @JvmField
        val PARDON_IP_SUCCESS: Args1<String> = Args1 { translatable("commands.pardonip.success", text(it)) }

        @JvmField
        val SEED_SUCCESS: Args1<Long> = Args1 {
            val style = Style.style()
                .color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.copyToClipboard(it.toString()))
                .hoverEvent(HoverEvent.showText(translatable("chat.copy.click")))
                .insertion(it.toString())
                .build()
            translatable("commands.seed.success", SQUARE_BRACKETS.build(text(it.toString(), style)))
        }

        @JvmField
        val STOP_STOPPING: Args0 = Args0 { translatable("commands.stop.stopping") }

        @JvmField
        val SUMMON_SUCCESS: Args1<Component> = Args1 { translatable("commands.summon.success", it) }

        @JvmField
        val TELEPORT_SINGLE_ENTITY: Args2<Component, Component> = Args2 { a, b -> translatable("commands.teleport.success.entity.single", a, b) }
        @JvmField
        val TELEPORT_MULTIPLE_ENTITIES: Args2<Int, Component> =
            Args2 { a, b -> translatable("commands.teleport.success.entity.multiple", text(a.toString()), b) }
        @JvmField
        val TELEPORT_MULTIPLE_LOCATIONS: Args2<Int, Vec3d> = Args2 { a, b ->
            translatable("commands.teleport.success.location.multiple", text(a), text(b.floorX()), text(b.floorY()), text(b.floorZ()))
        }

        @JvmField
        val WHITELIST_NONE: Args0 = Args0 { translatable("commands.whitelist.none") }
        @JvmField
        val WHITELIST_LIST: Args1<Array<String>> = Args1 { translatable("commands.whitelist.list", text(it.size), text(it.joinToString(", "))) }
        @JvmField
        val WHITELIST_RELOADED: Args0 = Args0 { translatable("commands.whitelist.reloaded") }
        @JvmField
        val WHITELIST_ENABLED: Args0 = Args0 { translatable("commands.whitelist.enabled") }
        @JvmField
        val WHITELIST_DISABLED: Args0 = Args0 { translatable("commands.whitelist.disabled") }
        @JvmField
        val WHITELIST_ADD_SUCCESS: Args1<GameProfile> = Args1 { translatable("commands.whitelist.add.success", text(it.name)) }
        @JvmField
        val WHITELIST_REMOVE_SUCCESS: Args1<GameProfile> = Args1 { translatable("commands.whitelist.remove.success", text(it.name)) }
    }

    object Disconnect {

        private val BANNED_EXPIRATION = bannedExpiration("multiplayer.disconnect.banned.expiration")
        private val BANNED_IP_EXPIRATION = bannedExpiration("multiplayer.disconnect.banned_ip.expiration")
        @JvmField
        val BANNED_MESSAGE: Args2<Component, OffsetDateTime?> = bannedMessage("multiplayer.disconnect.banned.reason", BANNED_EXPIRATION)
        @JvmField
        val BANNED_IP_MESSAGE: Args2<Component, OffsetDateTime?> = bannedMessage("multiplayer.disconnect.banned_ip.reason", BANNED_IP_EXPIRATION)
        @JvmField
        val NOT_WHITELISTED: Args0 = Args0 { translatable("multiplayer.disconnect.not_whitelisted") }
        @JvmField
        val MISSING_PUBLIC_KEY: Args0 = Args0 { translatable("multiplayer.disconnect.missing_public_key") }
        @JvmField
        val INVALID_SIGNATURE: Args0 = Args0 { translatable("multiplayer.disconnect.invalid_public_key_signature") }
        @JvmField
        val INVALID_PUBLIC_KEY: Args0 = Args0 { translatable("multiplayer.disconnect.invalid_public_key") }
        @JvmField
        val KICKED: Args0 = Args0 { translatable("multiplayer.disconnect.kicked") }
        @JvmField
        val SERVER_FULL: Args0 = Args0 { translatable("multiplayer.disconnect.server_full") }
        @JvmField
        val UNVERIFIED_USERNAME: Args0 = Args0 { translatable("multiplayer.disconnect.unverified_username") }
        @JvmField
        val UNEXPECTED_QUERY_RESPONSE: Args0 = Args0 { translatable("multiplayer.disconnect.unexpected_query_response") }
        @JvmField
        val ILLEGAL_CHARACTERS: Args0 = Args0 { translatable("multiplayer.disconnect.illegal_characters") }
        @JvmField
        val OUT_OF_ORDER_CHAT: Args0 = Args0 { translatable("multiplayer.disconnect.out_of_order_chat") }
        @JvmField
        val REQUIRED_TEXTURE_PROMPT: Args0 = Args0 { translatable("multiplayer.requiredTexturePrompt.disconnect") }
        @JvmField
        val SERVER_SHUTDOWN: Args0 = Args0 { translatable("multiplayer.disconnect.server_shutdown") }
        @JvmField
        val OUTDATED_CLIENT: Args1<String> = Args1 { translatable("multiplayer.disconnect.outdated_client", text(it)) }
        @JvmField
        val OUTDATED_SERVER: Args1<String> = Args1 { translatable("multiplayer.disconnect.outdated_server", text(it)) }
        @JvmField
        val INCOMPATIBLE: Args1<String> = Args1 { translatable("multiplayer.disconnect.incompatible", text(it)) }

        @JvmField
        val LOGIN_FAILED_INFO: Args1<String> = Args1 { translatable("disconnect.loginFailedInfo", text(it)) }
        @JvmField
        val TIMEOUT: Args0 = Args0 { translatable("disconnect.timeout") }
        @JvmField
        val END_OF_STREAM: Args0 = Args0 { translatable("disconnect.endOfStream") }
        @JvmField
        val GENERIC_REASON: Args1<String> = Args1 { translatable("disconnect.genericReason", text(it)) }

        @JvmStatic
        private fun bannedMessage(reasonKey: String, expires: Args1<OffsetDateTime>): Args2<Component, OffsetDateTime?> = Args2 { reason, expiry ->
            translatable().key(reasonKey).args(reason).apply { if (expiry != null) it.append(expires.build(expiry)) }.build()
        }

        @JvmStatic
        private fun bannedExpiration(key: String): Args1<OffsetDateTime> = Args1 { translatable(key, text(KryptonBan.DATE_FORMATTER.format(it))) }
    }

    object Pack {

        @JvmField
        val NAME_AND_SOURCE: Args2<Component, Component> = Args2 { a, b -> translatable("pack.nameAndSource", NamedTextColor.GRAY, a, b) }
        @JvmField
        val SOURCE_BUILTIN: Args0 = Args0 { translatable("pack.source.builtin") }
        @JvmField
        val SOURCE_WORLD: Args0 = Args0 { translatable("pack.source.world") }
    }
}
