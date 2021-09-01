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
package org.kryptonmc.krypton.util.converter.versions

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V1344 {

    private const val VERSION = MCVersions.V1_12_2 + 1
    private val BUTTON_ID_TO_NAME = Int2ObjectOpenHashMap<String>().apply {
        put(0, "key.unknown")
        put(11, "key.0")
        put(2, "key.1")
        put(3, "key.2")
        put(4, "key.3")
        put(5, "key.4")
        put(6, "key.5")
        put(7, "key.6")
        put(8, "key.7")
        put(9, "key.8")
        put(10, "key.9")
        put(30, "key.a")
        put(40, "key.apostrophe")
        put(48, "key.b")
        put(43, "key.backslash")
        put(14, "key.backspace")
        put(46, "key.c")
        put(58, "key.caps.lock")
        put(51, "key.comma")
        put(32, "key.d")
        put(211, "key.delete")
        put(208, "key.down")
        put(18, "key.e")
        put(207, "key.end")
        put(28, "key.enter")
        put(13, "key.equal")
        put(1, "key.escape")
        put(33, "key.f")
        put(59, "key.f1")
        put(68, "key.f10")
        put(87, "key.f11")
        put(88, "key.f12")
        put(100, "key.f13")
        put(101, "key.f14")
        put(102, "key.f15")
        put(103, "key.f16")
        put(104, "key.f17")
        put(105, "key.f18")
        put(113, "key.f19")
        put(60, "key.f2")
        put(61, "key.f3")
        put(62, "key.f4")
        put(63, "key.f5")
        put(64, "key.f6")
        put(65, "key.f7")
        put(66, "key.f8")
        put(67, "key.f9")
        put(34, "key.g")
        put(41, "key.grave.accent")
        put(35, "key.h")
        put(199, "key.home")
        put(23, "key.i")
        put(210, "key.insert")
        put(36, "key.j")
        put(37, "key.k")
        put(82, "key.keypad.0")
        put(79, "key.keypad.1")
        put(80, "key.keypad.2")
        put(81, "key.keypad.3")
        put(75, "key.keypad.4")
        put(76, "key.keypad.5")
        put(77, "key.keypad.6")
        put(71, "key.keypad.7")
        put(72, "key.keypad.8")
        put(73, "key.keypad.9")
        put(78, "key.keypad.add")
        put(83, "key.keypad.decimal")
        put(181, "key.keypad.divide")
        put(156, "key.keypad.enter")
        put(141, "key.keypad.equal")
        put(55, "key.keypad.multiply")
        put(74, "key.keypad.subtract")
        put(38, "key.l")
        put(203, "key.left")
        put(56, "key.left.alt")
        put(26, "key.left.bracket")
        put(29, "key.left.control")
        put(42, "key.left.shift")
        put(219, "key.left.win")
        put(50, "key.m")
        put(12, "key.minus")
        put(49, "key.n")
        put(69, "key.num.lock")
        put(24, "key.o")
        put(25, "key.p")
        put(209, "key.page.down")
        put(201, "key.page.up")
        put(197, "key.pause")
        put(52, "key.period")
        put(183, "key.print.screen")
        put(16, "key.q")
        put(19, "key.r")
        put(205, "key.right")
        put(184, "key.right.alt")
        put(27, "key.right.bracket")
        put(157, "key.right.control")
        put(54, "key.right.shift")
        put(220, "key.right.win")
        put(31, "key.s")
        put(70, "key.scroll.lock")
        put(39, "key.semicolon")
        put(53, "key.slash")
        put(57, "key.space")
        put(20, "key.t")
        put(15, "key.tab")
        put(22, "key.u")
        put(200, "key.up")
        put(47, "key.v")
        put(17, "key.w")
        put(45, "key.x")
        put(21, "key.y")
        put(44, "key.z")
    }

    fun register() = MCTypeRegistry.OPTIONS.addStructureConverter(VERSION) { data, _, _ ->
        data.keys().forEach {
            if (!it.startsWith("key_")) return@forEach
            val value = data.getString(it)
            val code = value?.toIntOrNull() ?: return@forEach

            val newEntry = if (code < 0) {
                when (val mouseCode = code + 100) {
                    0 -> "key.mouse.left"
                    1 -> "key.mouse.right"
                    2 -> "key.mouse.middle"
                    else -> "key.mouse.${mouseCode + 1}"
                }
            } else {
                BUTTON_ID_TO_NAME.getOrDefault(code, "key.unknown")
            }

            // no CMEs occur for existing entries in maps.
            data.setString(it, newEntry)
        }
        null
    }
}
