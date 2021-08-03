package org.kryptonmc.krypton.command.arguments.itemstack

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.util.toComponent
import org.kryptonmc.nbt.CompoundTag

class ItemStackParser(val reader: StringReader, val allowTags: Boolean) { //TODO: Tags for ItemStackPredicate etc.

    val ID_INVALID_EXCEPTION = DynamicCommandExceptionType {e ->
        translatable(
            "argument.item.id.invalid",
            listOf(e.toString().toComponent())
        ).toMessage()
    }
    val TAG_DISALLOWED_EXCEPTION = SimpleCommandExceptionType(translatable("argument.item.tag.disallowed").toMessage())


    fun readItem(reader: StringReader) : ItemType {
        val i = reader.cursor

        while (reader.canRead() && isCharValid(reader.peek())) {
            reader.skip()
        }

        val string = reader.string.substring(i, reader.cursor)
        val item = Registries.ITEM[key(string)] //TODO: Check if exist
        return item
    }

    fun parse() = ItemStackArgument(readItem(this.reader))

    fun isCharValid(c: Char): Boolean {
        return c in '0'..'9' || c in 'a'..'z' || c == '_' || c == ':' || c == '/' || c == '.' || c == '-'
    }

}
