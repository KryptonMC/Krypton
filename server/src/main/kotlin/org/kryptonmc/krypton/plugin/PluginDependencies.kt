/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the Apache License v2.0
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/959e75d16db352924e679fb5be545ee9b264fbd2/proxy/src/main/java/com/velocitypowered/proxy/plugin/util/PluginDependencyUtils.java
 */
package org.kryptonmc.krypton.plugin

import com.google.common.collect.Maps
import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import org.kryptonmc.api.plugin.PluginDescription
import java.util.ArrayDeque
import java.util.Deque

object PluginDependencies {

    @JvmStatic
    fun sortCandidates(candidates: List<PluginDescription>): List<PluginDescription> {
        val sortedCandidates = candidates.sortedBy { it.id }
        val graph = GraphBuilder.directed().allowsSelfLoops(false).expectedNodeCount(sortedCandidates.size).build<PluginDescription>()
        val candidateMap = Maps.uniqueIndex(sortedCandidates) { it.id }

        sortedCandidates.forEach { description ->
            graph.addNode(description)
            description.dependencies.forEach { dependency ->
                candidateMap.get(dependency.id)?.let { graph.putEdge(description, it) }
            }
        }

        val sorted = ArrayList<PluginDescription>()
        val marks = HashMap<PluginDescription, Mark>()
        graph.nodes().forEach { visitNode(graph, it, marks, sorted, ArrayDeque()) }
        return sorted
    }

    @JvmStatic
    private fun visitNode(graph: Graph<PluginDescription>, current: PluginDescription, visited: MutableMap<PluginDescription, Mark>,
                          sorted: MutableList<PluginDescription>, currentDependencyScanStack: Deque<PluginDescription>) {
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
        visited.put(current, Mark.VISITING)
        graph.successors(current).forEach { visitNode(graph, it, visited, sorted, currentDependencyScanStack) }
        visited.put(current, Mark.VISITED)
        currentDependencyScanStack.removeLast()
        sorted.add(current)
    }

    private enum class Mark {

        NOT_VISITED,
        VISITING,
        VISITED
    }
}
