package me.bristermitten.minekraft.packet.data

data class HandshakeData(
	var protocol: Int = 0,
	var address: String = "",
	var port: Short = 0,
	var nextState: Int = -1
)

