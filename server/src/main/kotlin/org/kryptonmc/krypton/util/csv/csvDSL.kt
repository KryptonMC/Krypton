package org.kryptonmc.krypton.util.csv

import java.io.Writer

@DslMarker
annotation class CSVDSL

@CSVDSL
fun csv(writer: Writer, builder: CSVOutput.Builder.() -> Unit) = CSVOutput.Builder().apply(builder).build(writer)