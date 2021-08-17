package org.kryptonmc.krypton

import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.kryptonmc.api.adventure.toComponent
import org.kryptonmc.api.adventure.toConfigurationNode
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.adventure.toLegacyText
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.adventure.toPlainText
import org.spongepowered.configurate.ConfigurationOptions
import org.spongepowered.configurate.gson.GsonConfigurationLoader
import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentTests {

    @Test
    fun `test component to message translation`() {
        val component = Component.text("Hello World!")
        val message = component.toMessage()
        assertEquals("Hello World!", message.string)
        assertEquals(component, message.wrapped)
    }

    @Test
    fun `test component conversions`() {
        val component = Component.text("Hello", NamedTextColor.GREEN)
            .append(Component.text(" World", NamedTextColor.BLUE))
            .append(Component.text("!", NamedTextColor.RED))
        val node = GsonConfigurationLoader.builder()
            .defaultOptions(ConfigurationOptions.defaults()
                .serializers(ConfigurateComponentSerializer.configurate().serializers()))
            .build().createNode()
            .set(component)
        val json = GsonComponentSerializer.gson().serializeToTree(component)
        assertEquals(json, component.toJson())
        assertEquals(GsonComponentSerializer.gson().serialize(component), component.toJsonString())
        assertEquals(PlainTextComponentSerializer.plainText().serialize(component), component.toPlainText())
        assertEquals(LegacyComponentSerializer.legacySection().serialize(component), component.toLegacyText(LegacyComponentSerializer.SECTION_CHAR))
        assertEquals(LegacyComponentSerializer.legacyAmpersand().serialize(component), component.toLegacyText(LegacyComponentSerializer.AMPERSAND_CHAR))
        assertEquals(ConfigurateComponentSerializer.configurate().serialize(component), component.toConfigurationNode())
        assertEquals(ConfigurateComponentSerializer.configurate().deserialize(node), node.toComponent())
        assertEquals(component, json.toComponent())
    }
}
