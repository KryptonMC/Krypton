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

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.kryptonmc.api.scoreboard.DisplaySlot
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.api.scoreboard.Score
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.api.scoreboard.criteria.Criterion
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.PacketGrouping
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
    private val displayObjectives = EnumMap<_, Objective>(DisplaySlot::class.java)
    private val teamsByName = HashMap<String, Team>()
    private val teamsByMember = HashMap<Component, Team>()

    private val trackedObjectives = HashSet<Objective>()
    private val listeners = ArrayList<Runnable>()

    override val objectives: Collection<Objective>
        get() = Collections.unmodifiableCollection(objectivesByName.values)
    override val teams: Collection<Team>
        get() = Collections.unmodifiableCollection(teamsByName.values)

    fun displayObjectives(): Collection<Objective> = displayObjectives.values

    override fun getObjective(name: String): Objective? = objectivesByName.get(name)

    override fun createObjectiveBuilder(): Objective.Builder = KryptonObjective.Builder(this)

    override fun addObjective(name: String, criterion: Criterion, displayName: Component, renderType: ObjectiveRenderType): Objective {
        require(!objectivesByName.containsKey(name)) { "An objective with the name $name is already registered!" }
        val objective = KryptonObjective(this, name, criterion, displayName, renderType)
        objectivesByName.put(name, objective)
        makeDirty()
        return objective
    }

    override fun removeObjective(objective: Objective) {
        objectivesByName.remove(objective.name)
        DISPLAY_SLOTS.forEach { if (displayObjectives.get(it) == objective) setDisplayObjective(it, null) }
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
        for (packet in getStartTrackingPackets(objective)) {
            PacketGrouping.sendGroupedPacket(server, packet)
        }
        trackedObjectives.add(objective)
    }

    private fun stopTrackingObjective(objective: Objective) {
        for (packet in getStopTrackingPackets(objective)) {
            PacketGrouping.sendGroupedPacket(server, packet)
        }
        trackedObjectives.remove(objective)
    }

    fun getStartTrackingPackets(objective: Objective): List<Packet> {
        val packets = ArrayList<Packet>()
        packets.add(PacketOutUpdateObjectives.create(objective))
        displayObjectives.forEach { if (it.value === objective) packets.add(PacketOutDisplayObjective.create(it.key, it.value)) }
        objective.scores.forEach { packets.add(PacketOutUpdateScore.createOrUpdate(it)) }
        return packets
    }

    private fun getStopTrackingPackets(objective: Objective): List<Packet> {
        val packets = ArrayList<Packet>()
        packets.add(PacketOutUpdateObjectives.remove(objective))
        displayObjectives.forEach { if (it.value === objective) packets.add(PacketOutDisplayObjective.create(it.key, it.value)) }
        return packets
    }

    fun forEachScore(criterion: Criterion, member: Component, action: Consumer<Score>) {
        objectivesByName.values.forEach { objective ->
            if (objective.criterion == criterion) action.accept(objective.getOrCreateScore(member))
        }
    }

    fun onEntityRemoved(entity: KryptonEntity?) {
        if (entity == null || entity is KryptonPlayer || entity.isAlive()) return
        val memberName = entity.teamRepresentation
        onMemberRemoved(memberName)
        objectivesByName.values.forEach { resetScore(it, memberName) }
        removeMemberFromTeam(memberName)
    }

    private fun resetScore(objective: Objective, member: Component) {
        if (objective.getScore(member) == null) return
        onMemberScoreRemoved(objective, member)
    }

    fun addMemberToTeam(member: Component, team: Team): Boolean {
        if (tryAddMember(member, team)) {
            PacketGrouping.sendGroupedPacket(server, PacketOutUpdateTeams.addOrRemoveMember(team, member, true))
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
        PacketGrouping.sendGroupedPacket(server, PacketOutUpdateTeams.addOrRemoveMember(team, member, false))
        makeDirty()
    }

    override fun getObjective(slot: DisplaySlot): Objective? = displayObjectives.get(slot)

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
                PacketGrouping.sendGroupedPacket(server, PacketOutDisplayObjective.create(slot, objective))
            } else {
                stopTrackingObjective(existing)
            }
        }
        if (objective != null) {
            if (trackedObjectives.contains(objective)) {
                PacketGrouping.sendGroupedPacket(server, PacketOutDisplayObjective.create(slot, objective))
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

    private fun makeDirty() {
        listeners.forEach(Runnable::run)
    }

    fun onObjectiveUpdated(objective: Objective) {
        if (trackedObjectives.contains(objective)) {
            PacketGrouping.sendGroupedPacket(server, PacketOutUpdateObjectives.updateText(objective))
        }
        makeDirty()
    }

    private fun onObjectiveRemoved(objective: Objective) {
        if (trackedObjectives.contains(objective)) stopTrackingObjective(objective)
        makeDirty()
    }

    fun onScoreUpdated(score: KryptonScore) {
        if (trackedObjectives.contains(score.objective)) {
            PacketGrouping.sendGroupedPacket(server, PacketOutUpdateScore.createOrUpdate(score))
        }
        makeDirty()
    }

    private fun onMemberRemoved(member: Component) {
        PacketGrouping.sendGroupedPacket(server, PacketOutUpdateScore.remove(member, null, 0))
        makeDirty()
    }

    private fun onMemberScoreRemoved(objective: Objective, member: Component) {
        if (trackedObjectives.contains(objective)) {
            PacketGrouping.sendGroupedPacket(server, PacketOutUpdateScore.remove(member, objective.name, 0))
        }
        makeDirty()
    }

    private fun onTeamAdded(team: Team) {
        PacketGrouping.sendGroupedPacket(server, PacketOutUpdateTeams.create(team))
        makeDirty()
    }

    fun onTeamUpdated(team: Team) {
        PacketGrouping.sendGroupedPacket(server, PacketOutUpdateTeams.update(team))
        makeDirty()
    }

    private fun onTeamRemoved(team: Team) {
        PacketGrouping.sendGroupedPacket(server, PacketOutUpdateTeams.remove(team))
        makeDirty()
    }

    companion object {

        private val DISPLAY_SLOTS = DisplaySlot.values()
    }
}
