package org.kryptonmc.krypton.world.data

interface WorldDataSerializer {

    fun load(name: String): WorldData?

    fun save(name: String, data: WorldData)
}
