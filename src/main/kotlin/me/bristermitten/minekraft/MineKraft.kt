package me.bristermitten.minekraft

import net.minecraft.server.v1_15_R1.EnumProtocol
import net.minecraft.server.v1_15_R1.EnumProtocolDirection
import net.minecraft.server.v1_15_R1.MinecraftServer
import net.minecraft.server.v1_15_R1.PacketPlayOutLogin

fun main() {
    println(EnumProtocol.PLAY.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutLogin()))
    Server().start()
}
