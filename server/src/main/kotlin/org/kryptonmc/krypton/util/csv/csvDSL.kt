package org.kryptonmc.krypton.util.csv

import java.io.Writer

@DslMarker
annotation class CSVDSL

@CSVDSL
fun csv(writer: Writer, builder: CSVFile.Builder.() -> Unit) = CSVFile.Builder().apply(builder).build(writer)
