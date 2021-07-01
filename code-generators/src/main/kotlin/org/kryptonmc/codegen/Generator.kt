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

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.javapoet.JavaFile
import com.squareup.kotlinpoet.FileSpec
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.IOException

abstract class Generator {

    abstract fun generate()

    protected fun writeFiles(files: List<FileSpec>, output: File) = files.forEach {
        try {
            it.writeTo(output)
        } catch (exception: IOException) {
            LOGGER.error("An error occurred while writing source files to the file system!", exception)
        }
    }

    @JvmName("writeJavaFiles")
    protected fun writeFiles(files: List<JavaFile>, output: File) = files.forEach {
        try {
            it.writeTo(output)
        } catch (exception: IOException) {

        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger(Generator::class.java)
        @JvmStatic protected val GSON: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    }
}
