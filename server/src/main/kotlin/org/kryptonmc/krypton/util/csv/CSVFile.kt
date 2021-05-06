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
package org.kryptonmc.krypton.util.csv

import java.io.Writer

class CSVFile(private val output: Writer, headers: List<String>) {

    private val columns = headers.size

    fun writeRow(vararg rows: Any) {
        require(rows.size == columns) { "Invalid number of columns! Expected $columns, got ${rows.size}" }
        writeLines(rows.toList())
    }

    private fun writeLines(lines: List<*>) =
        output.write(lines.map(Any?::escapedString).joinToString(",") + "\r\n")

    class Builder {

        private val headers = mutableListOf<String>()

        operator fun plus(header: String): Builder {
            headers += header
            return this
        }

        fun build(writer: Writer) = CSVFile(writer, headers)
    }
}
