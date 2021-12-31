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
package org.kryptonmc.krypton.world.biome

import com.google.common.collect.ImmutableList
import org.spongepowered.math.vector.Vector3i
import java.util.function.Supplier
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object Climate {

    private const val QUANTIZATION_FACTOR = 10000F

    @JvmStatic
    fun Float.quantize(): Long = (this * QUANTIZATION_FACTOR).toLong()

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
        Parameter.value(temperature),
        Parameter.value(humidity),
        Parameter.value(continentalness),
        Parameter.value(erosion),
        Parameter.value(depth),
        Parameter.value(weirdness),
        offset.quantize()
    )

    fun interface DistanceMetric<T> {

        fun distance(node: RTree.Node<T>, parameters: LongArray): Long
    }

    data class TargetPoint(
        val temperature: Long,
        val humidity: Long,
        val continentalness: Long,
        val erosion: Long,
        val depth: Long,
        val weirdness: Long
    ) {

        val parameters: LongArray = longArrayOf(temperature, humidity, continentalness, erosion, depth, weirdness)

        companion object {

            @JvmField
            val ZERO: TargetPoint = TargetPoint(0, 0, 0, 0, 0, 0)
        }
    }

    class ParameterList<T>(val biomes: List<Pair<ParameterPoint, Supplier<T>>>) {

        private val index = RTree.create(biomes)

        fun findBiome(target: TargetPoint, biome: Supplier<T>): T = findBiomeIndex(target)

        private fun findBiomeIndex(target: TargetPoint): T = index.search(target) { node, parameters -> node.distance(parameters) }
    }

    data class ParameterPoint(
        val temperature: Parameter,
        val humidity: Parameter,
        val continentalness: Parameter,
        val erosion: Parameter,
        val depth: Parameter,
        val weirdness: Parameter,
        val offset: Long,
    ) {

        private val parameterSpace: List<Parameter> = ImmutableList.of(
            temperature,
            humidity,
            continentalness,
            erosion,
            depth,
            weirdness,
            Parameter(offset, offset)
        )
        val parameterSpaceArray: Array<Parameter> = parameterSpace.toTypedArray()

        companion object {

            @JvmField
            val ZERO: ParameterPoint = ParameterPoint(
                Parameter.ZERO,
                Parameter.ZERO,
                Parameter.ZERO,
                Parameter.ZERO,
                Parameter.ZERO,
                Parameter.ZERO,
                0
            )
        }
    }

    @JvmRecord
    data class Parameter(
        val minimum: Long,
        val maximum: Long
    ) {

        fun distance(point: Long): Long = distance(point, point)

        fun distance(parameter: Parameter): Long = distance(parameter.minimum, parameter.maximum)

        private fun distance(min: Long, max: Long): Long {
            val maxDiff = min - maximum
            val minDiff = minimum - max
            return if (maxDiff > 0L) maxDiff else max(minDiff, 0L)
        }

        fun range(parameter: Parameter?): Parameter {
            if (parameter == null) return this
            return Parameter(min(minimum, parameter.minimum), max(maximum, parameter.maximum))
        }

        operator fun rangeTo(other: Parameter): Parameter = range(this, other)

        override fun toString(): String {
            if (minimum == maximum) return "$minimum"
            return "[$minimum-$maximum]"
        }

        companion object {

            @JvmField
            val ZERO: Parameter = value(0F)
            @JvmField
            val ONE: Parameter = value(1F)

            @JvmStatic
            fun range(minimum: Float, maximum: Float): Parameter {
                require(minimum <= maximum) { "Minimum value cannot be greater than maximum! $minimum > $maximum" }
                return Parameter(minimum.quantize(), maximum.quantize())
            }

            @JvmStatic
            fun range(minimum: Parameter, maximum: Parameter): Parameter {
                require(minimum.minimum <= maximum.maximum) {
                    "Minimum value cannot be greater than maximum! ${minimum.minimum} > ${maximum.maximum}"
                }
                return Parameter(minimum.minimum, maximum.maximum)
            }

            @JvmStatic
            fun value(value: Float): Parameter = Parameter(value.quantize(), value.quantize())
        }
    }

    class RTree<T>(private val root: Node<T>) {

        private val lastResult = ThreadLocal<Leaf<T>>()

        fun search(target: TargetPoint, metric: DistanceMetric<T>): T {
            val parameters = target.parameters
            val result = root.search(parameters, lastResult.get(), metric)
            lastResult.set(result)
            return result.biome.get()
        }

        abstract class Node<T>(val parameterSpace: Array<Parameter>) {

            abstract fun search(parameters: LongArray, lastResult: Leaf<T>?, metric: DistanceMetric<T>): Leaf<T>

            fun distance(parameters: LongArray): Long {
                var total = 0L
                for (i in 0..6) {
                    val distance = parameterSpace[i].distance(parameters[i])
                    total += distance * distance
                }
                return total
            }

            override fun toString(): String = parameterSpace.contentToString()
        }

        class Leaf<T>(point: ParameterPoint, val biome: Supplier<T>) : Node<T>(point.parameterSpaceArray) {

            override fun search(parameters: LongArray, lastResult: Leaf<T>?, metric: DistanceMetric<T>): Leaf<T> = this
        }

        class SubTree<T>(parameterSpace: Array<Parameter>, val children: Array<Node<T>>) : Node<T>(parameterSpace) {

            constructor(children: List<Node<T>>) : this(buildParameterSpace(children), children.toTypedArray())

            override fun search(parameters: LongArray, lastResult: Leaf<T>?, metric: DistanceMetric<T>): Leaf<T> {
                var currentDistance = if (lastResult == null) Long.MAX_VALUE else metric.distance(lastResult, parameters)
                var last = lastResult
                children.forEach {
                    val distance = metric.distance(it, parameters)
                    if (currentDistance <= distance) return@forEach
                    val found = it.search(parameters, last, metric)
                    val foundDistance = if (it === found) distance else metric.distance(found, parameters)
                    if (currentDistance <= foundDistance) return@forEach
                    currentDistance = foundDistance
                    last = found
                }
                return last!!
            }
        }

        companion object {

            @JvmStatic
            fun <T> create(biomes: List<Pair<ParameterPoint, Supplier<T>>>): RTree<T> {
                require(biomes.isNotEmpty()) { "At least one biome must be present to build the search tree!" }
                val size = biomes[0].first.parameterSpaceArray.size
                check(size == 7) { "Expecting parameter space to be size 7, was $size!" }
                val nodes = biomes.mapTo(mutableListOf()) { Leaf(it.first, it.second) }
                return RTree(build(size, nodes))
            }

            @JvmStatic
            private fun <T> build(size: Int, nodes: MutableList<out Node<T>>): Node<T> {
                require(nodes.isNotEmpty()) { "A node requires at least one child node!" }
                if (nodes.size == 1) return nodes[0]
                if (nodes.size <= 10) {
                    nodes.sortBy {
                        var total = 0L
                        for (i in 0 until size) {
                            val parameter = it.parameterSpace[i]
                            total += abs((parameter.minimum + parameter.maximum) / 2L)
                        }
                        total
                    }
                    return SubTree(nodes)
                }

                var lowestCost = Long.MAX_VALUE
                var index = -1
                var trees: MutableList<SubTree<T>>? = null
                for (i in 0 until size) {
                    sort(nodes, size, i, false)
                    val treeSplit = splitIntoTrees(nodes)
                    val cost = treeSplit.sumOf { cost(it.parameterSpace) }
                    if (lowestCost > cost) {
                        lowestCost = cost
                        index = i
                        trees = treeSplit
                    }
                }

                sort(trees!!, size, index, true)
                return SubTree(trees.map { build(size, it.children.toMutableList()) })
            }

            @JvmStatic
            private fun <T> sort(nodes: MutableList<out Node<T>>, size: Int, index: Int, final: Boolean) {
                var comparator = comparator<T>(index, final)
                for (i in 1 until size) {
                    comparator = comparator.thenComparing(comparator((index + i) % size, final))
                }
                nodes.sortWith(comparator)
            }

            @JvmStatic
            private fun <T> comparator(index: Int, final: Boolean): Comparator<Node<T>> = Comparator.comparingLong {
                val parameter = it.parameterSpace[index]
                val value = (parameter.minimum + parameter.maximum) / 2L
                if (final) abs(value) else value
            }

            @JvmStatic
            private fun <T> splitIntoTrees(allNodes: List<Node<T>>): MutableList<SubTree<T>> {
                val trees = mutableListOf<SubTree<T>>()
                var currentNodes = mutableListOf<Node<T>>()
                val value = 10.0.pow(floor(ln(allNodes.size - 0.1) / ln(10.0)))

                allNodes.forEach {
                    currentNodes.add(it)
                    if (currentNodes.size >= value) {
                        trees.add(SubTree(currentNodes))
                        currentNodes = mutableListOf()
                    }
                }
                if (currentNodes.isNotEmpty()) trees.add(SubTree(currentNodes))
                return trees
            }

            @JvmStatic
            private fun cost(parameters: Array<Parameter>): Long = parameters.sumOf { abs(it.maximum - it.minimum) }

            @Suppress("UNCHECKED_CAST")
            @JvmStatic
            private fun <T> buildParameterSpace(children: List<Node<T>>): Array<Parameter> {
                require(children.isNotEmpty()) { "Sub trees must have at least one child!" }
                val parameters = arrayOfNulls<Parameter>(7)
                children.forEach {
                    for (i in 0..6) {
                        parameters[i] = it.parameterSpace[i].range(parameters[i])
                    }
                }
                return parameters as Array<Parameter>
            }
        }
    }

    fun interface Sampler {

        fun sample(x: Int, y: Int, z: Int): TargetPoint

        fun findSpawnPosition(): Vector3i = Vector3i.ZERO
    }
}
