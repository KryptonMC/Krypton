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
package org.kryptonmc.datagen

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import net.minecraft.SharedConstants
import net.minecraft.server.Bootstrap
import org.apache.logging.log4j.LogManager
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

private val logger = LogManager.getLogger("Generator")
private val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

fun main() {
    val mainResources = Path.of("../../server/build/resources/main")
    val testResources = Path.of("../../server/build/resources/test")

    try {
        Files.createDirectories(mainResources)
        Files.createDirectories(testResources)
    } catch (exception: IOException) {
        throw ExceptionInInitializerError(exception)
    }

    SharedConstants.tryDetectVersion()
    Bootstrap.bootStrap()

    val generators = setOf<DataGenerator>(BlockDataGenerator)
    for (generator in generators) {
        generateData(generator, mainResources)
        generateData(generator, testResources)
    }
}

private fun generateData(generator: DataGenerator, output: Path) {
    logger.info("Running $generator")
    val data = generator.generate()
    val outputPath = output.resolve("1_19_3_${generator.name()}.json")
    writeJson(outputPath, data)
}

private fun writeJson(output: Path, data: JsonElement) {
    if (!Files.exists(output)) {
        try {
            Files.createFile(output)
        } catch (exception: IOException) {
            throw ExceptionInInitializerError(exception)
        }
    }

    try {
        Files.newBufferedWriter(output, Charsets.UTF_8).use { writer ->
            gson.toJson(data, writer)
        }
    } catch (exception: IOException) {
        logger.error("Something went wrong while writing data to $output!", exception)
    }
}
