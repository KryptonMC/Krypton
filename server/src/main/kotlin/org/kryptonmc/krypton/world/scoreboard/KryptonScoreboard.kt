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
package org.kryptonmc.krypton.world.scoreboard

import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.api.scoreboard.DisplaySlot
import org.kryptonmc.api.scoreboard.criteria.Criterion
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutDisplayObjective
import org.kryptonmc.krypton.packet.out.play.PacketOutObjective
import org.kryptonmc.krypton.packet.out.play.PacketOutTeam
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateScore
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class KryptonScoreboard(private val server: KryptonServer) : Scoreboard {

    private val objectivesByName = ConcurrentHashMap<String, Objective>()
    private val objectivesByCriterion = ConcurrentHashMap<Criterion, MutableSet<Objective>>()
    private val trackedObjectives = ConcurrentHashMap.newKeySet<Objective>()
    private val memberScores = ConcurrentHashMap<Component, MutableMap<Objective, KryptonScore>>()
    val displayObjectives = ConcurrentHashMap<DisplaySlot, Objective>(18)
    private val teamsByName = ConcurrentHashMap<String, Team>()
    private val teamsByMember = ConcurrentHashMap<Component, Team>()
    private val listeners = Collections.synchronizedList(ArrayList<Runnable>())

    override val objectives: Collection<Objective>
        get() = objectivesByName.values
    override val teams: Collection<Team>
        get() = teamsByName.values
    override val scores: Collection<KryptonScore>
        get() = memberScores.flatMap { it.value.values }

    constructor(builder: Builder) : this(builder.server) {
        builder.objectives.forEach {
            if (it is KryptonObjective) it.scoreboard = this
            addObjective(it)
        }
        builder.teams.forEach {
            if (it is KryptonTeam) it.scoreboard = this
            addTeam(it)
        }
    }

    private fun startTrackingObjective(objective: Objective) {
        val packets = getStartTrackingPackets(objective)
        server.playerManager.players.forEach { player -> packets.forEach { player.session.send(it) } }
        trackedObjectives.add(objective)
    }

    private fun stopTrackingObjective(objective: Objective) {
        val packets = getStopTrackingPackets(objective)
        server.playerManager.players.forEach { player -> packets.forEach { player.session.send(it) } }
        trackedObjectives.remove(objective)
    }

    fun getStartTrackingPackets(objective: Objective): List<Packet> = mutableListOf<Packet>().apply {
        add(PacketOutDisplayObjective(0, objective))
        displayObjectives.forEach { if (it.value === objective) add(PacketOutDisplayObjective(it.key, it.value)) }
        scores(objective).forEach { add(PacketOutUpdateScore(PacketOutUpdateScore.Action.CREATE_OR_UPDATE, it)) }
    }

    private fun getStopTrackingPackets(objective: Objective): List<Packet> = mutableListOf<Packet>().apply {
        add(PacketOutDisplayObjective(1, objective))
        displayObjectives.forEach { if (it.value === objective) add(PacketOutDisplayObjective(it.key, it.value)) }
    }

    private fun scores(objective: Objective): Collection<KryptonScore> = memberScores.values
        .mapNotNullTo(mutableListOf()) { it[objective] }
        .apply { sortWith(KryptonScore.COMPARATOR) }

    fun addListener(listener: Runnable) {
        listeners.add(listener)
    }

    fun forEachObjective(criterion: Criterion, member: Component, action: (KryptonScore) -> Unit) {
        objectivesByCriterion.getOrDefault(criterion, emptyList()).forEach { action(getOrCreateScore(member, it)) }
    }

    fun removeEntity(entity: KryptonEntity) {
        if (entity is KryptonPlayer || entity.isAlive) return
        resetScore(entity.teamRepresentation, null)
        removeMemberFromTeam(entity.teamRepresentation)
    }

    fun hasObjective(name: String): Boolean = objectivesByName.containsKey(name)

    fun hasScore(member: Component, objective: Objective): Boolean {
        val scores = memberScores[member] ?: return false
        return scores.containsKey(objective)
    }

    private fun getOrCreateScore(member: Component, objective: Objective): KryptonScore {
        val scores = memberScores.computeIfAbsent(member) { mutableMapOf() }
        return scores.computeIfAbsent(objective) { KryptonScore(this, it, member).apply { score = 0 } }
    }

    private fun resetScore(member: Component, objective: Objective?) {
        if (objective == null) {
            val scores = memberScores.remove(member)
            if (scores != null) onMemberRemoved(member)
            return
        }
        val scores = memberScores[member] ?: return
        val score = scores.remove(objective)
        if (scores.isEmpty()) {
            val removed = memberScores.remove(member)
            if (removed != null) onMemberRemoved(member)
        } else if (score != null) {
            onMemberScoreRemoved(member, objective)
        }
    }

    fun addMemberToTeam(member: Component, team: Team): Boolean {
        if (memberTeam(member) != null) removeMemberFromTeam(member)
        teamsByMember[member] = team
        if (team.members.contains(member)) return false
        team.addMember(member)
        return true
    }

    private fun removeMemberFromTeam(member: Component): Boolean {
        val team = memberTeam(member) ?: return false
        removeMemberFromTeam(member, team)
        return true
    }

    private fun removeMemberFromTeam(member: Component, team: Team) {
        check(memberTeam(member) === team) {
            "Cannot remove member ${member.toPlainText()} from team ${team.name}! Member is not on the team!"
        }
        teamsByMember.remove(member)
        team.removeMember(member)
    }

    override fun objective(name: String): Objective? = objectivesByName[name]

    override fun objective(slot: DisplaySlot): Objective? = displayObjectives[slot]

    override fun objectives(criterion: Criterion): Set<Objective> = objectivesByCriterion[criterion] ?: emptySet()

    override fun addObjective(objective: Objective) {
        require(!objectivesByName.containsKey(objective.name)) { "An objective with the name ${objective.name} is already registered!" }
        objectivesByCriterion.computeIfAbsent(objective.criterion) { mutableSetOf() }.add(objective)
        objectivesByName[objective.name] = objective
        makeDirty()
    }

    override fun removeObjective(objective: Objective) {
        objectivesByName.remove(objective.name)
        displayObjectives.forEach { if (it.value === objective) displayObjectives.remove(it.key) }
        objectivesByCriterion[objective.criterion]?.remove(objective)
        memberScores.values.forEach { it.remove(objective) }
        onObjectiveRemoved(objective)
    }

    @Suppress("ReplaceSizeCheckWithIsNotEmpty") // No it can't, IntelliJ
    override fun updateSlot(objective: Objective?, slot: DisplaySlot) {
        require(objective == null || objectivesByName.containsValue(objective)) {
            "Cannot set the display slot for an objective that is not registered to this scoreboard!"
        }
        val old = displayObjectives[slot]
        if (objective == null) displayObjectives.remove(slot) else displayObjectives[slot] = objective
        if (old !== objective && old != null) {
            if (displayObjectives.count { it.value === objective } > 0) {
                server.playerManager.sendToAll(PacketOutDisplayObjective(slot, objective))
            } else {
                stopTrackingObjective(old)
            }
        }
        if (objective != null) {
            if (trackedObjectives.contains(objective)) {
                server.playerManager.sendToAll(PacketOutDisplayObjective(slot, objective))
            } else {
                startTrackingObjective(objective)
            }
        }
        makeDirty()
    }

    override fun clearSlot(slot: DisplaySlot) {
        displayObjectives.remove(slot)
    }

    override fun scores(name: Component): Set<KryptonScore> = memberScores.values.flatMapTo(mutableSetOf()) { it.values }

    override fun removeScores(name: Component) {
        memberScores.forEach { entry -> if (entry.value.values.any { it.name == name }) memberScores.remove(entry.key) }
    }

    override fun team(name: String): Team? = teamsByName[name]

    override fun memberTeam(member: Component): Team? = teamsByMember[member]

    override fun addTeam(team: Team) {
        require(!teamsByName.containsKey(team.name)) { "A team with the name ${team.name} is already registered!" }
        teamsByName[team.name] = team
        team.members.forEach { teamsByMember[it] = team }
        onTeamAdded(team)
    }

    override fun removeTeam(team: Team) {
        teamsByName.remove(team.name)
        team.members.forEach { teamsByMember.remove(it) }
        onTeamRemoved(team)
    }

    override fun toBuilder(): Scoreboard.Builder = Builder(this)

    private fun makeDirty() {
        listeners.forEach { it.run() }
    }

    fun onObjectiveUpdated(objective: Objective) {
        if (trackedObjectives.contains(objective)) {
            server.playerManager.sendToAll(PacketOutObjective(PacketOutObjective.Action.UPDATE_TEXT, objective))
        }
        makeDirty()
    }

    private fun onObjectiveRemoved(objective: Objective) {
        if (trackedObjectives.contains(objective)) stopTrackingObjective(objective)
        makeDirty()
    }

    fun onScoreUpdated(score: KryptonScore) {
        if (trackedObjectives.contains(score.objective)) {
            server.playerManager.sendToAll(PacketOutUpdateScore(PacketOutUpdateScore.Action.CREATE_OR_UPDATE, score))
        }
        makeDirty()
    }

    private fun onMemberRemoved(member: Component) {
        server.playerManager.sendToAll(PacketOutUpdateScore(PacketOutUpdateScore.Action.REMOVE, member, null, 0))
        makeDirty()
    }

    private fun onMemberScoreRemoved(member: Component, objective: Objective) {
        if (trackedObjectives.contains(objective)) {
            server.playerManager.sendToAll(PacketOutUpdateScore(PacketOutUpdateScore.Action.REMOVE, member, objective.name, 0))
        }
        makeDirty()
    }

    private fun onTeamAdded(team: Team) {
        server.playerManager.sendToAll(PacketOutTeam(PacketOutTeam.Action.CREATE, team))
        makeDirty()
    }

    fun onTeamUpdated(team: Team) {
        server.playerManager.sendToAll(PacketOutTeam(PacketOutTeam.Action.UPDATE_INFO, team))
        makeDirty()
    }

    private fun onTeamRemoved(team: Team) {
        server.playerManager.sendToAll(PacketOutTeam(PacketOutTeam.Action.REMOVE, team))
        makeDirty()
    }

    class Builder(val server: KryptonServer) : Scoreboard.Builder {

        val objectives = mutableListOf<Objective>()
        val teams = mutableListOf<Team>()

        constructor(scoreboard: KryptonScoreboard) : this(scoreboard.server) {
            objectives.addAll(scoreboard.objectives)
            teams.addAll(scoreboard.teams)
        }

        override fun objective(objective: Objective): Scoreboard.Builder = apply { objectives.add(objective) }

        override fun objectives(vararg objectives: Objective): Scoreboard.Builder = apply { objectives.forEach { this.objectives.add(it) } }

        override fun objectives(objectives: Iterable<Objective>): Scoreboard.Builder = apply { this.objectives.addAll(objectives) }

        override fun team(team: Team): Scoreboard.Builder = apply { teams.add(team) }

        override fun teams(vararg teams: Team): Scoreboard.Builder = apply { teams.forEach { this.teams.add(it) } }

        override fun teams(teams: Iterable<Team>): Scoreboard.Builder = apply { this.teams.addAll(teams) }

        override fun build(): Scoreboard = KryptonScoreboard(this)
    }

    class Factory(private val server: KryptonServer) : Scoreboard.Factory {

        override fun empty(): Scoreboard = KryptonScoreboard(server)

        override fun builder(): Scoreboard.Builder = Builder(server)
    }
}
