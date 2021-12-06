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
package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.context.CommandContext
import net.kyori.adventure.key.Key
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.spongepowered.math.vector.Vector3d

fun CommandContext<Sender>.gameProfileArgument(name: String): EntityQuery = argument(name)

fun CommandContext<Sender>.summonableEntity(name: String): Key = SummonEntityArgument.ensureSummonable(argument(name))

fun CommandContext<Sender>.entityArgument(name: String): Key = SummonEntityArgument.ensureSummonable(argument(name))

fun CommandContext<Sender>.vectorArgument(name: String): Vector3d = argument<Coordinates>(name).position(source as Player)
