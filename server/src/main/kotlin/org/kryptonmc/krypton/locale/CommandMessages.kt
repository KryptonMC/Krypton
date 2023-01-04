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
    val TELEPORT_LOCATION_MULTIPLE: Args2<Int, Vec3d> = Args2 { a, b ->
        translatable("commands.teleport.success.location.multiple", text(a), text(b.floorX()), text(b.floorY()), text(b.floorZ()))
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
