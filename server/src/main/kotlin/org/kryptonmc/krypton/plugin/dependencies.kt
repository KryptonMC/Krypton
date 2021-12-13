/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2018 Velocity Contributors
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
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/0097359a99c23de4fc6b92c59a401a10208b4c4a/proxy/src/main/java/com/velocitypowered/proxy/plugin/util/PluginDependencyUtils.java
 */
package org.kryptonmc.krypton.plugin

import com.google.common.collect.Maps
import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import org.kryptonmc.api.plugin.PluginDescription
import java.util.ArrayDeque
import java.util.Deque

fun List<PluginDescription>.sortCandidates(): List<PluginDescription> {
    val sortedCandidates = sortedBy { it.id }

    val graph = GraphBuilder.directed()
        .allowsSelfLoops(false)
        .expectedNodeCount(sortedCandidates.size)
        .build<PluginDescription>()
    val candidateMap: Map<String, PluginDescription> = Maps.uniqueIndex(sortedCandidates) { it?.id }

    sortedCandidates.forEach { description ->
        graph.addNode(description)
        description.dependencies.forEach {
            val candidate = candidateMap[it.id]
            if (candidate != null) graph.putEdge(description, candidate)
        }
    }

    val sorted = mutableListOf<PluginDescription>()
    val marks = mutableMapOf<PluginDescription, Mark>()
    graph.nodes().forEach { graph.visitNode(it, marks, sorted, ArrayDeque()) }
    return sorted
}

private fun Graph<PluginDescription>.visitNode(
    node: PluginDescription,
    marks: MutableMap<PluginDescription, Mark>,
    sorted: MutableList<PluginDescription>,
    currentIteration: Deque<PluginDescription>
) {
    val mark = marks.getOrDefault(node, Mark.NOT_VISITED)
    if (mark == Mark.PERMANENT) return
    if (mark == Mark.TEMPORARY) {
        currentIteration.addLast(node)
        val errorMessage = buildString {
            currentIteration.forEach { append("${it.id} -> ") }
            setLength(length - 4)
        }
        error("Circular dependency detected: $errorMessage")
    }

    currentIteration.addLast(node)
    marks[node] = mark
    successors(node).forEach { visitNode(it, marks, sorted, currentIteration) }
    marks[node] = Mark.PERMANENT
    currentIteration.removeLast()
    sorted.add(node)
}

private enum class Mark {

    NOT_VISITED,
    TEMPORARY,
    PERMANENT
}
