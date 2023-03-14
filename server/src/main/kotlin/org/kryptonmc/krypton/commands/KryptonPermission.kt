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
    BYPASS_SPAWN_PROTECTION("krypton.bypass-spawn-protection"),
    BROADCAST_ADMIN("krypton.broadcast_admin");
}
