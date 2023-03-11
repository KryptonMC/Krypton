/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
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
 */
package org.kryptonmc.krypton.world.scoreboard

import com.google.common.collect.Multimaps
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.kryptonmc.api.scoreboard.DisplaySlot
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.api.scoreboard.criteria.Criterion
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutDisplayObjective
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateObjectives
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateScore
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTeams
import java.util.Collections
import java.util.EnumMap
import java.util.function.Consumer

class KryptonScoreboard(private val server: KryptonServer) : Scoreboard {

    private val objectivesByName = HashMap<String, Objective>()
    private val objectivesByCriterion = Multimaps.newSetMultimap<Criterion, Objective>(HashMap(), ::HashSet)
    private val memberScores = HashMap<Component, MutableMap<Objective, KryptonScore>>()
    private val displayObjectives = EnumMap<_, Objective>(DisplaySlot::class.java)
    private val teamsByName = HashMap<String, Team>()
    private val teamsByMember = HashMap<Component, Team>()

    private val trackedObjectives = HashSet<Objective>()
    private val listeners = ArrayList<Runnable>()

    override val objectives: Collection<Objective> = Collections.unmodifiableCollection(objectivesByName.values)
    override val teams: Collection<Team> = Collections.unmodifiableCollection(teamsByName.values)
    override val scores: Collection<KryptonScore>
        get() = memberScores.flatMap { it.value.values }

    fun displayObjectives(): Collection<Objective> = displayObjectives.values

    override fun getObjective(name: String): Objective? = objectivesByName.get(name)

    override fun createObjectiveBuilder(): Objective.Builder = KryptonObjective.Builder(this)

    override fun addObjective(name: String, criterion: Criterion, displayName: Component, renderType: ObjectiveRenderType): Objective {
        require(!objectivesByName.containsKey(name)) { "An objective with the name $name is already registered!" }
        val objective = KryptonObjective(this, name, criterion, displayName, renderType)
        objectivesByCriterion.put(criterion, objective)
        objectivesByName.put(name, objective)
        makeDirty()
        return objective
    }

    override fun removeObjective(objective: Objective) {
        objectivesByName.remove(objective.name)
        DISPLAY_SLOTS.forEach { if (displayObjectives.get(it) == objective) setDisplayObjective(it, null) }
        objectivesByCriterion.remove(objective.criterion, objective)
        memberScores.values.forEach { it.remove(objective) }
        onObjectiveRemoved(objective)
    }

    override fun getTeam(name: String): Team? = teamsByName.get(name)

    override fun getMemberTeam(member: Component): Team? = teamsByMember.get(member)

    override fun createTeamBuilder(name: String): Team.Builder = KryptonTeam.Builder(this, name)

    override fun addTeam(name: String): KryptonTeam {
        require(!teamsByName.containsKey(name)) { "A team with the name $name is already registered!" }
        return doAddTeam(name)
    }

    override fun getOrAddTeam(name: String): Team = getTeam(name) ?: doAddTeam(name)

    private fun doAddTeam(name: String): KryptonTeam {
        val team = KryptonTeam(this, name)
        teamsByName.put(name, team)
        onTeamAdded(team)
        return team
    }

    override fun removeTeam(team: Team) {
        teamsByName.remove(team.name)
        team.members.forEach(teamsByMember::remove)
        onTeamRemoved(team)
    }

    private fun startTrackingObjective(objective: Objective) {
        getStartTrackingPackets(objective).forEach(server.connectionManager::sendGroupedPacket)
        trackedObjectives.add(objective)
    }

    private fun stopTrackingObjective(objective: Objective) {
        getStopTrackingPackets(objective).forEach(server.connectionManager::sendGroupedPacket)
        trackedObjectives.remove(objective)
    }

    fun getStartTrackingPackets(objective: Objective): List<Packet> {
        val packets = ArrayList<Packet>()
        packets.add(PacketOutDisplayObjective.create(DisplaySlot.LIST, objective))
        displayObjectives.forEach { if (it.value === objective) packets.add(PacketOutDisplayObjective.create(it.key, it.value)) }
        getMemberScores(objective).forEach { packets.add(PacketOutUpdateScore.createOrUpdate(it)) }
        return packets
    }

    private fun getStopTrackingPackets(objective: Objective): List<Packet> {
        val packets = ArrayList<Packet>()
        packets.add(PacketOutDisplayObjective.create(DisplaySlot.SIDEBAR, objective))
        displayObjectives.forEach { if (it.value === objective) packets.add(PacketOutDisplayObjective.create(it.key, it.value)) }
        return packets
    }

    private fun getMemberScores(objective: Objective): Collection<KryptonScore> =
        memberScores.values.mapNotNullTo(ArrayList()) { it.get(objective) }.apply { sortWith(KryptonScore.COMPARATOR) }

    fun forEachObjective(criterion: Criterion, member: Component, action: Consumer<KryptonScore>) {
        objectivesByCriterion.get(criterion).forEach { action.accept(getOrCreateScore(member, it)) }
    }

    fun onEntityRemoved(entity: KryptonEntity?) {
        if (entity == null || entity is KryptonPlayer || entity.isAlive()) return
        resetScore(entity.teamRepresentation, null)
        removeMemberFromTeam(entity.teamRepresentation)
    }

    private fun getOrCreateScore(member: Component, objective: Objective): KryptonScore {
        val scores = memberScores.computeIfAbsent(member) { HashMap() }
        return scores.computeIfAbsent(objective) { KryptonScore(this, it, member).apply { score = 0 } }
    }

    private fun resetScore(member: Component, objective: Objective?) {
        if (objective == null) {
            val scores = memberScores.remove(member)
            if (scores != null) onMemberRemoved(member)
            return
        }
        val scores = memberScores.get(member) ?: return
        val score = scores.remove(objective)
        if (scores.isEmpty()) {
            memberScores.remove(member)?.let { onMemberRemoved(member) }
        } else if (score != null) {
            onMemberScoreRemoved(member, objective)
        }
    }

    fun addMemberToTeam(member: Component, team: Team): Boolean {
        if (tryAddMember(member, team)) {
            server.connectionManager.sendGroupedPacket(PacketOutUpdateTeams.addOrRemoveMember(team, member, true))
            makeDirty()
            return true
        }
        return false
    }

    private fun tryAddMember(member: Component, team: Team): Boolean {
        if (getMemberTeam(member) != null) removeMemberFromTeam(member)
        teamsByMember.put(member, team)
        return team.removeMember(member)
    }

    private fun removeMemberFromTeam(member: Component): Boolean {
        val team = getMemberTeam(member) ?: return false
        removeMemberFromTeam(member, team)
        return true
    }

    private fun removeMemberFromTeam(member: Component, team: Team) {
        check(getMemberTeam(member) === team) {
            "Cannot remove member ${PlainTextComponentSerializer.plainText().serialize(member)} from team ${team.name}! Member is not on the team!"
        }
        teamsByMember.remove(member)
        team.removeMember(member)
        server.connectionManager.sendGroupedPacket(PacketOutUpdateTeams.addOrRemoveMember(team, member, false))
        makeDirty()
    }

    override fun getObjective(slot: DisplaySlot): Objective? = displayObjectives.get(slot)

    override fun getObjectives(criterion: Criterion): Set<Objective> = objectivesByCriterion.get(criterion)

    override fun updateSlot(objective: Objective?, slot: DisplaySlot) {
        require(objective == null || objectivesByName.containsValue(objective)) {
            "Cannot set the display slot for an objective that is not registered to this scoreboard!"
        }
        setDisplayObjective(slot, objective)
    }

    private fun setDisplayObjective(slot: DisplaySlot, objective: Objective?) {
        val existing = displayObjectives.get(slot)
        if (objective != null) displayObjectives.put(slot, objective) else displayObjectives.remove(slot)
        if (existing !== objective && existing != null) {
            if (getObjectiveSlotCount(existing) > 0) {
                server.connectionManager.sendGroupedPacket(PacketOutDisplayObjective.create(slot, objective))
            } else {
                stopTrackingObjective(existing)
            }
        }
        if (objective != null) {
            if (trackedObjectives.contains(objective)) {
                server.connectionManager.sendGroupedPacket(PacketOutDisplayObjective.create(slot, objective))
            } else {
                startTrackingObjective(objective)
            }
        }
        makeDirty()
    }

    private fun getObjectiveSlotCount(objective: Objective): Int = displayObjectives.count { it.value === objective }

    override fun clearSlot(slot: DisplaySlot) {
        displayObjectives.remove(slot)
    }

    override fun getScores(name: Component): Set<KryptonScore> = memberScores.values.flatMapTo(HashSet()) { it.values }

    override fun removeScores(name: Component) {
        memberScores.forEach { entry -> if (entry.value.values.any { it.name == name }) memberScores.remove(entry.key) }
    }

    private fun makeDirty() {
        listeners.forEach(Runnable::run)
    }

    fun onObjectiveUpdated(objective: Objective) {
        if (trackedObjectives.contains(objective)) {
            server.connectionManager.sendGroupedPacket(PacketOutUpdateObjectives.updateText(objective))
        }
        makeDirty()
    }

    private fun onObjectiveRemoved(objective: Objective) {
        if (trackedObjectives.contains(objective)) stopTrackingObjective(objective)
        makeDirty()
    }

    fun onScoreUpdated(score: KryptonScore) {
        if (trackedObjectives.contains(score.objective)) {
            server.connectionManager.sendGroupedPacket(PacketOutUpdateScore.createOrUpdate(score))
        }
        makeDirty()
    }

    private fun onMemberRemoved(member: Component) {
        server.connectionManager.sendGroupedPacket(PacketOutUpdateScore.remove(member, null, 0))
        makeDirty()
    }

    private fun onMemberScoreRemoved(member: Component, objective: Objective) {
        if (trackedObjectives.contains(objective)) {
            server.connectionManager.sendGroupedPacket(PacketOutUpdateScore.remove(member, objective.name, 0))
        }
        makeDirty()
    }

    private fun onTeamAdded(team: Team) {
        server.connectionManager.sendGroupedPacket(PacketOutUpdateTeams.create(team))
        makeDirty()
    }

    fun onTeamUpdated(team: Team) {
        server.connectionManager.sendGroupedPacket(PacketOutUpdateTeams.update(team))
        makeDirty()
    }

    private fun onTeamRemoved(team: Team) {
        server.connectionManager.sendGroupedPacket(PacketOutUpdateTeams.remove(team))
        makeDirty()
    }

    companion object {

        private val DISPLAY_SLOTS = DisplaySlot.values()
    }
}
