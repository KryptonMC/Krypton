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
package org.kryptonmc.codegen

import org.apache.logging.log4j.LogManager
import org.kryptonmc.codegen.blocks.BlockGenerator
import java.io.File
import java.io.FileInputStream

private val LOGGER = LogManager.getLogger("Main")

fun main(args: Array<String>) {
    if (args.size < 3) {
        LOGGER.error("Usage: <MC version> <source folder | 'resources'> <target folder>")
        return
    }
    val targetVersion = args[0].replace('.', '_')
    val resourceMode = args[1] == "resources"
    val inputFolder = File(args[1])
    val kotlinOutput = File(args[2], "kotlin")
    val javaOutput = File(args[2], "java")
    val classLoader = Thread.currentThread().contextClassLoader

    // Generate blocks
    BlockGenerator(
        if (resourceMode) classLoader.getResourceAsStream("${targetVersion}_blocks.json") else FileInputStream(File(inputFolder, "${targetVersion}_blocks.json")),
        if (resourceMode) classLoader.getResourceAsStream("${targetVersion}_block_properties.json") else FileInputStream(File(inputFolder, "${targetVersion}_block_properties.json")),
        kotlinOutput,
        javaOutput
    ).generate()

    LOGGER.info("Generators finished!")
}
