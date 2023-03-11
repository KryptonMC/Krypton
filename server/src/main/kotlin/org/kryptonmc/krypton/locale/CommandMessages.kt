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
package org.kryptonmc.krypton.locale

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.adventure.Components
import org.kryptonmc.krypton.command.CommandSourceStack

object CommandMessages {

    @JvmField
    val BAN: Args2<String, Component> = Args2 { a, b -> translatable("commands.ban.success", text(a), b) }
    @JvmField
    val BAN_IP: Args2<String, Component> = Args2 { a, b -> translatable("commands.banip.success", text(a), b) }

    @JvmField
    val CLEAR_SINGLE: Args2<String, Component> = Args2 { a, b -> translatable("commands.clear.success.single", text(a), b) }
    @JvmField
    val CLEAR_MULTIPLE: Args2<String, Int> = Args2 { a, b -> translatable("commands.clear.success.multiple", text(a), text(b)) }

    @JvmField
    val DIFFICULTY_QUERY: Args1<Difficulty> = Args1 { translatable("commands.difficulty.query", translatable(it)) }
    @JvmField
    val DIFFICULTY_SUCCESS: Args1<Difficulty> = Args1 { translatable("commands.difficulty.success", translatable(it)) }

    @JvmField
    val GAME_MODE_SELF: Args1<GameMode> = Args1 { translatable("commands.gamemode.success.self", translatable(it)) }
    @JvmField
    val GAME_MODE_OTHER: Args2<Component, GameMode> = Args2 { a, b -> translatable("commands.gamemode.success.other", a, translatable(b)) }

    @JvmField
    val GAME_RULE_QUERY: Args2<String, String> = Args2 { a, b -> translatable("commands.gamerule.query", text(a), text(b)) }
    @JvmField
    val GAME_RULE_SET: Args2<String, String> = Args2 { a, b -> translatable("commands.gamerule.set", text(a), text(b)) }

    @JvmField
    val LIST_PLAYERS: Args2<Int, List<String>> =
        Args2 { a, b -> translatable("commands.list.players", text(b.size), text(a), text(b.joinToString("\n"))) }

    @JvmField
    val PARDON: Args1<String> = Args1 { translatable("commands.pardon.success", text(it)) }
    @JvmField
    val PARDON_IP: Args1<String> = Args1 { translatable("commands.pardonip.success", text(it)) }

    @JvmField
    val SEED: Args1<String> = Args1 {
        val style = Style.style()
            .color(NamedTextColor.GREEN)
            .clickEvent(ClickEvent.copyToClipboard(it))
            .hoverEvent(HoverEvent.showText(translatable("chat.copy.click")))
            .insertion(it)
            .build()
        translatable("commands.seed.success", Components.wrapInSquareBrackets(text(it, style)))
    }

    @JvmField
    val STOP: Component = translatable("commands.stop.stopping")

    @JvmField
    val SUMMON: Args1<Component> = Args1 { translatable("commands.summon.success", it) }

    @JvmField
    val TELEPORT_SINGLE: Args2<Component, Component> = Args2 { a, b -> translatable("commands.teleport.success.entity.single", a, b) }
    @JvmField
    val TELEPORT_ENTITY_MULTIPLE: Args2<Int, Component> =
        Args2 { a, b -> translatable("commands.teleport.success.entity.multiple", text(a.toString()), b) }
    @JvmField
    val TELEPORT_LOCATION_MULTIPLE: Args2<Int, Position> = Args2 { a, b ->
        translatable("commands.teleport.success.location.multiple", text(a), text(b.blockX()), text(b.blockY()), text(b.blockZ()))
    }

    @JvmField
    val WHITELIST_RELOADED: Component = translatable("commands.whitelist.reloaded")
    @JvmField
    val WHITELIST_LIST: Args1<Array<String>> = Args1 { translatable("commands.whitelist.list", text(it.size), text(it.joinToString(", "))) }
    @JvmField
    val WHITELIST_ADD: Args1<GameProfile> = Args1 { translatable("commands.whitelist.add.success", text(it.name)) }
    @JvmField
    val WHITELIST_REMOVE: Args1<GameProfile> = Args1 { translatable("commands.whitelist.remove.success", text(it.name)) }
    @JvmField
    val WHITELIST_ENABLED: Component = translatable("commands.whitelist.enabled")
    @JvmField
    val WHITELIST_DISABLED: Component = translatable("commands.whitelist.disabled")
    @JvmField
    val WHITELIST_NONE: Component = translatable("commands.whitelist.none")

    fun interface Args1<A> {

        fun build(a: A): Component

        fun sendSuccess(source: CommandSourceStack, a: A, allowLogging: Boolean) {
            source.sendSuccess(build(a), allowLogging)
        }
    }

    fun interface Args2<A, B> {

        fun build(a: A, b: B): Component

        fun sendSuccess(source: CommandSourceStack, a: A, b: B, allowLogging: Boolean) {
            source.sendSuccess(build(a, b), allowLogging)
        }
    }
}
