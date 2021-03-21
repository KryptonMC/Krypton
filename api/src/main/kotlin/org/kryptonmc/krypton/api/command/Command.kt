package org.kryptonmc.krypton.api.command

abstract class Command(
    val name: String,
    val permission: String? = null,
    val aliases: List<String> = emptyList()
) {

    abstract suspend fun execute(sender: Sender, args: List<String>)

    fun suggest(sender: Sender): List<String> = emptyList()
}