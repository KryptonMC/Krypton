package org.kryptonmc.krypton.api.command

/**
 * Represents a command that may be executed, or provide suggestions to the user.
 *
 * @param name the name of the command
 * @param permission the permission required to execute the command (`null` for no permission)
 * @param aliases a list of aliases for this command
 * @author Callum Seabrook
 */
abstract class Command(
    val name: String,
    val permission: String? = null,
    val aliases: List<String> = emptyList()
) {

    /**
     * Called when this command is executed (when the [sender] runs the command)
     *
     * @param sender the sender who ran this command
     * @param args the arguments supplied to this command
     */
    abstract suspend fun execute(sender: Sender, args: List<String>)

    // TODO: Support command suggestions
    fun suggest(sender: Sender): List<String> = emptyList()
}