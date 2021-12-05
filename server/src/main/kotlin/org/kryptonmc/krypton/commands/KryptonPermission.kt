/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.commands

/**
 * All of the built-in permissions used by Krypton's internals.
 */
enum class KryptonPermission(val node: String) {

    BAN("krypton.command.ban"),
    BAN_IP("krypton.command.banip"),
    CLEAR("krypton.command.clear"),
    DIFFICULTY("krypton.command.difficulty"),
    GAME_MODE("krypton.command.gamemode"),
    GAME_RULE("krypton.command.gamerule"),
    GIVE("krypton.command.give"),
    KICK("krypton.command.kick"),
    LIST("krypton.command.list"),
    ME("krypton.command.me"),
    MESSAGE("krypton.command.message"),
    PARDON("krypton.command.pardon"),
    PARDON_IP("krypton.command.pardonip"),
    RESTART("krypton.command.restart"),
    SAY("krypton.command.say"),
    SEED("krypton.command.seed"),
    STOP("krypton.command.stop"),
    SUMMON("krypton.command.summon"),
    TELEPORT("krypton.command.teleport"),
    TITLE("krypton.command.title"),
    WHITELIST("krypton.command.whitelist"),
    USE_GAME_MASTER_BLOCKS("krypton.feature.game-master-blocks"),
    ENTITY_QUERY("krypton.data.query.entity"),
    BYPASS_SPAWN_PROTECTION("krypton.bypass-spawn-protection");
}
