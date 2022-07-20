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
 * https://github.com/PaperMC/Velocity/blob/e8bf6ab5222d8bbf4e59e7c4d5400b10ab4c7e81/proxy/src/main/java/com/velocitypowered/proxy/plugin/util/PluginDependencyUtils.java
 */
package org.kryptonmc.krypton.plugin

import com.google.common.collect.Maps
import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import org.kryptonmc.api.plugin.PluginDescription
import java.util.ArrayDeque
import java.util.Deque

fun List<PluginDescription>.sortCandidates(): List<PluginDescription> {
    val sortedCandidates = sortedBy(PluginDescription::id)
    val graph = GraphBuilder.directed().allowsSelfLoops(false).expectedNodeCount(sortedCandidates.size).build<PluginDescription>()
    val candidateMap = Maps.uniqueIndex(sortedCandidates, PluginDescription::id)

    sortedCandidates.forEach { description ->
        graph.addNode(description)
        description.dependencies.forEach {
            val candidate = candidateMap[it.id]
            if (candidate != null) graph.putEdge(description, candidate)
        }
    }

    val sorted = ArrayList<PluginDescription>()
    val marks = HashMap<PluginDescription, Mark>()
    graph.nodes().forEach { graph.visitNode(it, marks, sorted, ArrayDeque()) }
    return sorted
}

private fun Graph<PluginDescription>.visitNode(
    current: PluginDescription,
    visited: MutableMap<PluginDescription, Mark>,
    sorted: MutableList<PluginDescription>,
    currentDependencyScanStack: Deque<PluginDescription>
) {
    val mark = visited.getOrDefault(current, Mark.NOT_VISITED)
    if (mark == Mark.VISITED) return // Already visited this node, nothing to do
    if (mark == Mark.VISITING) {
        // A circular dependency has been detected. (Specifically, if we are visiting any dependency and a dependency we are
        // looking at depends on any dependency being visited, we have a circular dependency, thus we do not have a directed
        // acyclic graph and therefore no topological sort is possible).
        currentDependencyScanStack.addLast(current)
        error("Circular dependency detected: ${currentDependencyScanStack.joinToString(" -> ") { it.id }}")
    }

    currentDependencyScanStack.addLast(current)
    visited[current] = Mark.VISITING
    successors(current).forEach { visitNode(it, visited, sorted, currentDependencyScanStack) }
    visited[current] = Mark.VISITED
    currentDependencyScanStack.removeLast()
    sorted.add(current)
}

private enum class Mark {

    NOT_VISITED,
    VISITING,
    VISITED
}
