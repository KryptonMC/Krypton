package org.kryptonmc.krypton.world.biome.gen

import kotlinx.collections.immutable.persistentListOf
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object Climate {

    private const val QUANTIZATION_FACTOR = 10000F
    private const val PARAMETER_COUNT = 7

    @JvmStatic
    fun target(
        temperature: Float,
        humidity: Float,
        continentalness: Float,
        erosion: Float,
        depth: Float,
        weirdness: Float
    ): TargetPoint = TargetPoint(
        quantize(temperature),
        quantize(humidity),
        quantize(continentalness),
        quantize(erosion),
        quantize(depth),
        quantize(weirdness)
    )

    @JvmStatic
    fun parameters(
        temperature: Float,
        humidity: Float,
        continentalness: Float,
        erosion: Float,
        depth: Float,
        weirdness: Float,
        offset: Float
    ): ParameterPoint = ParameterPoint(
        Parameter.point(temperature),
        Parameter.point(humidity),
        Parameter.point(continentalness),
        Parameter.point(erosion),
        Parameter.point(depth),
        Parameter.point(weirdness),
        quantize(offset)
    )

    @JvmStatic
    fun parameters(
        temperature: Parameter,
        humidity: Parameter,
        continentalness: Parameter,
        erosion: Parameter,
        depth: Parameter,
        weirdness: Parameter,
        offset: Float
    ): ParameterPoint = ParameterPoint(temperature, humidity, continentalness, erosion, depth, weirdness, quantize(offset))

    @JvmStatic
    fun quantize(value: Float): Long = (value * QUANTIZATION_FACTOR).toLong()

    @JvmStatic
    fun unquantize(value: Long): Float = value / QUANTIZATION_FACTOR

    @JvmStatic
    private fun square(value: Long): Long = value * value

    @JvmStatic
    private fun <T> toMutableList(list: List<T>): MutableList<T> {
        if (list is MutableList) return list
        return list.toMutableList()
    }

    fun interface Sampler {

        fun sample(x: Int, y: Int, z: Int): TargetPoint
    }

    @JvmRecord
    data class Parameter(val minimum: Long, val maximum: Long) {

        fun distance(value: Long): Long {
            val toMax = value - maximum
            val toMin = minimum - value
            if (toMax > 0L) return toMax
            return max(toMin, 0L)
        }

        fun distance(other: Parameter): Long {
            val toMax = other.minimum - maximum
            val toMin = minimum - other.maximum
            if (toMax > 0L) return toMax
            return max(toMin, 0L)
        }

        fun span(other: Parameter?): Parameter {
            if (other == null) return this
            return Parameter(min(minimum, other.minimum), max(maximum, other.maximum))
        }

        companion object {

            @JvmStatic
            fun point(value: Float): Parameter = span(value, value)

            @JvmStatic
            fun span(minimum: Float, maximum: Float): Parameter {
                require(minimum <= maximum) { "Minimum value cannot be greater than maximum! Minimum: $minimum, maximum: $maximum" }
                return Parameter(quantize(minimum), quantize(maximum))
            }

            @JvmStatic
            fun span(minimum: Parameter, maximum: Parameter): Parameter {
                require(minimum.minimum <= maximum.maximum) {
                    "Minimum value cannot be greater than maximum! Minimum: ${minimum.minimum}, maximum: ${maximum.maximum}"
                }
                return Parameter(minimum.minimum, maximum.maximum)
            }
        }
    }

    class ParameterList<T>(val pairs: List<Pair<ParameterPoint, T>>) {

        private val index = RTree.create(pairs)

        fun find(target: TargetPoint): T = index.search(target, RTree.Node<T>::distance)
    }

    @JvmRecord
    data class ParameterPoint(
        val temperature: Parameter,
        val humidity: Parameter,
        val continentalness: Parameter,
        val erosion: Parameter,
        val depth: Parameter,
        val weirdness: Parameter,
        val offset: Long
    ) {

        val parameters: List<Parameter>
            get() = persistentListOf(temperature, humidity, continentalness, erosion, depth, weirdness, Parameter(offset, offset))

        fun fitness(target: TargetPoint): Long {
            return square(temperature.distance(target.temperature)) +
                    square(humidity.distance(target.humidity)) +
                    square(continentalness.distance(target.continentalness)) +
                    square(erosion.distance(target.erosion)) +
                    square(depth.distance(target.depth)) +
                    square(weirdness.distance(target.weirdness)) +
                    square(offset)
        }
    }

    @JvmRecord
    data class TargetPoint(
        val temperature: Long,
        val humidity: Long,
        val continentalness: Long,
        val erosion: Long,
        val depth: Long,
        val weirdness: Long
    ) {

        fun toArray(): LongArray = longArrayOf(temperature, humidity, continentalness, erosion, depth, weirdness, 0L)
    }

    private class RTree<T>(private val root: Node<T>) {

        private val lastResult = ThreadLocal<Leaf<T>>()

        fun search(target: TargetPoint, metric: DistanceMetric<T>): T {
            val parameters = target.toArray()
            val last = root.search(parameters, lastResult.get(), metric)
            lastResult.set(last)
            return last.value
        }

        sealed class Node<T>(parameters: List<Parameter>) {

            val parameters: Array<Parameter> = parameters.toTypedArray()

            abstract fun search(values: LongArray, previous: Leaf<T>?, metric: DistanceMetric<T>): Leaf<T>

            fun distance(values: LongArray): Long {
                var total = 0L
                for (i in 0 until PARAMETER_COUNT) {
                    total += square(parameters[i].distance(values[i]))
                }
                return total
            }
        }

        class Leaf<T>(point: ParameterPoint, val value: T) : Node<T>(point.parameters) {

            override fun search(values: LongArray, previous: Leaf<T>?, metric: DistanceMetric<T>): Leaf<T> = this
        }

        class Branch<T>(val children: List<Node<T>>, parameters: List<Parameter>) : Node<T>(parameters) {

            constructor(children: List<Node<T>>) : this(children, createParameters(children))

            override fun search(values: LongArray, previous: Leaf<T>?, metric: DistanceMetric<T>): Leaf<T> {
                var lowestCost = if (previous != null) metric.distance(previous, values) else Long.MAX_VALUE
                var result = previous
                children.forEach {
                    val distance = metric.distance(it, values)
                    if (lowestCost <= distance) return@forEach
                    val current = it.search(values, result, metric)
                    val otherDistance = if (it == current) distance else metric.distance(current, values)
                    if (lowestCost <= otherDistance) return@forEach
                    lowestCost = otherDistance
                    result = current
                }
                return result!!
            }
        }

        companion object {

            @JvmStatic
            fun <T> create(values: List<Pair<ParameterPoint, T>>): RTree<T> {
                require(values.isNotEmpty()) { "At least one value is required to build the search tree!" }
                val parameterSize = values[0].first.parameters.size
                require(parameterSize == PARAMETER_COUNT) { "Expected parameter count of $PARAMETER_COUNT, was $parameterSize!" }
                return RTree(build(parameterSize, values.mapTo(mutableListOf()) { Leaf(it.first, it.second) }))
            }

            @JvmStatic
            @Suppress("UNCHECKED_CAST")
            private fun <T> createParameters(children: List<Node<T>>): List<Parameter> {
                require(children.isNotEmpty()) { "Branch must have at least one child!" }
                val result = persistentListOf<Parameter?>().builder()
                for (i in 0 until PARAMETER_COUNT) {
                    result.add(null)
                }
                children.forEach {
                    for (i in 0 until PARAMETER_COUNT) {
                        result[i] = it.parameters[i].span(result[i])
                    }
                }
                return result.build() as List<Parameter>
            }

            @JvmStatic
            private fun cost(parameters: Array<Parameter>): Long {
                var cost = 0L
                parameters.forEach { cost += abs(it.maximum - it.minimum) }
                return cost
            }

            @JvmStatic
            private fun <T> group(nodes: List<Node<T>>): MutableList<Branch<T>> {
                val branches = persistentListOf<Branch<T>>().builder()
                var temporaryChildren = mutableListOf<Node<T>>()
                val branchSize = 10.0.pow(floor(ln(nodes.size - 0.01) / ln(10.0)))
                nodes.forEach {
                    temporaryChildren.add(it)
                    if (temporaryChildren.size >= branchSize) {
                        branches.add(Branch(temporaryChildren))
                        temporaryChildren = mutableListOf()
                    }
                }
                if (temporaryChildren.isNotEmpty()) branches.add(Branch(temporaryChildren))
                return branches
            }

            @JvmStatic
            private fun <T> comparator(index: Int, absolute: Boolean): Comparator<Node<T>> = Comparator.comparingLong {
                val parameter = it.parameters[index]
                val value = (parameter.minimum + parameter.maximum) / 2L
                if (absolute) abs(value) else value
            }

            @JvmStatic
            private fun <T> sort(nodes: MutableList<out Node<T>>, limit: Int, index: Int, absolute: Boolean) {
                var comparator = comparator<T>(index, absolute)
                for (i in 1 until limit) {
                    comparator = comparator.thenComparing(comparator((index + i) % limit, absolute))
                }
                nodes.sortWith(comparator)
            }

            @JvmStatic
            private fun <T> build(limit: Int, nodes: MutableList<Node<T>>): Node<T> {
                check(nodes.isNotEmpty()) { "At least one child is required to build a node!" }
                if (nodes.size == 1) return nodes[0]
                if (nodes.size <= 10) {
                    nodes.sortWith(Comparator.comparingLong {
                        var total = 0L
                        for (i in 0 until limit) {
                            val parameter = it.parameters[i]
                            total += abs((parameter.minimum + parameter.maximum) / 2L)
                        }
                        total
                    })
                    return Branch(nodes)
                }
                var lowestCost = Long.MAX_VALUE
                var lowestCostIndex = -1
                var lowestCostBranches: MutableList<Branch<T>>? = null

                for (i in 0 until limit) {
                    sort(nodes, limit, i, false)
                    val branches = group(nodes)
                    var cost = 0L
                    branches.forEach { cost += cost(it.parameters) }
                    if (lowestCost > cost) {
                        lowestCost = cost
                        lowestCostIndex = i
                        lowestCostBranches = branches
                    }
                }

                sort(lowestCostBranches!!, limit, lowestCostIndex, true)
                return Branch(lowestCostBranches.map { build(limit, toMutableList(it.children)) })
            }
        }
    }

    private fun interface DistanceMetric<T> {

        fun distance(node: RTree.Node<T>, values: LongArray): Long
    }
}
