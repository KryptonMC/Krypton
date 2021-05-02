package org.kryptonmc.krypton.util.csv

import java.io.StringWriter
import java.io.Writer

class CSVOutput(private val output: Writer, headers: List<String>) {

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

        fun build(writer: Writer) = CSVOutput(writer, headers)
    }
}
