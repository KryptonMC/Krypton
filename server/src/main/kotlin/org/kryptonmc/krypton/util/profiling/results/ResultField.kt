package org.kryptonmc.krypton.util.profiling.results

class ResultField(
    val name: String,
    val percentage: Double,
    val globalPercentage: Double,
    val count: Long
) : Comparable<ResultField> {

    override fun compareTo(other: ResultField) =
        if (other.percentage < percentage) -1 else if (other.percentage > percentage) 1 else other.name.compareTo(name)
}