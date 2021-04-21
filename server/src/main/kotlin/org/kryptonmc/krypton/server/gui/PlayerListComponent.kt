package org.kryptonmc.krypton.server.gui

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import java.util.Vector
import javax.swing.JList

class PlayerListComponent(private val server: KryptonServer) : JList<String>() {

    private var tickCount = 0

    init {
        server.tickables.add(::tick)
    }

    private fun tick() {
        if (tickCount++ % 20 != 0) return
        setListData(server.players.map(KryptonPlayer::name).toVector())
    }
}

private fun <T> List<T>.toVector(): Vector<T> = Vector(this)